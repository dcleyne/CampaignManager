package bt.elements.unit;

public enum Role 
{
	PILOT,
	COMMANDER,
	GUNNER,
	DRIVER;
	
	private static String[] _Names = {"Pilot", "Commander", "Gunner", "Driver"};
	
	public String toString()
	{
		return _Names[ordinal()];
	}
	
	public static Role fromString(String str)
	{
		for (int i = 0; i < _Names.length; i++)
		{
			if (_Names[i].equalsIgnoreCase(str))
				return values()[i];
		}
		
		return null;
	}
}
