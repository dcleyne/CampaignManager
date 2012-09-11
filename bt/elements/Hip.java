package bt.elements;

public class Hip extends Actuator
{

	@Override
	public String getType()
	{
		return "Hip";
	}

	public double getCost()
	{
		return getWeight() * 300;
	}
}
