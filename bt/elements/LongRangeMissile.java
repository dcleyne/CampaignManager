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
}
