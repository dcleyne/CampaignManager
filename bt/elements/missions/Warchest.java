package bt.elements.missions;

import java.util.ArrayList;

public class Warchest 
{
	private int _MissionCost;
	ArrayList<WarchestOption> _Options = new ArrayList<WarchestOption>();
	
	public int getMissionCost() 
	{
		return _MissionCost;
	}
	public void setMissionCost(int missionCost) 
	{
		_MissionCost = missionCost;
	}
	
	public void addOption(WarchestOption option)
	{
		_Options.add(option);
	}
	
	public ArrayList<WarchestOption> getOptions()
	{
		return _Options;
	}
	
	public Warchest()
	{
		
	}
	
	public Warchest(Warchest wc)
	{
		_MissionCost = wc._MissionCost;
		for (WarchestOption option: wc._Options)
		{
			_Options.add(new WarchestOption(option));
		}
	}
}
