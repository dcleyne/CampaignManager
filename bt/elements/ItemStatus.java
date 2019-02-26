package bt.elements;

public enum ItemStatus
{
    OK,
    DAMAGED,
    DESTROYED,
    REPAIRED,
    JURYRIGGED;
    
    private static String[] _Names = {"Ok","Damaged","Destroyed","Repaired","JuryRigged"};
   
    public String GetName()
    { return _Names[ordinal()]; }

    public String toString()
    { return _Names[ordinal()]; }
    
    public boolean isUsable()
    {
    	return this != DAMAGED && this != DESTROYED; 
    }
    
    public static ItemStatus fromString(String s)
    {
    	for (int i = 0; i < _Names.length; i++)
    	{
    		if (_Names[i].equalsIgnoreCase(s))
    			return values()[i];
    	}
    	return null;
    }
    
}
