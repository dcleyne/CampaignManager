package bt.elements.personnel;

public class Administrator extends Personnel
{
	@Override
	public JobType getJobType()
	{
		return JobType.ADMINISTRATION;
	}

}
