package bt.elements;

public class HandActuator extends Actuator
{

	@Override
	public String getType()
	{
		return "Hand Actuator";
	}

	public double getCost()
	{
		return getWeight() * 80;
	}
}
