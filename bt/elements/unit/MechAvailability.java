package bt.elements.unit;

public class MechAvailability
{
    private String _Name;
    private String _Variant;
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
	public int getAvailability()
	{
		return _Availability;
	}
	public void setAvailability(int availability)
	{
		_Availability = availability;
	}
    
    public String getVariantName()
    {
        return _Variant + " " + _Name; 
    }	

}
