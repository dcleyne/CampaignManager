package bt.elements.unit;

public class PersonnelAssetAssignment implements Comparable<PersonnelAssetAssignment>
{
	private String _Name;
	private String _AssetIdentifier;
	private Role _Role;
	
	public String getName() {
		return _Name;
	}
	public void setName(String name) {
		_Name = name;
	}
	public String getAssetIdentifier() {
		return _AssetIdentifier;
	}
	public void setAssetIdentifier(String assetIdentifier) {
		_AssetIdentifier = assetIdentifier;
	}
	public Role getRole() {
		return _Role;
	}
	public void setRole(Role role) {
		_Role = role;
	}
	
	public PersonnelAssetAssignment()
	{
	}
	
	public PersonnelAssetAssignment(String name, String assetIdentifier, Role role)
	{
		_Name = name;
		_AssetIdentifier = assetIdentifier;
		_Role = role;
	}
	
	public String toString()
	{
		return "Name: " + _Name + "  Asset: " + _AssetIdentifier + "  Role: " + _Role.toString();
	}
	
	@Override
	public int compareTo(PersonnelAssetAssignment paa) 
	{
		return toString().compareTo(paa.toString());
	}
	

}
