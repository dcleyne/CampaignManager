package bt.common.elements;

public class BattlemechDesign extends Design 
{	
	private String m_Name;
	private String m_Model;
	private int m_AvailableEra;
	private int m_TechnologyLevel;

	private int m_WalkingSpeed;
	private int m_RunningSpeed;
	private int m_JumpDistance;
	
	private Cockpit m_Cockpit;
	private Engine m_Engine;
	private Gyro m_Gyro;
	
	
	public ElementType getDesignElementType() 
	{
		return ElementType.BATTLEMECH;
	}
	
	public String getName()
	{ return m_Name; }
	
	public String getModel()
	{ return m_Model; }
	
	public int getAvailableEra()
	{ return m_AvailableEra; }
	
	public int getTechnologyLevel()
	{ return m_TechnologyLevel; }
	
	public int getWalkingSpeed()
	{ return m_WalkingSpeed; }
	
	public int getRunningSpeed()
	{ return m_RunningSpeed; }
	
	public int getJumpingDistance()
	{ return m_JumpDistance; }

	public Cockpit getCockpit()
	{ return m_Cockpit; }
	
	public Engine getEngine()
	{ return m_Engine; }
	
	public Gyro getGyro()
	{ return m_Gyro; }
	
	
	public enum Cockpit
	{
		STANDARD;
		
		private static final String[] _Names = {"Standard"};
		private static final int[] _Weights = {3};
		private static final int[] _CritSlots = {1};
		
		public String toString()
		{ return _Names[ordinal()]; }
		
		public int getWeight()
		{ return _Weights[ordinal()]; }
		
		public int getCritSlots()
		{ return _CritSlots[ordinal()]; }
	}

	public enum Gyro
	{
		STANDARD;
		
		private static final String[] _Names = {"Standard"};
		private static final int[] _Weights = {3};
		private static final int[] _CritSlots = {1};
		
		public String toString()
		{ return _Names[ordinal()]; }
		
		public int getWeight()
		{ return _Weights[ordinal()]; }
		
		public int getCritSlots()
		{ return _CritSlots[ordinal()]; }
	}

	public enum TargettingSystem
	{
		STANDARD;
		
		private static final String[] _Names = {"Standard"};
		private static final int[] _Weights = {3};
		private static final int[] _CritSlots = {1};
		
		public String toString()
		{ return _Names[ordinal()]; }
		
		public int getWeight()
		{ return _Weights[ordinal()]; }
		
		public int getCritSlots()
		{ return _CritSlots[ordinal()]; }
	}
}
