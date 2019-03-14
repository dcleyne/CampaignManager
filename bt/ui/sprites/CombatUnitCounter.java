package bt.ui.sprites;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import bt.elements.unit.Unit;
import bt.mapping.Coordinate;
import bt.mapping.HexGrid;
import bt.ui.renderers.BoardRenderer;
import bt.util.ImageUtil;

public class CombatUnitCounter extends ImageCounter
{
	private static final Rectangle ParentUnitTextArea = new Rectangle(2, 2, 57, 10);

	private static final Font IdentifierFont = new Font("SansSerif", Font.BOLD, 8);

	private static final String[] UnitTypeImages = {
		"battlemech.png",
		"aerospace.png",
		"armour.png",
		"artillery.png",
		"infantry.png",
		"engineers.png",
		"headquarters.png",
	};

	private static final String[] UnitSizeImages = {
		"regiment.png",
		"battalion.png",
		"company.png"
	};
	
	private Unit _Unit;
	private BufferedImage _UnitSizeImage;
	private Color _IdentifierColor = Color.white;

	public Unit getUnit()
	{
		return _Unit;
	}
	
	public CombatUnitCounter(BoardRenderer renderer, Coordinate location, HexGrid grid, Color faceColor, Unit combatUnit)
	{
		super(renderer, location, grid, faceColor);
		
		_Unit = combatUnit;
		
		setImage(ImageUtil.loadImage("data/images/unit", UnitTypeImages[0]));
		_UnitSizeImage = ImageUtil.loadImage("data/images/unit", UnitSizeImages[2]);
		
		invalidate();
	}

	@Override
	protected Image drawContent(Graphics2D graph, Image tempImage, Rectangle bounds)
	{
		Image image = super.drawContent(graph, tempImage, bounds);
		
		if (_UnitSizeImage != null)
		{
			int xOffset = (bounds.width - _UnitSizeImage.getWidth()) / 2;
			int yOffset = (bounds.height - _UnitSizeImage.getHeight()) / 2;

			graph.drawImage(_UnitSizeImage, xOffset, yOffset, getComponent());
		}
		
		drawCenteredText(graph, ParentUnitTextArea, IdentifierFont, _IdentifierColor, "");
		
//		drawCenteredText(graph, IdentifierTextArea, IdentifierFont, _IdentifierColor, getIdentifierText());
//		drawCenteredText(graph, TypeTextArea, TypeFont, _IdentifierColor, getTypeText());
//		drawCenteredText(graph, FighterTextArea, FighterFont, _TextColor, getFighterText());
		
		return image;
	}
	
}
