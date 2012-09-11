package bt.elements;

public class Shoulder extends Actuator
{

	@Override
	public String getType()
	{
		return "Shoulder";
	}

	public double getCost()
	{
		return getWeight() * 200;
	}
}
