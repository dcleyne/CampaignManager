/*******************************************************************************
 * Title: Legatus
 * 
 * Copyright Daniel Cleyne (c) 2004-2013
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at our option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * @author Daniel Cleyne
 ******************************************************************************/

package bt.ui.renderers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import bt.mapping.Hexagon;
import bt.mapping.TerrainFactory;
import bt.mapping.campaign.CampaignMapHex;

public class CampaignBoardHex extends HexBoardHex
{

	public CampaignBoardHex(HexBoardRenderer renderer, CampaignMapHex theMapHex, int boardX, int boardY)
	{
		super(renderer, theMapHex, boardX, boardY);

	}

	protected CampaignMapHex getMapHex()
	{
		return (CampaignMapHex) _MapHex;
	}

	@Override
	public synchronized void draw(Graphics2D g, ImageObserver obs)
	{
        Color curCol = g.getColor();
        Color bakCol = java.awt.Color.darkGray;
        Image img = null;
        
        if (getMapHex() != null)
        {
            bakCol = TerrainFactory.INSTANCE.getTerrainBackground(getMapHex().getTerrainType());
            img = TerrainFactory.INSTANCE.getTerrainImage(getMapHex().getTerrainType());
        }

    	g.setColor(bakCol);
    	Hexagon h = _Renderer.getHex(_BoardCoordinate);
        g.fillPolygon(h);
        
        Rectangle aRect = h.getBounds();
        if (img != null)
        {
            int XOff = (int)(aRect.getWidth() - img.getWidth(null)) / 2;
            int YOff = (int)(aRect.getHeight() - img.getHeight(null)) / 2;
            g.drawImage(img,(int)aRect.getX() + XOff,(int)aRect.getY()+2+YOff,null);
        }

        g.setColor(curCol);
        super.draw(g, obs);
        
        g.setColor(curCol);
	}

	/*
    public void drawRivers(Graphics2D comp)
    {
        Color curCol = comp.getColor();
    	
        //Draw Rivers
        if (getMapHex().hasRiver())
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
		for (int i = 0; i < m_Offsets.size(); i++)
		{
            Hexagon aHex = GetOffsetHex(i);

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
        			Point startPoint = super.GetCenter();
        			Point endPoint = super.getHexsideMidPoint(road + 1);
        			comp.setStroke(new BasicStroke(roadSize + 1));
        			
        			comp.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        		}
        	}
            comp.setStroke(currentStroke);
        }
		for (int i = 0; i < m_Offsets.size(); i++)
		{
            Hexagon aHex = GetOffsetHex(i);

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
            			
            			Point startPoint = aHex.GetCenter();
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
        
		for (int i = 0; i < m_Offsets.size(); i++)
		{
            Hexagon aHex = GetOffsetHex(i);

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
    
*/    
}
