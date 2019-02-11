package bt.ui.panels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bt.elements.Asset;
import bt.elements.unit.PersonnelAssetAssignment;
import bt.elements.unit.Role;
import bt.elements.unit.Unit;
import bt.util.SwingHelper;

public class PersonnelAssetAssignmentPanel extends JPanel
{
	private static final long serialVersionUID = -3654449376751784958L;

	private Unit _Unit;
	private boolean _AssetCurrentlyAssigned = false;
	private String _Name;
	private String _AssignedAsset;
	private Role _AssignedRole = Role.PILOT;
	
	private JTextField _NameTextField;
	private JComboBox<Role> _RoleComboBox;
	private JComboBox<String> _AvailableAssetsComboBox;
	
	private HashMap<String, String> _AssetIDDetailMap = new HashMap<String, String>();
	private HashMap<String, String> _AssetDetailIDMap = new HashMap<String, String>();
	
	public PersonnelAssetAssignmentPanel(String personnelName, Unit u)
	{
		_Name = personnelName;
		_Unit = u;
		
		for (Asset asset : u.getAssets())
		{
			_AssetIDDetailMap.put(asset.getIdentifier(), asset.getDetails());
			_AssetDetailIDMap.put(asset.getDetails(), asset.getIdentifier());
		}
		
		Vector<String> assetAssignment = new Vector<String>();
		
		PersonnelAssetAssignment paa = u.getPersonnelAssetAssignment(personnelName);
		if (paa != null)
		{
			_AssetCurrentlyAssigned = true;
			_AssignedRole = paa.getRole();
			_AssignedAsset = paa.getAssetIdentifier();
			assetAssignment.add(_AssetIDDetailMap.get(_AssignedAsset));
		}
		
		assetAssignment.add("<No Assignment>");
		
		ArrayList<String> unassignedAssetIDs = u.getUnassignedAssetIDs();
		for (String id : unassignedAssetIDs)
		{
			assetAssignment.add(_AssetIDDetailMap.get(id));
		}
		
		_NameTextField = new JTextField(personnelName);
		_NameTextField.setEditable(false);
		
		_RoleComboBox = new JComboBox<Role>(Role.values());
		_AvailableAssetsComboBox = new JComboBox<String>(assetAssignment);
		
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(SwingHelper.GetTextField(_NameTextField, "Name", "Name of the personnel to set assignment for", true));
		add(SwingHelper.GetComboBox(_RoleComboBox, "Role", "The role this personnel assumes for this asset assignment", true));
		add(SwingHelper.GetComboBox(_AvailableAssetsComboBox, "Available Assets", "Assets available to assign this personnel to", true));
		
		_NameTextField.setText(personnelName);
		_RoleComboBox.setSelectedItem(_AssignedRole);
		_AvailableAssetsComboBox.setSelectedIndex(0);
	}
	
	public Unit getUnit()
	{
		return _Unit;
	}
	
	public void setPersonnelAssignedToAsset()
	{
		if (_AssetCurrentlyAssigned)
		{
			if (_AvailableAssetsComboBox.getSelectedIndex() > 1)
			{
				// New asset assignment
				_Unit.removePersonnelAssignment(_Name, _AssignedAsset, _AssignedRole);
				
				String newAssetIdentifier = _AssetDetailIDMap.get((String)_AvailableAssetsComboBox.getSelectedItem());
				Role newRole = (Role)_RoleComboBox.getSelectedItem();
				_Unit.addPersonnelAssignment(_Name, newAssetIdentifier, newRole);
			}
			else if (_AvailableAssetsComboBox.getSelectedIndex() == 1)
			{
				// Remove assignment
				_Unit.removePersonnelAssignment(_Name, _AssignedAsset, _AssignedRole);
			}
		}
		else
		{
			if (_AvailableAssetsComboBox.getSelectedIndex() > 0)
			{
				String newAssetIdentifier = _AssetDetailIDMap.get((String)_AvailableAssetsComboBox.getSelectedItem());
				Role newRole = (Role)_RoleComboBox.getSelectedItem();
				_Unit.addPersonnelAssignment(_Name, newAssetIdentifier, newRole);
			}			
		}
	}
}
