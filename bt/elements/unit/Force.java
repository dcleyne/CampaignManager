package bt.elements.unit;

import java.util.ArrayList;
import java.util.Date;

import bt.elements.Asset;
import bt.elements.personnel.Personnel;

public class Force
{
	private String _ParentUnit;	
	private Date _CurrentDate;
	private QualityRating _QualityRating;
	private TechRating _TechRating;

	private ArrayList<Personnel> _AssignedPersonnel = new ArrayList<Personnel>();
	private ArrayList<Asset> _AssignedAssets = new ArrayList<Asset>();

	private ArrayList<PersonnelAssetAssignment> _PersonnelAssetAssignments = new ArrayList<PersonnelAssetAssignment>();
	
	public String getParentUnit()
	{
		return _ParentUnit;
	}

	public void setParentUnit(String parentUnit)
	{
		_ParentUnit = parentUnit;
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

}
