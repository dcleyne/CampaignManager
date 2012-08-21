package bt.common.elements.scenario;

import bt.common.util.Dice;

public class Wind
{
	public enum Strength
	{
	    NONE,
	    LIGHT,
	    MODERATE,
	    HIGH,
	    GALE;
	    
	    private static String[] _Names = {"None","Light","Moderate","High","Gale"};
	    
	    public String toString()
	    { return _Names[ordinal()]; }
	    
	    public static Strength fromString(String r)
	    {
	    	
	    	for (int i = 0; i < _Names.length; i++)
	    	{
	    		if (_Names[i].equalsIgnoreCase(r))
	    			return values()[i];
	    	}
	    	return null;
	    }
	    
	    public static Strength getRandomStrength()
	    {
	    	int d = Dice.d10(1);
	    	if (d < 2) return GALE;
	    	if (d < 4) return HIGH;
	    	if (d < 6) return MODERATE;
	    	if (d < 9) return LIGHT;
	    	return NONE;
	    }
	}
	
	private Strength _Strength;
	private int _Direction;
	public Strength getStrength()
	{
		return _Strength;
	}
	public void setStrength(Strength strength)
	{
		_Strength = strength;
	}
	public int getDirection()
	{
		return _Direction;
	}
	public void setDirection(int direction)
	{
		_Direction = direction;
	}
	
	public static Wind getRandomWind()
	{
		Wind w = new Wind();
		w.setDirection(Dice.d6(1));
		w.setStrength(Strength.getRandomStrength());
		return w;
	}
	
	@Override
	public String toString()
	{
		return "Strength: " + _Strength.toString() + ",  Direction: " + Integer.toString(_Direction);
	}
	
}
