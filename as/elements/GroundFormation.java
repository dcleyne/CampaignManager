package as.elements;

public enum GroundFormation
{
	BATTLE,
	ASSAULT,
	STRIKER,
	CAVALRY,
	FIRE,
	RECON,
	PURSUIT,
	COMMAND,
	SUPPORT;
	
	private static String[] Names = {"Battle", "Assault", "Striker", "Cavalry", "Fire", "Recon", "Pusuit", "Command", "Support"};
	
	public String toString()
	{
		return Names[ordinal()];
	}
	
	public static GroundFormation fromString(String string) throws Exception
	{
		for (int i = 0; i < Names.length; i++)
		{
			if (string.equalsIgnoreCase(Names[i]))
				return values()[i];
		}
		throw new Exception("Unable to determine GroundFormation from: " + string);
	}
}
