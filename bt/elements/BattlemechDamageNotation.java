package bt.elements;

import java.util.Vector;

public class BattlemechDamageNotation
{
	private String _Area;
	private String _Location;
	private int _Index;
	private ItemStatus _Status;
	
	public String getArea() 
	{
		return _Area;
	}
	
	public void setArea(String area) 
	{
		_Area = area;
	}
	
	public String getLocation() 
	{
		return _Location;
	}
	
	public void setLocation(String location) 
	{
		_Location = location;
	}
	
	public int getIndex() 
	{
		return _Index;
	}
	
	public void setIndex(int index) 
	{
		_Index = index;
	}
	
	public ItemStatus getStatus() 
	{
		return _Status;
	}
	
	public void setStatus(ItemStatus status) 
	{	
		_Status = status;
	}
	
	public BattlemechDamageNotation(String area, String location, int index, ItemStatus status)
	{
		_Area = area;
		_Location = location;
		_Index = index;
		_Status = status;
	}
	
	public String toString()
	{
		return _Area + ":" + _Location + "(" + Integer.toString(_Index) + ")" + "-" + _Status.toString();
	}
	
	public int hashCode()
	{
		return toString().hashCode();
	}
	
    public static Vector<BattlemechDamageNotation> getDamageNotationsForArea(String area, Vector<BattlemechDamageNotation> damageNotations)
    {
    	Vector<BattlemechDamageNotation> notations = new Vector<BattlemechDamageNotation>();
    	
    	for (BattlemechDamageNotation notation : damageNotations)
    	{
    		if (notation.getArea().equalsIgnoreCase(area))
    			notations.add(notation);
    	}
    	
    	return notations;
    }
}
