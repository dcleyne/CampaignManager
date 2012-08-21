package bt.common.elements.design;

import java.util.Vector;

public abstract class DesignItem
{
	private String _Name = "";
	private String _Manufacturer = "";
	private String _Model = "";
	private Vector<InternalSlotReference> _SlotReferences = new Vector<InternalSlotReference>();

	public abstract String getType();

	public void setName(String _Name)
	{
		this._Name = _Name;
	}

	public String getName()
	{
		return _Name;
	}

	public void setManufacturer(String manufacturer)
	{
		_Manufacturer = manufacturer;
	}

	public String getManufacturer()
	{
		return _Manufacturer;
	}

	public void setModel(String model)
	{
		_Model = model;
	}

	public String getModel()
	{
		return _Model;
	}

	public void setSlotReferences(Vector<InternalSlotReference> slotReferences)
	{
		_SlotReferences = slotReferences;
	}

	public Vector<InternalSlotReference> getSlotReferences()
	{
		return _SlotReferences;
	}

}
