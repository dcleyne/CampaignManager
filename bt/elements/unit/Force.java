package bt.elements.unit;

import java.time.LocalDate;
import java.util.ArrayList;

import bt.elements.Asset;
import bt.elements.Battlemech;
import bt.elements.collection.ItemCollection;
import bt.elements.personnel.Mechwarrior;
import bt.elements.personnel.Personnel;
import bt.managers.DesignManager;

public class Force
{
	private String _ParentUnit;	
	private LocalDate _CurrentDate;
	private QualityRating _QualityRating;
	private TechRating _TechRating;
	private String _ItemCollectionName;

	private ArrayList<Personnel> _AssignedPersonnel = new ArrayList<Personnel>();
	private ArrayList<Asset> _AssignedAssets = new ArrayList<Asset>();

	private ArrayList<PersonnelAssetAssignment> _PersonnelAssetAssignments = new ArrayList<PersonnelAssetAssignment>();
	
	public Force()
	{
		
	}
	
	public Force(Unit u, ItemCollection collection)
	{
		_ParentUnit = u.getName();
		_QualityRating = u.getQualityRating();
		_TechRating = u.getTechRating();
		_ItemCollectionName = collection.getName();
		mergeUnit(u);
	}
	
	public String getParentUnit()
	{
		return _ParentUnit;
	}

	public void setParentUnit(String parentUnit)
	{
		_ParentUnit = parentUnit;
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

	public String getItemCollectionName()
	{
		return _ItemCollectionName;
	}

	public void setItemCollectionName(String itemCollectionName)
	{
		_ItemCollectionName = itemCollectionName;
	}

	public void assignAssetToForce(Asset asset, Personnel personnel, Role role)
	{
		if (!_AssignedAssets.contains(asset))
			_AssignedAssets.add(asset);
		
		if (!_AssignedPersonnel.contains(personnel))
			_AssignedPersonnel.add(personnel);
		
		ArrayList<PersonnelAssetAssignment> existingAssignments = new ArrayList<PersonnelAssetAssignment>();
		for (PersonnelAssetAssignment paa: _PersonnelAssetAssignments)
		{
			if (paa.getAssetIdentifier().equalsIgnoreCase(asset.getIdentifier()) || 
					paa.getName().equalsIgnoreCase(personnel.getName()))
				existingAssignments.add(paa);
		}
		_PersonnelAssetAssignments.removeAll(existingAssignments);
		_PersonnelAssetAssignments.add(new PersonnelAssetAssignment(personnel.getName(), asset.getIdentifier(), role));
	}

	public ArrayList<Asset> getAssets()
	{
		return _AssignedAssets;
	}
	
	public ArrayList<Personnel> getPersonnel()
	{
		return _AssignedPersonnel;
	}
	
	public ArrayList<PersonnelAssetAssignment> getPersonnelAssetAssignments()
	{
		return _PersonnelAssetAssignments;
	}
	
	public int getAssetCount()
	{
		return _PersonnelAssetAssignments.size();
	}
	
	public int getAssetBV()
	{
		DesignManager dm = DesignManager.getInstance();
		int BV = 0;
		
		for (Asset asset : _AssignedAssets)
		{
			if (asset instanceof Battlemech)
			{
				Battlemech bm = (Battlemech)asset;
				BV += dm.Design(bm.getVariantName()).getBV();
			}
		}
		
		return BV;
	}
	
	public Personnel getPersonnelByName(String name)
	{
		for (Personnel p: _AssignedPersonnel)
		{
			if (p.getName().equalsIgnoreCase(name))
				return p;
		}
		
		return null;
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

	public PersonnelAssetAssignment getPersonnelAssetAssignment(String name)
	{
		for (PersonnelAssetAssignment ass : _PersonnelAssetAssignments)
		{
			if (ass.getName().equalsIgnoreCase(name))
				return ass;
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
	

	public void mergeForce(Force f)
	{
		_AssignedAssets.addAll(f._AssignedAssets);
		_AssignedPersonnel.addAll(f._AssignedPersonnel);
		_PersonnelAssetAssignments.addAll(f._PersonnelAssetAssignments);
	}

	public void mergeUnit(Unit u)
	{
		for (PersonnelAssetAssignment paa : u.getPersonnelAssetAssignments())
		{
			Personnel p = u.getPersonnelByName(paa.getName());
			Asset a = u.getAssetFromID(u.getAssetAssignedToPersonnel(p.getName()));
			assignAssetToForce(a, p, paa.getRole());
		}
	}
}
