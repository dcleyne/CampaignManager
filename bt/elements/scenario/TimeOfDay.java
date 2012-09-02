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
    	switch (Dice.d6(2))
    	{
    	case 2:
    		return NIGHT;
    	case 3:
    		return DUSK;
    	case 4:
    		return DAWN;
    	}
    	return DAY;
    }
}
