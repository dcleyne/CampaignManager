package bt.common.elements;

public enum ElementType 
{
	NONE,
	BATTLEMECH,
	VEHICLE,
	AEROSPACEFIGHTER,
	DROPSHIP,
	JUMPSHIP,
	WARSHIP,
	CONVENTIONALFIGHTER,
	VTOL,
	PROTOMECH,
	POWERARMOUR;

	private static final String[] _Names = {"<None>",
	"Battlemech",
	"Vehicle",
	"Aerospace Fighter",
	"Dropship",
	"Jumpship",
	"Warship",
	"Conventional Fighter",
	"VTOL",
	"Protomech",
	"Power Armour"};
	
	public String toString()
	{
		return _Names[ordinal()];
	}

}
