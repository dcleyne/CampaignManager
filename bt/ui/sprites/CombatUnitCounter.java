package bt.ui.sprites;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import bt.mapping.Coordinate;
import bt.ui.renderers.HexBoardRenderer;
import bt.util.ImageUtil;

public class CombatUnitCounter extends ImageCounter
{
	private static final Font IdentifierFont = new Font("SansSerif", Font.BOLD, 8);
	private static final Font ParentFont = new Font("SansSerif", Font.BOLD, 9);

	public enum Type
	{
		BATTLEMECH,
		AEROSPACE,
		ARMOUR,
		ARTILLERY,
		INFANTRY,
		ENGINEERS,
		HEADQUARTERS;

		private static final String[] UnitTypeImages = {
				"battlemech.png",
				"aerospace.png",
				"armour.png",
				"artillery.png",
				"infantry.png",
				"engineers.png",
				"headquarters.png",
			};

		public String getImageName()
		{
			return UnitTypeImages[ordinal()];
		}
	}
	
	public enum Size
	{
		REGIMENT,
		BATTALION,
		COMPANY;
		
		private static final String[] UnitSizeImages = {
				"regiment.png",
				"battalion.png",
				"company.png"
			};
		
		public String getImageName()
		{
			return UnitSizeImages[ordinal()];
		}
	}

	
	private String _UnitName = "";
	private String _UnitAbbreviation = "";
	private Type _Type;
	private Size _Size;
	private BufferedImage _UnitSizeImage;
	private Color _IdentifierColor = Color.white;

	public String getUnitName()
	{
		return _UnitName;
	}
	
	public CombatUnitCounter(HexBoardRenderer renderer, Coordinate location, Color faceColor, String unitName, String unitAbbreviation)
	{
		this(renderer, location, faceColor, unitName, unitAbbreviation, Type.BATTLEMECH, Size.COMPANY);
	}

	public CombatUnitCounter(HexBoardRenderer renderer, Coordinate location, Color faceColor, String unitName, String unitAbbreviation, Type type, Size size)
	{
		super(renderer, location, renderer.getHexGrid(), faceColor);
		_UnitName = unitName;
		_UnitAbbreviation = unitAbbreviation;
		setType(type);
		setSize(size);
		invalidate();
}

	public void setType(Type type)
	{
		_Type = type;
		setImage(ImageUtil.loadImage("data/images/unit", _Type.getImageName()));
	}

	public void setSize(Size size)
	{
		_Size = size;
		_UnitSizeImage = ImageUtil.loadImage("data/images/unit", _Size.getImageName());
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
		
		if (_UnitName != null && !_UnitName.isEmpty())
		{
			Rectangle unitTextArea = new Rectangle(2, 2, bounds.width - 4, 10);

			drawCenteredText(graph, unitTextArea, IdentifierFont, _IdentifierColor, _UnitName);
		}
		if (_UnitAbbreviation != null && !_UnitAbbreviation.isEmpty())
		{
			Rectangle parentUnitTextArea = new Rectangle(2, bounds.height - 14, bounds.width - 4, 10);

			drawCenteredText(graph, parentUnitTextArea, ParentFont, _IdentifierColor, _UnitAbbreviation);
		}
		
//		drawCenteredText(graph, IdentifierTextArea, IdentifierFont, _IdentifierColor, getIdentifierText());
//		drawCenteredText(graph, TypeTextArea, TypeFont, _IdentifierColor, getTypeText());
//		drawCenteredText(graph, FighterTextArea, FighterFont, _TextColor, getFighterText());
		
		return image;
	}
	
}
