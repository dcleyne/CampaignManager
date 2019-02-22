package bt.elements.missions;

import java.util.ArrayList;

public class Mission
{
	private long _ID;
	private String _Name;
	private String _Description;
	private String _GameSetup;
	private String _MapSetTerrain;
	private PlayerBriefing _PlayerBriefing;
	private OpponentBriefing _OpponentBriefing;
	private Warchest _Warchest;
	private ArrayList<Objective> _Objectives = new ArrayList<Objective>();
	private ArrayList<SpecialRule> _SpecialRules = new ArrayList<SpecialRule>();
	private String _Notes;
	
	public long getID()
	{
		return _ID;
	}

	public void setID(long iD)
	{
		_ID = iD;
	}

	public String getName()
	{
		return _Name;
	}

	public void setName(String name)
	{
		_Name = name;
	}

	public String getDescription()
	{
		return _Description;
	}

	public void setDescription(String description)
	{
		_Description = description;
	}

	public String getGameSetup() 
	{
		return _GameSetup;
	}

	public void setGameSetup(String gameSetup) 
	{
		_GameSetup = gameSetup;
	}

	public String getMapSetTerrain()
	{
		return _MapSetTerrain;
	}

	public void setMapSetTerrain(String mapSetTerrain)
	{
		_MapSetTerrain = mapSetTerrain;
	}

	public String getNotes()
	{
		return _Notes;
	}

	public void setNotes(String notes)
	{
		_Notes = notes;
	}

	public PlayerBriefing getPlayerBriefing()
	{
		return _PlayerBriefing;
	}

	public void setPlayerBriefing(PlayerBriefing playerBriefing) 
	{
		_PlayerBriefing = playerBriefing;
	}

	public OpponentBriefing getOpponentBriefing() 
	{
		return _OpponentBriefing;
	}

	public void setOpponentBriefing(OpponentBriefing opponentBriefing) 
	{
		_OpponentBriefing = opponentBriefing;
	}
	
	public Warchest getWarchest() 
	{
		return _Warchest;
	}

	public void setWarchest(Warchest warchest) 
	{
		_Warchest = warchest;
	}

	public void addObjective(Objective o)
	{
		_Objectives.add(o);
	}
	
	public ArrayList<Objective> getObjectives()
	{
		return _Objectives;
	}
	
	public void addSpecialRule(SpecialRule sr)
	{
		_SpecialRules.add(sr);
	}
	
	public ArrayList<SpecialRule> getSpecialRules()
	{
		return _SpecialRules;
	}

	public Mission()
	{
		
	}
	
	public Mission(Mission mission)
	{
		_ID = mission._ID;
		_Name = mission._Name;
		_Description = mission._Description;
		_MapSetTerrain = mission._MapSetTerrain;
		_PlayerBriefing = new PlayerBriefing(mission._PlayerBriefing);
		_OpponentBriefing = new OpponentBriefing(mission._OpponentBriefing);
		_Warchest = new Warchest(mission._Warchest); 
		for (Objective o : mission._Objectives)
		{
			_Objectives.add(new Objective(o));
		}
		for (SpecialRule sr: mission._SpecialRules)
		{
			_SpecialRules.add(new SpecialRule(sr));
		}
		_Notes = mission._Notes;
		
	}
	
}
