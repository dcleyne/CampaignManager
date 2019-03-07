/**
 * Created on 07/06/2007
 * <p>Title: Legatus</p>
 *
 * <p>Copyright: Copyright Daniel Cleyne (c) 2004-2007</p>
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
package bt.mapping.planet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.HashMap;
import java.util.Vector;


public class PlanetHexGrid
{
	protected final int BaseXOffset = 4;
	protected final int BaseYOffset = 4;
	
	public enum PlanetArea
	{
		NORTH_POLE,
		AREA_1,
		AREA_2,
		AREA_3,
		AREA_4,
		AREA_5,
		AREA_6,
		AREA_7,
		AREA_8,
		AREA_9,
		AREA_10,
		EQUATOR,
		SOUTH_POLE;
		
		private static String[] Names = {"Area 1", "Area 2", "Area 3", "Area 4", "Area 5," +
				"Area 6", "Area 7", "Area 8", "Area 9", "Area 10", "Equator"};
		
		public String toString()
		{
			return Names[ordinal()];
		}
	}

	private int m_PlanetSize = 5;
	private int m_MapRowHeight = 0;
	private int m_SegmentSize = 0;
	private Sector[] m_Sectors;
	private int m_MapWidth;
	private int m_MapHeight;
	private int[] m_RowStarts;
	private int[] m_RowEnds;
	
	private Polygon m_MapBoundary = null;
	
	private HashMap<PlanetArea,Vector<Sector>> m_SectorMap = new HashMap<PlanetArea,Vector<Sector>>();
	
	
	protected Polygon m_ClipRegion;


	public PlanetHexGrid(int size)
	{
		m_PlanetSize = size;
		m_MapRowHeight = (m_PlanetSize * 2) + 1;
		m_SegmentSize = factorial(size);
		
		for (PlanetArea area: PlanetArea.values())
			m_SectorMap.put(area, new Vector<Sector>());		
		
		initialiseRows();
		initialiseMap(size);
		calculateHexNeighbours(size);
	}

	protected int getRowStarts(int Row)
	{
		return m_RowStarts[Row-1];
	}

	protected int getRowEnds(int Row)
	{
		return m_RowEnds[Row-1];
	}
	
	public Dimension getSize()
	{ return new Dimension(m_MapWidth,m_MapHeight); }

	protected void initialiseRows( )
	{
		m_RowStarts = new int[m_MapRowHeight];
		m_RowEnds = new int[m_MapRowHeight];

		int RowLen = 1;
		int CurEnd = 2;
		for (int i = 0; i < m_MapRowHeight; i++)
		{
			m_RowEnds[i] = CurEnd;
			m_RowStarts[i] = m_RowEnds[i] - RowLen + 1;

			if (i < m_PlanetSize - 1)
				RowLen++;
			else
				RowLen--;

			CurEnd += RowLen;
		}
	}


	public int factorial(int num)
	{
		int total = 0;
		while (num > 0)
		{
			total += num;
			num--;
		}
		return total;
	}

	protected void initialiseMap(int Size)
	{
		int HexX = PlanetHexagon.getXIncrement();
		int HexY = PlanetHexagon.getYIncrement();

		int equatorSize = (Size * 5) + 2;
		m_Sectors = new Sector[(m_SegmentSize * 10) + equatorSize + 2];

		m_MapWidth = (HexX * ((Size * 11) + 2)) + (BaseXOffset * 2);
		m_MapHeight = (BaseYOffset * 2) + ((m_MapRowHeight + 2) * (HexY + 1));

		int CurHex = 1;
		PlanetHexagon ph = new PlanetHexagon(BaseXOffset + ((Size + 2) * HexX),BaseYOffset);
		m_Sectors[CurHex-1] = new Sector(PlanetArea.NORTH_POLE,CurHex - 1,true,ph,false);
		m_SectorMap.get(PlanetArea.NORTH_POLE).add(m_Sectors[CurHex-1]);
		Point Offset = new Point(0,0);
		for (int i = 0; i < 4; i++)
		{
			Offset.x += Size * (HexX * 2);
			m_Sectors[CurHex-1].getHex().addOffset(Offset.x,0);
		}
		CurHex++;
		
		//Now do the northern hemisphere segments
		for (int Segment = 0; Segment < 5; Segment++)
		{
			Offset = new Point(BaseXOffset + ((Size + 2) * HexX) - HexX,BaseYOffset + HexY);
			Offset.x += Segment * Size * (HexX * 2);

			Point HexOff = new Point(Offset);
			for (int Row = 1; Row <= Size; Row++)
			{
				int Start = getRowStarts(Row);
				int End = getRowEnds(Row);
				int Cur = Start;
				Point OrigOff = new Point(HexOff);
				while (Cur <= End)
				{
					int NewOff = 0;
					ph = new PlanetHexagon(HexOff.x,HexOff.y);
					boolean tropic = (((Row / (Size / 3)) == 1) || (Row == Size));
					m_Sectors[CurHex - 1] = new Sector(PlanetArea.values()[Segment + 1],CurHex - 1,true,ph,tropic);
					m_SectorMap.get(PlanetArea.values()[Segment + 1]).add(m_Sectors[CurHex-1]);
					if ((Cur == Start) && (Row < Size))
					{
						NewOff = 0 - (((Size - Row) * 2) * HexX);
						if (Segment == 0)
							NewOff += 10 * Size * HexX;
						m_Sectors[CurHex-1].getHex().addOffset(NewOff,0);
					}
                    if (Segment == 0 && (Cur==Start) && (Row == Size))
                    {
                    	NewOff = 0 - (((Row - Size) * 2) * HexX);
                        NewOff += 10 * Size * HexX;
                        m_Sectors[CurHex-1].getHex().addOffset(NewOff,0);
                    }
					HexOff.x += HexX * 2;

					Cur++;
					CurHex++;
				}
				HexOff = OrigOff;
				HexOff.x -= HexX;
				HexOff.y += HexY;
			}
		}
		//Now do the equator segments
		Offset = new Point(BaseXOffset + (2 * HexX) - HexX,BaseYOffset + (Size * HexY) + HexY);
		ph = new PlanetHexagon(Offset.x,Offset.y);
			ph.addOffset((Size * HexX * 10), 0);
		m_Sectors[CurHex - 1] = new Sector(PlanetArea.EQUATOR,CurHex - 1,false,ph,true);
		m_SectorMap.get(PlanetArea.EQUATOR).add(m_Sectors[CurHex-1]);
		CurHex++;
		Offset.x += (HexX * 2);
		
		for (int Segment = 1; Segment < (Size * 5); Segment++)
		{
			ph = new PlanetHexagon(Offset.x,Offset.y);
			m_Sectors[CurHex - 1] = new Sector(PlanetArea.EQUATOR,CurHex - 1,false,ph,true);
			m_SectorMap.get(PlanetArea.EQUATOR).add(m_Sectors[CurHex-1]);
			Offset.x += (HexX * 2);
			CurHex++;
		}
		

		//Now do the southern hemisphere segments
		for (int Segment = 0; Segment < 5; Segment++)
		{
			Offset = new Point(BaseXOffset + (2 * HexX),BaseYOffset + ((Size + 1) * HexY) + HexY);
			Offset.x += Segment * Size * (HexX * 2);

			Point HexOff = new Point(Offset);
			for (int Row = Size; Row > 0; Row--)
			{
				int Start = getRowStarts(Row);
				int End = getRowEnds(Row);
				int Cur = Start;
				Point OrigOff = new Point(HexOff);
				while (Cur <= End)
				{
					int NewOff = 0;
					ph = new PlanetHexagon(HexOff.x,HexOff.y);
					boolean tropic = (((Row / (Size / 3)) == 1) || (Row == Size));
					m_Sectors[CurHex - 1] = new Sector(PlanetArea.values()[Segment + 6],CurHex - 1,false,ph,tropic);
					m_SectorMap.get(PlanetArea.values()[Segment + 6]).add(m_Sectors[CurHex-1]);
                    if ((Cur==Start) && (Row < Size))
                    {
                        NewOff = 0 - (((Size - Row) * 2) * HexX);
                        if (Segment == 0)
                            NewOff += 10 * Size * HexX;
                        m_Sectors[CurHex-1].getHex().addOffset(NewOff,0);
                    }
                    if (Segment == 0 && (Cur==Start) && (Row == Size))
                    {
                    	NewOff = 0 - (((Row - Size) * 2) * HexX);
                        NewOff += 10 * Size * HexX;
                        m_Sectors[CurHex-1].getHex().addOffset(NewOff,0);
                    }
					HexOff.x += HexX * 2;

					Cur++;
					CurHex++;
				}
				HexOff = OrigOff;
				HexOff.x += HexX;
				HexOff.y += HexY;
			}
		}
		ph = new PlanetHexagon(BaseXOffset + ((Size + 2) * HexX),BaseYOffset + ((m_MapRowHeight + 2) * HexY) - HexY);
		m_Sectors[CurHex-1] = new Sector(PlanetArea.SOUTH_POLE,CurHex-1,false,ph,false); 
		m_SectorMap.get(PlanetArea.SOUTH_POLE).add(m_Sectors[CurHex-1]);
		Offset = new Point(0,0);
		for (int i = 0; i < 4; i++)
		{
			Offset.x += Size * HexX * 2;
			m_Sectors[CurHex-1].getHex().addOffset(Offset.x,0);
		}
		
		getMapBoundary();
	}

	protected void getMapBoundary()
	{
		m_MapBoundary = new Polygon();

		Point p = m_SectorMap.get(PlanetArea.EQUATOR).elementAt(0).getHex().getHexSideMidPoint(0);		
		m_MapBoundary.addPoint(p.x,p.y);

		p = m_SectorMap.get(PlanetArea.AREA_1).elementAt(m_SegmentSize - m_PlanetSize).getHex().getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);
		
		p = m_SectorMap.get(PlanetArea.NORTH_POLE).elementAt(0).getHex().getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);

		p = m_SectorMap.get(PlanetArea.AREA_2).elementAt(m_SegmentSize - m_PlanetSize).getHex().getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);

		p = m_SectorMap.get(PlanetArea.NORTH_POLE).elementAt(0).getHex().getOffset(0).getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);

		p = m_SectorMap.get(PlanetArea.AREA_3).elementAt(m_SegmentSize - m_PlanetSize).getHex().getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);
		
		p = m_SectorMap.get(PlanetArea.NORTH_POLE).elementAt(0).getHex().getOffset(1).getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);

		p = m_SectorMap.get(PlanetArea.AREA_4).elementAt(m_SegmentSize - m_PlanetSize).getHex().getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);

		p = m_SectorMap.get(PlanetArea.NORTH_POLE).elementAt(0).getHex().getOffset(2).getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);
		
		p = m_SectorMap.get(PlanetArea.AREA_5).elementAt(m_SegmentSize - m_PlanetSize).getHex().getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);

		p = m_SectorMap.get(PlanetArea.NORTH_POLE).elementAt(0).getHex().getOffset(3).getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);
		
		p = m_SectorMap.get(PlanetArea.AREA_1).elementAt(m_SegmentSize - m_PlanetSize).getHex().getOffset(0).getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);

		PlanetHexagon equaterStartOffset = m_SectorMap.get(PlanetArea.EQUATOR).elementAt(0).getHex().getOffset(0); 
		p = equaterStartOffset.getHexSideMidPoint(0);		
		m_MapBoundary.addPoint(p.x,p.y);
		
		p = equaterStartOffset.getHexSideMidPoint(2);		
		m_MapBoundary.addPoint(p.x,p.y);

		/*
		p = equaterStartOffset.getHexSideEndPoint(0);
		m_MapBoundary.addPoint(p.x,p.y);
		p = equaterStartOffset.getHexSideStartPoint(0);		
		m_MapBoundary.addPoint(p.x,p.y);
		p = equaterStartOffset.getHexSideEndPoint(5);		
		m_MapBoundary.addPoint(p.x,p.y);
		p = equaterStartOffset.getHexSideStartPoint(5);		
		m_MapBoundary.addPoint(p.x,p.y);
		p = equaterStartOffset.getHexSideStartPoint(4);		
		m_MapBoundary.addPoint(p.x,p.y);
		p = equaterStartOffset.getHexSideStartPoint(3);		
		m_MapBoundary.addPoint(p.x,p.y);
		p = equaterStartOffset.getHexSideStartPoint(2);
		m_MapBoundary.addPoint(p.x,p.y);
		*/

		p = m_SectorMap.get(PlanetArea.AREA_6).elementAt(0).getHex().getOffset(0).getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);

		p = m_SectorMap.get(PlanetArea.SOUTH_POLE).elementAt(0).getHex().getOffset(3).getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);
		
		p = m_SectorMap.get(PlanetArea.AREA_10).elementAt(0).getHex().getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);

		p = m_SectorMap.get(PlanetArea.SOUTH_POLE).elementAt(0).getHex().getOffset(2).getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);
		
		p = m_SectorMap.get(PlanetArea.AREA_9).elementAt(0).getHex().getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);

		p = m_SectorMap.get(PlanetArea.SOUTH_POLE).elementAt(0).getHex().getOffset(1).getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);
		
		p = m_SectorMap.get(PlanetArea.AREA_8).elementAt(0).getHex().getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);

		p = m_SectorMap.get(PlanetArea.SOUTH_POLE).elementAt(0).getHex().getOffset(0).getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);
		
		p = m_SectorMap.get(PlanetArea.AREA_7).elementAt(0).getHex().getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);

		p = m_SectorMap.get(PlanetArea.SOUTH_POLE).elementAt(0).getHex().getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);

		p = m_SectorMap.get(PlanetArea.AREA_6).elementAt(0).getHex().getCenter();		
		m_MapBoundary.addPoint(p.x,p.y);
		
		PlanetHexagon equaterStart = m_SectorMap.get(PlanetArea.EQUATOR).elementAt(0).getHex(); 
		p = equaterStart.getHexSideMidPoint(2);
		m_MapBoundary.addPoint(p.x,p.y);
		p = equaterStart.getHexSideEndPoint(2);		
		m_MapBoundary.addPoint(p.x,p.y);
		p = equaterStart.getHexSideEndPoint(3);		
		m_MapBoundary.addPoint(p.x,p.y);
		p = equaterStart.getHexSideEndPoint(4);
		m_MapBoundary.addPoint(p.x,p.y);
		p = equaterStart.getHexSideEndPoint(5);
		m_MapBoundary.addPoint(p.x,p.y);
		
	}

    public void drawGrid(Graphics2D comp2D)
    {
        Color bg = comp2D.getColor();
        comp2D.setColor(Color.white);
        comp2D.fillRect(0, 0, m_MapWidth, m_MapHeight);

        comp2D.setColor(bg);

        Shape CurClip = comp2D.getClip();
        setWorldClip(comp2D);

        for (int i = 0; i < m_Sectors.length; i++)
        	if (m_Sectors[i] != null)
        		m_Sectors[i].draw(comp2D, false);

        comp2D.setClip(CurClip);

        comp2D.setColor(Color.red);
        comp2D.drawPolygon(m_MapBoundary);
        
        comp2D.setColor(bg);
    }
    
    public void drawHexes(Graphics2D comp2D, Vector<Sector> sectors, boolean highlight)
    {
        Shape CurClip = comp2D.getClip();
        setWorldClip(comp2D);
        
        for (Sector sector: sectors)
        	sector.draw(comp2D, highlight);

        comp2D.setClip(CurClip);
        
    }
    
	
    protected void setWorldClip(Graphics2D comp)
    {
        comp.clip(m_MapBoundary);
    }
    
    public Sector getSector(int hexIndex)
    {
    	return m_Sectors[hexIndex];
    }
    
    public Vector<Sector> getHexNeighbours(int hexIndex)
    {
    	Vector<Sector> neighbours = new Vector<Sector>();
    	
    	Vector<Integer> indexes = m_Sectors[hexIndex].getHexNeighbours();
    	for (Integer index: indexes)
    		neighbours.add(m_Sectors[index]);
    	
    	return neighbours;
    }
    
    protected void calculateHexNeighbours(int Size)
    {
    	calculateNorthPoleHexNeighbours();
    	calculateNorthHemisphereHexNeighbours(Size);
    }
    
    private void calculateNorthPoleHexNeighbours()
    {
    	Sector northPole = m_Sectors[0];
    	
    	for (int i = 0; i < 5; i++)
    	{
    		int index = (i * m_SegmentSize) + 1;
    		northPole.setHexNeighbour(i, index);
    	}
		northPole.setHexNeighbour(5, northPole.getHexNeighbour(0));
    }
    
    private void calculateNorthHemisphereHexNeighbours(int Size)
    {
		int CurHex = 1;
		//Now do the northern hemisphere segments
		for (int Segment = 0; Segment < 5; Segment++)
		{
			for (int Row = 1; Row <= Size; Row++)
			{
				int Start = getRowStarts(Row);
				int End = getRowEnds(Row);
				int Cur = Start;
				while (Cur <= End)
				{
					Sector curSector = m_Sectors[CurHex];
					// Do Direction 1

					if (Row == 1) //Then it will be the north pole
						curSector.setHexNeighbour(0, 0);
					else if (Cur == End)
					{
						
					}
					
					// Do Direction 2
					
					// Do Direction 3

					// Do Direction 4

					// Do Direction 5

					// Do Direction 6
					
					
					Cur++;
					CurHex++;
				}
			}
		}
    }
    
    public class Sector
    {
    	private PlanetArea m_Area = null;
    	private int m_Index = 0;
    	private boolean m_NorthernHemisphere = true;
    	private PlanetHexagon m_Hex = null;
    	private boolean m_Tropic = false;
    	private int[] m_HexNeighbours = new int[6];
    	
    	public Sector(PlanetArea area, int index, boolean northernHemisphere, PlanetHexagon hex, boolean tropic)
    	{
    		m_Area = area;
    		m_Index = index;
    		m_NorthernHemisphere = northernHemisphere;
    		m_Hex = hex;
    		m_Tropic = tropic;
    	}
    	
    	public PlanetArea getPlanetArea()
    	{ return m_Area; }
    	
    	public int getIndex()
    	{ return m_Index; }
    	
    	public boolean isNorthernHemisphere()
    	{ return m_NorthernHemisphere; }
    	
    	public PlanetHexagon getHex()
    	{ return m_Hex; }
    	
    	public boolean isTropic()
    	{ return m_Tropic; }
    	
    	
        public void draw(java.awt.Graphics comp, boolean highlight)
        {
        	Color c = comp.getColor();
        	
        	if (m_Tropic)
        	{
        		comp.setColor(Color.LIGHT_GRAY);
            	m_Hex.fill(comp);
        		comp.setColor(Color.DARK_GRAY);        		
        	}
        	else
        		comp.setColor(Color.LIGHT_GRAY);
        	
        	m_Hex.draw(comp,Integer.toString(m_Index));
        	comp.setColor(c);
        	
        	if (highlight)
        		m_Hex.draw(comp,Integer.toString(m_Index));
        }
        
        public void setHexNeighbour(int direction, int index)
        {
        	m_HexNeighbours[direction] = index;
        }
        
        public int getHexNeighbour(int direction)
        {
        	return m_HexNeighbours[direction];
        }
        
        public Vector<Integer> getHexNeighbours()
        {
        	Vector<Integer> indexes = new Vector<Integer>();
        	for (int i = 0; i < 6; i++)
        	{
        		Integer index = m_HexNeighbours[i];
        		if (!indexes.contains(index))
        			indexes.add(index);
        	}
        	return indexes;
        }
        
    	
    }
}
