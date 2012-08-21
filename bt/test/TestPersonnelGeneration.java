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


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

import bt.common.util.ExceptionUtil;
import bt.common.util.PropertyUtil;


public class TestPersonnelGeneration

{
    private static final long serialVersionUID = 1;
    private static Log log = LogFactory.getLog(TestPersonnelGeneration.class);
    
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
	        PropertyConfigurator.configure(Loader.getResource("bt/test/log4j.properties"));
	        PropertyUtil.loadSystemProperties("bt/common/system.properties");
	        
	        log.info("Starting Personnel Generation Test");
/*
	        Vector<Character> availableCharacters = UnitManager.getInstance().getAvailableCharacters(JobType.MECHWARRIOR);
	        for (Character c: availableCharacters)
	        	System.out.println(c.toString());
	        
	        Vector<Personnel> availablePersonnel = UnitManager.getInstance().getAvailablePersonnel(JobType.MECHWARRIOR);
	        for (Personnel p: availablePersonnel)
	        	System.out.println(p.toString());
	        
	        UnitManager.storeData();
*/	        
	        log.info("Personnel Generation Test Complete");
		}
		catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}

	}

}
