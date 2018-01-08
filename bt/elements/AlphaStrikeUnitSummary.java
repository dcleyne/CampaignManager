package bt.elements;

import java.util.Arrays;

public class AlphaStrikeUnitSummary
{
	private int _ID;
	private String _Type = "";
	private String _Name = "";
	private int _BV;
	private int[] _PV = new int[8];
	private int _Weight;
	private long _Cost;
	private String _Intro = "";
	private String _Role = "";
	private String _RulesLevel = "";
	private String _Technology = "";
	
	public int getID()
	{
		return _ID;
	}
	public void setID(int id)
	{
		_ID = id;
	}
	public String getType()
	{
		return _Type;
	}
	public void setType(String type)
	{
		_Type = type;
	}
	public String getName()
	{
		return _Name;
	}
	public void setName(String name)
	{
		_Name = name;
	}
	public int getBV()
	{
		return _BV;
	}
	public void setBV(int bv)
	{
		_BV = bv;
	}
	public int getPV(int skill)
	{
		return _PV[skill];
	}
	public int[] getPVs()
	{
		return _PV;
	}
	public void setPV(int pv, int skill)
	{
		_PV[skill] = pv;
	}
	public int getWeight()
	{
		return _Weight;
	}
	public void setWeight(int weight)
	{
		_Weight = weight;
	}
	public long getCost()
	{
		return _Cost;
	}
	public void setCost(long cost)
	{
		_Cost = cost;
	}
	public String getIntro()
	{
		return _Intro;
	}
	public void setIntro(String intro)
	{
		_Intro = intro;
	}
	public String getRole()
	{
		return _Role;
	}
	public void setRole(String role)
	{
		_Role = role;
	}
	public String getRulesLevel()
	{
		return _RulesLevel;
	}
	public void setRulesLevel(String rulesLevel)
	{
		_RulesLevel = rulesLevel;
	}
	public String getTechnology()
	{
		return _Technology;
	}
	public void setTechnology(String technology)
	{
		_Technology = technology;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(_Name);
		
		sb.append("(");
		sb.append(_ID);
		sb.append(") ");
		
		sb.append(_Role);
		sb.append(" ");
		
		sb.append(_Type);
		sb.append(" ");

		sb.append(Arrays.toString(_PV));

		return sb.toString();
	}
	
}
