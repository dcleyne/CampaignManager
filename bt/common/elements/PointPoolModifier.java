package bt.common.elements;

public enum PointPoolModifier
{
	EXPERIENCE,
	TECHNOLOGY,
	MASS,
	CASH;
	
	private static final String[] _Names = {"Experience","Technology","Mass","Cash"};
	
	public String toString()
	{ return _Names[ordinal()]; }
	
	public static PointPoolModifier fromString(String name)
	{
		for (int i = 0; i < values().length; i++)
			if (_Names[i].equalsIgnoreCase(name))
				return values()[i];
		
		return null;
	}
}
