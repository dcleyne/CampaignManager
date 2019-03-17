package bt.elements.campaign;

import java.util.ArrayList;

public class Force
{
	private String _Name;
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
	
	public ArrayList<CampaignUnit> getUnits()
	{
		return new ArrayList<CampaignUnit>(_Units);
	}
	
}
