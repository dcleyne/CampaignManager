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

import bt.mapping.MapHex;
import bt.mapping.TerrainType;

public class CampaignMapHex extends MapHex
{
	private static final String INDEX = "Index";
	private static final String ROAD = "Road";
	private static final String RIVER = "River";

	static final long serialVersionUID = 1;

	private int[] _Road = new int[6];;
	private int[] _River = new int[6];

	public CampaignMapHex()
	{
		super();
	}

	public CampaignMapHex(int x, int y)
	{
		super(x, y);
	}

	public CampaignMapHex(int x, int y, TerrainType terrainType)
	{
		super(x, y, terrainType);
	}

	public void setRiver(int HexSide, int value)
	{
		_River[HexSide] = value;
	}

	public void setRiver(int[] River)
	{
		for (int i = 0; i < River.length && i < _River.length; i++)
		{
			_River[i] = River[i];
		}
	}

	public void setRoad(int HexSide, int value)
	{
		_Road[HexSide] = value;
	}

	public void setRoad(int[] Road)
	{
		for (int i = 0; i < Road.length && i < _Road.length; i++)
		{
			_Road[i] = Road[i];
		}
	}

	public String roadToString()
	{
		String output = "";
		for (int i = 0; i < _Road.length; i++)
		{
			output += String.valueOf(_Road[i]) + ", ";
		}

		return output;
	}

	public String riverToString()
	{
		String output = "";
		for (int i = 0; i < _River.length; i++)
		{
			output += String.valueOf(_River[i]) + ", ";
		}

		return output;
	}

	public int getRoad(int HexSide)
	{
		return _Road[HexSide];
	}
	
	public int getRoadSides()
	{
		int sides = 0;
		for (int i = 0; i < 6; i++)
			sides += _Road[i];
		return sides;
	}

	public int getRiver(int HexSide)
	{
		return _River[HexSide];
	}

	public int getRiverSides()
	{
		int sides = 0;
		for (int i = 0; i < 6; i++)
			sides += _River[i];
		return sides;
	}

	private Element createElement(String name, int index, String value)
	{
		Element e = new Element(name);
		e.setAttribute(INDEX, Integer.toString(index));
		e.setText(value);
		return e;
	}
	
	
	public void saveToXMLElement(Element e)
	{
		super.saveToXMLElement(e);
		
		for (int index = 0; index < 6; index++)
		{
			e.addContent(createElement(ROAD, index, Integer.toString(_Road[index])));
			e.addContent(createElement(RIVER, index, Integer.toString(_River[index])));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadIntegerElement(Element e, String name, int[] array)
	{
		// This is to cope with the data file format on disk where the elements are stored differently
		String parentName = name + "s";
		if (e.getChild(parentName) != null)
		{
			Element parentElement = e.getChild(parentName);
			name += "Edge";
			
			List<Element> elements = (List<Element>)parentElement.getChildren(name);
			for (int i = 0; i < elements.size(); i++)
			{
				int index = Integer.parseInt(elements.get(i).getText()) - 1;
				array[index] = 1;
			}
		}
		else
		{
			for (Element intElement : (List<Element>)e.getChildren(name))
			{
				int index = Integer.parseInt(intElement.getAttributeValue(INDEX));
				int value = Integer.parseInt(intElement.getText());
				array[index] = value;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void loadBooleanElement(Element e, String name, boolean[] array)
	{
		for (Element intElement : (List<Element>)e.getChildren(name))
		{
			int index = Integer.parseInt(intElement.getAttributeValue(INDEX));
			boolean value = Boolean.parseBoolean(intElement.getText());
			array[index] = value;
		}
	}
	
	public void loadFromXMLElement(Element e) throws Exception
	{
		super.loadFromXMLElement(e);
		
		loadIntegerElement(e, ROAD, _Road);
		loadIntegerElement(e, RIVER, _River);
	}

}
