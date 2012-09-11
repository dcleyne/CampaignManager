package bt.elements;

public class Sensors extends Item
{
	private double _Weight = 1;
	
	public void setWeight(double weight)
	{
		_Weight = weight;
	}
	
	public double getWeight()
	{
		return _Weight;
	}

	@Override
	public String getType()
	{
		return "Sensors";
	}

	public double getCost()
	{
		return 2000;
	}
}
