package bt.elements.unit;

public enum PersonnelModifier
{
	SAME,
	COMPLIMENTARY,
	DISTANT,
	SUPPLY,
	MEDICAL,
	ADMINISTRATION;
	
	private static final String[] _Names = {"Same","Complimentary","Distant","Supply","Medical","Administration"};
	
	public String toString()
	{ return _Names[ordinal()]; }
	
	public static PersonnelModifier fromString(String name)
	{
		for (int i = 0; i < values().length; i++)
			if (_Names[i].equalsIgnoreCase(name))
				return values()[i];
		
		return null;
	}
	
}
