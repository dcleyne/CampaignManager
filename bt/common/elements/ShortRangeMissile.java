package bt.common.elements;

public class ShortRangeMissile extends Weapon
{
	private int _Size;
	
	public int getSize()
	{
		return _Size;
	}

    public ShortRangeMissile(int size)
    {
        _Size = size;
    }
    
	@Override
	public String getWeaponType()
	{
		return "SRM " + Integer.toString(_Size);
	}

}
