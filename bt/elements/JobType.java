package bt.elements;


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
public enum JobType
{	
	MECHWARRIOR,
	VEHICLECREWMEMBER,
	FIGHTERPILOT,
	INFANTRY,
	SCOUT,
	ARTILLERY,
	DROPSHIPCREWMEMBER,
	JUMPSHIPCREWMEMBER,
	WARSHIPCREWMEMBER,
	C3STAFF,
	ADMINISTRATION,
	SPECIALISTOTHER,
	TECHNICIAN,
	AEROSPACETECHNICIAN,
	MECHANIC,
	ENGINEER,
	MEDTECH,
	AEROSPACEPILOT,
	POWERARMOURINFANTRY,
	PROTOMECHPILOT,
	VTOLCREWMEMBER;

	private static final ElementType[] _ElementTypes = { 
	ElementType.BATTLEMECH,
	ElementType.VEHICLE,
	ElementType.CONVENTIONALFIGHTER,
	ElementType.NONE,
	ElementType.VEHICLE,
	ElementType.VEHICLE,
	ElementType.DROPSHIP,
	ElementType.JUMPSHIP,
	ElementType.WARSHIP,
	ElementType.NONE,
	ElementType.NONE,
	ElementType.NONE,
	ElementType.NONE,
	ElementType.NONE,
	ElementType.NONE,
	ElementType.NONE,
	ElementType.NONE,
	ElementType.AEROSPACEFIGHTER,
	ElementType.POWERARMOUR,
	ElementType.PROTOMECH,
	ElementType.VTOL};

	private static final String[] _Names = {
	"Mechwarrior",
	"VehicleCrewMember",
	"FighterPilot",
	"Infantry",
	"Scout",
	"Artillery",
	"Dropship Crew Member",
	"Jumpship Crew Member",
	"Warship Crew Member",
	"C3 Staff",
	"Administration",
	"Specialist Other",
	"Technician",
	"Aerospace Technician",
	"Mechanic",
	"Engineer",
	"Medtech",
	"Aerospace Pilot",
	"Power Armour Infantry",
	"Protomech Pilot",
	"VTOL Crew Member"};

	private static final double[] _BaseMonthlySalary = 
	{
		0.0,
		0.0,
		0.0,
		0.0,
		0.0,
		0.0,
		0.0,
		0.0,
		0.0,
		0.0,
		0.0,
		0.0,
		0.0,
		0.0,
		0.0,
		0.0,
		0.0,
		0.0,
		0.0,
		0.0,
		0.0
	};
	

    public String GetName()
    { return _Names[ordinal()]; }

    public ElementType GetElementType()
    { return _ElementTypes[ordinal()]; }

    public double GetBaseMonthlySalary()
    { return _BaseMonthlySalary[ordinal()]; }

    public String toString()
    { return _Names[ordinal()]; }
    
    public static JobType fromString(String s)
    {
    	for (int i = 0; i < _Names.length; i++)
    	{
    		if (_Names[i].equalsIgnoreCase(s))
    			return values()[i];
    	}
    	return null;
    }
}
