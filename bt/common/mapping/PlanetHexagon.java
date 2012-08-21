package bt.common.mapping;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Vector;

/**
 * <p>Title: Legatus</p>
 *
 * <p>Copyright: Copyright Daniel Cleyne (c) 2004-2006</p>
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


/*
 * This is the megamek hexagon class turned on its side to make hexes that work with Prefect maps
*/

public class PlanetHexagon extends Polygon
{
    static final long serialVersionUID = 1;

    // used for turns()
    private static final int    LEFT            = 1;
    private static final int    STRAIGHT        = 0;
    private static final int    RIGHT           = -1;
    
    private Point m_Center = null;
    private Rectangle m_Bounds = null;
    protected Vector<PlanetHexagon> m_Offsets = new Vector<PlanetHexagon>();

    public PlanetHexagon()
    {
    	buildHexagon(0,0);
    }

    public PlanetHexagon(int xOffset, int yOffset)
    {
    	buildHexagon(xOffset,yOffset);
    }
    
    private final void buildHexagon(int xOffset, int yOffset)
    {
    	addPoint(36 + xOffset, 0 + yOffset);
    	addPoint(71 + xOffset, 21 + yOffset);
    	addPoint(71 + xOffset, 62 + yOffset);
    	addPoint(36 + xOffset, 83 + yOffset);
    	addPoint(35 + xOffset, 83 + yOffset);
    	addPoint(0 + xOffset, 62 + yOffset);
    	addPoint(0 + xOffset, 21 + yOffset);
    	addPoint(35 + xOffset, 0 + yOffset);
        
        m_Center = new Point(xOffset + 36, yOffset + 42);
        m_Bounds = new Rectangle(xOffset,yOffset,73,84);
    }

    public Rectangle getBounds() 
    {
    	return m_Bounds;
    }

    public static int getWidth()
    {
    	return 72;
    }

    public static int getHeight()
    {
    	return 84;
    }

    public static int getXIncrement()
    {
    	return 36;
    }

    public static int getYIncrement()
    {
    	return 63;
    }

    void offset(Point aPoint)
    {
    	offset(aPoint.x,aPoint.y);
    }

    void offset(int XOffset, int YOffset)
    {
        for (int Count = 0; Count < xpoints.length; Count++)
        {
            xpoints[Count] += XOffset;
            ypoints[Count] += YOffset;
        }
    }

    public Point getCenter()
    {
    	return m_Center;
    }
    
    public boolean equals(Object o)
    {
    	if (!(o instanceof Polygon))
    		return false;
    	
    	Polygon p = (Polygon)o;
    	for (int i = 0; i < xpoints.length; i++)
    	{
    		if (xpoints[i] != p.xpoints[i]) return false;
    		if (ypoints[i] != p.ypoints[i]) return false;
    	}
    	return true;
    }

    /**
     * Returns true if this hex is intersected by the line
     * 
     * Changed arguments to int : DC 
     */
    public boolean isIntersectedBy(int x0, int y0, int x1, int y1) 
    {
        int side1 = turns(x0, y0, x1, y1, xpoints[0], ypoints[0]);
        if (side1 == STRAIGHT) 
        {
            return true;
        }
        for (int i = 1; i < 8; i++) 
        {
            int j = turns(x0, y0, x1, y1, xpoints[i], ypoints[i]);
            if (j == STRAIGHT || j != side1) 
            {
                return true;
            }
        }
        if (x0 == x1) //If the line is a vertical line
            if (xpoints[1] + 1 == x0 && xpoints[2] + 1 == x1) //Check if we miss the right of the hex by 1
                return true; //if we do then return true
        return false;
    }
    
    /**
     * From Megamek class IdealHex
     * Tests whether a line intersects a point or the point passes
     * to the left or right of the line.
     *
     * Changed arguments to int : DC 
     */
    public static int turns(int x0, int y0, int x1, int y1, int x2, int y2) {
        final double cross = (x1 - x0) * (y2 - y0) - (x2 - x0) * (y1 - y0);
        return ((cross > 0) ? LEFT : ((cross < 0) ? RIGHT : STRAIGHT));
    }

    /**
     * Returns the point the represents the 'start' point fo a hex side. These progress clockwise from
     * the top left point
     * @param hexSide
     * @return The start point of the hexSide
     */
    public Point getHexSideStartPoint(int hexSide)
    {
    	Point p = new Point();
    	switch (hexSide)
    	{
	    	case 0:
	    		p.x = xpoints[0];
	    		p.y = ypoints[0];
	    		break;
	    	case 1:
	    		p.x = xpoints[1];
	    		p.y = ypoints[1];
	    		break;
	    	case 2:
	    		p.x = xpoints[2];
	    		p.y = ypoints[2];
	    		break;
	    	case 3:
	    		p.x = xpoints[4];
	    		p.y = ypoints[4];
	    		break;
	    	case 4:
	    		p.x = xpoints[5];
	    		p.y = ypoints[5];
	    		break;
	    	case 5:
	    		p.x = xpoints[6];
	    		p.y = ypoints[6];
	    		break;
    	}
        return p;
    }

    /*
    * Returns the point the represents the 'end' point fo a hex side. These progress clockwise from
    * the top right point
     */
    public Point getHexSideEndPoint(int hexSide)
    {
    	Point p = new Point();
    	switch (hexSide)
    	{
	    	case 0:
	    		p.x = xpoints[1];
	    		p.y = ypoints[1];
	    		break;
	    	case 1:
	    		p.x = xpoints[2];
	    		p.y = ypoints[2];
	    		break;
	    	case 2:
	    		p.x = xpoints[3];
	    		p.y = ypoints[3];
	    		break;
	    	case 3:
	    		p.x = xpoints[5];
	    		p.y = ypoints[5];
	    		break;
	    	case 4:
	    		p.x = xpoints[6];
	    		p.y = ypoints[6];
	    		break;
	    	case 5:
	    		p.x = xpoints[7];
	    		p.y = ypoints[7];
	    		break;
    	}
        return p;
    }

    /**
     * Returns the point that represents the 'mid' point fo a hex side. These progress clockwise from
     * the top point
     * @param hexSide
     * @return The mid point of the hexSide
     */
    public Point getHexSideMidPoint(int hexSide)
    {
    	Point p1 = getHexSideStartPoint(hexSide);
    	Point p2 = getHexSideEndPoint(hexSide);
    	
    	Point p = new Point();
    	p.x = p1.x + ((p2.x - p1.x) / 2);
    	p.y = p1.y + ((p2.y - p1.y) / 2);
    	return p;
    }
    
    
//
    public void draw(java.awt.Graphics comp)
    {
    	draw(comp,"");
    }

    public void draw(java.awt.Graphics comp, String label)
    {
        comp.drawPolygon(this);
        if (!label.equalsIgnoreCase(""))
        {
        	Color c = comp.getColor();
        	comp.setColor(Color.BLACK);
        	Point p = getCenter();
        	comp.drawString(label, p.x, p.y);
        	comp.setColor(c);
        }
        
        for (PlanetHexagon ph: m_Offsets)
        {
        	comp.drawPolygon(ph);
        	Point p = ph.getCenter();
        	comp.drawString(label, p.x, p.y);
        }
    }
    
    public void fill(java.awt.Graphics comp)
    {
        comp.fillPolygon(this);
        
        for (PlanetHexagon ph: m_Offsets)
        	comp.fillPolygon(ph);
    }
    
    public int hashCode()
    {
        return toString().hashCode();
    }
    
    public String toString()
    {
        String retVal = "";
        for (int i = 0; i < 7; i++)
        {
            retVal += "(" + Integer.toString(xpoints[i]) + "," + Integer.toString(ypoints[i]) + ")";
            if (i < 6) retVal += ",";
        }
        return retVal;
    }

    public void addOffset(Point offset)
    {
        addOffset(offset.x + m_Bounds.x,offset.y + m_Bounds.y);
    }

    public void addOffset(int xOffset, int yOffset)
    {
    	xOffset += m_Bounds.x;
    	yOffset += m_Bounds.y;
        m_Offsets.add(new PlanetHexagon(xOffset,yOffset));
    }

    public void removeOffset(int index)
    {
        m_Offsets.remove(index);
    }

    public PlanetHexagon getOffset(int index)
    {
        return m_Offsets.get(index);
    }

    public PlanetHexagon getOffsetHex(int index)
    {
    	if (index > -1 && index < m_Offsets.size())
    	{
	        return m_Offsets.get(index);
    	}
    	return this;
    }

    
}
