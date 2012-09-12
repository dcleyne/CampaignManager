package bt.elements;

import java.util.Vector;

public class ItemMount
{
    private Item _MountedItem;
    private Vector<InternalSlotStatus> _SlotReferences = new Vector<InternalSlotStatus>();
    
    
    public Item getMountedItem()
	{
		return _MountedItem;
	}

	public void setMountedItem(Item mountedItem)
	{
		_MountedItem = mountedItem;
	}

	public Vector<InternalSlotStatus> getSlotReferences()
	{
		return _SlotReferences;
	}

	public void setSlotReferences(Vector<InternalSlotStatus> slotReferences)
	{
		_SlotReferences = slotReferences;
	}

	public ItemMount(Item item, Vector<InternalSlotStatus> slotReferences)
    {
        _MountedItem = item;
        _SlotReferences = slotReferences;
    }

    public ItemMount()
    {
    }
    
    public boolean isDamaged()
    {
    	for (InternalSlotStatus iss: _SlotReferences)
    		if (iss.getStatus() == ItemStatus.DAMAGED || iss.getStatus() == ItemStatus.DESTROYED)
    			return true;
    	
    	return false;
    }

    public boolean isRearFacing()
    {
    	for (InternalSlotStatus iss: _SlotReferences)
    		if (iss.getRearFacing())
    			return true;
    	
    	return false;
    }
    
    public String toString()
    {
    	StringBuilder sb = new StringBuilder();
    	sb.append("(" + _MountedItem.getIdentifier() + ") ");
    	sb.append(_MountedItem.toString());
    	sb.append(" [");
    	sb.append(_SlotReferences.elementAt(0).getInternalLocation());
    	if (_SlotReferences.elementAt(0).getRearFacing())
        	sb.append("(R)");
    	
    	sb.append("]");
    	return sb.toString();
    }
}
