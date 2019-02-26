package test;

import java.util.ArrayList;

import bt.elements.Battlemech;
import bt.elements.Era;
import bt.elements.Faction;
import bt.elements.collection.ItemCollection;
import bt.elements.personnel.Rating;
import bt.elements.unit.MechUnitParameters;
import bt.elements.unit.Player;
import bt.elements.unit.QualityRating;
import bt.elements.unit.TechRating;
import bt.elements.unit.Unit;
import bt.managers.MiniatureCollectionManager;
import bt.managers.UnitManager;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;

public class TestLanceGeneration
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
	        ArrayList<String> collectionNames = MiniatureCollectionManager.getInstance().getCollectionNames();	        
	        ItemCollection ic = MiniatureCollectionManager.getInstance().getMiniatureCollection(collectionNames.get(1));
	
	        for (String unitSize : UnitManager.getInstance().getMechUnitParameters().keySet())
	        {
		        String unitDisplay = "";
		        MechUnitParameters mup = UnitManager.getInstance().getMechUnitParameters().get(unitSize);
		        unitDisplay += "Unit Type: " + mup.getName() + ", ";
		        unitDisplay += "Unit Size: " + Integer.toString(mup.getMechCount()) + ", ";
		        unitDisplay += "Min BV: " + Integer.toString(mup.getMinBV()) + ", ";
		        unitDisplay += "Max BV: " + Integer.toString(mup.getMaxBV()) + "\r\n";
		        
		        Unit u = UnitManager.getInstance().generateUnit(Era.LATE_SUCCESSION_WAR_RENAISSANCE, Faction.MERCENARY, p, "Generated Unit", mup, Rating.REGULAR, QualityRating.D, TechRating.D, ic);
		
		        int totalBV = 0;
		        int totalWeight = 0;
		        for (Battlemech mech : u.getBattlemechs())
		        {
		            totalBV += mech.getBV();
		            totalWeight += mech.getWeight();
		            unitDisplay += mech.getDesignVariant() + " " + mech.getDesignName() + "\r\n";
		        }
		        unitDisplay += "\r\nTotal BV: " + Integer.toString(totalBV) + ", ";
		        unitDisplay += "Total Weight: " + Integer.toString(totalWeight) + "\r\n";
		        
		        System.out.println(unitDisplay);
		        
		        if (totalBV < mup.getMinBV() || totalBV > mup.getMaxBV())
		        {
		        	System.out.println("Unit outside Parameters... " + totalBV + " : " + mup);
		        }
	        }
		}
		catch (Exception ex)
		{
	        System.out.println(ExceptionUtil.getExceptionStackTrace(ex));			
		}
	}

}
