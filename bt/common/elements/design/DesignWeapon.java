package bt.common.elements.design;

public class DesignWeapon extends DesignItem
{
    private String _WeaponType;
    
	public String getWeaponType()
	{
		return _WeaponType;
	}

	public void setWeaponType(String weaponType)
	{
		_WeaponType = weaponType;
	}

	@Override
	public String getType()
	{
		return "Weapon";
	}

}
