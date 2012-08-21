package bt.common.elements;

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

}
