package bt.elements;

public abstract class Actuator extends WeightClassBasedItem
{
	private double _Weight;
	
	public void setWeight(double weight)
	{
		_Weight = weight;
	}
	
	public double getWeight()
	{
		return _Weight;
	}


}
