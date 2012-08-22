package bt.elements.scenario;

import bt.util.Dice;

public enum Season
{
    SUMMER,
    AUTUMN,
    WINTER,
    SPRING;

    private static String[] _Names = {"Summer", "Autumn", "Winter", "Spring" };
    private static int[] _WindModifiers = { 0, 1, 0, 1 };
    private static Season[] _RandomSeasons = { Season.SUMMER, Season.SUMMER, Season.AUTUMN, Season.WINTER, Season.SPRING, Season.SPRING };
    private static int[] _RainModifiers = { -1, 1, 0, 2 };

    public static String GetName(int season)
    {
        return _Names[season];
    }

    public static int GetWindModifier(int season)
    {
        return _WindModifiers[season];
    }

    public static Season getRandomSeason()
    {
        return _RandomSeasons[Dice.d6(1) - 1];
    }

    public static int getRainModifiers(int season)
    {
        return _RainModifiers[season];
    }
    
    public String toString()
    { return _Names[ordinal()]; }
    
    public static Season fromString(String r)
    {
    	
    	for (int i = 0; i < _Names.length; i++)
    	{
    		if (_Names[i].equalsIgnoreCase(r))
    			return values()[i];
    	}
    	return null;
    }
}
