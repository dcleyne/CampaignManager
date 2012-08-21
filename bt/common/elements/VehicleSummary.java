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
public class VehicleSummary extends Element
{
    protected String m_Name = "";
    protected String m_Tech = "";
    protected String m_Era = "";
    protected String m_MovementType = "";
    protected int m_Weight;
    protected String m_EngineType;
    protected int m_CruisingMP;
    protected int m_FlankMP;
    protected int m_HeatSinks;
    protected int m_FrontArmour;
    protected int m_SideArmour;
    protected int m_RearArmour;
    protected int m_TurretArmour;
    protected String m_Weapons = "";
    protected String m_Cargo = "";
    protected int m_Crew;
    protected int m_BattleValue;
    protected int m_Cost;
    protected String m_Notes = "";

    public String GetName()
    { return m_Name; }
    public String GetTech()
    { return m_Tech; }
    public String GetEra()
    { return m_Era; }
    public String GetMovementType()
    { return m_MovementType; }
    public int GetWeight()
    { return m_Weight; }
    public String GetEngineType()
    { return m_EngineType; }
    public int GetCruisingMP()
    { return m_CruisingMP; }
    public int GetFlankMP()
    { return m_FlankMP; }
    public int GetHeatSinks()
    { return m_HeatSinks; }
    public int GetFrontArmour()
    { return m_FrontArmour; }
    public int GetSideArmour()
    { return m_SideArmour; }
    public int GetRearArmour()
    { return m_RearArmour; }
    public int GetTurretArmour()
    { return m_TurretArmour; }
    public String GetWeapons()
    { return m_Weapons; }
    public String GetCargo()
    { return m_Cargo; }
    public int GetCrew()
    { return m_Crew; }

    public int GetBattleValue()
    { return m_BattleValue; }
    public int GetCost()
    { return m_Cost; }
    public String GetNotes()
    { return m_Notes; }

    public VehicleSummary()
    {
    }

    public String GetElementType()
    { return "Vehicle"; }
    public double GetSupportRequirement()
    { return 1.0; }
    public double GetCrewCompliment()
    { return m_Crew; }

}
