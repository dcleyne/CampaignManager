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
	private double _Cost = 0;
	private String _ItemManufacturer = "";
	private String _ItemModel = "";
	
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
	
	public double getCost()
	{
		return _Cost;
	}
	public void setCost(double cost)
	{
		_Cost = cost;
	}
	
	public String getItemManufacturer()
	{
		return _ItemManufacturer;
	}
	public void setItemManufacturer(String itemManufacturer)
	{
		_ItemManufacturer = itemManufacturer;
	}
	public String getItemModel()
	{
		return _ItemModel;
	}
	public void setItemModel(String itemModel)
	{
		_ItemModel = itemModel;
	}
	public ItemRepairDetail(Type type, String itemType, int skillModifier, int partialRepair, String partialRepairEffect, int time, double cost, String manufacturer, String model)
	{
		_Type = type;
		_ItemType = itemType;
		_SkillModifier = skillModifier;
		_PartialRepair = partialRepair;
		_PartialRepairEffect = partialRepairEffect;
		_Time = time;
		_Cost = cost;
		_ItemManufacturer = manufacturer;
		_ItemModel = model;
	}
	
	
}
