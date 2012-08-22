package bt.elements.scenario;

import java.util.HashMap;
import java.util.Vector;


import bt.elements.mapping.MapSet;
import bt.elements.missions.Mission;
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
}
