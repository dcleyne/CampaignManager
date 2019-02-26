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
	
	public int getBV()
	{
		return 5;
	}
	
	public int getHeat()
	{
		return 0;
	}

	public int getMaxRange()
	{
		return 3;
	}

	public int getMaxDamagePoints()
	{
		return 2;
	}
}
