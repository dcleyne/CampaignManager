package bt.mapping;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

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


/*Dumbed this class right down to work with Megamek images.
* Using mathematics did not agree with the images provided and was slow anyway
* It will mean that Hexes for Prefect Planets will have to be specially written.
* 
* Added some of the code from the Megamek IdealHex class. The rest of the code went into HexGrid
*/

public class Hexagon extends Polygon
{
    static final long serialVersionUID = 1;

    // used for turns()
    private static final int    LEFT            = 1;
    private static final int    STRAIGHT        = 0;
    private static final int    RIGHT           = -1;
    
    private Point m_Center = null;
    private Rectangle m_Bounds = null;

    public Hexagon()
    {
    	buildHexagon(0,0);
    }

    public Hexagon(int xOffset, int yOffset)
    {
    	buildHexagon(xOffset,yOffset);
    }
    
    private final void buildHexagon(int xOffset, int yOffset)
    {
        addPoint(21 + xOffset, 0 + yOffset);
        addPoint(62 + xOffset, 0 + yOffset);
        addPoint(83 + xOffset, 35 + yOffset);
        addPoint(83 + xOffset, 36 + yOffset);
        addPoint(62 + xOffset, 71 + yOffset);
        addPoint(21 + xOffset, 71 + yOffset);
        addPoint(0 + xOffset, 36 + yOffset);
        addPoint(0 + xOffset, 35 + yOffset);
        
        m_Center = new Point(xOffset + 42, yOffset + 36);
        m_Bounds = new Rectangle(xOffset,yOffset,84,73);
    }

    public Rectangle getBounds() 
    {
    	return m_Bounds;
    }

    public static int getWidth()
    {
    	return 84;
    }

    public static int getHeight()
    {
    	return 72;
    }

    public static int getXIncrement()
    {
    	return 63;
    }

    public static int getYIncrement()
    {
    	return 36;
    }
    
    public static int getReverseFacing(int facing)
    {
    	int newFacing = facing - 3;
    	if (newFacing < 0)
    		newFacing += 6;
    	
    	return newFacing;
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
        if (y0 == y1) //If the line is a horizontal line
            if (ypoints[4] + 1 == y0 && ypoints[5] + 1 == y1) //Check if we miss the bottom of the hex by 1
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
	    		p.x = xpoints[3];
	    		p.y = ypoints[3];
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
	    		p.x = xpoints[7];
	    		p.y = ypoints[7];
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
	    		p.x = xpoints[4];
	    		p.y = ypoints[4];
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
	    		p.x = xpoints[0];
	    		p.y = ypoints[0];
	    		break;
    	}
        return p;
    }

    /**
     * Returns the point that represents the 'mid' point fo a hex side. These progress clockwise from
     * the top left point
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
        comp.drawPolygon(this);
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

}
