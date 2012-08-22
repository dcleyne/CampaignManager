package bt.elements;

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
	
    private String _DesignName;
    private String _DesignVariant;
    private String _Manufacturer;

    private String _Type;
    private int _Weight;
    private int _BV;

    private HashMap<String, HashMap<Integer, ItemStatus>> _Internals = new HashMap<String, HashMap<Integer, ItemStatus>>();
    private HashMap<String, HashMap<Integer, ItemStatus>> _Armour = new HashMap<String, HashMap<Integer, ItemStatus>>();
    private Vector<ItemMount> _Items = new Vector<ItemMount>();
    
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
	public Vector<ItemMount> getItems()
	{
		return _Items;
	}
	public void setItems(Vector<ItemMount> items)
	{
		_Items = items;
	}
	
	@Override
	public int getAdjustedBV()
	{
		// TODO This routine has to factor in damage;
		return _BV;
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
	
}
