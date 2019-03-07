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
import java.util.List;

import org.jdom.Element;

import bt.mapping.Coordinate;
import bt.mapping.HexBoard;
import bt.mapping.HexMap;
import bt.mapping.Map;
import bt.ui.renderers.MapFactory;
import bt.util.ExceptionUtil;

public class CampaignBoard extends HexBoard
{
	private static final String CAMPAIGN = "Campaign";
	private static final String FILE_NAME = "FileName";
	private static final String LOCATION = "Location";
	private static final String MAP = "Map";
	private static final String NAME = "Name";
	private static final String VERSION = "Version";
	static final long serialVersionUID = 1;

	private int _Version = 1;
	
	public int getVersion()
	{
		return _Version;
	}
	
	public CampaignBoard(int hexDimension)
	{
		super(hexDimension);
	}
	
	@Override
	public void loadFromDataElement(Element e) throws Exception
	{
		List<?> CenturionMap = e.getChildren();
		for (int i = 0; i < CenturionMap.size(); i++)
		{
			Element val = (Element) CenturionMap.get(i);
			if (val.getName().equalsIgnoreCase(NAME))
			{
				setName(val.getValue());
			}
			if (val.getName().equalsIgnoreCase(VERSION))
			{
				_Version = Integer.parseInt(val.getValue());
			}
			if (val.getName().equalsIgnoreCase(MAP))
			{
				addMap(val);
			}
		}
	}

	@Override
	public void saveToDataElement(org.jdom.Element e)
	{
		e.addContent(new Element(NAME).setText(getName()));
		e.addContent(new Element(VERSION).setText(Integer.toString(getVersion())));
		
		Element mapElement = new Element("EmbeddedMap");
		
		mapElement.addContent(new Element("Name").setText("Map 1"));
		mapElement.addContent(new Element("Location").setText("1,1"));
		
		
		e.addContent(mapElement);
	}

	@Override
	public void addMap(Map map, Coordinate coord) throws Exception
	{
		super.addMap(map, coord);

	}

	protected void addMap(Element mapElement)
	{
		try
		{
			// String MapName = mapElement.getChild("Name").getValue();
			String mapFileName = mapElement.getChild(FILE_NAME).getValue();
			String location = mapElement.getChild(LOCATION).getValue();

			HexMap map = (HexMap)MapFactory.INSTANCE.getMap(CAMPAIGN, mapFileName);
			String[] coords = location.split(",");
			Coordinate c = new Coordinate(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));

			addMap(map, c);
		} catch (Exception ex)
		{
			System.out.println("Failed adding map while loading board " + getName() + " : " + ExceptionUtil.getExceptionStackTrace(ex));
		}
	}
	
	public void updateMapHex(Coordinate location, CampaignMapHex mapHex)
	{
		super.updateMapHex(location, mapHex);
	}

	public void saveToXMLElement(org.jdom.Element e)
	{
		super.saveToXMLElement(e);
		
		e.setAttribute(VERSION, Integer.toString(_Version));

	}

	public void loadFromXMLElement(org.jdom.Element e) throws Exception
	{		
		super.loadFromXMLElement(e);
		
		_Version = Integer.parseInt(e.getAttributeValue(VERSION));

	}

}
