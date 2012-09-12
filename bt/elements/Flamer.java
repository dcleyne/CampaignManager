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
		return 5;
	}
	
	public int getHeat()
	{
		return 3;
	}
}
