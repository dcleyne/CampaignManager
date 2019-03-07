package bt.elements.scenario;

import bt.elements.missions.Mission;
import bt.elements.unit.Force;
import bt.elements.unit.TechRating;
import bt.mapping.MapSet;

public class Scenario
{
	private Mission _Mission;

	private MapSet _MapSet;
	private Force _Attacker;
	private Force _Defender;

	private String _PreviousSuccessfulMission;
	private TechRating _OpponentTechRating;
	private float _OpponentForceRatio;
	private String _OpponentUnitComposition;
	private String _OpponentRandomAllocation;
	private String _OpponentSkillLevel;

	public Mission getMission()
	{
		return _Mission;
	}

	public void setMission(Mission mission)
	{
		_Mission = mission;
	}

	public MapSet getMapSet()
	{
		return _MapSet;
	}

	public void setMapSet(MapSet mapSet)
	{
		_MapSet = mapSet;
	}

	public Force getAttacker()
	{
		return _Attacker;
	}
	
	public void setAttacker(Force f)
	{
		_Attacker = f;
	}

	public Force getDefender()
	{
		return _Defender;
	}
	
	public void setDefender(Force f)
	{
		_Defender = f;
	}

	public String getPreviousSuccessfulMission()
	{
		return _PreviousSuccessfulMission;
	}

	public void setPreviousSuccessfulMission(String previousSuccessfulMission)
	{
		_PreviousSuccessfulMission = previousSuccessfulMission;
	}

	public TechRating getOpponentTechRating()
	{
		return _OpponentTechRating;
	}

	public void setOpponentTechRating(TechRating opponentTechRating)
	{
		_OpponentTechRating = opponentTechRating;
	}

	public float getOpponentForceRatio()
	{
		return _OpponentForceRatio;
	}

	public void setOpponentForceRatio(float opponentForceRatio)
	{
		_OpponentForceRatio = opponentForceRatio;
	}

	public String getOpponentUnitComposition()
	{
		return _OpponentUnitComposition;
	}

	public void setOpponentUnitComposition(String opponentUnitComposition)
	{
		_OpponentUnitComposition = opponentUnitComposition;
	}

	public String getOpponentRandomAllocation()
	{
		return _OpponentRandomAllocation;
	}

	public void setOpponentRandomAllocation(String opponentRandomAllocation)
	{
		_OpponentRandomAllocation = opponentRandomAllocation;
	}

	public String getOpponentSkillLevel()
	{
		return _OpponentSkillLevel;
	}

	public void setOpponentSkillLevel(String opponentSkillLevel)
	{
		_OpponentSkillLevel = opponentSkillLevel;
	}

	public void generateOpponentValues()
	{
		_OpponentTechRating = _Mission.getOpponentBriefing().getTechRating(_PreviousSuccessfulMission);
		_OpponentForceRatio = _Mission.getOpponentBriefing().getForceRatio(_PreviousSuccessfulMission);
		_OpponentUnitComposition = _Mission.getOpponentBriefing().getUnitComposition(_PreviousSuccessfulMission);
		_OpponentRandomAllocation = _Mission.getOpponentBriefing().getRandomAllocation(_PreviousSuccessfulMission);
		_OpponentSkillLevel = _Mission.getOpponentBriefing().getSkillLevel(_PreviousSuccessfulMission);
	}

}
