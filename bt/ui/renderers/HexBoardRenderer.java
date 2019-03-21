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

package bt.ui.renderers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import bt.mapping.Board;
import bt.mapping.Coordinate;
import bt.mapping.HexBoard;
import bt.mapping.HexGrid;
import bt.mapping.Hexagon;
import bt.mapping.MapHex;
import bt.ui.sprites.HexCursorSprite;
import bt.util.ExceptionUtil;

public abstract class HexBoardRenderer extends BoardRenderer
{
	private HexBoard _Board;
	private ArrayList<HexBoardHex> _BoardHexes = new ArrayList<HexBoardHex>();
	private Area _Background;

	private HexCursorSprite _Cursor;

	public HexBoardRenderer()
	{
	}

	public void addMapElements()
	{
	}

	public final HexGrid getHexGrid()
	{
		return _Board.getHexGrid();
	}

	@Override
	protected Dimension getRenderSurfaceSize()
	{
		if (_Board != null && _Board.getHexGrid() != null)
		{
			return _Board.getHexGrid().getGridDimension();
		} else
		{
			return new Dimension(500, 500);
		}
	}

	public abstract BufferedImage getTerrainImage(MapHex mapHex);
	public abstract ArrayList<BufferedImage> getFeatureImages(MapHex mapHex);
	public abstract Color getTextColour(MapHex mapHex);
	public abstract Color getHexBorderColour();

	@Override
	protected void setBoard(Board board)
	{
		_Board = (HexBoard)board;

		try
		{
			_BoardHexes.clear();

			_Background = new Area(new Rectangle(_Board.getHexGrid().getGridDimension()));

			for (int x = 0; x < _Board.getWidth(); x++)
			{
				for (int y = 0; y < _Board.getHeight(); y++)
				{
					MapHex theMapHex = _Board.getMapHex(x, y);
					if (theMapHex != null)
					{
						HexBoardHex boardHex = getBoardHex(theMapHex, x, y);
						boardHex.prepareBoardHex();
						_Background.subtract(new Area(boardHex.getHexagon()));
						_BoardHexes.add(boardHex);
					}
				}
			}
		} 
		catch (Exception ex)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}

	public HexBoardHex getBoardHex(int index)
	{
		return _BoardHexes.get(index);
	}

	private HexBoardHex getBoardHexByCoordinate(Coordinate coord)
	{
		for (HexBoardHex bh : _BoardHexes)
			if (bh._BoardCoordinate.equals(coord))
				return bh;
		
		return null;
	}
	
	public void updateMapHex(Coordinate location, MapHex mapHex)
	{
		HexBoardHex bh = getBoardHexByCoordinate(location);
		bh.updateMapHex(mapHex);
		repaint();
	}

	@Override
	public void draw(Graphics2D g, ImageObserver obs)
	{
		if (_Background == null)
		{
			return;
		}

		if (_BoardHexes == null)
		{
			return;
		}

		if (_BoardHexes.isEmpty())
		{
			return;
		}

		Rectangle2D clipRect = g.getClipBounds();

		for (int i = 0; i < _BoardHexes.size(); i++)
		{
			BoardHex bh = _BoardHexes.get(i);
			if (bh.intersects(clipRect))
			{
				bh.draw(g, obs);
			}
		}

		getSpriteManager().drawSprites(g, obs, clipRect.getBounds());
	}

	public Hexagon getHex(Coordinate c)
	{
		return _Board.getHexGrid().getHex(c.x, c.y);
	}

	public Hexagon getHex(int x, int y)
	{
		return _Board.getHexGrid().getHex(x, y);
	}

	@Override
	public void setCursorLocation(Point p)
	{
		double factor = getZoomLevel().getFactor();
		p.x /= factor;
		p.y /= factor;
		setCursorLocation(_Board.getHexGrid().getHexFromPoint(p));
	}
	
	public Coordinate getCoordinateFromPoint(Point p)
	{
		double factor = getZoomLevel().getFactor();
		p.x /= factor;
		p.y /= factor;
		return _Board.getHexGrid().getCoordinateFromPoint(p);
	}	

	public void setCursorLocation(Coordinate coordinate)
	{
		setCursorLocation(getHexGrid().getHex(coordinate));
	}

	public void setCursorLocation(Hexagon hex)
	{
		if (hex != null)
		{
			if (_Cursor == null)
			{
				_Cursor = new HexCursorSprite(this, Color.CYAN, _Board.getHexGrid());
				getSpriteManager().registerCursor(_Cursor);
			}
			_Cursor.setHexLocation(_Board.getHexGrid().getCoordinateFromHex(hex));
			_Cursor.setVisible(true);
		} 
		else if (_Cursor != null)
		{
			_Cursor.setVisible(false);
		}
	}

	// Note that thsi can return null
	public Coordinate getCursorLocation()
	{
		if (_Cursor == null)
		{
			return null;
		}
		if (!_Cursor.isVisible())
		{
			return null;
		}
		return _Cursor.getCoordinate();
	}

	public abstract HexBoardHex getBoardHex(MapHex mapHex, int MapX, int MapY);



}
