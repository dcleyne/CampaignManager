package bt.elements.unit;

public enum QualityRating 
{
	A,
	B,
	C,
	D,
	E,
	F;
	
	private static String[] _Strings = {"A", "B", "C", "D", "E", "F"};
	private static String[] _Descriptions = {"Salvage", "Poor", "Fair", "Average", "Good", "Excellent"};
	private static double[] _CostModifiers = {0.8, 0.9, 0.95, 1.0, 1.1, 1.3};
	private static int[] _Modifiers = {3,2,1,0,-1,-2};
	
	public String toString()
	{
		return _Strings[ordinal()];
	}
	
	public String getDescription()
	{
		return _Descriptions[ordinal()];
	}
	
	public double GetCostModifier()
	{
		return _CostModifiers[ordinal()];
	}
	
	public int GetModifier()
	{
		return _Modifiers[ordinal()];
	}
	
	public static QualityRating fromString(String string)
	{
		for (int i = 0; i < _Strings.length; i++)
		{
			if (string.equalsIgnoreCase(_Strings[i]))
				return values()[i];
		}
		return null;
	}
	
}
