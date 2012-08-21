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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import bt.common.elements.galaxy.InnerSpherePlanet;
import bt.common.managers.PlanetManager;
import bt.common.util.ExceptionUtil;
import bt.common.util.PropertyUtil;
import bt.common.util.WebFile;


public class TestPlanetDetailExtraction

{
    private static final long serialVersionUID = 1;
    private static Log log = LogFactory.getLog(TestPlanetDetailExtraction.class);
    
    public static String Proxy = "auproxy";
    public static int ProxyPort = 3128;
    
	/**
	 * @param args
	 */
    
    public TestPlanetDetailExtraction()
    {
    	
    }
    
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
	        if (f.exists())
	        	f.delete();
	        
            Document planetDoc = new Document();
            
            Element root = new Element("Planets"); 
            planetDoc.setRootElement(root);
	        
	        
	        log.info("Starting Personnel Generation Test");

	        HashMap<String,PlanetDetails> hashDetails = new HashMap<String,PlanetDetails>(); 
	        int planetCount = PlanetManager.getPlanetCount();
	        for (int i = 0; i < planetCount; i++)
	        {
	        	InnerSpherePlanet isp = PlanetManager.getPlanet(i);
	        	PlanetDetails pd = new PlanetDetails(isp.getSystem());
	        	hashDetails.put(isp.getSystem(), pd);
	        }
	        
	        for (char letter = 'A'; letter <= 'Z'; letter++)
	        {
	        	int startValue = 0;
	        	boolean searchRemaining = true;
	        	while (searchRemaining)
	        	{
	        		searchRemaining = false;
	        		String url = "http://isatlas.teamspam.net/planet.php?func=browsebyletter&searchvalue=" + letter + "&start=" + Integer.toString(startValue);
		        	String html = WebFile.getWebPageContent(url,Proxy,ProxyPort);
		        	
		        	int startIndex = html.indexOf("<table ");
		        	if (startIndex != -1)
		        	{
		        		int endIndex = html.indexOf("</table",startIndex);
		        		int startRowIndex = html.indexOf("<tr",startIndex);
		        		while (startRowIndex != -1)
		        		{
		        			int endRowIndex = html.indexOf("</tr",startRowIndex);
		        			String row = html.substring(startRowIndex,endRowIndex);
		        			row = row.replace("\n","");		        			
		        			String[] cells = row.split("</td>");
		        			
		        			if (cells.length > 1)
		        			{
		    	        		searchRemaining = true;
			        			
			        			String name = cells[0].substring(cells[0].indexOf(">",9) + 1,cells[0].indexOf("</a"));
		        				System.out.println("Grabbing summary for : " + name);
			        			PlanetDetails pd = hashDetails.get(name);
			        			if (pd != null)
			        			{
			        			
			        				pd.m_ID = cells[0].substring(cells[0].indexOf("planet=") + 7,cells[0].indexOf("\">"));			        				
				        			if (cells.length > 3)
				        			{
				        				pd.m_HasDescription = cells[3].indexOf("Description") != -1;
				        				pd.m_HasFactory = cells[3].indexOf("Factory") != -1;
				        			}
			        			}
		        			}
		        			
			        		startRowIndex = html.indexOf("<tr",startRowIndex + 1);
			        		if (startRowIndex > endIndex)
			        			break;
		        		}
		        	}
		        	startValue += 25;		        	
	        	}
	        }
	        
	        
	        
	        for (int i = 0; i < planetCount; i++)
	        {
            	org.jdom.Element e = new org.jdom.Element("Planet");
	        	InnerSpherePlanet isp = PlanetManager.getPlanet(i);
	        	
    			PlanetDetails pd = hashDetails.get(isp.getSystem());
    			if (pd != null)
    			{
    				if (pd.m_HasDescription)
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
		TestPlanetDetailExtraction tpde = new TestPlanetDetailExtraction();
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
