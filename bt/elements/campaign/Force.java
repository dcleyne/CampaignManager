package bt.elements.campaign;

import java.awt.Color;
import java.util.ArrayList;

public class Force
{
	private String _Name;
	private String _Abbreviation;
	private Color _Color;
	private ArrayList<CampaignUnit> _Units = new ArrayList<CampaignUnit>();

	public String getName()
	{
		return _Name;
	}

	public void setName(String name)
	{
		_Name = name;
	}
	
	public void addUnit(CampaignUnit u)
	{
		_Units.add(u);
	}
	
	public String getAbbreviation()
	{
		return _Abbreviation;
	}

	public void setAbbreviation(String abbreviation)
	{
		_Abbreviation = abbreviation;
	}

	public Color getColor()
	{
		return _Color;
	}

	public void setColor(Color color)
	{
		_Color = color;
	}

	public ArrayList<CampaignUnit> getUnits()
	{
		return new ArrayList<CampaignUnit>(_Units);
	}


}
