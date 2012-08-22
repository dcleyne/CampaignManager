package bt.elements.scenario;

import bt.util.Dice;


public enum TimeOfDay
{
	DAWN,
	DAY,
	DUSK,
	NIGHT;

	private static String[] _Names = {"Dawn","Day","Dusk","Night"}; 

    public String toString()
    { return _Names[ordinal()]; }
    
    public static TimeOfDay fromString(String r)
    {
    	
    	for (int i = 0; i < _Names.length; i++)
    	{
    		if (_Names[i].equalsIgnoreCase(r))
    			return values()[i];
    	}
    	return null;
    }
    
    public static TimeOfDay random()
    {
    	switch (Dice.d6(1))
    	{
    	case 1:
    		return DAWN;
    	case 2:
    		return DUSK;
    	case 3:
    		return NIGHT;
    	}
    	return DAY;
    }
}
