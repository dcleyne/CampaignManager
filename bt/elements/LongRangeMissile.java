package bt.elements;

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

	public double getCost()
	{
		switch (_Size)
		{
		case 5:
			return 30000;
		case 10:
			return 100000;
		case 15:
			return 175000;
		case 20:
			return 250000;
		}
		
		return Double.MAX_VALUE;
	}
	
	public int getBV()
	{
		switch (_Size)
		{
		case 5:
			return 45;
		case 10:
			return 90;
		case 15:
			return 136;
		case 20:
			return 181;
		}
		
		return Integer.MAX_VALUE;	
	}
	
	public int getHeat()
	{
		switch (_Size)
		{
		case 5:
			return 2;
		case 10:
			return 4;
		case 15:
			return 5;
		case 20:
			return 6;
		}
		
		return Integer.MAX_VALUE;
	}

	public int getMaxRange()
	{
		return 21;
	}

	public int getMaxDamagePoints()
	{
		return _Size;
	}
}
