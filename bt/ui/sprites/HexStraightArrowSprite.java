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

package bt.ui.sprites;

import java.awt.Color;
import java.awt.Point;

import bt.mapping.Hexagon;
import bt.ui.renderers.HexBoardRenderer;

/*
 * Simple subclass of StraightArrowSprite to take a start and end hex and work
 * out their centres.
 */
public class HexStraightArrowSprite extends StraightArrowSprite
{
	private Hexagon _StartHex = null;
	private Hexagon _EndHex = null;

	public HexStraightArrowSprite(HexBoardRenderer renderer, Hexagon startHex, Hexagon endHex, boolean halved)
	{
		super(renderer);

		Point startPoint = startHex.getCenter();
		Point endPoint = endHex.getCenter();

		if (startPoint.equals(endPoint))
		{
			startPoint = startHex.getHexsideMidPoint(0);
		}

		super.setProperties(startPoint, endPoint, halved);

		_StartHex = startHex;
		_EndHex = endHex;
	}

	public HexStraightArrowSprite(HexBoardRenderer renderer, Hexagon startHex, Hexagon endHex, boolean halved, Color fillColour, Color drawColour)
	{
		super(renderer);

		Point startPoint = startHex.getCenter();
		Point endPoint = endHex.getCenter();

		if (startPoint.equals(endPoint))
		{
			startPoint = startHex.getHexsideMidPoint(0);
		}

		super.setProperties(startPoint, endPoint, halved, fillColour, drawColour);
		_StartHex = startHex;
		_EndHex = endHex;
	}

	public Hexagon getStartHex()
	{
		return _StartHex;
	}

	public Hexagon getEndHex()
	{
		return _EndHex;
	}	
}
