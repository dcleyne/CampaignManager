package bt.elements;

import java.util.HashMap;
import java.util.Vector;

public class Battlemech extends Asset
{
	private static final String QUAD = "Quad";

	public static String[] HeatScale = {
		"_[30] Shutdown",
		"_[29] ",
		"_[28] Ammo Explosion, avoid on 8+",
		"_[27] ",
		"_[26] Shutdown, avoid on 10+",
		"_[25] -5 Movement points",
		"_[24] +4 Modifier to fire",
		"_[23] Ammo explosion, avoid on 6+",
		"_[22] Shutdown, avoid on 8+",
		"_[21] ",
		"_[20] -4 Movement points",
		"_[19] Ammo explosion, avoid on 4+",
		"_[18] Shutdown, avoid on 6+",
		"_[17] +3 Modifier to fire",
		"_[16] ",
		"_[15] -3 Movement points",
		"_[14] Shutdown, avoid on 4+",
		"_[13] +2 Modifier to fire",
		"_[12] ",
		"_[11] ",
		"_[10] -2 Movement points",
		"_[09] ",
		"_[08] +1 Modifier to fire",
		"_[07] ",
		"_[06] ",
		"_[05] -1 Movement point",
		"_[04] ",
		"_[03] ",
		"_[02] ",
		"_[01] ",
		"_[00] ",
	};
	
	private String _Name;
    private String _DesignName;
    private String _DesignVariant;

    private String _Type;
    private int _Weight;

    private HashMap<BattlemechSection, SectionStatus> _SectionStatuses = new HashMap<BattlemechSection, SectionStatus>();
    private HashMap<String, HashMap<Integer, ItemStatus>> _Internals = new HashMap<String, HashMap<Integer, ItemStatus>>();
    private HashMap<String, HashMap<Integer, ItemStatus>> _Armour = new HashMap<String, HashMap<Integer, ItemStatus>>();
    private Vector<ItemMount> _Items = new Vector<ItemMount>();
    
	public String getName()
	{
		return _Name;
	}
	public void setName(String name)
	{
		_Name = name;
	}
	public String getDesignName()
	{
		return _DesignName;
	}
	public void setDesignName(String designName)
	{
		_DesignName = designName;
	}
	public String getDesignVariant()
	{
		return _DesignVariant;
	}
	public void setDesignVariant(String designVariant)
	{
		_DesignVariant = designVariant;
	}
	public String getType()
	{
		return _Type;
	}
	public void setType(String type)
	{
		_Type = type;
	}
	public int getWeight()
	{
		return _Weight;
	}
	public void setWeight(int weight)
	{
		_Weight = weight;
	}
	public HashMap<String, HashMap<Integer, ItemStatus>> getInternals()
	{
		return _Internals;
	}
	public void setInternals(HashMap<String, HashMap<Integer, ItemStatus>> internals)
	{
		_Internals = internals;
	}
	public HashMap<String, HashMap<Integer, ItemStatus>> getArmour()
	{
		return _Armour;
	}
	public void setArmour(HashMap<String, HashMap<Integer, ItemStatus>> armour)
	{
		_Armour = armour;
	}
	public HashMap<BattlemechSection, SectionStatus> getSectionStatuses() 
	{
		return _SectionStatuses;
	}
	public void setSectionStatuses(HashMap<BattlemechSection, SectionStatus> sectionStatuses) 
	{
		_SectionStatuses = sectionStatuses;
	}
	public Vector<ItemMount> getItems()
	{
		return _Items;
	}
	public void setItems(Vector<ItemMount> items)
	{
		_Items = items;
	}
	
	public Vector<ItemMount> getAllMountsForInternalLocation(String locationName)
	{
		Vector<ItemMount> mounts = new Vector<ItemMount>();
		
		for (ItemMount im : _Items)
		{
			for (InternalSlotStatus iss : im.getSlotReferences())
				if (iss.getInternalLocation().equalsIgnoreCase(locationName))
					if (!mounts.contains(im))
						mounts.add(im);
		}
		
		return mounts;
	}

	public ItemMount getItemMount(String itemName, String locationName)
	{
		Vector<ItemMount> mounts = getAllMountsForInternalLocation(locationName);
		
		for (ItemMount im : mounts)
		{
			if (im.getMountedItem().getType().equalsIgnoreCase(itemName))
				return im;
		}
		
		return null;
	}

	public ItemMount getItemMount(String itemString)
	{
		for (ItemMount im : _Items)
		{
			if (im.toString().equalsIgnoreCase(itemString))
				return im;
		}
		
		return null;
	}

	public Vector<ItemMount> getAllWeaponMounts()
	{
		return getAllMounts("Weapon");
	}
	
	public Vector<ItemMount> getAllAmmunitionMounts()
	{
		return getAllMounts("Ammunition");
	}
	
	public Vector<ItemMount> getAllHeatSinkMounts()
	{
		return getAllMounts("Heat Sink");
	}
	
	public Vector<ItemMount> getAllMounts(String itemName)
	{
		Vector<ItemMount> mounts = new Vector<ItemMount>();
		
		for (ItemMount im : _Items)
		{
			if (im.getMountedItem().getType().equalsIgnoreCase(itemName))
				mounts.add(im);
		}
		return mounts;
	}
	
	public int getItemHits(String itemName)
	{
		int engineHits = 0;
		Vector<ItemMount> engineMounts = getAllMounts(itemName);
		if (engineMounts.size() > 0)
		{
			ItemMount mount = engineMounts.elementAt(0);
			for (InternalSlotStatus iss : mount.getSlotReferences())
			{
				if (iss.getStatus() != ItemStatus.OK)
					engineHits++;
			}
		}
		
		return engineHits;
	}

	public int getEngineHits()
	{
		return getItemHits("Engine");
	}
	
	public int getGyroHits()
	{
		return getItemHits("Gyro");
	}
	
	public int getSensorHits()
	{
		return getItemHits("Sensors");
	}
	
	public boolean isLifeSupportHit()
	{
		return getItemHits("Life Support") > 0;		
	}

	public int getWalkRating()
	{
		int walk = 0;
		HashMap<String, ItemMount> hips = new HashMap<String, ItemMount>();
		if (_Type.equalsIgnoreCase(QUAD))
		{
			hips.put("Left Front Leg", getItemMount("Hip","Left Front Leg"));
			hips.put("Right Front Leg", getItemMount("Hip","Right Front Leg"));
			hips.put("Left Rear Leg", getItemMount("Hip","Left Rear Leg"));
			hips.put("Right Rear Leg", getItemMount("Hip","Right Rear Leg"));
		}
		else
		{
			hips.put("Left Leg", getItemMount("Hip","Left Leg"));
			hips.put("Right Leg", getItemMount("Hip","Right Leg"));
		}

		int hipsDamaged = 0;
		for (ItemMount hip : hips.values())
		{
			if (hip.isDamaged())
				hipsDamaged++;
		}
		if (hipsDamaged > 1)
			return 0;

		ItemMount engine = getItemMount("Engine","Centre Torso");
		walk = ((Engine)engine.getMountedItem()).getRating() / getWeight();
		
		Vector<ItemMount> legBits = new Vector<ItemMount>();

		for (String legLocation : hips.keySet())
		{
			ItemMount hip = hips.get(legLocation);
			if (!hip.isDamaged())
			{
				legBits.add(getItemMount("Upper Leg Actuator", legLocation));
				legBits.add(getItemMount("Lower Leg Actuator", legLocation));
				legBits.add(getItemMount("Foot Actuator", legLocation));
			}
			else
				walk = Math.round((walk / 2.0f) + 0.49f);
		}
		
		for (ItemMount legPart : legBits)
			if (legPart.isDamaged())
				walk -= 1;

		if (walk < 0)
			walk = 0;
		
		return walk;
	}
	
	public int getRunRating()
	{
		return Math.round((getWalkRating() * 1.5f + 0.49f));
	}
	
	public int getJumpRating()
	{
		int jumpRating = 0;
		Vector<ItemMount> jumpJets = getAllMounts("Jump Jet");
		for (ItemMount jet : jumpJets)
			if (!jet.isDamaged())
				jumpRating++;
		
		return jumpRating;
	}
	
	public int getIntegralHeatSinks()
	{
		ItemMount engine = getItemMount("Engine","Centre Torso");
		int purchasedEngineHeatsinks = ((Engine)engine.getMountedItem()).getHeatSinks();
		int integralHeatSinks = (int)(((Engine)engine.getMountedItem()).getRating() / 25);
		if (purchasedEngineHeatsinks > integralHeatSinks) purchasedEngineHeatsinks = integralHeatSinks;
		return purchasedEngineHeatsinks;
	}
	
	public int getTotalHeatSinks()
	{
		int excessHS = 10 - getIntegralHeatSinks();
		
		Vector<ItemMount> heatSinks = getAllHeatSinkMounts();
		return 10 + (heatSinks.size() - excessHS);
	}

	@Override
	public ElementType getElementType() 
	{
		return ElementType.BATTLEMECH;
	}
	
	@Override
	public Asset.Status getStatus()
	{
		Asset.Status status = Asset.Status.OK;
		
		for (BattlemechSection section: _SectionStatuses.keySet())
		{
			if (_SectionStatuses.get(section).getStatus() != SectionStatus.Status.OK)
			{
				status = getWorstStatus(status, Asset.Status.DAMAGED);
			}
		}		
		
		if (_SectionStatuses.get(BattlemechSection.LEFT_TORSO).getStatus() == SectionStatus.Status.DESTROYED || 
				_SectionStatuses.get(BattlemechSection.RIGHT_TORSO).getStatus() == SectionStatus.Status.DESTROYED)
		{
			status = getWorstStatus(status, Asset.Status.CRIPPLED);
		}

		if (getEngineHits() == 2 || (getEngineHits() == 1 && getGyroHits() == 1) || getSensorHits() == 2)
			status = getWorstStatus(status, Asset.Status.CRIPPLED);

		int limbInternalDamageCount = 0;
		if (_Type.equalsIgnoreCase(QUAD))
		{
			limbInternalDamageCount += _SectionStatuses.get(BattlemechSection.LEFT_FRONT_LEG).getStatus() != SectionStatus.Status.OK ? 1 : 0;
			limbInternalDamageCount += _SectionStatuses.get(BattlemechSection.RIGHT_FRONT_LEG).getStatus() != SectionStatus.Status.OK ? 1 : 0;
			limbInternalDamageCount += _SectionStatuses.get(BattlemechSection.LEFT_REAR_LEG).getStatus() != SectionStatus.Status.OK ? 1 : 0;
			limbInternalDamageCount += _SectionStatuses.get(BattlemechSection.RIGHT_REAR_LEG).getStatus() != SectionStatus.Status.OK ? 1 : 0;			
		}
		else
		{
			limbInternalDamageCount += _SectionStatuses.get(BattlemechSection.LEFT_LEG).getStatus() != SectionStatus.Status.OK ? 1 : 0;
			limbInternalDamageCount += _SectionStatuses.get(BattlemechSection.RIGHT_LEG).getStatus() != SectionStatus.Status.OK ? 1 : 0;
			limbInternalDamageCount += _SectionStatuses.get(BattlemechSection.LEFT_ARM).getStatus() != SectionStatus.Status.OK ? 1 : 0;
			limbInternalDamageCount += _SectionStatuses.get(BattlemechSection.RIGHT_ARM).getStatus() != SectionStatus.Status.OK ? 1 : 0;
		}
		if (limbInternalDamageCount >= 3)
		{
			status = getWorstStatus(status, Asset.Status.CRIPPLED);			
		}
		
		int undamagedWeaponsWithRangeOfFive = 0;
		int undamagedWeaponsDamagePoints = 0;
		Vector<ItemMount> weaponMounts = getAllWeaponMounts();
		for (ItemMount im : weaponMounts)
		{
			if (im.getWorstStatus().isUsable())
			{
				Weapon w = (Weapon)im.getMountedItem();
				if (w.getMaxRange() > 5)
				{
					undamagedWeaponsWithRangeOfFive++;
				}
				undamagedWeaponsDamagePoints += w.getMaxDamagePoints();
			}
		}
		
		if (undamagedWeaponsWithRangeOfFive == 0 || undamagedWeaponsDamagePoints < 5)
			status = getWorstStatus(status, Asset.Status.CRIPPLED);			
		
		if (_SectionStatuses.get(BattlemechSection.CENTER_TORSO).getStatus() == SectionStatus.Status.DESTROYED)
			status = getWorstStatus(status, Asset.Status.DESTROYED);
		
		// TODO add calc for armour and internal damage 
		
		return status;
	}
	
	private Asset.Status getWorstStatus(Asset.Status s1, Asset.Status s2)
	{
		int maxOrd = Math.max(s1.ordinal(), s2.ordinal());
		return Asset.Status.values()[maxOrd];
	}
		
	public String getModelInformation() 
	{
		return _DesignVariant + " " + _DesignName;
	}
	
	public void applyDamage(Vector<BattlemechDamageNotation> damageNotations)
	{
        Vector<BattlemechDamageNotation> internalNotations = BattlemechDamageNotation.getDamageNotationsForArea("Internals", damageNotations);
        for (BattlemechDamageNotation internalNotation : internalNotations)
        {
        	HashMap<Integer, ItemStatus> areaMap = _Internals.get(internalNotation.getLocation());
        	areaMap.put(internalNotation.getIndex(), internalNotation.getStatus());
        }
                
        Vector<BattlemechDamageNotation> armourNotations = BattlemechDamageNotation.getDamageNotationsForArea("Armour", damageNotations);
        for (BattlemechDamageNotation armourNotation : armourNotations)
        {
        	HashMap<Integer, ItemStatus> areaMap = _Armour.get(armourNotation.getLocation());
        	areaMap.put(armourNotation.getIndex(), armourNotation.getStatus());
        }
        
        Vector<BattlemechDamageNotation> heatsinkNotations = BattlemechDamageNotation.getDamageNotationsForArea("HeatSinks", damageNotations);
        
        Vector<BattlemechDamageNotation> equipmentNotations = new Vector<BattlemechDamageNotation>(damageNotations);
        equipmentNotations.removeAll(internalNotations);
        equipmentNotations.removeAll(armourNotations);
        equipmentNotations.removeAll(heatsinkNotations);

        for (BattlemechDamageNotation equipmentNotation : equipmentNotations)
        {
        	ItemMount im = getItemMount(equipmentNotation.getLocation());
        	if (im != null)
        	{
	    		InternalSlotStatus iss = im.getSlotReferences().get(equipmentNotation.getIndex());
	    		iss.setStatus(equipmentNotation.getStatus());
        	}
        }

	}
	

	
}
