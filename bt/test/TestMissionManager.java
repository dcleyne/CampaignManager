package bt.test;

import java.util.Vector;



import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

import bt.elements.Battlemech;
import bt.elements.personnel.Rating;
import bt.elements.scenario.Scenario;
import bt.elements.unit.QualityRating;
import bt.elements.unit.TechRating;
import bt.elements.unit.Unit;
import bt.managers.MissionManager;
import bt.managers.UnitManager;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;

public class TestMissionManager 
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

            Vector<String> missions = MissionManager.getInstance().getMissionList();
            for (String missionName : missions)
            	System.out.println(missionName);
            
            System.out.println("\n"); 
            
            Vector<String> unitNames = UnitManager.getInstance().getUnitNames();
            for (String unitName : unitNames)
            {
	            Unit u = UnitManager.getInstance().getUnit(unitName);
	            
	            String filename = "/tmp/TestScenario.xml";
	            
	            System.out.println("Generating Scenario for unit: " + unitName + "\n"); 
	            Scenario scenario = MissionManager.getInstance().generateScenario(u, Rating.REGULAR, QualityRating.D, TechRating.D);
	            System.out.println("Storing Scenario\n"); 
	            MissionManager.getInstance().saveScenario(filename, scenario);
	            System.out.println("Loading Scenario\n"); 
	            scenario = MissionManager.getInstance().loadScenario(filename);
	            System.out.println("Scenario Generation Ok\n");
	            
	            System.out.println("Scenario : " + scenario.getMission().getName());
	            System.out.println("Force Ratio : " + scenario.getMission().getForceRatio());
	            System.out.println("\n"); 
	            System.out.println("Season : " + scenario.getSeason());
	            System.out.println("Wind : " + scenario.getWind());
	            System.out.println("Time Of Day : " + scenario.getTimeOfDay());
	            System.out.println("\n"); 
	
	            for (String side : scenario.getSides().keySet())
	            {
	                Unit unit = scenario.getSides().get(side);
	                System.out.println("Side : " + side);
	                System.out.println("Unit : " + unit.getName());
	                
			        String unitDisplay = "";
			        int totalBV = 0;
			        int totalWeight = 0;
			        for (Battlemech mech : unit.getBattlemechs())
			        {
			            totalBV += mech.getBV();
			            totalWeight += mech.getWeight();
			            unitDisplay += mech.getDesignVariant() + " " + mech.getDesignName() + "\r\n";
			        }
			        unitDisplay += "\r\nTotal BV: " + Integer.toString(totalBV) + ", ";
			        unitDisplay += "Total Weight: " + Integer.toString(totalWeight) + "\r\n";
			        
			        System.out.println(unitDisplay);
	            }
	            
	            MissionManager.getInstance().printScenarioToPDF("/tmp/scenarios",u,1, scenario);
	            
	            //System.exit(0);
            }
		}
		catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}

}
