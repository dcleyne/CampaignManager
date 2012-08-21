package bt.test;

import java.awt.image.BufferedImage;

import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;


import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

import bt.common.display.MapLayoutRenderer;
import bt.common.elements.mapping.MapSet;
import bt.common.util.ExceptionUtil;
import bt.common.util.PropertyUtil;
import bt.server.managers.MapManager;

public class TestMapLayoutRenderer 
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

            Vector<MapSet> mapSets = MapManager.getInstance().getMapSets();
            for (MapSet mapSet : mapSets)
            {
	            BufferedImage image = MapLayoutRenderer.getInstance().renderMapLayout(mapSet);
	            
	            System.out.println(mapSet.getName());
	            
	            String filename = "C:/temp/" + mapSet.getName().replace('/', '-') + ".png";
	            ImageIO.write(image, "PNG", new File(filename));
	            
	            System.out.println("Rendered Ok " + mapSet.getName());
	            //System.exit(0);
            }
		}
		catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}

}
