package bt.elements.unit;

public enum TechRating 
{
	A,
	B,
	C,
	D,
	E,
	F;
	
	private static String[] _Strings = {"A", "B", "C", "D", "E", "F"};
	private static int[] _Modifiers = {-4, -2, 0, 1, 2, 3};
	
	public String toString()
	{
		return _Strings[ordinal()];
	}
	
	public int GetModifier()
	{
		return _Modifiers[ordinal()];
	}
	
	public static TechRating fromString(String string)
	{
		for (int i = 0; i < _Strings.length; i++)
		{
			if (string.equalsIgnoreCase(_Strings[i]))
				return values()[i];
		}
		return null;
	}
}
