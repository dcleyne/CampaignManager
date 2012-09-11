package bt.elements;

public class Gyro extends WeightClassBasedItem
{
	
	public enum Type
	{
		STANDARD,
		COMPACT,
		HEAVY_DUTY,
		XL;
		
		private static String[] _Names = {"Standard", "Compact", "Heavy Duty", "XL"};
		private static int[] _CostMultiplier = {300000, 400000, 500000, 750000};
		
		public int getCostMultiplier()
		{
			return _CostMultiplier[ordinal()];
		}
		
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
	
	private int _Weight = 1;
	private Type _Type = Type.STANDARD;
	
	public int getWeight()
	{
		return _Weight;
	}
	
	public void setWeight(int weight)
	{
		_Weight = weight;
	}

	@Override
	public String getType()
	{
		return "Gyro";
	}
	
	public double getCost()
	{
		return _Weight * _Type.getCostMultiplier(); 
	}

}
