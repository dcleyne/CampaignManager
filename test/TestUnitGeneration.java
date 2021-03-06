package test;

import bt.elements.Battlemech;
import bt.elements.Era;
import bt.elements.Faction;
import bt.elements.collection.ItemCollection;
import bt.elements.personnel.Rating;
import bt.elements.unit.Player;
import bt.elements.unit.QualityRating;
import bt.elements.unit.TechRating;
import bt.elements.unit.Unit;
import bt.managers.DesignManager;
import bt.managers.MiniatureCollectionManager;
import bt.managers.UnitManager;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;

public class TestUnitGeneration
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			PropertyUtil.loadSystemProperties("bt/system.properties");
			
			
	        Player p = new Player();
	        //ArrayList<String> collectionNames = MiniatureCollectionManager.getInstance().getCollectionNames();	        
	        ItemCollection ic = MiniatureCollectionManager.getInstance().getMiniatureCollection("Bright Yellow");
	
	        String[] lanceWeights = new String[] 
    		{
    				"Light", 
    				"Light Medium", 
    				"Medium"
    		};

	        Unit u = null;
	        int tries = 0;
	        while (u == null && tries < 10)
	        {
	        	u = UnitManager.getInstance().generateUnit(Era.LATE_SUCCESSION_WAR_RENAISSANCE, Faction.DRACONIS_COMBINE, p, "Generated Unit", lanceWeights, Rating.REGULAR, QualityRating.D, TechRating.D, ic);
	        	tries++;
	        }
        	if (u == null)
        		throw new Exception("Failed :(");
	
	        String unitDisplay = "";
	        
	        int totalBV = 0;
	        int totalWeight = 0;
	        DesignManager dm = DesignManager.getInstance();
	        for (Battlemech mech : u.getBattlemechs())
	        {
	            totalBV += dm.Design(mech.getVariantName()).getBV();
	            totalWeight += mech.getWeight();
	            unitDisplay += mech.getDesignVariant() + " " + mech.getDesignName() + "\r\n";
	        }
	        unitDisplay += "\r\nTotal BV: " + Integer.toString(totalBV) + ", ";
	        unitDisplay += "Total Weight: " + Integer.toString(totalWeight) + "\r\n";
	        
	        UnitManager.getInstance().saveUnit(u);
	        
	        System.out.println(unitDisplay);		        
		}
		catch (Exception ex)
		{
	        System.out.println(ExceptionUtil.getExceptionStackTrace(ex));			
		}
	}

}
