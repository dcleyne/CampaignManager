package bt.elements;

import java.util.HashMap;
import java.util.Vector;

public class BattlemechRepairReport
{
	private String _ID;
	private String _Name;
	private String _DesignName;
	private String _DesignVariant;
	private int _ModifiedSkillTarget;

	private Vector<ItemRepairDetail> _SectionReplacementDetails = new Vector<ItemRepairDetail>();
	private HashMap<String, ItemRepairDetail> _ArmourReplacementDetails = new HashMap<String, ItemRepairDetail>();
	private Vector<ItemRepairDetail> _ItemReplacementDetails = new Vector<ItemRepairDetail>();

	private HashMap<String, ItemRepairDetail> _InternalRepairDetails = new HashMap<String, ItemRepairDetail>();
	private Vector<ItemRepairDetail> _SectionRepairDetails = new Vector<ItemRepairDetail>();
	private Vector<ItemRepairDetail> _ItemRepairDetails = new Vector<ItemRepairDetail>();
	
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
	
	public void addSectionReplacementDetail(ItemRepairDetail ird)
	{
		_SectionReplacementDetails.add(ird);
	}
	
	public void addArmourReplacementDetail(String section, ItemRepairDetail ird)
	{
		_ArmourReplacementDetails.put(section, ird);
	}
	
	public void addItemReplacementDetail(ItemRepairDetail ird)
	{
		_ItemReplacementDetails.add(ird);
	}
	
	public void addInternalRepairDetail(String section, ItemRepairDetail ird)
	{
		_InternalRepairDetails.put(section,  ird);
	}
	
	public void addSectionRepairDetail(ItemRepairDetail ird)
	{
		_SectionRepairDetails.add(ird);
	}
	
	public void addItemRepairDetail(ItemRepairDetail ird)
	{
		_ItemRepairDetails.add(ird);
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
	
	private String buildItemRepairDetailLine(ItemRepairDetail ird, String detailStr, int col1, int col2, int col3, int col4, int col5)
	{
		StringBuilder sb1 = new StringBuilder();
		sb1.append(indent());
		sb1.append(detailStr);
		sb1.append(padToColumn(sb1.length(), getIndentLength() + col1));
		sb1.append(Integer.toString(ird.getTime()));
		sb1.append(padToColumn(sb1.length(), getIndentLength() + col2));
		sb1.append(Double.toString(ird.getCost()));
		sb1.append(padToColumn(sb1.length(), getIndentLength() + col3));
		sb1.append(Integer.toString(_ModifiedSkillTarget + ird.getSkillModifier()));
		sb1.append(padToColumn(sb1.length(), getIndentLength() + col4));
		if (ird.getPartialRepair() > Integer.MIN_VALUE)
		{
			sb1.append(Integer.toString(_ModifiedSkillTarget + ird.getSkillModifier() - ird.getPartialRepair()));				
		}
		sb1.append(padToColumn(sb1.length(), getIndentLength() + col5));
		sb1.append(ird.getPartialRepairEffect());
		sb1.append(System.lineSeparator());
		
		return sb1.toString();
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
		sb.append("============" + System.lineSeparator());
		sb.append(System.lineSeparator());
		
		if (_SectionReplacementDetails.size() > 0)
		{
			indent(1);
			
			sb.append("Section" + System.lineSeparator());
			sb.append("-------" + System.lineSeparator());
			sb.append(indent(1) + "Section Damage:               Time:   Cost:       Target#:  Partial Repair:   Partial Repair Effect: ");
			sb.append(System.lineSeparator());
			for (ItemRepairDetail ird : _SectionReplacementDetails)
			{
				sb.append(buildItemRepairDetailLine(ird,ird.getItemType(),30,38,50,60,78));
			}
			indent(-1);
			indent(-1);
		}

		if (_ArmourReplacementDetails.size() > 0)
		{
			indent(1);
	
			sb.append("Armour" + System.lineSeparator());
			sb.append("------" + System.lineSeparator());
			sb.append(indent(1) + "Location:           Time:   Cost:       Target#:  Partial Repair:   Partial Repair Effect: ");
			sb.append(System.lineSeparator());
			for (String location : _ArmourReplacementDetails.keySet())
			{
				ItemRepairDetail ird = _ArmourReplacementDetails.get(location);
				sb.append(buildItemRepairDetailLine(ird,location,20,28,40,50,68));
			}
			indent(-1);
			indent(-1);
		}
		
		if (_ItemReplacementDetails.size() > 0)
		{
			indent(1);
			
			sb.append("Items" + System.lineSeparator());
			sb.append("-----" + System.lineSeparator());
			sb.append(indent(1) + "Mounted Item Type:            Time:   Cost:       Target#:  Partial Repair:   Partial Repair Effect: ");
			sb.append(System.lineSeparator());
			for (ItemRepairDetail ird : _ItemReplacementDetails)
			{
				sb.append(buildItemRepairDetailLine(ird,ird.getItemType(),30,38,50,60,78));
			}
			indent(-1);
			indent(-1);
		}


		sb.append("Repairs" + System.lineSeparator());
		sb.append("=======" + System.lineSeparator());
		sb.append(System.lineSeparator());
		
		if (_InternalRepairDetails.size() > 0)
		{
			indent(1);
			
			sb.append("Internal Structure" + System.lineSeparator());
			sb.append("------------------" + System.lineSeparator());
			sb.append(indent(1) + "Location:           Time:   Cost:       Target#:  Partial Repair:   Partial Repair Effect: ");
			sb.append(System.lineSeparator());
			for (String location : _InternalRepairDetails.keySet())
			{
				ItemRepairDetail ird = _InternalRepairDetails.get(location);
				sb.append(buildItemRepairDetailLine(ird,location,20,28,40,50,68));
			}
			indent(-1);
			indent(-1);
		}

		if (_SectionRepairDetails.size() > 0)
		{
			indent(1);
			
			sb.append("Section" + System.lineSeparator());
			sb.append("-------" + System.lineSeparator());
			sb.append(indent(1) + "Section Damage:               Time:   Cost:       Target#:  Partial Repair:   Partial Repair Effect: ");
			sb.append(System.lineSeparator());
			for (ItemRepairDetail ird : _SectionRepairDetails)
			{
				sb.append(buildItemRepairDetailLine(ird,ird.getItemType(),30,38,50,60,78));
			}
			indent(-1);
			indent(-1);
		}

		if (_ItemRepairDetails.size() > 0)
		{
			indent(1);
			
			sb.append("Items" + System.lineSeparator());
			sb.append("-----" + System.lineSeparator());
			sb.append(indent(1) + "Mounted Item Type:            Time:   Cost:       Target#:  Partial Repair:   Partial Repair Effect: ");
			sb.append(System.lineSeparator());
			for (ItemRepairDetail ird : _ItemRepairDetails)
			{
				sb.append(buildItemRepairDetailLine(ird,ird.getItemType(),30,38,50,60,78));
			}
			indent(-1);
			indent(-1);
		}
		
		return sb.toString();
	}
}
