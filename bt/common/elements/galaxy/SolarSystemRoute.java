package bt.common.elements.galaxy;

import org.jdom.Element;



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
public class SolarSystemRoute
{
    protected long m_System1;
    protected long m_System2;
    protected double m_Distance;

    public long getSystem1()
    { return m_System1; }

    public void setSystem1(long ID)
    { m_System1 = ID; }

    public long getSystem2()
    { return m_System2; }

    public void setSystem2(long ID)
    { m_System2 = ID; }

    public double getDistance()
    { return m_Distance; }

    public void setDistance(double Distance)
    { m_Distance = Distance; }

    public SolarSystemRoute()
    {
    }

    public long getDestinationSystem(long ID)
    {
        if (m_System1 == ID)
            return m_System2;
        else
            return m_System1;
    }
    
    public void loadFromElement(Element e)
    {
    	m_System2 = Long.parseLong(e.getAttributeValue("SystemID"));
    	m_Distance = Double.parseDouble(e.getAttributeValue("Distance"));
    }
    
}
