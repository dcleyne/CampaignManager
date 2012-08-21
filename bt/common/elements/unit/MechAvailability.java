package bt.common.elements.unit;

import bt.common.elements.BattleValue;

public class MechAvailability implements BattleValue
{
    private String _Name;
    private String _Variant;
    private int _Weight;
    private int _BV;
    private int _Availability;
	public String getName()
	{
		return _Name;
	}
	public void setName(String name)
	{
		_Name = name;
	}
	public String getVariant()
	{
		return _Variant;
	}
	public void setVariant(String variant)
	{
		_Variant = variant;
	}
	public int getWeight()
	{
		return _Weight;
	}
	public void setWeight(int weight)
	{
		_Weight = weight;
	}
	public int getBV()
	{
		return _BV;
	}
	public void setBV(int bV)
	{
		_BV = bV;
	}
	public int getAvailability()
	{
		return _Availability;
	}
	public void setAvailability(int availability)
	{
		_Availability = availability;
	}
    
}
