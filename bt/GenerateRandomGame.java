package bt;

import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import bt.elements.Battlemech;
import bt.elements.Era;
import bt.elements.Faction;
import bt.elements.collection.UnlimitedCollection;
import bt.elements.missions.Mission;
import bt.elements.personnel.Rating;
import bt.elements.scenario.Opponent;
import bt.elements.scenario.Scenario;
import bt.elements.unit.Force;
import bt.elements.unit.MechUnitParameters;
import bt.elements.unit.Player;
import bt.elements.unit.QualityRating;
import bt.elements.unit.TechRating;
import bt.elements.unit.Unit;
import bt.managers.DesignManager;
import bt.managers.MissionManager;
import bt.managers.UnitManager;
import bt.util.Dice;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;

public class GenerateRandomGame 
{

	public static void main(String[] args) 
	{
		(new GenerateRandomGame()).generateGame();
	}
	
	private Unit buildUnit(Player p, String unitName, MechUnitParameters mup)
	{
		DesignManager dm = DesignManager.getInstance();
		try {
			for (int i = 0; i < 1000; i++)
			{
		        Unit u = UnitManager.getInstance().generateUnit(Era.LATE_SUCCESSION_WAR_RENAISSANCE, Faction.MERCENARY, p, unitName, mup, Rating.REGULAR, QualityRating.D, TechRating.D, new UnlimitedCollection());
		        int totalBV = 0;
		        for (Battlemech mech : u.getBattlemechs())
		        {
		            totalBV += dm.Design(mech.getVariantName()).getBV();
		        }
		        
		        if (totalBV >= mup.getMinBV() && totalBV <= mup.getMaxBV())
		        	return u;
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public void generateGame()
	{
		try
		{
			PropertyUtil.loadSystemProperties("bt/system.properties");
			
			Player p1 = new Player();
			p1.setName("Player 1");
			p1.setNickname("P1");
			
			int unitParams = Dice.random(UnitManager.getInstance().getMechUnitParameters().keySet().size()) - 1;
			String unitSize = new ArrayList<String>(UnitManager.getInstance().getMechUnitParameters().keySet()).get(unitParams);
			MechUnitParameters mup = UnitManager.getInstance().getMechUnitParameters().get(unitSize);
			
			Unit player1Unit = buildUnit(p1, "Player 1 Unit", mup);
			if (player1Unit == null)
				throw new Exception("Unable to create unit for player 1");
			Force playerForce = new Force();
			Opponent opponent = new Opponent(Faction.MERCENARY, Rating.REGULAR, QualityRating.D, TechRating.D);
			Mission m = MissionManager.getInstance().getMission("Assault");

			
			Scenario s = MissionManager.getInstance().generateScenario(Era.LATE_SUCCESSION_WAR_RENAISSANCE, m, new Date(), playerForce, opponent, new UnlimitedCollection());
			if (s == null)
				throw new Exception("Unable to create scenario");
			
			
			String filename = MissionManager.getInstance().printScenarioToPDF(player1Unit.getName(), 1, s);
			System.out.println(filename);
			Desktop.getDesktop().open(new File(filename));
		}
		catch (Exception ex)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}

}
