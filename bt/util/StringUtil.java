/**
 * Created on Dec 2, 2007
 * <p>Title: Legatus</p>
 *
 * <p>Copyright: Copyright Daniel Cleyne (c) 2004-2007</p>
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
package bt.util;

public class StringUtil
{
	public static String padString(String str, int len)
	{
		return padString(str,len," ");
	}	
	
	public static String padString(String str, int len, String padStr)
	{
		StringBuilder builder = new StringBuilder(str);
		builder.ensureCapacity(len);
		
		while (builder.length() < len)
			builder.append(padStr);


		if (builder.length() > len) 
			return builder.substring(0, len - 1);
		else
			return builder.toString();		
	}
}
