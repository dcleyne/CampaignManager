package bt.server.managers;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import bt.common.elements.galaxy.InnerSpherePlanet;
import bt.common.elements.galaxy.SolarSystemDetails;
import bt.common.elements.galaxy.StarType;
import bt.common.managers.PlanetManager;
import bt.common.util.Dice;
import bt.common.util.ExceptionUtil;

/**
 * <p>Title: Miradan Phedd</p>
 *
 * <p>Description: Library for all Miradan related work</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Miradan Phedd</p>
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
public class SolarSystemManager
{
    private static Log log = LogFactory.getLog(SolarSystemManager.class);
	
    private static SolarSystemManager theInstance;

    protected SolarSystemManager()
    {
   		log.info("Initialising SolarSystemManager");
        checkPlanetDetailStore();
        //CheckRoutes();
    }
    
    public static void initialize()
    {
    	getInstance();
    }
    

    public static SolarSystemManager getInstance()
    {
        if (theInstance == null)
            theInstance = new SolarSystemManager();

        return theInstance;
    }

    public static SolarSystemDetails getSystemDetails(InnerSpherePlanet isp)
    {
    	SolarSystemDetails ssd = new SolarSystemDetails();
    	getInstance().loadSolarSystemDetails(isp.getID(),isp.getSystem(),ssd);
        return ssd;
    }
    
    public static SolarSystemDetails generateRandomDetails(InnerSpherePlanet isp)
    {
    	SolarSystemDetails ssd = new SolarSystemDetails();

    	int d100 = Dice.d100(1);
    	StarType st = SolarSystemDetails.getRandomStarType();
    	ssd.setStarType(st);
    	ssd.setStarMagnitude(Dice.d10(1) - 1);
    	
    	double innerLimit = SolarSystemDetails.GetBiozoneInnerLimit(st);
    	double outerLimit = SolarSystemDetails.GetBiozoneOuterLimit(st);
    	double orbitAU = innerLimit + (((outerLimit - innerLimit) / 100) * d100);
    	
    	ssd.setPrimaryPlanetOrbitInAU(orbitAU);
    	ssd.setMeanTemperatureAtSeaLevel(28 + (d100 / 4));
    	
    	ssd.generateRandomPlanet(true, false, false);
    	
    	return ssd;    	
    }

    
    public void loadSolarSystemDetails(long id, String name, SolarSystemDetails ssd)
    {
    	String filename = Long.toString(id) + "-" + name;
        String path = System.getProperty("UniverseDataPath");
        if (path == null)
            path = "data/planets/";
        else
        	path += "/planets/";
        
        new File(path).mkdirs();
        
        try 
        {        	
	        File f = new File(path + filename + ".gz");
	        if (f.exists())
	        {
	        	FileInputStream fis = new FileInputStream(f);
	        	GZIPInputStream ZIPStream = new GZIPInputStream(fis);            	
	        	loadSolarSystemDetails(ZIPStream, ssd);
	        	return;
	        }
	        f = new File(path + filename + ".xml");
	        if (f.exists())
	        {
	        	FileInputStream fis = new FileInputStream(f);
	        	loadSolarSystemDetails(fis, ssd);
	        	return;
	        }
	        f = new File(path + filename);
	        if (f.exists())
	        {
	        	FileInputStream fis = new FileInputStream(f);
	        	loadSolarSystemDetails(fis, ssd);
	        	return;
	        }
	        
        } catch(java.io.IOException ex) {
            log.info("Error Opening SolarSystem File!");
            log.error(ex);
        }        
    }
    
    public void loadSolarSystemDetails(InputStream stream, SolarSystemDetails ssd)
    {
        try {        	
            SAXBuilder b = new SAXBuilder();
            Document planetDoc = b.build(stream);
            Element root = planetDoc.getRootElement();
            
            ssd.loadFromElement(root);            
    	
        } catch (JDOMException jdex) {
            log.info("Failure Parsing SolarSystem File!");
            log.error(jdex);
        } catch (Exception exx) {
            log.info("Failure Loading SolarSystems!");
            log.error(ExceptionUtil.getExceptionStackTrace(exx));
            System.exit(1);
        }    	
    }

    public void saveSolarSystemDetails(InnerSpherePlanet isp, SolarSystemDetails ssd, boolean compress)
    {
    	saveSolarSystemDetails(isp.getID(), isp.getSystem(), ssd, compress);
    }
    
    public void saveSolarSystemDetails(long id, String name, SolarSystemDetails ssd, boolean compress)
    {
    	String filename = Long.toString(id) + "-" + name;
    	
        String path = System.getProperty("UniverseDataPath");
        if (path == null)
            path = "data/planets/";
        else
        	path += "/planets/";
        
        new File(path).mkdirs();
        
        File f = new File(path + filename);
        if (f.exists())
        	f.delete();
        
        try {            
            Document planetDoc = new Document();
            
            Element root = new Element("SolarSystemDetails"); 
            planetDoc.setRootElement(root);
            ssd.saveToElement(root);            

            XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
            if (compress)
            {
	        	GZIPOutputStream ZIPStream = new GZIPOutputStream(new FileOutputStream(path + filename + ".gz"));            	
	            out.output(planetDoc, ZIPStream);
	            
	            ZIPStream.close();
            }
            else
            {
                out.output(planetDoc, new FileOutputStream(path + filename + ".xml"));            
            }
                          
        } catch (Exception exx) {
            log.info("Failure Saving SolarSystemDetails");
            log.error(exx);
        } 
        
    }

    @SuppressWarnings("unchecked")
	private void checkPlanetDetailStore()
    {
    	try
    	{
	        String path = System.getProperty("UniverseDataPath");
	        if (path == null)
	            path = "data/planets/";
	        else
	        	path += "/planets";
	        
	        File f = new File(path); 
	        if (!f.exists())
	        {
	        	f.mkdirs();
	        	
		        String dataPath = System.getProperty("BaseDataPath");
		        if (dataPath == null)
		            dataPath = "data/";
		        
	            SAXBuilder b = new SAXBuilder();
	            Document SolarSystemDoc = b.build(new File(dataPath, "PlanetDetails.xml"));
	            Element root = SolarSystemDoc.getRootElement();
	            List<Element> planetElements = root.getChildren("Planet");
	            for (Element planetElement: planetElements)
	            {
	            	int index = Integer.parseInt(planetElement.getChildText("Index"));
	            	if (index > 72) index++;
	            	
	            	InnerSpherePlanet isp = PlanetManager.getPlanetFromID(index);
	            	if (isp != null)
	            	{
		            	log.info("Creating : " + isp.getSystem());
		            	
		            	SolarSystemDetails ssd = SolarSystemManager.generateRandomDetails(isp);
		            	ssd.setNotes(planetElement.getChildText("Description"));
		            	SolarSystemManager.getInstance().saveSolarSystemDetails(index, isp.getSystem(), ssd, true);
	            	}
	            	else
	            		log.info("Couldn't find : " + Integer.toString(index));
	            }
	        }
    	}
    	catch (Exception ex)
    	{
    		log.fatal("Unable to build planet store : " + ExceptionUtil.getExceptionStackTrace(ex),ex);
    		System.exit(1);
    	}

    }
}
