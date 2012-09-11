package bt.elements;

public enum BattlemechSection 
{
    HEAD,
    CENTER_TORSO,
    LEFT_TORSO,
    RIGHT_TORSO,
    LEFT_ARM,
    RIGHT_ARM,
    LEFT_LEG,
    RIGHT_LEG,
    LEFT_FRONT_LEG,
    RIGHT_FRONT_LEG,
    LEFT_REAR_LEG,
    RIGHT_REAR_LEG;
    
    private static String[] _Names = {"Head","Center Torso","Left Torso","Right Torso","Left Arm", "Right Arm", "Left Leg", "Right Leg","Left Front Leg","Right Front Leg","Left Rear Leg", "Right Rear Leg"};
    private static boolean[] _CanBeBlownOff = {true,false,false,false,true,true,true,true,true,true,true};
   
    private static SectionStatus.Status[] _BlowOffableStatuses = SectionStatus.Status.values();
    private static SectionStatus.Status[] _NotBlowOffableStatuses = { SectionStatus.Status.OK, SectionStatus.Status.DAMAGED,SectionStatus.Status.DESTROYED, SectionStatus.Status.JURYRIGGED };
    
    
    public String GetName()
    { return _Names[ordinal()]; }

    public String toString()
    { return _Names[ordinal()]; }
    
    public boolean canBeBlownOff()
    {
    	return _CanBeBlownOff[ordinal()];
    }
    
    public static BattlemechSection fromString(String s)
    {
    	for (int i = 0; i < _Names.length; i++)
    	{
    		if (_Names[i].equalsIgnoreCase(s))
    			return values()[i];
    	}
    	return null;
    }
    
    public SectionStatus.Status[] validStatuses()
    {
    	if (_CanBeBlownOff[ordinal()])
    		return _BlowOffableStatuses;
    	
    	return _NotBlowOffableStatuses;
    }
}
