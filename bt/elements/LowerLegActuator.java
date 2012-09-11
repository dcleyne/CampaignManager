package bt.elements;

public class LowerLegActuator extends Actuator
{

	@Override
	public String getType()
	{
		return "Lower Leg Actuator";
	}

	public double getCost()
	{
		return getWeight() * 80;
	}
}
