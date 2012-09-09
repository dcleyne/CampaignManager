package bt.elements;

import java.util.HashMap;

public class BattlemechRepairReport
{
	private String _ID;
	private String _Name;
	private String _DesignName;
	private String _DesignVariant;
	private int _ModifiedSkillTarget;
	
	private HashMap<String, ItemRepairDetail> _InternalRepairDetails = new HashMap<String, ItemRepairDetail>();
	private HashMap<String, ItemRepairDetail> _ArmourRepairDetails = new HashMap<String, ItemRepairDetail>();
	
	private int _Indent = 0;
	
	public String getID()
	{
		return _ID;
	}
	
	public String getName()
	{
		return _Name;
	}
	
	public String getDesignName()
	{
		return _DesignName;
	}
	
	public String getDesignVariant()
	{
		return _DesignVariant;
	}
	
	public BattlemechRepairReport(Battlemech mech, int modifiedSkillTarget)
	{
		_ID = mech.getIdentifier();
		_Name = mech.getName();
		_DesignName = mech.getDesignName();
		_DesignVariant = mech.getDesignVariant();
		_ModifiedSkillTarget = modifiedSkillTarget;
	}
	
	public void addInternalRepairDetail(String section, ItemRepairDetail ird)
	{
		_InternalRepairDetails.put(section,  ird);
	}
	
	public void addArmourRepairDetail(String section, ItemRepairDetail ird)
	{
		_ArmourRepairDetails.put(section, ird);
	}
	
	private int getIndentLength()
	{
		return _Indent * 4;
	}
	
	private String indent()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getIndentLength(); i++)
			sb.append(" ");
		
		return sb.toString();
	}
	
	private String indent(int indentLevel)
	{
		_Indent += indentLevel;
		if (_Indent < 0)
			_Indent = 0;
		
		return indent();
	}
	
	private String padToColumn(int length, int column)
	{
		StringBuilder sb = new StringBuilder();
		
		while (sb.length() + length < column)
		{
			sb.append(" ");
		}
		
		return sb.toString();
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder("Battlemech Repair Report");
		sb.append(System.lineSeparator());
		sb.append("------------------------");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append(_Name + "(" + _ID + ") - " + _DesignVariant + " " + _DesignName);
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());

		sb.append("Replacements" + System.lineSeparator());
		sb.append("------------" + System.lineSeparator());
		sb.append(System.lineSeparator());
		
		if (_ArmourRepairDetails.size() > 0)
		{
			indent(1);
	
			sb.append("Armour" + System.lineSeparator());
			sb.append("------" + System.lineSeparator());
			sb.append(indent(1) + "Location:           Time:   Cost:       Target#:  Partial Repair:   Partial Repair Effect: ");
			sb.append(System.lineSeparator());
			for (String location : _ArmourRepairDetails.keySet())
			{
				ItemRepairDetail ird = _ArmourRepairDetails.get(location);
				StringBuilder sb1 = new StringBuilder();
				sb1.append(indent());
				sb1.append(location);
				sb1.append(padToColumn(sb1.length(), getIndentLength() + 20));
				sb1.append(Integer.toString(ird.getTime()));
				sb1.append(padToColumn(sb1.length(), getIndentLength() + 28));
				sb1.append(Integer.toString(ird.getCost()));
				sb1.append(padToColumn(sb1.length(), getIndentLength() + 40));
				sb1.append(Integer.toString(_ModifiedSkillTarget + ird.getSkillModifier()));
				sb1.append(padToColumn(sb1.length(), getIndentLength() + 50));
				if (ird.getPartialRepair() > Integer.MIN_VALUE)
				{
					sb1.append(Integer.toString(ird.getPartialRepair()));				
				}
				sb1.append(padToColumn(sb.length(), getIndentLength() + 68));
				sb1.append(ird.getPartialRepairEffect());
				sb1.append(System.lineSeparator());	
				
				sb.append(sb1.toString());
			}
			indent(-1);
			indent(-1);
		}
		
		return sb.toString();
	}
}
