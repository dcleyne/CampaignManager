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
    private BattlemechDesign.Type _Type = BattlemechDesign.Type.BIPED;
    private int _Weight;
    private int _Cost;
    private int _BV;
    private HashMap<String, Integer> _Armour = new HashMap<String,Integer>();

    private Vector<DesignItem> _Items = new Vector<DesignItem>();

    private static BattlemechSection[] _Sections;
    private String[] _InternalLocations = _InternalLocationsBiped;
    private String[] _ArmourLocations = _ArmourLocationsBiped;
    
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
        if (_Type == Type.BIPED)
        {
        	_Sections = _SectionsBiped;
            _InternalLocations = _InternalLocationsBiped;
            _ArmourLocations = _ArmourLocationsBiped;
        }
        if (_Type == Type.QUAD)
        {
        	_Sections = _SectionsQuad;
            _InternalLocations = _InternalLocationsQuad;
            _ArmourLocations = _ArmourLocationsQuad;
        }
        if (_Type == Type.LAM)
        {
        	_Sections = _SectionsLAM;
            _InternalLocations = _InternalLocationsLAM;
            _ArmourLocations = _ArmourLocationsLAM;
        }
        if (_Type == Type.OMNI)
        {
        	_Sections = _SectionsOmni;
            _InternalLocations = _InternalLocationsOmni;
            _ArmourLocations = _ArmourLocationsOmni;
        }
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
		return _Sections;
	}
	public String[] getInternalLocations()
	{
		return _InternalLocations;
	}
	public void setInternalLocations(String[] internalLocations)
	{
		_InternalLocations = internalLocations;
	}
	public String[] getArmourLocations()
	{
		return _ArmourLocations;
	}
	public void setArmourLocations(String[] armourLocations)
	{
		_ArmourLocations = armourLocations;
	}

    public String getVariantName()
    {
        return _Variant + " " + _Name; 
    }	
}
