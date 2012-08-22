package bt.elements.design;

public class BasicDesignItem extends DesignItem
{
    private String _Type;
    

    public BasicDesignItem(String type)
    {
        _Type = type;
    }
    
	@Override
	public String getType()
	{
		return _Type;
	}

}
