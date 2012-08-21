package bt.common.elements.galaxy;


public enum SettlementType
{
    NONE,
    CAPITOL,
    SPACEPORT,
    CITY,
    TOWN,
    SETTLEMENT,
    MINE;

    private static final String _SettlementNames[] =
        {
        "None", "Capitol", "Space Port", "City", "Town", "Settlement", "Mine"};

    private static final int _RoadWidths[] =
    { 0, 3, 3, 2, 1, 1, 1};
    
    public static SettlementType getDefaultTerrain()
    {
        return TOWN;
    }

    public static String getName(int index)
    {
        return _SettlementNames[index];
    }

    public int getRoadWidth()
    {
        return _RoadWidths[ordinal()];
    }
    
    public static SettlementType fromString(String name)
    {
    	for (int i = 0; i < values().length; i++)
    		if (_SettlementNames[i].equalsIgnoreCase(name))
    			return values()[i];
    	return SettlementType.NONE;
    }
    
    public String toString()
    {
    	return _SettlementNames[ordinal()];
    }


}
