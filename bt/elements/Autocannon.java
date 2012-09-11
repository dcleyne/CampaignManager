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
}
