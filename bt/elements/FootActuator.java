package bt.elements;

public class FootActuator extends WeightClassBasedItem
{

	@Override
	public String getType()
	{
		return "Foot Actuator";
	}

	public double getCost()
	{
		return getMechWeight() * 120;
	}
}
