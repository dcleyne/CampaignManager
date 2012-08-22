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

}
