package bt.mapping;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import bt.elements.galaxy.SettlementType;
import bt.util.ExceptionUtil;
import bt.util.ImageUtil;

public enum TerrainFactory
{
	INSTANCE;
	
    private ArrayList<BufferedImage> _TerrainImages;
    private ArrayList<BufferedImage> _SettlementImages;

    private String[] _TerrainImageNames =
    {
	    "Desert.png", "Forest.png", "Jungle.png", "Mountains.png", "WoodedHills.png", "Hills.png", "Plains.png", "Swamp.png",
	    "Wasteland.png", "Scrub.png", "Water.png"
    };
    private String[] _SettlementImageNames =
    {
	    "Capitol.png", "Spaceport.png", "City.png", "Town.png", 
	    "Settlement.png", "Mine.png"
    };



    private TerrainFactory()
    {
    	try
    	{
	    	_TerrainImages = ImageUtil.loadImages(_TerrainImageNames);
	    	_SettlementImages = ImageUtil.loadImages(_SettlementImageNames);
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
    	}
    }


	
    public Color getTerrainBackground(TerrainType index)
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

    public BufferedImage getTerrainImage(TerrainType index)
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

        return _TerrainImages.get(image);
    }

    public BufferedImage getSettlementImage(SettlementType settlementType)
    {
        return _SettlementImages.get(settlementType.ordinal() - 1);
    }

}
