package bt.elements;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

public class Battlemech extends Asset implements BattleValue
{
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
	
	private static double _SpeedFactor[] = {0.44,0.54,0.65,0.77,0.88,1,1.12,1.24,
		1.37,1.5,1.63,1.76,1.89,2.02,2.16,2.3,2.44,2.58,2.72,2.86,3,3.15,3.29,3.44,
		3.59,3.74};
	
	private String _Name;
    private String _DesignName;
    private String _DesignVariant;
    private String _Manufacturer;

    private String _Type;
    private int _Weight;
    private int _BV;

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
	public String getManufacturer()
	{
		return _Manufacturer;
	}
	public void setManufacturer(String manufacturer)
	{
		_Manufacturer = manufacturer;
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
	public int getBV()
	{
		return _BV;
	}
	public void setBV(int bV)
	{
		_BV = bV;
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
	
	private int getMoveModifier(int move)
	{
		if (move < 3) return 0;
		if (move < 5) return 1;
		if (move < 7) return 2;
		if (move < 10) return 3;
		return 4;
	}
	
	private double getMaxMoveModifier(int run, int jump)
	{
		int mod = Math.max(getMoveModifier(run), getMoveModifier(jump) + 1);
		return 1 + ((double)mod / 10.0);
	}
	
	private int getMaxMovementHeat()
	{
		return Math.max(2, getJumpRating());
	}
	
	private Vector<ItemMount> getWeaponsByBV()
	{
		Vector<ItemMount> weapons = getAllWeaponMounts();
		for (int i = weapons.size() - 1; i >= 0; i--)
		{
			ItemMount mount = weapons.elementAt(i);
			if (mount.isDamaged())
				weapons.remove(i);
		}
		
		Collections.sort(weapons, new Comparator<ItemMount>() 
		{
			public int compare(ItemMount m1, ItemMount m2)
			{
				Weapon w1 = (Weapon)m1.getMountedItem();
				Weapon w2 = (Weapon)m2.getMountedItem();
				
				int bv1 = w1.getBV();
				int bv2 = w2.getBV();
				if (bv1 == bv2)
				{
					Integer.compare(w1.getHeat(), w1.getHeat());
				}
				return Integer.compare(bv1, bv2);
			}
		});
		Collections.reverse(weapons);
		
		return weapons;
	}
	

	private double getSpeedFactor()
	{
		int rating = getRunRating() + (getJumpRating() / 2);
		return _SpeedFactor[rating];
	}
	
	@Override
	public int getAdjustedBV()
	{
		double defensiveBV = 0;
		int armourFactors = 0;
		for (String location : _Armour.keySet())
		{
			for (Integer index : _Armour.get(location).keySet())
			{
				if (_Armour.get(location).get(index) == ItemStatus.OK)
				{
					armourFactors++;
				}
			}
		}
		defensiveBV += (double)armourFactors * 2;
		
		int internalFactors = 0;
		for (String location : _Internals.keySet())
		{
			for (Integer index : _Internals.get(location).keySet())
			{
				if (_Internals.get(location).get(index) == ItemStatus.OK)
				{
					internalFactors++;
				}
			}
		}
		defensiveBV += (double)internalFactors * 1.5;
		
		defensiveBV += (double)_Weight * 0.5;
		
		int numAmmoSlots = 0;
		Vector<ItemMount> ammunitionMounts = getAllAmmunitionMounts();
		for (ItemMount mount : ammunitionMounts)
		{
			if (!mount.isDamaged())
				numAmmoSlots++;
		}
		
		defensiveBV -= (20 * numAmmoSlots);
		defensiveBV *= getMaxMoveModifier(getRunRating(), getJumpRating());
		
		double offensiveBV = 0;
		int heatEfficiency = 6 + getTotalHeatSinks() - getMaxMovementHeat();
		
		int weaponHeatGenerated = 0;
		int weaponBV = 0;
		Vector<ItemMount> weapons = getWeaponsByBV();
		for (ItemMount mount : weapons)
		{
			Weapon w = (Weapon)mount.getMountedItem();
			if (weaponHeatGenerated < heatEfficiency)
			{
				weaponHeatGenerated += w.getHeat();
				if (!mount.isRearFacing())
					weaponBV += w.getBV();
				else
					weaponBV += w.getBV() / 2;					
			}
			else
			{
				weaponHeatGenerated += w.getHeat();
				weaponBV += w.getBV() / 2;				
			}
		}
		
		for (ItemMount mount : getAllAmmunitionMounts())
		{
			Ammunition ammo = (Ammunition)mount.getMountedItem();
			offensiveBV += ammo.getBV();
		}
		
		offensiveBV += weaponBV;
		offensiveBV += _Weight;
		offensiveBV *= getSpeedFactor();
		
		return (int) Math.round(offensiveBV + defensiveBV);
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

	public int getWalkRating()
	{
		ItemMount leftHip = getItemMount("Hip","Left Leg");
		ItemMount rightHip = getItemMount("Hip","Right Leg");
		
		if (leftHip.isDamaged() && rightHip.isDamaged())
			return 0;

		ItemMount engine = getItemMount("Engine","Centre Torso");
		int walk = ((Engine)engine.getMountedItem()).getRating() / getWeight();
		
		Vector<ItemMount> legBits = new Vector<ItemMount>();
		if (!leftHip.isDamaged())
		{
			legBits.add(getItemMount("Upper Leg Actuator","Left Leg"));
			legBits.add(getItemMount("Lower Leg Actuator","Left Leg"));
			legBits.add(getItemMount("Foot Actuator","Left Leg"));
		}
		else
			walk = Math.round((walk / 2.0f) + 0.49f);
		
		if (!rightHip.isDamaged())
		{
			legBits.add(getItemMount("Upper Leg Actuator","Right Leg"));
			legBits.add(getItemMount("Lower Leg Actuator","Right Leg"));
			legBits.add(getItemMount("Foot Actuator","Right Leg"));
		}
		else
			walk = Math.round((walk / 2.0f) + 0.49f);

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
		int integralHeatSinks = (int)(((Engine)engine.getMountedItem()).getRating() / 25);
		if (integralHeatSinks > 10) integralHeatSinks = 10;
		return integralHeatSinks;
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
	public String getStatus()
	{
		// TODO Auto-generated method stub
		return "Functional";
	}
	
	@Override
	public String getCondition() 
	{
		// TODO Auto-generated method stub
		return "Add Condition Calc";
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
        	areaMap.put(new Integer(internalNotation.getIndex()), internalNotation.getStatus());
        }
                
        Vector<BattlemechDamageNotation> armourNotations = BattlemechDamageNotation.getDamageNotationsForArea("Armour", damageNotations);
        for (BattlemechDamageNotation armourNotation : armourNotations)
        {
        	HashMap<Integer, ItemStatus> areaMap = _Armour.get(armourNotation.getLocation());
        	areaMap.put(new Integer(armourNotation.getIndex()), armourNotation.getStatus());
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
