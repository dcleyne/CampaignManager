package bt.elements.campaign;

import java.util.ArrayList;

public class CampaignUnit
{
	private String _Name;
	private String _Size;
	private String _Type;
	private int _SubUnitSize;
	private String _ParentUnitName;
	private ArrayList<CampaignUnitElement> _Elements = new ArrayList<CampaignUnitElement>();
	
	public String getName()
	{
		return _Name;
	}
	public void setName(String name)
	{
		_Name = name;
	}
	public String getSize()
	{
		return _Size;
	}
	public void setSize(String size)
	{
		_Size = size;
	}
	public String getType()
	{
		return _Type;
	}
	public void setType(String type)
	{
		_Type = type;
	}
	public int getSubUnitSize()
	{
		return _SubUnitSize;
	}
	public void setSubUnitSize(int subUnitSize)
	{
		_SubUnitSize = subUnitSize;
	}
	public String getParentUnitName()
	{
		return _ParentUnitName;
	}
	public void setParentUnitName(String parentUnitName)
	{
		_ParentUnitName = parentUnitName;
	}
	
	public void addElement(CampaignUnitElement e)
	{
		_Elements.add(e);
	}
	
	public ArrayList<CampaignUnitElement> getElements()
	{
		return new ArrayList<CampaignUnitElement>(_Elements);
	}
	
}
