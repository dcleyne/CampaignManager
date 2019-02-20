package bt.elements.missions;

public class SpecialRule 
{
	private String _Name;
	private String _Description;
	
	public String getName() 
	{
		return _Name;
	}
	public void setName(String name) 
	{
		_Name = name;
	}
	public String getDescription() 
	{
		return _Description;
	}
	public void setDescription(String description) 
	{
		_Description = description;
	}
	
	public SpecialRule()
	{
		
	}
	
	public SpecialRule(SpecialRule sr)
	{
		_Name = sr._Name;
		_Description = sr._Description;
	}
	
}
