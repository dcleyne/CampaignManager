package bt.util;

/**
 * <p>Title: Battletech Mercenary Management</p>
 * <p>Description: Utility to manage mercenary units and manage contracts</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Daniel Cleyne
 * @version 0.1
 */

public class Dice
{
    public Dice()
    {
    }

    static public double random(double Level)
    {
        return Math.random() * Level;
    }

    static public int random(int Level, int Iterations)
    {
        if (! (Level > 0))
        {
            return 0;
        }

        int total = 0;
        for (; Iterations > 0; Iterations--)
        {
            total += (Math.random() * Level) + 1;
        }

        return total;
    }

    static public int random(int Level)
    {
        return random(Level, 1);
    }

    static public int d6(int Iterations)
    {
        return random(6, Iterations);
    }

    static public int d10(int Iterations)
    {
        return random(10, Iterations);
    }

    static public int d100(int Iterations)
    {
        return random(100, Iterations);
    }

    static public int Factorial(int Num, int Fact)
    {
        int result = 0;
        for (int Count = Fact; Count > 0; Count--)
        {
            result += Num * Count;
        }
        return result;
    }

    static public int HexRowContent(int Rows)
    {
        int result = 0;
        for (int Count = Rows; Count > 0; Count--)
        {
            result += Rows--;
        }
        return result;
    }
    
}
