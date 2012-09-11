package bt.elements;

public class LowerArmActuator extends WeightClassBasedItem
{

	@Override
	public String getType()
	{
		return "Lower Arm Actuator";
	}

	public double getCost()
	{
		return getMechWeight() * 50;
	}
}
