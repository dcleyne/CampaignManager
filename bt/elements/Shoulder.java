package bt.elements;

public class Shoulder extends WeightClassBasedItem
{

	@Override
	public String getType()
	{
		return "Shoulder";
	}

	public double getCost()
	{
		return getMechWeight() * 200;
	}
}
