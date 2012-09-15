package bt.managers;

import java.io.File;

import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import bt.elements.galaxy.InnerSpherePlanet;
import bt.elements.galaxy.SolarSystemRoute;
import bt.util.PropertyUtil;

public class PlanetManager 
{
	private static PlanetManager theInstance = null;
	
    private Vector<InnerSpherePlanet> m_Planets;
    private Vector<SolarSystemRoute> m_Routes = new Vector<SolarSystemRoute>();


	private PlanetManager()
	{
        m_Planets = loadPlanets();
	}
	
	public static PlanetManager getInstance()
	{
		if (theInstance == null)
			theInstance = new PlanetManager();
		
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
        PlanetManager pm = getInstance();
        for (int i = 0; i < pm.m_Routes.size(); i++)
        {
            SolarSystemRoute ssr = pm.m_Routes.elementAt(i);
            if (ssr.getSystem1() == p.getID() || ssr.getSystem2() == p.getID())
                routes.add(ssr);
        }
        return routes;
    }


    @SuppressWarnings("unchecked")
    private Vector<InnerSpherePlanet> loadPlanets()
    {
    	Vector<InnerSpherePlanet> planets = new Vector<InnerSpherePlanet>();
        String Path = PropertyUtil.getStringProperty("DataPath", "data");
        
        try {            
            SAXBuilder b = new SAXBuilder();
            Document SolarSystemDoc = b.build(new File(Path, "/SolarSystem.xml"));
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
        	System.out.println("Error Opening SolarSystem File!");
            ex.printStackTrace();
        } catch (JDOMException jdex) {
        	System.out.println("Failure Parsing SolarSystem File!");
            jdex.printStackTrace();
        } catch (Exception exx) {
        	System.out.println("Failure Loading SolarSystems!");
            exx.printStackTrace();
        }
    	
    	return planets;
    }
    

}
