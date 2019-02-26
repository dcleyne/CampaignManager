package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import bt.elements.Asset;
import bt.elements.Battlemech;
import bt.elements.Era;
import bt.elements.Faction;
import bt.elements.missions.Mission;
import bt.elements.personnel.Rating;
import bt.elements.scenario.Opponent;
import bt.elements.scenario.Scenario;
import bt.elements.unit.Force;
import bt.elements.unit.QualityRating;
import bt.elements.unit.TechRating;
import bt.elements.unit.Unit;
import bt.managers.MiniatureCollectionManager;
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
			PropertyUtil.loadSystemProperties("bt/system.properties");

			Vector<String> missions = MissionManager.getInstance().getMissionList();
			for (String missionName : missions)
				System.out.println(missionName);

			System.out.println("\n");

			Date missionDate = new Date();
			ArrayList<String> unitNames = UnitManager.getInstance().getUnitNames();
			for (String unitName : unitNames)
			{
				try
				{
					Unit u = UnitManager.getInstance().getUnit(unitName);

					Force playerForce = new Force(u, MiniatureCollectionManager.getInstance().getMiniatureCollection("Desert Yellow"));
					playerForce.setCurrentDate(missionDate);

					Opponent opponent = new Opponent(Faction.MERCENARY, Rating.REGULAR, QualityRating.D, TechRating.D);
					Mission m = MissionManager.getInstance().getMission("Beachhead (Attacker)");

					String filename = u.getName() + "-TestScenario.xml";

					System.out.println("Generating Scenario for unit: " + unitName + System.lineSeparator());
					Scenario scenario = MissionManager.getInstance().generateScenario(
							Era.LATE_SUCCESSION_WAR_RENAISSANCE, m, missionDate, playerForce, opponent, MiniatureCollectionManager.getInstance().getMiniatureCollection("Plastic"));
					System.out.println("Storing Scenario" + System.lineSeparator());
					MissionManager.getInstance().saveScenario(filename, scenario);
					System.out.println("Loading Scenario" + System.lineSeparator());
					scenario = MissionManager.getInstance().loadScenario(filename);
					System.out.println("Scenario Generation Ok" + System.lineSeparator());

					System.out.println("Scenario : " + scenario.getMission().getName());
					System.out.println(System.lineSeparator());

					printForce(scenario.getAttacker(), "Attacker");
					printForce(scenario.getDefender(), "Defender");

					MissionManager.getInstance().printScenarioToPDF(unitName, 1, scenario);
				} catch (Exception ex)
				{
					System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
				}
			}
		} catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}

	private static void printForce(Force f, String name)
	{
		System.out.println("Side : " + name);
		System.out.println("Parent Unit : " + f.getParentUnit());

		String unitDisplay = "";
		int totalBV = 0;
		int totalWeight = 0;
		for (Asset asset : f.getAssets())
		{
			if (asset instanceof Battlemech)
			{
				Battlemech mech = (Battlemech) asset;
				totalBV += mech.getBV();
				totalWeight += mech.getWeight();
				unitDisplay += mech.getDesignVariant() + " " + mech.getDesignName() + System.lineSeparator();
			}
		}
		unitDisplay += "\r\nTotal BV: " + Integer.toString(totalBV) + ", ";
		unitDisplay += "Total Weight: " + Integer.toString(totalWeight) + System.lineSeparator();

		System.out.println(unitDisplay);

	}

}
