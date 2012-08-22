package bt.ui.mapping.planetmap;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Vector;

import bt.elements.galaxy.TerrainType;

public class WorldMapTerrain
{
    private static Vector<Image> TerrainImages = new Vector<Image>();
    private static WorldMapTerrain theInst;

    protected WorldMapTerrain()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        for (int i = 0; i < TerrainImageNames.length; i++)
        {
        	Image img = tk.getImage(TerrainImageNames[i]);
        	while (img.getWidth(null) < 0)
        	{
        		Thread.yield();
        	}
        	TerrainImages.add(img);
        }
    }

    public static WorldMapTerrain getInstance()
    {
        if (theInst == null)
        {
            theInst = new WorldMapTerrain();
        }

        return theInst;
    }
	
    private static String TerrainImageNames[] =
    {
	    "images/Desert.png", "images/Forest.png", "images/Jungle.png", "images/Mountains.png", "images/WoodedHills.png", "images/Hills.png", "images/Plains.png", "images/Swamp.png",
	    "images/Wasteland.png", "images/Scrub.png", "images/Water.png"
    };

	
    public static Color getTerrainBackground(TerrainType index)
    {
        switch (index)
        {
            case DESERT:
                return new Color(252, 244, 54);
            case DEEPFOREST:
            case DEEPJUNGLE:
                return new Color(0, 128, 0);
            case DEEPWATER:
                return new Color(0, 0, 128);
            case MOUNTAINS:
                return new Color(145, 101, 38);
            case FOREST:
            case JUNGLE:
                return new Color(0, 177, 0);
            case WATER:
                return new Color(0, 206, 255);
            case WOODEDHILLS:
            case HILLS:
                return new Color(206, 148, 0);
            case PLAINS:
                return new Color(0, 177, 0);
            case SWAMP:
                return new Color(111, 164, 20);
            case WASTELAND:
                return new Color(148, 148, 148);
            case SCRUB:
                return new Color(99, 148, 99);
            case LANDICE:
                return new Color(232, 232, 232);
            case VOLCANO:
            	return Color.red;
            case LAKE:
            	return new Color(0,0,255);
            case WATERICE:
                return new Color(232, 232, 240);
            case NONE:
            	throw new RuntimeException("getTerrainBackground called with TerrainType.NONE");
        }
        return Color.white;
    }

    public Image getTerrainImage(TerrainType index)
    {
        int image = -1;
        switch (index)
        {
            case DESERT:
                image = 0;
                break;
            case DEEPFOREST:
                image = 1;
                break;
            case DEEPJUNGLE:
                image = 2;
                break;
            case DEEPWATER:
                image = 10;
                break;
            case MOUNTAINS:
                image = 3;
                break;
            case FOREST:
                image = 1;
                break;
            case JUNGLE:
                image = 2;
                break;
            case WATER:
                image = 10;
                break;
            case WOODEDHILLS:
                image = 4;
                break;
            case HILLS:
                image = 5;
                break;
            case PLAINS:
                image = 6;
                break;
            case SWAMP:
                image = 7;
                break;
            case WASTELAND:
                image = 8;
                break;
            case SCRUB:
                image = 9;
                break;
            case LANDICE:
                image = 10;
                break;
            case VOLCANO:
                image = 3;
                break;
            case LAKE:
            	image = 10;
            	break;
            case WATERICE:
                image = 10;
                break;
            case NONE:
            	throw new RuntimeException("getTerrainImage called with TerrainType.NONE");
        }

        if (image == -1)
        {
            return null;
        }

        return TerrainImages.elementAt(image);
    }

}
