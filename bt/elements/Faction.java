package bt.elements;

/**
 * <p>Title: Inner Sphere</p>
 * <p>Description: Campaign Tracking Software</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Daniel Cleyne
 * @version 0.1
 */

public enum Faction
{	
	ARC_ROYAL_DEFENSE_CORDON,
	BANDIT_KINGDOMS,
	BARONY_OF_STRANG,
	CAPELLAN_CONFEDERATION,
	CIRCINUS_FEDERATION,
	COMSTAR,
	DRACONIS_COMBINE,
	DISPUTED_LC_AND_DC,
	ELYSIAN_FIELDS,
	FEDERATED_COMMONWEALTH,
	FREE_RASALHAGUE_REPUBLIC,
	FEDERATED_SUNS,
	FREE_WORLDS_LEAGUE,
	GREATER_VALKRATE,
	ILLYRIAN_PALATINATE,
	LYRAN_ALLIANCE,
	LYRAN_COMMONWEALTH,
	LOTHIAN_LEAGUE,
	MARIAN_HEGEMONY,
	MICA_MAJORITY,
	MAGISTRACY_OF_CANOPUS,
	NOT_OWNED,
	NEW_COLONY_REGION,
	OUTWORLDS_ALLIANCE,
	OBERON_CONFEDERATION,
	PERIPHERY_OTHER,
	PORT_KRIN,
	RIM_COLLECTION,
	RIM_WORLDS_REPUBLIC,
	ST_IVES_COMPACT,
	TAURIAN_CONCORDAT,
	TORTUGA_DOMINIONS,
	TIKONOV_FREE_REPUBLIC,
	TERRAN_HEGEMONY,
	WORD_OF_BLAKE;

	private static final String[] _Names = {
		"Arc Royal Defense Cordon",
		"Capellan Confederation",
		"Chaos March Disputed",
		"Chaos March with Capellan Influence",
		"Circinus Federation",
		"Clan Blood Spirit",
		"Clan General (Mech Availability)",
		"Clan Ghost Bears",
		"Clan Hell's Horses",
		"Clan Jade Falcons",
		"Clan Nova Cats",
		"Clan Smoke Jaguars",
		"Clan Steel Vipers",
		"Clan Wolf",
		"Combine Peacekeepers",
		"Comstar",
		"Disputed Between Capellan Confederation and Federated Suns",
		"Disputed Between Cappellan Confederation and Free Worlds League",
		"Disputed Between Clan Nova Cats and Clan Smoke Jaguars",
		"Disputed Between Draconis Combine and Lyran Commonwealth",
		"Disputed Between Free Worlds League and Lyran Commonwealth",
		"Draconis Combine",
		"Duchy of Small",
		"Elysian Fields",
		"Federated Commonwealth",
		"Federated Suns",
		"Free Rasalhague Republic",
		"Free Worlds League",
		"Greater Valkrate",
		"Illyrian Palatinate",
		"Inner Sphere General (Mech Availability)",
		"Lyran Alliance",
		"Lyran Commonwealth",
		"Magistry of Cannopus",
		"Marian Hegemony",
		"New Colony Region",
		"Not Owned",
		"Oberon Confederation",
		"Outworlds Alliance",
		"Periphery (Other)",
		"Rim Collection",
		"Rim Worlds Republic",
		"Saiph Triumvirate",
		"Sarna Supremacy",
		"St. Ives Compact",
		"Star League (Mech Availability)",
		"Styk Commonality",
		"Taurian Concordat",
		"Terracap Confederation",
		"Terran Hegemony",
		"Tikonov Free Republic",
		"Tortuga Dominions",
		"Word of Blake"
	};
	
	private static final String[] _Abbreviations = {
		"AR",
		"CC",
		"CMD",
		"CMC",
		"CF",
		"CBS",
		"CG",
		"CGB",
		"CHH",
		"CJF",
		"CNC",
		"CSJ",
		"CSV",
		"CW",
		"CP",
		"CS",
		"CC/FS",
		"CC/FWL",
		"CNC/CSJ",
		"DC/LC",
		"FWL/LC",
		"DC",
		"DS",
		"EF",
		"FC",
		"FS",
		"FRR",
		"FWL",
		"GV",
		"IP",
		"ISG",
		"LA",
		"LC",
		"MoC",
		"MH",
		"NCR",
		"N/A",
		"OC",
		"OA",
		"P",
		"RC",
		"RWR",
		"ST",
		"SS",
		"SIC",
		"SL",
		"SC",
		"TC",
		"TCC",
		"TH",
		"TFR",
		"TD",
		"WOB"
	};

	private static final String[] _Colours = {
		"0x993267",
		"0x33659A",
		"0xFF7E81",
		"0x888888",
		"0x888888",
		"0x888888",
		"0x888888",
		"0x66989A",
		"0xCC64CE",
		"0x669834",
		"0xCCCA68",
		"0x333300",
		"0x003333",
		"0x663300",
		"0x888888",
		"0x0000FF",
		"0xFF7E81",
		"0xFF7E81",
		"0xFF7E81",
		"0xFF7E81",
		"0xFF7E81",
		"0xCC3234",
		"0x888888",
		"0x888888",
		"0xFABC48",
		"0xFABC48",
		"0x66329A",
		"0x9932CD",
		"0x888888",
		"0x888888",
		"0x888888",
		"0x336600",
		"0x4A7FF1",
		"0x33989A",
		"0x006633",
		"0x888888",
		"0x3F3F3F",
		"0x888888",
		"0x33CB9A",
		"0xE8E6F4",
		"0x888888",
		"0x888888",
		"0x888888",
		"0x888888",
		"0x6698CD",
		"0x888888",
		"0x888888",
		"0x996534",
		"0x888888",
		"0x00FE01",
		"0x88C60F",
		"0x888888",
		"0xFEFF01"
	};
	
	
	
	public String toString()
	{ return _Names[ordinal()]; }

	public String getName()
	{ return _Names[ordinal()]; }
	
	public String getAbbreviation()
	{ return _Abbreviations[ordinal()]; }

	public String getColour()
	{ return _Colours[ordinal()]; }
	
	public static Faction getFromAbbreviation(String abbreviation)
	{
		for (Faction f : values() )
			if (_Abbreviations[f.ordinal()].equalsIgnoreCase(abbreviation))
				return f;
		return NOT_OWNED; 
	}	
}
