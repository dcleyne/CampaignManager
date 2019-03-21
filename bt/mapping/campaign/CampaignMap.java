/*******************************************************************************
 * Title: Legatus
 * 
 * Copyright Daniel Cleyne (c) 2004-2013
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at our option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * @author Daniel Cleyne
 ******************************************************************************/

package bt.mapping.campaign;

import java.util.HashMap;
import java.util.List;

import org.jdom.Element;

import bt.mapping.Coordinate;
import bt.mapping.HexMap;
import bt.mapping.TerrainType;

public class CampaignMap extends HexMap
{
	private static final String VERSION = "Version";
	private static final String WIDTH = "Width";
	private static final String HEIGHT = "Height";
	private static final String DEFAULT_TERRAIN = "DefaultTerrain";
	private static final String MAP_HEXES = "MapHexes";
	private static final String ROADS_AUTO_EXIT = "RoadsAutoExit";
	static final long serialVersionUID = 1;

	private int _Width;
	private int _Height;
	private boolean _RoadsAutoExit;

	public CampaignMap(Element e) throws Exception
	{
		loadFromElement(e);
	}
	
	public CampaignMap(int width, int height)
	{
		super(width, height);
		_Width = width;
		_Height = height;
	}

	@Override
	public String getMapType()
	{
		return "Campaign";
	}

	@Override
	public int getWidth()
	{
		return _Width;
	}

	@Override
	public int getHeight()
	{
		return _Height;
	}

	@Override
	public void setSize(int width, int height) throws Exception
	{
		_Width = width;
		_Height = height;
		super.setSize(width, height);
	}

	public void setRoadsAutoExit(Boolean AutoExit)
	{
		_RoadsAutoExit = AutoExit;
	}

	public boolean getRoadsAutoExit()
	{
		return _RoadsAutoExit;
	}

	public Coordinate getCoordinateForSettlement(String locationName)
	{
		for (int y = 0; y < getHeight(); y++)
		{
			for (int x = 0; x < getWidth(); x++)
			{
				CampaignMapHex mapHex = (CampaignMapHex)getHex(x, y);
				if (mapHex.getSettlementName().equalsIgnoreCase(locationName))
					return new Coordinate(x,y);
			}
		}
		return null;
	}
	
	private TerrainType determineDefaultTerrain()
	{
		HashMap<TerrainType, Integer> terrainCounts = new HashMap<TerrainType, Integer>();
		
		for (int y = 0; y < getHeight(); y++)
		{
			for (int x = 0; x < getWidth(); x++)
			{
				CampaignMapHex mapHex = (CampaignMapHex)getHex(x, y);
				TerrainType tt = mapHex.getTerrainType();
				if (!terrainCounts.containsKey(tt))
					terrainCounts.put(tt, 0);
				
				terrainCounts.put(tt, terrainCounts.get(tt) + 1);
			}
		}
		
		if (terrainCounts.size() > 0)
		{
			int count = 0;
			TerrainType terrainType = TerrainType.NONE;
			
			for (TerrainType tt : terrainCounts.keySet())
			{
				if (terrainCounts.get(tt) > count)
				{
					count = terrainCounts.get(tt);
					terrainType = tt;
				}
			}
			return terrainType;
		}
		return TerrainType.NONE;
	}

	private boolean hasRivers(CampaignMapHex mapHex)
	{
		for (int side = 0; side < 6; side++)
		{
			if (mapHex.getRiver(side) != 0)
				return true;
		}
		return false;
	}

	private boolean hasRoads(CampaignMapHex mapHex)
	{
		for (int side = 0; side < 6; side++)
		{
			if (mapHex.getRoad(side) != 0)
				return true;
		}
		return false;
	}

	private boolean notDefaultMapHex(CampaignMapHex mapHex, TerrainType defaultTerrainType)
	{
		if (mapHex.getTerrainType() != defaultTerrainType)
			return true;
		
		if (hasRivers(mapHex))
			return true;
		
		if (hasRoads(mapHex))
			return true;
		
		return false;
	}

	@Override
	protected void setDefaultHex(TerrainType defaultTerrain)
	{
		for (int j = 0; j < getHeight(); j++)
		{
			for (int i = 0; i < getWidth(); i++)
			{
				if (defaultTerrain == null)
				{
					setHex(j * getWidth() + i, new CampaignMapHex(i, j));
				} else
				{
					setHex(j * getWidth() + i, new CampaignMapHex(i, j, defaultTerrain));
				}
			}
		}
	}

	@Override
	public String toString()
	{
		// for debugging
		String output = "Map: " + getName() + "\n";
		CampaignMapHex hex;
		for (int i = 0; i < getHexCount(); i++)
		{
			hex = (CampaignMapHex) getHex(i);
			output += "MapHex index:" + String.valueOf(i) + "; ";
			output += "Coordinate:" + hex.getCoordinate().toString() + "; ";
			output += "Terrain:" + hex.getTerrainType() + "; ";
			output += "Roads:" + hex.roadToString();
			output += "Rivers:" + hex.riverToString();
			output += "\n";
		}

		return output;

	}
	
	public void createBlankMap(TerrainType defaultTerrainType)
	{
		setDefaultHex(defaultTerrainType);
	}

	private void updateMapHexes(List<?> mapHexes, TerrainType defaultTerrain) throws Exception
	{
		if (mapHexes != null)
		{

			for (int i = 0; i < mapHexes.size(); i++)
			{
				Element hexElement = (Element) mapHexes.get(i);
				
				CampaignMapHex mapHex = new CampaignMapHex(0, 0, defaultTerrain);
				mapHex.loadFromElement(hexElement);
				setHex(mapHex);
			}
		}
	}
	
	public void saveToDataElement(Element e)
	{
		
	}

	
	public void saveToXMLElement(Element e)
	{
		TerrainType defaultTerrainType = determineDefaultTerrain();
		
		e.setAttribute(ROADS_AUTO_EXIT, Boolean.toString(_RoadsAutoExit));
        e.addContent(new Element(DEFAULT_TERRAIN).setText(defaultTerrainType.toString()));
        e.addContent(new Element(VERSION).setText("1"));
        e.addContent(new Element(WIDTH).setText(Integer.toString(getWidth())));
        e.addContent(new Element(HEIGHT).setText(Integer.toString(getHeight())));
        
        Element mapHexesElement = new Element(MAP_HEXES);
        e.addContent(mapHexesElement);
        
		for (int y = 0; y < getHeight(); y++)
		{
			for (int x = 0; x < getWidth(); x++)
			{
				CampaignMapHex mapHex = (CampaignMapHex)getHex(x, y);

				if (notDefaultMapHex(mapHex, defaultTerrainType))
				{
					mapHexesElement.addContent(mapHex.saveToElement());
				}
			}
		}

	}
	
	public void loadFromXMLElement(Element e) throws Exception
	{
		_RoadsAutoExit = Boolean.parseBoolean(e.getAttributeValue(ROADS_AUTO_EXIT));
        setSize(Integer.parseInt(e.getChild(WIDTH).getText()),Integer.parseInt(e.getChild(HEIGHT).getText()));
		
		String defaultTerrain = null;
		Element defaultTerrainElement = e.getChild(DEFAULT_TERRAIN);
		if (defaultTerrainElement != null)
			defaultTerrain = defaultTerrainElement.getText();
		
		TerrainType defaultTerrainType = TerrainType.fromString(defaultTerrain);
		setDefaultHex(defaultTerrainType);
		updateMapHexes(e.getChild(MAP_HEXES).getChildren(),  defaultTerrainType);
	}

}
