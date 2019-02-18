package bt.elements.design;

public class EngineDesign extends DesignItem
{
    private int _Rating;
    private double _Weight;
    private int _HeatSinks = 10;
    
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

	public int getHeatSinks()
	{
		return _HeatSinks;
	}

	public void setHeatSinks(int heatSinks)
	{
		_HeatSinks = heatSinks;
	}

}
