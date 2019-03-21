package bt.elements.campaign;

public class CampaignScenarioOutcome
{
	private String _Winner;
	private String _NextScenario;
	private String _CampaignResult;
	
	public String getWinner()
	{
		return _Winner;
	}
	public void setWinner(String winner)
	{
		_Winner = winner;
	}
	public String getNextScenario()
	{
		return _NextScenario;
	}
	public void setNextScenario(String nextScenario)
	{
		_NextScenario = nextScenario;
	}
	public String getCampaignResult()
	{
		return _CampaignResult;
	}
	public void setCampaignResult(String campaignOutcome)
	{
		_CampaignResult = campaignOutcome;
	}
	
}
