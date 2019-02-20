package bt.elements.missions;

public class Objective 
{
	private String _Name;
	private int _PointsAwarded;
	private String _Description;
	
	public String getName() 
	{
		return _Name;
	}
	public void setName(String name) 
	{
		_Name = name;
	}
	public int getPointsAwarded() 
	{
		return _PointsAwarded;
	}
	public void setPointsAwarded(int pointsAwarded) 
	{
		_PointsAwarded = pointsAwarded;
	}
	public String getDescription() 
	{
		return _Description;
	}
	public void setDescription(String description) 
	{
		_Description = description;
	}
	
	public Objective()
	{
		
	}

	public Objective(Objective o)
	{
		_Name = o._Name;
		_PointsAwarded = o._PointsAwarded;
		_Description = o._Description;
	}
}
