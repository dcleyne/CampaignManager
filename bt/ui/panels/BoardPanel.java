package bt.ui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import bt.ui.panels.listeners.BoardPanelListener;
import bt.ui.renderers.BoardRenderer;
import bt.ui.renderers.BoardRendererView;

public class BoardPanel extends BoardRendererView implements MouseListener
{
	static final long serialVersionUID = 1;
	private BoardRenderer _BoardRenderer = null;
	private ArrayList<BoardPanelListener> _Listeners = new ArrayList<BoardPanelListener>();
	
	public BoardPanel()
	{
		this.setDoubleBuffered(true);
		this.setBackground(Color.WHITE);
		addMouseListener(this);
	}
	
	public void addListener(BoardPanelListener l)
	{
		if (!_Listeners.contains(l))
		{
			_Listeners.add(l);
		}
	}

	public void removeListener(BoardPanelListener l)
	{
		if (_Listeners.contains(l))
		{
			_Listeners.remove(l);
		}
	}
	
	public synchronized void setBoardRenderer(BoardRenderer theBoard)
	{
		_BoardRenderer = theBoard;
	}
	
	public BoardRenderer getBoardRenderer()
	{
		return _BoardRenderer;
	}
	
	@Override
	public void paint(Graphics g)
	{
		g.setColor(getBackground());
		g.fillRect(0, 0, getBounds().width, getBounds().height);

		Graphics2D g2 = (Graphics2D) g;
		if (_BoardRenderer != null)
		{
			_BoardRenderer.drawBoard(g2, this);
		}
	}

	@Override
	public Dimension getPreferredSize()
	{
		if (_BoardRenderer != null)
		{
			return _BoardRenderer.getSize();
		} else
		{
			return new Dimension(500, 500);
		}
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
		Point clickPoint = e.getPoint();

		int Button = e.getButton();
		switch (Button)
		{
		case MouseEvent.NOBUTTON:
			break;
		case MouseEvent.BUTTON1:
			_BoardRenderer.setCursorLocation(clickPoint);
			notifySelectedLocation(false);
			break;
		case MouseEvent.BUTTON2:
			notifyCenterMap(clickPoint);
			break;
		case MouseEvent.BUTTON3:
			_BoardRenderer.setCursorLocation(clickPoint);
			notifySelectedLocation(true);
			notifyMenuRequested(_BoardRenderer.translatePoint(clickPoint));
			notifySelectedLocation(true);
			break;
		}
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	private void notifyMenuRequested(Point p)
	{
		for (int i = 0; i < _Listeners.size(); i++)
		{
			_Listeners.get(i).contextMenuRequested(p);
		}
	}

	private void notifyCenterMap(Point p)
	{
		for (int i = 0; i < _Listeners.size(); i++)
		{
			_Listeners.get(i).centerMapOnPoint(p);
		}
	}

	private void notifySelectedLocation(boolean mergeSelection)
	{
		for (int i = 0; i < _Listeners.size(); i++)
		{
			_Listeners.get(i).selectedLocation(mergeSelection);
		}
	}

}
