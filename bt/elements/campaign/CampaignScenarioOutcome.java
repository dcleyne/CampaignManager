package bt.elements.campaign;

public class CampaignScenarioOutcome
{
	private String _Winner;
	private String _NextScenario;
	private String _CampaignOutcome;
	
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
	public String getCampaignOutcome()
	{
		return _CampaignOutcome;
	}
	public void setCampaignOutcome(String campaignOutcome)
	{
		_CampaignOutcome = campaignOutcome;
	}
	
}
