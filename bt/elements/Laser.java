package bt.elements;

public class Laser extends Weapon
{
    private String _Size;
    
	public String getSize()
	{
		return _Size;
	}

    public Laser(String size)
    {
        _Size = size;
    }
    
    @Override
	public String getWeaponType()
	{
		return _Size + " Laser";
	}

    public double getCost()
    {
    	if (_Size.equalsIgnoreCase("Small"))
    		return 11250;
    	if (_Size.equalsIgnoreCase("Medium"))
    		return 40000;
    	if (_Size.equalsIgnoreCase("Large"))
    		return 100000;
    	
    	return Double.MAX_VALUE;
    }
}
