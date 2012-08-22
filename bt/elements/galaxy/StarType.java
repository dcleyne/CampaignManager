package bt.elements.galaxy;

public enum StarType {
	O,
	B,
	A,
	F,
	G,
	K,
	M;
	
	private static final String[] _Names = {"O","B","A","F","G","K","M"};
	
	public String toString()
	{
		return _Names[ordinal()];
	}
	
	public static StarType fromString(String type)
	{
		if (type.equalsIgnoreCase("O"))
			return O;
		else if (type.equalsIgnoreCase("B"))
			return B;
		else if (type.equalsIgnoreCase("A"))
			return A;
		else if (type.equalsIgnoreCase("F"))
			return F;
		else if (type.equalsIgnoreCase("G"))
			return G;
		else if (type.equalsIgnoreCase("K"))
			return K;
		else if (type.equalsIgnoreCase("M"))
			return M;
		return G;
	}

}
