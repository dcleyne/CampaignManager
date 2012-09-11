package bt.elements;

public class LowerArmActuator extends Actuator
{

	@Override
	public String getType()
	{
		return "Lower Arm Actuator";
	}

	public double getCost()
	{
		return getWeight() * 50;
	}
}
