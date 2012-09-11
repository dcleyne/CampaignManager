package bt.elements;

public class LowerLegActuator extends WeightClassBasedItem
{

	@Override
	public String getType()
	{
		return "Lower Leg Actuator";
	}

	public double getCost()
	{
		return getMechWeight() * 80;
	}
}
