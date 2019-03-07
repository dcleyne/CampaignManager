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
 * This code derived from internal class CursorSprite contained in class
 * BoardView1 from the Megamek project
 * http://megamek.sourceforge.net/idx.php?pg=main
 * 
 * @author Megamek project
 ******************************************************************************/

package bt.ui.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;

import bt.mapping.Coordinate;
import bt.mapping.HexGrid;
import bt.ui.filters.TintFilter;
import bt.ui.renderers.HexBoardRenderer;
import bt.util.Hexagon;
import bt.util.ImageUtil;

/**
 * Sprite for a cursor. Just a hexagon outline in a specified color.
 */
public class HexCursorSprite extends Sprite
{
	private Color _Colour = null;
	private HexGrid _Grid = null;
	private Area _Area = null;
	private Hexagon _Hex = null;
	private Coordinate _Coordinate = null;

	private String _BrushImageName = "25percentbrush.gif";
	
	public HexCursorSprite(HexBoardRenderer renderer, Color colour, HexGrid grid)
	{
		super(renderer);

		_Colour = colour;
		_Grid = grid;
		_Hex = _Grid.getHex(0, 0);
		Rectangle bounds = _Hex.getBounds();
		_Area = createArea(bounds.x, bounds.y);
	}

	public Coordinate getCoordinate()
	{
		return _Coordinate;
	}

	public Hexagon getHex()
	{
		return _Hex;
	}

	public Color getColour()
	{
		return _Colour;
	}

	@Override
	public Image drawContent(Graphics2D graph, Image tempImage, Rectangle bounds)
	{
		graph.translate(0 - bounds.x, 0 - bounds.y);

		BufferedImage brush = ImageUtil.loadImage(_BrushImageName);
		TexturePaint paint = new TexturePaint(brush, new Rectangle2D.Float(0.0f, 0.0f, 16.0f, 16.0f));
		graph.setPaint(paint);
		graph.fill(_Area);
		
		graph.setPaint(getColour());
		graph.draw(_Area);
		
		graph.translate(bounds.x, bounds.y);
		
		tempImage = createImage(new FilteredImageSource(tempImage.getSource(), new TintFilter(Color.WHITE.getRGB(), getColour().getRGB())));

		return tempImage;
	}

	public void setHexLocation(Coordinate location)
	{
		repaint();
		_Coordinate = location;
		_Hex = _Grid.getHex(location);
		Rectangle bounds = _Hex.getBounds();
		_Area = createArea(bounds.x, bounds.y);
		repaint();
	}

	@Override
	public Rectangle getBounds()
	{
		return _Hex.getBounds();
	}

	public void setColour(Color colour)
	{
		_Colour = colour;
		_Image = null;
		repaint();
	}

	private Area createArea(int xOffset, int yOffset)
	{
		Area area = new Area(new Hexagon(xOffset, yOffset, _Hex.getMainDimension(), _Hex.isVertical()));
		area.subtract(new Area(getInnerHex(xOffset, yOffset)));

		return area;
	}

	public Polygon getInnerHex(int xOffset, int yOffset)
	{
		Polygon p = new Polygon();

		p.addPoint(xOffset + 25, yOffset + 7);
		p.addPoint(xOffset + 58, yOffset + 7);
		p.addPoint(xOffset + 75, yOffset + 35);
		p.addPoint(xOffset + 75, yOffset + 36);
		p.addPoint(xOffset + 58, yOffset + 64);
		p.addPoint(xOffset + 25, yOffset + 64);
		p.addPoint(xOffset + 8, yOffset + 36);
		p.addPoint(xOffset + 8, yOffset + 35);
		p.addPoint(xOffset + 24, yOffset + 7);
		
		return p;
	}
}
