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
public class BattlemechSummary extends Element
{
//    private static Log log = LogFactory.getLog(BattlemechSummary.class);
	
    protected String m_Origin = "";
    protected int m_Era;
    protected String m_Name = "";
    protected int m_Weight;
    protected String m_MovementType = "";
    protected String m_EngineType = "";
    protected String m_StructureType = "";
    protected String m_ArmourType = "";
    protected String m_HeatSinkType = "";
    protected int m_BattleValue;
    protected int m_CombatValue;
    protected int m_MNA;
    protected int m_Cost;
    protected int m_EngineRating;
    protected String m_Movement = "";
    protected int m_ArmourFactors;
    protected String m_HeadArmour = "";
    protected String m_CenterTorsoArmour = "";
    protected String m_SideTorsoArmour = "";
    protected String m_ArmArmour = "";
    protected String m_LegArmour = "";
    protected int m_HeatSinks;
    protected String m_Equipment = "";


    public String  GetOrigin()
    { return m_Origin; }
    public int GetEra()
    { return m_Era; }
    public String GetName()
    { return m_Name; }
    public int GetWeight()
    { return m_Weight; }
    public String GetMovementType()
    { return m_MovementType; }
    public String GetEngineType()
    { return m_EngineType; }
    public String GetStructureType()
    { return m_StructureType; }
    public String GetArmourType()
    { return m_ArmourType; }
    public String GetHeatSinkType()
    { return m_HeatSinkType; }
    public int GetBattleValue()
    { return m_BattleValue; }
    public int GetCombatValue()
    { return m_CombatValue; }
    public int GetMNA()
    { return m_MNA; }
    public int GetCost()
    { return m_Cost; }
    public int GetEngineRating()
    { return m_EngineRating;}
    public String GetMovement()
    { return m_Movement; }
    public int GetArmourFactors()
    { return m_ArmourFactors;}
    public String GetHeadArmour()
    { return m_HeadArmour; }
    public String GetSenterTorsoArmour()
    { return m_CenterTorsoArmour; }
    public String GetSideTorsoArmour()
    { return m_SideTorsoArmour; }
    public String GetArmArmour()
    { return m_ArmArmour; }
    public String GetLegArmour()
    { return m_LegArmour; }
    public int GetHeatSinks()
    { return m_HeatSinks;}
    public String GetEquipment()
    { return m_Equipment;}


    public BattlemechSummary()
    {
    }

    public String GetElementType()
    { return "Battlemech"; }
    public double GetSupportRequirement()
    { return 1.0; }
    public double GetCrewCompliment()
    { return 1.0; }

    public String GetTableName()
   {
       return "BattlemechSummary";
   }

}
