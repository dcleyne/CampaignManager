package bt.elements.missions;

import java.util.ArrayList;

public class PreviousSuccessfulPlayerMission 
{
	public enum ListTreatment
	{
		INCLUDES,
		EXCLUDES
	}
	
	private ListTreatment _ListTreatment = ListTreatment.INCLUDES;
	private ArrayList<String> _MissionList = new ArrayList<String>();
	private int _Modifier = 0;
	
	public void setInclusive()
	{
		_ListTreatment = ListTreatment.INCLUDES;
	}
	
	public void setExclusive()
	{
		_ListTreatment = ListTreatment.EXCLUDES;
	}
	
	public void addMission(String mission)
	{
		if (!_MissionList.contains(mission))
			_MissionList.add(mission);
	}
	
	public int getModifier()
	{
		return _Modifier;
	}
	
	public String getList()
	{
		StringBuilder sb = new StringBuilder();
		for (String mission: _MissionList)
		{
			if (sb.length() > 0)
				sb.append(",");
			
			sb.append(mission);
		}
		return sb.toString();
	}
	
	public boolean isInclusive()
	{
		return _ListTreatment == ListTreatment.INCLUDES;
	}

	public int getModifier(String previousMission)
	{
		if (isInclusive())
			return _MissionList.contains(previousMission) ? _Modifier : 0;
		else
			return _MissionList.contains(previousMission) ? 0 : _Modifier;			
	}
	
	public void setModifier(int modifier)
	{
		_Modifier = modifier;
	}
	
	public PreviousSuccessfulPlayerMission()
	{
		
	}
	
	public PreviousSuccessfulPlayerMission(PreviousSuccessfulPlayerMission pspm)
	{
		_ListTreatment = pspm._ListTreatment;
		_MissionList = new ArrayList<String>(pspm._MissionList);
		_Modifier = pspm._Modifier;
	}
}
