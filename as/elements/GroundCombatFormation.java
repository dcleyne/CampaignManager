package as.elements;

import java.util.ArrayList;
import java.util.List;

public class GroundCombatFormation
{
	private GroundFormation _GroundFormation;
	private ArrayList<UnitSummary> _Elements = new ArrayList<UnitSummary>();
	
	public GroundFormation getGroundFormation()
	{
		return _GroundFormation;
	}
	public void setGroundFormation(GroundFormation groundFormation)
	{
		_GroundFormation = groundFormation;
	}
	
	public List<UnitSummary> getElements()
	{
		return _Elements;
	}
	
	public void addElement(UnitSummary unitSummary)
	{
		if (!_Elements.contains(unitSummary))
			_Elements.add(unitSummary);
	}
	
}
