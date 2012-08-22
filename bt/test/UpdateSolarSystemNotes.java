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


import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import bt.elements.galaxy.InnerSpherePlanet;
import bt.elements.galaxy.SolarSystemDetails;
import bt.managers.PlanetManager;
import bt.managers.SolarSystemManager;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;


public class UpdateSolarSystemNotes
{
    private static Log log = LogFactory.getLog(UpdateSolarSystemNotes.class);
    
    public static String Proxy = "auproxy";
    public static int ProxyPort = 3128;
    
	/**
	 * @param args
	 */
    
    public UpdateSolarSystemNotes()
    {
    	
    }
    
    @SuppressWarnings("unchecked")
    public void runTest()
    {
		try
		{
	        PropertyConfigurator.configure(Loader.getResource("bt/test/log4j.properties"));
	        PropertyUtil.loadSystemProperties("bt/system.properties");
	        
	        String Path = System.getProperty("DataPath");
	        if (Path == null)
	            Path = "data/";
	        
            SAXBuilder b = new SAXBuilder();
            Document SolarSystemDoc = b.build(new File(Path, "PlanetDetails.xml"));
            Element root = SolarSystemDoc.getRootElement();
            List<Element> planetElements = root.getChildren("Planet");
            for (Element planetElement: planetElements)
            {
            	int index = Integer.parseInt(planetElement.getChildText("Index"));
            	if (index > 72) index++;
            	
            	InnerSpherePlanet isp = PlanetManager.getPlanetFromID(index);
            	if (isp != null)
            	{
	            	log.info("Updating : " + isp.getSystem());
	            	
	            	SolarSystemDetails ssd = SolarSystemManager.getSystemDetails(isp);
	            	ssd.setNotes(planetElement.getChildText("Description"));
	            	SolarSystemManager.getInstance().saveSolarSystemDetails(index, isp.getSystem(), ssd, true);
            	}
            	else
            		log.info("Couldn't find : " + Integer.toString(index));
            }

	        log.info("Solar System Note Update Complete");
		}
		catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}
    }
    
    
	public static void main(String[] args)
	{
		UpdateSolarSystemNotes tpde = new UpdateSolarSystemNotes();
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
