package bt.ui.sprites;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import bt.mapping.Coordinate;
import bt.mapping.HexGrid;
import bt.mapping.Hexagon;
import bt.ui.renderers.BoardRenderer;


public abstract class Counter extends Sprite
{
	private HexGrid _Grid = null;
	private Hexagon _Sector = null;
	private Coordinate _Location;
	private Color _FaceColor;
	private Rectangle _Bounds;
	private Rectangle _FaceBounds;
	
	public Counter(BoardRenderer parent, Coordinate location, HexGrid grid, Color faceColor)
	{
		super(parent);
		
		_Grid = grid;
		_Sector = _Grid.getHex(location);
		_Location = location;
		_FaceColor = faceColor;
		
		setBoundaries(_Sector.getBounds());
		
		setVisible(false);
		prepare();
	}

	public Coordinate getLocation()
	{
		return _Location;
	}
	
	public void setFaceColor(Color faceColor)
	{
		_FaceColor = faceColor;
		invalidate();
	}
	
	@Override
	protected Image drawContent(Graphics2D graph, Image tempImage, Rectangle bounds)
	{
		RoundRectangle2D shadow = new RoundRectangle2D.Float(2, 2, 61, 54, 5, 5);

		graph.setColor(Color.black);
        graph.fill(shadow);
        graph.draw(shadow);
		
		RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, 61, 54, 5, 5);
		
		fillBackground(graph, roundedRectangle);

		graph.setColor(Color.black);
        graph.draw(roundedRectangle);

		return tempImage;
	}
	
	public void fillBackground(Graphics2D graph, RoundRectangle2D faceRectangle)
	{
		graph.setColor(_FaceColor);
        graph.fill(faceRectangle);		
	}
	
	public void drawCenteredText(Graphics2D graph, Rectangle bounds, Font font, Color textColor, String text)
	{
		Font currentFont = graph.getFont();
		Color currentColor = graph.getColor();
		
		graph.setFont(font);
		graph.setColor(textColor);
		
        FontMetrics fm = graph.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(text, graph);
        int x = bounds.x + (bounds.width - (int) r.getWidth()) / 2;
        int y = bounds.y + (bounds.height - (int) r.getHeight()) / 2 + fm.getAscent();
        graph.drawString(text, x, y);
        
        graph.setColor(currentColor);
        graph.setFont(currentFont);
	}

	public void setHexLocation(Coordinate location)
	{
		repaint();
		_Location = location;
		_Sector = _Grid.getHex(location);
		
		setBoundaries(_Sector.getBounds());
		
		repaint();
	}
	
	private void setBoundaries(Rectangle bounds)
	{
		_Bounds = new Rectangle(bounds.x + 4, bounds.y + 14, bounds.width - 9, bounds.height - 27);
		_FaceBounds = new Rectangle(bounds.x + 6, bounds.y + 16, bounds.width - 12, bounds.height - 30);		
	}

	@Override
	public Rectangle getBounds()
	{
		return _Bounds;
	}

	public Rectangle getFaceBounds()
	{
		return _FaceBounds;
	}
}
