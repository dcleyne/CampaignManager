package bt.elements.missions;

public class PlayerBriefing
{
    private String _Team;
    private String _Briefing;
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
	    _Team = briefing._Team;
	    _Briefing = briefing._Briefing;
	    _Notes = briefing._Notes;
	}
}
