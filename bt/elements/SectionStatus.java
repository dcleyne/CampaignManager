package bt.elements;

public enum SectionStatus 
{
    OK,
    DAMAGED,
    DESTROYED,
    BLOWNOFF,
    JURYRIGGED;
    
    private static String[] _Names = {"Ok","Damaged","Destroyed","Blown Off","JuryRigged"};
   
    public String GetName()
    { return _Names[ordinal()]; }

    public String toString()
    { return _Names[ordinal()]; }
    
    public static SectionStatus fromString(String s)
    {
    	for (int i = 0; i < _Names.length; i++)
    	{
    		if (_Names[i].equalsIgnoreCase(s))
    			return values()[i];
    	}
    	return null;
    }
    
}
