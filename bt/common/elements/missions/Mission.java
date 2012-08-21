package bt.common.elements.missions;

import java.util.Vector;

public class Mission
{
	private long _ID;
	private String _Name;
	private String _Description;
	private MissionMapSet _MapSet;
	private float _ForceRatio = 1.0f;
	private String _Notes;
	
	private Vector<PlayerBriefing> _Briefings = new Vector<PlayerBriefing>();

	public long getID()
	{
		return _ID;
	}

	public void setID(long iD)
	{
		_ID = iD;
	}

	public String getName()
	{
		return _Name;
	}

	public void setName(String name)
	{
		_Name = name;
	}

	public String getDescription()
	{
		return _Description;
	}

	public void setDescription(String description)
	{
		_Description = description;
	}

	public MissionMapSet getMapSet()
	{
		return _MapSet;
	}

	public void setMapSet(MissionMapSet mapSet)
	{
		_MapSet = mapSet;
	}

	public float getForceRatio()
	{
		return _ForceRatio;
	}

	public void setForceRatio(float forceRatio)
	{
		_ForceRatio = forceRatio;
	}

	public String getNotes()
	{
		return _Notes;
	}

	public void setNotes(String notes)
	{
		_Notes = notes;
	}

	public Vector<PlayerBriefing> getBriefings()
	{
		return _Briefings;
	}

	public PlayerBriefing getBriefing(String teamName)
	{
		for (PlayerBriefing briefing: _Briefings)
			if (briefing.getTeam().equalsIgnoreCase(teamName))
				return briefing;
		
		return null;
	}
	
	public Mission()
	{
		
	}
	
	public Mission(Mission mission)
	{
		this._ID = mission._ID;
		this._Name = mission._Name;
		this._Description = mission._Description;
		this._MapSet = mission._MapSet;
		this._ForceRatio = mission._ForceRatio;
		this._Notes = mission._Notes;
		
		for (PlayerBriefing briefing : mission._Briefings)
		{
			_Briefings.add(new PlayerBriefing(briefing));
		}
		
	}
	
}
