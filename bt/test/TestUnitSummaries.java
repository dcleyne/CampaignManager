package bt.test;

import java.util.ArrayList;
import bt.elements.unit.Unit;
import bt.managers.UnitManager;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;

public class TestUnitSummaries 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try
		{
			PropertyUtil.loadSystemProperties("bt/system.properties");
			
			ArrayList<String> unitNames = UnitManager.getInstance().getUnitNames();
            for (String unitName : unitNames)
            {
	            Unit u = UnitManager.getInstance().getUnit(unitName);
            	System.out.println(u.getName() + System.lineSeparator());
	            UnitManager.getInstance().printUnitSummaryToPDF(u);
            }
		}
		catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}

}
