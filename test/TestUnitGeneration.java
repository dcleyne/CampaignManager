package test;

import java.util.ArrayList;
import java.util.Arrays;

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
	        ItemCollection ic = MiniatureCollectionManager.getInstance().getMiniatureCollection("Plastic");
	
	        String[] designNames = new String[] 
    		{
    				"MAD-3R Marauder", 
    				"WHM-6R Warhammer", 
    				"PXH-1K Phoenix Hawk",
    				"STG-3R Stinger",
    				"CRD-3R Crusader",
    				"PXH-1K Phoenix Hawk",
    				"RFL-3N Rifleman",
    				"WSP-1A Wasp",
    				"GRF-1N Griffin",
    				"SHD-2H Shadow Hawk",
    				"WSP-1A Wasp",
    				"STG-3R Stinger"
    		};
	        ArrayList<String> mechDesigns = new ArrayList<String>(Arrays.asList(designNames));

	        Unit u = null;
	        int tries = 0;
	        while (u == null && tries < 10)
	        {
	        	u = UnitManager.getInstance().generateUnitWithElements(Era.LATE_SUCCESSION_WAR_RENAISSANCE, Faction.MERCENARY, p, "Generated Unit	", mechDesigns, Rating.REGULAR, QualityRating.D, TechRating.D, ic);
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
