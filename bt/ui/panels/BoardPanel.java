package bt.ui.panels;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import bt.ui.panels.listeners.BoardPanelListener;
import bt.ui.renderers.BoardRendererView;

public class BoardPanel extends BoardRendererView implements MouseListener
{
	static final long serialVersionUID = 1;
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
			setCursorLocation(clickPoint);
			notifySelectedLocation(false);
			break;
		case MouseEvent.BUTTON2:
			notifyCenterMap(clickPoint);
			break;
		case MouseEvent.BUTTON3:
			setCursorLocation(clickPoint);
			notifySelectedLocation(true);
			notifyMenuRequested(translatePoint(clickPoint));
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
