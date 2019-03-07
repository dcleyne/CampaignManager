package bt.mapping.planet;

import java.io.Serializable;

import bt.mapping.TerrainType;



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
	private int _SectorNumber;
    private String _Name;
    private String _Label;
    private String _Notes;
    private String _Links;
    private int _MeanAltitude;
    private TerrainType _TerrainType = TerrainType.NONE;
    private int _AverageWindDirection;
    private int _AverageCurrentDirection;
    private int _MeanSurfaceTemperature;
    private int _AverageAnnualRainfall;
	private int[] _Rivers = new int[6];
	private int[] _Roads = new int[6];
	private int[] _Crossings = new int[6];

    public PlanetMapSector(int SectorNumber)
    {
        _SectorNumber = SectorNumber;
    }
    
//------------------------------------------------------------------------------
// WorldSectorDetails Interface implementation
//------------------------------------------------------------------------------

    public String getName()
    { return _Name; }

    public String getLabel()
    { return _Label; }

    public String getNotes()
    { return _Notes; }

    public String getLinks()
    { return _Links; }

    public int getSectorNumber()
    { return _SectorNumber; }

    public int getMeanAltitude()
    { return _MeanAltitude; }

    public TerrainType getTerrainType()
    { return _TerrainType; }

    public int getAverageWindDirection()
    { return _AverageWindDirection; }

    public int setAverageCurrentDirection()
    { return _AverageCurrentDirection; }

    public int getMeanSurfaceTemperature()
    { return _MeanSurfaceTemperature; }

    public int getAverageAnnualRainfall()
    { return _AverageAnnualRainfall; }

    public void setName(String rhs)
    {
        if (_Name.compareTo(rhs) != 0)
        {
            _Name = rhs;


        }
    }

    public void setLabel(String rhs)
    {
        if (_Label.compareTo(rhs) != 0)
        {
            _Label = rhs;


        }
    }

    public void setNotes(String rhs)
    {
        if (_Notes.compareTo(rhs) != 0)
        {
            _Notes = rhs;


        }
    }

    public void setLinks(String rhs)
    {
        if (_Links.compareTo(rhs) != 0)
        {
            _Links = rhs;


        }
    }

    public void setMeanAltitude(int rhs)
    {
        if (_MeanAltitude != rhs)
        {
            _MeanAltitude = rhs;


        }
    }

    public void setTerrainType(TerrainType tt)
    {
        if (_TerrainType != tt)
        {
            _TerrainType = tt;


        }
    }

    public void setAverageWindDirection(int rhs)
    {
        if (_AverageWindDirection != rhs)
        {
            _AverageWindDirection = rhs;


        }
    }

    public void setAverageCurrentDirection(int rhs)
    {
        if (_AverageCurrentDirection != rhs)
        {
            _AverageCurrentDirection = rhs;


        }
    }

    public void setMeanSurfaceTemperature(int rhs)
    {
        if (_MeanSurfaceTemperature != rhs)
        {
            _MeanSurfaceTemperature = rhs;


        }
    }

    public void setAverageAnnualRainfall(int rhs)
    {
        if (_AverageAnnualRainfall != rhs)
        {
            _AverageAnnualRainfall = rhs;


        }
    }
    
    public void clearRivers()
    {
    	for (int i = 0; i < 6; i++)
    		_Rivers[i] = 0;
    }
    
    public void clearRoads()
    {
    	for (int i = 0; i < 6; i++)
    		_Roads[i] = 0;
    }
    
    public void clearCrossings()
    {
    	for (int i = 0; i < 6; i++)
    		_Crossings[i] = 0;
    }
    
    public int getRoad(int Index)
    {
    	return _Roads[Index];
    }

    public int getRiver(int Index)
    {
    	return _Rivers[Index];
    }

    public int getCrossing(int Index)
    {
    	return _Crossings[Index];
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
    	_Roads[Index] = Road;
    }

    public void setRiver(int Index, int River)
    {
    	_Rivers[Index] = River;
    }

    public void setCrossing(int Index, int Crossing)
    {
    	_Crossings[Index] = Crossing;
    }

    public void saveToElement(org.jdom.Element e)
    {
    	e.setAttribute("Number",Integer.toString(_SectorNumber));
    	e.addContent(new org.jdom.Element("Name").setText(_Name));
    	e.addContent(new org.jdom.Element("Label").setText(_Label));
    	e.addContent(new org.jdom.Element("Notes").setText(_Notes));
    	e.addContent(new org.jdom.Element("Links").setText(_Links));
    	e.addContent(new org.jdom.Element("MeanAltitude").setText(Integer.toString(_MeanAltitude)));
    	e.addContent(new org.jdom.Element("TerrainType").setText(_TerrainType.toString()));
    	e.addContent(new org.jdom.Element("AverageWindDirection").setText(Integer.toString(_AverageWindDirection)));
    	e.addContent(new org.jdom.Element("AverageCurrentDirection").setText(Integer.toString(_AverageCurrentDirection)));
    	e.addContent(new org.jdom.Element("MeanSurfaceTemperature").setText(Integer.toString(_MeanSurfaceTemperature)));
    	e.addContent(new org.jdom.Element("AverageAnnualRainfall").setText(Integer.toString(_AverageAnnualRainfall)));
    	org.jdom.Element rivers = new org.jdom.Element("Rivers");
    	for (int i = 0; i < 6; i++)
    		rivers.setAttribute("River-" + Integer.toString(i + 1),Integer.toString(_Rivers[i]));
    	e.addContent(rivers);
    	org.jdom.Element roads = new org.jdom.Element("Roads");
    	for (int i = 0; i < 6; i++)
    		roads.setAttribute("Road-" + Integer.toString(i + 1),Integer.toString(_Roads[i]));
    	e.addContent(roads);
    	org.jdom.Element crossings = new org.jdom.Element("Crossings");
    	for (int i = 0; i < 6; i++)
    		crossings.setAttribute("Crossing-" + Integer.toString(i + 1),Integer.toString(_Crossings[i]));
    	e.addContent(crossings);
    }

    public void loadFromElement(org.jdom.Element e)
    {
    	_Name = e.getChild("Name").getValue();
    	_Label = e.getChild("Label").getValue();
    	_Notes = e.getChild("Notes").getValue();
    	_Links = e.getChild("Links").getValue();
    	_TerrainType = TerrainType.fromString(e.getChild("TerrainType").getValue());
    	_MeanAltitude = Integer.parseInt(e.getChild("MeanAltitude").getValue());
    	_AverageWindDirection = Integer.parseInt(e.getChild("AverageWindDirection").getValue());
    	_AverageCurrentDirection = Integer.parseInt(e.getChild("AverageCurrentDirection").getValue());
    	_MeanSurfaceTemperature = Integer.parseInt(e.getChild("MeanSurfaceTemperature").getValue());
    	_AverageAnnualRainfall = Integer.parseInt(e.getChild("AverageAnnualRainfall").getValue());
    	org.jdom.Element rivers = e.getChild("Rivers");
    	for (int i = 0; i < 6; i++)
    		_Rivers[i] = Integer.parseInt(rivers.getAttributeValue("River-" + Integer.toString(i + 1)));
    	org.jdom.Element roads = e.getChild("Roads");
    	for (int i = 0; i < 6; i++)
    		_Roads[i] = Integer.parseInt(roads.getAttributeValue("Road-" + Integer.toString(i + 1)));
    	org.jdom.Element crossings = e.getChild("Crossings");
    	for (int i = 0; i < 6; i++)
    		_Crossings[i] = Integer.parseInt(crossings.getAttributeValue("Crossing-" + Integer.toString(i + 1)));
    }    
}
