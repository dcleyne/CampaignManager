package bt.elements.unit;

import java.io.Serializable;

import java.util.Date;
import java.util.Vector;

import bt.elements.Ammunition;
import bt.elements.Asset;
import bt.elements.Battlemech;
import bt.elements.Character;
import bt.elements.ElementType;
import bt.elements.Item;
import bt.elements.personnel.Personnel;

/**
 * <p>
 * Title: Miradan Phedd
 * </p>
 * 
 * <p>
 * Description: Library for all Miradan related work
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * 
 * <p>
 * Company: Miradan Phedd
 * </p>
 * 
 * @author Daniel Cleyne
 * @version 0.1
 */
public class Unit implements Serializable
{
	private static final long serialVersionUID = 1;

	private String _Name;
	private Player _Player;

	private Date _EstablishDate;
	private Date _CurrentDate;

	private QualityRating _QualityRating;
	private TechRating _TechRating;

	private String _Notes;

	private Character m_Leader;
	private Vector<Personnel> _Personnel = new Vector<Personnel>();

	private Vector<Asset> _Assets = new Vector<Asset>();
	private Vector<Ammunition> _SpareAmmunition = new Vector<Ammunition>();
	private Vector<Item> _SpareParts = new Vector<Item>();
	private Vector<Asset> _SalvagedAssets = new Vector<Asset>();
	private Vector<Long> _AssignedMissions = new Vector<Long>();
	private Vector<Long> _CompletedMissions = new Vector<Long>();
	private Vector<Formation> m_Formations = new Vector<Formation>();

	public String getName()
	{
		return _Name;
	}

	public void setName(String name)
	{
		_Name = name;
	}

	public Player getPlayer()
	{
		return _Player;
	}

	public void setPlayer(Player player)
	{
		_Player = player;
	}

	public Character getLeader()
	{
		return m_Leader;
	}

	public void setLeader(Character leader)
	{
		m_Leader = leader;
	}

	public Date getCurrentDate()
	{
		return _CurrentDate;
	}

	public void setCurrentDate(Date currentDate)
	{
		_CurrentDate = currentDate;
	}

	public QualityRating getQualityRating()
	{
		return _QualityRating;
	}

	public void setQualityRating(QualityRating qualityRating)
	{
		_QualityRating = qualityRating;
	}

	public TechRating getTechRating()
	{
		return _TechRating;
	}

	public void setTechRating(TechRating techRating)
	{
		_TechRating = techRating;
	}

	public Date getEstablishDate()
	{
		return _EstablishDate;
	}

	public void setEstablishDate(Date establishDate)
	{
		_EstablishDate = establishDate;
	}

	public String getNotes()
	{
		return _Notes;
	}

	public void setNotes(String notes)
	{
		_Notes = notes;
	}

	public Vector<Personnel> getPersonnel()
	{
		return _Personnel;
	}

	public int getGroupCount()
	{
		return m_Formations.size();
	}

	public Formation getGroup(int Index)
	{
		if (Index < 0)
			return null;
		return (Formation) m_Formations.elementAt(Index);
	}

	public int getGroupIndex(Formation ug)
	{
		return m_Formations.indexOf(ug);
	}

	public Formation addNewGroup(UnitDesignation ud)
	{
		Formation ug = new Formation();
		ug.setUnitDesignation(ud);
		ug.setCommander(-1);
		ug.setName("New Group");
		m_Formations.add(ug);
		return ug;
	}

	public void removeGroup(Formation f)
	{
		m_Formations.remove(f);
	}

	public Vector<Asset> getAssets()
	{
		return _Assets;
	}

	public int getAssetCount()
	{
		return _Assets.size();
	}

	public Asset getAsset(int Index)
	{
		if (Index < 0)
			return null;
		return _Assets.elementAt(Index);
	}

	public Asset addNewAsset(ElementType et, String identifier)
	{
		Asset NewAsset = new Asset();
		NewAsset.setElementType(et);
		NewAsset.setIdentifier(identifier);
		_Assets.add(NewAsset);
		return NewAsset;
	}

	public int getAssetIndex(Asset a)
	{
		return _Assets.indexOf(a);
	}

	public Vector<Battlemech> getBattlemechs()
	{
		Vector<Battlemech> mechs = new Vector<Battlemech>();
		for (Asset a : _Assets)
			if (a.getElementType() == ElementType.BATTLEMECH)
				mechs.add((Battlemech) a);

		return mechs;
	}

	public void addBattlemechs(Vector<Battlemech> mechs)
	{
		_Assets.addAll(mechs);
	}

	public Vector<Ammunition> getSpareAmmunition()
	{
		return _SpareAmmunition;
	}

	public Vector<Item> getSpareParts()
	{
		return _SpareParts;
	}

	public Vector<Battlemech> getSalvagedMechs()
	{
		Vector<Battlemech> mechs = new Vector<Battlemech>();
		for (Asset a : _SalvagedAssets)
			if (a.getElementType() == ElementType.BATTLEMECH)
				mechs.add((Battlemech) a);

		return mechs;
	}

	public Vector<Asset> getSalvagedAssets()
	{
		return _SalvagedAssets;
	}

	public Vector<Long> getAssignedMissions()
	{
		return _AssignedMissions;
	}

	public Vector<Long> getCompletedMissions()
	{
		return _CompletedMissions;
	}
	
	public void missionCompleted(long missionID)
	{
		Long id = new Long(missionID);
		_AssignedMissions.remove(id);
		_CompletedMissions.add(id);
	}
	
	public void missionAbandoned(long missionID)
	{
		Long id = new Long(missionID);
		_AssignedMissions.remove(id);		
	}

	public int getUnitStrength()
	{
		// TODO This will have to be expanded to include other unit types.
		return _Assets.size();
	}

	public int getUnitBV()
	{
		// TODO This will have to be expanded to include other unit types.
		int totalBV = 0;
		for (Asset bm : _Assets)
			totalBV += bm.getAdjustedBV();

		return totalBV;
	}

}
