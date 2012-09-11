package bt.elements;

public class FootActuator extends Actuator
{

	@Override
	public String getType()
	{
		return "Foot Actuator";
	}

	public double getCost()
	{
		return getWeight() * 120;
	}
}
