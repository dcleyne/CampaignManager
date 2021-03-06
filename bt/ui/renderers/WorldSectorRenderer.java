package bt.ui.renderers;

import java.awt.*;

import bt.elements.galaxy.SettlementType;
import bt.mapping.TerrainType;
import bt.mapping.Hexagon;
import bt.mapping.TerrainFactory;
import bt.mapping.planet.PlanetMapSector;
import bt.mapping.planet.WorldMapNavigation;

/**
 * Title:        Ant
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 * @author
 * @version 1.0
 */

public class WorldSectorRenderer extends Hexagon
{
	private static final long serialVersionUID = 1;

    PlanetMapSector m_Details;

    boolean m_NorthHemisphere = true;
    boolean m_RowStart = false;
    boolean m_RowEnd = false;

    public WorldSectorRenderer(int Index)
    {
    }

    public WorldSectorRenderer(int XOffset, int YOffset, int MainDimension, PlanetMapSector Details, WorldMapNavigation nav)
    {
        super(XOffset,YOffset,MainDimension,true);

        m_Details = Details;
        
        int sector = Details.getSectorNumber();
        m_NorthHemisphere = nav.isHexNorthern(sector);
        m_RowStart = nav.isRowStart(sector);
        m_RowEnd = nav.isRowEnd(sector);
    }

    public void SetHexWidth(int Width)
    {
	_MainDimension = Width;
    }

    public void drawMain(Graphics2D comp)
    {
    	super.draw(comp);
    }
    public void fill(Graphics2D comp)
    {
        comp.fillPolygon(this);
    }

    public void draw(Graphics2D comp)
    {
        Color curCol = comp.getColor();
        Color bakCol = java.awt.Color.darkGray;
        Image img = null;
        if (m_Details != null)
        {
            bakCol = TerrainFactory.INSTANCE.getTerrainBackground(m_Details.getTerrainType());
            img = TerrainFactory.INSTANCE.getTerrainImage(m_Details.getTerrainType());
        }

    	comp.setColor(bakCol);
        comp.fillPolygon(this);
        
        Rectangle aRect = getBounds();
        if (img != null)
        {
            int XOff = (int)(aRect.getWidth() - img.getWidth(null)) / 2;
            int YOff = (int)(aRect.getHeight() - img.getHeight(null)) / 2;
            comp.drawImage(img,(int)aRect.getX() + XOff,(int)aRect.getY()+2+YOff,null);
        }


        comp.setColor(curCol);
        super.draw(comp);
        
        comp.setColor(curCol);

		for (int i = 0; i < _Offsets.size(); i++)
		{
            Hexagon aHex = getOffsetHex(i);

        	comp.setColor(bakCol);            
            comp.fillPolygon(aHex);

            if (img != null)
            {
                aRect = aHex.getBounds();
                int XOff = (int)(aRect.getWidth() - img.getWidth(null)) / 2;
                int YOff = (int)(aRect.getHeight() - img.getHeight(null)) / 2;
                comp.drawImage(img,(int)aRect.getX() + XOff,(int)aRect.getY()+2+YOff,null);
            }

            comp.setColor(curCol);
            aHex.draw(comp);
        }
    }
    
    public void drawRivers(Graphics2D comp)
    {
        Color curCol = comp.getColor();
    	
        //Draw Rivers
        if (m_Details.hasRiver())
        {
            Stroke currentStroke = comp.getStroke();
			comp.setColor(TerrainFactory.INSTANCE.getTerrainBackground(TerrainType.LAKE));
        	for (int river = 0; river < 6; river++)
        	{
        		int riverSize = m_Details.getRiver(river);
        		if (riverSize > 0)
        		{
        			Point startPoint = super.getHexsideStartPoint(river + 1);
        			Point endPoint = super.getHexsideEndPoint(river + 1);
        			comp.setStroke(new BasicStroke(riverSize + 1));
        			
        			comp.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        		}
        	}
            comp.setStroke(currentStroke);
        }
		for (int i = 0; i < _Offsets.size(); i++)
		{
            Hexagon aHex = getOffsetHex(i);

            //Draw Rivers
            if (m_Details.hasRiver())
            {
                Stroke currentStroke = comp.getStroke();
            	for (int river = 0; river < 6; river++)
            	{
            		int riverSize = m_Details.getRiver(river);
            		if (riverSize > 0)
            		{
            			comp.setColor(TerrainFactory.INSTANCE.getTerrainBackground(TerrainType.LAKE));
            			Point startPoint = aHex.getHexsideStartPoint(river + 1);
            			Point endPoint = aHex.getHexsideEndPoint(river + 1);
            			comp.setStroke(new BasicStroke(riverSize));
            			
            			comp.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
            		}
            	}
                comp.setStroke(currentStroke);
            }
            
        }
        comp.setColor(curCol);
    }

    public void drawRoads(Graphics2D comp)
    {
        Color curCol = comp.getColor();
		
		//Draw Roads
        if (m_Details.hasRoad())
        {
            Stroke currentStroke = comp.getStroke();
			comp.setColor(Color.lightGray);
        	for (int road = 0; road < 6; road++)
        	{
        		int roadSize = m_Details.getRoad(road);
        		if (roadSize > 0)
        		{
        			Point startPoint = super.getCenter();
        			Point endPoint = super.getHexsideMidPoint(road + 1);
        			comp.setStroke(new BasicStroke(roadSize + 1));
        			
        			comp.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        		}
        	}
            comp.setStroke(currentStroke);
        }
		for (int i = 0; i < _Offsets.size(); i++)
		{
            Hexagon aHex = getOffsetHex(i);

            //Draw Roads
            if (m_Details.hasRoad())
            {
                Stroke currentStroke = comp.getStroke();
            	for (int road = 0; road < 6; road++)
            	{
            		int offset = 0;
            		int roadSize = m_Details.getRoad(road);
            		if (roadSize > 0)
            		{    
            			comp.setColor(Color.lightGray);
            			if (m_RowStart)
            			{
            				if (m_NorthHemisphere)
            				{
            					if (road == 0)
            					{
                        			comp.setColor(Color.pink);
            						offset = 6;
            					}
            				}
            				else
            				{
            					switch (road)
            					{
            					case 0:
            						offset = 2;
            						break;
            					case 1:
            						offset = 2;
            						break;
            					case 2:
            						break;
            					case 3:
            						offset = 2;
            						break;
            					case 4:
            						offset = 2;
            						break;
            					case 5:
            						offset = -4;
            						break;
            					}
            				}
            			}
            			else
            				offset++;
            			
            			Point startPoint = aHex.getCenter();
            			Point endPoint = aHex.getHexsideMidPoint(road + offset);
            			comp.setStroke(new BasicStroke(roadSize + 1));
            			
            			comp.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
            		}
            	}
                comp.setStroke(currentStroke);
            }
            
        }
		
        comp.setColor(curCol);
    }

    public void drawSettlement(Graphics2D comp, SettlementType st)
    {
        Image img = TerrainFactory.INSTANCE.getSettlementImage(st);
        Rectangle aRect = getBounds();
        
        if (img != null)
        {
            comp.setColor(Color.white);
            comp.fillPolygon(this);
        	
            int XOff = (int)(aRect.getWidth() - img.getWidth(null)) / 2;
            int YOff = (int)(aRect.getHeight() - img.getHeight(null)) / 2;
            comp.drawImage(img,(int)aRect.getX() + XOff + 1,(int)aRect.getY()+YOff,null);

            comp.setColor(Color.black);
            comp.drawPolygon(this);
        } else
        	System.out.println("Failed to draw Settlement of type " + st.toString() + " at " + m_Details.getSectorNumber());
        
		for (int i = 0; i < _Offsets.size(); i++)
		{
            Hexagon aHex = getOffsetHex(i);

            if (img != null)
            {
                comp.setColor(Color.white);
                comp.fillPolygon(aHex);

                aRect = aHex.getBounds();
                int XOff = (int)(aRect.getWidth() - img.getWidth(null)) / 2;
                int YOff = (int)(aRect.getHeight() - img.getHeight(null)) / 2;
                comp.drawImage(img,(int)aRect.getX() + XOff + 1,(int)aRect.getY()+YOff,null);

                comp.setColor(Color.black);
                comp.drawPolygon(aHex);
            }
		}
        
    }
    
    public void drawElevation(Graphics2D comp)
    {
        Color curCol = comp.getColor();

    	Color backCol = Color.white;
    	int elevation = m_Details.getMeanAltitude(); 
		if ( elevation > 0 )
			backCol = new Color(0, elevation / 200 + 127, 0 );
		else
			backCol = new Color(0, 0, (elevation / 200) + 255 );

		comp.setColor(backCol);
        comp.fillPolygon(this);

		for (int i = 0; i < _Offsets.size(); i++)
		{
            Hexagon aHex = getOffsetHex(i);
            comp.fillPolygon(aHex);
		}
		
		comp.setColor(curCol);
    }
    
    public void drawTemperature(Graphics2D comp)
    {
        Color curCol = comp.getColor();

    	Color backCol = Color.white;
    	int temp = m_Details.getMeanSurfaceTemperature(); 
		if ( temp > 0 )
		{
			int red = (temp * 6) + 15;
			if (red > 255) red = 255;
			backCol = new Color(red, 0, 0 );
		}
		else
		{
			int blue = 255 + (temp * 6);
			if (blue < 0) blue = 0;
			backCol = new Color(0, 0, blue);
		}

		comp.setColor(backCol);
        comp.fillPolygon(this);

		for (int i = 0; i < _Offsets.size(); i++)
		{
            Hexagon aHex = getOffsetHex(i);
            comp.fillPolygon(aHex);
		}
		
		comp.setColor(curCol);
    }
    

    public boolean contains(int x, int y)
    {
        if (super.contains(x,y))
            return true;

        for (int i = 0; i < _Offsets.size(); i++)
        {
            Hexagon aHex = getOffsetHex(i);
            if (aHex.contains(x,y))
                return true;
        }
        return false;
    }

    public void setTerrain(TerrainType Terrain)
    {
        m_Details.setTerrainType(Terrain);
    }
}
