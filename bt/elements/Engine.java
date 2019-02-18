package bt.elements;

public class Engine extends Item
{
	public enum Type
	{
		FUSION,
		ICE,
		XL_FUSION;
	}
	
	private Engine.Type _Type = Engine.Type.FUSION;
    private int _Rating;
    private double _Weight;
    private int _HeatSinks = 10;
    
    @Override
    public String getType()
    { return "Engine"; }
    
	public Type getEngineType()
	{
		return _Type;
	}
	public void setEngineType(Type type)
	{
		_Type = type;
	}
	public int getRating()
	{
		return _Rating;
	}
	public void setRating(int rating)
	{
		_Rating = rating;
	}
	public double getWeight()
	{
		return _Weight;
	}
	public void setWeight(double weight)
	{
		_Weight = weight;
	}
	public int getHeatSinks()
	{
		return _HeatSinks;
	}
	public void setHeatSinks(int heatSinks)
	{
		_HeatSinks = heatSinks;
	}
    
	public double getCost()
	{
		int factor = 5000;
		switch (_Type)
		{
		case FUSION:
			factor = 5000;
			break;
		case ICE:
			factor = 1250;
			break;
		case XL_FUSION:
			factor = 20000;
			break;
		}
		
		return Math.round(factor * _Weight * _Rating / 75);
	}

}
