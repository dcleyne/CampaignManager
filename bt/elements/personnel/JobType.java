package bt.elements.personnel;

import bt.elements.ElementType;


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
	VTOLCREWMEMBER,
	ASSISTANTTECHNICIAN;

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
	ElementType.VTOL,
	ElementType.NONE};

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
	"VTOL Crew Member",
	"Astech"};

	private static final double[] _BaseMonthlySalary = 
	{
	    1625.0,
	    975.0,
	    0,
	    812.5,
	    1137.5,
	    975.0,
	    866.67,
	    866.67,
	    866.67,
	    0,
	    693.33,
	    1300.0,
	    693.33,
	    520.0,
	    606.67,
	    1040.0,
	    693.33,
	    1625.0,
	    1040.0,
	    0,
	    975.0,
	    520.0,
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
