package bt.elements;

public class MachineGun extends Weapon
{

	@Override
	public String getWeaponType()
	{
		return "Machine Gun";
	}

	public double getCost()
	{
		return 5000;
	}
}
