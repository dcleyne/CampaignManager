package bt.elements.scenario;

import bt.util.Dice;

public class Wind
{
	public enum Strength
	{
	    NONE,
	    LIGHT,
	    LIGHT_GAlE,
	    MODERATE_GALE,
	    STRONG_GALE,
	    STORM,
	    TORNADO_F1_F3,
	    TORNADO_F4_F5;
	    
	    private static String[] _Names = {"No Effect","Light Gale","Moderate Gale","Strong Gale","Storm", "Tornado F1-F3", "Tornado F4-F5"};
	    
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
	    	int d = Dice.d100(1);
	    	if (d < 2) return TORNADO_F4_F5;
	    	if (d < 3) return TORNADO_F1_F3;
	    	if (d < 5) return STORM;
	    	if (d < 10) return STRONG_GALE;
	    	if (d < 25) return MODERATE_GALE;
	    	if (d < 50) return LIGHT_GAlE;
	    	if (d < 90) return LIGHT;
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
		return "Strength: " + _Strength.toString() + (_Strength != Strength.NONE ? ",  Direction: " + Integer.toString(_Direction) : "") + " [TO-57]";
	}
	
}
