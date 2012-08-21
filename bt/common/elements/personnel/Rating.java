package bt.common.elements.personnel;


/**
 * <p>Title: Miradan Phedd</p>
 *
 * <p>Description: Library for all Miradan related work</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Miradan Phedd</p>
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
public enum Rating
{
	GREEN,
	REGULAR,
	VETERAN,
	ELITE;
	
	private static final String[] _Names = {
		"Green",
		"Regular",
		"Veteran",
		"Elite"
	};
	
	private static final double[] _SalaryMultipliers = {
		1.0,
		1.5,
		2.0,
		3.0
	};
	
	private static int[] _PilotingValues = {6,5,4,3};
	private static int[] _GunneryValues = {5,4,3,2};
	private static int[] _AgeValues = {6,8,10,12};
	
    public String getName()
    { return _Names[ordinal()]; }

    public double getSalaryMultiplier()
    { return _SalaryMultipliers[ordinal()]; }

    public int getPilotingValue()
    { return _PilotingValues[ordinal()]; }
    
    public int getGunneryValue()
    { return _GunneryValues[ordinal()]; }
    
    public int getAgeModifier()
    { return _AgeValues[ordinal()]; }
    
    public String toString()
    { return getName(); }
    
    public static Rating fromString(String s)
    {
    	for (int i = 0; i < _Names.length; i++)
    	{
    		if (_Names[i].equalsIgnoreCase(s))
    			return values()[i];
    	}
    	return null;
    }

}
