package bt.elements;

public class InternalSlotStatus
{
    private String _InternalLocation;
    private int _Table;
    private int _Slot;
    private Boolean _RearFacing = false;
    private ItemStatus _Status;
	public String getInternalLocation()
	{
		return _InternalLocation;
	}
	public void setInternalLocation(String internalLocation)
	{
		_InternalLocation = internalLocation;
	}
	public int getTable()
	{
		return _Table;
	}
	public void setTable(int table)
	{
		_Table = table;
	}
	public int getSlot()
	{
		return _Slot;
	}
	public void setSlot(int slot)
	{
		_Slot = slot;
	}
	public Boolean getRearFacing()
	{
		return _RearFacing;
	}

	public void setRearFacing(Boolean rearFacing)
	{
		_RearFacing = rearFacing;
	}

	public ItemStatus getStatus()
	{
		return _Status;
	}
	public void setStatus(ItemStatus status)
	{
		_Status = status;
	}
    
    public InternalSlotStatus()
    {
    }

    public InternalSlotStatus(String internalLocation, int table, int slot, ItemStatus status)
    {
        _InternalLocation = internalLocation;
        _Table = table;
        _Slot = slot;
        _Status = status;
    }
}
