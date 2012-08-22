package bt.elements.personnel;

public class Mechwarrior extends Personnel
{
    private int _GunnerySkill;
    private int _PilotingSkill;
    private int _Hits;

    private int _SuccessfulPilotingRolls;
    private int _FailedPilotingRolls;
    private int _SuccessfulFiringAttempts;
    private int _FailedFiringAttempts; //This is per ROUND not per weapon.
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

	public int getSuccessfulPilotingRolls()
	{
		return _SuccessfulPilotingRolls;
	}

	public void setSuccessfulPilotingRolls(int successfulPilotingRolls)
	{
		_SuccessfulPilotingRolls = successfulPilotingRolls;
	}

	public int getFailedPilotingRolls()
	{
		return _FailedPilotingRolls;
	}

	public void setFailedPilotingRolls(int failedPilotingRolls)
	{
		_FailedPilotingRolls = failedPilotingRolls;
	}

	public int getSuccessfulFiringAttempts()
	{
		return _SuccessfulFiringAttempts;
	}

	public void setSuccessfulFiringAttempts(int successfulFiringAttempts)
	{
		_SuccessfulFiringAttempts = successfulFiringAttempts;
	}

	public int getFailedFiringAttempts()
	{
		return _FailedFiringAttempts;
	}

	public void setFailedFiringAttempts(int failedFiringAttempts)
	{
		_FailedFiringAttempts = failedFiringAttempts;
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
