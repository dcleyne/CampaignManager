package bt.elements;

public class UpperArmActuator extends Actuator
{

	@Override
	public String getType()
	{
		return "Upper Arm Actuator";
	}

	public double getCost()
	{
		return getWeight() * 200;
	}
}
