package bt.elements.unit;

public enum Role 
{
	NONE,
	PILOT,
	COMMANDER,
	GUNNER,
	DRIVER;
	
	private static String[] _Names = {"No Role", "Pilot", "Commander", "Gunner", "Driver"};
	
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
