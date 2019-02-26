package bt.elements;

public class ParticleProjectionCannon extends Weapon
{

	@Override
	public String getWeaponType()
	{
		return "PPC";
	}

	public double getCost()
	{
		return 200000;
	}
	
	public int getBV()
	{
		return 176;
	}
	
	public int getHeat()
	{
		return 10;
	}

	public int getMaxRange()
	{
		return 18;
	}

	public int getMaxDamagePoints()
	{
		return 10;
	}
}
