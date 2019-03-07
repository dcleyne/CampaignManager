package bt.mapping;

import java.awt.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;

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
 * Added some of the code from the Megamek IdealHex class. The rest of the code went into Hexagon
 * 
 * This class is now restricted to being a horizontal hex grid.
 * It was intended for it to be more flexible than that but reality intervened :).
 */

public class HexGrid extends Object implements Serializable
{
    static final long serialVersionUID = 1;
    public static final double HEXSIDE = Math.PI / 3.0;

    private int m_NumHexes;
    
    protected Hexagon[] m_HexList;

    private int m_XOffset;
    private int m_YOffset;
    private int m_GridWidth;
    private int m_GridHeight;
    
    private HashMap <Hexagon,Coordinate> m_CoordMappings = new HashMap<Hexagon,Coordinate>();
    
    public HexGrid()
    {
    }

    public HexGrid(int gridWidth, int gridHeight, int xOffset,int yOffset)
    {
    	System.out.println("Creating hexGrid Width(" + Integer.toString(gridWidth) + ") Height(" + Integer.toString(gridHeight) + ")");
    	m_GridWidth = gridWidth;
    	m_GridHeight = gridHeight;
    	m_XOffset = xOffset;
    	m_YOffset = yOffset;
    	
		buildHexGrid(xOffset,yOffset, gridWidth,gridHeight);
	}
       
    public Hexagon getHex(int HexNo)
    {
        return m_HexList[HexNo];
    }

    public Hexagon getHex(int x, int y)
    {
    	if (x < 0 || x >= m_GridWidth) return null;
    	if (y < 0 || y >= m_GridHeight) return null;
    	
    	int index = (m_GridWidth) * y + x;
    	if (index >= 0 && index < m_HexList.length)
    		return m_HexList[index];
    	else
    		return null;
    }

    public Hexagon getHex(Coordinate c)
    {
        return getHex(c.x,c.y);
    }
    
    public Point getOffset()
    {
    	return new Point(m_XOffset,m_YOffset);
    }
    
    public Dimension getGridDimension()
    {
        int HexWidth = Hexagon.getXIncrement();
        int HexHeight = Hexagon.getHeight();
        int XRem = Hexagon.getWidth() - HexWidth;
        int YRem = HexHeight;// - Hexagon.getYIncrement();
        return new Dimension(HexWidth * m_GridWidth + XRem,HexHeight * m_GridHeight + YRem);    	
    }
    
    public Dimension getHexSize()
    {
    	return new Dimension(Hexagon.getWidth(),Hexagon.getHeight());
    }
    
    
    protected void buildHexGrid(int xOff,int yOff, int GridWidth, int GridHeight)
    {
    	
	  m_NumHexes = GridWidth * GridHeight;
	  m_HexList = new Hexagon[m_NumHexes];

      int HexXInc = Hexagon.getXIncrement();
      int HexYInc = Hexagon.getYIncrement();

      int XOffset = xOff;
      int YOffset = yOff;

      
      for (int x = 0; x < GridWidth; x++)
      {
    	  for (int y = 0; y < GridHeight; y++)
          {
          	  Hexagon newHex = new Hexagon(XOffset , YOffset + (y * HexYInc * 2));
              m_HexList[(y*GridWidth + x)] = newHex;
              m_CoordMappings.put(newHex,new Coordinate(x,y));
          }
    	  if (x%2 == 0)
          {
              YOffset += HexYInc;
          }
          else
          {
        	  YOffset -= HexYInc;
          }
          XOffset += HexXInc;
      }
    }
    
    public int getSize()
    {
    	return m_NumHexes;
    }
    
    public Hexagon getHexFromPoint(Point p)
    {
        for (int i = 0; i < m_HexList.length; i++)
            if (m_HexList[i].contains(p))
                return m_HexList[i];
        return null;
    }

    public Coordinate getCoordinateFromPoint(Point p)
    {
        for (int i = 0; i < m_HexList.length; i++)
        {
            if (m_HexList[i].contains(p))
            {
                int y = i / m_GridWidth;
                int x = i - (y * m_GridWidth);
                return new Coordinate(x,y);
            }
        }
        return null;
    }
    
    public Coordinate getCoordinateFromHex(Hexagon hex)
    {
        return m_CoordMappings.get(hex);
    }

    /**
     * Code from Megamek
     * Returns the radian direction of another Coordinate.
     * 
     * @param srcCoord the source coordinate.
     * @param destCoord the destination coordinate.
     */
    public double radian(Coordinate srcCoord, Coordinate destCoord) 
    {
        final Hexagon src = getHex(srcCoord);
        final Hexagon dst = getHex(destCoord);
        
        // don't divide by 0
        if (src.getCenter().y == dst.getCenter().y) 
        {
            return (src.getCenter().x < dst.getCenter().x) ? Math.PI / 2 : Math.PI * 1.5;
        }
        
        double r = Math.atan((dst.getCenter().x - src.getCenter().x) / (src.getCenter().y - dst.getCenter().y));
        // flip if we're upside down
        if (src.getCenter().y < dst.getCenter().y) {
            r = (r + Math.PI) % (Math.PI * 2);
        }
        // account for negative angles
        if (r < 0) {
            r += Math.PI * 2;
        }

        return r;
    }
    
    /**
     * Code from Megamek
     * Returns the degree direction of another Coordinate.
     * 
     * @param s the source coordinate.
     * @param d the destination coordinate.
     */
    public int degree(Coordinate s, Coordinate d) {
        return (int)Math.round((180 / Math.PI) * radian(s,d));
    }
    
    /**
     * Code from Megamek
     * Returns the direction in which another coordinate lies; 0 if
     * the coordinates are equal.
     * 
     * @param c the source coordinate.
     * @param d the destination coordinate.
     */
    public int direction(Coordinate c, Coordinate d) 
    {
        return (int)Math.round(radian(c,d) / HEXSIDE) % 6;
    }
    
    
    /**
     * Code from Megamek
     * Returns the distance to another coordinate.
     * 
     * @param src the source coordinate
     * @param dst the destination coordinate
     */
    public int distance(Coordinate src, Coordinate dst) 
    {
        int xd, ym, ymin, ymax, yo;
        xd = Math.abs(src.x - dst.x);
        yo = (xd / 2) + (!src.isXOdd() && dst.isXOdd() ? 1 : 0);
        ymin = src.y - yo;
        ymax = ymin + xd;
        ym = 0;
        if (dst.y < ymin) {
            ym = ymin - dst.y;
        }
        if (dst.y > ymax) {
            ym = dst.y - ymax;
        }
        return xd + ym;
    }
    
    
    
    /**
     * This code taken from Megamek class IdealHex
     * 
     * Returns an array of the Coordinate of hexes that are crossed by a straight
     * line from the center of src to the center of dest, including src & dest.
     *
     * The returned coordinates are in line order, and if the line passes
     * directly between two hexes, it returns them both.
     *
     * Based on the degree of the angle, the next hex is going to be one of
     * three hexes.  We check those three hexes, sides first, add the first one
     * that intersects and continue from there.
     *
     * Based off of some of the formulas at Amit's game programming site.
     * (http://www-cs-students.stanford.edu/~amitp/gameprog.html)
     */
    public ArrayList<Coordinate> intervening(Coordinate src, Coordinate dest) 
    {
        return intervening(src,dest,false);
    }

    public ArrayList<Coordinate> intervening(Coordinate src, Coordinate dest, boolean split) 
    {
        ArrayList<Coordinate> hexes = new ArrayList<Coordinate>();

        Hexagon iSrc = getHex(src);
        Hexagon iDest = getHex(dest);
        
        if (iSrc == null || iDest == null) return hexes;
    
        int[] directions = new int[3];
        int centerDirection = direction(src,dest);
        if(split) {
            // HACK to make left appear before right in the sequence reliably
            centerDirection = (int)Math.round(radian(src,dest) + 0.0001 / HEXSIDE) % 6;
        }
        directions[2] = centerDirection; // center last
        directions[1] = (centerDirection + 5) % 6;
        directions[0] = (centerDirection + 1) % 6;
    
        Coordinate current = src;
    
        hexes.add(current);
        while(!dest.equals(current)) {
            current = nextHex(current, iSrc, iDest, directions);
            if (current != null) //Add the next hex into the list
            	hexes.add(current);
            else //We couldn't find the next hex so we must be trying to reach offboard
            	break;
        }
        return hexes;
    }

    /**
     * This code taken from Megamek class IdealHex
     * 
     * Returns the first further hex found along the line from the centers of
     * src to dest.  Checks the three directions given and returns the closest.
     *
     * This relies on the side directions being given first.  If it checked the
     * center first, it would end up missing the side hexes sometimes.
     *
     * Not the most elegant solution, but it works.
     */
    private Coordinate nextHex(Coordinate current, Hexagon iSrc, Hexagon iDest, int[] directions) 
    {
        for (int i = 0; i < directions.length; i++) 
        {
            Coordinate testing = current.translated(directions[i]);
            if (isValidCoordinate(testing))
            {
                if (getHex(testing).isIntersectedBy(iSrc.getCenter().x, iSrc.getCenter().y, iDest.getCenter().x, iDest.getCenter().y)) 
                {
                    return testing;
                }
            }
        }
        //More than likely we're trying to go offboard
        return null;
    }

    /*
     * Checks that a Coordinate is inside this grid
     */
    public boolean isValidCoordinate(Coordinate c)
    {
        if (c.x < 0) return false;
        if (c.x >= m_GridWidth) return false;
        if (c.y < 0) return false;
        if (c.y >= m_GridHeight) return false;
        return true;
    }
    
}
