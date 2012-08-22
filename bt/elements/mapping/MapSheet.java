package bt.elements.mapping;

import java.util.Vector;

import bt.util.Dice;

public class MapSheet
{
	private String _Name;
	private String _Set;
	private String _Notes;

	private Vector<String> _Locations = new Vector<String>();
	
	public String getName()
	{
		return _Name;
	}
	
	public void setName(String name)
	{
		_Name = name;
	}
	
	public String getSet()
	{
		return _Set;
	}
	
	public void setSet(String set)
	{
		_Set = set;
	}
	
	public String getNotes()
	{
		return _Notes;
	}
	
	public void setNotes(String notes)
	{
		_Notes = notes;
	}	
	
	public Vector<String> getLocations()
	{
		return _Locations;
	}
	
	public String getRandomHexList(int numHexes)
	{
		String list = "";
		int locationsLeft = numHexes;
		Vector<String> locations = new Vector<String>(_Locations);
		while (locationsLeft > 0)
		{
			int index = Dice.random(locations.size()) - 1;
			list += locations.elementAt(index);
			locations.remove(index);
			if (locationsLeft > 1)
				list += ",";
			locationsLeft--;
		}
		
		return list;
	}

	
}
