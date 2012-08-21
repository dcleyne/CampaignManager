package bt.common.util;

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
	
    public static final double Tan30 = Math.tan(Math.PI / 6.0);

    protected boolean m_Vertical;
    protected int m_MainDimension;
    protected Vector<Point> m_Offsets;

    public Hexagon()
    {
        m_Vertical = false;
        m_MainDimension = 0;
        for (int Count = 0; Count < 6; Count++)
        {
            addPoint(0, 0);
        }

        m_Offsets = new Vector<Point>();
    }

    public Hexagon(int XOffset, int YOffset, int MainDimension, boolean Vertical)
    {
        m_Vertical = Vertical;
        m_MainDimension = MainDimension;
        for (int Count = 0; Count < 6; Count++)
        {
            Point aPoint;
            if (Vertical)
            {
                aPoint = GetVertPolyPoint(Count + 1);
            }
            else
            {
                aPoint = GetHorzPolyPoint(Count + 1);
            }

            addPoint(aPoint.x + XOffset, aPoint.y + YOffset);
        }
        m_Offsets = new Vector<Point>();
    }

    public Hexagon(Rectangle aRect, boolean Vertical)
    {
        m_Vertical = Vertical;
        int Width = aRect.width;
        int Height = aRect.height;
        int XOffset = 0;
        int YOffset = 0;

        if (m_Vertical)
        {
            m_MainDimension = Width;
            while (GetVertHexHeight() > Height)
            {
                m_MainDimension--;
            }

            XOffset = aRect.x + ( (aRect.width - GetVertHexWidth()) / 2);
            YOffset = aRect.y + ( (aRect.height - GetVertHexHeight()) / 2);
        }
        else
        {
            m_MainDimension = Height;
            while (GetHorzHexWidth() > Width)
            {
                m_MainDimension--;
            }

            XOffset = aRect.x + ( (aRect.width - GetHorzHexWidth()) / 2);
            YOffset = aRect.y + ( (aRect.height - GetHorzHexHeight()) / 2);
        }
        for (int Count = 0; Count < 6; Count++)
        {
            Point aPoint;
            if (Vertical)
            {
                aPoint = GetVertPolyPoint(Count + 1);
            }
            else
            {
                aPoint = GetHorzPolyPoint(Count + 1);
            }

            addPoint(aPoint.x + XOffset, aPoint.y + YOffset);
        }
        m_Offsets = new Vector<Point>();
    }

    public int GetWidth()
    {
        if (m_Vertical)
        {
            return GetVertHexWidth();
        }
        else
        {
            return GetHorzHexWidth();
        }
    }

    public int GetHeight()
    {
        if (m_Vertical)
        {
            return GetVertHexHeight();
        }
        else
        {
            return GetHorzHexHeight();
        }
    }

    public int GetXIncrement()
    {
        if (m_Vertical)
        {
            return GetVertXIncrement();
        }
        else
        {
            return GetHorzXIncrement();
        }
    }

    public int GetYIncrement()
    {
        if (m_Vertical)
        {
            return GetVertYIncrement();
        }
        else
        {
            return GetHorzYIncrement();
        }
    }

    void Offset(Point aPoint)
    {
        for (int Count = 0; Count < 7; Count++)
        {
            xpoints[Count] += aPoint.x;
            ypoints[Count] += aPoint.y;
        }
    }

    void Offset(int XOffset, int YOffset)
    {
        for (int Count = 0; Count < 7; Count++)
        {
            xpoints[Count] += XOffset;
            ypoints[Count] += YOffset;
        }
    }

    public Point GetCenter()
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
    	if (m_Vertical)
    		retVal = GetVertHexsideMidPoint(side);
    	else
    		retVal = GetHorzHexsideMidPoint(side);
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
    	if (m_Vertical)
    		retVal = GetVertPolyPoint(side);
    	else
    		retVal = GetHorzPolyPoint(side);
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
    	if (m_Vertical)
    		retVal = GetVertPolyPoint(side + 1);
    	else
    		retVal = GetHorzPolyPoint(side + 1);
    	retVal.x += aRect.x;
    	retVal.y += aRect.y;
    	return retVal;
    }
    
///////////////////////////////////////////////////////////////////////////////////////
// Horizontal Hex Routines

    private int GetHorzHexWidth()
    {
        return (int) (2 * ( (double) m_MainDimension) * Tan30);
    }

    private int GetHorzHexHeight()
    {
        return m_MainDimension;
    }

    private int GetHorzYIncrement()
    {
        return m_MainDimension / 2;
    }

    private int GetHorzXIncrement()
    {
        return (int) (3.0 * ( (double) m_MainDimension) / 2.0 * Tan30);
    }

    private Point GetHorzPolyPoint(int PolyPoint)
    {
        Point aPoint = new Point(GetHorzXPolyPoint(PolyPoint), GetHorzYPolyPoint(PolyPoint));
        return aPoint;
    }

    private int GetHorzYPolyPoint(int PolyPoint)
    {
        int YPoint = 0;
        if (PolyPoint >= 0 && PolyPoint < 13)
        {
            switch (PolyPoint)
            {
                case 0:
                    YPoint = m_MainDimension / 2;
                    break;
                case 1:
                case 4:
                case 7:
                case 10:
                    YPoint = GetHorzYIncrement();
                    break;
                case 2:
                case 3:
                    YPoint = m_MainDimension;
                    break;
                case 5:
                case 6:
                    YPoint = 0;
                    break;
                case 8:
                case 9:
                    YPoint = (int) ( ( (double) m_MainDimension) * 3.0 / 4.0);
                    break;
                case 11:
                case 12:
                    YPoint = (int) ( ( (double) m_MainDimension) / 4.0);
                    break;
            }
        }
        return YPoint;
    }

    private int GetHorzXPolyPoint(int PolyPoint)
    {
        int XPoint = 0;
        if (PolyPoint >= 0 && PolyPoint < 13)
        {
            switch (PolyPoint)
            {
                case 0:
                    XPoint = GetHorzHexWidth() / 2;
                    if ( (GetHorzHexWidth() % 2) != 0)
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
                    XPoint = (int) ( ( (double) m_MainDimension) / 2 * Tan30);
                    break;
                case 3:
                case 5:
                case 10:
                    XPoint = (int) (3.0 * ( (double) m_MainDimension) / 2.0 * Tan30);
                    break;
                case 4:
                    XPoint = GetHorzHexWidth();
                    break;
                case 8:
                case 12:
                    XPoint = (int) ( ( (double) m_MainDimension) / 2 * Tan30);
                    XPoint += (int) ( ( ( (double) m_MainDimension) / 2.0) / 2 * Tan30);
                    break;
                case 9:
                case 11:
                    XPoint = (int) ( ( (double) m_MainDimension) / 2 * Tan30);
                    XPoint += (int) (3.0 * ( ( (double) m_MainDimension) / 2.0) / 2.0 * Tan30);
                    break;
            }
        }
        return XPoint;
    }

    private Point GetHorzHexsideMidPoint(int Hexside)
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
                p1 = GetHorzPolyPoint(6);
                p2 = GetHorzPolyPoint(5);
                angle = 0;
                break;
            case 2:
                p1 = GetHorzPolyPoint(5);
                p2 = GetHorzPolyPoint(4);
                angle = Math.PI / 3.0;
                break;
            case 3:
                p1 = GetHorzPolyPoint(3);
                p2 = GetHorzPolyPoint(4);
                angle = Math.PI / 3.0;
                break;
            case 4:
                p1 = GetHorzPolyPoint(2);
                p2 = GetHorzPolyPoint(3);
                angle = 0;
                break;
            case 5:
                p1 = GetHorzPolyPoint(1);
                p2 = GetHorzPolyPoint(2);
                angle = Math.PI / 3.0;
                break;
            case 6:
                p1 = GetHorzPolyPoint(1);
                p2 = GetHorzPolyPoint(6);
                angle = Math.PI / 3.0;
                break;
            default:
                // bad bad bad bad bad
        }
        grad = GetGradient(p1, p2);
        yInt = GetYIntercept(p1, p2);
        int LineLength = GetLineLength(p1, p2);
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

    private int GetVertHexWidth()
    {
        return m_MainDimension;
    }

    private int GetVertHexHeight()
    {
        return (int) (2 * ( (double) m_MainDimension) * Tan30);
    }

    private int GetVertYIncrement()
    {
        return (int) (3.0 * ( (double) m_MainDimension) / 2.0 * Tan30);
    }

    private int GetVertXIncrement()
    {
        return m_MainDimension / 2;
    }

    private Point GetVertPolyPoint(int PolyPoint)
    {
        Point aPoint = new Point(GetVertXPolyPoint(PolyPoint), GetVertYPolyPoint(PolyPoint));
        return aPoint;
    }

    private int GetVertYPolyPoint(int PolyPoint)
    {
        int YPoint = 0;
        if (PolyPoint >= 0 && PolyPoint < 13)
        {
            switch (PolyPoint)
            {
                case 0:
                    YPoint = GetVertHexHeight() / 2;
                    if ( (GetVertHexHeight() % 2) != 0)
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
                    YPoint = (int) ( ( (double) m_MainDimension) / 2 * Tan30);
                    break;
                case 3:
                case 5:
                case 10:
                    YPoint = (int) (3.0 * ( (double) m_MainDimension) / 2.0 * Tan30);
                    break;
                case 4:
                    YPoint = GetVertHexHeight();
                    break;
                case 8:
                case 12:
                    YPoint = (int) ( ( (double) m_MainDimension) / 2 * Tan30);
                    YPoint += (int) ( ( ( (double) m_MainDimension) / 2.0) / 2 * Tan30);
                    break;
                case 9:
                case 11:
                    YPoint = (int) ( ( (double) m_MainDimension) / 2 * Tan30);
                    YPoint += (int) (3.0 * ( ( (double) m_MainDimension) / 2.0) / 2.0 * Tan30);
                    break;
            }
        }
        return YPoint;
    }

    private int GetVertXPolyPoint(int PolyPoint)
    {
        int XPoint = 0;
        if (PolyPoint >= 0 && PolyPoint < 13)
        {
            switch (PolyPoint)
            {
                case 0:
                    XPoint = m_MainDimension / 2;
                    break;
                case 1:
                case 4:
                case 7:
                case 10:
                    XPoint = GetVertXIncrement();
                    break;
                case 2:
                case 3:
                    XPoint = m_MainDimension;
                    break;
                case 5:
                case 6:
                    XPoint = 0;
                    break;
                case 8:
                case 9:
                    XPoint = (int) ( ( (double) m_MainDimension) * 3.0 / 4.0);
                    break;
                case 11:
                case 12:
                    XPoint = (int) ( ( (double) m_MainDimension) / 4.0);
                    break;
            }
        }
        return XPoint;
    }

    private Point GetVertHexsideMidPoint(int Hexside)
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
                p1 = GetVertPolyPoint(1);
                p2 = GetVertPolyPoint(2);
                angle = Math.PI / 6.0;
                break;
            case 2:
                p1 = GetVertPolyPoint(2);
                p2 = GetVertPolyPoint(3);
                angle = Math.PI / 2.0;
                break;
            case 3:
                p1 = GetVertPolyPoint(4);
                p2 = GetVertPolyPoint(3);
                angle = Math.PI / 6.0;
                break;
            case 4:
                p1 = GetVertPolyPoint(5);
                p2 = GetVertPolyPoint(4);
                angle = Math.PI / 6.0;
                break;
            case 5:
                p1 = GetVertPolyPoint(6);
                p2 = GetVertPolyPoint(5);
                angle = Math.PI / 2.0;
                break;
            case 6:
                p1 = GetVertPolyPoint(6);
                p2 = GetVertPolyPoint(1);
                angle = Math.PI / 6.0;
                break;
            default:
        }
        grad = GetGradient(p1, p2);
        yInt = GetYIntercept(p1,p2);
        int LineLength = GetLineLength(p1, p2);
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

    private double GetGradient(Point p1, Point p2)
    {
        return (double) (p2.y - p1.y) / (double) (p2.x - p1.x);
    }

    private double GetYIntercept(Point p1, Point p2)
    {
        return (double) p1.y - (p1.x * GetGradient(p1,p2));
    }
    
    private int GetLineLength(Point p1, Point p2)
    {
        int x = p2.x - p1.x;
        int y = p2.y - p1.y;
        return (int) Math.sqrt(x * x + y * y);
    }

    public void AddOffset(Point Offset)
    {
        m_Offsets.add(Offset);
    }

    public void AddOffset(int XOffset, int YOffset)
    {
        Point Offset = new Point(XOffset, YOffset);
        m_Offsets.add(Offset);
    }

    public void RemoveOffset(int Index)
    {
        if (Index < m_Offsets.size())
        {
            m_Offsets.remove(Index);
        }
    }

    public Point GetOffset(int Index)
    {
        if (Index < m_Offsets.size())
        {
            return (Point) m_Offsets.get(Index);
        }

        return new Point();
    }

    public Hexagon GetOffsetHex(int Index)
    {
        if (Index < m_Offsets.size())
        {
            Hexagon OffsetHex = new Hexagon();
            OffsetHex = copy();

            OffsetHex.Offset( (Point) m_Offsets.get(Index));
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

        aHex.m_Vertical = m_Vertical;
        aHex.m_MainDimension = m_MainDimension;

        for (int i = 0; i < npoints; i++)
        {
            aHex.xpoints[i] = xpoints[i];
            aHex.ypoints[i] = ypoints[i];
        }
        aHex.npoints = npoints;

        return aHex;
    }
}
