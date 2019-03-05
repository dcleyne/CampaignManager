package bt.elements.collection;

public class MiniatureCollectionItem 
{
	private String _Name;
	private String _Notes;
	
	public MiniatureCollectionItem()
	{
	}
	
	public MiniatureCollectionItem(MiniatureCollectionItem mci)
	{
		_Name = mci._Name;
		_Notes = mci._Notes;
	}
	
	public String getName() 
	{
		return _Name;
	}
	
	public void setName(String design) 
	{
		_Name = design;
	}
	
	public String getNotes() 
	{
		return _Notes;
	}
	
	public void setNotes(String notes) 
	{
		_Notes = notes;
	}

	
}
