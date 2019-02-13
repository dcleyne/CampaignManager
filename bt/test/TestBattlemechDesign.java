package bt.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;

import bt.elements.Battlemech;
import bt.elements.design.BattlemechDesign;
import bt.managers.BattlemechManager;
import bt.managers.DesignManager;
import bt.ui.renderers.BattlemechRenderer;
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
                    
	                String filename = path + "/battlemechs/" + mech.getDesignVariant() + " " + mech.getDesignName() + ".png";
	                BufferedImage mechImage = BattlemechRenderer.getInstance().RenderBattlemech(mech, null);
	                ImageIO.write(mechImage, "PNG", new File(filename));
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
