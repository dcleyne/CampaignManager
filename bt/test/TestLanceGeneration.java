package bt.test;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

import bt.elements.Battlemech;
import bt.elements.personnel.Rating;
import bt.elements.unit.MechUnitParameters;
import bt.elements.unit.Player;
import bt.elements.unit.Unit;
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
	        PropertyConfigurator.configure(Loader.getResource("bt/test/log4j.properties"));
			PropertyUtil.loadSystemProperties("bt/system.properties");
			
			
	        Player p = new Player();
	
	        for (int i = 0; i < 1000; i++)
	        {
		        for (String unitSize : UnitManager.getInstance().getMechUnitParameters().keySet())
		        {
			        String unitDisplay = "";
			        MechUnitParameters mup = UnitManager.getInstance().getMechUnitParameters().get(unitSize);
			        unitDisplay += "Unit Type: " + mup.getName() + ", ";
			        unitDisplay += "Unit Size: " + Integer.toString(mup.getMechCount()) + ", ";
			        unitDisplay += "Min BV: " + Integer.toString(mup.getMinBV()) + ", ";
			        unitDisplay += "Max BV: " + Integer.toString(mup.getMaxBV()) + "\r\n";
			        
			        Unit u = UnitManager.getInstance().GenerateUnit(p, "Generated Unit", mup, Rating.REGULAR);
			
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
			        	throw new Exception("Unit outside Parameters");
		        }
	        }
		}
		catch (Exception ex)
		{
	        System.out.println(ExceptionUtil.getExceptionStackTrace(ex));			
		}
	}

}
