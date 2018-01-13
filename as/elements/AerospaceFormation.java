package as.elements;

public enum AerospaceFormation
{
	INTERCEPTOR,
	AEROSPACE_SUPERIORITY_SQUADROM,
	FIRE_SUPPORT,
	STRIKE,
	ELECTRONIC_WARFARE,
	TRANSPORT;
	
	private static String[] Names = {"Interceptor", "Aerospace Superiority", "Fire Support", "Strike", "Electronic Warfare", "Transport"};
	
	public String toString()
	{
		return Names[ordinal()];
	}
	
	public static AerospaceFormation fromString(String string) throws Exception
	{
		for (int i = 0; i < Names.length; i++)
		{
			if (string.equalsIgnoreCase(Names[i]))
				return values()[i];
		}
		throw new Exception("Unable to determine AerospaceFormation from: " + string);
	}
}
