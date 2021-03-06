package bt.ui.renderers;

import java.awt.BasicStroke;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.imageio.ImageIO;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import bt.elements.Battlemech;
import bt.elements.BattlemechDamageNotation;
import bt.elements.BattlemechSection;
import bt.elements.InternalSlotStatus;
import bt.elements.ItemMount;
import bt.elements.ItemStatus;
import bt.elements.SectionStatus;
import bt.elements.Weapon;
import bt.elements.design.BattlemechDesign;
import bt.elements.personnel.Mechwarrior;
import bt.managers.DesignManager;
import bt.ui.filters.TransparentColorFilter;
import bt.util.ExceptionUtil;
import bt.util.IndexedRectangle;
import bt.util.PropertyUtil;

public class BattlemechRenderer
{
    private static BattlemechRenderer theInstance = new BattlemechRenderer();

    private BufferedImage _MechDiagram;
    private HashMap<String, HashMap<Integer, Point>> _InternalDots = new HashMap<String, HashMap<Integer, Point>>();
    private HashMap<String, HashMap<Integer, Point>> _ArmourDots = new HashMap<String, HashMap<Integer, Point>>();
    private HashMap<String, HashMap<Integer, HashMap<Integer, Point>>> _CriticalTablePoints = new HashMap<String, HashMap<Integer,HashMap<Integer,Point>>>();
    private HashMap<String, Rectangle> _CriticalTableAreas = new HashMap<String, Rectangle>();
    private HashMap<String, Point> _NamedPoints = new HashMap<String, Point>();
    private HashMap<Integer, Point> _WeaponPoints = new HashMap<Integer, Point>();
    private HashMap<Integer, Point> _HeatSinkDots = new HashMap<Integer, Point>();
    
    
    private HashMap<String, HashMap<String, Integer>> _DotSizes = new HashMap<String, HashMap<String, Integer>>();
    
    private Font _CriticalSlotFont = new Font("SansSerif",Font.PLAIN,14);
    private Font _InformationFont = new Font("SansSerif",Font.BOLD,14);
    private Font _SmallInformationFont = new Font("SansSerif",Font.PLAIN,11);
    private Font _LargeInformationFont = new Font("SansSerif",Font.BOLD,16);

    private final int extraSmallDot = 6;
    private final int smallDot = 8;
    private final int mediumDot = 10;
    private final int largeDot = 11;
    private final int extraLargeDot = 13;
    private final int superLargeDot = 18;

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
        	System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
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


    public BufferedImage RenderBattlemech(Battlemech mech, Mechwarrior warrior)
    {
    	return RenderBattlemech(mech, warrior, 1);
    }
    
    public BufferedImage RenderBattlemech(Battlemech mech, Mechwarrior warrior, double scale)
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
        	ArrayList<Point> points = new ArrayList<Point>();
        	for (InternalSlotStatus iss : im.getSlotReferences())
        	{
    			String mountText = im.getMountedItem().toString();
				if (iss.getRearFacing())
					mountText += " (R)";
				
				Point p = _CriticalTablePoints.get(iss.getInternalLocation()).get(iss.getTable()).get(iss.getSlot());
				drawCriticalSlotItem(g, p.x, p.y, mountText, iss.getStatus());
				points.add(new Point(p.x - 10, p.y));
        	}
        	if (im.getMountedItem() instanceof Weapon && points.size() > 1)
        	{
        		java.util.Collections.sort(points, new SortPoint());
        		g.setColor(Color.BLACK);
        		g.setStroke(new BasicStroke(3F));
        		Point p = points.get(0);
    			String mountText = im.getMountedItem().toString();
    			int verticalAdjust = (int)_CriticalSlotFont.getLineMetrics(mountText, g.getFontRenderContext()).getAscent();
        		g.drawLine(p.x, p.y - verticalAdjust , p.x+ 5, p.y - verticalAdjust);
        		Point p2 = points.get(points.size() - 1);
        		g.drawLine(p.x, p.y - verticalAdjust, p2.x, p2.y);
        		g.drawLine(p2.x, p2.y, p2.x+ 5, p2.y);        		
        	}
        }
        g.setFont(_InformationFont);
        drawInformation(g,mech.getDesignVariant() + " " + mech.getDesignName(), _NamedPoints.get("Type"), null);
        drawInformation(g,Integer.toString(mech.getWeight()), _NamedPoints.get("Tonnage"), null);
        drawInformation(g,Integer.toString(mech.getWalkRating()), _NamedPoints.get("Walking"), null);
        drawInformation(g,Integer.toString(mech.getRunRating()), _NamedPoints.get("Running"), null);
        drawInformation(g,Integer.toString(mech.getJumpRating()), _NamedPoints.get("Jumping"), null);
        drawInformation(g,Integer.toString(DesignManager.getInstance().Design(mech.getVariantName()).getBV()), _NamedPoints.get("BattleValue"), null);
        drawInformation(g,Integer.toString(mech.getTotalHeatSinks()), _NamedPoints.get("HeatSinkTotal"), null);
        
        g.setFont(_SmallInformationFont);
        int WeaponIndex = 1;
        for (ItemMount im : mech.getAllWeaponMounts())
        {
			Point p = _WeaponPoints.get(WeaponIndex);
			drawInformation(g, im.toString(), p, im.getWorstStatus());
			WeaponIndex++;
			if (WeaponIndex > 16) break;
        }
        if (WeaponIndex <= 16)
        {
            for (ItemMount im : mech.getAllAmmunitionMounts())
            {
    			Point p = _WeaponPoints.get(WeaponIndex);
    			drawInformation(g, im.toString(), p, im.getWorstStatus());
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

        g.setColor(Color.BLACK);
        for (BattlemechSection section : mech.getSectionStatuses().keySet())
        {
        	if (_CriticalTableAreas.containsKey(section.GetName()))
        	{
        		SectionStatus status = mech.getSectionStatuses().get(section);
				
        		switch (status.getStatus())
        		{
        			case BLOWNOFF:
					case DAMAGED:
					case DESTROYED:
					case JURYRIGGED:
		        		Rectangle rect = _CriticalTableAreas.get(section.GetName());
						g.drawRect(rect.x, rect.y, rect.width, rect.height);
						g.drawLine(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height);
						g.drawLine(rect.x + rect.width, rect.y, rect.x, rect.y + rect.height);
//						Point p = new Point(rect.x, rect.y + (rect.height / 2));
						Point p = new Point(rect.x, rect.y - 2);
						g.setFont(_LargeInformationFont);

        				drawInformation(g, status.getStatus().toString(), p, null);
						break;
					case OK:
					default:
						break;
        		}
        	}
        }
        
        int engineHits = mech.getEngineHits();
        if (engineHits > 0)
        {
        	for (int i = 1; i <= engineHits; i++)
        	{
        		String name = "Engine-" + Integer.toString(i);
        		Point p = _NamedPoints.get(name);
        		drawDotStatus(g, p.x, p.y, superLargeDot, ItemStatus.DESTROYED);
        	}
        }
        
        int gyroHits = mech.getGyroHits();
        if (gyroHits > 0)
        {
        	for (int i = 1; i <= gyroHits; i++)
        	{
        		String name = "Gyro-" + Integer.toString(i);
        		Point p = _NamedPoints.get(name);
        		drawDotStatus(g, p.x, p.y, superLargeDot, ItemStatus.DESTROYED);
        	}        	
        }
        
        int sensorHits = mech.getSensorHits();
        if (sensorHits > 0)
        {
        	for (int i = 1; i <= sensorHits; i++)
        	{
        		String name = "Sensors-" + Integer.toString(i);
        		Point p = _NamedPoints.get(name);
        		drawDotStatus(g, p.x, p.y, superLargeDot, ItemStatus.DESTROYED);
        	}
        }
        
        if (mech.isLifeSupportHit())
        {
    		Point p = _NamedPoints.get("Life Support");
    		drawDotStatus(g, p.x, p.y, superLargeDot, ItemStatus.DESTROYED);
        }
        
        if (warrior != null)
        {
            g.setFont(_LargeInformationFont);
    		drawInformation(g, warrior.getName(), _NamedPoints.get("PilotName"), null);
    		drawInformation(g, Integer.toString(warrior.getGunnerySkill()), _NamedPoints.get("GunnerySkill"), null);
    		drawInformation(g, Integer.toString(warrior.getPilotingSkill()), _NamedPoints.get("PilotingSkill"), null);
    		
    		for (int i = 1; i <= Math.min(warrior.getHits(), 6); i++)
    		{
        		Point p = _NamedPoints.get("Pilot Hit-" + Integer.toString(i));
        		drawDotStatus(g, p.x, p.y, superLargeDot, ItemStatus.DESTROYED);
    		}
        }
        
        return RenderedImage;
    }

    public BufferedImage RenderBattlemechDamage(Battlemech mech, double scale, Vector<BattlemechDamageNotation> damageNotations)
    {
        BufferedImage RenderedImage = new BufferedImage(_MechDiagram.getWidth(), _MechDiagram.getHeight(), BufferedImage.TYPE_INT_ARGB);
        if (mech == null) return RenderedImage;

        Graphics2D g = (Graphics2D)RenderedImage.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, _MechDiagram.getWidth(), _MechDiagram.getHeight());
        g.scale(scale, scale);
        
        Vector<BattlemechDamageNotation> internalNotations = BattlemechDamageNotation.getDamageNotationsForArea("Internals", damageNotations);
        for (BattlemechDamageNotation internalNotation : internalNotations)
        {
            int dotSize = _DotSizes.get("Internals").get(internalNotation.getLocation());
            Point p = _InternalDots.get(internalNotation.getLocation()).get(internalNotation.getIndex());

            drawDotStatus(g, p.x, p.y, dotSize, internalNotation.getStatus());
        }
                
        Vector<BattlemechDamageNotation> armourNotations = BattlemechDamageNotation.getDamageNotationsForArea("Armour", damageNotations);
        for (BattlemechDamageNotation armourNotation : armourNotations)
        {
            int dotSize = _DotSizes.get("Armour").get(armourNotation.getLocation());
            Point p = _ArmourDots.get(armourNotation.getLocation()).get(armourNotation.getIndex());

            drawDotStatus(g, p.x, p.y, dotSize, armourNotation.getStatus());
        }

        
        Vector<BattlemechDamageNotation> heatsinkNotations = BattlemechDamageNotation.getDamageNotationsForArea("HeatSinks", damageNotations);
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
        		System.out.println("Failed to find ItemMount " + equipmentNotation.toString());
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
    
    private void drawInformation(Graphics2D g, String text, Point p, ItemStatus status)
    {
    	g.drawString(text, p.x, p.y);
    	
    	if (status == null)
    		return;
    	
    	int xOff = p.x;
    	int yOff = p.y;
    	Rectangle2D fontRect = g.getFontMetrics().getStringBounds(text, g);

    	switch (status)
    	{
    	case DAMAGED:
            g.setColor(Color.red);
    		g.setStroke(new BasicStroke(3F));
    		g.drawLine(xOff, (int)(yOff - (fontRect.getHeight() / 4)), (int)(xOff + fontRect.getWidth()), (int)(yOff - (fontRect.getHeight() / 4)));
    		break;
    	case DESTROYED:
            g.setColor(Color.black);
    		g.setStroke(new BasicStroke(3F));
    		g.drawLine(xOff, (int)(yOff - (fontRect.getHeight() / 4)), (int)(xOff + fontRect.getWidth()), (int)(yOff - (fontRect.getHeight() / 4)));
    		break;
		case JURYRIGGED:
            g.setColor(Color.yellow);
    		g.setStroke(new BasicStroke(3F));
    		g.drawLine(xOff, (int)(yOff - (fontRect.getHeight() / 4)), (int)(xOff + fontRect.getWidth()), (int)(yOff - (fontRect.getHeight() / 4)));
			break;
		case OK:
			break;
		case REPAIRED:
            g.setColor(Color.green);
    		g.setStroke(new BasicStroke(3F));
    		g.drawLine(xOff, (int)(yOff - (fontRect.getHeight() / 4)), (int)(xOff + fontRect.getWidth()), (int)(yOff - (fontRect.getHeight() / 4)));
			break;
		default:
			break;
    	}
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
                g.setColor(Color.red);
                g.fill(new Ellipse2D.Double(x, y, dotSize, dotSize));
                break;
			case JURYRIGGED:
                g.setColor(Color.yellow);
                g.fill(new Ellipse2D.Double(x, y, dotSize, dotSize));
				break;
			case OK:
				break;
			case REPAIRED:
                g.setColor(Color.green);
                g.fill(new Ellipse2D.Double(x, y, dotSize, dotSize));
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
            g.setColor(Color.red);
    		g.setStroke(new BasicStroke(3F));
    		g.drawLine(xOff, (int)(yOff - (fontRect.getHeight() / 4)), (int)(xOff + fontRect.getWidth()), (int)(yOff - (fontRect.getHeight() / 4)));
    		break;
    	case DESTROYED:
            g.setColor(Color.black);
    		g.setStroke(new BasicStroke(3F));
    		g.drawLine(xOff, (int)(yOff - (fontRect.getHeight() / 4)), (int)(xOff + fontRect.getWidth()), (int)(yOff - (fontRect.getHeight() / 4)));
    		break;
		case JURYRIGGED:
            g.setColor(Color.yellow);
    		g.setStroke(new BasicStroke(3F));
    		g.drawLine(xOff, (int)(yOff - (fontRect.getHeight() / 4)), (int)(xOff + fontRect.getWidth()), (int)(yOff - (fontRect.getHeight() / 4)));
			break;
		case OK:
			break;
		case REPAIRED:
            g.setColor(Color.green);
    		g.setStroke(new BasicStroke(3F));
    		g.drawLine(xOff, (int)(yOff - (fontRect.getHeight() / 4)), (int)(xOff + fontRect.getWidth()), (int)(yOff - (fontRect.getHeight() / 4)));
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
        
        iter = rootElement.getChild("CriticalTableAreas").getChildren("Area").iterator();
        while (iter.hasNext())
        {
        	org.jdom.Element dotElement = (org.jdom.Element)iter.next();
            String locationName = dotElement.getAttributeValue("location");
            Integer x = Integer.parseInt(dotElement.getAttributeValue("x"));
            Integer y = Integer.parseInt(dotElement.getAttributeValue("y"));
            Integer width = Integer.parseInt(dotElement.getAttributeValue("width"));
            Integer height = Integer.parseInt(dotElement.getAttributeValue("height"));
            
            _CriticalTableAreas.put(locationName, new Rectangle(x,y,width,height));
        }

        
        _DotSizes.put("Internals", new HashMap<String, Integer>());
        _DotSizes.get("Internals").put("Head", smallDot);
        _DotSizes.get("Internals").put("Left Arm", smallDot);
        _DotSizes.get("Internals").put("Right Arm", smallDot);
        _DotSizes.get("Internals").put("Left Leg", smallDot);
        _DotSizes.get("Internals").put("Right Leg", smallDot);
        _DotSizes.get("Internals").put("Left Front Leg", smallDot);
        _DotSizes.get("Internals").put("Right Front Leg", smallDot);
        _DotSizes.get("Internals").put("Left Rear Leg", smallDot);
        _DotSizes.get("Internals").put("Right Rear Leg", smallDot);
        _DotSizes.get("Internals").put("Left Torso", extraSmallDot);
        _DotSizes.get("Internals").put("Right Torso", extraSmallDot);
        _DotSizes.get("Internals").put("Centre Torso", smallDot);
        _DotSizes.put("Armour", new HashMap<String, Integer>());
        _DotSizes.get("Armour").put("Head", smallDot);
        _DotSizes.get("Armour").put("Left Arm", extraLargeDot);
        _DotSizes.get("Armour").put("Right Arm", extraLargeDot);
        _DotSizes.get("Armour").put("Left Leg", extraLargeDot);
        _DotSizes.get("Armour").put("Right Leg", extraLargeDot);
        _DotSizes.get("Armour").put("Left Front Leg", extraLargeDot);
        _DotSizes.get("Armour").put("Right Front Leg", extraLargeDot);
        _DotSizes.get("Armour").put("Left Rear Leg", extraLargeDot);
        _DotSizes.get("Armour").put("Right Rear Leg", extraLargeDot);
        _DotSizes.get("Armour").put("Left Torso", extraSmallDot);
        _DotSizes.get("Armour").put("Right Torso", extraSmallDot);
        _DotSizes.get("Armour").put("Centre Torso", largeDot);
        _DotSizes.get("Armour").put("Left Torso Rear", mediumDot);
        _DotSizes.get("Armour").put("Right Torso Rear", mediumDot);
        _DotSizes.get("Armour").put("Centre Torso Rear", mediumDot);


    }
    
    class SortPoint implements Comparator<Point>
    {
		@Override
		public int compare(Point o1, Point o2) 
		{
			return Integer.compare(o1.y, o2.y);
		}
    	
    }
}
