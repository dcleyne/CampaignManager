package as.elements;

public class Era 
{

	private int _ID;
	private String _Name;
	private int _StartYear;
	private int _EndYear;
	
	public int getID()
	{
		return _ID;
	}
	
	public void setID(int iD)
	{
		_ID = iD;
	}

	public String getName()
	{
		return _Name;
	}

	public void setName(String name)
	{
		_Name = name;
	}

	public int getStartYear()
	{
		return _StartYear;
	}

	public void setStartYear(int startYear)
	{
		_StartYear = startYear;
	}

	public int getEndYear()
	{
		return _EndYear;
	}

	public void setEndYear(int endYear)
	{
		_EndYear = endYear;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(_Name);
		sb.append("(");
		sb.append(_ID);
		sb.append(") [");
		sb.append(_StartYear);
		sb.append("-");
		sb.append(_EndYear);
		sb.append("]");
		return sb.toString();
	}
	
}
