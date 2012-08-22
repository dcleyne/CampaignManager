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
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import bt.elements.galaxy.InnerSpherePlanet;
import bt.managers.PlanetManager;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;
import bt.util.WebFile;


public class ExtractPlanetDetailsFromISAtlas

{
    private static Log log = LogFactory.getLog(ExtractPlanetDetailsFromISAtlas.class);
    
    public static String Proxy = "auproxy";
    public static int ProxyPort = 3128;
    
	/**
	 * @param args
	 */
    
    public ExtractPlanetDetailsFromISAtlas()
    {
    	
    }

    @SuppressWarnings("unchecked")
    public void runTest()
    {
		try
		{
	        PropertyConfigurator.configure(Loader.getResource("bt/test/log4j.properties"));
	        PropertyUtil.loadSystemProperties("bt/common/system.properties");
	        
	        String Path = System.getProperty("DataPath");
	        if (Path == null)
	            Path = "data/";
	        
	        File f = new File(Path + "PlanetDetails.xml");
	        
	        log.info("Starting Personnel Generation Test");

	        HashMap<String,PlanetDetails> hashDetails = new HashMap<String,PlanetDetails>(); 
	        int planetCount = PlanetManager.getPlanetCount();
	        for (int i = 0; i < planetCount; i++)
	        {
	        	InnerSpherePlanet isp = PlanetManager.getPlanet(i);
	        	PlanetDetails pd = new PlanetDetails(isp.getSystem());
	        	hashDetails.put(isp.getSystem(), pd);
	        }
	        
            SAXBuilder b = new SAXBuilder();
            Document SolarSystemDoc = b.build(new File(Path, "PlanetDetails.xml"));
            Element root = SolarSystemDoc.getRootElement();
            List<Element> planetElements = root.getChildren("Planet");
            for (Element planetElement: planetElements)
            {
    			PlanetDetails pd = hashDetails.get(planetElement.getChildText("Name"));

    			if (pd != null)
    			{
    				pd.m_HasDescription = Boolean.parseBoolean(planetElement.getChildText("HasDescription"));
    				pd.m_HasFactory = Boolean.parseBoolean(planetElement.getChildText("HasFactory"));
    				pd.m_Description = planetElement.getChildText("Description");    				
    				pd.m_Description = pd.m_Description.replace('\uFFFD','\'');
    				
    				pd.m_Factory = planetElement.getChildText("Factory");
    				pd.m_ID = planetElement.getChildText("ID");
    			}
            }
            b = null;
	        
	        
            Document planetDoc = new Document();
            
            root = new Element("Planets"); 
            planetDoc.setRootElement(root);

            for (int i = 0; i < planetCount; i++)
	        {
            	org.jdom.Element e = new org.jdom.Element("Planet");
	        	InnerSpherePlanet isp = PlanetManager.getPlanet(i);
	        	
    			PlanetDetails pd = hashDetails.get(isp.getSystem());
    			if (pd != null)
    			{
    				if (pd.m_HasDescription && pd.m_Description.isEmpty())
    				{
        				System.out.println("Grabbing description for : " + pd.m_Name);
    	        		String url = "http://isatlas.teamspam.net/planet-detail.php?planet=" + pd.m_ID;
    		        	String html = WebFile.getWebPageContent(url,Proxy,ProxyPort);
    		        	int startIndex = html.indexOf("<h2>Description:</h2>");
    		        	int endIndex = html.indexOf("</blockquote>");
    		        	if (startIndex != -1 && endIndex > startIndex)
    		        	{
	    		        	pd.m_Description = html.substring(startIndex + 22, endIndex);
	    		        	pd.m_Description = pd.m_Description.replace("\n", "");
	    		        	pd.m_Description = pd.m_Description.replace("<p>", " ");
	    		        	pd.m_Description = pd.m_Description.replace("</p>", "");    					
	    		        	pd.m_Description = pd.m_Description.replace("<blockquote>", "");

    		        	}
    		        	else
    		        		System.out.println(html);
    				}
    				
		        	e.addContent(new org.jdom.Element("Index").setText(Integer.toString(i+1)));
		        	e.addContent(new org.jdom.Element("Name").setText(pd.m_Name ));
		        	e.addContent(new org.jdom.Element("ID").setText(pd.m_ID ));
		        	e.addContent(new org.jdom.Element("HasDescription").setText(Boolean.toString(pd.m_HasDescription)));
		        	e.addContent(new org.jdom.Element("HasFactory").setText(Boolean.toString(pd.m_HasFactory)));
		        	e.addContent(new org.jdom.Element("Description").setText(pd.m_Description ));
		        	e.addContent(new org.jdom.Element("Factory").setText(pd.m_Factory ));
    						        	
		        	root.addContent(e);
    			}
	        }
	        
            XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());            
            out.output(planetDoc, new FileOutputStream(f));
            
	        log.info("Planet Detail Extraction Complete");
		}
		catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}
    }
    
    
	public static void main(String[] args)
	{
		ExtractPlanetDetailsFromISAtlas tpde = new ExtractPlanetDetailsFromISAtlas();
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
