/**
 * Created on 05/04/2007
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
package test;


import bt.managers.PasswordManager;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;


public class TestPasswordGeneration
{
    
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
	        PropertyUtil.loadSystemProperties("bt/system.properties");

        	String password = "decker01";
        	String hash = PasswordManager.encrypt(password);
        	System.out.println("hash for password '" + password + "' -> " + hash);

	        for (int i = 0; i < 10; i++)
	        {
	        	password = "Password " + Integer.toString(i);
	        	hash = PasswordManager.encrypt(password);
	        	System.out.println("hash for password '" + password + "' -> " + hash);
	        }
		}
		catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}

	}

}
