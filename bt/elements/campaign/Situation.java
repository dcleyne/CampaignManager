package bt.elements.campaign;

import java.util.ArrayList;

public class Situation
{
	private ArrayList<SituationUnitLocation> _UnitLocations = new ArrayList<SituationUnitLocation>();
	private ArrayList<SituationIntendedMovement> _UnitMovements = new ArrayList<SituationIntendedMovement>();
	
	
	
	public void addUnitLocation(SituationUnitLocation unitLocation)
	{
		_UnitLocations.add(unitLocation);
	}
	
	public ArrayList<SituationUnitLocation> getUnitLocations()
	{
		return _UnitLocations;
	}

	public void addUnitMovement(SituationIntendedMovement intendedMovement)
	{
		_UnitMovements.add(intendedMovement);
	}
	
	public ArrayList<SituationIntendedMovement> getUnitMovements()
	{
		return _UnitMovements;
	}
}
