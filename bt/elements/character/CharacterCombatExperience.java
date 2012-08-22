package bt.elements.character;

import bt.elements.unit.PersonnelModifier;

public enum CharacterCombatExperience 
{
	COVERT_OPS,
	TRAINING_BATTALION,
	TOUR_OF_DUTY;
	
	private static String[] _Names = {"Covert Ops","Training Battalion","Tour Of Duty"};
	

	private static int[] _AgeModifiers = {4,2,2};
	private static int[] _PilotingModifiers = {-1,0,-1};
	private static int[] _GunneryModifiers = {-1,0,-1};
	
	private static int[][] _PersonnelModifiers = {
	{0,1,1},
	{0,0,0},
	{0,0,0},
	{0,0,1},
	{0,0,0},
	{0,0,0}};
	
	private static int[][] _PointPoolModifiers = {
		{2,1,1},
		{1,0,1},
		{-2,1,0},
		{0,0,0}};
	
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
		
	public String toString()
	{ return _Names[ordinal()]; }
	
    public static CharacterCombatExperience fromString(String s)
    {
    	for (int i = 0; i < _Names.length; i++)
    	{
    		if (_Names[i].equalsIgnoreCase(s))
    			return values()[i];
    	}
    	return null;
    }
}
