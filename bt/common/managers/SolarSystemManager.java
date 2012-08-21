package bt.common.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Vector;
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
import bt.common.elements.galaxy.SolarSystemRoute;
import bt.common.elements.galaxy.StarType;
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

    private Vector<InnerSpherePlanet> m_Planets;
    private Vector<SolarSystemRoute> m_Routes = new Vector<SolarSystemRoute>();

    protected static int[][] m_JumpSailRecharge = {
    /*M*/{201,202,203,204,205,206,207,208,209,210},
    /*K*/{191,192,193,194,195,196,197,198,199,200},
    /*G*/{181,182,183,184,185,186,187,188,189,190},
    /*F*/{171,172,173,174,175,176,177,178,179,180},
    /*A*/{161,162,163,164,165,166,167,168,169,170},
    /*B*/{151,152,153,154,155,156,157,158,159,160}  };

    private static double[][] ProximityPoints = {
    /*M*/{  0.18,  0.16,  0.15,  0.13,  0.12,  0.11,  0.10, 0.09, 0.08, 0.07},
    /*K*/{  0.55,  0.49,  0.43,  0.39,  0.34,  0.31,  0.28, 0.25, 0.22, 0.20},
    /*G*/{  1.99,  1.74,  1.52,  1.33,  1.16,  1.02,  0.90, 0.79, 0.70, 0.62},
    /*F*/{  8.80,  7.51,  6.43,  5.51,  4.74,  4.08,  3.52, 3.04, 2.64, 2.29},
    /*A*/{ 48.59, 40.51, 33.85, 28.36, 23.82, 20.06, 19.63,14.32,12.15,10.32},
    /*B*/{347.84,282.07,229.40,187.12,153.06,125.56,103.29,85.20,70.47,58.44}};

    private static double[][] BiozoneLimitsAU = {
    /*M*/{0.1,0.2},
    /*K*/{0.5,0.6},
    /*G*/{0.8,1.2},
    /*F*/{1.6,2.4},
    /*A*/{3.1,4.7},
    /*B*/{30.0,45.0}};

    //private static double AUinBillionKm = 0.149598550;
    private static double AUinKm = 149598550.0;
    private static double GsInKmPHPH = 127008.0;

    protected SolarSystemManager()
    {
        m_Planets = loadPlanets();

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

    public static int getPlanetCount()
    {
        return getInstance().m_Planets.size();
    }

    public static InnerSpherePlanet getPlanet(int index)
    {
        return (InnerSpherePlanet)getInstance().m_Planets.elementAt(index);
    }

    public static SolarSystemDetails getSystemDetails(InnerSpherePlanet isp)
    {
    	SolarSystemDetails ssd = new SolarSystemDetails();
    	getInstance().loadSolarSystemDetails(isp.getID(),isp.getSystem(),ssd);
        return ssd;
    }

    
    public static InnerSpherePlanet getPlanetFromID(long id)
    {
        for (int i = 0; i < getPlanetCount(); i++)
        {
            InnerSpherePlanet isp = getPlanet(i);
            if (isp.getID() == id)
                return isp;
        }
        return null;
    }

    public static int getPlanetIndexFromID(long id)
    {
        for (int i = 0; i < getPlanetCount(); i++)
        {
            InnerSpherePlanet isp = getPlanet(i);
            if (isp.getID() == id)
                return i;
        }
        return -1;
    }

    public static long getPlanetIDFromIndex(int index)
    {
        InnerSpherePlanet p = getPlanet(index);
        return p.getID();
    }

    public static double getDistance(long systemID1, long systemID2)
    {
        InnerSpherePlanet System1 = getPlanetFromID(systemID1);
        InnerSpherePlanet System2 = getPlanetFromID(systemID2);

        return System1.getDistance(System2);
    }

    protected void checkRoutes()
    {
        for (int i = 0; i < m_Planets.size(); i++)
        {
            InnerSpherePlanet p1 = (InnerSpherePlanet)m_Planets.elementAt(i);
            for (int j = 0; j < m_Planets.size(); j++)
            {
                InnerSpherePlanet p2 = (InnerSpherePlanet)m_Planets.elementAt(j);
                checkRouteToSystem(p1,p2);
            }
        }
    }

    protected void checkRouteToSystem(InnerSpherePlanet p1,InnerSpherePlanet p2)
    {
        if (p1.getID() == p2.getID()) return; //Don't want to add a route to ourselves

        double Distance = p1.getDistance(p2);
        if (Distance < 30)
        {
            SolarSystemRoute ssr = getRouteToSystem(p1,p2);
            if (ssr == null)
            {
                ssr = new SolarSystemRoute();
                ssr.setSystem1(p1.getID());
                ssr.setSystem2(p2.getID());
                ssr.setDistance(Distance);
            }
            m_Routes.add(ssr);
        }
    }

    protected SolarSystemRoute getRouteToSystem(InnerSpherePlanet p1,InnerSpherePlanet p2)
    {
        for (int i = 0; i < m_Routes.size(); i++)
        {
            SolarSystemRoute ssr = (SolarSystemRoute)m_Routes.elementAt(i);
            if ((ssr.getSystem1() == p1.getID() && ssr.getSystem2() == p2.getID()) ||(ssr.getSystem1() == p2.getID() && ssr.getSystem2() == p1.getID()))
                return ssr;
        }
        return null;
    }

    public static Vector<SolarSystemRoute> getRoutesForSystem(InnerSpherePlanet p)
    {
        Vector<SolarSystemRoute> routes = new Vector<SolarSystemRoute>();
        SolarSystemManager ssm = SolarSystemManager.getInstance();
        for (int i = 0; i < ssm.m_Routes.size(); i++)
        {
            SolarSystemRoute ssr = ssm.m_Routes.elementAt(i);
            if (ssr.getSystem1() == p.getID() || ssr.getSystem2() == p.getID())
                routes.add(ssr);
        }
        return routes;
    }


    public static int getJumpSailRechargeTime(StarType st, int starMagnitude)
    {
        int stIndex = GetStarTypeIndex(st);

        return m_JumpSailRecharge[stIndex][starMagnitude];
    }

    protected static double getJumpPointProximity(StarType st, int magnitude)
    {
            int StarTypeIndex = GetStarTypeIndex(st);
            int MagnitudeIndex = magnitude;
            double jpp = ProximityPoints[StarTypeIndex][MagnitudeIndex];
            return jpp;
    }

    public static double getJumpPointDistanceFromPlanet(StarType st, int starMagnitude, double planetOrbitInAU)
    {
            double JumpPointProximity = getJumpPointProximity(st,starMagnitude) * 1000000000.0;
            double PlanetDistance = planetOrbitInAU * AUinKm;
            double JPPSquare = JumpPointProximity * JumpPointProximity;
            double PDSquare = PlanetDistance * PlanetDistance;

            double Dist = Math.sqrt(JPPSquare + PDSquare);
            return Dist;
    }

    public static double getTransitTime(StarType st, int starMagnitude, double distance, double gRating)
    {
            if (distance == 0.0 || gRating == 0.0)
                    return 0.0;

            double Accell = GsInKmPHPH * gRating;
            double FullDist =  getJumpPointDistanceFromPlanet(st,starMagnitude,distance) / 2;
            double BurnUnits = FullDist / Accell * 2;
            double TransitTime = 2 * Math.sqrt(BurnUnits);
            return TransitTime;
    }

    public static double GetBiozoneInnerLimit(StarType st)
    {
            int Index = GetStarTypeIndex(st);
            return BiozoneLimitsAU[Index][0];
    }

    public static double GetBiozoneOuterLimit(StarType st)
    {
            int Index = GetStarTypeIndex(st);
            return BiozoneLimitsAU[Index][1];
    }

    protected static int GetStarTypeIndex(StarType st)
    {
    	switch (st)
    	{
    		case O: return 5;
	    	case B: return 5;
	    	case A: return 4;
	    	case F: return 3;
	    	case G: return 2;
	    	case K: return 1;
	    	case M: return 0;    		
    	}
        return 0;
    }
    
    @SuppressWarnings("unchecked")
    private Vector<InnerSpherePlanet> loadPlanets()
    {
    	Vector<InnerSpherePlanet> planets = new Vector<InnerSpherePlanet>();
        String Path = System.getProperty("DataPath");
        if (Path == null)
            Path = "data/";
        
        try {            
            SAXBuilder b = new SAXBuilder();
            Document SolarSystemDoc = b.build(new File(Path, "SolarSystem.xml"));
            Element root = SolarSystemDoc.getRootElement();
            List<Element> planetElements = root.getChildren("SolarSystem");
            for (Element planetElement: planetElements)
            {
            	InnerSpherePlanet p = new InnerSpherePlanet();
            	p.loadFromElement(planetElement);            	
            	planets.add(p);
            	
            	List<Element> routeElements = planetElement.getChildren("Route");
            	for (Element routeElement: routeElements)
            	{
            		SolarSystemRoute ssr = new SolarSystemRoute();
            		ssr.setSystem1(p.getID());
            		ssr.loadFromElement(routeElement);
            		m_Routes.add(ssr);
            	}
            }
    	
        } catch(java.io.IOException ex) {
            log.info("Error Opening SolarSystem File!");
            log.error(ex);
        } catch (JDOMException jdex) {
            log.info("Failure Parsing SolarSystem File!");
            log.error(jdex);
        } catch (Exception exx) {
            log.info("Failure Loading SolarSystems!");
            log.error(exx);
        }
    	
    	return planets;
    }
    
    public void loadSolarSystemDetails(long id, String name, SolarSystemDetails ssd)
    {
    	String filename = Long.toString(id) + "-" + name + ".gz";
        String path = System.getProperty("PlanetDataDirectory");
        if (path == null)
            path = "data/planets/";
        
        File f = new File(path + filename);
        if (f.exists())
        {
            try {
            	
            	FileInputStream fis = new FileInputStream(f);
            	GZIPInputStream ZIPStream = new GZIPInputStream(fis);            	
                SAXBuilder b = new SAXBuilder();
                Document planetDoc = b.build(ZIPStream);
                Element root = planetDoc.getRootElement();
                
                ssd.loadFromElement(root);            
        	
            } catch(java.io.IOException ex) {
                log.info("Error Opening SolarSystem File!");
                log.error(ex);
            } catch (JDOMException jdex) {
                log.info("Failure Parsing SolarSystem File!");
                log.error(jdex);
            } catch (Exception exx) {
                log.info("Failure Loading SolarSystems!");
                log.error(ExceptionUtil.getExceptionStackTrace(exx));
                System.exit(1);
            }
        }
    	
    }

    public void saveSolarSystemDetails(long id, String name, SolarSystemDetails ssd, boolean compress)
    {
    	String filename = Long.toString(id) + "-" + name;
    	
        String path = System.getProperty("PlanetDataDirectory");
        if (path == null)
            path = "data/planets/";
        
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
    
}
