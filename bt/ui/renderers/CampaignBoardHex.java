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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import bt.elements.galaxy.SettlementType;
import bt.mapping.TerrainFactory;
import bt.mapping.TerrainType;
import bt.mapping.campaign.CampaignMapHex;
import bt.util.Hexagon;

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
	protected void drawImage(Graphics2D g, ImageObserver obs)
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
        
        Shape curClip = g.getClip();
        g.setClip(h);
        
        Rectangle aRect = h.getBounds();
        if (img != null)
        {
        	int imgHeight = img.getHeight(null);
        	int imgWidth = img.getWidth(null);
        	
        	int rows = aRect.height / imgHeight;
        	if (aRect.height % imgHeight != 0)
        		rows ++;
        	
        	int cols = aRect.width / imgWidth;
        	if (aRect.width % imgWidth != 0)
        		cols++;

            int yOff = 0; //(int)(aRect.getHeight() - img.getHeight(null)) / 2;

        	for (int row = 0; row < rows; row++)
        	{
                int xOff = 0; //(int)(aRect.getWidth() - img.getWidth(null)) / 2;
        		for (int col = 0; col < cols; col++)
        		{
                    g.drawImage(img,(int)aRect.getX() + xOff,(int)aRect.getY()+2+yOff,null);
                    xOff += imgWidth;
        		}
        		yOff += imgHeight;
        	}
        }

        g.setClip(curClip);
        g.setColor(curCol);		
	}
	
	@Override
	public synchronized void draw(Graphics2D g, ImageObserver obs)
	{
        super.draw(g, obs);
	}

    public void drawRivers(Graphics2D comp)
    {
        Color curCol = comp.getColor();
    	
        //Draw Rivers
        if (getMapHex().hasRiver())
        {
        	Hexagon h = _Renderer.getHex(_BoardCoordinate);
            Stroke currentStroke = comp.getStroke();
			comp.setColor(TerrainFactory.INSTANCE.getTerrainBackground(TerrainType.LAKE));
        	for (int river = 0; river < 6; river++)
        	{
        		int riverSize = getMapHex().getRiver(river);
        		if (riverSize > 0)
        		{
        			Point startPoint = h.getHexsideStartPoint(river + 1);
        			Point endPoint = h.getHexsideEndPoint(river + 1);
        			comp.setStroke(new BasicStroke(riverSize + 1));
        			
        			comp.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        		}
        	}
            comp.setStroke(currentStroke);
        }
        comp.setColor(curCol);
    }

    public void drawRoads(Graphics2D comp)
    {
        Color curCol = comp.getColor();
        CampaignMapHex mapHex = getMapHex();
    	Hexagon h = _Renderer.getHex(_BoardCoordinate);
        
		//Draw Roads
        if (mapHex.hasRoad())
        {
            Stroke currentStroke = comp.getStroke();
			comp.setColor(Color.darkGray);
        	for (int road = 0; road < 6; road++)
        	{
        		int roadSize = mapHex.getRoad(road);
        		if (roadSize > 0)
        		{
        			Point startPoint = h.getCenter();
        			Point endPoint = h.getHexsideMidPoint(road + 1);
        			comp.setStroke(new BasicStroke(roadSize + 1));
        			
        			comp.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        		}
        	}
            comp.setStroke(currentStroke);
        }
            
        comp.setColor(curCol);
    }

    public void drawSettlement(Graphics2D comp)
    {
    	SettlementType st = getMapHex().getSettlementType();
    	if (st != null && st != SettlementType.NONE)
    	{
	        BufferedImage img = TerrainFactory.INSTANCE.getSettlementImage(st);
	    	Hexagon h = _Renderer.getHex(_BoardCoordinate);
	        Rectangle aRect = h.getBounds();
	        
	        if (img != null)
	        {
	            int xOff = (int)(aRect.getWidth() - img.getWidth(null)) / 2;
	            int yOff = (int)(aRect.getHeight() - img.getHeight(null)) / 2;
	            comp.drawImage(img,(int)aRect.getX() + xOff + 1,(int)aRect.getY()+yOff,null);

	    		Font currentFont = comp.getFont();
	    		comp.setFont(_Renderer.getFont(BoardRenderer.FontSize.NINE));

	    		String label = getMapHex().getSettlementName();
	    		if (label.isEmpty())
	    			return;
	    		
    			int tWidth = comp.getFontMetrics().stringWidth(label);
    			int tHeight = comp.getFontMetrics().getAscent();
    			float xOffset = (float) (aRect.getX() + (aRect.getWidth() - tWidth) / 2);
    			float yOffset = (float) (aRect.getY() + img.getHeight(null) + yOff + tHeight);

	    		comp.setColor(TerrainFactory.INSTANCE.getReverseTextColor(getMapHex().getTerrainType()));
	    		comp.drawString(label, xOffset - 1, yOffset + 1);
	    		comp.setColor(_TextColour);
	    		comp.drawString(label, xOffset, yOffset);

	    		comp.setFont(currentFont);
	        } 
	        else
	        	System.out.println("Failed to draw Settlement of type " + st.toString() + " at " + getMapHex().getCoordinate());
    	}
    }
    
}
