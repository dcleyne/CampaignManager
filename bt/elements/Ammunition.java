package bt.elements;



/**
 * <p>Title: Miradan Phedd</p>
 *
 * <p>Description: Library for all Miradan related work</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Miradan Phedd</p>
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
public class Ammunition extends Item
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


	public String getType()
    {
        return "Ammunition";
    }

    @Override
    public String toString()
    {
    	return "@ " + _WeaponType + " (" + _Shots + ")";
    }

    public double getCost()
    {
    	//TODO fix this!
    	return 0;
    }
}
