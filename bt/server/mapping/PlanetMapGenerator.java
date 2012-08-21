package bt.server.mapping;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bt.common.elements.galaxy.InnerSpherePlanet;
import bt.common.elements.galaxy.PlanetMap;
import bt.common.elements.galaxy.SolarSystemDetails;
import bt.common.ui.mapping.planetmap.WorldMap;
import bt.common.util.ExceptionUtil;
import bt.server.managers.SolarSystemManager;

public class PlanetMapGenerator 
{
    private static Log log = LogFactory.getLog(PlanetMapGenerator.class);

	public PlanetMapGenerator()
	{
		
	}
	
	public void generatePlanetMap(InnerSpherePlanet planet, SolarSystemDetails ssd, boolean generateTerrain, boolean generateSettlements, boolean generateRoads, boolean forceCreate, boolean compress)
	{
		
		if (ssd.isMapGenerated() && !forceCreate)
		{
			log.info("Map Already generated for " + planet.toString());
			return;
		}
		
		log.info("Generating Map for " + planet.toString());
		ssd.generateRandomPlanet(generateTerrain,generateSettlements,generateRoads);
		
		log.info("Storing Map for " + planet.toString());
		SolarSystemManager.getInstance().saveSolarSystemDetails(planet, ssd, compress);
		
		log.info("Completed Generating Map for " + planet.toString());
	}
	
	public void generatePlanetMapImage(InnerSpherePlanet planet, SolarSystemDetails ssd, boolean forceCreate)
	{
		String path = "data/planets/maps/";
		File pathFile = new File(path);
		if (!pathFile.exists())
			pathFile.mkdirs();
		
		File f = new File(path + planet.toString() + ".png");
		if (f.exists())
		{
			if (forceCreate)
				f.delete();
			else
				return;
		}
		
		
		PlanetMap pm = ssd.getMap();
		
		if (pm == null) return;
		
		WorldMap wm = new WorldMap(ssd,24);
		Dimension bounds = wm.getMapSize();
		
		try
		{
			log.info("Generating map image for " + planet.toString());
			BufferedImage image = new BufferedImage(bounds.width, bounds.height,BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D)image.getGraphics();
			g.setClip(0, 0, bounds.width, bounds.height);
			wm.draw(g, 0, 0, WorldMap.DrawMode.MAP);		
			ImageIO.write(image, "PNG", f);
		}
		catch (Exception ex)
		{
			log.fatal(ExceptionUtil.getExceptionStackTrace(ex));
		}
		
	}

	public void generatePlanetElevationImage(InnerSpherePlanet planet, SolarSystemDetails ssd, boolean forceCreate)
	{
		String path = "data/planets/elevation/";
		File pathFile = new File(path);
		if (!pathFile.exists())
			pathFile.mkdirs();

		PlanetMap pm = ssd.getMap();
		
		if (pm == null) return;

		File f = new File(path + planet.toString() + ".png");
		if (f.exists())
		{
			if (forceCreate)
				f.delete();
			else
				return;
		}
		
		WorldMap wm = new WorldMap(ssd,24);
		Dimension bounds = wm.getMapSize();
		
		try
		{
			BufferedImage image = new BufferedImage(bounds.width, bounds.height,BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D)image.getGraphics();
			g.setClip(0, 0, bounds.width, bounds.height);

			log.info("Generating elevation image for " + planet.toString());
			wm.draw(g, 0, 0, WorldMap.DrawMode.ELEVATION);		
			ImageIO.write(image, "PNG", f);
		}
		catch (Exception ex)
		{
			log.fatal(ExceptionUtil.getExceptionStackTrace(ex));
		}
		
	}

	public void generatePlanetTemperatureImage(InnerSpherePlanet planet, SolarSystemDetails ssd, boolean forceCreate)
	{
		String path = "data/planets/temperature/";
		File pathFile = new File(path);
		if (!pathFile.exists())
			pathFile.mkdirs();

		PlanetMap pm = ssd.getMap();
		
		if (pm == null) return;
		
		File f = new File(path + planet.toString() + ".png");
		if (f.exists())
		{
			if (forceCreate)
				f.delete();
			else
				return;
		}
		
		WorldMap wm = new WorldMap(ssd,24);
		Dimension bounds = wm.getMapSize();
		
		try
		{
			log.info("Generating map image for " + planet.toString());
			BufferedImage image = new BufferedImage(bounds.width, bounds.height,BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D)image.getGraphics();
			g.setClip(0, 0, bounds.width, bounds.height);
			log.info("Generating temperature image for " + planet.toString());
			wm.draw(g, 0, 0, WorldMap.DrawMode.TEMPERATURE);		

			ImageIO.write(image, "PNG", f);
		}
		catch (Exception ex)
		{
			log.fatal(ExceptionUtil.getExceptionStackTrace(ex));
		}
		
	}
	
}
