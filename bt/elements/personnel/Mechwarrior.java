package bt.elements.personnel;

public class Mechwarrior extends Personnel
{
    private int _GunnerySkill;
    private int _PilotingSkill;
    private int _Hits;
    private int _Missions;
    
	public int getGunnerySkill()
	{
		return _GunnerySkill;
	}

	public void setGunnerySkill(int gunnerySkill)
	{
		_GunnerySkill = gunnerySkill;
	}

	public int getPilotingSkill()
	{
		return _PilotingSkill;
	}

	public void setPilotingSkill(int pilotingSkill)
	{
		_PilotingSkill = pilotingSkill;
	}

	public int getHits()
	{
		return _Hits;
	}

	public void setHits(int hits)
	{
		_Hits = hits;
	}

	public int getMissions()
	{
		return _Missions;
	}

	public void setMissions(int missions)
	{
		_Missions = missions;
	}

	@Override
	public JobType getJobType()
	{
		return JobType.MECHWARRIOR;
	}

}
