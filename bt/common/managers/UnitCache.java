package bt.common.managers;

import java.util.HashMap;
import java.util.Vector;

import bt.common.elements.unit.Unit;
import bt.common.elements.unit.UnitSummary;

public class UnitCache 
{
	private static UnitCache theInstance;
	
	private Vector<UnitSummary> _Units = new Vector<UnitSummary>();
	private HashMap<String, Unit> _UnitDetails = new HashMap<String, Unit>();
	
	private UnitCache()
	{
		
	}	
	
	public static UnitCache getInstance()
	{
		if (theInstance == null)
			theInstance = new UnitCache();
		
		return theInstance;
	}
	
	public Vector<UnitSummary> getUnits()
	{
		return new Vector<UnitSummary>(_Units);
	}
	
	public void setUnits(Vector<UnitSummary> newUnits)
	{
		_Units.clear();
		_Units.addAll(newUnits);
	}
	
	public void putUnit(String unitName, Unit unit)
	{
		_UnitDetails.put(unitName,unit);
	}
	
	public Unit getUnit(String unitName)
	{
		return _UnitDetails.get(unitName);
	}
	
}
