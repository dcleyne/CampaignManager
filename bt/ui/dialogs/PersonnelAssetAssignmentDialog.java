package bt.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import bt.elements.unit.Unit;
import bt.ui.panels.PersonnelAssetAssignmentPanel;

public class PersonnelAssetAssignmentDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = -1764806639655146333L;

	private JPanel _ButtonPanel;
	private JButton _CancelButton;
	private JButton _OkButton;
	private PersonnelAssetAssignmentPanel _AssignmentPanel;
	
	private boolean _AssetAssigned = false;
	
	public PersonnelAssetAssignmentDialog(String personnelName, Unit u)
	{
		BorderLayout thisLayout = new BorderLayout();
		getContentPane().setLayout(thisLayout);
		{
			_ButtonPanel = new JPanel();
			getContentPane().add(_ButtonPanel, BorderLayout.SOUTH);
			FlowLayout _ButtonPanelLayout = new FlowLayout();
			_ButtonPanelLayout.setAlignment(FlowLayout.RIGHT);
			_ButtonPanel.setLayout(_ButtonPanelLayout);
			{
				_OkButton = new JButton();
				_OkButton.addActionListener(this);
				_OkButton.setActionCommand("Ok");
				_ButtonPanel.add(_OkButton);
				_OkButton.setText("Ok");
			}
			{
				_CancelButton = new JButton();
				_CancelButton.setActionCommand("Cancel");
				_CancelButton.addActionListener(this);
				_ButtonPanel.add(_CancelButton);
				_CancelButton.setText("Cancel");
			}			
		}
		setTitle("Mission Complete");
		
		_AssignmentPanel = new PersonnelAssetAssignmentPanel(personnelName, u);
		getContentPane().add(_AssignmentPanel, BorderLayout.CENTER);
		
		setSize(450,200);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String actionCommand = e.getActionCommand();
		if (actionCommand.equalsIgnoreCase("Ok"))
		{
			_AssignmentPanel.setPersonnelAssignedToAsset();
			_AssetAssigned = true;
			setVisible(false);
		}
		if (actionCommand.equalsIgnoreCase("Cancel"))
		{
			setVisible(false);
		}		
	}
	
	public boolean wasAssetAssigned()
	{
		return _AssetAssigned;
	}
}
