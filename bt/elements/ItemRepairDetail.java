package bt.elements;

public class ItemRepairDetail
{
	public enum Type
	{
		REPLACEMENT,
		REPAIR;
		
		private static String[] _Names = { "Replacement, Repair" };
		
		public String toString()
		{
			return _Names[ordinal()];
		}
		
		public static Type fromString(String str)
		{
			for (int i = 0; i < _Names.length; i++)
			{
				if (_Names[i].equalsIgnoreCase(str))
					return values()[i];
			}
			
			return null;
		}
	}
	
	private Type _Type = Type.REPLACEMENT;
	private String _ItemType = "";
	private int _SkillModifier = 0;
	private int _PartialRepair = Integer.MIN_VALUE;
	private String _PartialRepairEffect = "";
	private int _Time = 0;
	private int _Cost = 0;
	
	public Type getType()
	{
		return _Type;
	}
	public void setType(Type type)
	{
		_Type = type;
	}
	public String getItemType()
	{
		return _ItemType;
	}
	public void setItemType(String itemType)
	{
		_ItemType = itemType;
	}
	public int getSkillModifier()
	{
		return _SkillModifier;
	}
	public void setSkillModifier(int skillModifier)
	{
		_SkillModifier = skillModifier;
	}
	public int getPartialRepair()
	{
		return _PartialRepair;
	}
	public void setPartialRepair(int partialRepair)
	{
		_PartialRepair = partialRepair;
	}
	public String getPartialRepairEffect()
	{
		return _PartialRepairEffect;
	}
	public void setPartialRepairEffect(String partialRepairEffect)
	{
		_PartialRepairEffect = partialRepairEffect;
	}
	public int getTime()
	{
		return _Time;
	}
	public void setTime(int time)
	{
		_Time = time;
	}
	
	public int getCost()
	{
		return _Cost;
	}
	public void setCost(int cost)
	{
		_Cost = cost;
	}
	public ItemRepairDetail(Type type, String itemType, int skillModifier, int partialRepair, String partialRepairEffect, int time, int cost)
	{
		_Type = type;
		_ItemType = itemType;
		_SkillModifier = skillModifier;
		_PartialRepair = partialRepair;
		_PartialRepairEffect = partialRepairEffect;
		_Time = time;
		setCost(cost);
	}
	
	
}
