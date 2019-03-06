package bt.elements.design;

import java.util.Vector;

import java.util.HashMap;

import bt.elements.BattleValue;
import bt.elements.BattlemechSection;

public class BattlemechDesign implements BattleValue
{
	public enum Type
	{
		BIPED,
		QUAD,
		LAM,
		OMNI;
		
		private static String[] _Names = {"Biped","Quad","LAM","Omni"};
		
	    public String toString()
	    { return _Names[ordinal()]; }

	    public static Type fromString(String r)
	    {
	    	
	    	for (int i = 0; i < _Names.length; i++)
	    	{
	    		if (_Names[i].equalsIgnoreCase(r))
	    			return values()[i];
	    	}
	    	return null;
	    }
	}

    private static BattlemechSection[] _SectionsBiped = { BattlemechSection.HEAD, BattlemechSection.CENTER_TORSO, BattlemechSection.LEFT_TORSO, BattlemechSection.RIGHT_TORSO, BattlemechSection.LEFT_ARM, BattlemechSection.RIGHT_ARM, BattlemechSection.LEFT_LEG, BattlemechSection.RIGHT_LEG };
    private static BattlemechSection[] _SectionsQuad = { BattlemechSection.HEAD, BattlemechSection.CENTER_TORSO, BattlemechSection.LEFT_TORSO, BattlemechSection.RIGHT_TORSO, BattlemechSection.LEFT_FRONT_LEG, BattlemechSection.RIGHT_FRONT_LEG, BattlemechSection.LEFT_REAR_LEG, BattlemechSection.RIGHT_REAR_LEG };
    private static BattlemechSection[] _SectionsLAM = { BattlemechSection.HEAD, BattlemechSection.CENTER_TORSO, BattlemechSection.LEFT_TORSO, BattlemechSection.RIGHT_TORSO, BattlemechSection.LEFT_ARM, BattlemechSection.RIGHT_ARM, BattlemechSection.LEFT_LEG, BattlemechSection.RIGHT_LEG  };
    private static BattlemechSection[] _SectionsOmni = { BattlemechSection.HEAD, BattlemechSection.CENTER_TORSO, BattlemechSection.LEFT_TORSO, BattlemechSection.RIGHT_TORSO, BattlemechSection.LEFT_ARM, BattlemechSection.RIGHT_ARM, BattlemechSection.LEFT_LEG, BattlemechSection.RIGHT_LEG  };

    private static String[] _InternalLocationsBiped = { "Head", "Centre Torso", "Left Torso", "Right Torso", "Left Arm", "Right Arm", "Left Leg", "Right Leg" };
    private static String[] _InternalLocationsQuad = { "Head", "Centre Torso", "Left Torso", "Right Torso", "Left Front Leg", "Right Front Leg", "Left Rear Leg", "Right Rear Leg" };
    private static String[] _InternalLocationsLAM = { "Head", "Centre Torso", "Left Torso", "Right Torso", "Left Arm", "Right Arm", "Left Leg", "Right Leg" };
    private static String[] _InternalLocationsOmni = { "Head", "Centre Torso", "Left Torso", "Right Torso", "Left Arm", "Right Arm", "Left Leg", "Right Leg" };

    private static String[] _ArmourLocationsBiped = { "Head", "Centre Torso", "Centre Torso Rear", "Left Torso", "Left Torso Rear", "Right Torso", "Right Torso Rear", "Left Arm", "Right Arm", "Left Leg", "Right Leg" };
    private static String[] _ArmourLocationsQuad = { "Head", "Centre Torso", "Centre Torso Rear", "Left Torso", "Left Torso Rear", "Right Torso", "Right Torso Rear", "Left Front Leg", "Right Front Leg", "Left Rear Leg", "Right Rear Leg" };
    private static String[] _ArmourLocationsLAM = { "Head", "Centre Torso", "Centre Torso Rear", "Left Torso", "Left Torso Rear", "Right Torso", "Right Torso Rear", "Left Arm", "Right Arm", "Left Leg", "Right Leg" };
    private static String[] _ArmourLocationsOmni = { "Head", "Centre Torso", "Centre Torso Rear", "Left Torso", "Left Torso Rear", "Right Torso", "Right Torso Rear", "Left Arm", "Right Arm", "Left Leg", "Right Leg" };


    private String _Name;
    private String _Variant;
    private String _Role;
    private String _Manufacturer;
    private BattlemechDesign.Type _Type;
    private int _Weight;
    private int _Cost;
    private int _BV;
    private HashMap<String, Integer> _Armour = new HashMap<String,Integer>();

    private Vector<DesignItem> _Items = new Vector<DesignItem>();

	public String getName()
	{
		return _Name;
	}
	public void setName(String name)
	{
		_Name = name;
	}
	public String getVariant()
	{
		return _Variant;
	}
	public void setVariant(String variant)
	{
		_Variant = variant;
	}
	public String getRole()
	{
		return _Role;
	}
	public void setRole(String role)
	{
		_Role = role;
	}
	public String getManufacturer()
	{
		return _Manufacturer;
	}
	public void setManufacturer(String manufacturer)
	{
		_Manufacturer = manufacturer;
	}
	public Type getType()
	{
		return _Type;
	}
	public void setType(Type type)
	{
        if (type != Type.BIPED && type != Type.QUAD && type != Type.LAM && type != Type.OMNI)
            throw new IllegalArgumentException("Cannot set type other than Biped, Quad, LAM or Omni - " + type);

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
	public int getCost()
	{
		return _Cost;
	}
	public void setCost(int cost)
	{
		_Cost = cost;
	}
	
	public int getBV()
	{
		return _BV;
	}
	public void setBV(int bV)
	{
		_BV = bV;
	}
	public HashMap<String, Integer> getArmour()
	{
		return _Armour;
	}
	public void setArmour(HashMap<String, Integer> armour)
	{
		_Armour = armour;
	}
	public Vector<DesignItem> getItems()
	{
		return _Items;
	}
	public void setItems(Vector<DesignItem> items)
	{
		_Items = items;
	}
	public BattlemechSection[] getSections()
	{
		switch (_Type)
		{
			case BIPED:
				return _SectionsBiped;
			case QUAD:
				return _SectionsQuad;
			case LAM:
				return _SectionsLAM;
			case OMNI:
				return _SectionsOmni;
		}
		return null;
	}
	
	public String[] getInternalLocations()
	{
		switch (_Type)
		{
			case BIPED:
				return _InternalLocationsBiped;
			case QUAD:
				return _InternalLocationsQuad;
			case LAM:
				return _InternalLocationsLAM;
			case OMNI:
				return _InternalLocationsOmni;
		}
		return null;
	}

	public String[] getArmourLocations()
	{
		switch (_Type)
		{
			case BIPED:
				return _ArmourLocationsBiped;
			case QUAD:
				return _ArmourLocationsQuad;
			case LAM:
				return _ArmourLocationsLAM;
			case OMNI:
				return _ArmourLocationsOmni;
		}
		return null;
	}

    public String getVariantName()
    {
        return _Variant + " " + _Name; 
    }	
}
