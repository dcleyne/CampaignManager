package bt.managers;

import java.util.HashMap;

import java.util.Iterator;
import java.util.Vector;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;

import bt.elements.design.BasicDesignItem;
import bt.elements.design.BattlemechDesign;
import bt.elements.design.DesignAmmunition;
import bt.elements.design.DesignItem;
import bt.elements.design.DesignWeapon;
import bt.elements.design.EngineDesign;
import bt.elements.design.HeatSinkDesign;
import bt.elements.design.InternalSlotReference;
import bt.elements.design.JumpJetDesign;
import bt.elements.design.SensorsDesign;
import bt.util.ExtensionFileFilter;
import bt.util.PropertyUtil;

import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.Document;
import org.jdom.Element;

public class DesignManager
{
    public interface ItemLoadHandler 
	{
		DesignItem loadItem(Element element);
	}
	
	public interface ItemSaveHandler 
	{
		void saveItem(DesignItem di, Element element);
	}
	
    private static DesignManager theInstance = new DesignManager();

    private String _DataPath = PropertyUtil.getStringProperty("DataPath", "data") + "/design/battlemech/";
    private HashMap<String, BattlemechDesign> _Designs = new HashMap<String, BattlemechDesign>();

    private DesignManager()
    {
    	File dataPath = new File(_DataPath);
    	FilenameFilter ff = new ExtensionFileFilter("xml");
        String[] files =  dataPath.list(ff);
        for (String fileName : files)
        {
        	try
        	{
        		BattlemechDesign design = loadBattlemechDesign(_DataPath + fileName);
                _Designs.put(design.getVariantName(), design);
        	}
        	catch (Exception ex)
        	{
        		ex.printStackTrace();
        	}
        }
    }

    public static DesignManager getInstance()
    {
        return theInstance;
    }

    public Vector<String> getDesignNames()
    {
        return new Vector<String>(_Designs.keySet());
    }

    public BattlemechDesign Design(String designName)
    {
        return _Designs.get(designName);
    }

    public BattlemechDesign[] Designs()
    {
        return (BattlemechDesign[]) new Vector<BattlemechDesign>(_Designs.values()).toArray();
    }

    private BattlemechDesign loadBattlemechDesign(String filename) throws Exception
    {
        HashMap<String, ItemLoadHandler> handlers = RegisterItemLoadHandlers();

        BattlemechDesign design = new BattlemechDesign();

        SAXBuilder b = new SAXBuilder();
        Document doc = b.build(new File(filename));

        Element designElement = doc.getRootElement();

        design.setName(designElement.getChild("Name").getTextTrim());
        design.setVariant(designElement.getChild("Variant").getTextTrim());
        design.setManufacturer(designElement.getChild("Manufacturer").getTextTrim());
        design.setType(BattlemechDesign.Type.fromString(designElement.getChild("Type").getTextTrim()));
        design.setWeight(Integer.parseInt(designElement.getChild("Weight").getTextTrim()));
        design.setCost(Integer.parseInt(designElement.getChild("Cost").getTextTrim()));
        design.setBV(Integer.parseInt(designElement.getChild("BV").getTextTrim()));

        Iterator<?> itr = designElement.getChild("Armour").getChildren("Location").iterator();
        while(itr.hasNext())
        {
        	Element armourElement = (Element)itr.next();
            String Location = armourElement.getAttributeValue("Name");
            int Points = Integer.parseInt(armourElement.getAttributeValue("Points"));
            design.getArmour().put(Location,Points);
        }

        itr = designElement.getChild("Items").getChildren("Item").iterator();
        while(itr.hasNext())
        {
        	Element itemElement = (Element)itr.next();
            String type = itemElement.getAttributeValue("Type");
            DesignItem di;
            if (handlers.containsKey(type))
                di = handlers.get(type).loadItem(itemElement);
            else
                di = new BasicDesignItem(type);

            loadItemBaseElements(itemElement, di);
            loadCritSlotReferences(itemElement, di);
            design.getItems().add(di);
        }

        return design;
    }

    private HashMap<String,ItemLoadHandler> RegisterItemLoadHandlers()
    {
        HashMap<String, ItemLoadHandler> handlers = new HashMap<String, ItemLoadHandler>();

        handlers.put("Sensors", new ItemLoadHandler(){public DesignItem loadItem(Element element){return loadSensors(element);} });
        handlers.put("Engine", new ItemLoadHandler(){public DesignItem loadItem(Element element){return loadEngine(element);} });
        handlers.put("Jump Jet", new ItemLoadHandler(){public DesignItem loadItem(Element element){return loadJumpJet(element);} });
        handlers.put("Weapon", new ItemLoadHandler(){public DesignItem loadItem(Element element){return loadWeapon(element);} });
        handlers.put("Ammunition", new ItemLoadHandler(){public DesignItem loadItem(Element element){return loadAmmunition(element);} });
        handlers.put("Heat Sink", new ItemLoadHandler(){public DesignItem loadItem(Element element){return loadHeatSink(element);} });

        return handlers;
    }

    private void loadItemBaseElements(Element element, DesignItem item)
    {
        if (element.getAttribute("Manufacturer") != null)
            item.setManufacturer(element.getAttributeValue("Manufacturer"));

        if (element.getAttribute("Model") != null)
            item.setModel(element.getAttributeValue("Model"));
    }

    private void loadCritSlotReferences(Element element, DesignItem item)
    {

        Iterator<?> itr = (element.getChildren("SlotReference")).iterator();
        while(itr.hasNext())
        {
        	Element slotReferenceElement = (Element)itr.next();
            InternalSlotReference isr = new InternalSlotReference();
            isr.setInternalLocation(slotReferenceElement.getAttributeValue("Location"));
            isr.setTable(Integer.parseInt(slotReferenceElement.getAttributeValue("Table")));
            isr.setSlot(Integer.parseInt(slotReferenceElement.getAttributeValue("Slot")));
            if (slotReferenceElement.getAttribute("RearFacing") != null)
                isr.setRearFacing(Boolean.parseBoolean(slotReferenceElement.getAttributeValue("RearFacing")));

            item.getSlotReferences().add(isr);
        }
    }

    private DesignItem loadSensors(Element element)
    {
        return new SensorsDesign();
    }


    private DesignItem loadEngine(Element element)
    {
        EngineDesign ed = new EngineDesign();
        ed.setRating(Integer.parseInt(element.getAttributeValue("Rating")));
        ed.setWeight(Double.parseDouble(element.getAttributeValue("Weight")));
        if (element.getAttribute("HeatSinks") != null)
        	ed.setHeatSinks(Integer.parseInt(element.getAttributeValue("HeatSinks")));

        return ed;
    }

    private DesignItem loadJumpJet(Element element)
    {
        JumpJetDesign jjd = new JumpJetDesign();
        jjd.setWeight(Double.parseDouble(element.getAttributeValue("Weight")));

        return jjd;
    }

    private DesignItem loadWeapon(Element element)
    {
        DesignWeapon dw = new DesignWeapon();
        dw.setWeaponType(element.getAttributeValue("WeaponType"));

        return dw;
    }

    private DesignItem loadAmmunition(Element element)
    {
        DesignAmmunition da = new DesignAmmunition();
        da.setWeaponType(element.getAttributeValue("WeaponType"));
        da.setShots(Integer.parseInt(element.getAttributeValue("Shots")));
        if (element.getAttribute("Weight") != null)
            da.setWeight(Double.parseDouble(element.getAttributeValue("Weight")));
        return da;
    }

    private DesignItem loadHeatSink(Element element)
    {
        HeatSinkDesign hsd = new HeatSinkDesign();
        if (element.getAttribute("HeatSinkType") != null)
            hsd.setHeatSinkType(element.getAttributeValue("HeatSinkType"));

        return hsd;
    }

    public void saveBattlemechDesign(BattlemechDesign design) throws Exception
    {
        HashMap<String, ItemSaveHandler> handlers = RegisterItemSaveHandlers();

        Document doc = new Document();

        org.jdom.Element battlemechNode = new org.jdom.Element("BattlemechDesign");
        doc.setRootElement(battlemechNode);

        battlemechNode.addContent(new org.jdom.Element("Name").setText(design.getName()));
        battlemechNode.addContent(new org.jdom.Element("Variant").setText(design.getVariant()));
        battlemechNode.addContent(new org.jdom.Element("Manufacturer").setText(design.getManufacturer()));
        battlemechNode.addContent(new org.jdom.Element("Type").setText(design.getType().toString()));
        battlemechNode.addContent(new org.jdom.Element("Weight").setText(Integer.toString(design.getWeight())));
        battlemechNode.addContent(new org.jdom.Element("Cost").setText(Integer.toString(design.getCost())));
        battlemechNode.addContent(new org.jdom.Element("BV").setText(Integer.toString(design.getBV())));

        Element armourElement = new org.jdom.Element("Armour");
        for (String location : design.getArmour().keySet())
        {
            Element locationElement = new org.jdom.Element("Location");
            locationElement.setAttribute("Name",location);
            locationElement.setAttribute("Points", Integer.toString(design.getArmour().get(location)));
            armourElement.addContent(locationElement);
        }
        battlemechNode.addContent(armourElement);

        Element itemsElement = new org.jdom.Element("Items");
        for (DesignItem di : design.getItems())
        {
            Element itemElement = new org.jdom.Element("Item");
            itemElement.setAttribute("Type", di.getType());
            if (handlers.containsKey(di.getType()))
                handlers.get(di.getType()).saveItem(di, itemElement);

            saveItemBaseElements(itemElement, di);
            saveCritSlotReferences(itemElement, di);
            itemsElement.addContent(itemElement);
        }
        battlemechNode.addContent(itemsElement);

        String filename = _DataPath + design.getVariantName() + ".xml";
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());            
        out.output(doc, new FileOutputStream(filename));

    }

    private HashMap<String, ItemSaveHandler> RegisterItemSaveHandlers()
    {
        HashMap<String, ItemSaveHandler> handlers = new HashMap<String, ItemSaveHandler>();

        handlers.put("Engine", new ItemSaveHandler() {public void saveItem(DesignItem item, org.jdom.Element element){saveEngine(item,element);}});
        handlers.put("Weapon", new ItemSaveHandler() {public void saveItem(DesignItem item, org.jdom.Element element){saveWeapon(item,element);}});
        handlers.put("Ammunition", new ItemSaveHandler() {public void saveItem(DesignItem item, org.jdom.Element element){saveAmmunition(item,element);}});
        handlers.put("Heat Sink", new ItemSaveHandler() {public void saveItem(DesignItem item, org.jdom.Element element){saveHeatSink(item,element);}});
        handlers.put("Jump Jet", new ItemSaveHandler() {public void saveItem(DesignItem item, org.jdom.Element element){saveJumpJet(item,element);}});

        return handlers;
    }

    private void saveItemBaseElements(Element element, DesignItem item)
    {
        element.setAttribute("Manufacturer", item.getManufacturer());
        element.setAttribute("Model", item.getModel());
    }

    private void saveCritSlotReferences(Element element, DesignItem item)
    {
        for (InternalSlotReference isr : item.getSlotReferences())
        {
            Element slotRefElement = new org.jdom.Element("SlotReference");
            slotRefElement.setAttribute("Location", isr.getInternalLocation());
            slotRefElement.setAttribute("Table", Integer.toString(isr.getTable()));
            slotRefElement.setAttribute("Slot", Integer.toString(isr.getSlot()));
            if (isr.getRearFacing())
                slotRefElement.setAttribute("RearFacing", "True");

            element.addContent(slotRefElement);
        }
    }


    private void saveEngine(DesignItem di, Element element)
    {
        EngineDesign ed = (EngineDesign)di;
        element.setAttribute("Rating", Integer.toString(ed.getRating()));
        element.setAttribute("Weight", Double.toString(ed.getWeight()));
        if (ed.getHeatSinks() != 10)
        	element.setAttribute("HeatSinks", Integer.toString(ed.getHeatSinks()));
    }

    private void saveJumpJet(DesignItem di, Element element)
    {
        JumpJetDesign jjd = (JumpJetDesign)di;
        element.setAttribute("Weight", Double.toString(jjd.getWeight()));
    }

    private void saveWeapon(DesignItem di, Element element)
    {
        DesignWeapon dw = (DesignWeapon)di;
        element.setAttribute("WeaponType", dw.getWeaponType());
    }

    private void saveAmmunition(DesignItem di, Element element)
    {
        DesignAmmunition da = (DesignAmmunition)di;
        element.setAttribute("WeaponType", da.getWeaponType());
        element.setAttribute("Shots", Integer.toString(da.getShots()));
        if (da.getWeight() != 1.0)
            element.setAttribute("Weight", Double.toString(da.getWeight()));
    }

    private void saveHeatSink(DesignItem di, Element element)
    {
        HeatSinkDesign hsd = (HeatSinkDesign)di;
        if (!hsd.getHeatSinkType().equals("Single"))
            element.setAttribute("HeatSinkType", hsd.getHeatSinkType());
    }

}


