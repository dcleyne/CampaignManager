package bt.elements;

public class UpperLegActuator extends Actuator
{

	@Override
	public String getType()
	{
		return "Upper Leg Actuator";
	}

	public double getCost()
	{
		return getWeight() * 150;
	}
}
