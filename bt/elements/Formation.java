package bt.elements;

import java.util.ArrayList;
import java.util.List;

public class Formation 
{
	private String _Name;
	private Quality _Quality;
	private ArrayList<Formation> _Children = new ArrayList<Formation>();
	
	public String getName() 
	{
		return _Name;
	}
	
	public void setName(String name) 
	{
		_Name = name;
	}
	
	public Quality getQuality() 
	{
		return _Quality;
	}

	public void setQuality(Quality quality) 
	{
		_Quality = quality;
	}

	public void addChild(Formation f)
	{
		if (!_Children.contains(f))
			_Children.add(f);
	}
	
	public List<Formation> getChildren()
	{
		return _Children;
	}
}
