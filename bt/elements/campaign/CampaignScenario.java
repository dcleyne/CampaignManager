package bt.elements.campaign;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class CampaignScenario
{
	
	public enum OutcomeType
	{
		NEXT_SCENARIO,
		CAMPAIGN_RESULT;
		
		private static final String[] _Names = {"NextScenario", "CampaignResult"};
		
		public static OutcomeType fromString(String string) throws Exception
		{
			for (int i = 0; i < _Names.length; i++)
			{
				if (_Names[i].equalsIgnoreCase(string))
					return values()[i];
			}
			throw new Exception("Unable to determine OutcomeType from String: " + string);
		}
	}
	
	private String _Number;
	private String _Map;
	private OutcomeType _OutcomeType;
	private String _Track;
	private LocalDate _DateTime;
	private String _Synopsis;
	private Situation _Situation;
	private HashMap<String, CampaignScenarioSide> _Sides = new HashMap<String, CampaignScenarioSide>();
	private HashMap<String, CampaignScenarioOutcome> _Outcomes = new HashMap<String, CampaignScenarioOutcome>();
	
	public String getNumber()
	{
		return _Number;
	}
	public void setNumber(String number)
	{
		_Number = number;
	}
	public String getMap()
	{
		return _Map;
	}
	public void setMap(String map)
	{
		_Map = map;
	}
	public OutcomeType getOutcomeType()
	{
		return _OutcomeType;
	}
	public void setOutcomeType(OutcomeType outcomeType)
	{
		_OutcomeType = outcomeType;
	}
	public String getTrack()
	{
		return _Track;
	}
	public void setTrack(String track)
	{
		_Track = track;
	}
	public LocalDate getDateTime()
	{
		return _DateTime;
	}
	public void setDateTime(LocalDate dateTime)
	{
		_DateTime = dateTime;
	}
	public String getSynopsis()
	{
		return _Synopsis;
	}
	public void setSynopsis(String synopsis)
	{
		_Synopsis = synopsis;
	}
	public Situation getSituation()
	{
		return _Situation;
	}
	public void setSituation(Situation situation)
	{
		_Situation = situation;
	}
	
	public void addSide(CampaignScenarioSide side)
	{
		_Sides.put(side.getSideName(), side);
	}
	
	public ArrayList<String> getSides()
	{
		return new ArrayList<String>(_Sides.keySet());
	}
	
	public CampaignScenarioSide getSide(String name)
	{
		return _Sides.get(name);
	}

	public void addOutcome(CampaignScenarioOutcome Outcome)
	{
		_Outcomes.put(Outcome.getWinner(), Outcome);
	}
	
	public ArrayList<String> getOutcomes()
	{
		return new ArrayList<String>(_Outcomes.keySet());
	}
	
	public CampaignScenarioOutcome getOutcome(String sideName)
	{
		return _Outcomes.get(sideName);
	}

}
