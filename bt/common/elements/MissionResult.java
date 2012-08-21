package bt.common.elements;

public class MissionResult
{
    private String _MissionName;
    private int _PlayerStartBV;
    private int _PlayerEndBV;
    private int _OpponentStartBV;
    private int _OpponentEndBV;
    private int _Result;
	public String getMissionName()
	{
		return _MissionName;
	}
	public void setMissionName(String missionName)
	{
		_MissionName = missionName;
	}
	public int getPlayerStartBV()
	{
		return _PlayerStartBV;
	}
	public void setPlayerStartBV(int playerStartBV)
	{
		_PlayerStartBV = playerStartBV;
	}
	public int getPlayerEndBV()
	{
		return _PlayerEndBV;
	}
	public void setPlayerEndBV(int playerEndBV)
	{
		_PlayerEndBV = playerEndBV;
	}
	public int getOpponentStartBV()
	{
		return _OpponentStartBV;
	}
	public void setOpponentStartBV(int opponentStartBV)
	{
		_OpponentStartBV = opponentStartBV;
	}
	public int getOpponentEndBV()
	{
		return _OpponentEndBV;
	}
	public void setOpponentEndBV(int opponentEndBV)
	{
		_OpponentEndBV = opponentEndBV;
	}
	public int getResult()
	{
		return _Result;
	}
	public void setResult(int result)
	{
		_Result = result;
	}
    
}
