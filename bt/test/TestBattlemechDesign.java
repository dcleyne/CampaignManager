package bt.test;

import java.util.Vector;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

import bt.elements.Battlemech;
import bt.elements.design.BattlemechDesign;
import bt.managers.BattlemechManager;
import bt.managers.DesignManager;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;

public class TestBattlemechDesign 
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

			String path = PropertyUtil.getStringProperty("ExternalDataPath", "data");
			
            DesignManager dm = DesignManager.getInstance();
            BattlemechManager bm = new BattlemechManager();
            Vector<String> designs = dm.getDesignNames();
            for (String designName : designs)
            {
                try
                {
                    BattlemechDesign design = dm.Design(designName);

                    Battlemech mech = bm.createBattlemechFromDesign(design);
                    
                    String saveName = path + "/battlemechs/" + mech.getDesignVariant() + " " + mech.getDesignName() + ".xml";
                                        
                    bm.saveBattlemech(mech, saveName);

                    System.out.println(design.getName());

                    mech = bm.loadBattlemech(saveName);

                    System.out.println("Loaded Ok " + mech.getDesignName() + " " + mech.getDesignVariant());
                }
                catch (Exception ex)
                {
                    System.out.println("FAILED on " + designName);
                    System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
                }
            }
		}
		catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}

}
