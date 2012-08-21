package bt.test;

import java.util.Vector;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

import bt.common.elements.Battlemech;
import bt.common.elements.design.BattlemechDesign;
import bt.common.elements.unit.Unit;
import bt.common.util.ExceptionUtil;
import bt.common.util.PropertyUtil;
import bt.server.managers.BattlemechManager;
import bt.server.managers.DesignManager;
import bt.server.managers.UnitManager;

public class RefreshUnitBattlemechs
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
	
	        DesignManager dm = DesignManager.getInstance();
	        BattlemechManager bm = new BattlemechManager();
	        
	        Vector<String> unitNames = UnitManager.getInstance().getUnitNames();
	        for (String unitName : unitNames)
	        {
	        	System.out.println("Rebuilding mechs for " + unitName);
	        	Unit u = UnitManager.getInstance().getUnit(unitName);
	        	Vector<Battlemech> mechs = new Vector<Battlemech>(u.getBattlemechs());
	        	u.getBattlemechs().clear();
	        	for (Battlemech oldMech : mechs)
	        	{
	        		String designName = oldMech.getDesignVariant() + " " + oldMech.getDesignName();
	        		BattlemechDesign bd = dm.Design(designName);
	        		if (bd != null)
	        		{
	        			Battlemech newMech = bm.createBattlemechFromDesign(bd);
	        			u.getBattlemechs().add(newMech);
	        		}
	        	}
	        	UnitManager.getInstance().saveUnit(u);
	        	System.out.println("Rebuilding mechs for " + unitName + " DONE");
	        }
	        
		}
		catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}

}
