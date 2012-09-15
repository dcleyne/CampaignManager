package bt.test;

import java.util.Vector;
import bt.elements.Battlemech;
import bt.elements.BattlemechRepairReport;
import bt.elements.unit.Unit;
import bt.managers.BattlemechManager;
import bt.managers.UnitManager;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;

public class TestBattlemechRepairReport 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try
		{
			PropertyUtil.loadSystemProperties("bt/system.properties");
			
			BattlemechManager bm = new BattlemechManager();

            Vector<String> unitNames = UnitManager.getInstance().getUnitNames();
            for (String unitName : unitNames)
            {
	            Unit u = UnitManager.getInstance().getUnit(unitName);
            	System.out.println(u.getName() + System.lineSeparator());
	            
	            for (Battlemech mech : u.getBattlemechs())
	            {
	            	BattlemechRepairReport brr = bm.createRepairReport(mech, 7);
	            	System.out.println(brr.toString());
	            }
	            
	            System.out.println(System.lineSeparator());
	            System.out.println(System.lineSeparator());
            }
		}
		catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}

}
