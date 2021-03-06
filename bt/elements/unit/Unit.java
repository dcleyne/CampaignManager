package bt.elements.unit;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bt.elements.Ammunition;
import bt.elements.Asset;
import bt.elements.Battlemech;
import bt.elements.Character;
import bt.elements.ElementType;
import bt.elements.Item;
import bt.elements.personnel.Mechwarrior;
import bt.elements.personnel.Personnel;
import bt.managers.DesignManager;

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

	private LocalDate _EstablishDate;
	private LocalDate _CurrentDate;

	private QualityRating _QualityRating;
	private TechRating _TechRating;
	private int _BaseUnitSize = 4;
	private int _WarchestPoints = 0;

	private String _Notes = "";

	private Character _Leader;
	private ArrayList<Personnel> _Personnel = new ArrayList<Personnel>();

	private ArrayList<Asset> _Assets = new ArrayList<Asset>();
	private ArrayList<Ammunition> _SpareAmmunition = new ArrayList<Ammunition>();
	private ArrayList<Item> _SpareParts = new ArrayList<Item>();
	private ArrayList<Asset> _SalvagedAssets = new ArrayList<Asset>();
	
	private Long _AssignedMission = null;
	private String _AssignedMissionTitle = null;
	
	private ArrayList<CompletedMission> _CompletedMissions = new ArrayList<CompletedMission>();
	private ArrayList<Formation> _Formations = new ArrayList<Formation>();
	
	private ArrayList<PersonnelAssetAssignment> _PersonnelAssetAssignments = new ArrayList<PersonnelAssetAssignment>();

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

	public LocalDate getCurrentDate()
	{
		return _CurrentDate;
	}

	public void setCurrentDate(LocalDate currentDate)
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

	public int getBaseUnitSize()
	{
		return _BaseUnitSize;
	}

	public void setBaseUnitSize(int baseUnitSize)
	{
		_BaseUnitSize = baseUnitSize;
	}

	public int getWarchestPoints()
	{
		return _WarchestPoints;
	}

	public void setWarchestPoints(int warchestPoints)
	{
		_WarchestPoints = warchestPoints;
	}

	public LocalDate getEstablishDate()
	{
		return _EstablishDate;
	}

	public void setEstablishDate(LocalDate establishDate)
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

	public ArrayList<Personnel> getPersonnel()
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
		return (Formation) _Formations.get(Index);
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

	public ArrayList<Asset> getAssets()
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
		return _Assets.get(Index);
	}

	public Asset addNewAsset(ElementType et, String identifier)
	{
		Asset NewAsset = createAsset(et);
		NewAsset.setIdentifier(identifier);
		_Assets.add(NewAsset);
		return NewAsset;
	}
	
	private Asset createAsset(ElementType et)
	{
		switch (et)
		{
			case BATTLEMECH:
				return new Battlemech();
			default:
				break;
		}
		return null;
	}

	public int getAssetIndex(Asset a)
	{
		return _Assets.indexOf(a);
	}
	
	public ArrayList<String> getUnassignedAssetIDs()
	{
		ArrayList<String> assetIDs = new ArrayList<String>();
		for (Asset a : _Assets)
			assetIDs.add(a.getIdentifier());
		
		for (PersonnelAssetAssignment paa : _PersonnelAssetAssignments)
			if (assetIDs.contains(paa.getAssetIdentifier()))
				assetIDs.remove(paa.getAssetIdentifier());
		
		return assetIDs;
	}

	public ArrayList<Battlemech> getBattlemechs()
	{
		ArrayList<Battlemech> mechs = new ArrayList<Battlemech>();
		for (Asset a : _Assets)
			if (a.getElementType() == ElementType.BATTLEMECH)
				mechs.add((Battlemech) a);

		return mechs;
	}

	public void addBattlemechs(ArrayList<Battlemech> mechs)
	{
		_Assets.addAll(mechs);
	}

	public ArrayList<Ammunition> getSpareAmmunition()
	{
		return _SpareAmmunition;
	}

	public ArrayList<Item> getSpareParts()
	{
		return _SpareParts;
	}

	public ArrayList<Battlemech> getSalvagedMechs()
	{
		ArrayList<Battlemech> mechs = new ArrayList<Battlemech>();
		for (Asset a : _SalvagedAssets)
			if (a.getElementType() == ElementType.BATTLEMECH)
				mechs.add((Battlemech) a);

		return mechs;
	}

	public ArrayList<Asset> getSalvagedAssets()
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

	public CompletedMission[] getCompletedMissions()
	{
		return _CompletedMissions.toArray(new CompletedMission[_CompletedMissions.size()]);
	}

	public ArrayList<Long> getCompletedMissionIDs()
	{
		ArrayList<Long> missionIDs = new ArrayList<Long>();
		for (CompletedMission cm : _CompletedMissions)
			missionIDs.add(cm.getMissionIdentifier());
		
		return missionIDs;
	}
	
	public void addCompletedMission(CompletedMission cm)
	{
		_CompletedMissions.add(cm);
	}
	
	public void assignedMissionCompleted(CompletedMission.Result result, int warchestPoints)
	{
		if (_AssignedMission != null)
		{
			_CompletedMissions.add(new CompletedMission(_AssignedMission, _AssignedMissionTitle, result, warchestPoints, _CurrentDate));
			_WarchestPoints += warchestPoints;
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
	
	public Asset getAssetFromID(String assetID)
	{
		for (Asset a : _Assets)
		{
			if (a.getIdentifier().equalsIgnoreCase(assetID))
				return a;
		}
		
		return null;
	}

	public int getUnitStrength()
	{
		// TODO This will have to be expanded to include other unit types.
		return _Assets.size();
	}

	public int getUnitBV()
	{
		// TODO This will have to be expanded to include other unit types.
		DesignManager dm = DesignManager.getInstance();
		
		int totalBV = 0;
		for (Asset bm : _Assets)
			totalBV += dm.Design(bm.getVariantName()).getBV();

		return totalBV;
	}
	
	public PersonnelAssetAssignment getPersonnelAssetAssignment(String name)
	{
		for (PersonnelAssetAssignment ass : _PersonnelAssetAssignments)
		{
			if (ass.getName().equalsIgnoreCase(name))
				return ass;
		}
		
		return null;
	}
	
	public String getAssetAssignedToPersonnel(String name)
	{
		PersonnelAssetAssignment ass = getPersonnelAssetAssignment(name);
		if (ass != null)
			return ass.getAssetIdentifier();
		
		return null;
	}
	
	public String getAssetDetailForAssetAssignedToPersonnel(String name)
	{
		PersonnelAssetAssignment paa = getPersonnelAssetAssignment(name);
		if (paa != null)
		{
			Asset ass = getAssetFromID(paa.getAssetIdentifier()); 
			return ass.getDetails();
		}
		
		return null;
	}
	
	public List<PersonnelAssetAssignment> getPersonnelAssetAssignments()
	{
		return Collections.unmodifiableList(_PersonnelAssetAssignments);
	}
	
	public ArrayList<String> getPersonnelAssignedToAsset(String assetIdentifier)
	{
		ArrayList<String> personnel = new ArrayList<String>();
		for (PersonnelAssetAssignment ass : _PersonnelAssetAssignments)
		{
			if (ass.getAssetIdentifier().equalsIgnoreCase(assetIdentifier))
				personnel.add(ass.getName());
		}
		
		return personnel;		
	}
	
	public Mechwarrior getMechwarriorAssignedToMech(String assetIdentifier)
	{
		for (PersonnelAssetAssignment ass : _PersonnelAssetAssignments)
		{
			if (ass.getAssetIdentifier().equalsIgnoreCase(assetIdentifier))
			{
				if (ass.getRole() == Role.PILOT)
					return (Mechwarrior)getPersonnelByName(ass.getName());
			}
		}
		
		return null;
	}

	public Personnel getPersonnelByName(String name)
	{
		for (Personnel p: _Personnel)
		{
			if (p.getName().equalsIgnoreCase(name))
				return p;
		}
		
		return null;
	}
	
	public void addPersonnelAssignment(String name, String assetIdentifier, Role role)
	{
		PersonnelAssetAssignment paa = getPersonnelAssetAssignment(name);
		if (_PersonnelAssetAssignments.contains(paa))
			_PersonnelAssetAssignments.remove(paa);
		
		_PersonnelAssetAssignments.add(new PersonnelAssetAssignment(name, assetIdentifier, role));
	}
	
	public void removePersonnelAssignment(String name)
	{
		PersonnelAssetAssignment paa = getPersonnelAssetAssignment(name);
		if (paa != null)
			_PersonnelAssetAssignments.remove(paa);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		
		sb.append("Name: " + _Name + System.lineSeparator());
		sb.append("Player: " + _Player + System.lineSeparator());

		sb.append("Establish Date: " + _EstablishDate + System.lineSeparator());

		sb.append("Quality Rating: " + _QualityRating + System.lineSeparator());
		sb.append("Tech Rating: " + _TechRating + System.lineSeparator());
		sb.append("Warchest Points: " + _WarchestPoints + System.lineSeparator());

		sb.append(System.lineSeparator());
		sb.append("Notes: " + _Notes + System.lineSeparator());
		sb.append(System.lineSeparator());

		sb.append("Leader: " + _Leader + System.lineSeparator());
		sb.append("Personnel: " + System.lineSeparator());
		for (Personnel p : _Personnel)
			sb.append("    " + p.getSummary() + System.lineSeparator());
		sb.append(System.lineSeparator());
			
		sb.append("Assets: " + System.lineSeparator());
		for (Asset a : _Assets)
			sb.append("    " + a.toString() + System.lineSeparator());
		sb.append(System.lineSeparator());
		
		return sb.toString();
	}
	
}
