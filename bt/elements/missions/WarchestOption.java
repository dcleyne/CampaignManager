package bt.elements.missions;

public class WarchestOption 
{
	private String _Name;
	private int _Bonus;
	private String _Notes;
	
	public String getName() 
	{
		return _Name;
	}
	public void setName(String name) 
	{
		_Name = name;
	}
	public int getBonus() 
	{
		return _Bonus;
	}
	public void setBonus(int bonus) 
	{
		_Bonus = bonus;
	}
	public String getNotes() 
	{
		return _Notes;
	}
	public void setNotes(String notes) 
	{
		_Notes = notes;
	}
	
	public WarchestOption()
	{
		
	}
	
	public WarchestOption(WarchestOption option)
	{
		_Name = option._Name;
		_Bonus = option._Bonus;
		_Notes = option._Notes;
	}
}
