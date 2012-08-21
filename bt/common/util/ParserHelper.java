package bt.common.util;

/**
 * <p>Title: Battletech Mercenary Management</p>
 * <p>Description: Utility to manage mercenary units and manage contracts</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Daniel Cleyne
 * @version 0.1
 */

public class ParserHelper
{
    public ParserHelper()
    {
    }

    static public String GetField(String Line, int index, String sep)
    {
        String Field;
        int pos = 0;
        int start = 0;
        int i = 0;
        for (i = 0; i < index; i++)
        {
            if (pos == -1)
            {
                break;
            }
            start = pos;
            pos = Line.indexOf(sep, pos + 1);
        }
        if (index > 1)
        {
            start++;
        }
        if (i < index)
        {
            pos = start; //If we hit then end of the string before we wanted to, then return an empty string
        }

        if (pos > -1)
        {
            Field = Line.substring(start, pos);
        }
        else
        {
            Field = Line.substring(start);
        }

        return Field.trim();
    }

}
