package bt.common.util;

import java.awt.*;

/**
 * <p>Title: Battletech Mercenary Management</p>
 * <p>Description: Utility to manage mercenary units and manage contracts</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Daniel Cleyne
 * @version 0.1
 */

public class HexGrid extends Object
{

    public HexGrid()
    {
    }

    public HexGrid(int NumHexes, int GridWidth, int HexDimension, boolean Vertical, boolean Square)
    {
        //Not yet implemented
    }

    public HexGrid(int NumHexes, int GridWidth, Rectangle BoundBox, boolean Vertical, boolean Square)
    {
        Hexagon aHex = new Hexagon(BoundBox, Vertical);
        int HexWidth = aHex.GetWidth();
        int HexHeight = aHex.GetHeight();

        if (Vertical)
        {
            HexWidth = BoundBox.width / GridWidth;
            if (Square)
            {
                BuildVerticalSquareGrid(BoundBox.x, BoundBox.y, NumHexes, HexWidth, GridWidth);
            }
            else
            {
                BuildVerticalHexGrid(BoundBox, NumHexes, HexWidth, GridWidth);
            }
        }
        else
        {
            HexHeight = HexHeight / GridWidth;
            if (Square)
            {
                BuildHorizontalSquareGrid(BoundBox.x, BoundBox.y, NumHexes, HexHeight, GridWidth);
            }
            else
            {
                BuildHorizontalHexGrid(BoundBox.x, BoundBox.y, NumHexes, HexHeight, GridWidth);
            }
        }
        m_GridWidth = GridWidth;
        m_SquareGrid = Square;
    }

    public Hexagon GetHex(int HexNo)
    {
        return m_HexList[HexNo];
    }

    protected Hexagon[] m_HexList;
    protected int m_GridWidth;
    protected boolean m_SquareGrid;

    protected void BuildVerticalSquareGrid(int XOff, int YOff, int NumHexes, int Width, int GridWidth)
    {
        //Not yet implemented
    }

    protected void BuildVerticalHexGrid(Rectangle BoundBox, int NumHexes, int Width, int GridWidth)
    {
        if (NumHexes == -1)
        {
            NumHexes = Dice.Factorial(6, GridWidth / 2) + 1;
        }

        m_HexList = new Hexagon[NumHexes];

        Hexagon aHex = new Hexagon(0, 0, Width, true);
        int HexHeight = aHex.GetHeight();
        int HexXInc = aHex.GetXIncrement();
        int HexYInc = aHex.GetYIncrement();

        int XOffset = BoundBox.x + ( (GridWidth / 2) * HexXInc);
        XOffset += ( (BoundBox.width - (HexXInc * 2 * GridWidth)) / 2);

        int YOffset = BoundBox.y;
        YOffset += ( (BoundBox.height - (HexHeight + (HexYInc * (GridWidth - 1)))) / 2);

        int CurrentWidth = (GridWidth / 2) + 1;
        int CurrentHex = 0;

        for (int Count = 0; Count < GridWidth; Count++)
        {
            for (int InCount = 0; InCount < CurrentWidth; InCount++)
            {
                Hexagon newHex = new Hexagon(XOffset + (InCount * HexXInc * 2), YOffset, Width, true);
                m_HexList[CurrentHex] = newHex;
                CurrentHex++;
            }
            if (Count >= GridWidth / 2)
            {
                XOffset += HexXInc;
                CurrentWidth--;
            }
            else
            {
                XOffset -= HexXInc;
                CurrentWidth++;
            }
            YOffset += HexYInc;
        }
    }

    protected void BuildHorizontalSquareGrid(int XOff, int YOff, int NumHexes, int Height, int GridWidth)
    {
        //Not yet implemented
    }

    protected void BuildHorizontalHexGrid(int XOff, int YOff, int NumHexes, int Height, int GridWidth)
    {
        //Not yet implemented
    }
}
