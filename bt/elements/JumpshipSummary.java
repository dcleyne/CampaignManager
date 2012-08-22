package bt.elements;



/**
 * <p>Title: Miradan Phedd</p>
 *
 * <p>Description: Library for all Miradan related work</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Miradan Phedd</p>
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
public class JumpshipSummary extends Element
{
    protected String m_Name = "";
    protected String m_Tech = "";
    protected String m_Era = "";
    protected int m_Mass;
    protected int m_Length;
    protected int m_SailDiameter;
    protected int m_Fuel;
    protected double m_FuelConsumption;
    protected int m_SafeThrust;
    protected int m_MaxThrust;
    protected int m_SailIntegrity;
    protected int m_KFDriveIntegrity;
    protected int m_ForeArmour;
    protected int m_ForeSideArmour;
    protected int m_AftSideArmour;
    protected int m_AftArmour;
    protected int m_HeatSinks;
    protected String m_Weapons = "";
    protected String m_Cargo = "";
    protected int m_DropShipCapacity;
    protected String m_GravDeck = "";
    protected int m_Crew;
    protected int m_Passengers;
    protected int m_MarinePoints;
    protected int m_Cost;
    protected String m_Notes = "";

    public JumpshipSummary()
    {
    }

    public String GetName()
    { return m_Name; }
    public String GetTech()
    { return m_Tech; }
    public String GetEra()
    { return m_Era; }
    public int GetMass()
    { return m_Mass; }
    public int GetLength()
    { return m_Length; }
    public int GetSailDiameter()
    { return m_SailDiameter; }
    public int GetFuel()
    { return m_Fuel; }
    public double GetFuelConsumption()
    { return m_FuelConsumption; }
    public int GetSafeThrust()
    { return m_SafeThrust; }
    public int GetMaxThrust()
    { return m_MaxThrust; }
    public int GetSailIntegrity()
    { return m_SailIntegrity; }
    public int GetKFDriveIntegrity()
    { return m_KFDriveIntegrity; }
    public int GetForeArmour()
    { return m_ForeArmour; }
    public int GetForeSideArmour()
    { return m_ForeSideArmour; }
    public int GetAftSideArmour()
    { return m_AftSideArmour; }
    public int GetAftArmour()
    { return m_AftArmour; }
    public int GetHeatSinks()
    { return m_HeatSinks; }
    public String GetWeapons()
    { return m_Weapons; }
    public String GetCargo()
    { return m_Cargo; }
    public int GetDropShipCapacity()
    { return m_DropShipCapacity; }
    public String GetGravDeck()
    { return m_GravDeck; }
    public int GetCrew()
    { return m_Crew; }
    public int GetPassengers()
    { return m_Passengers; }
    public int GetMarinePoints()
    { return m_MarinePoints; }
    public int GetCost()
    { return m_Cost; }
    public String GetNotes()
    { return m_Notes; }


    public String GetElementType()
    { return "Jumpship"; }
    public double GetSupportRequirement()
    { return 1.0; }
    public double GetCrewCompliment()
    { return m_Crew; }

    public String toString()
    { return m_Name; }

}
