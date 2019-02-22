package bt.elements.missions;

public class PlayerBriefing
{
    private String _Team;
    private String _Briefing;
    private float _ForceRestriction;
    
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
	
	public float getForceRestriction()
	{
		return _ForceRestriction;
	}
	public void setForceRestriction(float forceRestriction)
	{
		_ForceRestriction = forceRestriction;
	}
	public PlayerBriefing()
	{
	}
	
	public PlayerBriefing(PlayerBriefing briefing)
	{
	    _Team = briefing._Team;
	    _Briefing = briefing._Briefing;
	    _ForceRestriction = briefing._ForceRestriction;
	}
}
