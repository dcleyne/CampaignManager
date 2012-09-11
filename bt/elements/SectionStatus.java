package bt.elements;

public class SectionStatus 
{
	public enum Status
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
	    
	    public static Status fromString(String s)
	    {
	    	for (int i = 0; i < _Names.length; i++)
	    	{
	    		if (_Names[i].equalsIgnoreCase(s))
	    			return values()[i];
	    	}
	    	return null;
	    }
	}
	
	private Status _Status = Status.OK;
	private boolean _Breached = false;
	
	public Status getStatus() {
		return _Status;
	}
	public void setStatus(Status status) {
		_Status = status;
	}
	public boolean isBreached() {
		return _Breached;
	}
	public void setBreached(boolean breached) {
		_Breached = breached;
	}
	
	public SectionStatus()
	{
	}
	
	public SectionStatus(Status status, boolean breached)
	{
		_Status = status;
		_Breached = breached;
	}
    
}
