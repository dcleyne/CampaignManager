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


import java.io.File;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import bt.elements.galaxy.InnerSpherePlanet;
import bt.elements.galaxy.SolarSystemDetails;
import bt.managers.PlanetManager;
import bt.managers.SolarSystemManager;
import bt.mapping.PlanetMapGenerator;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;


public class PlanetMapGenerationTest
{
	/**
	 * @param args
	 */
    @SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
        try
        {
        	System.out.println("Generating Planets");
            
            boolean doAll = true;
            
            PropertyUtil.loadSystemProperties("bt/system.properties");
	        
	        String Path = System.getProperty("DataPath");
	        if (Path == null)
	            Path = "data/";
	        
	        HashMap<Integer, String> planetNotes = new HashMap<Integer,String>();
            SAXBuilder b = new SAXBuilder();
            Document SolarSystemDoc = b.build(new File(Path, "PlanetDetails.xml"));
            Element root = SolarSystemDoc.getRootElement();
            List<Element> planetElements = root.getChildren("Planet");
            for (Element planetElement: planetElements)
            {
            	int index = Integer.parseInt(planetElement.getChildText("Index"));
            	if (index > 72) index++;
            	planetNotes.put(index,planetElement.getChildText("Description"));
            }            
            
            if (doAll)
            {
	        	for (int i = 0; i < PlanetManager.getPlanetCount(); i++)
	        	{
		        	InnerSpherePlanet isp = PlanetManager.getPlanet(i);
		        	SolarSystemDetails ssd = SolarSystemManager.getSystemDetails(isp);
		        	ssd.setNotes(planetNotes.get(i + 1));
		        	
		        	PlanetMapGenerator pmg = new PlanetMapGenerator();
		        	
		        	boolean generateTerrain = !ssd.isMapGenerated();
		        	boolean generateSettlements = ssd.getSettlementCount() == 0;
		        	boolean generateRoads = generateSettlements;
		        	
		        	pmg.generatePlanetMap(isp, ssd, generateTerrain, generateSettlements, generateRoads, true, true);	
		        	pmg.generatePlanetMapImage(isp, ssd, false);
	        	}
            }
            else
            {
            	int index = 2050;
	        	InnerSpherePlanet isp = PlanetManager.getPlanet(index);
	        	SolarSystemDetails ssd = SolarSystemManager.getSystemDetails(isp);
	        	ssd.setNotes(planetNotes.get(index + 1));
	        	
	        	PlanetMapGenerator pmg = new PlanetMapGenerator();
	        	pmg.generatePlanetMap(isp, ssd, false, true, true, true, true);

	        	pmg.generatePlanetMapImage(isp, ssd, true);
            }

        }
        catch (Exception e)
        {
        	System.out.println(ExceptionUtil.getExceptionStackTrace(e));
        }
        System.out.println("Generating Planets Complete");

	}

}
