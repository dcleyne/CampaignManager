package bt.ui.renderers;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JPanel;

public abstract class BoardRendererView extends JPanel
{
	private static final long serialVersionUID = 1L;

	private BoardRenderer _BoardRenderer = null;

	public synchronized void setBoardRenderer(BoardRenderer theBoard)
	{
		_BoardRenderer = theBoard;
	}
	
	public BoardRenderer getBoardRenderer()
	{
		return _BoardRenderer;
	}
	
	public void setCursorLocation(Point p)
	{
		if (_BoardRenderer != null)
			_BoardRenderer.setCursorLocation(p);
	}
	
	public Point translatePoint(Point p)
	{
		if (_BoardRenderer != null)
			return _BoardRenderer.translatePoint(p);
		return p;
	}
	
	@Override
	public void paint(Graphics g)
	{
		Shape curClip = g.getClip();
		Rectangle bounds = getVisibleRect();		
		g.setClip(bounds);
		
		g.setColor(getBackground());
		g.fillRect(0, 0, bounds.width, bounds.height);

		if (_BoardRenderer != null)
		{
			_BoardRenderer.drawBoard((Graphics2D)g, this);
		}
		g.setClip(curClip);
	}

	@Override 
	public Dimension getSize()
	{
		return getPreferredSize();
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		if (getBoardRenderer() != null)
		{
			return getBoardRenderer().getSize();
		} else
		{
			return new Dimension(500, 500);
		}
	}


}
