package bt.common.display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.AttributedString;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bt.common.elements.mapping.MapSet;
import bt.common.util.Hexagon;

public class MapLayoutRenderer
{
	private static Log log = LogFactory.getLog(MapLayoutRenderer.class);
	private static MapLayoutRenderer theInstance = new MapLayoutRenderer();

	private final int _MapDesiredWidth = 600;
	private final Hexagon _Hex = new Hexagon(0, 0, (int) (2 * (_MapDesiredWidth / 16) / (3 * Hexagon.Tan30)), false);
	private final int _MapWidth = (int) _Hex.GetXIncrement() * 16;
	private final int _MapHeight = _Hex.GetHeight() * 17;
	private final Font _SmallFont = new Font("SansSerif",Font.BOLD,24);
	private final Font _LargeFont = new Font("SansSerif",Font.BOLD,72);

	public static MapLayoutRenderer getInstance()
	{
		return theInstance;
	}

	public BufferedImage renderMapLayout(MapSet mapSet)
	{
		log.debug("Rendering MapSet " + mapSet.getName());

		int imageWidth = mapSet.getColumnCount() * _MapWidth;
		int imageHeight = mapSet.getRowCount() * _MapHeight;
		BufferedImage image = new BufferedImage(imageWidth + 1, imageHeight + 2, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setBackground(Color.white);
		g.setColor(Color.black);
		g.clearRect(0, 0, imageWidth, imageHeight);
		for (int row = 0; row < mapSet.getRowCount(); row++)
		{
			for (int col = 0; col < mapSet.getColumnCount(); col++)
			{
				MapSet.MapCell cell = mapSet.getCell(row + 1, col + 1);
				if (cell == null)
					continue;

				renderMapCell(g, row, col, cell);
			}
		}
		return image;
	}

	private void renderMapCell(Graphics2D g, int row, int col, MapSet.MapCell cell)
	{
		int x = col * _MapWidth;
		int y = row * _MapHeight;

		g.drawRect(x, y, _MapWidth, _MapHeight);
		renderHexGrid(g, x, y);
		
	    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    
		int paraWidth = (_MapWidth / 10) * 9;

		float height = measureParagraphHeight(g, cell.getMapName(), paraWidth,_LargeFont);
		int yOff = (int) (_MapHeight - height) / 2;
		int xOff = (int) (_MapWidth / 10);
		drawParagraph(g, x + xOff, y + yOff, cell.getMapName(), paraWidth,_LargeFont);

	
		String mapNumber = "Map " + cell.getMapNumber();
		height = measureParagraphHeight(g, mapNumber, paraWidth,_SmallFont);
		
		yOff = (int)height;
		drawParagraph(g, x + xOff, y + yOff, mapNumber, paraWidth,_SmallFont);
	
	}

	private float measureParagraphHeight(Graphics2D g, String paragraph, float width, Font font)
	{
		FontRenderContext frc = g.getFontRenderContext();
		AttributedString as = new AttributedString(paragraph);
		as.addAttribute(TextAttribute.FONT, font);
		LineBreakMeasurer linebreaker = new LineBreakMeasurer(as.getIterator(), frc);

		float y = 0.0f;
		while (linebreaker.getPosition() < paragraph.length())
		{
			TextLayout tl = linebreaker.nextLayout(width);

			y += tl.getAscent();
			y += tl.getDescent() + tl.getLeading();
		}
		return y;
	}

	private void drawParagraph(Graphics2D g, float x, float y, String paragraph, float width, Font font)
	{
		FontRenderContext frc = g.getFontRenderContext();
		AttributedString as = new AttributedString(paragraph);
		as.addAttribute(TextAttribute.FONT, font);
		LineBreakMeasurer linebreaker = new LineBreakMeasurer(as.getIterator(), frc);
		while (linebreaker.getPosition() < paragraph.length())
		{
			TextLayout tl = linebreaker.nextLayout(width);

			Rectangle2D bounds = tl.getBounds();
			float xOff = (float)((width - bounds.getWidth()) / 2f);
			y += tl.getAscent();
			tl.draw(g, x + xOff, y);
			y += tl.getDescent() + tl.getLeading();
		}
	}

	private void renderHexGrid(Graphics2D g, int xCoord, int yCoord)
	{
		int xOffset = xCoord - (_Hex.GetWidth() / 2);
		int yOffset = yCoord - (_Hex.GetHeight() / 2);

		int y = yOffset;
		for (int row = 0; row < 18; row++)
		{
			int x = xOffset;
			for (int col = 0; col < 17; col++)
			{
				int yOff = 0;
				if ((col % 2) == 0)
					yOff += _Hex.GetYIncrement();

				Hexagon drawHex = new Hexagon(x, y + yOff, _Hex.GetHeight(), false);
				drawHex.draw(g);
				x += _Hex.GetXIncrement();
			}
			y += _Hex.GetHeight();
		}
	}

}
