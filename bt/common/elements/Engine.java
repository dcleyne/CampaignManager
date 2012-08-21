package bt.common.elements;

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
    

}
