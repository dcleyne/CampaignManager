package bt.mapping;

public enum DecorationLocation
{
	NORTH, // Is reserved for the Hex Number
	EAST, SOUTH, WEST, CENTRE;
	
	private static final String[] Names = {"North", "South", "East", "West", "Centre"};
	
	public String toString()
	{
		return Names[ordinal()];
	}
	
	public static DecorationLocation fromString(String string) throws Exception
	{
		for (int i = 0; i < Names.length; i++)
			if (Names[i].equalsIgnoreCase(string))
				return values()[i];
		throw new Exception("Unable to get DecorationLocation from : " + string);
	}
}
