package bt.elements;

public class Flamer extends Weapon
{

	@Override
	public String getWeaponType()
	{
		return "Flamer";
	}

	public double getCost()
	{
		return 7500;
	}
	
	public int getBV()
	{
		return 6;
	}
	
	public int getHeat()
	{
		return 3;
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
