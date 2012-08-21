package bt.common.elements.design;

public class DesignAmmunition extends DesignItem
{
    private String _WeaponType;
    private double _Weight = 1.0;
    private int _Shots;

    public String getWeaponType()
	{
		return _WeaponType;
	}

	public void setWeaponType(String weaponType)
	{
		_WeaponType = weaponType;
	}

	public double getWeight()
	{
		return _Weight;
	}

	public void setWeight(double weight)
	{
		_Weight = weight;
	}

	public int getShots()
	{
		return _Shots;
	}

	public void setShots(int shots)
	{
		_Shots = shots;
	}

	@Override
	public String getType()
	{
		return "Ammunition";
	}

}
