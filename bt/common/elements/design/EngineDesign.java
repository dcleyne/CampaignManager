package bt.common.elements.design;

public class EngineDesign extends DesignItem
{
    private int _Rating;
    private double _Weight;
    
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

	@Override
	public String getType()
	{
		return "Engine";
	}

}
