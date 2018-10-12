package bt.elements.unit;

import java.util.Vector;

public class MechUnitParameters
{
	public static final int[] AllMechWeights = {20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100};
	
    private String _Name;
    private int _MechCount;
    private int _MinBV;
    private int _MaxBV;
    private int _MinWeight;
    private int _MaxWeight;
    private Vector<Integer> _IncludedMechWeights = new Vector<Integer>();
	public String getName()
	{
		return _Name;
	}
	public void setName(String name)
	{
		_Name = name;
	}
	public int getMechCount()
	{
		return _MechCount;
	}
	public void setMechCount(int mechCount)
	{
		_MechCount = mechCount;
	}
	public int getMinBV()
	{
		return _MinBV;
	}
	public void setMinBV(int minBV)
	{
		_MinBV = minBV;
	}
	public int getMaxBV()
	{
		return _MaxBV;
	}
	public void setMaxBV(int maxBV)
	{
		_MaxBV = maxBV;
	}
	public int getMinWeight()
	{
		return _MinWeight;
	}
	public void setMinWeight(int minWeight)
	{
		_MinWeight = minWeight;
	}
	public int getMaxWeight()
	{
		return _MaxWeight;
	}
	public void setMaxWeight(int maxWeight)
	{
		_MaxWeight = maxWeight;
	}
	public Vector<Integer> getIncludedMechWeights()
	{
		return _IncludedMechWeights;
	}

	public void setIncludedMechWeights(int[] includedMechWeights)
	{
		_IncludedMechWeights.clear();
		for (int i : includedMechWeights)
			_IncludedMechWeights.add(i);
	}
    
}
