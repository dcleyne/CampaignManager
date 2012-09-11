package bt.elements.unit;

import java.io.Serializable;

import java.util.Collections;
import java.util.Date;
import java.util.List;
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

	private Character _Leader;
	private Vector<Personnel> _Personnel = new Vector<Personnel>();

	private Vector<Asset> _Assets = new Vector<Asset>();
	private Vector<Ammunition> _SpareAmmunition = new Vector<Ammunition>();
	private Vector<Item> _SpareParts = new Vector<Item>();
	private Vector<Asset> _SalvagedAssets = new Vector<Asset>();
	
	private Long _AssignedMission = null;
	private String _AssignedMissionTitle = null;
	
	private Vector<CompletedMission> _CompletedMissions = new Vector<CompletedMission>();
	private Vector<Formation> _Formations = new Vector<Formation>();
	
	private Vector<PersonnelAssetAssignment> _PersonnelAssetAssignments = new Vector<PersonnelAssetAssignment>();

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
		return _Leader;
	}

	public void setLeader(Character leader)
	{
		_Leader = leader;
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
		return _Formations.size();
	}

	public Formation getGroup(int Index)
	{
		if (Index < 0)
			return null;
		return (Formation) _Formations.elementAt(Index);
	}

	public int getGroupIndex(Formation ug)
	{
		return _Formations.indexOf(ug);
	}

	public Formation addNewGroup(UnitDesignation ud)
	{
		Formation ug = new Formation();
		ug.setUnitDesignation(ud);
		ug.setCommander(-1);
		ug.setName("New Group");
		_Formations.add(ug);
		return ug;
	}

	public void removeGroup(Formation f)
	{
		_Formations.remove(f);
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

	public Long getAssignedMission()
	{
		return _AssignedMission;
	}
	
	public String getAssignedMissionTitle()
	{
		return _AssignedMissionTitle;
	}
	
	public void setAssignedMission(Long missionID, String missionTitle)
	{
		_AssignedMission = missionID;
		_AssignedMissionTitle = missionTitle;
	}

	public Vector<Long> getCompletedMissions()
	{
		Vector<Long> completedMissions = new Vector<Long>();
		for (CompletedMission cm : _CompletedMissions)
			completedMissions.add(new Long(cm.getMissionIdentifier()));
		
		return completedMissions;
	}
	
	public void assignedMissionCompleted(CompletedMission.Result result, double prizeMoney)
	{
		if (_AssignedMission != null)
		{
			_CompletedMissions.add(new CompletedMission(_AssignedMission, _AssignedMissionTitle, result, prizeMoney));
			_AssignedMission = null;
			_AssignedMissionTitle = null;
		}
	}
	
	public void assignedMissionAbandoned()
	{
		if (_AssignedMission != null)
		{
			_AssignedMission = null;
			_AssignedMissionTitle = null;
		}
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
	
	public Vector<String> getAssetsAssignedToPersonnel(String name)
	{
		Vector<String> assets = new Vector<String>();
		for (PersonnelAssetAssignment ass : _PersonnelAssetAssignments)
		{
			if (ass.getName().equalsIgnoreCase(name))
				assets.add(ass.getAssetIdentifier());
		}
		
		return assets;
	}
	
	public List<PersonnelAssetAssignment> getPersonnelAssetAssignments()
	{
		return Collections.unmodifiableList(_PersonnelAssetAssignments);
	}
	
	public Vector<String> getPersonnelAssignedToAsset(String assetIdentifier)
	{
		Vector<String> personnel = new Vector<String>();
		for (PersonnelAssetAssignment ass : _PersonnelAssetAssignments)
		{
			if (ass.getAssetIdentifier().equalsIgnoreCase(assetIdentifier))
				personnel.add(ass.getName());
		}
		
		return personnel;		
	}
	
	public Vector<PersonnelAssetAssignment> getAssignments(String personnelName)
	{
		Vector<PersonnelAssetAssignment> assignments = new Vector<PersonnelAssetAssignment>();
		for (PersonnelAssetAssignment ass : _PersonnelAssetAssignments)
		{
			if (ass.getName().equalsIgnoreCase(personnelName))
				assignments.add(ass);
		}
		
		return assignments;
	}
	
	public void addPersonnelAssignment(String name, String assetIdentifier, Role role)
	{
		PersonnelAssetAssignment paa = new PersonnelAssetAssignment(name, assetIdentifier, role);
		if (_PersonnelAssetAssignments.contains(paa))
			_PersonnelAssetAssignments.remove(paa);
		
		_PersonnelAssetAssignments.add(paa);
	}

}
