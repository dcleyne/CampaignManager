package bt.elements.campaign;

import bt.mapping.Coordinate;

public class SituationIntendedMovement
{
	private String _UnitName;
	private Coordinate _Destination;
	
	public String getUnitName()
	{
		return _UnitName;
	}
	public void setUnitName(String unitName)
	{
		_UnitName = unitName;
	}
	public Coordinate getDestination()
	{
		return _Destination;
	}
	public void setDestination(Coordinate destination)
	{
		_Destination = destination;
	}

}
