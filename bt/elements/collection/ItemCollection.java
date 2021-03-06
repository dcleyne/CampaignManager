package bt.elements.collection;

public interface ItemCollection
{
	public String getName();
	public boolean isItemAvailable(String itemName);
	
	public void resetCollection();
	public void moveToPending(String itemName);
	public void resetPending();
	public void consumePending();
	
}
