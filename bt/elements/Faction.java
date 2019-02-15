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
	CHAOS_MARCH_DISPUTED,
	CHAOS_MARCH_WITH_CAPELLAN_INFLUENCE,
	CIRCINUS_FEDERATION,
	CLAN_BLOOD_SPIRIT,
	CLAN_GENERAL,
	CLAN_GHOST_BEARS,
	CLAN_HELLS_HORSE,
	CLAN_JADE_FALCONS,
	CLAN_NOVA_CATS,
	CLAN_SMOKE_JAGUARS,
	CLAN_STEEL_VIPERS,
	CLAN_WOLF,
	COMBINE_PEACEKEEPERS,
	COMSTAR,
	DISPUTED_CC_AND_FS,
	DISPUTED_CC_AND_FWL,
	DISPUTED_CNC_AND_CSJ,
	DISPUTED_DC_AND_LC,
	DISPUTED_FWL_AND_LC,
	DRACONIS_COMBINE,
	DUTCHY_OF_SMALL,
	ELYSIAN_FIELDS,
	FEDERATED_COMMONWEALTH,
	FEDERATED_SUNS,
	FREE_RASALHAGUE_REPUBLIC,
	FREE_WORLDS_LEAGUE,
	GREATER_VALKRATE,
	ILLYRIAN_PALATINATE,
	INNER_SPHERE_GENERAL,
	LYRAN_ALLIANCE,
	LYRAN_COMMONWEALTH,
	LOTHIAN_LEAGUE,
	MAGISTRACY_OF_CANOPUS,
	MARIAN_HEGEMONY,
	MICA_MAJORITY,
	NEW_COLONY_REGION,
	NOT_OWNED,
	OBERON_CONFEDERATION,
	OUTWORLDS_ALLIANCE,
	PERIPHERY_OTHER,
	PORT_KRIN,
	RIM_COLLECTION,
	RIM_WORLDS_REPUBLIC,
	SAIPH_TRIUMVIRATE,
	SARNA_SUPREMACY,
	ST_IVES_COMPACT,
	STAR_LEAGUE,
	STYK_COMMONALITY,
	TAURIAN_CONCORDAT,
	TERRACAP_CONFEDERATION,
	TERRAN_HEGEMONY,
	TIKONOV_FREE_REPUBLIC,
	TORTUGA_DOMINIONS,
	WORD_OF_BLAKE,
	MERCENARY;

	private static final String[] _Names = {
		"Arc Royal Defense Cordon",
		"Bandit Kingdoms",
		"Barony of Strang",
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
		"Lothian League",
		"Magistry of Canopus",
		"Marian Hegemony",
		"Mica Majority",
		"New Colony Region",
		"Not Owned",
		"Oberon Confederation",
		"Outworlds Alliance",
		"Periphery (Other)",
		"Port Krin",
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
		"Word of Blake",
		"Mercenary"
	};
	
	private static final String[] _Abbreviations = {
		"AR",
		"BK",
		"BS",
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
		"LL",
		"MoC",
		"MH",
		"MM",
		"NCR",
		"N/A",
		"OC",
		"OA",
		"P",
		"PC",
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
		"WOB",
		"MERC"
	};

	private static final String[] _Colours = {
		"0x993267",
		"0x888888",
		"0x888888",
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
		"0xFEFF01",
		"0x888888",
		"0x888888",
		"0xFEFF01",
		"0x535353"
	};
	
	public static Faction fromAlphaStrikeID(int id) throws Exception
	{
		switch (id)
		{
			case 5: return Faction.CAPELLAN_CONFEDERATION;
			case 18: return Faction.COMSTAR;
			case 27: return Faction.DRACONIS_COMBINE;
			case 84: return Faction.FEDERATED_COMMONWEALTH;
			case 29: return Faction.FEDERATED_SUNS;
			case 28: return Faction.FREE_RASALHAGUE_REPUBLIC;
			case 30: 
			case 59: 
			case 74: 
			case 67: 
			case 72: 
			case 76: 
			case 75: return Faction.FREE_WORLDS_LEAGUE;
			case 55: return Faction.NOT_OWNED;
			case 32: return Faction.LYRAN_ALLIANCE;
			case 60: return Faction.LYRAN_COMMONWEALTH;
			case 83: return Faction.ST_IVES_COMPACT;
			case 87: return Faction.TERRAN_HEGEMONY;
			case 48: return Faction.WORD_OF_BLAKE;
			
			case 9: return Faction.CIRCINUS_FEDERATION;
			case 33: return Faction.MAGISTRACY_OF_CANOPUS;
			case 35: return Faction.MARIAN_HEGEMONY;
			case 36: return Faction.OUTWORLDS_ALLIANCE;
			case 57: return Faction.PERIPHERY_OTHER;
			case 42: 
			case 88: return Faction.RIM_WORLDS_REPUBLIC;
			case 47: return Faction.TAURIAN_CONCORDAT;
			
			case 34: return Faction.MERCENARY;
		}
		
		throw new Exception("Unable to find faction from AlphaStrike faction ID (" + id + ")");
	}
	
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

	public static Faction fromString(String string) throws Exception
	{
		for (Faction f : values() )
			if (_Names[f.ordinal()].equalsIgnoreCase(string))
				return f;
		
		throw new Exception("Unable to get Faction from string (" + string + ")");
	}	
}
