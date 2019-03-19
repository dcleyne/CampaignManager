package bt.elements.campaign;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import bt.mapping.campaign.CampaignMap;

public class Campaign
{
	private String _Name;
	private LocalDate _StartDate;
	private String _StartScenario;
	private String _Synopsis;
	private HashMap<String, Outcome> _Outcomes = new HashMap<String, Outcome>();
	private HashMap<String, Track> _Tracks = new HashMap<String, Track>();
	private HashMap<String, Side> _Sides = new HashMap<String, Side>();
	private HashMap<String, CampaignLocation> _Locations = new HashMap<String, CampaignLocation>();
	private CampaignMap _Map;
	private HashMap<String, CampaignScenario> _Scenarios = new HashMap<String, CampaignScenario>();
	
	public String getName()
	{
		return _Name;
	}
	public void setName(String name)
	{
		_Name = name;
	}
	public LocalDate getStartDate()
	{
		return _StartDate;
	}
	public void setStartDate(LocalDate startDate)
	{
		_StartDate = startDate;
	}
	public String getStartScenario()
	{
		return _StartScenario;
	}
	public void setStartScenario(String startScenario)
	{
		_StartScenario = startScenario;
	}
	public String getSynopsis()
	{
		return _Synopsis;
	}
	public void setSynopsis(String synopsis)
	{
		_Synopsis = synopsis;
	}
	
	public void addOutcome(Outcome outcome)
	{
		_Outcomes.put(outcome.getNumber(), outcome);
	}
	
	public ArrayList<Outcome> getOutcomes()
	{
		return new ArrayList<Outcome>(_Outcomes.values());
	}
	
	public Outcome getOutcome(String number)
	{
		return _Outcomes.get(number);
	}

	public void addTrack(Track track)
	{
		_Tracks.put(track.getName(), track);
	}
	
	public ArrayList<Track> getTracks()
	{
		return new ArrayList<Track>(_Tracks.values());
	}
	
	public Track getTrack(String name)
	{
		return _Tracks.get(name);
	}

	public void addSide(Side side)
	{
		_Sides.put(side.getName(), side);
	}
	
	public ArrayList<Side> getSides()
	{
		return new ArrayList<Side>(_Sides.values());
	}
	
	public Side getSide(String name)
	{
		return _Sides.get(name);
	}
	
	public Side getSideForUnit(String unitName)
	{
		for (Side s: _Sides.values())
		{
			for (Force f: s.getForces())
			{
				for (CampaignUnit u: f.getUnits())
				{
					if (u.getName().equalsIgnoreCase(unitName))
						return s;
				}
			}
		}
		return null;
	}
	
	public Force getForceForUnit(String unitName)
	{
		for (Side s: _Sides.values())
		{
			for (Force f: s.getForces())
			{
				for (CampaignUnit u: f.getUnits())
				{
					if (u.getName().equalsIgnoreCase(unitName))
						return f;
				}
			}
		}
		return null;
	}
	
	public void addLocation(CampaignLocation location)
	{
		_Locations.put(location.getNumber(), location);
	}
	
	public ArrayList<CampaignLocation> getLocations()
	{
		return new ArrayList<CampaignLocation>(_Locations.values());
	}
	
	public CampaignLocation getLocation(String number)
	{
		return _Locations.get(number);
	}
	
	public CampaignMap getMap()
	{
		return _Map;
	}
	public void setMap(CampaignMap map)
	{
		_Map = map;
	}

	public void addScenario(CampaignScenario Scenario)
	{
		_Scenarios.put(Scenario.getNumber(), Scenario);
	}
	
	public ArrayList<CampaignScenario> getScenarios()
	{
		return new ArrayList<CampaignScenario>(_Scenarios.values());
	}
	
	public CampaignScenario getScenario(String number)
	{
		return _Scenarios.get(number);
	}
	
	public Campaign()
	{	
	}
}
