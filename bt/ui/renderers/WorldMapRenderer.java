package bt.ui.renderers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;

import bt.elements.galaxy.Settlement;
import bt.elements.galaxy.SolarSystemDetails;
import bt.mapping.TerrainType;
import bt.mapping.planet.PlanetMap;
import bt.mapping.planet.PlanetMapSector;
import bt.mapping.planet.WorldMapNavigation;
import bt.util.Hexagon;

/**
 * Title:        Ant
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 * @author
 * @version 1.0
 */

public class WorldMapRenderer
{
    private WorldSectorRenderer[] m_WorldSectors;
    private WorldMapNavigation m_HexMap;
    private Polygon[] m_SegmentBoundaries;
    private Polygon m_ClipRegion;
    private Image m_Image;
    private int m_MapWidth;
    private int m_MapHeight;

    private final int BaseXOffset = 4;
    private final int BaseYOffset = 4;

    private SolarSystemDetails m_Details;
    private PlanetMap m_Map;
    
    public enum DrawMode
    {
    	MAP,
    	ELEVATION,
    	TEMPERATURE,
    	WIND;
    }

    public WorldMapRenderer(SolarSystemDetails details,int hexWidth)
    {
        m_Details = details;
        m_Map = details.getMap();
        m_HexMap = new WorldMapNavigation();
        SetWorldSize(m_Details.getSize(),hexWidth);
    }

    public WorldMapRenderer(PlanetMap map, int size, int hexWidth)
    {
        m_Details = null;
        m_Map = map;
        m_HexMap = new WorldMapNavigation();
        SetWorldSize(size,hexWidth);
    }
    
    protected void SetWorldSize(int Size, int HexWidth)
    {
        try
        {
            InitialiseMap(Size,HexWidth);
        } catch (Exception e)
        {
    	    m_MapWidth = 100;
    	    m_MapHeight = 100;
        	e.printStackTrace();
        }
    }

    public Dimension getMapSize()
    {
        return new Dimension(m_MapWidth,m_MapHeight);
    }


    public int GetSelectedSegment(int x, int y)
    {
        int Segment = 0;
        for ( ; Segment < 10; Segment++ )
        {
            if (m_SegmentBoundaries[Segment].contains(x,y))
            return Segment+1;
        }

        return -1; //None found;
    }

    public int GetSelectedSector(int x, int y)
    {
        int Segment = 0;
        for ( ; Segment < 10; Segment++ )
        {
            if (m_SegmentBoundaries[Segment].contains(x,y))
                break;
        }
        int Sector;
        if (Segment < 10)
        {
            Sector = Segment * m_HexMap.getSegmentSize();
            int SegmentEnd = ((Segment + 1) * m_HexMap.getSegmentSize());
            for ( ; Sector < SegmentEnd; Sector++)
            {
                if (m_WorldSectors[Sector].contains(x,y))
                    return Sector+1;
            }
        }
        //Ok, we haven't found any via segment, try whole map
        for (Sector = 0; Sector < m_WorldSectors.length; Sector++)
        {
            if (m_WorldSectors[Sector].contains(x,y))
                return Sector+1;
        }

        return -1; //None found;
    }

    public void HighlightSegment(Graphics comp, int Segment, boolean Highlight)
    {
        // Segment passed into this routine needs to be 1 based
        if (Segment < 1 || Segment > 10)
            return;

        if (Highlight)
            comp.setColor( Color.red );

        comp.drawPolygon(m_SegmentBoundaries[Segment-1]);

        comp.setColor( Color.black );
    }

    public void HighlightSector(Graphics2D comp, int Sector, boolean Highlight, boolean ShowHexNeighbours)
    {
        comp.setClip(m_ClipRegion);
        if (Sector < 1 || Sector > m_WorldSectors.length)
            return;


        if (Highlight)
            comp.setColor( Color.pink );

        if (ShowHexNeighbours)
        {
            for (int dir = 1; dir < 7; dir++)
            {
                int Sect = m_HexMap.getHexNeighbour(Sector, dir);
                m_WorldSectors[Sect - 1].draw(comp);
                m_WorldSectors[Sect - 1].fill(comp);
            }
        }

        if (Highlight)
            comp.setColor( Color.red );

        m_WorldSectors[Sector-1].draw(comp);


        comp.setColor( Color.black );
    }

    public void HighlightPath(Graphics2D comp, int StartSector, int EndSector, boolean Highlight, boolean ShowHexNeighbours)
    {
        int[] Path = m_HexMap.getSectorPath(StartSector,EndSector);
        HighlightPath(comp,Path,Highlight,ShowHexNeighbours);
    }

    public void HighlightPath(Graphics2D comp, int[] Path, boolean Highlight, boolean ShowHexNeighbours)
    {
        if (Path != null)
        {
            for (int i = 0; i < Path.length; i++)
                HighlightSector(comp, Path[i], Highlight, ShowHexNeighbours);
        }
    }


    protected int GetHexWidth(int MapPixelWidth, int NumXHexes)
    {
        return MapPixelWidth / NumXHexes;
    }

    protected int GetHexHeight(int HexPixelWidth)
    {
        Hexagon aHex = new Hexagon(0,0,HexPixelWidth,true);
        return aHex.getHeight();
    }

    protected void InitialiseMap( int Size, int HexWidth )
    {

		Hexagon aHex = new Hexagon(0,0,HexWidth,true);
		int HexX = aHex.getXIncrement();
		int HexY = aHex.getYIncrement();
	
		m_HexMap.initialiseMap(Size);
	
	    m_WorldSectors = new WorldSectorRenderer[(Size * Size * 10) + 2];
	
	    m_MapWidth = (HexX * (Size * 12)) + (BaseXOffset * 2);
	    m_MapHeight = (BaseYOffset * 2) + (m_HexMap.getMapRowHeight() * (HexY + 1));
	
		int CurHex = 1;
		m_WorldSectors[CurHex-1] = new WorldSectorRenderer(BaseXOffset + (Size * HexX),BaseYOffset,HexWidth,m_Map.getPlanetSector(CurHex),m_HexMap);
		Point Offset = new Point(0,0);
		for (int i = 0; i < 4; i++)
		{
	            Offset.x += Size * HexX * 2;
	            Point addOff = new Point(Offset);
	            m_WorldSectors[CurHex-1].addOffset(addOff);
		}
		CurHex++;
		for (int Segment = 0; Segment < 10; Segment++)
		{
			boolean TopSeg = (Segment % 2) == 0;
			Offset = new Point(BaseXOffset + (Size * HexX) - HexX,BaseYOffset + HexY);
			Offset.x += Segment * Size * HexX;
			if (!TopSeg)
	                    Offset.y += Size * HexY;
	
			Point HexOff = new Point(Offset);
			for (int Row = 1; Row <= m_HexMap.getSegmentRows(); Row++)
			{
	                    int Start = m_HexMap.getRowStarts(Row);
	                    int End = m_HexMap.getRowEnds(Row);
	                    int Cur = Start;
	                    Point OrigOff = new Point(HexOff);
	                    while (Cur <= End)
	                    {
	                        int NewOff = 0;
	                        m_WorldSectors[CurHex-1] = new WorldSectorRenderer(HexOff.x,HexOff.y,HexWidth,m_Map.getPlanetSector(CurHex),m_HexMap);
	                        if (TopSeg && (Cur == Start) && (Row < Size))
	                        {
	                            NewOff = 0 - (((Size - Row) * 2) * HexX);
	                            if (Segment == 0)
	                                NewOff += 10 * Size * HexX;
	                            m_WorldSectors[CurHex-1].addOffset(NewOff,0);
	                        }
	                        if ((Segment==0) && (Cur==Start) && (Row >= Size))
	                        {
	                            NewOff = Size * 10 * HexX;
	                            m_WorldSectors[CurHex-1].addOffset(NewOff,0);
	                        }
	                        if ((!TopSeg) && (Cur==Start) && (Row >= Size))
	                        {
	                            NewOff = 0 - (((Row - Size) * 2) * HexX);
	                            if (Segment == 1)
	                                NewOff += 10 * Size * HexX;
	                            m_WorldSectors[CurHex-1].addOffset(NewOff,0);
	                        }
	
	                        HexOff.x += HexX * 2;
	
	                        Cur++;
	                        CurHex++;
	                    }
	                    HexOff = OrigOff;
	                    if (Row < Size)
	                    {
	                        HexOff.x -= HexX;
	                        HexOff.y += HexY;
	                    }
	                    else
	                    {
	                        HexOff.x += HexX;
	                        HexOff.y += HexY;
	                    }
			}
		}
		m_WorldSectors[CurHex-1] = new WorldSectorRenderer(BaseXOffset + ((Size * 2) * HexX),BaseYOffset + (m_HexMap.getMapRowHeight() * HexY) - HexY,HexWidth,m_Map.getPlanetSector(CurHex),m_HexMap);
		Offset = new Point(0,0);
		for (int i = 0; i < 4; i++)
		{
	            Offset.x += Size * HexX * 2;
	            Point addOff = new Point(Offset);
	            m_WorldSectors[CurHex-1].addOffset(addOff);
		}
	
	        m_SegmentBoundaries = new Polygon[10];
		Point[] Points = new Point[5];
		for (int i = 0; i < 10; i++)
		{
	            m_SegmentBoundaries[i] = new Polygon();
	            GetSegmentBoundary(i,Points);
	            for (int j = 0; j < 5; j++)
	                m_SegmentBoundaries[i].addPoint(Points[j].x,Points[j].y);
		}
    }

    protected void GetSegmentBoundary(int Segment, Point[] Points)
    {
        int WorldSize = m_HexMap.getMapFaceLength();
        int SegmentSize = m_HexMap.getSegmentSize();
	int[] Hexes = new int[4];
	if (Segment % 2 == 0)
	{	//TopSegment
            Hexes[0] = 1;
            if (Segment == 0)
                Points[0] = m_WorldSectors[Hexes[0]-1].getCenter();
            else
                Points[0] = m_WorldSectors[Hexes[0]-1].getOffsetHex((Segment / 2)-1).getCenter();

            Hexes[1] = m_HexMap.getRowStarts(WorldSize) + ((Segment + 2) * SegmentSize);
            if (Hexes[1] > m_WorldSectors.length)
            {
                Hexes[1] -= SegmentSize * 10;
                Points[1] = m_WorldSectors[Hexes[1]-1].getOffsetHex(0).getCenter();
            }
            else
                Points[1] = m_WorldSectors[Hexes[1]-1].getCenter();

            Hexes[2] = m_HexMap.getRowStarts(WorldSize) + ((Segment + 1) * SegmentSize);
            if (Hexes[2] > m_WorldSectors.length)
            {
                Hexes[2] -= SegmentSize * 10;
                Points[2] = m_WorldSectors[Hexes[2]-1].getOffsetHex(0).getCenter();
            }
            else
                Points[2] = m_WorldSectors[Hexes[2]-1].getCenter();

            Hexes[3] = m_HexMap.getRowStarts(WorldSize) + (Segment * SegmentSize);
            Points[3] = m_WorldSectors[Hexes[3]-1].getCenter();

            Points[4] = Points[0];
	}
	else
	{
            Hexes[0] = m_HexMap.getRowStarts(WorldSize) + ((Segment + 1) * SegmentSize);
            if (Hexes[0] > m_WorldSectors.length)
            {
                Hexes[0] -= SegmentSize * 10;
                Points[0] = m_WorldSectors[Hexes[0]-1].getOffsetHex(0).getCenter();
            }
            else
                Points[0] = m_WorldSectors[Hexes[0]-1].getCenter();

            Hexes[1] = m_HexMap.getRowStarts(WorldSize) + ((Segment + 2) * SegmentSize);
            if (Hexes[1] > m_WorldSectors.length)
            {
                Hexes[1] -= SegmentSize * 10;
                Points[1] = m_WorldSectors[Hexes[1]-1].getOffsetHex(0).getCenter();
            }
            else
                Points[1] = m_WorldSectors[Hexes[1]-1].getCenter();

            Hexes[2] = m_WorldSectors.length;
            if (Segment == 1)
                Points[2] = m_WorldSectors[Hexes[2]-1].getCenter();
            else
                Points[2] = m_WorldSectors[Hexes[2]-1].getOffsetHex(((Segment-1) / 2)-1).getCenter();

            Hexes[3] = m_HexMap.getRowStarts(WorldSize) + (Segment * SegmentSize);
            Points[3] = m_WorldSectors[Hexes[3]-1].getCenter();

            Points[4] = Points[0];
	}
    }

    protected void setWorldClip(Graphics2D comp)
    {
        m_ClipRegion = new Polygon();
        m_ClipRegion.addPoint(m_SegmentBoundaries[0].xpoints[0],m_SegmentBoundaries[0].ypoints[0]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[0].xpoints[1],m_SegmentBoundaries[0].ypoints[1]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[2].xpoints[0],m_SegmentBoundaries[2].ypoints[0]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[2].xpoints[1],m_SegmentBoundaries[2].ypoints[1]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[4].xpoints[0],m_SegmentBoundaries[4].ypoints[0]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[4].xpoints[1],m_SegmentBoundaries[4].ypoints[1]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[6].xpoints[0],m_SegmentBoundaries[6].ypoints[0]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[6].xpoints[1],m_SegmentBoundaries[6].ypoints[1]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[8].xpoints[0],m_SegmentBoundaries[8].ypoints[0]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[8].xpoints[1],m_SegmentBoundaries[8].ypoints[1]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[9].xpoints[1],m_SegmentBoundaries[9].ypoints[1]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[9].xpoints[2],m_SegmentBoundaries[9].ypoints[2]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[9].xpoints[3],m_SegmentBoundaries[9].ypoints[3]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[7].xpoints[2],m_SegmentBoundaries[7].ypoints[2]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[7].xpoints[3],m_SegmentBoundaries[7].ypoints[3]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[5].xpoints[2],m_SegmentBoundaries[5].ypoints[2]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[5].xpoints[3],m_SegmentBoundaries[5].ypoints[3]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[3].xpoints[2],m_SegmentBoundaries[3].ypoints[2]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[3].xpoints[3],m_SegmentBoundaries[3].ypoints[3]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[1].xpoints[2],m_SegmentBoundaries[1].ypoints[2]);
        m_ClipRegion.addPoint(m_SegmentBoundaries[0].xpoints[3],m_SegmentBoundaries[0].ypoints[3]);

        comp.clip(m_ClipRegion);
    }

    public void draw(Graphics2D comp, int XOff, int YOff, DrawMode mode)
    {
        comp.setPaintMode();
		comp.setColor(Color.black);		
        
    	Shape CurClip = new Rectangle(0, 0, m_MapWidth, m_MapHeight);
        comp.setClip(CurClip);

        drawMap(comp, CurClip, mode);
    }

    protected void drawMap(Graphics2D comp2D, Shape CurClip, DrawMode mode)
    {
        Point[] Points = new Point[5];


        Color bg = comp2D.getColor();
        comp2D.setColor(Color.gray);

        comp2D.fill(CurClip);
        comp2D.setColor(bg);

        setWorldClip(comp2D);

        switch (mode)
        {
        	case MAP:
		        for (int i = 0; i < m_WorldSectors.length; i++)
		            m_WorldSectors[i].draw(comp2D);
		
		        for (int i = 0; i < m_WorldSectors.length; i++)
		            m_WorldSectors[i].drawRoads(comp2D);		        

		        if (m_Details != null)
		        {
			        for (int i = 0; i < m_Details.getSettlementCount(); i++)
			        {
			        	Settlement s = m_Details.getSettlement(i);
			        	WorldSectorRenderer ws = m_WorldSectors[s.getLocation() - 1];
			        	ws.drawSettlement(comp2D,s.getType());
			        }
		        }
		        
		        for (int i = 0; i < m_WorldSectors.length; i++)
		            m_WorldSectors[i].drawRivers(comp2D);		        
		        break;		        
        	case ELEVATION:
		        for (int i = 0; i < m_WorldSectors.length; i++)
		            m_WorldSectors[i].drawElevation(comp2D);
        		break;
        	case TEMPERATURE:
		        for (int i = 0; i < m_WorldSectors.length; i++)
		            m_WorldSectors[i].drawTemperature(comp2D);
        		break;
        	case WIND:
        		break;
        		
        }

        comp2D.setClip(CurClip);

        for (int i = 0; i < 10; i++)
            comp2D.drawPolygon(m_SegmentBoundaries[i]);


        GetSegmentBoundary(0,Points);
        Point p1 = Points[3];
        GetSegmentBoundary(8,Points);
        Point p2 = Points[1];
        comp2D.drawLine(p1.x,p1.y,p2.x,p2.y);

        GetSegmentBoundary(1,Points);
        p1 = Points[3];
        GetSegmentBoundary(9,Points);
        p2 = Points[1];
        comp2D.drawLine(p1.x,p1.y,p2.x,p2.y);
    }

    public void setSectorTerrainType(int Sector, TerrainType Terrain)
    {
        if (Sector < 1 || Sector > m_WorldSectors.length)
            return;

        PlanetMapSector wsd = m_Map.getPlanetSector(Sector);
        if (wsd != null)
        {
            wsd.setTerrainType(Terrain);
            updateSectorInImage(Sector);
        }
    }

    public Rectangle getSectorBounds(int Sector)
    {
        if (Sector < 1 || Sector > m_WorldSectors.length)
            return new Rectangle(0,0,0,0);

        return m_WorldSectors[Sector-1].getBounds();
    }

    public void updateSectorInImage(int Sector)
    {
        if (Sector < 1 || Sector > m_WorldSectors.length)
            return;

        Graphics2D comp2D = (Graphics2D)m_Image.getGraphics();
        Shape CurClip = new Rectangle(0,0,m_MapWidth,m_MapHeight);
        comp2D.setClip(CurClip);
        setWorldClip(comp2D);
        m_WorldSectors[Sector-1].draw(comp2D);
        comp2D.setClip(CurClip);
    }


    public String GetSectorAsString(int Sector)
    {
        String info = Integer.toString(Sector) + " (";
        for (int i = 1; i < 7; i++)
        {
            info += Integer.toString(m_HexMap.getHexNeighbour(Sector,i));
            if (i < 6) info += ",";
        }
        info += ")";
        return info;
    }

    public void Redraw()
    {
        m_Image = null;
    }

    public int[] GetSectorPath(int StartSector, int EndSector)
    {
        return m_HexMap.getSectorPath(StartSector,EndSector);
    }

}
