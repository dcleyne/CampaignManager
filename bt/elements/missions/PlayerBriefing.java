package bt.elements.missions;

public class PlayerBriefing
{
    private String _Team;
    private String _Briefing;
    private String _Setup;
    private String _Objective;
    private String _VictoryConditions;
    private String _SpecialConditions;
    private String _Notes;
    
	public String getTeam()
	{
		return _Team;
	}
	public void setTeam(String team)
	{
		_Team = team;
	}
	public String getBriefing()
	{
		return _Briefing;
	}
	public void setBriefing(String briefing)
	{
		_Briefing = briefing;
	}
	public String getSetup()
	{
		return _Setup;
	}
	public void setSetup(String setup)
	{
		_Setup = setup;
	}
	public String getObjective()
	{
		return _Objective;
	}
	public void setObjective(String objective)
	{
		_Objective = objective;
	}
	public String getVictoryConditions()
	{
		return _VictoryConditions;
	}
	public void setVictoryConditions(String victoryConditions)
	{
		_VictoryConditions = victoryConditions;
	}
	public String getSpecialConditions()
	{
		return _SpecialConditions;
	}
	public void setSpecialConditions(String specialConditions)
	{
		_SpecialConditions = specialConditions;
	}
	public String getNotes()
	{
		return _Notes;
	}
	public void setNotes(String notes)
	{
		_Notes = notes;
	}
	
	public PlayerBriefing()
	{
	}
	
	public PlayerBriefing(PlayerBriefing briefing)
	{
	    this._Team = briefing._Team;
	    this._Briefing = briefing._Briefing;
	    this._Setup = briefing._Setup;
	    this._Objective = briefing._Objective;
	    this._VictoryConditions = briefing._VictoryConditions;
	    this._SpecialConditions = briefing._SpecialConditions;
	    this._Notes = briefing._Notes;
	}
}
