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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jdom.Element;

public abstract class HexBoard extends Board
{
	private static final String WIDTH = "Width";
	private static final String HEIGHT = "Height";
	private static final String MAP_WIDTH = "MapWidth";
	private static final String MAP_HEIGHT = "MapHeight";
	private static final String MAP_TYPE = "MapType";
	private static final String MAPS = "Maps";
	private static final String NUM_MAPS = "NumMaps";
	private static final String INDEX = "Index";
	private static final String MAP_REFERENCES = "MapReferences";
	private static final String MAP_REFERENCE = "MapReference";
	private static final String HEX_REFERENCES = "HexReferences";
	private static final String HEX_REFERENCE = "HexReference";
	private static final String X = "X";
	private static final String Y = "Y";
	private static final String MAP_INDEX = "MapIndex";
	private static final String MAP_X = "MapX";
	private static final String MAP_Y = "MapY";

	static final long serialVersionUID = 1;

	private int _Width;
	private int _Height;
	private int _MapWidth;
	private int _MapHeight;
	private String _MapType;
	private ArrayList<HexMap> _Maps = new ArrayList<HexMap>();
	
	private HashMap<Coordinate, HexMap> _MapReferences; // These are the
															// locations of the
															// map withing the
															// grid
	private HashMap<Coordinate, MapReference> _HexReferences; // These are the
																// mappings
																// between board
																// hexes and map
																// hexes
	private HexGrid _HexGrid = null;
	
	
	public HexBoard()
	{
		clear();
	}
	
	@Override
	public String getMapType()
	{
		return _MapType;
	}

	@Override
	public void clear()
	{
		_Width = 0;
		_Height = 0;
		_MapWidth = 0;
		_MapHeight = 0;
		_MapType = "";
		_MapReferences = new HashMap<Coordinate, HexMap>();
		_HexReferences = new HashMap<Coordinate, MapReference>();
	}

	@Override
	public void addMap(Map map, Coordinate coord) throws Exception
	{
		addMap((HexMap) map, coord);
	}

	public void addMap(HexMap map, Coordinate coord) throws Exception
	{
		if (_MapReferences.containsKey(coord))
		{
			throw new Exception("Map already loaded into this position (" + coord.toString() + ") on the Board : " + coord.toString());
		}

		if (_MapType.length() == 0)
		{
			_MapType = map.getMapType();
		} 
		else if (!_MapType.equalsIgnoreCase(map.getMapType()))
		{
			throw new Exception("All maps in a game board must be of the same type. Map : " + map.getName() + " is not of type " + _MapType);
		}

		if (_MapWidth == 0)
		{
			_MapWidth = map.getWidth();
		} 
		else if (_MapWidth != map.getWidth())
		{
			throw new Exception("All maps in a game board must be of the same size and orientation. Map : " + map.getName() + " differs in width from maps loaded in this board");
		}

		if (_MapHeight == 0)
		{
			_MapHeight = map.getHeight();
		} 
		else if (_MapHeight != map.getHeight())
		{
			throw new Exception("All maps in a game board must be of the same size and orientation. Map : " + map.getName() + " differs in height from maps loaded in this board");
		}

		if (coord.x > _Width)
		{
			_Width = coord.x;
		}
		if (coord.y > _Height)
		{
			_Height = coord.y;
		}

		_Maps.add(map);
		_MapReferences.put(coord, map);

		int XOffset = (coord.x - 1) * (_MapWidth);
		int YOffset = (coord.y - 1) * (_MapHeight - 1);

		for (int x = 0; x < _MapWidth; x++)
		{
			for (int y = 0; y < _MapHeight; y++)
			{
				Coordinate mapHex = new Coordinate(x, y);
				Coordinate boardHex = new Coordinate(XOffset + x, YOffset + y);

				if (_HexReferences.containsKey(boardHex))
				{
					_HexReferences.remove(boardHex);
				}

				MapReference mr = new MapReference(map, mapHex);
				_HexReferences.put(boardHex, mr);
			}
		}
	}

	/*
	 * This function must be called after a series of addMapCalls
	 */
	@Override
	public void completedAddingMaps()
	{
		_HexGrid = new HexGrid(getWidth(), getHeight(), 1, 1);
	}

	public final HexGrid getHexGrid()
	{
		return _HexGrid;
	}

	public boolean isValidBoard()
	{
		Set<Coordinate> keys = _MapReferences.keySet();

		for (int x = 0; x < _Width; x++)
		{
			for (int y = 0; y < _Height; y++)
			{
				Coordinate coord = new Coordinate(x, y);
				if (!keys.contains(coord))
				{
					return false;
				}
			}
		}

		return true;
	}

	public MapHex getMapHex(int x, int y)
	{
		Coordinate coord = new Coordinate(x, y);
		return getMapHex(coord);
	}

	public MapHex getMapHex(Coordinate coord)
	{
		MapReference mr = _HexReferences.get(coord);
		if (mr != null)
		{
			HexMap map = mr.getMap();
			if (map != null)
			{
				return map.getHex(mr.getCoordinate());
			}
		}
		return null;
	}
	
	public void updateMapHex(MapHex mapHex)
	{
		updateMapHex(mapHex.getCoordinate(), mapHex);
	}

	public void updateMapHex(Coordinate location, MapHex mapHex)
	{
		MapReference mr = _HexReferences.get(location);
		if (mr != null)
		{
			HexMap map = mr.getMap();
			if (map != null)
			{
				map.setHex(mapHex);
			}
		}
	}

	/**
	 * Code from Megamek Gets the hex in the specified direction from the
	 * specified starting coordinates.
	 * @param c
	 * 	The coordinate to check
	 * @param direction
	 * 	The direction in which to get the MapHex
	 * @return The MapHex in the specified direction
	 */
	public MapHex getHexInDirection(Coordinate c, int direction)
	{
		return getHexInDirection(c.x, c.y, direction);
	}

	/**
	 * Code from Megamek Gets the hex in the specified direction from the
	 * specified starting coordinates.
	 * 
	 * Avoids calls to Coords.translated, and thus, object construction.
	 * @param x
	 * 	The x component of the coordinate
	 * @param y
	 * 	The y component of the coordinate
	 * @param direction
	 * 	The direction in which to get the MapHex
	 * @return The MapHex in the specified direction
	 */
	public MapHex getHexInDirection(int x, int y, int direction)
	{
		return getMapHex(Coordinate.xInDir(x, y, direction), Coordinate.yInDir(x, y, direction));
	}

	/**
	 * this routine returns the direction of an adjacent hex
	 * 
	 * @param source
	 * 	The source coordinate to check
	 * @param target
	 * 	The target coordinate to check
	 * @return the directed of the target coordinate relative to the source
	 *         coordinate
	 */
	public int getDirectionOfHex(Coordinate source, Coordinate target)
	{
		for (int i = 0; i < 6; i++)
		{
			int x = Coordinate.xInDir(source.x, source.y, i);
			int y = Coordinate.yInDir(source.x, source.y, i);
			if (x == target.x && y == target.y)
			{
				return i;
			}
		}

		return 0;
	}

	public int getOppositeDir(int direction)
	{
		int dir = direction - 3;
		if (dir < 0)
		{
			dir += 6;
		}
		return dir;
	}

	public int getWidth()
	{
		return _Width * (_MapWidth);
	}

	public int getHeight()
	{
		return _Height * (_MapHeight - 1) + 1;
	}

	private class MapReference implements Serializable
	{
		static final long serialVersionUID = 1;

		private HexMap _Map;
		private Coordinate _Coordinate;

		public MapReference(HexMap map, Coordinate coord)
		{
			_Map = map;
			_Coordinate = coord;
		}

		public HexMap getMap()
		{
			return _Map;
		}

		public Coordinate getCoordinate()
		{
			return _Coordinate;
		}
	}

	public ArrayList<Coordinate> getInterveningCoordinates(Coordinate sourceCoord, Coordinate targetCoord)
	{
		return getHexGrid().intervening(sourceCoord, targetCoord, false);
	}

	public int getDistanceBetweenLocations(Coordinate sourceCoord, Coordinate targetCoord)
	{
		return Coordinate.distance(sourceCoord, targetCoord);
	}

	public boolean isOffboardCoordinate(Coordinate coord)
	{
		if (coord.x < 0)
		{
			return true;
		}
		if (coord.y < 0)
		{
			return true;
		}
		if (coord.x >= getWidth())
		{
			return true;
		}
		if (coord.y >= getHeight())
		{
			return true;
		}

		return false;
	}
	
	public ArrayList<Coordinate> getHexesClosestTo(Coordinate targetCoordinate, ArrayList<Coordinate> eligibleCoordinates, int number)
	{
		ArrayList<Coordinate> results = new ArrayList<Coordinate>();

		HashMap<Integer, ArrayList<Coordinate>> distances = new HashMap<Integer, ArrayList<Coordinate>>();

		for (Coordinate coord : eligibleCoordinates)
		{
			Integer distance = getDistanceBetweenLocations(coord, targetCoordinate);
			if (!distances.containsKey(distance))
			{
				distances.put(distance, new ArrayList<Coordinate>());
			}

			distances.get(distance).add(coord);
		}

		ArrayList<Integer> distanceKeys = new ArrayList<Integer>(distances.keySet());
		Collections.sort(distanceKeys);

		int index = 0;
		while (results.size() < number && index < distanceKeys.size())
		{
			int elementsToAdd = number - results.size();
			ArrayList<Coordinate> coords = distances.get(distanceKeys.get(index));

			if (coords.size() < elementsToAdd)
			{
				results.addAll(coords);
			} else
			{
				for (int i = 0; i < elementsToAdd; i++)
				{
					results.add(coords.get(i));
				}
			}

			index++;
		}

		return results;
	}
	
	
	public void saveToXMLElement(org.jdom.Element e)
	{
		e.setAttribute(WIDTH, Integer.toString(_Width));
		e.setAttribute(HEIGHT, Integer.toString(_Height));
		e.setAttribute(MAP_WIDTH, Integer.toString(_MapWidth));
		e.setAttribute(MAP_HEIGHT, Integer.toString(_MapHeight));
		e.setAttribute(MAP_TYPE, _MapType);

		org.jdom.Element mapsElement = new Element(MAPS);
		mapsElement.setAttribute(NUM_MAPS, Integer.toString(_Maps.size()));
		for (int index = 0; index < _Maps.size(); index++)
		{
			HexMap hexMap = _Maps.get(index);
			org.jdom.Element hexMapElement = hexMap.saveToElement();
			hexMapElement.setAttribute(INDEX, Integer.toString(index));
			
			mapsElement.addContent(hexMapElement);	
		}
		e.addContent(mapsElement);

		org.jdom.Element mapReferencesElement = new Element(MAP_REFERENCES);
		
		for (Coordinate coord : _MapReferences.keySet())
		{
			org.jdom.Element mapReferenceElement = new Element(MAP_REFERENCE);
			mapReferenceElement.setAttribute(X, Integer.toString(coord.x));
			mapReferenceElement.setAttribute(Y, Integer.toString(coord.y));
			mapReferenceElement.setAttribute(MAP_INDEX, Integer.toString(_Maps.indexOf(_MapReferences.get(coord))));
			
			mapReferencesElement.addContent(mapReferenceElement);
		}
		
		e.addContent(mapReferencesElement);
		
		org.jdom.Element hexReferencesElement = new Element(HEX_REFERENCES);
		for (Coordinate coord : _HexReferences.keySet())
		{
			org.jdom.Element hexReferenceElement = new Element(HEX_REFERENCE);
			
			hexReferenceElement.setAttribute(X, Integer.toString(coord.x));
			hexReferenceElement.setAttribute(Y, Integer.toString(coord.y));
			
			MapReference reference = _HexReferences.get(coord);
			hexReferenceElement.setAttribute(MAP_INDEX, Integer.toString(_Maps.indexOf(reference.getMap())));
			hexReferenceElement.setAttribute(MAP_X, Integer.toString(reference.getCoordinate().x));
			hexReferenceElement.setAttribute(MAP_Y, Integer.toString(reference.getCoordinate().y));
			
			hexReferencesElement.addContent(hexReferenceElement);
		}
		e.addContent(hexReferencesElement);				
	}
	
	@SuppressWarnings("unchecked")
	public void loadFromXMLElement(org.jdom.Element e) throws Exception
	{
		_Width = Integer.parseInt(e.getAttributeValue(WIDTH));
		_Height = Integer.parseInt(e.getAttributeValue(HEIGHT));
		_MapWidth = Integer.parseInt(e.getAttributeValue(MAP_WIDTH));
		_MapHeight = Integer.parseInt(e.getAttributeValue(MAP_HEIGHT));
		_MapType = e.getAttributeValue(MAP_TYPE);
		
		_Maps.clear();
		Element mapsElement = e.getChild(MAPS);
		int numMaps = Integer.parseInt(mapsElement.getAttributeValue(NUM_MAPS));
		HexMap[] hexMaps = new HexMap[numMaps];
		for (Element hexMapElement : (List<Element>)mapsElement.getChildren(Map.MAP))
		{
			int mapIndex = Integer.parseInt(hexMapElement.getAttributeValue(INDEX));
			hexMaps[mapIndex] = (HexMap)Map.loadMapFromElement(hexMapElement);
		}
		_Maps.addAll(Arrays.asList(hexMaps));
		
		_MapReferences.clear();
		Element mapReferencesElement = e.getChild(MAP_REFERENCES);
		for (Element mapReferenceElement : (List<Element>)mapReferencesElement.getChildren(MAP_REFERENCE))
		{
			int x = Integer.parseInt(mapReferenceElement.getAttributeValue(X));
			int y = Integer.parseInt(mapReferenceElement.getAttributeValue(Y));
			int mapIndex = Integer.parseInt(mapReferenceElement.getAttributeValue(MAP_INDEX));
			
			_MapReferences.put(new Coordinate(x,y), _Maps.get(mapIndex));
		}
		
		_HexReferences.clear();
		Element hexReferencesElement = e.getChild(HEX_REFERENCES);
		for (Element hexReferenceElement : (List<Element>)hexReferencesElement.getChildren(HEX_REFERENCE))
		{		
			int x = Integer.parseInt(hexReferenceElement.getAttributeValue(X));
			int y = Integer.parseInt(hexReferenceElement.getAttributeValue(Y));
			int mapIndex = Integer.parseInt(hexReferenceElement.getAttributeValue(MAP_INDEX));
			int mapX = Integer.parseInt(hexReferenceElement.getAttributeValue(MAP_X));
			int mapY = Integer.parseInt(hexReferenceElement.getAttributeValue(MAP_Y));			
			
			MapReference mapReference = new MapReference(_Maps.get(mapIndex), new Coordinate(mapX, mapY));
			_HexReferences.put(new Coordinate(x,y), mapReference);
		}
		
		completedAddingMaps();
	}

}
