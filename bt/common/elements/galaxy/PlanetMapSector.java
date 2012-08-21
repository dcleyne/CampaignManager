package bt.common.elements.galaxy;

import java.io.Serializable;



/**
 * <p>Title: Inner Sphere</p>
 * <p>Description: Campaign Tracking Software</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Daniel Cleyne
 * @version 0.1
 */

public class PlanetMapSector implements Serializable
{

	private static final long serialVersionUID = -7596887281750164135L;
	private int m_SectorNumber;
    private String m_Name;
    private String m_Label;
    private String m_Notes;
    private String m_Links;
    private int m_MeanAltitude;
    private TerrainType m_TerrainType = TerrainType.NONE;
    private int m_AverageWindDirection;
    private int m_AverageCurrentDirection;
    private int m_MeanSurfaceTemperature;
    private int m_AverageAnnualRainfall;
	private int[] m_Rivers = new int[6];
	private int[] m_Roads = new int[6];
	private int[] m_Crossings = new int[6];



    public PlanetMapSector(int SectorNumber)
    {
        m_SectorNumber = SectorNumber;
    }
    
//------------------------------------------------------------------------------
// WorldSectorDetails Interface implementation
//------------------------------------------------------------------------------

    public String getName()
    { return m_Name; }

    public String getLabel()
    { return m_Label; }

    public String getNotes()
    { return m_Notes; }

    public String getLinks()
    { return m_Links; }

    public int getSectorNumber()
    { return m_SectorNumber; }

    public int getMeanAltitude()
    { return m_MeanAltitude; }

    public TerrainType getTerrainType()
    { return m_TerrainType; }

    public int getAverageWindDirection()
    { return m_AverageWindDirection; }

    public int setAverageCurrentDirection()
    { return m_AverageCurrentDirection; }

    public int getMeanSurfaceTemperature()
    { return m_MeanSurfaceTemperature; }

    public int getAverageAnnualRainfall()
    { return m_AverageAnnualRainfall; }

    public void setName(String rhs)
    {
        if (m_Name.compareTo(rhs) != 0)
        {
            m_Name = rhs;


        }
    }

    public void setLabel(String rhs)
    {
        if (m_Label.compareTo(rhs) != 0)
        {
            m_Label = rhs;


        }
    }

    public void setNotes(String rhs)
    {
        if (m_Notes.compareTo(rhs) != 0)
        {
            m_Notes = rhs;


        }
    }

    public void setLinks(String rhs)
    {
        if (m_Links.compareTo(rhs) != 0)
        {
            m_Links = rhs;


        }
    }

    public void setMeanAltitude(int rhs)
    {
        if (m_MeanAltitude != rhs)
        {
            m_MeanAltitude = rhs;


        }
    }

    public void setTerrainType(TerrainType tt)
    {
        if (m_TerrainType != tt)
        {
            m_TerrainType = tt;


        }
    }

    public void setAverageWindDirection(int rhs)
    {
        if (m_AverageWindDirection != rhs)
        {
            m_AverageWindDirection = rhs;


        }
    }

    public void setAverageCurrentDirection(int rhs)
    {
        if (m_AverageCurrentDirection != rhs)
        {
            m_AverageCurrentDirection = rhs;


        }
    }

    public void setMeanSurfaceTemperature(int rhs)
    {
        if (m_MeanSurfaceTemperature != rhs)
        {
            m_MeanSurfaceTemperature = rhs;


        }
    }

    public void setAverageAnnualRainfall(int rhs)
    {
        if (m_AverageAnnualRainfall != rhs)
        {
            m_AverageAnnualRainfall = rhs;


        }
    }
    
    public void clearRivers()
    {
    	for (int i = 0; i < 6; i++)
    		m_Rivers[i] = 0;
    }
    
    public void clearRoads()
    {
    	for (int i = 0; i < 6; i++)
    		m_Roads[i] = 0;
    }
    
    public void clearCrossings()
    {
    	for (int i = 0; i < 6; i++)
    		m_Crossings[i] = 0;
    }
    
    public int getRoad(int Index)
    {
    	return m_Roads[Index];
    }

    public int getRiver(int Index)
    {
    	return m_Rivers[Index];
    }

    public int getCrossing(int Index)
    {
    	return m_Crossings[Index];
    }
    
    public boolean hasRoad()
    {
    	for (int i = 0; i < 6; i++)
    	{
    		if (getRoad(i) > 0)
    			return true;
    	}
    	return false;
    }

    public boolean hasRiver()
    {
    	for (int i = 0; i < 6; i++)
    	{
    		if (getRiver(i) > 0)
    			return true;
    	}
    	return false;
    }

    public boolean hasCrossing()
    {
    	for (int i = 0; i < 6; i++)
    	{
    		if (getCrossing(i) > 0)
    			return true;
    	}
    	return false;
    }

    public void setRoad(int Index, int Road)
    {
    	m_Roads[Index] = Road;
    }

    public void setRiver(int Index, int River)
    {
    	m_Rivers[Index] = River;
    }

    public void setCrossing(int Index, int Crossing)
    {
    	m_Crossings[Index] = Crossing;
    }

    public void saveToElement(org.jdom.Element e)
    {
    	e.setAttribute("Number",Integer.toString(m_SectorNumber));
    	e.addContent(new org.jdom.Element("Name").setText(m_Name));
    	e.addContent(new org.jdom.Element("Label").setText(m_Label));
    	e.addContent(new org.jdom.Element("Notes").setText(m_Notes));
    	e.addContent(new org.jdom.Element("Links").setText(m_Links));
    	e.addContent(new org.jdom.Element("MeanAltitude").setText(Integer.toString(m_MeanAltitude)));
    	e.addContent(new org.jdom.Element("TerrainType").setText(m_TerrainType.toString()));
    	e.addContent(new org.jdom.Element("AverageWindDirection").setText(Integer.toString(m_AverageWindDirection)));
    	e.addContent(new org.jdom.Element("AverageCurrentDirection").setText(Integer.toString(m_AverageCurrentDirection)));
    	e.addContent(new org.jdom.Element("MeanSurfaceTemperature").setText(Integer.toString(m_MeanSurfaceTemperature)));
    	e.addContent(new org.jdom.Element("AverageAnnualRainfall").setText(Integer.toString(m_AverageAnnualRainfall)));
    	org.jdom.Element rivers = new org.jdom.Element("Rivers");
    	for (int i = 0; i < 6; i++)
    		rivers.setAttribute("River-" + Integer.toString(i + 1),Integer.toString(m_Rivers[i]));
    	e.addContent(rivers);
    	org.jdom.Element roads = new org.jdom.Element("Roads");
    	for (int i = 0; i < 6; i++)
    		roads.setAttribute("Road-" + Integer.toString(i + 1),Integer.toString(m_Roads[i]));
    	e.addContent(roads);
    	org.jdom.Element crossings = new org.jdom.Element("Crossings");
    	for (int i = 0; i < 6; i++)
    		crossings.setAttribute("Crossing-" + Integer.toString(i + 1),Integer.toString(m_Crossings[i]));
    	e.addContent(crossings);
    }

    public void loadFromElement(org.jdom.Element e)
    {
    	m_Name = e.getChild("Name").getValue();
    	m_Label = e.getChild("Label").getValue();
    	m_Notes = e.getChild("Notes").getValue();
    	m_Links = e.getChild("Links").getValue();
    	m_TerrainType = TerrainType.fromString(e.getChild("TerrainType").getValue());
    	m_MeanAltitude = Integer.parseInt(e.getChild("MeanAltitude").getValue());
    	m_AverageWindDirection = Integer.parseInt(e.getChild("AverageWindDirection").getValue());
    	m_AverageCurrentDirection = Integer.parseInt(e.getChild("AverageCurrentDirection").getValue());
    	m_MeanSurfaceTemperature = Integer.parseInt(e.getChild("MeanSurfaceTemperature").getValue());
    	m_AverageAnnualRainfall = Integer.parseInt(e.getChild("AverageAnnualRainfall").getValue());
    	org.jdom.Element rivers = e.getChild("Rivers");
    	for (int i = 0; i < 6; i++)
    		m_Rivers[i] = Integer.parseInt(rivers.getAttributeValue("River-" + Integer.toString(i + 1)));
    	org.jdom.Element roads = e.getChild("Roads");
    	for (int i = 0; i < 6; i++)
    		m_Roads[i] = Integer.parseInt(roads.getAttributeValue("Road-" + Integer.toString(i + 1)));
    	org.jdom.Element crossings = e.getChild("Crossings");
    	for (int i = 0; i < 6; i++)
    		m_Crossings[i] = Integer.parseInt(crossings.getAttributeValue("Crossing-" + Integer.toString(i + 1)));
    }    
}
