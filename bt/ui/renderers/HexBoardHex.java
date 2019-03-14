package bt.ui.renderers;

import java.awt.Graphics2D;

import bt.mapping.Coordinate;
import bt.mapping.Hexagon;
import bt.mapping.MapHex;

public class HexBoardHex extends BoardHex
{
	protected Coordinate _BoardCoordinate;
	protected Hexagon _Hexagon = null;

	public HexBoardHex(HexBoardRenderer renderer, MapHex theMapHex, int boardX, int boardY)
	{
		super(renderer, theMapHex);
		_BoardCoordinate = new Coordinate(boardX, boardY);
		_Hexagon = renderer.getHex(boardX, boardY);
	}
	
	protected String getLabel()
	{
		return _BoardCoordinate.getBoardNum();
	}
	
	protected double getTopLeftX()
	{
		return _Hexagon.getBounds2D().getX();
	}
	
	protected double getTopLeftY()
	{
		return _Hexagon.getBounds2D().getY();
	}

	protected double getWidth()
	{
		return _Hexagon.getBounds2D().getWidth();
	}
	
	protected double getHeight()
	{
		return _Hexagon.getBounds2D().getHeight();
	}

	public Hexagon getHexagon()
	{
		return _Hexagon;
	}
	
	protected void drawBorderDetail(Graphics2D g)
	{
		g.drawPolygon(_Hexagon);
	}

	public int hashCode()
	{
		return _BoardCoordinate.toString().hashCode();
	}

	public boolean equals(Object o)
	{
		if (o instanceof HexBoardHex)
		{
			HexBoardHex bh = (HexBoardHex)o;
			return bh._BoardCoordinate.equals(_BoardCoordinate);
		}
		if (o instanceof Coordinate)
		{
			return _BoardCoordinate.equals((Coordinate)o);
		}
		return false;
	}
}
