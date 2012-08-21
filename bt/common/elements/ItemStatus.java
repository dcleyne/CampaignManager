package bt.common.elements;

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
