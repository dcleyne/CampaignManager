package bt.elements.campaign;

import java.util.ArrayList;

import bt.elements.Era;
import bt.elements.Faction;
import bt.elements.unit.TechRating;

public class Side
{
	private String _Name;
	private Faction _Faction;
	private Era _Era;
	private TechRating _TechRating;
	private ArrayList<Force> _Forces = new ArrayList<Force>();
	
	public String getName()
	{
		return _Name;
	}
	public void setName(String name)
	{
		_Name = name;
	}
	public Faction getFaction()
	{
		return _Faction;
	}
	public void setFaction(Faction faction)
	{
		_Faction = faction;
	}
	public Era getEra()
	{
		return _Era;
	}
	public void setEra(Era era)
	{
		_Era = era;
	}
	public TechRating getTechRating()
	{
		return _TechRating;
	}
	public void setTechRating(TechRating techRating)
	{
		_TechRating = techRating;
	}
	
	public void addForce(Force f)
	{
		_Forces.add(f);
	}
	
	public ArrayList<Force> getForces()
	{
		return new ArrayList<Force>(_Forces);
	}
	
}
