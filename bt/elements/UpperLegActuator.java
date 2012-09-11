package bt.elements;

public class UpperLegActuator extends WeightClassBasedItem
{

	@Override
	public String getType()
	{
		return "Upper Leg Actuator";
	}

	public double getCost()
	{
		return getMechWeight() * 150;
	}
}
