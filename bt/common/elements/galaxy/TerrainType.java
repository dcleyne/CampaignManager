package bt.common.elements.galaxy;


public enum TerrainType
{
    NONE,
    DESERT,
    DEEPFOREST,
    DEEPJUNGLE,
    DEEPWATER,
    MOUNTAINS,
    FOREST,
    JUNGLE,
    WATER,
    WOODEDHILLS,
    HILLS,
    PLAINS,
    SWAMP,
    WASTELAND,
    SCRUB,
    LANDICE,
    VOLCANO,
    LAKE,
    WATERICE;

    private static final String TerrainNames[] =
    {
        "None", "Desert", "Deep Forest", "Deep Jungle", "Deep Water", "Mountains", "Forest", "Jungle", "Water", "Wooded Hills", "Hills", "Plains", "Swamp", "Wasteland", "Scrub", "Ice (Land)", "Volcano","Lake","Ice (Water)"
    };

    private static final int MovementCost[] =
    {
    	Integer.MAX_VALUE, 6, 20, 20, Integer.MAX_VALUE, 25, 10, 10, Integer.MAX_VALUE, 15, 6, 2, 15, 6, 6, 35, Integer.MAX_VALUE,Integer.MAX_VALUE,40
    };
    
    public static TerrainType getDefaultTerrain()
    {
        return PLAINS; //Deep Water
    }

    public static String getTerrainName(int index)
    {
        return TerrainNames[index];
    }
    
    public static TerrainType fromString(String name)
    {
    	for (int i = 0; i < values().length; i++)
    		if (TerrainNames[i].equalsIgnoreCase(name))
    			return values()[i];
    	return TerrainType.NONE;
    }
    
    public String toString()
    {
    	return TerrainNames[ordinal()];
    }
    
    public int movementCost()
    {
    	return MovementCost[ordinal()];
    }


}
