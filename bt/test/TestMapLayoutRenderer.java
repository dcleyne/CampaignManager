package bt.test;

import java.awt.image.BufferedImage;

import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;


import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

import bt.elements.mapping.MapSet;
import bt.managers.MapManager;
import bt.ui.renderers.MapLayoutRenderer;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;

public class TestMapLayoutRenderer 
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

            Vector<MapSet> mapSets = MapManager.getInstance().getMapSets();
            for (MapSet mapSet : mapSets)
            {
	            BufferedImage image = MapLayoutRenderer.getInstance().renderMapLayout(mapSet);
	            
	            System.out.println(mapSet.getName());
	            
	            String filename = "/tmp/" + mapSet.getName().replace('/', '-') + ".png";
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
