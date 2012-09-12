package bt.elements;

public class Autocannon extends Weapon
{
    private int _Size;

    public int getSize()
    { return _Size; }

    public Autocannon(int size)
    {
        _Size = size;
    }

	@Override
	public String getWeaponType()
	{
		return "Autocannon " + Integer.toString(_Size);
	}

	public double getCost()
	{
		switch (_Size)
		{
		case 2:
			return 75000;
		case 5:
			return 125000;
		case 10:
			return 200000;
		case 20:
			return 300000;
		}
		
		return Double.MAX_VALUE;
	}
	
	public int getBV()
	{
		switch (_Size)
		{
		case 2:
			return 37;
		case 5:
			return 70;
		case 10:
			return 123;
		case 20:
			return 178;
		}
		
		return Integer.MAX_VALUE;
	}
	
	public int getHeat()
	{
		switch (_Size)
		{
		case 2:
			return 1;
		case 5:
			return 1;
		case 10:
			return 3;
		case 20:
			return 7;
		}
		
		return Integer.MAX_VALUE;	
	}
}
