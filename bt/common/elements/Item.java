package bt.common.elements;

public abstract class Item
{
    private String _Name = "";
    private String _Manufacturer = "";
    private String _Model = "";
    
    public String getName()
	{
		return _Name;
	}

	public void setName(String name)
	{
		_Name = name;
	}

	public String getManufacturer()
	{
		return _Manufacturer;
	}

	public void setManufacturer(String manufacturer)
	{
		_Manufacturer = manufacturer;
	}

	public String getModel()
	{
		return _Model;
	}

	public void setModel(String model)
	{
		_Model = model;
	}

	public Item()
    {
    }

    public Item(String Manufacturer, String Model)
    {
        _Manufacturer = Manufacturer;
        _Model = Model;
    }


    public abstract String getType();
    
    @Override
    public String toString()
    {
    	return getType();
    }
}
