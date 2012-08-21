package bt.common.elements;



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
public class DropshipSummary extends Element
{
    protected String m_Name = "";
    protected String m_Type = "";
    protected String m_ShipUsage = "";
    protected String m_Tech = "";
    protected String m_Era = "";
    protected int m_Mass;
    protected double m_Length;
    protected double m_Width;
    protected double m_Height;
    protected int m_Fuel;
    protected double m_FuelConsumption;
    protected int m_SafeThrust;
    protected int m_MaxThrust;
    protected int m_ForeArmour;
    protected int m_SideArmour;
    protected int m_AftArmour;
    protected int m_HeatSinks;
    protected String m_Weapons = "";
    protected String m_Cargo = "";
    protected int m_Crew;
    protected int m_Passengers;
    protected int m_MarinePoints;
    protected int m_Cost;
    protected int m_RevenuePerMission;
    protected String m_Notes = "";

    public String GetName()
    { return m_Name; }
    public String GetType()
    { return m_Type; }
    public String GetTech()
    { return m_Tech; }
    public String GetEra()
    { return m_Era; }
    public int GetMass()
    { return m_Mass; }
    public double Getlength()
    { return m_Length; }
    public double GetWidth()
    { return m_Width; }
    public double GetHeight()
    { return m_Height; }
    public double GetFuelConsumption()
    { return m_FuelConsumption; }
    public int GetSafeThrust()
    { return m_SafeThrust; }
    public int GetMaxThrust()
    { return m_MaxThrust; }
    public int GetForeArmour()
    { return m_ForeArmour; }
    public int GetSideArmour()
    { return m_SideArmour; }
    public int GetAftArmour()
    { return m_AftArmour; }
    public int GetHeatSinks()
    { return m_HeatSinks; }
    public String GetWeapons()
    { return m_Weapons; }
    public String GetCargo()
    { return m_Cargo; }
    public int GetCrew()
    { return m_Crew; }
    public int GetPassengers()
    { return m_Passengers; }
    public int GetMarinePoints()
    { return m_MarinePoints; }
    public int GetCost()
    { return m_Cost; }
    public int GetRevenuePerMission()
    { return m_RevenuePerMission; }
    public String GetNotes()
    { return m_Notes; }

    public String GetElementType()
    { return "Dropship"; }
    public double GetSupportRequirement()
    { return 1.0; }
    public double GetCrewCompliment()
    { return m_Crew; }


    public DropshipSummary()
    {
    }

    public String toString()
    { return m_Name; }

}
