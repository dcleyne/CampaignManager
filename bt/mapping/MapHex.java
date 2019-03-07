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

package bt.mapping;

import org.jdom.Element;

public abstract class MapHex extends AbstractMapHex
{
	private static final String COORDINATE = "Coordinate";
	static final long serialVersionUID = 1;

	private Coordinate _Coordinate;

	public MapHex()
	{
		_Coordinate = new Coordinate(0, 0);
	}

	public MapHex(int x, int y)
	{
		_Coordinate = new Coordinate(x, y);
	}

	public MapHex(int x, int y, TerrainType terrainType)
	{
		super(terrainType);
		_Coordinate = new Coordinate(x, y);
	}

	public Coordinate getCoordinate()
	{
		return _Coordinate;
	}

	public void saveToXMLElement(Element e)
	{
    	e.addContent(new Element(COORDINATE).setText(String.format("%d,%d", _Coordinate.x + 1, _Coordinate.y + 1)));
	}
	
	public void loadFromXMLElement(Element e) throws Exception
	{
		String coordinate = e.getChild(COORDINATE).getText();
		String[] coordinateElements = coordinate.split(",");
		int x = Integer.parseInt(coordinateElements[0]) - 1;
		int y = Integer.parseInt(coordinateElements[1]) - 1;
		_Coordinate = new Coordinate(x,y);
	}
}
