package bt.elements;

public class HandActuator extends WeightClassBasedItem
{

	@Override
	public String getType()
	{
		return "Hand Actuator";
	}

	public double getCost()
	{
		return getMechWeight() * 80;
	}
}
