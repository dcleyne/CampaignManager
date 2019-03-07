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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

import bt.mapping.Board;
import bt.util.ExceptionUtil;


public abstract class BoardRenderer
{
	public enum FontSize
	{
		SEVEN, EIGHT, NINE, TEN, TWELVE
	}
	
	public enum ZoomLevel
	{
		ONE_QUARTER,
		HALF,
		THREE_QUARTERS,
		NORMAL,
		ONE_AND_A_QUARTER,
		ONE_AND_A_HALF,
		ONE_AND_THREE_QUARTERS,
		DOUBLE,
		TWO_AND_A_HALF;
		
		private static double[] Factors = {0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0, 2.5};
		
		public double getFactor()
		{
			return Factors[ordinal()];
		}
		
		public ZoomLevel zoomIn()
		{
			if (ordinal() < values().length - 1)
				return values()[ordinal() + 1];
			
			return DOUBLE;
		}
		
		public ZoomLevel zoomOut()
		{
			if (ordinal() > 1)
				return values()[ordinal() - 1];
			
			return HALF;
		}
	}

	private Board _Board;
	private Font[] _Fonts = new Font[FontSize.values().length];
	protected BoardRendererView _View = null;
	private SpriteManager _SpriteManager = new SpriteManager();
	private ZoomLevel _CurrentZoom = ZoomLevel.NORMAL;

	public BoardRenderer()
	{
		initialise();
	}
	
	public final void setViewState()
	{ 
		if (_View != null)
		{
			_View.setSize(getSize());
			_View.invalidate();
			_View.repaint();
		}
	}
	
	public SpriteManager getSpriteManager()
	{
		return _SpriteManager;
	}

	protected void initialise()
	{
		setupFonts();
	}

	public final void clear()
	{
		initialise();
	}

	public final Board getBoard()
	{
		return _Board;
	}
	
	public final void setBoard(BoardRendererView view, Board board, String themeName)
	{
		_View = view;
		_View.setBoardRenderer(this);
		_Board = board;
		try
		{
			if (_Board != null)
			{
				setBoard(board);
				registerCursors();
				setViewState();
			}
		} catch (Exception ex)
		{
			throw new RuntimeException(ExceptionUtil.getExceptionStackTrace(ex));
		}		
	}

	public Dimension getSize()
	{
		Dimension dim = getRenderSurfaceSize();
		dim.width *= _CurrentZoom.getFactor();
		dim.height *= _CurrentZoom.getFactor();
		return dim;
	}

	protected abstract Dimension getRenderSurfaceSize();

	private void setupFonts()
	{
		_Fonts[FontSize.SEVEN.ordinal()] = new Font("SansSerif", Font.PLAIN, 7);
		_Fonts[FontSize.EIGHT.ordinal()] = new Font("SansSerif", Font.PLAIN, 8);
		_Fonts[FontSize.NINE.ordinal()] = new Font("SansSerif", Font.PLAIN, 9);
		_Fonts[FontSize.TEN.ordinal()] = new Font("SansSerif", Font.PLAIN, 10);
		_Fonts[FontSize.TWELVE.ordinal()] = new Font("SansSerif", Font.PLAIN, 12);
	}

	public Font getFont(FontSize size)
	{
		return _Fonts[size.ordinal()];
	}
	
	public void zoomIn()
	{
		setZoomLevel(_CurrentZoom.zoomIn());
	}

	public void zoomOut()
	{
		setZoomLevel(_CurrentZoom.zoomOut());
	}
	
	public void setZoomLevel(ZoomLevel newLevel)
	{
		if (newLevel == null)
			return;
		
		if (newLevel != _CurrentZoom)
		{
			_CurrentZoom = newLevel;
			_View.setSize(_View.getPreferredSize());
		}
	}
	
	public ZoomLevel getZoomLevel()
	{
		return _CurrentZoom;
	}
	
	public final void drawBoard(Graphics2D g, ImageObserver obs)
	{
		AffineTransform saveXform = g.getTransform();
		
		g.scale(_CurrentZoom.getFactor(), _CurrentZoom.getFactor());
		
		draw(g, obs);
		
		g.setTransform(saveXform);
	}
	
	public void repaint()
	{
		_View.repaint();
	}
	
	public void repaint(Rectangle bounds)
	{
		if (bounds != null)
		{
			int x = (int)(bounds.x * _CurrentZoom.getFactor());
			int y = (int)(bounds.y * _CurrentZoom.getFactor());
			int width = (int)(bounds.width * _CurrentZoom.getFactor());
			int height = (int)(bounds.height * _CurrentZoom.getFactor());
			_View.repaint(x, y, width, height);
		}
		else
			_View.repaint();
	}
	
	public Point translatePoint(Point p)
	{
		Point newPoint = new Point();
		newPoint.x = (int)(p.x * _CurrentZoom.getFactor());
		newPoint.y = (int)(p.y * _CurrentZoom.getFactor());
		return newPoint;
	}
	
	public Image createImage(int width, int height)
	{
		return _View.createImage(width, height);
	}

	public Image createImage(ImageProducer producer)
	{
		return _View.createImage(producer);
	}
	
	public Graphics getGraphics()
	{
		return _View.getGraphics();
	}
	
	public Component getComponent()
	{
		return _View;
	}

	protected abstract void registerCursors();
	
	protected abstract void setBoard(Board board);
	protected abstract void draw(Graphics2D g, ImageObserver obs);

	public abstract void setCursorLocation(Point p);	
}
