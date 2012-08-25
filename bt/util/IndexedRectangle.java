package bt.util;

import java.awt.geom.Rectangle2D;

public class IndexedRectangle 
{
	private int _Index;
	private Rectangle2D.Double _Rectangle;

	public IndexedRectangle()
	{
		
	}
	
	public IndexedRectangle(int index, Rectangle2D.Double rect)
	{
		_Index = index;
		_Rectangle = rect;
	}

	public int getIndex() 
	{
		return _Index;
	}

	public void setIndex(int index) 
	{
		this._Index = index;
	}

	public Rectangle2D.Double getRectangle() 
	{
		return _Rectangle;
	}

	public void setRectangle(Rectangle2D.Double rectangle) 
	{
		this._Rectangle = rectangle;
	}
}
