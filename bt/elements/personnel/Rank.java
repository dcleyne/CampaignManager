package bt.elements.personnel;


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
public enum Rank
{
	NONE,
	WARLORD,
	FIELDMARSHAL,
	GENERAL,
	LTGENERAL,
	COMMANDERGENERAL,
	CAPTAINGENERAL,
	MAJGENERAL,
	BRIGGENERAL,
	COLONEL,
	LTCOLONEL,
	FORCECOMMANDER,
	MAJOR,
	CAPTAIN,
	COMMANDER,
	LIEUTENANT,
	CHIEFPETTYOFFICER,
	FIRSTLIEUTENANT,
	LIEUTENANTSG,
	SECONDLIEUTENANT,
	LIEUTENANTJG,
	SUBCOMMANDER,
	CHIEFWARRANTOFFICER,
	WARRANTOFFICER,
	COMMANDSERGEANTMAJOR,
	SENIORSERGEANTMAJOR,
	FORCELEADER,
	STAFFSERGEANTMAJOR,
	FIRSTSERGEANT,
	ASSISTANTFORCELEADER,
	TALONSERGEANT,
	MASTERSERGEANT,
	SERGEANTFC,
	STAFFSERGEANT,
	LANCESERGEANT,
	SERGEANT,
	SENIORCOPORAL,
	LANCECORPORAL,
	CORPORAL,
	PRIVATEFC,
	PRIVATE,
	ASTECH3,
	ASTECH2,
	ASTECH1,
	RECRUIT,
	MECH_WARRIOR;	
	
	private static final String[] _Names = {
		"None",
		"Warlord",
		"Field Marshal",
		"General",
		"Lt. General",
		"Commander General",
		"Captain General",
		"Maj. General",
		"Brig. General",
		"Colonel",
		"Lt. Colonel",
		"Force Commander",
		"Major",
		"Captain",
		"Commander",
		"Lieutenant",
		"Chief Petty Officer",
		"1st Lieutenant",
		"Lieutenant SG",
		"2nd Lieutenant",
		"Lieutenant JG",
		"Subcommander",
		"Chief Warrant Officer",
		"Warrant Officer",
		"Command Sergeant Major",
		"Senior Sergeant Major",
		"Force Leader",
		"Staff Sergeant Major",
		"First Sergeant",
		"Assistant Force Leader",
		"Talon Sergeant",
		"Master Sergeant",
		"Sergeant FC",
		"Staff Sergeant",
		"Lance Sergeant",
		"Sergeant",
		"Senior Coporal",
		"Lance Corporal",
		"Corporal",
		"Private FC",
		"Private",
		"Astech 3",
		"Astech 2",
		"Astech 1",
		"Recruit",
		"MechWarrior"
	};

	private static final double[] _SalaryMultiplier = {
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
		0.0,
		0.0,
		0.0
	};
	
    public String toString()
    { return _Names[ordinal()]; }

    public double getSalaryMultiplier()
    { return _SalaryMultiplier[ordinal()]; }
    
    public static Rank fromString(String r)
    {
    	
    	for (int i = 0; i < _Names.length; i++)
    	{
    		if (_Names[i].equalsIgnoreCase(r))
    			return values()[i];
    	}
    	return null;
    }
}
