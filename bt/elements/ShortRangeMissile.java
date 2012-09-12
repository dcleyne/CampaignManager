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
	
	public int getBV()
	{
		switch (_Size)
		{
		case 2:
			return 21;
		case 4:
			return 39;
		case 6:
			return 59;
		}
		
		return Integer.MAX_VALUE;		
	}
	
	public int getHeat()
	{
		switch (_Size)
		{
		case 2:
			return 2;
		case 4:
			return 3;
		case 6:
			return 4;
		}
		
		return Integer.MAX_VALUE;
	}
}
