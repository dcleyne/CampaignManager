package bt.test;

import java.util.Vector;
import bt.elements.Battlemech;
import bt.elements.design.BattlemechDesign;
import bt.managers.BattlemechManager;
import bt.managers.DesignManager;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;

public class TestBattlemechBVCalculations 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try
		{
			PropertyUtil.loadSystemProperties("bt/system.properties");

			Vector<Battlemech> _Ok = new Vector<Battlemech>();
			Vector<Battlemech> _Different = new Vector<Battlemech>();
			
            DesignManager dm = DesignManager.getInstance();
            BattlemechManager bm = new BattlemechManager();
            Vector<String> designs = dm.getDesignNames();
            String singleDesign = "";
            for (String designName : designs)
            {
                try
                {
                    BattlemechDesign design = dm.Design(designName);
                    
                    if (!singleDesign.isEmpty() && !designName.equalsIgnoreCase(singleDesign))
                    	continue;
                    
                    Battlemech mech = bm.createBattlemechFromDesign(design);
                    
                    if (mech.getBV() == mech.getAdjustedBV())
                    	_Ok.add(mech);
                    else
                    	_Different.add(mech);
                }
                catch (Exception ex)
                {
                    System.out.println("FAILED on " + designName);
                    System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
                }
            }

            System.out.println("== OK ==");
            for (Battlemech mech : _Ok)
            	System.out.println(mech.getDesignVariant() + " " + mech.getDesignName() + " BV:" + Integer.toString(mech.getBV()) + " AdjBV:" + Integer.toString(mech.getAdjustedBV()));
            
            System.out.println("");
            System.out.println("== Different ==");
            
            for (Battlemech mech : _Different)
            	System.out.println(mech.getDesignVariant() + " " + mech.getDesignName() + " BV:" + Integer.toString(mech.getBV()) + " AdjBV:" + Integer.toString(mech.getAdjustedBV()));

		}
		catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}

}
