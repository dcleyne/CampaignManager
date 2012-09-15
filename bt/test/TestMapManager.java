package bt.test;

import java.util.Vector;
import bt.managers.MapManager;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;

public class TestMapManager 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try
		{
			PropertyUtil.loadSystemProperties("bt/system.properties");

            Vector<String> mapSheets = MapManager.getInstance().getMapSheetList();
            for (String sheetName : mapSheets)
            	System.out.println(sheetName);
            
            System.out.println("\n");
            
            Vector<String> mapSets = MapManager.getInstance().getMapSetList();
            for (String setName : mapSets)
            	System.out.println(setName);
		}
		catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}

}
