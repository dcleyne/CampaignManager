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
import bt.managers.RandomNameManager;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;

public class TestRandomNameExtraction
{
    private static Log log = LogFactory.getLog(TestRandomNameExtraction.class);
    
	/**
	 * @param args
	 */
    
    public TestRandomNameExtraction()
    {
    	
    }
    
    public void runTest()
    {
		try
		{
	        PropertyConfigurator.configure(Loader.getResource("bt/test/log4j.properties"));
	        PropertyUtil.loadSystemProperties("bt/system.properties");
	        
	        log.info("Random Name Extraction Started");
	        
	        log.info(RandomNameManager.getInstance().dumpRandomNames());
	        
	        log.info("Random Name Extraction Complete");
		}
		catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}
    }
    
    
	public static void main(String[] args)
	{
		TestRandomNameExtraction tpde = new TestRandomNameExtraction();
		tpde.runTest();
	}

	public class PlanetDetails
	{
	
		public String m_Name;
		public String m_ID;
		public boolean m_HasDescription;
		public boolean m_HasFactory;
		public String m_Description;
		public String m_Factory;
		
		public PlanetDetails(String name)
		{
			m_Name = name;
		}
	}
	
}
