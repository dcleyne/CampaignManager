package bt.elements.collection;

public class UnlimitedCollection implements ItemCollection 
{
	public boolean isItemAvailable(String itemName) 
	{
		return true;
	}
	
	public void resetCollection()
	{
		
	}
	
	public void moveToPending(String itemName)
	{
		
	}
	
	public void resetPending()
	{
		
	}
	
	public void consumePending()
	{
		
	}
	
	@Override
	public String toString()
	{
		return "Unlimited Collection";
	}

}
