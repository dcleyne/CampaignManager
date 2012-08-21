package bt.common.elements;

public class LongRangeMissile extends Weapon
{
    private int _Size;
    
	public int getSize()
	{
		return _Size;
	}

    public LongRangeMissile(int size)
    {
        _Size = size;
    }
    
	@Override
	public String getWeaponType()
	{
		// TODO Auto-generated method stub
		return "LRM " + Integer.toString(_Size);
	}

}
