package bt.elements;

public enum Quality
{
	GREEN,
	REGULAR,
	VETERAN,
	ELITE;
	
	private static String[] Names = {"Green", "Regular", "Veteran", "Elite"};
	
	public String toString()
	{
		return Names[ordinal()];
	}
	
	public static Quality fromString(String name) throws Exception
	{
		for (int i = 0; i < values().length; i++)
		{
			if (name.equalsIgnoreCase(Names[i]))
				return values()[i];
		}
		
		throw new Exception("Unable to get Quality from : " + name);
	}
}
