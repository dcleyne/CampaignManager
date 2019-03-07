package bt.mapping;

import java.awt.Color;
import org.jdom.Element;
import bt.util.PersistToXML;

public class Decoration implements PersistToXML
{
	public static final String DECORATION = "Decoration";
	private static final String TEXT = "Text";
	private static final String COLOUR = "Colour";
	private static final String LOCATION = "Location";

	static final long serialVersionUID = 1;

	private String _Text;
	private Color _Colour;
	private DecorationLocation _Location;

	public Decoration(String text, Color colour, DecorationLocation location)
	{
		_Text = text;
		_Colour = colour;
		_Location = location;
	}
	
	private Decoration()
	{
	}

	public String getText()
	{
		return _Text;
	}

	public Color getColour()
	{
		return _Colour;
	}

	public DecorationLocation getLocation()
	{
		return _Location;
	}

	public Element saveToElement()
	{
		Element e = new Element(DECORATION);
		e.setAttribute(TEXT, _Text);
		if (_Colour != null)
			e.setAttribute(COLOUR, _Colour.toString());
		if (_Location != null)
			e.setAttribute(LOCATION, _Location.toString());
		return e;
	}

	public void loadFromElement(Element e) throws Exception
	{
		
		_Text = e.getAttributeValue(TEXT);
		if (e.getAttribute(COLOUR) != null)
			_Colour = Color.decode(e.getAttributeValue(COLOUR));
		if (e.getAttribute(LOCATION) != null)
			_Location = DecorationLocation.fromString(e.getAttributeValue(LOCATION));
	}
	
	public static Decoration loadDecorationFromElement(Element e) throws Exception
	{
		Decoration d = new Decoration();
		d.loadFromElement(e);
		return d;
	}
}
