package bt.mapping;

import java.util.*;

import java.awt.*;

/**
 * <p>Title: Battletech Mercenary Management</p>
 * <p>Description: Utility to manage mercenary units and manage contracts</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Daniel Cleyne
 * @version 0.1
 */

public class Hexagon extends Polygon
{
	private static final long serialVersionUID = 1;
	// used for turns()
	private static final int LEFT = 1;
	private static final int STRAIGHT = 0;
	private static final int RIGHT = -1;

	
    public static final double Tan30 = Math.tan(Math.PI / 6.0);

    protected boolean _Vertical;
    protected int _MainDimension;
    protected Vector<Point> _Offsets;

    public Hexagon()
    {
        _Vertical = false;
        _MainDimension = 0;
        for (int Count = 0; Count < 6; Count++)
        {
            addPoint(0, 0);
        }

        _Offsets = new Vector<Point>();
    }

    public Hexagon(int XOffset, int YOffset, int MainDimension, boolean Vertical)
    {
        _Vertical = Vertical;
        _MainDimension = MainDimension;
        for (int Count = 0; Count < 6; Count++)
        {
            Point aPoint;
            if (Vertical)
            {
                aPoint = getVertPolyPoint(Count + 1);
            }
            else
            {
                aPoint = getHorzPolyPoint(Count + 1);
            }

            addPoint(aPoint.x + XOffset, aPoint.y + YOffset);
        }
        _Offsets = new Vector<Point>();
    }

    public Hexagon(Rectangle aRect, boolean Vertical)
    {
        _Vertical = Vertical;
        int Width = aRect.width;
        int Height = aRect.height;
        int XOffset = 0;
        int YOffset = 0;

        if (_Vertical)
        {
            _MainDimension = Width;
            while (getVertHexHeight() > Height)
            {
                _MainDimension--;
            }

            XOffset = aRect.x + ( (aRect.width - getVertHexWidth()) / 2);
            YOffset = aRect.y + ( (aRect.height - getVertHexHeight()) / 2);
        }
        else
        {
            _MainDimension = Height;
            while (getHorzHexWidth() > Width)
            {
                _MainDimension--;
            }

            XOffset = aRect.x + ( (aRect.width - getHorzHexWidth()) / 2);
            YOffset = aRect.y + ( (aRect.height - getHorzHexHeight()) / 2);
        }
        for (int Count = 0; Count < 6; Count++)
        {
            Point aPoint;
            if (Vertical)
            {
                aPoint = getVertPolyPoint(Count + 1);
            }
            else
            {
                aPoint = getHorzPolyPoint(Count + 1);
            }

            addPoint(aPoint.x + XOffset, aPoint.y + YOffset);
        }
        _Offsets = new Vector<Point>();
    }
    
    public int getMainDimension()
    {
    	return _MainDimension;
    }
    
    public boolean isVertical()
    {
    	return _Vertical;
    }

    public int getWidth()
    {
        if (_Vertical)
        {
            return getVertHexWidth();
        }
        else
        {
            return getHorzHexWidth();
        }
    }

    public int getHeight()
    {
        if (_Vertical)
        {
            return getVertHexHeight();
        }
        else
        {
            return getHorzHexHeight();
        }
    }

    public int getXIncrement()
    {
        if (_Vertical)
        {
            return getVertXIncrement();
        }
        else
        {
            return getHorzXIncrement();
        }
    }

    public int getYIncrement()
    {
        if (_Vertical)
        {
            return getVertYIncrement();
        }
        else
        {
            return getHorzYIncrement();
        }
    }

    void offset(Point aPoint)
    {
        for (int Count = 0; Count < 7; Count++)
        {
            xpoints[Count] += aPoint.x;
            ypoints[Count] += aPoint.y;
        }
    }

    void offset(int XOffset, int YOffset)
    {
        for (int Count = 0; Count < 7; Count++)
        {
            xpoints[Count] += XOffset;
            ypoints[Count] += YOffset;
        }
    }
    
	/**
	 * Returns true if this hex is intersected by the line
	 * 
	 * Changed arguments to int : DC
	 * @param x0
	 * 	The x value of the line start
	 * @param y0
	 * 	The y value of the line start
	 * @param x1
	 * 	The x value of the line end
	 * @param y1
	 * 	The y value of the line end
	 * @return true if the line intersects 
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
		if (y0 == y1)
		{
			if (ypoints[4] + 1 == y0 && ypoints[5] + 1 == y1)
			{
				return true; // if we do then return true
			}
		}
		return false;
	}

	/**
	 * From Megamek class IdealHex Tests whether a line intersects a point or
	 * the point passes to the left or right of the line.
	 * 
	 * Changed arguments to int : DC
	 * @param x0
	 * 	The x0 value
	 * @param y0
	 * 	The y0 value
	 * @param x1
	 * 	The x1 value
	 * @param y1
	 * 	The y1 value
	 * @param x2
	 * 	The x2 value
	 * @param y2
	 * 	The y2 value
	 * @return turns
	 */
	public static int turns(int x0, int y0, int x1, int y1, int x2, int y2)
	{
		final double cross = (x1 - x0) * (y2 - y0) - (x2 - x0) * (y1 - y0);
		return cross > 0 ? LEFT : cross < 0 ? RIGHT : STRAIGHT;
	}


    public Point getCenter()
    {
        Rectangle aRect = getBounds();
        Point aPoint = new Point(0, 0);
        if (aRect.width > 0)
        {
            aPoint.x = aRect.x + (aRect.width / 2);
        }
        if (aRect.height > 0)
        {
            aPoint.y = aRect.y + (aRect.height / 2);
        }
        return aPoint;
    }
    
    public Point getHexsideMidPoint(int side)
    {
        Rectangle aRect = getBounds();
    	Point retVal = null;
    	if (_Vertical)
    		retVal = getVertHexsideMidPoint(side);
    	else
    		retVal = getHorzHexsideMidPoint(side);
    	retVal.x += aRect.x;
    	retVal.y += aRect.y;
    	return retVal;
    }
    
    public Point getHexsideStartPoint(int side)
    {
    	if (side == 0)
    		side = 6;
    	
        Rectangle aRect = getBounds();
    	Point retVal = null;
    	if (_Vertical)
    		retVal = getVertPolyPoint(side);
    	else
    		retVal = getHorzPolyPoint(side);
    	retVal.x += aRect.x;
    	retVal.y += aRect.y;
    	return retVal;
    }

    public Point getHexsideEndPoint(int side)
    {
    	if (side == 6)
    		side = 0;
        Rectangle aRect = getBounds();
    	Point retVal = null;
    	if (_Vertical)
    		retVal = getVertPolyPoint(side + 1);
    	else
    		retVal = getHorzPolyPoint(side + 1);
    	retVal.x += aRect.x;
    	retVal.y += aRect.y;
    	return retVal;
    }
    
///////////////////////////////////////////////////////////////////////////////////////
// Horizontal Hex Routines

    private int getHorzHexWidth()
    {
        return (int) (2 * ( (double) _MainDimension) * Tan30);
    }

    private int getHorzHexHeight()
    {
        return _MainDimension;
    }

    private int getHorzYIncrement()
    {
        return _MainDimension / 2;
    }

    private int getHorzXIncrement()
    {
        return (int) (3.0 * ( (double) _MainDimension) / 2.0 * Tan30);
    }

    private Point getHorzPolyPoint(int PolyPoint)
    {
        Point aPoint = new Point(getHorzXPolyPoint(PolyPoint), getHorzYPolyPoint(PolyPoint));
        return aPoint;
    }

    private int getHorzYPolyPoint(int PolyPoint)
    {
        int YPoint = 0;
        if (PolyPoint >= 0 && PolyPoint < 13)
        {
            switch (PolyPoint)
            {
                case 0:
                    YPoint = _MainDimension / 2;
                    break;
                case 1:
                case 4:
                case 7:
                case 10:
                    YPoint = getHorzYIncrement();
                    break;
                case 2:
                case 3:
                    YPoint = _MainDimension;
                    break;
                case 5:
                case 6:
                    YPoint = 0;
                    break;
                case 8:
                case 9:
                    YPoint = (int) ( ( (double) _MainDimension) * 3.0 / 4.0);
                    break;
                case 11:
                case 12:
                    YPoint = (int) ( ( (double) _MainDimension) / 4.0);
                    break;
            }
        }
        return YPoint;
    }

    private int getHorzXPolyPoint(int PolyPoint)
    {
        int XPoint = 0;
        if (PolyPoint >= 0 && PolyPoint < 13)
        {
            switch (PolyPoint)
            {
                case 0:
                    XPoint = getHorzHexWidth() / 2;
                    if ( (getHorzHexWidth() % 2) != 0)
                    {
                        XPoint--;
                    }
                    break;
                case 1:
                    XPoint = 0;
                    break;
                case 2:
                case 6:
                case 7:
                    XPoint = (int) ( ( (double) _MainDimension) / 2 * Tan30);
                    break;
                case 3:
                case 5:
                case 10:
                    XPoint = (int) (3.0 * ( (double) _MainDimension) / 2.0 * Tan30);
                    break;
                case 4:
                    XPoint = getHorzHexWidth();
                    break;
                case 8:
                case 12:
                    XPoint = (int) ( ( (double) _MainDimension) / 2 * Tan30);
                    XPoint += (int) ( ( ( (double) _MainDimension) / 2.0) / 2 * Tan30);
                    break;
                case 9:
                case 11:
                    XPoint = (int) ( ( (double) _MainDimension) / 2 * Tan30);
                    XPoint += (int) (3.0 * ( ( (double) _MainDimension) / 2.0) / 2.0 * Tan30);
                    break;
            }
        }
        return XPoint;
    }

    private Point getHorzHexsideMidPoint(int Hexside)
    {
        Point retPoint = new Point();
        double grad = 0.0;
        double yInt = 0.0;
        double angle = 0;
        Point p1 = new Point(0, 0);
        Point p2 = new Point(0, 0);

        switch (Hexside)
        {
            case 1:
                p1 = getHorzPolyPoint(6);
                p2 = getHorzPolyPoint(5);
                angle = 0;
                break;
            case 2:
                p1 = getHorzPolyPoint(5);
                p2 = getHorzPolyPoint(4);
                angle = Math.PI / 3.0;
                break;
            case 3:
                p1 = getHorzPolyPoint(3);
                p2 = getHorzPolyPoint(4);
                angle = Math.PI / 3.0;
                break;
            case 4:
                p1 = getHorzPolyPoint(2);
                p2 = getHorzPolyPoint(3);
                angle = 0;
                break;
            case 5:
                p1 = getHorzPolyPoint(1);
                p2 = getHorzPolyPoint(2);
                angle = Math.PI / 3.0;
                break;
            case 6:
                p1 = getHorzPolyPoint(1);
                p2 = getHorzPolyPoint(6);
                angle = Math.PI / 3.0;
                break;
            default:
                // bad bad bad bad bad
        }
        grad = getGradient(p1, p2);
        yInt = getYIntercept(p1, p2);
        int LineLength = getLineLength(p1, p2);
        if (LineLength % 2 > 0)
        	LineLength = (LineLength / 2) + 1;
        else
        	LineLength = LineLength / 2;
        retPoint.x = (int) (Math.cos(angle) * LineLength) + p1.x;
        retPoint.y = (int) (grad * retPoint.x + yInt);
        return retPoint;
    }

///////////////////////////////////////////////////////////////////////////////////////
// Vertical Hex Routines

    private int getVertHexWidth()
    {
        return _MainDimension;
    }

    private int getVertHexHeight()
    {
        return (int) (2 * ( (double) _MainDimension) * Tan30);
    }

    private int getVertYIncrement()
    {
        return (int) (3.0 * ( (double) _MainDimension) / 2.0 * Tan30);
    }

    private int getVertXIncrement()
    {
        return _MainDimension / 2;
    }

    private Point getVertPolyPoint(int PolyPoint)
    {
        Point aPoint = new Point(getVertXPolyPoint(PolyPoint), getVertYPolyPoint(PolyPoint));
        return aPoint;
    }

    private int getVertYPolyPoint(int PolyPoint)
    {
        int YPoint = 0;
        if (PolyPoint >= 0 && PolyPoint < 13)
        {
            switch (PolyPoint)
            {
                case 0:
                    YPoint = getVertHexHeight() / 2;
                    if ( (getVertHexHeight() % 2) != 0)
                    {
                        YPoint--;
                    }
                    break;
                case 1:
                    YPoint = 0;
                    break;
                case 2:
                case 6:
                case 7:
                    YPoint = (int) ( ( (double) _MainDimension) / 2 * Tan30);
                    break;
                case 3:
                case 5:
                case 10:
                    YPoint = (int) (3.0 * ( (double) _MainDimension) / 2.0 * Tan30);
                    break;
                case 4:
                    YPoint = getVertHexHeight();
                    break;
                case 8:
                case 12:
                    YPoint = (int) ( ( (double) _MainDimension) / 2 * Tan30);
                    YPoint += (int) ( ( ( (double) _MainDimension) / 2.0) / 2 * Tan30);
                    break;
                case 9:
                case 11:
                    YPoint = (int) ( ( (double) _MainDimension) / 2 * Tan30);
                    YPoint += (int) (3.0 * ( ( (double) _MainDimension) / 2.0) / 2.0 * Tan30);
                    break;
            }
        }
        return YPoint;
    }

    private int getVertXPolyPoint(int PolyPoint)
    {
        int XPoint = 0;
        if (PolyPoint >= 0 && PolyPoint < 13)
        {
            switch (PolyPoint)
            {
                case 0:
                    XPoint = _MainDimension / 2;
                    break;
                case 1:
                case 4:
                case 7:
                case 10:
                    XPoint = getVertXIncrement();
                    break;
                case 2:
                case 3:
                    XPoint = _MainDimension;
                    break;
                case 5:
                case 6:
                    XPoint = 0;
                    break;
                case 8:
                case 9:
                    XPoint = (int) ( ( (double) _MainDimension) * 3.0 / 4.0);
                    break;
                case 11:
                case 12:
                    XPoint = (int) ( ( (double) _MainDimension) / 4.0);
                    break;
            }
        }
        return XPoint;
    }

    private Point getVertHexsideMidPoint(int Hexside)
    {
        Point retPoint = new Point(0, 0);
        double grad = 0.0;
        double yInt = 0.0;
        double angle = 0;
        Point p1 = new Point(0, 0);
        Point p2 = new Point(0, 0);

        switch (Hexside)
        {
            case 1:
                p1 = getVertPolyPoint(1);
                p2 = getVertPolyPoint(2);
                angle = Math.PI / 6.0;
                break;
            case 2:
                p1 = getVertPolyPoint(2);
                p2 = getVertPolyPoint(3);
                angle = Math.PI / 2.0;
                break;
            case 3:
                p1 = getVertPolyPoint(4);
                p2 = getVertPolyPoint(3);
                angle = Math.PI / 6.0;
                break;
            case 4:
                p1 = getVertPolyPoint(5);
                p2 = getVertPolyPoint(4);
                angle = Math.PI / 6.0;
                break;
            case 5:
                p1 = getVertPolyPoint(6);
                p2 = getVertPolyPoint(5);
                angle = Math.PI / 2.0;
                break;
            case 6:
                p1 = getVertPolyPoint(6);
                p2 = getVertPolyPoint(1);
                angle = Math.PI / 6.0;
                break;
            default:
        }
        grad = getGradient(p1, p2);
        yInt = getYIntercept(p1,p2);
        int LineLength = getLineLength(p1, p2);
        if (LineLength % 2 > 0)
        	LineLength = (LineLength / 2) + 1;
        else
        	LineLength = LineLength / 2;
        retPoint.x = (int) (Math.cos(angle) * LineLength) + p1.x;
        if (Double.isInfinite(grad))
        	retPoint.y = p1.y + LineLength;
        else
        	retPoint.y = (int) (grad * retPoint.x + yInt);
        return retPoint;
    }

    private double getGradient(Point p1, Point p2)
    {
        return (double) (p2.y - p1.y) / (double) (p2.x - p1.x);
    }

    private double getYIntercept(Point p1, Point p2)
    {
        return (double) p1.y - (p1.x * getGradient(p1,p2));
    }
    
    private int getLineLength(Point p1, Point p2)
    {
        int x = p2.x - p1.x;
        int y = p2.y - p1.y;
        return (int) Math.sqrt(x * x + y * y);
    }

    public void addOffset(Point Offset)
    {
        _Offsets.add(Offset);
    }

    public void addOffset(int XOffset, int YOffset)
    {
        Point Offset = new Point(XOffset, YOffset);
        _Offsets.add(Offset);
    }

    public void removeOffset(int Index)
    {
        if (Index < _Offsets.size())
        {
            _Offsets.remove(Index);
        }
    }

    public Point getOffset(int Index)
    {
        if (Index < _Offsets.size())
        {
            return (Point) _Offsets.get(Index);
        }

        return new Point();
    }

    public Hexagon getOffsetHex(int Index)
    {
        if (Index < _Offsets.size())
        {
            Hexagon OffsetHex = new Hexagon();
            OffsetHex = copy();

            OffsetHex.offset( (Point) _Offsets.get(Index));
            return OffsetHex;
        }
        return this;
    }

    public void draw(Graphics2D comp)
    {
        comp.drawPolygon(this);
    }

    public Hexagon copy()
    {
        Hexagon aHex = new Hexagon();

        aHex._Vertical = _Vertical;
        aHex._MainDimension = _MainDimension;

        for (int i = 0; i < npoints; i++)
        {
            aHex.xpoints[i] = xpoints[i];
            aHex.ypoints[i] = ypoints[i];
        }
        aHex.npoints = npoints;

        return aHex;
    }
}
