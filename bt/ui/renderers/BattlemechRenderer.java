package bt.ui.renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import bt.elements.Battlemech;
import bt.elements.BattlemechDamageNotation;
import bt.elements.InternalSlotStatus;
import bt.elements.ItemMount;
import bt.elements.ItemStatus;
import bt.elements.design.BattlemechDesign;
import bt.ui.filters.TransparentColorFilter;
import bt.util.ExceptionUtil;
import bt.util.IndexedRectangle;
import bt.util.PropertyUtil;

public class BattlemechRenderer
{
    private static Log log = LogFactory.getLog(BattlemechRenderer.class);
    private static BattlemechRenderer theInstance = new BattlemechRenderer();

    private BufferedImage _MechDiagram;
    private HashMap<String, HashMap<Integer, Point>> _InternalDots = new HashMap<String, HashMap<Integer, Point>>();
    private HashMap<String, HashMap<Integer, Point>> _ArmourDots = new HashMap<String, HashMap<Integer, Point>>();
    private HashMap<String, HashMap<Integer, HashMap<Integer, Point>>> _CriticalTablePoints = new HashMap<String, HashMap<Integer,HashMap<Integer,Point>>>();
    private HashMap<String, Point> _NamedPoints = new HashMap<String, Point>();
    private HashMap<Integer, Point> _WeaponPoints = new HashMap<Integer, Point>();
    private HashMap<Integer, Point> _HeatSinkDots = new HashMap<Integer, Point>();
    
    
    private HashMap<String, HashMap<String, Integer>> _DotSizes = new HashMap<String, HashMap<String, Integer>>();
    
    private Font _CriticalSlotFont = new Font("SansSerif",Font.PLAIN,14);
    private Font _InformationFont = new Font("SansSerif",Font.BOLD,14);
    private Font _SmallInformationFont = new Font("SansSerif",Font.PLAIN,11);

    private final int extraSmallDot = 6;
    private final int smallDot = 8;
    private final int mediumDot = 10;
    private final int largeDot = 11;
    private final int extraLargeDot = 13;

    private BattlemechRenderer()
    {
        String DataPath = PropertyUtil.getStringProperty("DataPath", "data");
        String filename = DataPath + "/images/MechSheet.png";
        System.out.println(filename);
        try
        {
	        _MechDiagram = ImageIO.read(new File(filename));
	
	        loadDots(DataPath);
        }
        catch (Exception ex)
        {
        	log.fatal(ExceptionUtil.getExceptionStackTrace(ex));
        }
    }

    public static BattlemechRenderer getInstance()
    { return theInstance; }

    public Image RenderBattlemechDesign(BattlemechDesign design)
    {
        BufferedImage RenderedImage = copyImage(_MechDiagram);
        if (design == null) return RenderedImage;


        return RenderedImage;
    }

    // This method returns a buffered image with the contents of an image
    public BufferedImage copyImage(BufferedImage image, double scale)
    {
        BufferedImage bimage = new BufferedImage((int)(image.getWidth() * scale), (int)(image.getHeight() * scale), image.getType());
        // Copy image to buffered image
        Graphics2D g = bimage.createGraphics();
        g.scale(scale, scale);
    
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
    
        return bimage;
    }

        // This method returns a buffered image with the contents of an image
    public BufferedImage copyImage(BufferedImage image) 
    {
    	return copyImage(image, 1);
    }


    public BufferedImage RenderBattlemech(Battlemech mech)
    {
    	return RenderBattlemech(mech, 1);
    }
    
    public BufferedImage RenderBattlemech(Battlemech mech, double scale)
    {
        BufferedImage RenderedImage = copyImage(_MechDiagram, scale);
        if (mech == null) return RenderedImage;

        Graphics2D g = (Graphics2D)RenderedImage.getGraphics();
        g.scale(scale, scale);

        for (String locationName : mech.getInternals().keySet())
        {
            int dotSize = _DotSizes.get("Internals").get(locationName);
            for (Integer index : mech.getInternals().get(locationName).keySet())
            {
                ItemStatus status = mech.getInternals().get(locationName).get(index);
                Point p = _InternalDots.get(locationName).get(index);

                drawDotStatus(g, p.x, p.y, dotSize, status);
            }
        }
        for (String locationName : mech.getArmour().keySet())
        {
            int dotSize = _DotSizes.get("Armour").get(locationName);
            for (Integer index : mech.getArmour().get(locationName).keySet())
            {
                ItemStatus status = mech.getArmour().get(locationName).get(index);
                Point p = _ArmourDots.get(locationName).get(index);

                drawDotStatus(g, p.x, p.y, dotSize, status);
            }
        }
        g.setFont(_CriticalSlotFont);
        for (ItemMount im : mech.getItems())
        {
        	for (InternalSlotStatus iss : im.getSlotReferences())
        	{
				String mountText = im.getMountedItem().toString();
				if (iss.getRearFacing())
					mountText += " (R)";
				
				Point p = _CriticalTablePoints.get(iss.getInternalLocation()).get(iss.getTable()).get(iss.getSlot());
				drawCriticalSlotItem(g, p.x, p.y, mountText, iss.getStatus());
        	}
        }
        g.setFont(_InformationFont);
        drawInformation(g,mech.getDesignVariant() + " " + mech.getDesignName(), _NamedPoints.get("Type"));
        drawInformation(g,Integer.toString(mech.getWeight()), _NamedPoints.get("Tonnage"));
        drawInformation(g,Integer.toString(mech.getWalkRating()), _NamedPoints.get("Walking"));
        drawInformation(g,Integer.toString(mech.getRunRating()), _NamedPoints.get("Running"));
        drawInformation(g,Integer.toString(mech.getJumpRating()), _NamedPoints.get("Jumping"));
        drawInformation(g,Integer.toString(mech.getAdjustedBV()), _NamedPoints.get("BattleValue"));
        drawInformation(g,Integer.toString(mech.getTotalHeatSinks()), _NamedPoints.get("HeatSinkTotal"));
        
        g.setFont(_SmallInformationFont);
        int WeaponIndex = 1;
        for (ItemMount im : mech.getAllWeaponMounts())
        {
			Point p = _WeaponPoints.get(WeaponIndex);
			drawInformation(g, im.toString(), p);
			WeaponIndex++;
			if (WeaponIndex > 16) break;
        }
        if (WeaponIndex <= 16)
        {
            for (ItemMount im : mech.getAllAmmunitionMounts())
            {
    			Point p = _WeaponPoints.get(WeaponIndex);
    			drawInformation(g, im.toString(), p);
    			WeaponIndex++;
    			if (WeaponIndex > 16) break;
            }
        }
        int HeatSinkIndex = 1;
        int integralHS = mech.getIntegralHeatSinks();
        for (int i = 0; i < integralHS; i++)
        {
        	Point p = _HeatSinkDots.get(HeatSinkIndex);
        	drawDotStatus(g, p.x, p.y, largeDot, ItemStatus.OK);
        	HeatSinkIndex++;
        }
        for (ItemMount im : mech.getAllHeatSinkMounts())
        {
        	for (InternalSlotStatus iss : im.getSlotReferences())
        	{
	        	Point p = _HeatSinkDots.get(HeatSinkIndex);
	        	drawDotStatus(g, p.x, p.y, largeDot, iss.getStatus());
	        	HeatSinkIndex++;
        	}
        }

        return RenderedImage;
    }
    
    private Vector<BattlemechDamageNotation> getDamageNotationsForArea(String area, Vector<BattlemechDamageNotation> damageNotations)
    {
    	Vector<BattlemechDamageNotation> notations = new Vector<BattlemechDamageNotation>();
    	
    	for (BattlemechDamageNotation notation : damageNotations)
    	{
    		if (notation.getArea().equalsIgnoreCase(area))
    			notations.add(notation);
    	}
    	
    	return notations;
    }

    public BufferedImage RenderBattlemechDamage(Battlemech mech, double scale, Vector<BattlemechDamageNotation> damageNotations)
    {
        BufferedImage RenderedImage = new BufferedImage(_MechDiagram.getWidth(), _MechDiagram.getHeight(), _MechDiagram.getType());
        if (mech == null) return RenderedImage;

        Graphics2D g = (Graphics2D)RenderedImage.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, _MechDiagram.getWidth(), _MechDiagram.getHeight());
        g.scale(scale, scale);
        
        Vector<BattlemechDamageNotation> internalNotations = getDamageNotationsForArea("Internals", damageNotations);
        for (BattlemechDamageNotation internalNotation : internalNotations)
        {
            int dotSize = _DotSizes.get("Internals").get(internalNotation.getLocation());
            Point p = _InternalDots.get(internalNotation.getLocation()).get(internalNotation.getIndex());

            drawDotStatus(g, p.x, p.y, dotSize, internalNotation.getStatus());
        }
                
        Vector<BattlemechDamageNotation> armourNotations = getDamageNotationsForArea("Armour", damageNotations);
        for (BattlemechDamageNotation armourNotation : armourNotations)
        {
            int dotSize = _DotSizes.get("Armour").get(armourNotation.getLocation());
            Point p = _ArmourDots.get(armourNotation.getLocation()).get(armourNotation.getIndex());

            drawDotStatus(g, p.x, p.y, dotSize, armourNotation.getStatus());
        }

        
        Vector<BattlemechDamageNotation> heatsinkNotations = getDamageNotationsForArea("HeatSinks", damageNotations);
        for (BattlemechDamageNotation heatsinkNotation : heatsinkNotations)
        {
    		Point p = _HeatSinkDots.get(heatsinkNotation.getIndex());
    		drawDotStatus(g, p.x, p.y, largeDot, heatsinkNotation.getStatus());
        }
        
        g.setFont(_CriticalSlotFont);
        
        Vector<BattlemechDamageNotation> equipmentNotations = new Vector<BattlemechDamageNotation>(damageNotations);
        equipmentNotations.removeAll(internalNotations);
        equipmentNotations.removeAll(armourNotations);
        equipmentNotations.removeAll(heatsinkNotations);

        for (BattlemechDamageNotation equipmentNotation : equipmentNotations)
        {
        	ItemMount im = mech.getItemMount(equipmentNotation.getLocation());
        	if (im != null)
        	{
	    		InternalSlotStatus iss = im.getSlotReferences().get(equipmentNotation.getIndex());
				String mountText = im.getMountedItem().toString();
				if (iss.getRearFacing())
					mountText += " (R)";
				
				Point p = _CriticalTablePoints.get(iss.getInternalLocation()).get(iss.getTable()).get(iss.getSlot());
				drawCriticalSlotItem(g, p.x, p.y, mountText, equipmentNotation.getStatus());
        	}
        	else
        	{
        		log.error("Failed to find ItemMount " + equipmentNotation.toString());
        	}
        }

        g.dispose();
        
        return imageToBufferedImage(makeColorTransparent(RenderedImage, Color.white));
    }
    
    private BufferedImage imageToBufferedImage(Image image) 
    {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return bufferedImage;
    }

    private Image makeColorTransparent(BufferedImage im, final Color color) 
    {
        ImageProducer ip = new FilteredImageSource(im.getSource(), new TransparentColorFilter(color.getRGB()));
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
    
    private void drawInformation(Graphics2D g, String text, Point p)
    {
    	g.drawString(text, p.x, p.y);
    }

    private void drawDotStatus(Graphics2D g, int xOff, int yOff, int dotSize, ItemStatus status)
    {
        int x = xOff - (dotSize / 2);
        int y = yOff - (dotSize / 2);
        g.setColor(Color.black);
        g.draw(new Ellipse2D.Double(x, y, dotSize, dotSize));

        switch (status)
        {
            case DESTROYED:
                g.fill(new Ellipse2D.Double(x, y, dotSize, dotSize));
                break;
            case DAMAGED:
                g.drawLine(x, y, x + dotSize, y + dotSize);
                g.drawLine(x, y + dotSize, x + dotSize, y);
                break;
			case JURYRIGGED:
				break;
			case OK:
				break;
			case REPAIRED:
				break;
			default:
				break;
        }
    }
    
    private void drawCriticalSlotItem(Graphics2D g, int xOff, int yOff, String mountText, ItemStatus status)
    {
        g.setColor(Color.black);
    	Rectangle2D fontRect = g.getFontMetrics().getStringBounds(mountText, g);
    	g.drawString(mountText, xOff, yOff);
    	
    	switch (status)
    	{
    	case DAMAGED:
    	case DESTROYED:
    		g.setStroke(new BasicStroke(3F));
    		g.drawLine(xOff, (int)(yOff - (fontRect.getHeight() / 4)), (int)(xOff + fontRect.getWidth()), (int)(yOff - (fontRect.getHeight() / 4)));
    		break;
		case JURYRIGGED:
			break;
		case OK:
			break;
		case REPAIRED:
			break;
		default:
			break;
    	}
    }
    
    private Rectangle2D.Double createHotspotRectangle(double xOff, double yOff, double size, double scale)
    {
        double x = xOff - (size / 2);
        double y = yOff - (size / 2);
        
        return new Rectangle2D.Double(x * scale, y * scale, size * scale, size * scale);
    }
    
    public HashMap<String, HashMap<String, Vector<IndexedRectangle>>> GenerateBattlemechDiagramHotspots(Graphics2D g, Battlemech mech, double scale)
    {
    	HashMap<String, HashMap<String, Vector<IndexedRectangle>>> hotSpots = new HashMap<String, HashMap<String, Vector<IndexedRectangle>>>();
    	
        if (mech == null) return hotSpots;

        HashMap<String, Vector<IndexedRectangle>> internalHotspots = new HashMap<String, Vector<IndexedRectangle>>();
        for (String locationName : mech.getInternals().keySet())
        {
            int dotSize = _DotSizes.get("Internals").get(locationName);
            Vector<IndexedRectangle> internalHotspotRects = new Vector<IndexedRectangle>();
            for (Integer index : mech.getInternals().get(locationName).keySet())
            {
                Point p = _InternalDots.get(locationName).get(index);
                internalHotspotRects.add(new IndexedRectangle(index,createHotspotRectangle(p.x,p.y,dotSize,scale)));
            }
            internalHotspots.put(locationName, internalHotspotRects);
        }
        hotSpots.put("Internals", internalHotspots);
        
        HashMap<String, Vector<IndexedRectangle>> armourHotspots = new HashMap<String, Vector<IndexedRectangle>>();        
        for (String locationName : mech.getArmour().keySet())
        {
            int dotSize = _DotSizes.get("Armour").get(locationName);
            Vector<IndexedRectangle> armourHotspotRects = new Vector<IndexedRectangle>();
            for (Integer index : mech.getArmour().get(locationName).keySet())
            {
                Point p = _ArmourDots.get(locationName).get(index);
                armourHotspotRects.add(new IndexedRectangle(index,createHotspotRectangle(p.x,p.y,dotSize,scale)));
            }
            armourHotspots.put(locationName, armourHotspotRects);
        }
        hotSpots.put("Armour", armourHotspots);
        
        HashMap<String, Vector<IndexedRectangle>> mountedItemHotspots = new HashMap<String, Vector<IndexedRectangle>>();        
        FontMetrics metrics = g.getFontMetrics(_CriticalSlotFont);
        double hgt = 16.8; // Inferred from the point data
        int baseline = metrics.getAscent();
        for (ItemMount im : mech.getItems())
        {
        	Vector<IndexedRectangle> mountedItemHotspotRects = new Vector<IndexedRectangle>();

        	for (int slotReference = 0; slotReference < im.getSlotReferences().size(); slotReference++)
        	{
        		InternalSlotStatus iss = im.getSlotReferences().get(slotReference);
				String mountText = im.getMountedItem().toString();
				if (iss.getRearFacing())
					mountText += " (R)";
				
				Point p = _CriticalTablePoints.get(iss.getInternalLocation()).get(iss.getTable()).get(iss.getSlot());
		        int adv = metrics.stringWidth(mountText);
				mountedItemHotspotRects.add(new IndexedRectangle(slotReference,new Rectangle2D.Double(p.x * scale, (p.y - baseline) * scale, adv * scale, hgt * scale)));
        	}
        	mountedItemHotspots.put(im.toString(), mountedItemHotspotRects);
        }
        hotSpots.put("MountedItems", mountedItemHotspots);

        
        HashMap<String, Vector<IndexedRectangle>> heatSinkHotspots = new HashMap<String, Vector<IndexedRectangle>>();
        Vector<IndexedRectangle> heatSinkHotspotRects = new Vector<IndexedRectangle>();

        int HeatSinkIndex = 1;
        int integralHS = mech.getIntegralHeatSinks();
        for (int i = 0; i < integralHS; i++)
        {
        	Point p = _HeatSinkDots.get(HeatSinkIndex);
        	heatSinkHotspotRects.add(new IndexedRectangle(HeatSinkIndex,createHotspotRectangle(p.x,p.y,largeDot,scale)));
        	HeatSinkIndex++;
        }
        heatSinkHotspots.put("Internal", heatSinkHotspotRects);
        
        for (ItemMount im : mech.getAllHeatSinkMounts())
        {
            heatSinkHotspotRects = new Vector<IndexedRectangle>();
        	for (int i = 0; i < im.getSlotReferences().size(); i++)
        	{
	        	Point p = _HeatSinkDots.get(HeatSinkIndex);
	        	heatSinkHotspotRects.add(new IndexedRectangle(HeatSinkIndex, createHotspotRectangle(p.x,p.y,largeDot,scale)));	        	
	        	HeatSinkIndex++;
        	}
        	heatSinkHotspots.put(im.toString(), heatSinkHotspotRects);
        }
        
        hotSpots.put("HeatSinks", heatSinkHotspots);

        return hotSpots;
    }


    private void loadDots(String dataPath) throws Exception
    {
        String filename = dataPath + "/BattlemechDiagramLocations.xml";

        SAXBuilder b = new SAXBuilder();
        Document doc = b.build(new File(filename));
        
        org.jdom.Element rootElement = doc.getRootElement();
        Iterator<?> iter = rootElement.getChild("InternalLocations").getChildren("Dot").iterator();
        while (iter.hasNext())
        {
        	org.jdom.Element dotElement = (org.jdom.Element)iter.next();
            String locationName = dotElement.getAttributeValue("location");
            Integer index = Integer.parseInt(dotElement.getAttributeValue("index"));
            Integer x = Integer.parseInt(dotElement.getAttributeValue("x"));
            Integer y = Integer.parseInt(dotElement.getAttributeValue("y"));

            if (!_InternalDots.containsKey(locationName))
                _InternalDots.put(locationName, new HashMap<Integer, Point>());

            _InternalDots.get(locationName).put(index, new Point(x, y));
        }

        iter = rootElement.getChild("ArmourLocations").getChildren("Dot").iterator();
        while (iter.hasNext())
        {
        	org.jdom.Element dotElement = (org.jdom.Element)iter.next();
            String locationName = dotElement.getAttributeValue("location");
            Integer index = Integer.parseInt(dotElement.getAttributeValue("index"));
            Integer x = Integer.parseInt(dotElement.getAttributeValue("x"));
            Integer y = Integer.parseInt(dotElement.getAttributeValue("y"));

            if (!_ArmourDots.containsKey(locationName))
                _ArmourDots.put(locationName, new HashMap<Integer, Point>());

            _ArmourDots.get(locationName).put(index, new Point(x, y));
        }
        
        iter = rootElement.getChild("CriticalTableLocations").getChildren("Point").iterator();
        while (iter.hasNext())
        {
        	org.jdom.Element dotElement = (org.jdom.Element)iter.next();
            String locationName = dotElement.getAttributeValue("location");
            Integer table = Integer.parseInt(dotElement.getAttributeValue("table"));
            Integer slot = Integer.parseInt(dotElement.getAttributeValue("slot"));
            Integer x = Integer.parseInt(dotElement.getAttributeValue("x"));
            Integer y = Integer.parseInt(dotElement.getAttributeValue("y"));
            
            if (!_CriticalTablePoints.containsKey(locationName))
            	_CriticalTablePoints.put(locationName, new HashMap<Integer, HashMap<Integer,Point>>());
            
            if (!_CriticalTablePoints.get(locationName).containsKey(table))
            	_CriticalTablePoints.get(locationName).put(table, new HashMap<Integer, Point>());
            
            _CriticalTablePoints.get(locationName).get(table).put(slot, new Point(x,y));
        }
        iter = rootElement.getChild("NamedLocations").getChildren("Point").iterator();
        while (iter.hasNext())
        {
        	org.jdom.Element dotElement = (org.jdom.Element)iter.next();
            String locationName = dotElement.getAttributeValue("location");
            Integer x = Integer.parseInt(dotElement.getAttributeValue("x"));
            Integer y = Integer.parseInt(dotElement.getAttributeValue("y"));
            
            _NamedPoints.put(locationName, new Point(x,y));
        }   
        iter = rootElement.getChild("WeaponLocations").getChildren("Point").iterator();
        while (iter.hasNext())
        {
        	org.jdom.Element dotElement = (org.jdom.Element)iter.next();
            Integer index = Integer.parseInt(dotElement.getAttributeValue("index"));
            Integer x = Integer.parseInt(dotElement.getAttributeValue("x"));
            Integer y = Integer.parseInt(dotElement.getAttributeValue("y"));

            _WeaponPoints.put(index, new Point(x, y));
        }
        iter = rootElement.getChild("HeatSinkLocations").getChildren("Dot").iterator();
        while (iter.hasNext())
        {
        	org.jdom.Element dotElement = (org.jdom.Element)iter.next();
            Integer index = Integer.parseInt(dotElement.getAttributeValue("index"));
            Integer x = Integer.parseInt(dotElement.getAttributeValue("x"));
            Integer y = Integer.parseInt(dotElement.getAttributeValue("y"));

            _HeatSinkDots.put(index, new Point(x, y));
        }
        
        _DotSizes.put("Internals", new HashMap<String, Integer>());
        _DotSizes.get("Internals").put("Head", smallDot);
        _DotSizes.get("Internals").put("Left Arm", smallDot);
        _DotSizes.get("Internals").put("Right Arm", smallDot);
        _DotSizes.get("Internals").put("Left Leg", smallDot);
        _DotSizes.get("Internals").put("Right Leg", smallDot);
        _DotSizes.get("Internals").put("Left Torso", extraSmallDot);
        _DotSizes.get("Internals").put("Right Torso", extraSmallDot);
        _DotSizes.get("Internals").put("Centre Torso", smallDot);
        _DotSizes.put("Armour", new HashMap<String, Integer>());
        _DotSizes.get("Armour").put("Head", smallDot);
        _DotSizes.get("Armour").put("Left Arm", extraLargeDot);
        _DotSizes.get("Armour").put("Right Arm", extraLargeDot);
        _DotSizes.get("Armour").put("Left Leg", extraLargeDot);
        _DotSizes.get("Armour").put("Right Leg", extraLargeDot);
        _DotSizes.get("Armour").put("Left Torso", extraSmallDot);
        _DotSizes.get("Armour").put("Right Torso", extraSmallDot);
        _DotSizes.get("Armour").put("Centre Torso", largeDot);
        _DotSizes.get("Armour").put("Left Torso Rear", mediumDot);
        _DotSizes.get("Armour").put("Right Torso Rear", mediumDot);
        _DotSizes.get("Armour").put("Centre Torso Rear", mediumDot);


    }

}
