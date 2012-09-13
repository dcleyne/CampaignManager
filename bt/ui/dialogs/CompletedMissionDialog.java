package bt.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import bt.elements.unit.CompletedMission;
import bt.ui.panels.CompletedMissionPanel;

public class CompletedMissionDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = -2380371521047144344L;
	private JPanel _ButtonPanel;
	private JButton _CancelButton;
	private JButton _OkButton;
	private CompletedMissionPanel _CompletedMissionPanel;
	
	private boolean _MissionCompleted = false;
	
	public CompletedMissionDialog(String missionID, String missionTitle)
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
		
		_CompletedMissionPanel = new CompletedMissionPanel(missionID, missionTitle);
		getContentPane().add(_CompletedMissionPanel, BorderLayout.CENTER);
		
		setSize(350,200);
	}
	
	public boolean wasMissionCompleted()
	{
		return _MissionCompleted;
	}
	
	public double getPrizeMoney()
	{
		return _CompletedMissionPanel.getPrizeMoney();
	}
	
	public CompletedMission.Result getMissionResult()
	{
		return _CompletedMissionPanel.getResult();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String actionCommand = e.getActionCommand();
		if (actionCommand.equalsIgnoreCase("Ok"))
		{
			_MissionCompleted = true;
			setVisible(false);
		}
		if (actionCommand.equalsIgnoreCase("Cancel"))
		{
			setVisible(false);
		}		
	}
}
