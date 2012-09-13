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
    
    public int getBV()
    {
    	if (_WeaponType.equalsIgnoreCase("Autocannon 2"))
    	{
    		return (int)(5 * _Weight);
    	}
    	if (_WeaponType.equalsIgnoreCase("Autocannon 5"))
    	{
    		return (int)(9 * _Weight);
    	}
    	if (_WeaponType.equalsIgnoreCase("Autocannon 10"))
    	{
    		return (int)(15 * _Weight);
    	}
    	if (_WeaponType.equalsIgnoreCase("Autocannon 20"))
    	{
    		return (int)(20 * _Weight);
    	}
    	if (_WeaponType.equalsIgnoreCase("LRM 5"))
    	{
    		return (int)(6 * _Weight);
    	}
    	if (_WeaponType.equalsIgnoreCase("LRM 10"))
    	{
    		return (int)(11 * _Weight);
    	}
    	if (_WeaponType.equalsIgnoreCase("LRM 15"))
    	{
    		return (int)(17 * _Weight);
    	}
    	if (_WeaponType.equalsIgnoreCase("LRM 20"))
    	{
    		return (int)(23 * _Weight);
    	}
    	if (_WeaponType.equalsIgnoreCase("SRM 2"))
    	{
    		return (int)(3 * _Weight);
    	}
    	if (_WeaponType.equalsIgnoreCase("SRM 4"))
    	{
    		return (int)(5 * _Weight);
    	}
    	if (_WeaponType.equalsIgnoreCase("SRM 6"))
    	{
    		return (int)(7 * _Weight);
    	}
    	if (_WeaponType.equalsIgnoreCase("Machine Gun"))
    	{
    		return (int)(1 * _Weight);
    	}

    	return 0;
    }
}
