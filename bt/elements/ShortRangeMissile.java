package bt.elements;

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

	public double getCost()
	{
		switch (_Size)
		{
		case 2:
			return 10000;
		case 4:
			return 60000;
		case 6:
			return 80000;
		}
		
		return Double.MAX_VALUE;
	}
}
