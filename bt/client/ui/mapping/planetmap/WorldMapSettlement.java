package bt.client.ui.mapping.planetmap;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.Vector;

import bt.common.elements.galaxy.SettlementType;

public class WorldMapSettlement
{
    private static Vector<Image> SettlementImages = new Vector<Image>();
    private static WorldMapSettlement theInst;

    private WorldMapSettlement()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        for (int i = 0; i < SettlementImageNames.length; i++)
        {
        	Image img = tk.createImage(SettlementImageNames[i]);
        	while (img.getWidth(null) < 0)
        	{
        		Thread.yield();
        	}
        	SettlementImages.add(img);
        }
    }

    public static WorldMapSettlement getInstance()
    {
        if (theInst == null)
        {
            theInst = new WorldMapSettlement();
        }

        return theInst;
    }
	
    private static String SettlementImageNames[] =
    {
	    "images/Capitol.png", "images/Spaceport.png", "images/City.png", "images/Town.png", 
	    "images/Settlement.png", "images/Mine.png"
    };

	

    public Image getSettlementImage(SettlementType index)
    {
        return SettlementImages.elementAt(index.ordinal() - 1);
    }

}
