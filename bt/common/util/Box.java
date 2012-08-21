package bt.common.util;

/**
 * Title:        Space Conquest Game
 * Description:  The same old Game
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author Daniel Cleyne
 * @version 1.0
 */

public class Box
{
    int m_x1;
    int m_x2;
    int m_y1;
    int m_y2;
    int m_z1;
    int m_z2;

    public Box(int x, int x_width, int y, int y_height, int z, int z_depth)
    {
        m_x1 = x;
        m_x2 = x + x_width;
        m_y1 = y;
        m_y2 = y + y_height;
        m_z1 = z;
        m_z2 = z + z_depth;
    }

    public int getX1()
    {
        return m_x1;
    }

    public int getX2()
    {
        return m_x2;
    }

    public int getY1()
    {
        return m_y1;
    }

    public int getY2()
    {
        return m_y2;
    }

    public int getZ1()
    {
        return m_z1;
    }

    public int getZ2()
    {
        return m_z2;
    }

    public int getWidth()
    {
        return m_x2 - m_x1;
    }

    public int getHeight()
    {
        return m_y2 - m_y1;
    }

    public int getDepth()
    {
        return m_z2 - m_z1;
    }

    public String toString()
    {
        String s = new String("X(");
        s += m_x1 + "," + m_x2 + "), Y(";
        s += m_y1 + "," + m_y2 + "), Z(";
        s += m_z1 + "," + m_z2 + ")";

        return s;
    }

}
