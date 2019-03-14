package bt.ui.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import bt.mapping.Coordinate;
import bt.mapping.HexGrid;
import bt.ui.renderers.BoardRenderer;

public abstract class ImageCounter extends Counter
{
	private BufferedImage _Image = null;

	public ImageCounter(BoardRenderer renderer, Coordinate location, HexGrid grid, Color faceColor)
	{
		super(renderer, location, grid, faceColor);
	}
	
	public void setImage(BufferedImage image)
	{
		_Image = image;
		invalidate();
	}
	
	@Override
	protected Image drawContent(Graphics2D graph, Image tempImage, Rectangle bounds)
	{
		Image image = super.drawContent(graph, tempImage, bounds);
		
		if (_Image != null)
		{
			int xOffset = (bounds.width - _Image.getWidth()) / 2;
			int yOffset = (bounds.height - _Image.getHeight()) / 2;

			graph.drawImage(_Image, xOffset, yOffset, getComponent());
		}
		
		return image;
	}

}
