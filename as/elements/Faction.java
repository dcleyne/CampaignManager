package as.elements;

import java.util.ArrayList;
import java.util.List;

public class Faction
{
	private int _ID;
	private String _Group;
	private String _Name;
	private ArrayList<Integer> _Eras = new ArrayList<Integer>();

	public int getID()
	{
		return _ID;
	}

	public void setID(int id)
	{
		_ID = id;
	}

	public String getGroup()
	{
		return _Group;
	}

	public void setGroup(String group)
	{
		_Group = group;
	}

	public String getName()
	{
		return _Name;
	}

	public void setName(String name)
	{
		_Name = name;
	}

	public List<Integer> getEras()
	{
		return _Eras;
	}

	public void addEra(int era)
	{
		if (!_Eras.contains(era))
			_Eras.add(era);
	}

	public void clearEras()
	{
		_Eras.clear();
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(_Name);
		sb.append("(");
		sb.append(_ID);
		sb.append(") [");
		sb.append(_Group);
		sb.append("]");
		return sb.toString();
	}

}
