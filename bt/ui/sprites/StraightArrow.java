/*******************************************************************************
 * MegaMek - Copyright (C) 2000-2002 Ben Mazur (bmazur@sev.org)
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
 * This class calculates and stores points of polygon shaped as straight arrow.
 * Minimum required arguments are two Point elements - start and end of arrow.
 * 
 * Special feature of this class is last boolean argument. It defines if it will
 * be full shaped arrow or left half only. Private Polygon hotArea contains same
 * points as an arrow itself except when arrow is changed to halved hotArea
 * stays if full arrow shape. It was done in order to get only one tooltip for
 * two arrows in case of mutual attack.
 * 
 * @author Slava Zipunov (zipp32). Code style changed by Daniel Cleyne
 ******************************************************************************/

package bt.ui.sprites;

import java.awt.Point;
import java.awt.Polygon;

public class StraightArrow extends Polygon
{
	static final long serialVersionUID = 1;

	private Polygon _HotArea = new Polygon();
	private Point _StartPoint;
	private Point _EndPoint;
	private int _HeadLength = 30;
	private int _HeadWidth = 5;
	private int _ArrowWidthAtHead = 3;
	private int _TailWidth = 3;
	private int _TailLength = 0;
	private boolean _Halved = false;

	/* Most extensive constructor with all paremeters given */
	public StraightArrow(Point startPoint, Point endPoint, int headLength, int headWidth, int arrowWidthAtHead, int tailWidth, int tailLength, boolean halved)
	{
		super();
		_StartPoint = startPoint;
		_EndPoint = endPoint;
		_HeadLength = headLength;
		_HeadWidth = headWidth;
		_ArrowWidthAtHead = arrowWidthAtHead;
		_TailWidth = tailWidth;
		_Halved = halved;
		buildPointsArrays();
	}

	/* Short constructor. Two points and boolean value. */
	public StraightArrow(Point startPoint, Point endPoint, boolean halved)
	{
		super();
		this._StartPoint = startPoint;
		this._EndPoint = endPoint;
		this._Halved = halved;
		buildPointsArrays();
	}

	/* One more constructor */
	public StraightArrow(Point startPoint, Point endPoint, int width, boolean halved)
	{
		super();
		this._StartPoint = startPoint;
		this._EndPoint = endPoint;
		this._HeadWidth = width + 2;
		this._ArrowWidthAtHead = width;
		this._TailWidth = width;
		this._Halved = halved;
		buildPointsArrays();
	}

	/* I know, it is annoying, but another constructor */
	public StraightArrow(Point startPoint, Point endPoint, int width, int headWidth, boolean halved)
	{
		super();
		this._StartPoint = startPoint;
		this._EndPoint = endPoint;
		this._HeadWidth = headWidth;
		this._ArrowWidthAtHead = width;
		this._TailWidth = width;
		this._Halved = halved;
		buildPointsArrays();
	}

	/* Calculating and adding points to Polygon class. Some trigonometry. */
	private void buildPointsArrays()
	{
		int dX = _EndPoint.x - _StartPoint.x;
		int dY = _EndPoint.y - _StartPoint.y;
		double arrowLength = Math.sqrt(dX * dX + dY * dY);
		double sin = dY / arrowLength;
		double cos = dX / arrowLength;
		this.addPoint(_StartPoint.x, _StartPoint.y);
		this.addPoint((int) Math.round(_StartPoint.x + _TailWidth * sin - _TailLength * cos), (int) Math.round(_StartPoint.y - _TailWidth * cos - _TailLength * sin));
		this.addPoint((int) Math.round(_EndPoint.x - _HeadLength * cos + _ArrowWidthAtHead * sin),
				(int) Math.round(_EndPoint.y - _HeadLength * sin - _ArrowWidthAtHead * cos));
		this.addPoint((int) Math.round(_EndPoint.x - _HeadLength * cos + _HeadWidth * sin), (int) Math.round(_EndPoint.y - _HeadLength * sin - _HeadWidth * cos));
		this.addPoint(_EndPoint.x, _EndPoint.y);
		if (!_Halved)
		{
			this.addPoint((int) Math.round(_EndPoint.x - _HeadLength * cos - _HeadWidth * sin), (int) Math.round(_EndPoint.y - _HeadLength * sin + _HeadWidth * cos));
			this.addPoint((int) Math.round(_EndPoint.x - _HeadLength * cos - _ArrowWidthAtHead * sin),
					(int) Math.round(_EndPoint.y - _HeadLength * sin + _ArrowWidthAtHead * cos));
			this.addPoint((int) Math.round(_StartPoint.x - _TailWidth * sin - _TailLength * cos), (int) Math.round(_StartPoint.y + _TailWidth * cos - _TailLength * sin));
		}
		_HotArea.addPoint(_StartPoint.x, _StartPoint.y);
		_HotArea.addPoint((int) Math.round(_StartPoint.x + _TailWidth * sin - _TailLength * cos), (int) Math.round(_StartPoint.y - _TailWidth * cos - _TailLength * sin));
		_HotArea.addPoint((int) Math.round(_EndPoint.x - _HeadLength * cos + _ArrowWidthAtHead * sin),
				(int) Math.round(_EndPoint.y - _HeadLength * sin - _ArrowWidthAtHead * cos));
		_HotArea.addPoint((int) Math.round(_EndPoint.x - _HeadLength * cos + _HeadWidth * sin), (int) Math.round(_EndPoint.y - _HeadLength * sin - _HeadWidth * cos));
		_HotArea.addPoint(_EndPoint.x, _EndPoint.y);
		_HotArea.addPoint((int) Math.round(_EndPoint.x - _HeadLength * cos - _HeadWidth * sin), (int) Math.round(_EndPoint.y - _HeadLength * sin + _HeadWidth * cos));
		_HotArea.addPoint((int) Math.round(_EndPoint.x - _HeadLength * cos - _ArrowWidthAtHead * sin),
				(int) Math.round(_EndPoint.y - _HeadLength * sin + _ArrowWidthAtHead * cos));
		_HotArea.addPoint((int) Math.round(_StartPoint.x - _TailWidth * sin - _TailLength * cos), (int) Math.round(_StartPoint.y + _TailWidth * cos - _TailLength * sin));
	}

	@Override
	public boolean contains(int x, int y)
	{
		return _HotArea.contains(x, y);
	}

	@Override
	public boolean contains(Point p)
	{
		return _HotArea.contains(p);
	}

	@Override
	public boolean contains(double x, double y)
	{
		return _HotArea.contains((int) Math.round(x), (int) Math.round(y));
	}

	@Override
	public void translate(int deltaX, int deltaY)
	{
		super.translate(deltaX, deltaY);
		_HotArea.translate(deltaX, deltaY);
	}

}
