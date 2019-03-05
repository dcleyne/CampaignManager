package bt.elements.unit;

import java.time.LocalDate;

import bt.util.SwingHelper;

public class CompletedMission 
{
	public enum Result
	{
		WIN,
		LOSS,
		DRAW;
		
		public static String[] _Names = {"Win", "Loss", "Draw"};
		
		public String toString()
		{
			return _Names[ordinal()];
		}
		
		public static Result fromString(String str)
		{
			for (int i = 0; i < _Names.length; i++)
				if (_Names[i].equalsIgnoreCase(str))
					return values()[i];
			
			return null;
		}
	}

	private long _MissionIdentifier;
	private String _MissionTitle;
	private LocalDate _MissionDate;
	private Result _Result;
	private int _WarchestPoints;
	
	public long getMissionIdentifier() {
		return _MissionIdentifier;
	}
	public void setMissionIdentifier(long missionIdentifier) {
		_MissionIdentifier = missionIdentifier;
	}
	public String getMissionTitle() {
		return _MissionTitle;
	}
	public void setMissionTitle(String missionTitle) {
		_MissionTitle = missionTitle;
	}
	public LocalDate getMissionDate()
	{
		return _MissionDate;
	}
	public void setMissionDate(LocalDate missionDate)
	{
		_MissionDate = missionDate;
	}
	public Result getResult() {
		return _Result;
	}
	public void setResult(Result result) {
		_Result = result;
	}
	public int getWarchestPoints() {
		return _WarchestPoints;
	}
	public void setWarchestPoints(int warchestPoints) {
		_WarchestPoints = warchestPoints;
	}
	
	public CompletedMission()
	{
	}

	public CompletedMission(long missionIdentifier, String missionTitle, Result result, int warchestPoints, LocalDate missionDate)
	{
		_MissionIdentifier = missionIdentifier;
		_MissionTitle = missionTitle;
		_Result = result;
		_WarchestPoints = warchestPoints;
		_MissionDate = missionDate;
	}
	
	public String toString()
	{
		return _MissionTitle + " (" + Long.toString(_MissionIdentifier) + ") " + _Result.toString() + " - " + SwingHelper.FormatDate(_MissionDate);
	}
}
