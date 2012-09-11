package bt.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import bt.elements.Ammunition;
import bt.elements.Autocannon;
import bt.elements.Battlemech;
import bt.elements.BattlemechRepairReport;
import bt.elements.BattlemechSection;
import bt.elements.Cockpit;
import bt.elements.Engine;
import bt.elements.Flamer;
import bt.elements.FootActuator;
import bt.elements.Gyro;
import bt.elements.HandActuator;
import bt.elements.HeatSink;
import bt.elements.Hip;
import bt.elements.InternalSlotStatus;
import bt.elements.Item;
import bt.elements.ItemMount;
import bt.elements.ItemRepairDetail;
import bt.elements.ItemStatus;
import bt.elements.JumpJet;
import bt.elements.Laser;
import bt.elements.LifeSupport;
import bt.elements.LongRangeMissile;
import bt.elements.LowerArmActuator;
import bt.elements.LowerLegActuator;
import bt.elements.MachineGun;
import bt.elements.ParticleProjectionCannon;
import bt.elements.SectionStatus;
import bt.elements.Sensors;
import bt.elements.ShortRangeMissile;
import bt.elements.Shoulder;
import bt.elements.UpperArmActuator;
import bt.elements.UpperLegActuator;
import bt.elements.Weapon;
import bt.elements.WeightClassBasedItem;
import bt.elements.ItemRepairDetail.Type;
import bt.elements.design.BattlemechDesign;
import bt.elements.design.DesignAmmunition;
import bt.elements.design.DesignItem;
import bt.elements.design.DesignWeapon;
import bt.elements.design.EngineDesign;
import bt.elements.design.InternalSlotReference;
import bt.elements.design.JumpJetDesign;

public class BattlemechManager
{
	public interface ItemLoadHandler { public Item loadItem(org.jdom.Element element); }
	public interface WeaponLoadHandler { public Item loadWeapon(org.jdom.Element element); }

	public interface ItemSaveHandler { public void saveItem(Item item, org.jdom.Element element); }
	
	public interface ItemCreateHandler { public Item createItem(DesignItem di); }
	public interface WeaponCreateHandler { public Item createWeapon(DesignWeapon di); }

	public interface ItemReplacementHandler { public ItemRepairDetail getItemRepairDetail(ItemMount mount); }
	public interface ItemRepairHandler { public ItemRepairDetail getItemRepairDetail(ItemMount mount); }

    private int[][] _InternalSizes = 
    {
        {3,6,5,3,4},
        {3,8,6,4,6},
        {3,10,7,5,7},
        {3,11,8,6,8},
        {3,12,10,6,10},
        {3,14,11,7,11},
        {3,16,12,8,12},
        {3,18,13,9,13},
        {3,20,14,10,14},
        {3,21,15,10,15},
        {3,22,15,11,15},
        {3,23,16,12,16},
        {3,25,17,13,17},
        {3,27,18,14,18},
        {3,29,19,15,19},
        {3,30,20,16,20},
        {3,31,21,17,21}
    };

    private HashMap<String, Integer> _InternalLocationIndexes = new HashMap<String,Integer>();

    HashMap<String, ItemLoadHandler> _ItemLoadHandlers;
    HashMap<String, ItemCreateHandler> _ItemCreateHandlers;
    HashMap<String, WeaponLoadHandler> _WeaponLoadHandlers;
    HashMap<String, WeaponCreateHandler> _WeaponCreateHandlers;

    HashMap<String, ItemSaveHandler> _ItemSaveHandlers;

    HashMap<String, ItemReplacementHandler> _ItemReplacementHandlers;
    HashMap<String, ItemRepairHandler> _ItemRepairHandlers;

    public BattlemechManager()
    {
        registerInternalLocationIndexes();
        _ItemLoadHandlers = registerItemLoadHandlers();
        _ItemCreateHandlers = registerItemCreateHandlers();
        _WeaponLoadHandlers = registerWeaponLoadHandlers();
        _WeaponCreateHandlers = registerWeaponCreateHandlers();
        _ItemSaveHandlers = registerItemSaveHandlers();
        _ItemReplacementHandlers = registerItemReplacementHandlers();
        _ItemRepairHandlers = registerItemRepairHandlers();
    }


    private void registerInternalLocationIndexes()
    {
        _InternalLocationIndexes.put("Head", 0);
        _InternalLocationIndexes.put("Centre Torso", 1);
        _InternalLocationIndexes.put("Left Torso", 2);
        _InternalLocationIndexes.put("Right Torso", 2);
        _InternalLocationIndexes.put("Left Arm", 3);
        _InternalLocationIndexes.put("Right Arm", 3);
        _InternalLocationIndexes.put("Left Leg", 4);
        _InternalLocationIndexes.put("Right Leg", 4);
        _InternalLocationIndexes.put("Left Front Leg", 4);
        _InternalLocationIndexes.put("Right Front Leg", 4);
        _InternalLocationIndexes.put("Left Rear Leg", 4);
        _InternalLocationIndexes.put("Right Rear Leg", 4);
    }

    private int getIndexFromWeight(int weight)
    {
        return (weight / 5) - 4;
    }



    public Battlemech createBattlemechFromDesign(BattlemechDesign design)
    {
        Battlemech mech = new Battlemech();

        mech.setDesignName(design.getName());
        mech.setDesignVariant(design.getVariant());
        mech.setManufacturer(design.getManufacturer());

        mech.setType(design.getType().toString());
        mech.setWeight(design.getWeight());
        mech.setBV(design.getBV());

        mech.setSectionStatuses(buildMechSections(design.getSections()));
        mech.setInternals(buildMechInternals(mech.getWeight(), design.getInternalLocations()));
        mech.setArmour(buildMechArmour(design.getArmour()));

        int itemIdentifier = 0;
        for (DesignItem di : design.getItems())
        {
            Item i = _ItemCreateHandlers.get(di.getType()).createItem(di);
            i.setManufacturer(di.getManufacturer());
            i.setModel(di.getModel());
            i.setIdentifier(itemIdentifier++);

            if (i instanceof WeightClassBasedItem)
                ((WeightClassBasedItem)i).setMechWeight(design.getWeight());

            Vector<InternalSlotStatus> slots = new Vector<InternalSlotStatus>();
            for (InternalSlotReference isr : di.getSlotReferences())
            {
                InternalSlotStatus iss = new InternalSlotStatus(isr.getInternalLocation(), isr.getTable(), isr.getSlot(), ItemStatus.OK);
                if (isr.getRearFacing())
                	iss.setRearFacing(true);
                slots.add(iss);
            }

            mech.getItems().add(new ItemMount(i,slots));
        }

        return mech;
    }

    private HashMap<BattlemechSection, SectionStatus> buildMechSections(BattlemechSection[] sections)
    {
        HashMap<BattlemechSection, SectionStatus> sectionStatuses = new HashMap<BattlemechSection, SectionStatus>();

        for (BattlemechSection sec : sections)
        {
        	sectionStatuses.put(sec, new SectionStatus(SectionStatus.Status.OK, false));
        }

        return sectionStatuses;
    }

    private HashMap<String, HashMap<Integer, ItemStatus>> buildMechInternals(int weight, String[] Locations)
    {
        HashMap<String, HashMap<Integer, ItemStatus>> internals = new HashMap<String, HashMap<Integer, ItemStatus>>();

        for (String loc : Locations)
        {
            HashMap<Integer, ItemStatus> dotStatuses = new HashMap<Integer, ItemStatus>();

            int dots = _InternalSizes[getIndexFromWeight(weight)][_InternalLocationIndexes.get(loc)];
            for (int i = 1; i <= dots; i++)
            {
                dotStatuses.put(i, ItemStatus.OK);
            }
            internals.put(loc, dotStatuses);
        }

        return internals;
    }

    private HashMap<String, HashMap<Integer, ItemStatus>> buildMechArmour(HashMap<String,Integer> armourDesign)
    {
        HashMap<String, HashMap<Integer, ItemStatus>> armour = new HashMap<String, HashMap<Integer, ItemStatus>>();

        for (String location : armourDesign.keySet())
        {
            HashMap<Integer, ItemStatus> dotStatuses = new HashMap<Integer, ItemStatus>();

            int dots = armourDesign.get(location);
            for (int i = 1; i <= dots; i++)
            {
                dotStatuses.put(i, ItemStatus.OK);
            }
            armour.put(location, dotStatuses);
        }
        return armour;
    }

    /*
    private ItemMount addItem(Item i, String location, int table, int slot)
    {
        ItemMount mount = new ItemMount();
        mount.setMountedItem(i);
        mount.getSlotReferences().add(new InternalSlotStatus(location,table,slot,ItemStatus.OK));
        return mount;
    }
    */

    public Battlemech loadBattlemech(String filename) throws Exception
    {
		SAXBuilder b = new SAXBuilder();
		Document doc = b.build(new File(filename));

        return loadBattlemech(doc.getRootElement());
    }

    public Battlemech loadBattlemech(org.jdom.Element mechElement)
    {

        Battlemech mech = new Battlemech();

        mech.setIdentifier(mechElement.getChildTextTrim("ID"));
        mech.setName(mechElement.getChildTextTrim("Name"));
        mech.setDesignName(mechElement.getChildTextTrim("DesignName"));
        mech.setDesignVariant(mechElement.getChildTextTrim("DesignVariant"));
        mech.setManufacturer(mechElement.getChildTextTrim("Manufacturer"));

        mech.setType(mechElement.getChildTextTrim("Type"));
        mech.setWeight(Integer.parseInt(mechElement.getChildTextTrim("Weight")));
        mech.setBV(Integer.parseInt(mechElement.getChildTextTrim("BV")));

        if (mechElement.getChild("Sections") != null)
        {
	        Iterator<?> iter = mechElement.getChild("Sections").getChildren().iterator();
	        while (iter.hasNext())
	        {
	        	org.jdom.Element internalElement = (org.jdom.Element)iter.next();
	            String location = internalElement.getAttributeValue("Name");
	            String status = internalElement.getAttributeValue("Status");
	            boolean breached = Boolean.parseBoolean(internalElement.getAttributeValue("Breached"));
	            mech.getSectionStatuses().put(BattlemechSection.fromString(location), new SectionStatus(SectionStatus.Status.fromString(status), breached));
	        }
        }
        else
        {
			BattlemechDesign bd = DesignManager.getInstance().Design(mech.getDesignVariant() + " " + mech.getDesignName());
			if (bd != null)
			{
				mech.setSectionStatuses(buildMechSections(bd.getSections()));
			}
        }

        Iterator<?> iter = mechElement.getChild("Internals").getChildren().iterator();
        while (iter.hasNext())
        {
        	org.jdom.Element internalElement = (org.jdom.Element)iter.next();
            String Location = internalElement.getAttributeValue("Name");
            HashMap<Integer, ItemStatus> internalStatuses = new HashMap<Integer, ItemStatus>();
            Iterator<?> internalIter = internalElement.getChildren().iterator();
            while (internalIter.hasNext())
            {
            	org.jdom.Element internalSpotElement = (org.jdom.Element)internalIter.next();
                Integer id = Integer.parseInt(internalSpotElement.getAttributeValue("ID"));
                ItemStatus status = ItemStatus.fromString(internalSpotElement.getAttributeValue("Status"));
                internalStatuses.put(id, status);
            }

            mech.getInternals().put(Location, internalStatuses);
        }
        iter = mechElement.getChild("Armour").getChildren().iterator();
        while (iter.hasNext())
        {
        	org.jdom.Element armourElement = (org.jdom.Element)iter.next();
            String Location = armourElement.getAttributeValue("Name");
            HashMap<Integer, ItemStatus> armourStatuses = new HashMap<Integer, ItemStatus>();
            Iterator<?> armourIter = armourElement.getChildren().iterator();
            while (armourIter.hasNext())
            {
            	org.jdom.Element armourSpotElement = (org.jdom.Element)armourIter.next();
                Integer id = Integer.parseInt(armourSpotElement.getAttributeValue("ID"));
                ItemStatus status = ItemStatus.fromString(armourSpotElement.getAttributeValue("Status"));
                armourStatuses.put(id, status);
            }

            mech.getArmour().put(Location, armourStatuses);
        }

        int itemIdentifier = 1;
        iter = mechElement.getChild("Items").getChildren().iterator();
        while (iter.hasNext())
        {
        	org.jdom.Element itemMountElement = (org.jdom.Element)iter.next();
            ItemMount mount = new ItemMount();
            org.jdom.Element itemElement = itemMountElement.getChild("Item");
            
            String type = itemElement.getAttributeValue("Type");
            if (_ItemLoadHandlers.containsKey(type))
            {
            	Item item = _ItemLoadHandlers.get(type).loadItem(itemElement);
            	if (itemElement.getAttribute("ID") != null)
            		item.setIdentifier(Integer.parseInt(itemElement.getAttributeValue("ID")));
            	else
            		item.setIdentifier(itemIdentifier);
            	
                mount.setMountedItem(item);
                loadItemBaseElements(itemElement, mount.getMountedItem(), mech.getWeight());
            }
            else
        		throw new RuntimeException("Unable to load item from type " + type);
            	
            loadCritSlotReferences(itemMountElement, mount);
            mech.getItems().add(mount);
            itemIdentifier++;
        }

        return mech;
    }


    private HashMap<String, ItemCreateHandler> registerItemCreateHandlers()
    {
        HashMap<String, ItemCreateHandler> handlers = new HashMap<String, ItemCreateHandler>();

        handlers.put("Life Support", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createLifeSupport(di); }});
        handlers.put("Cockpit", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createCockpit(di); }});
        handlers.put("Gyro", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createGyro(di); }});
        handlers.put("Heat Sink", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createHeatSink(di); }});
        handlers.put("Shoulder", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createShoulder(di); }});
        handlers.put("Upper Arm Actuator", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createUpperArmActuator(di); }});
        handlers.put("Lower Arm Actuator", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createLowerArmActuator(di); }});
        handlers.put("Hand Actuator", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createHandActuator(di); }});
        handlers.put("Hip", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createHip(di); }});
        handlers.put("Upper Leg Actuator", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createUpperLegActuator(di); }});
        handlers.put("Lower Leg Actuator", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createLowerLegActuator(di); }});
        handlers.put("Foot Actuator", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createFootActuator(di); }});
        
        handlers.put("Sensors", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createSensors(di); }});
        handlers.put("Engine", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createEngine(di); }});
        handlers.put("Jump Jet", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createJumpJet(di); }});
        handlers.put("Weapon", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createWeapon(di); }});
        handlers.put("Ammunition", new ItemCreateHandler() { public Item createItem(DesignItem di) { return createAmmunition(di); }});
        return handlers;
    }


    private HashMap<String, ItemLoadHandler> registerItemLoadHandlers()
    {
        HashMap<String, ItemLoadHandler> handlers = new HashMap<String, ItemLoadHandler>();

        handlers.put("Sensors", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadSensors(element); }});
        handlers.put("Life Support", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadLifeSupport(element); }});
        handlers.put("Cockpit", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadCockpit(element); }});
        handlers.put("Shoulder", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadShoulder(element); }});
        handlers.put("Upper Arm Actuator", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadUpperArmActuator(element); }});
        handlers.put("Lower Arm Actuator", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadLowerArmActuator(element); }});
        handlers.put("Hand Actuator", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadHandActuator(element); }});
        handlers.put("Hip", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadHip(element); }});
        handlers.put("Upper Leg Actuator", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadUpperLegActuator(element); }});
        handlers.put("Lower Leg Actuator", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadLowerLegActuator(element); }});
        handlers.put("Foot Actuator", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadFootActuator(element); }});

        handlers.put("Engine", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadEngine(element); }});
        handlers.put("Gyro", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadGyro(element); }});
        handlers.put("Heat Sink", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadHeatSink(element); }});
        handlers.put("Jump Jet", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadJumpJet(element); }});
        handlers.put("Weapon", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadWeapon(element); }});
        handlers.put("Ammunition", new ItemLoadHandler() { public Item loadItem(org.jdom.Element element) { return loadAmmunition(element); }});

        return handlers;
    }

    private HashMap<String, ItemSaveHandler> registerItemSaveHandlers()
    {
        HashMap<String, ItemSaveHandler> handlers = new HashMap<String, ItemSaveHandler>();
 
        handlers.put("Engine", new ItemSaveHandler() { public void saveItem(Item item, org.jdom.Element element) { saveEngine(item,element); }});
        handlers.put("Jump Jet", new ItemSaveHandler() { public void saveItem(Item item, org.jdom.Element element) { saveJumpJet(item,element); }});
        handlers.put("Weapon", new ItemSaveHandler() { public void saveItem(Item item, org.jdom.Element element) { saveWeapon(item,element); }});
        handlers.put("Ammunition", new ItemSaveHandler() { public void saveItem(Item item, org.jdom.Element element) { saveAmmunition(item,element); }});

        return handlers;
    }


    private void loadItemBaseElements(org.jdom.Element element, Item item, int mechWeight)
    {
        if (element.getAttribute("Manufacturer") != null)
            item.setManufacturer(element.getAttributeValue("Manufacturer"));

        if (element.getAttribute("Model") != null)
            item.setModel(element.getAttributeValue("Model"));

        if (item instanceof WeightClassBasedItem)
        {
            if (element.getAttribute("Manufacturer") != null)
            {
                WeightClassBasedItem wcbi = (WeightClassBasedItem)item;
                if (element.getAttribute("MechWeight") != null)
                	wcbi.setMechWeight(Integer.parseInt(element.getAttributeValue("MechWeight")));
                else
                    wcbi.setMechWeight(mechWeight);            	
            }
        }
    }

    private void loadCritSlotReferences(org.jdom.Element element, ItemMount item)
    {
    	Iterator<?> iter = element.getChildren("SlotReference").iterator();
    	while (iter.hasNext())
        {
    		org.jdom.Element slotReferenceElement = (org.jdom.Element)iter.next();
            InternalSlotStatus iss = new InternalSlotStatus();
            iss.setInternalLocation(slotReferenceElement.getAttributeValue("Location"));
            iss.setTable(Integer.parseInt(slotReferenceElement.getAttributeValue("Table")));
            iss.setSlot(Integer.parseInt(slotReferenceElement.getAttributeValue("Slot")));
            iss.setStatus(ItemStatus.fromString(slotReferenceElement.getAttributeValue("Status")));
            if (slotReferenceElement.getAttribute("RearFacing") != null)
            	iss.setRearFacing(Boolean.parseBoolean(slotReferenceElement.getAttributeValue("RearFacing")));

            item.getSlotReferences().add(iss);
        }
    }

    private Item loadSensors(org.jdom.Element element)
    {
        return new Sensors();
    }

    private Item loadCockpit(org.jdom.Element element)
    {
        return new Cockpit();
    }

    private Item loadLifeSupport(org.jdom.Element element)
    {
        return new LifeSupport();
    }

    private Item loadShoulder(org.jdom.Element element)
    {
        return new Shoulder();
    }

    private Item loadUpperArmActuator(org.jdom.Element element)
    {
        return new UpperArmActuator();
    }

    private Item loadLowerArmActuator(org.jdom.Element element)
    {
        return new LowerArmActuator();
    }

    private Item loadHandActuator(org.jdom.Element element)
    {
        return new HandActuator();
    }

    private Item loadHip(org.jdom.Element element)
    {
        return new Hip();
    }

    private Item loadUpperLegActuator(org.jdom.Element element)
    {
        return new UpperLegActuator();
    }

    private Item loadLowerLegActuator(org.jdom.Element element)
    {
        return new LowerLegActuator();
    }

    private Item loadFootActuator(org.jdom.Element element)
    {
        return new FootActuator();
    }

    private Item loadGyro(org.jdom.Element element)
    {
        return new Gyro();
    }

    private Item loadHeatSink(org.jdom.Element element)
    {
        return new HeatSink();
    }

    private Item loadEngine(org.jdom.Element element)
    {
        Engine ed = new Engine();
        ed.setRating(Integer.parseInt(element.getAttributeValue("Rating")));
        ed.setWeight(Double.parseDouble(element.getAttributeValue("Weight")));

        return ed;
    }

    private Item loadJumpJet(org.jdom.Element element)
    {
        JumpJet jj = new JumpJet();
        jj.setWeight(Double.parseDouble(element.getAttributeValue("Weight")));

        return jj;
    }

    private Item loadWeapon(org.jdom.Element element)
    {
        String type = element.getAttributeValue("WeaponType");
        if (_WeaponLoadHandlers.containsKey(type))
        {
            return _WeaponLoadHandlers.get(type).loadWeapon(element);
        }

        return null;
    }

    private Item loadAmmunition(org.jdom.Element element)
    {
        Ammunition ammo = new Ammunition();
        ammo.setWeaponType(element.getAttributeValue("WeaponType"));
        if (element.getAttribute("Weight") != null)
            ammo.setWeight(Double.parseDouble(element.getAttributeValue("Weight")));
        ammo.setShots(Integer.parseInt(element.getAttributeValue("Shots")));
        return ammo;
    }



    private void saveEngine(Item i, org.jdom.Element element)
    {
        Engine e = (Engine)i;
        element.setAttribute("Rating", Integer.toString(e.getRating()));
        element.setAttribute("Weight", Double.toString(e.getWeight()));
    }

    private void saveJumpJet(Item i, org.jdom.Element element)
    {
        JumpJet jj = (JumpJet)i;
        element.setAttribute("Weight", Double.toString(jj.getWeight()));
    }

    private void saveWeapon(Item i, org.jdom.Element element)
    {
        Weapon w = (Weapon)i;
        element.setAttribute("WeaponType", w.getWeaponType());
    }

    private void saveAmmunition(Item i, org.jdom.Element element)
    {
        Ammunition a = (Ammunition)i;
        element.setAttribute("WeaponType", a.getWeaponType());
        element.setAttribute("Weight", Double.toString(a.getWeight()));
        element.setAttribute("Shots", Integer.toString(a.getShots()));
    }

    private HashMap<String, WeaponCreateHandler> registerWeaponCreateHandlers()
    {
        HashMap<String, WeaponCreateHandler> handlers = new HashMap<String, WeaponCreateHandler>();

        handlers.put("PPC", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createPPC(dw); }});
        handlers.put("Autocannon 20", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createAutocannon20(dw); }});
        handlers.put("Autocannon 10", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createAutocannon10(dw); }});
        handlers.put("Autocannon 5", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createAutocannon5(dw); }});
        handlers.put("Autocannon 2", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createAutocannon2(dw); }});
        handlers.put("SRM 6", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createSRM6(dw); }});
        handlers.put("SRM 4", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createSRM4(dw); }});
        handlers.put("SRM 2", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createSRM2(dw); }});
        handlers.put("LRM 20", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createLRM20(dw); }});
        handlers.put("LRM 15", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createLRM15(dw); }});
        handlers.put("LRM 10", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createLRM10(dw); }});
        handlers.put("LRM 5", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createLRM5(dw); }});
        handlers.put("Small Laser", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createSmallLaser(dw); }});
        handlers.put("Medium Laser", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createMediumLaser(dw); }});
        handlers.put("Large Laser", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createLargeLaser(dw); }});
        handlers.put("Machine Gun", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createMachineGun(dw); }});
        handlers.put("Flamer", new WeaponCreateHandler() { public Item createWeapon(DesignWeapon dw) { return createFlamer(dw); }});

        return handlers;
    }

    private HashMap<String, WeaponLoadHandler> registerWeaponLoadHandlers()
    {
        HashMap<String, WeaponLoadHandler> handlers = new HashMap<String, WeaponLoadHandler>();

        handlers.put("PPC", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadPPC(element); }});
        handlers.put("Autocannon 20", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadAutocannon20(element); }});
        handlers.put("Autocannon 10", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadAutocannon10(element); }});
        handlers.put("Autocannon 5", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadAutocannon5(element); }});
        handlers.put("Autocannon 2", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadAutocannon2(element); }});
        handlers.put("SRM 6", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadSRM6(element); }});
        handlers.put("SRM 4", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadSRM4(element); }});
        handlers.put("SRM 2", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadSRM2(element); }});
        handlers.put("LRM 20", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadLRM20(element); }});
        handlers.put("LRM 15", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadLRM15(element); }});
        handlers.put("LRM 10", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadLRM10(element); }});
        handlers.put("LRM 5", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadLRM5(element); }});
        handlers.put("Small Laser", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadSmallLaser(element); }});
        handlers.put("Medium Laser", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadMediumLaser(element); }});
        handlers.put("Large Laser", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadLargeLaser(element); }});
        handlers.put("Machine Gun", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadMachineGun(element); }});
        handlers.put("Flamer", new WeaponLoadHandler() { public Item loadWeapon(org.jdom.Element element) { return loadFlamer(element); }});

        return handlers;
    }

    private Item loadPPC(org.jdom.Element element)
    {
        return new ParticleProjectionCannon();
    }

    private Item loadAutocannon(int size)
    {
        return new Autocannon(size);
    }

    private Item loadAutocannon20(org.jdom.Element element)
    {
        return loadAutocannon(20);
    }

    private Item loadAutocannon10(org.jdom.Element element)
    {
        return loadAutocannon(10);
    }

    private Item loadAutocannon5(org.jdom.Element element)
    {
        return loadAutocannon(5);
    }

    private Item loadAutocannon2(org.jdom.Element element)
    {
        return loadAutocannon(2);
    }

    private Item loadSRM(int size)
    {
        return new ShortRangeMissile(size);
    }

    private Item loadSRM6(org.jdom.Element element)
    {
        return loadSRM(6);
    }

    private Item loadSRM4(org.jdom.Element element)
    {
        return loadSRM(4);
    }

    private Item loadSRM2(org.jdom.Element element)
    {
        return loadSRM(2);
    }

    private Item loadLRM(int size)
    {
        return new LongRangeMissile(size);
    }

    private Item loadLRM20(org.jdom.Element element)
    {
        return loadLRM(20);
    }

    private Item loadLRM15(org.jdom.Element element)
    {
        return loadLRM(15);
    }

    private Item loadLRM10(org.jdom.Element element)
    {
        return loadLRM(10);
    }

    private Item loadLRM5(org.jdom.Element element)
    {
        return loadLRM(5);
    }

    private Item loadLaser(String laserType)
    {
        return new Laser(laserType);
    }

    private Item loadSmallLaser(org.jdom.Element element)
    {
        return loadLaser("Small");
    }

    private Item loadMediumLaser(org.jdom.Element element)
    {
        return loadLaser("Medium");
    }

    private Item loadLargeLaser(org.jdom.Element element)
    {
        return loadLaser("Large");
    }

    private Item loadMachineGun(org.jdom.Element element)
    {
        return new MachineGun();
    }

    private Item loadFlamer(org.jdom.Element element)
    {
        return new Flamer();
    }



    private Item createPPC(DesignWeapon dw)
    {
        return new ParticleProjectionCannon();
    }

    private Item createAutocannon(int size)
    {
        return new Autocannon(size);
    }

    private Item createAutocannon20(DesignWeapon dw)
    {
        return createAutocannon(20);
    }

    private Item createAutocannon10(DesignWeapon dw)
    {
        return createAutocannon(10);
    }

    private Item createAutocannon5(DesignWeapon dw)
    {
        return createAutocannon(5);
    }

    private Item createAutocannon2(DesignWeapon dw)
    {
        return createAutocannon(2);
    }

    private Item createSRM(int size)
    {
        return new ShortRangeMissile(size);
    }

    private Item createSRM6(DesignWeapon dw)
    {
        return createSRM(6);
    }

    private Item createSRM4(DesignWeapon dw)
    {
        return createSRM(4);
    }

    private Item createSRM2(DesignWeapon dw)
    {
        return createSRM(2);
    }

    private Item createLRM(int size)
    {
        return new LongRangeMissile(size);
    }

    private Item createLRM20(DesignWeapon dw)
    {
        return createLRM(20);
    }

    private Item createLRM15(DesignWeapon dw)
    {
        return createLRM(15);
    }

    private Item createLRM10(DesignWeapon dw)
    {
        return createLRM(10);
    }

    private Item createLRM5(DesignWeapon dw)
    {
        return createLRM(5);
    }

    private Item createLaser(String laserType)
    {
        return new Laser(laserType);
    }

    private Item createSmallLaser(DesignWeapon dw)
    {
        return createLaser("Small");
    }

    private Item createMediumLaser(DesignWeapon dw)
    {
        return createLaser("Medium");
    }

    private Item createLargeLaser(DesignWeapon dw)
    {
        return createLaser("Large");
    }

    private Item createMachineGun(DesignWeapon dw)
    {
        return new MachineGun();
    }

    private Item createFlamer(DesignWeapon dw)
    {
        return new Flamer();
    }

    private Item createLifeSupport(DesignItem di)
    {
        return new LifeSupport();
    }

    private Item createCockpit(DesignItem di)
    {
        return new Cockpit();
    }

    private Item createGyro(DesignItem di)
    {
        return new Gyro();
    }

    private Item createHeatSink(DesignItem di)
    {
        return new HeatSink();
    }

    private Item createShoulder(DesignItem di)
    {
        return new Shoulder();
    }

    private Item createUpperArmActuator(DesignItem di)
    {
        return new UpperArmActuator();
    }

    private Item createLowerArmActuator(DesignItem di)
    {
        return new LowerArmActuator();
    }

    private Item createHandActuator(DesignItem di)
    {
        return new HandActuator();
    }

    private Item createHip(DesignItem di)
    {
        return new Hip();
    }

    private Item createUpperLegActuator(DesignItem di)
    {
        return new UpperLegActuator();
    }

    private Item createLowerLegActuator(DesignItem di)
    {
        return new LowerLegActuator();
    }

    private Item createFootActuator(DesignItem di)
    {
        return new FootActuator();
    }

    private Item createSensors(DesignItem di)
    {
        return new Sensors();
    }







    private Item createEngine(DesignItem di)
    {
        EngineDesign ed = (EngineDesign)di;
        Engine e = new Engine();
        e.setRating(ed.getRating());
        e.setWeight(ed.getWeight());

        return e;
    }

    private Item createJumpJet(DesignItem di)
    {
        JumpJetDesign jjd = (JumpJetDesign)di;
        JumpJet jj = new JumpJet();
        jj.setWeight(jjd.getWeight());

        return jj;
    }

    private Item createWeapon(DesignItem di)
    {
        DesignWeapon dw = (DesignWeapon)di;
        String weaponType = dw.getWeaponType();
        if (_WeaponCreateHandlers.containsKey(weaponType))
        {
            return _WeaponCreateHandlers.get(weaponType).createWeapon(dw);
        }

        return null;
    }

    private Item createAmmunition(DesignItem di)
    {
        DesignAmmunition da = (DesignAmmunition)di;
        Ammunition ammo = new Ammunition();
        ammo.setWeaponType(da.getWeaponType());
        ammo.setWeight(da.getWeight());
        ammo.setShots(da.getShots());

        return ammo;
    }

    private void saveInternalSlotStatuses(ItemMount item, org.jdom.Element element)
    {
    	
        for (InternalSlotStatus iss : item.getSlotReferences())
        {
            org.jdom.Element refElement = new org.jdom.Element("SlotReference");
            refElement.setAttribute("Location", iss.getInternalLocation());
            refElement.setAttribute("Table", Integer.toString(iss.getTable()));
            refElement.setAttribute("Slot", Integer.toString(iss.getSlot()));
            refElement.setAttribute("Status", iss.getStatus().toString());
            if (iss.getRearFacing())
            	refElement.setAttribute("RearFacing", Boolean.toString(iss.getRearFacing()));
            	
            element.addContent(refElement);
        }
    }

    private void saveItemBaseElements(Item item, org.jdom.Element element)
    {
        element.setAttribute("Manufacturer", item.getManufacturer());
        element.setAttribute("Model", item.getModel());
        if (item instanceof WeightClassBasedItem)
        {
            WeightClassBasedItem wcbi = (WeightClassBasedItem)item;
            element.setAttribute("MechWeight", Integer.toString(wcbi.getMechWeight()));
        }
    }

    public void saveBattlemech(Battlemech mech, String filename) throws Exception
    {
        org.jdom.Document doc = new org.jdom.Document();

        doc.addContent(saveBattlemech(mech));
        
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		out.output(doc, new FileOutputStream(filename));
    }

    public org.jdom.Element saveBattlemech(Battlemech mech)
    {
        org.jdom.Element battlemechNode = new org.jdom.Element("Battlemech");

        battlemechNode.addContent(new org.jdom.Element("ID").setText(mech.getIdentifier()));
        battlemechNode.addContent(new org.jdom.Element("Name").setText(mech.getName()));
        battlemechNode.addContent(new org.jdom.Element("DesignName").setText(mech.getDesignName()));
        battlemechNode.addContent(new org.jdom.Element("DesignVariant").setText(mech.getDesignVariant()));
        battlemechNode.addContent(new org.jdom.Element("Manufacturer").setText(mech.getManufacturer()));

        battlemechNode.addContent(new org.jdom.Element("Type").setText(mech.getType().toString()));
        battlemechNode.addContent(new org.jdom.Element("Weight").setText(Integer.toString(mech.getWeight())));
        battlemechNode.addContent(new org.jdom.Element("BV").setText(Integer.toString(mech.getBV())));

        org.jdom.Element sectionsNode = new org.jdom.Element("Sections");
        for (BattlemechSection section : mech.getSectionStatuses().keySet())
        {
            org.jdom.Element sectionNode = new org.jdom.Element("Section");
            sectionNode.setAttribute("Name", section.toString());
            sectionNode.setAttribute("Status", mech.getSectionStatuses().get(section).getStatus().toString());
            sectionNode.setAttribute("Breached", Boolean.toString(mech.getSectionStatuses().get(section).isBreached()));
            sectionsNode.addContent(sectionNode);
        }
        battlemechNode.addContent(sectionsNode);
        
        org.jdom.Element internalsNode = new org.jdom.Element("Internals");
        for (String locationName : mech.getInternals().keySet())
        {
            org.jdom.Element locationNode = new org.jdom.Element("Location");
            locationNode.setAttribute("Name", locationName);

            for (Integer Index : mech.getInternals().get(locationName).keySet())
            {
                org.jdom.Element spotElement = new org.jdom.Element("Spot");
                spotElement.setAttribute("ID", Index.toString());
                spotElement.setAttribute("Status", mech.getInternals().get(locationName).get(Index).toString());
                locationNode.addContent(spotElement);
            }
            internalsNode.addContent(locationNode);
        }
        battlemechNode.addContent(internalsNode);

        org.jdom.Element armourNode = new org.jdom.Element ("Armour");
        for (String locationName : mech.getArmour().keySet())
        {
        	org.jdom.Element locationNode = new org.jdom.Element ("Location");
            locationNode.setAttribute("Name", locationName);

            for (Integer Index : mech.getArmour().get(locationName).keySet())
            {
                org.jdom.Element spotElement = new org.jdom.Element("Spot");
                spotElement.setAttribute("ID", Index.toString());
                spotElement.setAttribute("Status", mech.getArmour().get(locationName).get(Index).toString());
                locationNode.addContent(spotElement);
            }
            armourNode.addContent(locationNode);
        }
        battlemechNode.addContent(armourNode);

        org.jdom.Element itemsNode = new org.jdom.Element("Items");
        for (ItemMount mount : mech.getItems())
        {
            org.jdom.Element itemMountElement = new org.jdom.Element("ItemMount");
            org.jdom.Element itemElement = new org.jdom.Element("Item");
            if (mount.getMountedItem() == null)
            	throw new RuntimeException("Item mount contains no Item for " + mech.getDesignVariant() + " " + mech.getDesignName());
            String mountedItemType = mount.getMountedItem().getType();
            itemElement.setAttribute("Type", mountedItemType);
            itemElement.setAttribute("ID", Integer.toString(mount.getMountedItem().getIdentifier()));
            if (_ItemSaveHandlers.containsKey(mountedItemType))
                _ItemSaveHandlers.get(mountedItemType).saveItem(mount.getMountedItem(), itemElement);
            saveItemBaseElements(mount.getMountedItem(), itemElement);

            itemMountElement.addContent(itemElement);

            saveInternalSlotStatuses(mount,itemMountElement);
            itemsNode.addContent(itemMountElement);
        }
        battlemechNode.addContent(itemsNode);
        return battlemechNode;
    }

    private HashMap<String, ItemReplacementHandler> registerItemReplacementHandlers()
    {
        HashMap<String, ItemReplacementHandler> handlers = new HashMap<String, ItemReplacementHandler>();

        handlers.put("Life Support", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getLifeSupportReplacementDetail(mount); }});
        handlers.put("Cockpit", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getCockpitReplacementDetail(mount); }});
        handlers.put("Gyro", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getGyroReplacementDetail(mount); }});
        handlers.put("Heat Sink", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getHeatSinkReplacementDetail(mount); }});
        handlers.put("Shoulder", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getActuatorReplacementDetail(mount); }});
        handlers.put("Upper Arm Actuator", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getActuatorReplacementDetail(mount); }});
        handlers.put("Lower Arm Actuator", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getActuatorReplacementDetail(mount); }});
        handlers.put("Hand Actuator", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getActuatorReplacementDetail(mount); }});
        handlers.put("Hip", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getActuatorReplacementDetail(mount); }});
        handlers.put("Upper Leg Actuator", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getActuatorReplacementDetail(mount); }});
        handlers.put("Lower Leg Actuator", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getActuatorReplacementDetail(mount); }});
        handlers.put("Foot Actuator", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getActuatorReplacementDetail(mount); }});
        
        handlers.put("Sensors", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getSensorsReplacementDetail(mount); }});
        handlers.put("Engine", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getEngineReplacementDetail(mount); }});
        handlers.put("Jump Jet", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getJumpJetReplacementDetail(mount); }});
        handlers.put("Weapon", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getWeaponReplacementDetail(mount); }});
        handlers.put("Ammunition", new ItemReplacementHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getAmmunitionReplacementDetail(mount); }});
        return handlers;
    }

    private ItemRepairDetail getLifeSupportReplacementDetail(ItemMount mount)
    {
    	int destroyed = 0;
    	for (InternalSlotStatus iss : mount.getSlotReferences())
    		if (iss.getStatus() == ItemStatus.DESTROYED)
    			destroyed++;
    	
    	if (destroyed > 0)
    		return new ItemRepairDetail(Type.REPLACEMENT, mount.getMountedItem().getType(), -1, Integer.MIN_VALUE, "", 180, mount.getMountedItem().getCost());
    		
    	return null;
    }
    
    private ItemRepairDetail getCockpitReplacementDetail(ItemMount mount)
    {
    	if (mount.getSlotReferences().elementAt(0).getStatus() == ItemStatus.DESTROYED)
    		return new ItemRepairDetail(Type.REPLACEMENT, mount.getMountedItem().getType(), 0, Integer.MIN_VALUE, "", 120, mount.getMountedItem().getCost());
    		
    	return null;
    }
    
    private ItemRepairDetail getGyroReplacementDetail(ItemMount mount)
    {
    	int destroyed = 0;
    	for (InternalSlotStatus iss : mount.getSlotReferences())
    		if (iss.getStatus() == ItemStatus.DESTROYED)
    			destroyed++;
    	
    	if (destroyed > 0)
    	{
    		return new ItemRepairDetail(Type.REPLACEMENT, mount.getMountedItem().getType(), 0, 2, "+1 Piloting Modifier", 200, mount.getMountedItem().getCost());
    	}

    	return null;
    }
    
    private ItemRepairDetail getHeatSinkReplacementDetail(ItemMount mount)
    {
    	if (mount.getSlotReferences().elementAt(0).getStatus() == ItemStatus.DESTROYED)
    		return new ItemRepairDetail(Type.REPLACEMENT, mount.getMountedItem().getType(), -2, Integer.MIN_VALUE, "", 90, mount.getMountedItem().getCost());
    		
    	return null;
    }   
    
    private ItemRepairDetail getActuatorReplacementDetail(ItemMount mount)
    {
    	if (mount.getSlotReferences().elementAt(0).getStatus() == ItemStatus.DESTROYED)
    		return new ItemRepairDetail(Type.REPLACEMENT, mount.getMountedItem().getType(), -3, Integer.MIN_VALUE, "", 90, mount.getMountedItem().getCost());
    		
    	return null;
    }    
    
    private ItemRepairDetail getSensorsReplacementDetail(ItemMount mount)
    {
    	int destroyed = 0;
    	for (InternalSlotStatus iss : mount.getSlotReferences())
    		if (iss.getStatus() == ItemStatus.DESTROYED)
    			destroyed++;
    	
    	if (destroyed > 0)
    		return new ItemRepairDetail(Type.REPLACEMENT, mount.getMountedItem().getType(), 0, Integer.MIN_VALUE, "", 260, mount.getMountedItem().getCost());

    	return null;
    }    
    
    private ItemRepairDetail getEngineReplacementDetail(ItemMount mount)
    {
    	int destroyed = 0;
    	for (InternalSlotStatus iss : mount.getSlotReferences())
    		if (iss.getStatus() == ItemStatus.DESTROYED)
    			destroyed++;
    	
    	if (destroyed > 0)
    		return new ItemRepairDetail(Type.REPLACEMENT, mount.getMountedItem().getType(), -1, 1, "+1 Heat Point/turn", 360, mount.getMountedItem().getCost());

    	return null;   	
    }    
    
    private ItemRepairDetail getJumpJetReplacementDetail(ItemMount mount)
    {
    	if (mount.getSlotReferences().elementAt(0).getStatus() == ItemStatus.DESTROYED)
    		return new ItemRepairDetail(Type.REPLACEMENT, mount.getMountedItem().getType(), 0, Integer.MIN_VALUE, "", 60, mount.getMountedItem().getCost());
    		
    	return null;
    } 
    
    private ItemRepairDetail getWeaponReplacementDetail(ItemMount mount)
    {
    	int destroyed = 0;
    	for (InternalSlotStatus iss : mount.getSlotReferences())
    		if (iss.getStatus() == ItemStatus.DESTROYED)
    			destroyed++;
    	
    	if (destroyed > 0)
    		return new ItemRepairDetail(Type.REPLACEMENT, mount.getMountedItem().getType(), 0, Integer.MIN_VALUE, "", 120, mount.getMountedItem().getCost());

    	return null;
    }    
    
    private ItemRepairDetail getAmmunitionReplacementDetail(ItemMount mount)
    {
    	
    	return null;
    }   
   
    private HashMap<String, ItemRepairHandler> registerItemRepairHandlers()
    {
        HashMap<String, ItemRepairHandler> handlers = new HashMap<String, ItemRepairHandler>();

        handlers.put("Life Support", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getLifeSupportRepairDetail(mount); }});
        handlers.put("Cockpit", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getCockpitRepairDetail(mount); }});
        handlers.put("Gyro", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getGyroRepairDetail(mount); }});
        handlers.put("Heat Sink", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getHeatSinkRepairDetail(mount); }});
        handlers.put("Shoulder", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getActuatorRepairDetail(mount); }});
        handlers.put("Upper Arm Actuator", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getActuatorRepairDetail(mount); }});
        handlers.put("Lower Arm Actuator", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getActuatorRepairDetail(mount); }});
        handlers.put("Hand Actuator", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getActuatorRepairDetail(mount); }});
        handlers.put("Hip", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getActuatorRepairDetail(mount); }});
        handlers.put("Upper Leg Actuator", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getActuatorRepairDetail(mount); }});
        handlers.put("Lower Leg Actuator", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getActuatorRepairDetail(mount); }});
        handlers.put("Foot Actuator", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getActuatorRepairDetail(mount); }});
        
        handlers.put("Sensors", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getSensorsRepairDetail(mount); }});
        handlers.put("Engine", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getEngineRepairDetail(mount); }});
        handlers.put("Jump Jet", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getJumpJetRepairDetail(mount); }});
        handlers.put("Weapon", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getWeaponRepairDetail(mount); }});
        handlers.put("Ammunition", new ItemRepairHandler() { public ItemRepairDetail getItemRepairDetail(ItemMount mount) { return getAmmunitionRepairDetail(mount); }});
        return handlers;
    }
    
    private ItemRepairDetail getLifeSupportRepairDetail(ItemMount mount)
    {
    	int destroyed = 0;
    	for (InternalSlotStatus iss : mount.getSlotReferences())
    		if (iss.getStatus() == ItemStatus.DESTROYED)
    			destroyed++;
    	
    	if (destroyed == 1)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), -1, Integer.MIN_VALUE, "", 60, 0);
    	else if (destroyed == 2)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), 1, Integer.MIN_VALUE, "", 120, 0);
    		
    	return null;
    }
    
    private ItemRepairDetail getCockpitRepairDetail(ItemMount mount)
    {
    	if (mount.getSlotReferences().elementAt(0).getStatus() == ItemStatus.DESTROYED)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), -3, Integer.MIN_VALUE, "", 100, 0);
    		
    	return null;
    }
    
    private ItemRepairDetail getGyroRepairDetail(ItemMount mount)
    {
    	int destroyed = 0;
    	for (InternalSlotStatus iss : mount.getSlotReferences())
    		if (iss.getStatus() == ItemStatus.DESTROYED)
    			destroyed++;
    	
    	if (destroyed == 1)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), 1, 3, "+1 Piloting Modifier", 120, 0);
    	else if (destroyed == 2)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), 4, 4, "+2 Piloting Modifier", 240, 0);

    	return null;
    }
    
    private ItemRepairDetail getHeatSinkRepairDetail(ItemMount mount)
    {
    	if (mount.getSlotReferences().elementAt(0).getStatus() == ItemStatus.DESTROYED)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), -1, 3, "Heat sink works at 1/2 capacity", 90, 0);
    		
    	return null;
    }   
    
    private ItemRepairDetail getActuatorRepairDetail(ItemMount mount)
    {
    	if (mount.getSlotReferences().elementAt(0).getStatus() == ItemStatus.DESTROYED)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), 0, Integer.MIN_VALUE, "", 120, 0);
    		
    	return null;
    }    
    
    private ItemRepairDetail getSensorsRepairDetail(ItemMount mount)
    {
    	int destroyed = 0;
    	for (InternalSlotStatus iss : mount.getSlotReferences())
    		if (iss.getStatus() == ItemStatus.DESTROYED)
    			destroyed++;
    	
    	if (destroyed == 1)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), 0, 3, "+1 to-hit Modifier", 75, 0);
    	else if (destroyed == 2)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), 3, 4, "+2 to-hit Modifier", 150, 0);

    	return null;
    }    
    
    private ItemRepairDetail getEngineRepairDetail(ItemMount mount)
    {
    	int destroyed = 0;
    	for (InternalSlotStatus iss : mount.getSlotReferences())
    		if (iss.getStatus() == ItemStatus.DESTROYED)
    			destroyed++;
    	
    	if (destroyed == 1)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), -1, 2, "+3 Heat Points/turn", 100, 0);
    	else if (destroyed == 2)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), 0, 3, "+5 Heat Points/turn", 200, 0);
    	else if (destroyed == 3)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), 2, 4, "+8 Heat Points/turn", 300, 0);

    	return null;   	
    }    
    
    private ItemRepairDetail getJumpJetRepairDetail(ItemMount mount)
    {
    	if (mount.getSlotReferences().elementAt(0).getStatus() == ItemStatus.DESTROYED)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), 0, 1, "+1 Heat Point when jumping", 90, 0);
    		
    	return null;
    } 
    
    private ItemRepairDetail getWeaponRepairDetail(ItemMount mount)
    {
    	int destroyed = 0;
    	for (InternalSlotStatus iss : mount.getSlotReferences())
    		if (iss.getStatus() == ItemStatus.DESTROYED)
    			destroyed++;
    	
    	if (destroyed == 1)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), -3, Integer.MIN_VALUE, "", 100, 0);
    	else if (destroyed == 2)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), -2, Integer.MIN_VALUE, "", 150, 0);
    	else if (destroyed == 3)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), 0, Integer.MIN_VALUE, "", 200, 0);
    	else if (destroyed >= 4)
    		return new ItemRepairDetail(Type.REPAIR, mount.getMountedItem().getType(), 2, Integer.MIN_VALUE, "", 250, 0);

    	return null;
    }    
    
    private ItemRepairDetail getAmmunitionRepairDetail(ItemMount mount)
    {
    	
    	return null;
    }   
    
    public BattlemechRepairReport createRepairReport(Battlemech mech, int modifiedSkillTarget)
    {
    	BattlemechRepairReport report = new BattlemechRepairReport(mech, modifiedSkillTarget);

    	doSectionsReplacementReport(mech, report);
    	doArmourReplacementReport(mech, report);
    	doItemsReplacementReport(mech, report);

    	doInternalRepairReport(mech , report);
    	doItemsRepairReport(mech, report);
    	doSectionsRepairReport(mech, report);
    	
    	return report;
    }
    
    private void doSectionsReplacementReport(Battlemech mech, BattlemechRepairReport report)
    {    	
    	for (BattlemechSection section : mech.getSectionStatuses().keySet())
    	{
    		SectionStatus status = mech.getSectionStatuses().get(section);
    		if (status.getStatus() == SectionStatus.Status.BLOWNOFF)
    		{
    			if (section == BattlemechSection.HEAD)
    			{
        			ItemRepairDetail ird = new ItemRepairDetail(ItemRepairDetail.Type.REPLACEMENT, section.toString() + " - Reattach/Replace", 2, Integer.MIN_VALUE, "", 200, 0);
    				report.addSectionReplacementDetail(ird);    				
    			}
    			else
    			{
        			ItemRepairDetail ird = new ItemRepairDetail(ItemRepairDetail.Type.REPLACEMENT, section.toString() + " - Reattach/Replace", 1, Integer.MIN_VALUE, "", 180, 0);
    				report.addSectionReplacementDetail(ird);    				    				
    			}
    		} else if (status.getStatus() == SectionStatus.Status.DESTROYED)
    		{
    			ItemRepairDetail ird = new ItemRepairDetail(ItemRepairDetail.Type.REPLACEMENT, section.toString() + " - Replace", 3, Integer.MIN_VALUE, "", 240, 0);
				report.addSectionReplacementDetail(ird);
    		}
    	}    	
    }
    
    private void doArmourReplacementReport(Battlemech mech, BattlemechRepairReport report)
    {
    	for (String location : mech.getArmour().keySet())
    	{
    		int destroyed = 0;
    		
    		HashMap<Integer, ItemStatus> statuses = mech.getArmour().get(location);
    		for (Integer index : statuses.keySet())
    		{
    			ItemStatus status = statuses.get(index);
    			
    			if (status == ItemStatus.DESTROYED)
    				destroyed += 1;
    		}
    		
    		if (destroyed > 0)
    		{
    			int cost = (int)(10000.0 * (double)destroyed / 16.0);
    			ItemRepairDetail ird = new ItemRepairDetail(ItemRepairDetail.Type.REPLACEMENT, "Armour", -2, Integer.MIN_VALUE, "", 5 * destroyed, cost);
    			report.addArmourReplacementDetail(location, ird);
    		}
    	}
    }
    
    private void doItemsReplacementReport(Battlemech mech, BattlemechRepairReport report)
    {    	
    	for (ItemMount mount : mech.getItems())
    	{
    		String mountedItemType = mount.getMountedItem().getType();
    		if (_ItemReplacementHandlers.containsKey(mountedItemType))
    		{
    			ItemRepairDetail ird = _ItemReplacementHandlers.get(mountedItemType).getItemRepairDetail(mount);
    			if (ird != null)
    			{
    				report.addItemReplacementDetail(ird);
    			}
    		}
    	}
    }
    
    private void doInternalRepairReport(Battlemech mech, BattlemechRepairReport report)
    {
    	for (String location: mech.getInternals().keySet())
    	{
    		int destroyed = 0;
    		int total = 0;
    		HashMap<Integer, ItemStatus> statuses = mech.getInternals().get(location);
    		for (Integer index : statuses.keySet())
    		{
    			ItemStatus status = statuses.get(index);
    			
    			if (status == ItemStatus.DESTROYED)
    				destroyed += 1;
    			
    			total++;
    		}
    		
    		if (destroyed > 0)
    		{
    			ItemRepairDetail ird;
    			
    			int cost = (int)(400.0 * (double)destroyed / 16.0);

    			double percent = (double)total / (double) destroyed;
    			if (percent < 0.25)
    				ird = new ItemRepairDetail(ItemRepairDetail.Type.REPAIR, "Internal Structure", -1, 1, "1 pt permanent damage", 90, cost);
    			else if (percent < 0.50)
    				ird = new ItemRepairDetail(ItemRepairDetail.Type.REPAIR, "Internal Structure", 0, 1, "2 pts permanent damage", 135, cost);
    			else if (percent < 0.75)
    				ird = new ItemRepairDetail(ItemRepairDetail.Type.REPAIR, "Internal Structure", 1, 2, "3 pts permanent damage", 180, cost);
    			else
    				ird = new ItemRepairDetail(ItemRepairDetail.Type.REPAIR, "Internal Structure", 2, 2, "4 pts permanent damage", 270, cost);
    			 
    			report.addInternalRepairDetail(location, ird);
    		}
    	}
    }
    
    private void doItemsRepairReport(Battlemech mech, BattlemechRepairReport report)
    {    	
    	for (ItemMount mount : mech.getItems())
    	{
    		String mountedItemType = mount.getMountedItem().getType();
    		if (_ItemRepairHandlers.containsKey(mountedItemType))
    		{
    			ItemRepairDetail ird = _ItemRepairHandlers.get(mountedItemType).getItemRepairDetail(mount);
    			if (ird != null)
    			{
    				report.addItemRepairDetail(ird);
    			}
    		}
    	}
    }

    private void doSectionsRepairReport(Battlemech mech, BattlemechRepairReport report)
    {    	
    	for (BattlemechSection section : mech.getSectionStatuses().keySet())
    	{
    		SectionStatus status = mech.getSectionStatuses().get(section);
    		if (status.isBreached())
    		{
    			ItemRepairDetail ird = new ItemRepairDetail(ItemRepairDetail.Type.REPAIR, section.toString() + " - BREACHED", -5, Integer.MIN_VALUE, "", 60, 0);
				report.addSectionRepairDetail(ird);
    		}
    	}    	
    }
}
