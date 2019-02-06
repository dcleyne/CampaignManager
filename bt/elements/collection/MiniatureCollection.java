package bt.elements.collection;

import java.util.ArrayList;

public class MiniatureCollection 
{
	private String _Name;
	private String _Description;
	
	private ArrayList<MiniatureCollectionItem> _Items = new ArrayList<MiniatureCollectionItem>();

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
	
	public void addItem(MiniatureCollectionItem item)
	{
		_Items.add(item);
	}

}
