package bt.elements;

public class Hip extends WeightClassBasedItem
{

	@Override
	public String getType()
	{
		return "Hip";
	}

	public double getCost()
	{
		return getMechWeight() * 300;
	}
}
