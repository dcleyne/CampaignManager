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
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import bt.ui.renderers.HexBoardRenderer;

public class StraightArrowSprite extends Sprite
{
	private StraightArrow _Arrow = null;
	private Point _StartPoint = null;
	private Point _EndPoint = null;
	private boolean _Halved = false;

	private Color _FillColour = Color.lightGray;
	private Color _DrawColour = Color.darkGray;

	public StraightArrowSprite(HexBoardRenderer renderer)
	{
		super(renderer);
	}

	public StraightArrowSprite(HexBoardRenderer renderer, Point startPoint, Point endPoint, boolean halved)
	{
		super(renderer);

		setProperties(startPoint, endPoint, halved);
	}

	public StraightArrowSprite(HexBoardRenderer renderer, Point startPoint, Point endPoint, boolean halved, Color fillColour, Color drawColour)
	{
		super(renderer);

		setProperties(startPoint, endPoint, halved, fillColour, drawColour);
	}

	public void setProperties(Point startPoint, Point endPoint, boolean halved)
	{
		setProperties(startPoint, endPoint, halved, _FillColour, _DrawColour);
	}

	public void setProperties(Point startPoint, Point endPoint, boolean halved, Color fillColour, Color drawColour)
	{
		_StartPoint = startPoint;
		_EndPoint = endPoint;
		_Halved = halved;
		_Arrow = new StraightArrow(_StartPoint, _EndPoint, _Halved);
		_FillColour = fillColour;
		_DrawColour = drawColour;

		invalidate();
	}
	
	public void setColour(Color drawColour, Color fillColour)
	{
		_FillColour = fillColour;
		_DrawColour = drawColour;
		invalidate();
	}

	@Override
	protected Image drawContent(Graphics2D graph, Image tempImage, Rectangle bounds)
	{
		graph.setColor(_FillColour);
		graph.translate(0 - bounds.x, 0 - bounds.y);
		graph.fillPolygon(_Arrow);
		graph.setColor(_DrawColour);
		graph.drawPolygon(_Arrow);
		return tempImage;
	}

	@Override
	public Rectangle getBounds()
	{
		return _Arrow.getBounds();
	}

	public void setHalved(boolean halved)
	{
		if (_Halved != halved)
		{
			_Halved = halved;
			_Arrow = new StraightArrow(_StartPoint, _EndPoint, _Halved);
			_Image = null;
			invalidate();
		}
	}
}
