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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import bt.mapping.MapHex;
import bt.util.ExceptionUtil;

public abstract class BoardHex
{
	protected HexBoardRenderer _Renderer;
	protected MapHex _MapHex;
	protected BufferedImage _TerrainImage;
	protected ArrayList<BufferedImage> _FeatureImages = new ArrayList<BufferedImage>();
	protected ArrayList<BufferedImage> _GeneratedImages = null;

	protected Point2D.Double _TopLeftPoint = null;
	protected Color _TextColour = null;
	protected Color _HexBorderColour = null;
	private Point2D.Float _LabelPoint = null;
	private boolean _DrawLabel = true; 

	private Rectangle2D _IntersectRect = null;
	
	public BoardHex(HexBoardRenderer renderer, MapHex theMapHex)
	{
		_Renderer = renderer;
		_MapHex = theMapHex;
	}
	
	public void prepareBoardHex()
	{
		try
		{
			_TopLeftPoint = new Point2D.Double(getTopLeftX(), getTopLeftY());
			_TerrainImage = _Renderer.getTerrainImage(_MapHex);
			_FeatureImages = _Renderer.getFeatureImages(_MapHex);
			_TextColour = _Renderer.getTextColour(_MapHex);
			_HexBorderColour = _Renderer.getHexBorderColour();
	
			_IntersectRect = new Rectangle2D.Double(_TopLeftPoint.x, _TopLeftPoint.y, getWidth(), getHeight());
		} 
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println(ExceptionUtil.getExceptionStackTrace(e));
		}
	}
	
	protected abstract double getTopLeftX();
	protected abstract double getTopLeftY();
	protected abstract double getWidth();
	protected abstract double getHeight();
		
	public void setLabelDraw(boolean drawLabel)
	{
		_DrawLabel = drawLabel;
	}
	
	public void updateMapHex(MapHex theMapHex)
	{
		try
		{
			_MapHex = theMapHex;
			_TerrainImage = _Renderer.getTerrainImage(_MapHex);
			_FeatureImages = _Renderer.getFeatureImages(_MapHex);
			_TextColour = _Renderer.getTextColour(_MapHex);
		} 
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println(ExceptionUtil.getExceptionStackTrace(e));
		}
	}

	public boolean intersects(Rectangle2D rect)
	{
		if (_IntersectRect == null)
		{
			return false;
		}
		return _IntersectRect.intersects(rect);
	}

	protected void drawImage(Graphics2D g, ImageObserver obs)
	{
		g.drawImage(_TerrainImage, (int) _TopLeftPoint.x, (int) _TopLeftPoint.y, obs);
	}

	protected void drawFeatures(Graphics2D g, ImageObserver obs)
	{
		for (int i = 0; i < _FeatureImages.size(); i++)
		{
			g.drawImage(_FeatureImages.get(i), (int) _TopLeftPoint.x, (int) _TopLeftPoint.y, obs);
		}
	}
	
	protected abstract String getLabel();

	protected void drawLabel(Graphics2D g)
	{
		if (!_DrawLabel)
			return;
		
		Font currentFont = g.getFont();
		g.setFont(_Renderer.getFont(BoardRenderer.FontSize.NINE));

		String label = getLabel();
		if (label.isEmpty())
			return;
		
		if (_LabelPoint == null)
		{
			int tWidth = g.getFontMetrics().stringWidth(label);
			int tHeight = g.getFontMetrics().getAscent();
			float XOffset = (float) (_IntersectRect.getX() + (_IntersectRect.getWidth() - tWidth) / 2);
			float YOffset = (float) (_IntersectRect.getY() + tHeight);
			_LabelPoint = new Point2D.Float(XOffset, YOffset);
		}
		// Draw the Hex Number
		g.setColor(Color.BLACK);
		g.drawString(label, _LabelPoint.x + 1, _LabelPoint.y + 1);
		g.setColor(_TextColour);
		g.drawString(label, _LabelPoint.x, _LabelPoint.y);

		g.setFont(currentFont);
	}
	
	protected abstract void drawBorderDetail(Graphics2D g);

	protected void drawBorder(Graphics2D g)
	{
		Color currentColor = g.getColor();
		g.setColor(_HexBorderColour);
		drawBorderDetail(g);
		g.setColor(currentColor);
	}


	public synchronized void draw(Graphics2D g, ImageObserver obs)
	{
		// log.info("Drawing hex : " + _Coordinate.toString());
		drawImage(g, obs);
		drawFeatures(g, obs);
		drawBorder(g);
	}
	

}
