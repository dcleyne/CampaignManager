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
	
    private static final Color _WaterIceGrey = new Color(232, 232, 240);
	private static final Color _LakeBlue = new Color(0,0,255);
	private static final Color _LandIceGrey = new Color(232, 232, 232);
	private static final Color _ScrubBrown = new Color(99, 148, 99);
	private static final Color _WastelandGrey = new Color(148, 148, 148);
	private static final Color _SwampGreen = new Color(111, 164, 20);
	private static final Color _HillsGreen = new Color(206, 148, 0);
	private static final Color _WaterBlue = new Color(0, 206, 255);
	private static final Color _ForestGreen = new Color(0, 177, 0);
	private static final Color _DeepForestGreen = new Color(0, 128, 0);
    private static final Color _DeepWaterBlue = new Color(0, 0, 128);
    private static final Color _MountainBrown = new Color(145, 101, 38);
    private static final Color _DesertYellow = new Color(252, 244, 54);
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

    public Color getTextColor(TerrainType index)
    {
        return Color.WHITE;
    }

    public Color getReverseTextColor(TerrainType index)
    {
    	return Color.BLACK;
    }

    public Color getTerrainBackground(TerrainType index)
    {
        switch (index)
        {
            case DESERT:
                return _DesertYellow;
            case DEEPFOREST:
            case DEEPJUNGLE:
                return _DeepForestGreen;
            case DEEPWATER:
                return _DeepWaterBlue;
            case MOUNTAINS:
                return _MountainBrown;
            case FOREST:
            case JUNGLE:
                return _ForestGreen;
            case WATER:
                return _WaterBlue;
            case WOODEDHILLS:
            case HILLS:
                return _HillsGreen;
            case PLAINS:
                return _ForestGreen;
            case SWAMP:
                return _SwampGreen;
            case WASTELAND:
                return _WastelandGrey;
            case SCRUB:
                return _ScrubBrown;
            case LANDICE:
                return _LandIceGrey;
            case VOLCANO:
            	return Color.red;
            case LAKE:
            	return _LakeBlue;
            case WATERICE:
                return _WaterIceGrey;
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
