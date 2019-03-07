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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import bt.ui.sprites.Sprite;

public class SpriteManager
{

	private ArrayList<Sprite> _Highlights = new ArrayList<Sprite>();
	private ArrayList<Sprite> _Elements = new ArrayList<Sprite>();
	private ArrayList<Sprite> _Widgets = new ArrayList<Sprite>();
	private ArrayList<Sprite> _Cursors = new ArrayList<Sprite>();

	public SpriteManager()
	{
	}

	public void registerHighlight(Sprite s)
	{
		if (!_Highlights.contains(s))
		{
			_Highlights.add(s);
		}
	}

	public void registerElement(Sprite s)
	{
		if (!_Elements.contains(s))
		{
			_Elements.add(s);
		}
	}

	public void registerWidget(Sprite s)
	{
		if (!_Widgets.contains(s))
		{
			_Widgets.add(s);
		}
	}

	public void registerCursor(Sprite s)
	{
		if (!_Cursors.contains(s))
		{
			_Cursors.add(s);
		}
	}

	public void deregisterSprite(Sprite s)
	{
		if (_Highlights.contains(s))
		{
			_Highlights.remove(s);
		}

		if (_Elements.contains(s))
		{
			_Elements.remove(s);
		}

		if (_Widgets.contains(s))
		{
			_Widgets.remove(s);
		}

		if (_Cursors.contains(s))
		{
			_Cursors.remove(s);
		}

		s.setVisible(false);
	}

	public void bringSpriteToFront(Sprite s)
	{
		if (_Highlights.contains(s))
		{
			_Highlights.remove(s);
			_Highlights.add(s);
		}

		if (_Elements.contains(s))
		{
			_Elements.remove(s);
			_Elements.add(s);
		}

		if (_Widgets.contains(s))
		{
			_Widgets.remove(s);
			_Widgets.add(s);
		}

		if (_Cursors.contains(s))
		{
			_Cursors.remove(s);
			_Cursors.add(s);
		}

		s.setVisible(true);
	}

	public void clearAllSprites()
	{
		for (Sprite s : _Highlights)
			s.setVisible(false);
			
		for (Sprite s : _Elements)
			s.setVisible(false);

		for (Sprite s : _Widgets)
			s.setVisible(false);
		
		for (Sprite s : _Cursors)
			s.setVisible(false);
		
		_Highlights.clear();
		_Elements.clear();
		_Widgets.clear();
		_Cursors.clear();
	}


	public void drawSprites(Graphics2D g, ImageObserver obs, Rectangle updateRect)
	{
		drawSpriteArrayList(g, obs, updateRect, _Highlights);
		drawSpriteArrayList(g, obs, updateRect, _Elements);
		drawSpriteArrayList(g, obs, updateRect, _Widgets);
		drawSpriteArrayList(g, obs, updateRect, _Cursors);
	}

	private void drawSpriteArrayList(Graphics2D g, ImageObserver obs, Rectangle updateRect, ArrayList<Sprite> Sprites)
	{
		for (int i = 0; i < Sprites.size(); i++)
		{
			Sprite s = Sprites.get(i);
			if (s.isVisible())
			{
				if (s.intersects(updateRect))
				{
					s.draw(g, obs);
				}
			}
		}
	}

}
