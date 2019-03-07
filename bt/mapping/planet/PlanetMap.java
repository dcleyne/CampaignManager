package bt.mapping.planet;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import bt.mapping.TerrainType;


/**
 * <p>Title: Inner Sphere</p>
 * <p>Description: Campaign Tracking Software</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Daniel Cleyne
 * @version 0.1
 */

public class PlanetMap implements Serializable
{

	private static final long serialVersionUID = -4095090008085275592L;
	private int m_Hydrographics;
	private int m_MeanAltitude;
	private int m_MeanSurfaceTemperature;

    /**************************************************************************/
    /* Member Variables */
    /**************************************************************************/
    protected Vector<PlanetMapSector> m_PlanetSectors = new Vector<PlanetMapSector>();
    
    public int getHydrographics()
    { return m_Hydrographics; }
    
    public int getMeanAltitude()
    { return m_MeanAltitude; }
    
    public int getMeanSurfaceTemperature()
    { return m_MeanSurfaceTemperature; }

    public PlanetMap(int numSectors)
    {
    	for (int i = 0; i < numSectors; i++)
    		m_PlanetSectors.add(new PlanetMapSector(i + 1));
    }
    
    public PlanetMap(org.jdom.Element e)
    {
    	loadFromElement(e);
    }
        


    public PlanetMapSector getPlanetSector(int index)
    {
        if (m_PlanetSectors != null)
        {
            if (m_PlanetSectors.size() == 0)
                return null;

            if (index > m_PlanetSectors.size() || index < 1)
                return null;
            else
                return m_PlanetSectors.elementAt(index-1);
        }
        else
            return null;
    }

    public int getPlanetSectorCount()
    {
        return m_PlanetSectors.size();
    }
    
    public void recalculateAverages()
    {
    	int totalTemperature = 0;
    	int totalAltitude = 0;
    	int totalWater = 0;
    	
    	for (PlanetMapSector sector: m_PlanetSectors)
    	{
    		totalTemperature += sector.getMeanSurfaceTemperature();
    		TerrainType tt = sector.getTerrainType();
    		if (tt == TerrainType.DEEPWATER || 
    				tt == TerrainType.WATER ||
    				tt == TerrainType.WATERICE ||
    				tt == TerrainType.LAKE)
    		{
    			totalWater ++;
    		}
    		else
    		{
        		totalAltitude += sector.getMeanAltitude();
    		}
    	}
    	
    	m_MeanSurfaceTemperature = totalTemperature / m_PlanetSectors.size();
    	m_Hydrographics = (totalWater * 100) / m_PlanetSectors.size();
    	
    	if (m_PlanetSectors.size() > totalWater)
    		m_MeanAltitude = totalAltitude / (m_PlanetSectors.size() - totalWater);    	
    }
    
    public Vector<Integer> findSettlementSites(WorldMapNavigation nav)
    {
    	Vector<Integer> sites = new Vector<Integer>();
    	for (PlanetMapSector sector: m_PlanetSectors)
    	{
    		TerrainType tt = sector.getTerrainType();
    		if (sector.hasRiver() && 
    				(tt == TerrainType.PLAINS || 
    						tt == TerrainType.HILLS || 
    						tt == TerrainType.FOREST || 
    						tt == TerrainType.JUNGLE || 
    						tt == TerrainType.WOODEDHILLS
    				))
    		{
        		for (int i = 1; i < 7; i++)
        		{
            		int neighbour = nav.getHexNeighbour(sector.getSectorNumber(),i);
            		PlanetMapSector pms1 = getPlanetSector(neighbour);
            		if (pms1.getTerrainType() == TerrainType.WATER || 
            				pms1.getTerrainType() == TerrainType.LAKE)
            		{
            			sites.add(sector.getSectorNumber());
            			break;
            		}
        		}
    		}
    	}    	
    	
    	return sites;
    }
    
    
    public void saveToElement(org.jdom.Element e)
    {
    	e.addContent(new org.jdom.Element("MeanSurfaceTemperature").setText(Integer.toString(m_MeanSurfaceTemperature)));
    	e.addContent(new org.jdom.Element("MeanAltitude").setText(Integer.toString(m_MeanAltitude)));
    	e.addContent(new org.jdom.Element("Hydrographics").setText(Integer.toString(m_Hydrographics)));    	
    	org.jdom.Element sectors = new org.jdom.Element("Sectors");
    	e.addContent(sectors);
    	for (PlanetMapSector s: m_PlanetSectors)
    	{
    		org.jdom.Element sector = new org.jdom.Element("Sector");
    		sectors.addContent(sector);
    		s.saveToElement(sector);
    	}
    }

    @SuppressWarnings("unchecked")
    public void loadFromElement(org.jdom.Element e)
    {
    	m_MeanSurfaceTemperature = Integer.parseInt(e.getChild("MeanSurfaceTemperature").getValue());
    	m_MeanAltitude = Integer.parseInt(e.getChild("MeanAltitude").getValue());
    	m_Hydrographics = Integer.parseInt(e.getChild("Hydrographics").getValue());
    	List<org.jdom.Element> sectors = e.getChild("Sectors").getChildren("Sector");
    	for (org.jdom.Element sectorElement : sectors)
    	{
    		int sectorNumber = Integer.parseInt(sectorElement.getAttribute("Number").getValue());
    		PlanetMapSector pms = new PlanetMapSector(sectorNumber);
    		pms.loadFromElement(sectorElement);
    		m_PlanetSectors.add(pms);    		
    	}
    		
    }
    
}
