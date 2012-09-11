package bt.elements;

public class JumpJet extends WeightClassBasedItem
{
    private double _Weight;
    
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
		return "Jump Jet";
	}

	public double getCost()
	{
		return 200 * _Weight;
	}
}
