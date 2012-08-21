package bt.test;

import java.util.Vector;



import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

import bt.common.elements.Battlemech;
import bt.common.elements.scenario.Scenario;
import bt.common.elements.unit.Unit;
import bt.common.util.ExceptionUtil;
import bt.common.util.PropertyUtil;
import bt.server.managers.MissionManager;
import bt.server.managers.UnitManager;

public class TestMissionManager 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try
		{
	        PropertyConfigurator.configure(Loader.getResource("bt/server/log4j.properties"));
			PropertyUtil.loadSystemProperties("bt/common/system.properties");
            PropertyUtil.loadSystemProperties("bt/client/client.properties");

            Vector<String> missions = MissionManager.getInstance().getMissionList();
            for (String missionName : missions)
            	System.out.println(missionName);
            
            System.out.println("\n"); 
            
            Vector<String> unitNames = UnitManager.getInstance().getUnitNames();
            for (String unitName : unitNames)
            {
	            Unit u = UnitManager.getInstance().getUnit(unitName);
	            
	            String filename = "/tmp/TestScenario.xml";
	            
	            System.out.println("Generating Scenario\n"); 
	            Scenario scenario = MissionManager.getInstance().generateScenario(u);
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
