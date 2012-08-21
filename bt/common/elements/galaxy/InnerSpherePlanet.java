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
public class InnerSpherePlanet
{
//    private static Log log = LogFactory.getLog(InnerSpherePlanet.class);
	
	private long m_ID = -1;
    protected String m_System = "";
    protected double m_XCoord;
    protected double m_YCoord;
    protected String m_Owner_2750 = "";
    protected String m_Owner_3025 = "";
    protected String m_Owner_3030 = "";
    protected String m_Owner_3040 = "";
    protected String m_Owner_3052 = "";
    protected String m_Owner_3057 = "";
    protected String m_Owner_3062 = "";

    public long getID()
    {
    	return m_ID;
    }
    
    public String getSystem()
    { return m_System; }

    public double getXCoord()
    { return m_XCoord; }

    public double getYCoord()
    { return m_YCoord; }

    public String getOwner(int era)
    {
        switch (era)
        {
            case 2750: return m_Owner_2750;
            case 3025: return m_Owner_3025;
            case 3030: return m_Owner_3030;
            case 3040: return m_Owner_3040;
            case 3052: return m_Owner_3052;
            case 3057: return m_Owner_3057;
            case 3062: return m_Owner_3062;
        }
        return "Unknown Era";
    }

    public InnerSpherePlanet()
    {
    }


    public String toString()
    {
        return Long.toString(m_ID) +  "-" + m_System;
    }


    public double getDistance(InnerSpherePlanet p)
    {
        double X = Math.abs(this.getXCoord() - p.getXCoord());
        double Y = Math.abs(this.getYCoord() - p.getYCoord());
        double distance = Math.sqrt( (X * X) + (Y * Y));

        return distance;
    }
    
    public void loadFromElement(Element e)
    {
    	m_ID = Long.parseLong(e.getChild("ID").getValue());
    	m_System = e.getChild("Name").getValue();
    	m_XCoord = Double.parseDouble(e.getChild("X_Coord").getValue());
    	m_YCoord = Double.parseDouble(e.getChild("Y_Coord").getValue());

        m_Owner_2750 = e.getChild("Owner_2750").getValue();
        m_Owner_3025 = e.getChild("Owner_3025").getValue();
        m_Owner_3030 = e.getChild("Owner_3030").getValue();
        m_Owner_3040 = e.getChild("Owner_3040").getValue();
        m_Owner_3052 = e.getChild("Owner_3052").getValue();
        m_Owner_3057 = e.getChild("Owner_3057").getValue();
        m_Owner_3062 = e.getChild("Owner_3062").getValue();    	
    }

}
