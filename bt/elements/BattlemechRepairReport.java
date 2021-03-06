package bt.elements;

import java.util.ArrayList;

public class BattlemechRepairReport
{
	private String _ID;
	private String _Name;
	private String _DesignName;
	private String _DesignVariant;
	private int _ModifiedSkillTarget;

	private ArrayList<ItemRepairDetail> _SectionReplacementDetails = new ArrayList<ItemRepairDetail>();
	private ArrayList<ItemRepairDetail> _ArmourReplacementDetails = new ArrayList<ItemRepairDetail>();
	private ArrayList<ItemRepairDetail> _ItemReplacementDetails = new ArrayList<ItemRepairDetail>();

	private ArrayList<ItemRepairDetail> _InternalRepairDetails = new ArrayList<ItemRepairDetail>();
	private ArrayList<ItemRepairDetail> _SectionRepairDetails = new ArrayList<ItemRepairDetail>();
	private ArrayList<ItemRepairDetail> _ItemRepairDetails = new ArrayList<ItemRepairDetail>();
	
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
	
	public boolean hasReplacementDetails()
	{
		return hasSectionReplacementDetails() ||
				hasArmourReplacementDetails() ||
				hasItemReplacementDetails();
	}
	
	public boolean hasSectionReplacementDetails()
	{
		return _SectionReplacementDetails.size() > 0;
	}
	
	public ArrayList<ItemRepairDetail> getSectionReplacementDetails()
	{
		return new ArrayList<ItemRepairDetail>(_SectionReplacementDetails);
	}
	
	public boolean hasArmourReplacementDetails()
	{
		return _ArmourReplacementDetails.size() > 0;
	}

	public ArrayList<ItemRepairDetail> getArmourReplacementDetails()
	{
		return new ArrayList<ItemRepairDetail>(_ArmourReplacementDetails);
	}
	
	public boolean hasItemReplacementDetails()
	{
		return _ItemReplacementDetails.size() > 0;
	}

	public ArrayList<ItemRepairDetail> getItemReplacementDetails()
	{
		return new ArrayList<ItemRepairDetail>(_ItemReplacementDetails);
	}
	
	public boolean hasRepairDetails()
	{
		return hasInternalRepairDetails() ||
				hasSectionRepairDetails() ||
				hasItemRepairDetails();
	}
	
	public boolean hasSectionRepairDetails()
	{
		return _SectionRepairDetails.size() > 0;
	}
	
	public ArrayList<ItemRepairDetail> getSectionRepairDetails()
	{
		return new ArrayList<ItemRepairDetail>(_SectionRepairDetails);
	}
	
	public boolean hasInternalRepairDetails()
	{
		return _InternalRepairDetails.size() > 0;
	}
	
	public ArrayList<ItemRepairDetail> getInternalRepairDetails()
	{
		return new ArrayList<ItemRepairDetail>(_InternalRepairDetails);
	}
	
	public boolean hasItemRepairDetails()
	{
		return _ItemRepairDetails.size() > 0;
	}

	public ArrayList<ItemRepairDetail> getItemRepairDetails()
	{
		return new ArrayList<ItemRepairDetail>(_ItemRepairDetails);
	}

	public void addSectionReplacementDetail(ItemRepairDetail ird)
	{
		_SectionReplacementDetails.add(ird);
	}
	
	public void addArmourReplacementDetail(ItemRepairDetail ird)
	{
		_ArmourReplacementDetails.add(ird);
	}
	
	public void addItemReplacementDetail(ItemRepairDetail ird)
	{
		_ItemReplacementDetails.add(ird);
	}
	
	public void addInternalRepairDetail(ItemRepairDetail ird)
	{
		_InternalRepairDetails.add(ird);
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
		StringBuilder sb = new StringBuilder();
		sb.append(indent());
		sb.append(detailStr);
		sb.append(padToColumn(sb.length(), getIndentLength() + col1));
		sb.append(Integer.toString(ird.getTime()));
		sb.append(padToColumn(sb.length(), getIndentLength() + col2));
		
		double cost = ird.getCost();
		if (cost >= 0)
			sb.append(Double.toString(ird.getCost()));
		else
			sb.append("**");
		
		sb.append(padToColumn(sb.length(), getIndentLength() + col3));
		sb.append(Integer.toString(_ModifiedSkillTarget + ird.getSkillModifier()));
		sb.append(padToColumn(sb.length(), getIndentLength() + col4));
		if (ird.getPartialRepair() > Integer.MIN_VALUE)
		{
			sb.append(Integer.toString(_ModifiedSkillTarget + ird.getSkillModifier() - ird.getPartialRepair()));				
		}
		sb.append(padToColumn(sb.length(), getIndentLength() + col5));
		sb.append(ird.getPartialRepairEffect());
		sb.append(System.lineSeparator());
		
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
			for (ItemRepairDetail ird : _ArmourReplacementDetails)
			{
				sb.append(buildItemRepairDetailLine(ird,ird.getItemType(),20,28,40,50,68));
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
			for (ItemRepairDetail ird : _InternalRepairDetails)
			{
				sb.append(buildItemRepairDetailLine(ird,ird.getItemType(),20,28,40,50,68));
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

		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("** - If the section is destroyed, the cost is that of an equivalent section. " + System.lineSeparator());
		sb.append("     If it is a blown off limb and the limb is retained, the cost is 0; " + System.lineSeparator());
		sb.append("     If the limb is not retained then the cost is of a replacement limb without any equipment other than actuators and no armour");
		sb.append(System.lineSeparator());

		
		return sb.toString();
	}
}
