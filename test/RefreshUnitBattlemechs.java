package test;

import java.util.ArrayList;
import java.util.Vector;

import bt.elements.Battlemech;
import bt.elements.design.BattlemechDesign;
import bt.elements.unit.Unit;
import bt.managers.BattlemechManager;
import bt.managers.DesignManager;
import bt.managers.UnitManager;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;

public class RefreshUnitBattlemechs
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			PropertyUtil.loadSystemProperties("bt/system.properties");
	
	        DesignManager dm = DesignManager.getInstance();
	        BattlemechManager bm = new BattlemechManager();
	        
	        ArrayList<String> unitNames = UnitManager.getInstance().getUnitNames();
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
