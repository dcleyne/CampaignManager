package bt.elements.campaign;

public class CampaignScenarioSide
{
	public enum Participation
	{
		ATTACKER,
		DEFENDER;
		
		private static final String[] _Names = {"Attacker", "Defender"};
		
		public String toString()
		{
			return _Names[ordinal()];
		}
		
		public static Participation fromString(String string) throws Exception
		{
			for (int i = 0; i < _Names.length; i++)
			{
				if (_Names[i].equalsIgnoreCase(string))
					return values()[i];
			}
			throw new Exception("Unable to determine Participation from string: " + string);
		}
	}

	private String _SideName;
	private Participation _Participation;
	private String _ForceName;
	private String _UnitName;
	
	public String getSideName()
	{
		return _SideName;
	}
	public void setSideName(String sideName)
	{
		_SideName = sideName;
	}
	public Participation getParticipation()
	{
		return _Participation;
	}
	public void setParticipation(Participation participation)
	{
		_Participation = participation;
	}
	public String getForceName()
	{
		return _ForceName;
	}
	public void setForceName(String forceName)
	{
		_ForceName = forceName;
	}
	public String getUnitName()
	{
		return _UnitName;
	}
	public void setUnitName(String unitName)
	{
		_UnitName = unitName;
	}
}
