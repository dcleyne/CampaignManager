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
package bt.test;

import bt.managers.MissionManager;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;


public class TestBattleValueCalculations
{
    
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
	        PropertyUtil.loadSystemProperties("bt/system.properties");

	        int numUnits = 4;
	        int battleValue = 2500;
	        
	        System.out.println("Calculating opposing force size for force of " + Integer.toString(numUnits) + " mechs at " + Integer.toString(battleValue) + " BV total");
	        for (int i = numUnits - 1; i < numUnits + 5; i++)
	        {
	        	int BV = MissionManager.getInstance().getFinalBattleValue(numUnits, battleValue, i);
	        	System.out.println(Integer.toString(i) + " Mechs -> " + Integer.toString(BV) + " BV total");
	        }
		}
		catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}

	}

}
