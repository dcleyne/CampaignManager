package bt.elements;

import bt.elements.personnel.PersonnelModifier;

public enum CharacterTraining 
{
	FAMILY_TRAINED,
	ACADEMY_DROPOUT,
	ACADEMY_GRADUATE,
	ACADEMY_HONORS_GRADUATE,
	CLAN_DROPOUT,
	CLAN_GRADUATE,
	CLAN_HONORS_GRADUATE;

	private static String[] _Names = {"Family Trained (Nobility)","Academy Dropout","Academy Graduate","Academy Honors Graduate","Clan Dropout", "Clan Graduate", "Clan Graduate w/2+ Kills"};
	
	private static int[] _BaseAgeNumbers = {18,18,20,22,18,20,20};
	private static int[] _PilotingTargetNumbers = {7,6,5,4,6,5,4};
	private static int[] _GunneryTargetNumbers = {6,5,4,4,5,4,3};
	
	private static int[][] _PersonnelModifiers = {
		{1,-1,0,1,-1,0,0},
		{0,0,1,1,0,1,0},
		{0,1,0,0,1,0,0},
		{0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0},
		{1,0,0,0,0,0,0}
	};
	
	private static int[][] _PointPoolModifiers = {
		{0,-1,0,1,-1,0,0},
		{0,0,0,0,0,0,1},
		{0,0,0,0,0,0,0},
		{2,0,0,0,0,0,0}
	};
	
	private static boolean[] _ClanTraining = {false,false,false,false,true,true,true};

	public int getBaseAge()
	{ return _BaseAgeNumbers[ordinal()]; }
	
	public int getPilotingTargetNumber()
	{ return _PilotingTargetNumbers[ordinal()]; }
	
	public int getGunneryTargetNumber()
	{ return _GunneryTargetNumbers[ordinal()]; }
	
	public int getPersonnelModifier(PersonnelModifier pm)
	{ return _PersonnelModifiers[pm.ordinal()][ordinal()]; }
	
	public int getPointPoolModifier(PointPoolModifier ppm)
	{ return _PointPoolModifiers[ppm.ordinal()][ordinal()]; }
	
	public boolean isClanTraining()
	{ return _ClanTraining[ordinal()]; }
	
	public String toString()
	{ return _Names[ordinal()]; }
	
    public static CharacterTraining fromString(String s)
    {
    	for (int i = 0; i < _Names.length; i++)
    	{
    		if (_Names[i].equalsIgnoreCase(s))
    			return values()[i];
    	}
    	return null;
    }	
}
