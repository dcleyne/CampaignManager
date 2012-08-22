package bt.elements;

public class JumpJet extends Item
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

}
