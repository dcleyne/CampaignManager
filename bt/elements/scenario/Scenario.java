package bt.elements.scenario;

import java.util.HashMap;
import java.util.Vector;

import bt.elements.mapping.MapSet;
import bt.elements.missions.Mission;
import bt.elements.unit.TechRating;
import bt.elements.unit.Unit;

public class Scenario
{
	private Mission _Mission;

	private Season _Season;
	private Wind _Wind;
	private TimeOfDay _TimeOfDay;
	private MapSet _MapSet;
	private Vector<String> _WeatherConditions = new Vector<String>();
	private Vector<String> _TerrainConditions = new Vector<String>();
	private HashMap<String, Unit> _Sides = new HashMap<String, Unit>();

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

	public Season getSeason()
	{
		return _Season;
	}

	public void setSeason(Season season)
	{
		_Season = season;
	}

	public Wind getWind()
	{
		return _Wind;
	}

	public void setWind(Wind wind)
	{
		_Wind = wind;
	}

	public TimeOfDay getTimeOfDay()
	{
		return _TimeOfDay;
	}

	public void setTimeOfDay(TimeOfDay timeOfDay)
	{
		_TimeOfDay = timeOfDay;
	}

	public MapSet getMapSet()
	{
		return _MapSet;
	}

	public void setMapSet(MapSet mapSet)
	{
		_MapSet = mapSet;
	}

	public Vector<String> getWeatherConditions()
	{
		return _WeatherConditions;
	}

	public Vector<String> getTerrainConditions()
	{
		return _TerrainConditions;
	}

	public HashMap<String, Unit> getSides()
	{
		return _Sides;
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
