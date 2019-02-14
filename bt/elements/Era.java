package bt.elements;

public enum Era 
{
	AGE_OF_WAR,
	STAR_LEAGUE,
	EARLY_SUCCESSION_WAR,
	LATE_SUCCESSION_WAR_LOSTECH,
	LATE_SUCCESSION_WAR_RENAISSANCE,
	CLAN_INVASION,
	CIVIL_WAR,
	JIHAD,
	EARLY_REPUBLIC,
	LATE_REPUBLIC,
	DARK_AGES;
	
	private static String[] Names = {
			"Age of War","Star League","Early Succession War","Late Succession War - LosTech","Late Succession War - Renaissance"
			,"Clan Invasion","Civil War","Jihad","Early Republic","Late Republic","Dark Ages"};
	
	private static int[] Ids = {9,10,11,255,256,13,247,14,15,254,16};
	private static int[] StartYears = {2005,2571,2781,2901,3020,3050,3062,3068,3068,3101,3131};
	private static int[] EndYears = {2570,2780,2900,3019,3049,3061,3067,3085,3100,3130,3999};
	
	public String toString()
	{
		return Names[ordinal()];
	}
	
	public int getID()
	{
		return Ids[ordinal()];
	}
	public int getStartYear()
	{
		return StartYears[ordinal()];
	}
	public int getEndYears()
	{
		return EndYears[ordinal()];
	}
	
	public static Era fromString(String string) throws Exception
	{
		for (Era e : values() )
			if (Names[e.ordinal()].equalsIgnoreCase(string))
				return e;
		
		throw new Exception("Unable to determine Era from string (" + string + ")"); 
	}	

	public static Era fromID(int id) throws Exception
	{
		for (Era e : values() )
			if (Ids[e.ordinal()] == id)
				return e;
		
		throw new Exception("Unable to determine Era from id (" + id + ")"); 
	}	

}
