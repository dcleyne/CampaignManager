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

import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;


public class TestPersonnelGeneration
{
    
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
	        PropertyUtil.loadSystemProperties("bt/system.properties");
	        
	        System.out.println("Starting Personnel Generation Test");
/*
	        Vector<Character> availableCharacters = UnitManager.getInstance().getAvailableCharacters(JobType.MECHWARRIOR);
	        for (Character c: availableCharacters)
	        	System.out.println(c.toString());
	        
	        Vector<Personnel> availablePersonnel = UnitManager.getInstance().getAvailablePersonnel(JobType.MECHWARRIOR);
	        for (Personnel p: availablePersonnel)
	        	System.out.println(p.toString());
	        
	        UnitManager.storeData();
*/	        
	        System.out.println("Personnel Generation Test Complete");
		}
		catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}

	}

}
