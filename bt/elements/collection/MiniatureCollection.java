package bt.elements.collection;

import java.util.ArrayList;
import java.util.List;

public class MiniatureCollection implements ItemCollection
{
	private String _Name;
	private String _Description;
	
	private ArrayList<MiniatureCollectionItem> _Items = new ArrayList<MiniatureCollectionItem>();
	private ArrayList<MiniatureCollectionItem> _PendingItems = new ArrayList<MiniatureCollectionItem>();
	private ArrayList<MiniatureCollectionItem> _ConsumedItems = new ArrayList<MiniatureCollectionItem>();

	public String getName() 
	{
		return _Name;
	}

	public void setName(String name) 
	{
		_Name = name;
	}

	public String getDescription() 
	{
		return _Description;
	}

	public void setDescription(String description) 
	{
		_Description = description;
	}
	
	public void addItem(MiniatureCollectionItem item)
	{
		_Items.add(item);
	}
	
	@Override
	public boolean isItemAvailable(String itemName)
	{
		for (MiniatureCollectionItem item: _Items)
			if (item.getName().equalsIgnoreCase(itemName))
				return true;
		
		return false;
	}
	
	public void resetCollection()
	{
		_Items.addAll (_PendingItems);
		_Items.addAll(_ConsumedItems);
		_PendingItems.clear();
		_ConsumedItems.clear();
	}
	
	private MiniatureCollectionItem findItem(String itemName)
	{
		for (MiniatureCollectionItem item: _Items)
			if (item.getName().equalsIgnoreCase(itemName))
				return item;
		
		return null;
	}
	
	public void moveToPending(String itemName)
	{
		MiniatureCollectionItem item = findItem(itemName);
		if (item != null)
		{
			_PendingItems.add(item);
			_Items.remove(item);
		}
	}
	
	public void resetPending()
	{
		_Items.addAll (_PendingItems);
		_PendingItems.clear();		
	}
	
	public void consumePending()
	{
		_ConsumedItems.addAll (_PendingItems);
		_PendingItems.clear();		
	}
	
	public List<MiniatureCollectionItem> getPendingItems()
	{
		return new ArrayList<MiniatureCollectionItem>(_PendingItems);
	}

	public List<MiniatureCollectionItem> getConsumedItems()
	{
		return new ArrayList<MiniatureCollectionItem>(_ConsumedItems);
	}

	@Override
	public String toString()
	{
		return _Name;
	}
}
