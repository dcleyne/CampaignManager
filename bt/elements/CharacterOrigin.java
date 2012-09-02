package bt.elements;

import bt.elements.personnel.PersonnelModifier;

public enum CharacterOrigin 
{
	INNER_SPHERE,
	PERIPHERY,
	CLAN;
	
	private static String[] _Names = {
		"Inner Sphere",
		"Periphery",
		"Clan"
	};
		
	private static int[] _AgeModifiers = {0,1,-1};
	private static int[] _PilotingModifiers = {0,1,-1};
	private static int[] _GunneryModifiers = {0,0,-1};
	
	private static int[][] _PersonnelModifiers = {
		{0,1,-1},
		{0,1,-1},
		{0,1,-1},
		{0,1,-1},
		{0,1,-1},
		{0,1,0}
	};
	
	private static int[][] _PointPoolModifiers = {
		{0,0,1},
		{0,-2,2},
		{0,0,0},
		{0,-1,-3}
	};
	
	private static boolean[] _ClanTraining = {false,false,true};

	public int getAgeModifier()
	{ return _AgeModifiers[ordinal()]; }
	
	public int getPilotingModifier()
	{ return _PilotingModifiers[ordinal()]; }
	
	public int getGunneryModifier()
	{ return _GunneryModifiers[ordinal()]; }
	
	public int getPersonnelModifier(PersonnelModifier pm)
	{ return _PersonnelModifiers[pm.ordinal()][ordinal()]; }
	
	public int getPointPoolModifier(PointPoolModifier ppm)
	{ return _PointPoolModifiers[ppm.ordinal()][ordinal()]; }
	
	public boolean isClanTraining()
	{ return _ClanTraining[ordinal()]; }
		
	public String toString()
	{ return _Names[ordinal()]; }
	
	public static CharacterOrigin fromString(String s)
	{
    	for (int i = 0; i < _Names.length; i++)
    	{
    		if (_Names[i].equalsIgnoreCase(s))
    			return values()[i];
    	}
    	return null;
	}
		
}
