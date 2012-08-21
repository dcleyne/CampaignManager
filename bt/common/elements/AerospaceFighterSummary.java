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
public class AerospaceFighterSummary extends Element
{
    protected String m_Name = "";
    protected String m_Type = "";
    protected String m_Tech = "";
    protected String m_Era = "";
    protected int m_Mass;
    protected int m_StructuralIntegrity;
    protected int m_Fuel;
    protected int m_SafeThrust;
    protected int m_MaxThrust;
    protected String m_Armour = "";
    protected int m_HeatSinks;
    protected String m_Weapons = "";
    protected String m_Cargo = "";
    protected int m_Crew;
    protected int m_Cost;
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
    public int GetStructuralIntegrity()
    { return m_StructuralIntegrity; }
    public int GetFuel()
    { return m_Fuel; }
    public int GetSafeThrust()
    { return m_SafeThrust; }
    public int GetMaxThrust()
    { return m_MaxThrust; }
    public String GetArmour()
    { return m_Armour; }
    public int GetHeatSinks()
    { return m_HeatSinks; }
    public String GetWeapons()
    { return m_Weapons; }
    public String GetCargo()
    { return m_Cargo; }
    public int GetCrew()
    { return m_Crew; }
    public int GetCost()
    { return m_Cost; }
    public String GetNotes()
    { return m_Notes; }

    public String GetElementType()
    { return "Aerospace"; }
    public double GetSupportRequirement()
    { return 1.0; }
    public double GetCrewCompliment()
    { return m_Crew; }



    public AerospaceFighterSummary()
    {
    }

    public String GetTableName()
    {
        return "AerospaceFighterSummary";
    }

    public String toString()
    { return m_Name; }

}
