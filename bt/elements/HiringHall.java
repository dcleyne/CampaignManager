package bt.elements;

public enum HiringHall
{

	ANTALLOS_PORT_KRIN,
	ARC_ROYAL,
	ASTROKASZY,
	FLETCHER,
	GALATEA,
	HEROTITUS,
	NORTHWIND,
	OUTREACH,
	SOLARISVII,
	WESTERHAND;
	
	private static final String[] _Names = {
		"Antallos (Port Krin)",
		"Arc-Royal",
		"Astrokaszy",
		"Fletcher",
		"Galatea",
		"Herotitus",
		"Northwind",
		"Outreach",
		"Solaris VII",
		"Westerhand"		
		};
	
	private static int[][] _PersonnelModifiers = {
		{0,1,0,1,2,0,1,2,2,1},
		{1,1,2,1,1,2,2,2,1,1},
		{2,0,3,0,0,2,0,1,0,0},
		{-1,1,1,1,0,1,2,1,2,0},
		{0,1,0,0,0,2,0,0,1,0},
		{0,0,0,0,0,0,0,0,0,0}
	};
	
	private static int[][] _PointPoolModifiers = {
		{-2,2,-1,-1,0,0,1,2,1,-1},
		{-2,1,-1,-1,-1,-1,1,2,-1,-1},
		{1,-2,-1,2,1,1,0,0,-1,-1},
		{0,-1,0,0,-1,-1,-1,-3,-2,0}
	};
	
	private static int[] _Planets = {
		118,
		128,
		155,
		670,
		697,
		826,
		1414,
		1466,
		1778,
		2053
	};
	public int getHiringHallPlanetID()
	{ return _Planets[ordinal()]; }
	
	
	public int getPersonnelModifier(PersonnelModifier pm)
	{ return _PersonnelModifiers[pm.ordinal()][ordinal()]; }
	
	public int getPointPoolModifier(PointPoolModifier ppm)
	{ return _PointPoolModifiers[ppm.ordinal()][ordinal()]; }
		
	
	public String toString()
	{ return _Names[ordinal()]; }
	
	public static HiringHall fromString(String name)
	{
		for (int i = 0; i < values().length; i++)
			if (_Names[i].equalsIgnoreCase(name))
				return values()[i];
		
		
		return null;
	}
}
