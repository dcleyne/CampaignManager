package as;

import as.elements.Era;
import as.elements.Faction;
import as.elements.GroundCombatFormation;
import as.elements.GroundFormation;

public class ForceBuilder
{
	//private UnitManager _UnitManager = new UnitManager();
	
	public void generateForce(Era era, Faction faction, int PVMax)
	{
		
	}
	
	public GroundCombatFormation generateCombatFormation(Era era, Faction faction, GroundFormation groundFormation)
	{
		
		switch (faction.getID())
		{
			case 5:
				return generateLance(era, groundFormation);
			case 48:
				return generateLevel2(era, groundFormation);
			default:
				return generateStar(era, groundFormation);
		}
	}
	
	private GroundCombatFormation generateLance(Era era, GroundFormation groundFormation)
	{
		return null;
	}

	private GroundCombatFormation generateLevel2(Era era, GroundFormation groundFormation)
	{
		return null;
	}

	private GroundCombatFormation generateStar(Era era, GroundFormation groundFormation)
	{
		return null;
	}

	public void main(String[] args)
	{
		//ForceBuilder fb = new ForceBuilder();
		
		
		
	}
}
