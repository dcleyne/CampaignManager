package bt.elements.missions;

import java.util.Arrays;

import bt.elements.unit.TechRating;
import bt.util.Dice;

public class OpponentBriefing
{
    private String _Team;
    private String _Briefing;
    private PreviousSuccessfulPlayerMission _PreviousSuccessfulPlayerMission;
    private TechRating[] _TechRatingTable = new TechRating[8];
    private float[] _ForceRatioTable = new float[8];
    private String[] _UnitCompositionTable = new String[8];
    private String[] _RandomAllocationTable = new String[8];
    private String[] _SkillLevelTable = new String[8];
    
    private boolean _Generated = false;
    private TechRating _TechRating;
    private float _ForceRatio;
    private String _UnitComposition;
    private String _RandomAllocation;
    private String _SkillLevel;
    
    private String _Notes;
    
	public String getTeam()
	{
		return _Team;
	}
	public void setTeam(String team)
	{
		_Team = team;
	}
	public String getBriefing()
	{
		return _Briefing;
	}
	public void setBriefing(String briefing)
	{
		_Briefing = briefing;
	}
	public PreviousSuccessfulPlayerMission getPreviousSuccessfulPlayerMission() 
	{
		return _PreviousSuccessfulPlayerMission;
	}
	public void setPreviousSuccessfulPlayerMission(PreviousSuccessfulPlayerMission previousSuccessfulPlayerMission) 
	{
		_PreviousSuccessfulPlayerMission = previousSuccessfulPlayerMission;
	}
	
	public void setTechRating(int index, TechRating rating)
	{
		_TechRatingTable[index - 1] = rating;
	}
	
	public TechRating getTechRating(int index)
	{
		return _TechRatingTable[index - 1];
	}
	
	public void setForceRatio(int index, float forceRatio)
	{
		_ForceRatioTable[index - 1] = forceRatio;
	}
	
	public float getForceRatio(int index)
	{
		return _ForceRatioTable[index - 1];
	}
	
	public void setUnitComposition(int index, String unitComposition)
	{
		_UnitCompositionTable[index - 1] = unitComposition;
	}
	
	public String getUnitComposition(int index)
	{
		return _UnitCompositionTable[index - 1];
	}
	
	public void setRandomAllocation(int index, String randomAllocation)
	{
		_RandomAllocationTable[index - 1] = randomAllocation;
	}
	
	public String getRandomAllocation(int index)
	{
		return _RandomAllocationTable[index - 1];
	}
	
	public void setSkillLevel(int index, String skillLevel)
	{
		_SkillLevelTable[index - 1] = skillLevel;
	}
	
	public String getSkillLevel(int index)
	{
		return _SkillLevelTable[index - 1];
	}
	
	public String getNotes()
	{
		return _Notes;
	}
	public void setNotes(String notes)
	{
		_Notes = notes;
	}
	
	public OpponentBriefing()
	{
	}
	
	public OpponentBriefing(OpponentBriefing briefing)
	{
	    _Team = briefing._Team;
	    _Briefing = briefing._Briefing;
	    _PreviousSuccessfulPlayerMission = new PreviousSuccessfulPlayerMission(briefing._PreviousSuccessfulPlayerMission);
	    _TechRatingTable = Arrays.copyOf(briefing._TechRatingTable, 8);
	    _ForceRatioTable = Arrays.copyOf(briefing._ForceRatioTable, 8);
	    _UnitCompositionTable = Arrays.copyOf(briefing._UnitCompositionTable, 8);
	    _RandomAllocationTable = Arrays.copyOf(briefing._RandomAllocationTable, 8);
	    _SkillLevelTable = Arrays.copyOf(briefing._SkillLevelTable, 8);

	    _Generated = briefing._Generated;
	    if (_Generated)
	    {
	    	_TechRating = briefing._TechRating;
	    	_ForceRatio = briefing._ForceRatio;
	    	_UnitComposition = briefing._UnitComposition;
	    	_RandomAllocation = briefing._RandomAllocation;
	    	_SkillLevel = briefing._SkillLevel;
	    }
	    
	    _Notes = briefing._Notes;
	}
	
	public void clearGeneration()
	{
		_Generated = false;
	}
	
	private void generate(String previousSuccessfulMission)
	{
		if (_Generated)
			return;
		
		_TechRating = _TechRatingTable[Dice.d6(1) - 1 + _PreviousSuccessfulPlayerMission.getModifier(previousSuccessfulMission)];
		_ForceRatio = _ForceRatioTable[Dice.d6(1) - 1 + _PreviousSuccessfulPlayerMission.getModifier(previousSuccessfulMission)];
		_UnitComposition = _UnitCompositionTable[Dice.d6(1) - 1 + _PreviousSuccessfulPlayerMission.getModifier(previousSuccessfulMission)];
		_RandomAllocation = _RandomAllocationTable[Dice.d6(1) - 1 + _PreviousSuccessfulPlayerMission.getModifier(previousSuccessfulMission)];
		_SkillLevel = _SkillLevelTable[Dice.d6(1) - 1 + _PreviousSuccessfulPlayerMission.getModifier(previousSuccessfulMission)];
		
		_Generated = true;
	}
	
	public TechRating getTechRating(String previousSuccessfulMission)
	{
		generate(previousSuccessfulMission);
		return _TechRating;
	}

	public float getForceRatio(String previousSuccessfulMission)
	{
		generate(previousSuccessfulMission);
		return _ForceRatio;
	}

	public String getUnitComposition(String previousSuccessfulMission)
	{
		generate(previousSuccessfulMission);
		return _UnitComposition;
	}

	public String getRandomAllocation(String previousSuccessfulMission)
	{
		generate(previousSuccessfulMission);
		return _RandomAllocation;
	}

	public String getSkillLevel(String previousSuccessfulMission)
	{
		generate(previousSuccessfulMission);
		return _SkillLevel;
	}
}
