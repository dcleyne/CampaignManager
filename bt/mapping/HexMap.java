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

import java.util.StringTokenizer;

public abstract class HexMap extends Map
{
	static final long serialVersionUID = 1;

	private MapHex[] _Hexes;

	public HexMap()
	{
		_Hexes = new MapHex[0];
	}

	protected HexMap(int Width, int Height)
	{
	}

	public MapHex getHex(int Index)
	{
		return _Hexes[Index];
	}

	public MapHex getHex(int x, int y)
	{
		return _Hexes[y * getWidth() + x];
	}

	public MapHex getHex(Coordinate coord)
	{
		return getHex(coord.x, coord.y);
	}
	
	public void setHex(MapHex mapHex)
	{
		int x = mapHex.getCoordinate().x;
		int y = mapHex.getCoordinate().y;
		_Hexes[y * getWidth() + x] = mapHex;
	}

	public int getHexCount()
	{
		return _Hexes.length;
	}

	public void setHex(int Index, MapHex Hex)
	{
		_Hexes[Index] = Hex;
	}

	public void setHex(String Index, MapHex Hex)
	{
		int idx = indexFor(Index);
		setHex(idx, Hex);
	}

	public void setHex(int Column, int Row, MapHex Hex)
	{
		int idx = Row * getWidth() + Column;
		setHex(idx, Hex);
	}

	public abstract int getWidth();
	public abstract int getHeight();
	
	public void setSize(int width, int height) throws Exception
	{
		_Hexes = new MapHex[width * height];		
	}

	@Override
	public String toString()
	{
		// for debugging
		String output = "Map: " + getName() + "/n";
		MapHex hex;
		for (int i = 0; i < getHexCount(); i++)
		{
			hex = _Hexes[i];
			output += "MapHex index:" + String.valueOf(i) + "; ";
			output += "Coordinate:" + hex.getCoordinate().toString() + "; ";
			output += "Terrain:" + hex.getTerrainType() + "; ";
			output += "/n";
		}

		return output;

	}

	public boolean isValidGameBoard()
	{
		if (getWidth() <= 1)
		{
			return false;
		}
		if (getHeight() <= 1)
		{
			return false;
		}

		return true;
	}

	protected int indexFor(String hexNum)
	{
		int x = 0;
		int y = -1;
		StringTokenizer st = new StringTokenizer(hexNum, ",");
		for (int i = 0; i < 2 && st.hasMoreTokens(); i++)
		{
			if (i == 0)
			{
				x = Integer.parseInt(st.nextToken().trim()) - 1;
			} else
			{
				y = Integer.parseInt(st.nextToken().trim()) - 1;
			}
		}
		return y * getWidth() + x;
	}

	/**
	 * Code from Megamek Gets the hex in the specified direction from the
	 * specified starting coordinates.
	 * 
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
		return getHex(Coordinate.xInDir(x, y, direction), Coordinate.yInDir(x, y, direction));
	}

	protected abstract void setDefaultHex(TerrainType DefaultTerrain);
	
}
