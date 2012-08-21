package bt.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

import bt.common.display.BattlemechRenderer;
import bt.common.elements.Battlemech;
import bt.common.elements.design.BattlemechDesign;
import bt.common.util.ExceptionUtil;
import bt.common.util.PropertyUtil;
import bt.server.managers.BattlemechManager;
import bt.server.managers.DesignManager;

public class TestBattlemechRenderer
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
	        Vector<String> designs = dm.getDesignNames();
	        
	        for (String designName : designs)
	        {
	            try
	            {
	                BattlemechDesign design = dm.Design(designName);
	            	//BattlemechDesign design = dm.Design("STK-3F Stalker");
	                Battlemech mech = bm.createBattlemechFromDesign(design);
	                
	                System.out.println(design.getName());
	                
	                String filename = "C:/temp/" + mech.getDesignVariant() + " " + mech.getDesignName() + ".png";
	                BufferedImage mechImage = BattlemechRenderer.getInstance().RenderBattlemech(mech);
	                ImageIO.write(mechImage, "PNG", new File(filename));
	                
	                System.out.println("Rendered Ok " + mech.getDesignName() + " " + mech.getDesignVariant());
	                //System.exit(0);
	            }
	            catch (Exception ex)
	            {
                    System.out.println("FAILED on " + designName);
                    System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
	            }
	        }
            //String filename = "C:/temp/blankMechImage.png";
            //BufferedImage mechImage = BattlemechRenderer.getInstance().RenderBattlemech(null);
            //ImageIO.write(mechImage, "PNG", new File(filename));
		}
		catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}

}
