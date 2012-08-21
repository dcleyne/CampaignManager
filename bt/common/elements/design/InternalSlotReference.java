package bt.common.elements.design;

public class InternalSlotReference 
{
    private String _InternalLocation;
	private int _Table;
    private int _Slot;
    private Boolean _RearFacing = false;

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


    public InternalSlotReference()
    {
    }

    public InternalSlotReference(String location, int table, int slot)
    {
        _InternalLocation = location;
        _Table = table;
        _Slot = slot;
    }

}
