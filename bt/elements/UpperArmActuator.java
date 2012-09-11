package bt.elements;

public class UpperArmActuator extends WeightClassBasedItem
{

	@Override
	public String getType()
	{
		return "Upper Arm Actuator";
	}

	public double getCost()
	{
		return getMechWeight() * 200;
	}
}
