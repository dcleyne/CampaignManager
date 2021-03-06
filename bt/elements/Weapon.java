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
public abstract class Weapon extends Item
{
	@Override
    public String getType()
    {
        return "Weapon";
    }

    public abstract String getWeaponType();
    public abstract int getBV();
    public abstract int getHeat();
    public abstract int getMaxRange();
    public abstract int getMaxDamagePoints();
    
    @Override
    public String toString()
    {
    	return getWeaponType();
    }
}
