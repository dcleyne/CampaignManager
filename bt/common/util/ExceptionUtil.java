/**
 * Created on 8/12/2005
 * <p>Title: RenegadeLegion</p>
 *
 * <p>Copyright: Copyright Daniel Cleyne (c) 2004-2005</p>
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
package bt.common.util;

import java.io.*;

public class ExceptionUtil
{
	//Provides exception utility functions
    private ExceptionUtil()
    {
        
    }
    
    public static String getExceptionStackTrace(Exception e)
    {     
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }
}
