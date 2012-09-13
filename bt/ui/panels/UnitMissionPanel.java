package bt.ui.panels;

import javax.swing.*;


import bt.elements.unit.Unit;
import bt.managers.MissionManager;
import bt.managers.UnitManager;
import bt.ui.dialogs.CompletedMissionDialog;
import bt.ui.dialogs.GenerateNewMissionDialog;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>Title: Miradan Phedd</p>
 *
 * <p>Description: Library for all Miradan related work</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Miradan Phedd</p>
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
public class UnitMissionPanel extends JPanel implements ClosableEditPanel, ListSelectionListener, ActionListener
{
	private static final long serialVersionUID = 1;
	
    private static Log log = LogFactory.getLog(UnitMissionPanel.class);
    protected Unit _Unit;

    private JList<String> _CompletedMissionList;
    private JTextField _AssignedMissionTextField;
    
    private JButton _DeleteCompletedMissionButton;
    private JButton _ExportCompletedMissionButton;
    private JButton _ViewCompletedMissionButton;
    
    private JButton _DeleteAssignedMissionButton;
    private JButton _MarkMissionCompleteButton;
    private JButton _GenerateNewMissionButton;
    private JButton _ExportAssignedMissionButton;
    private JButton _ViewAssignedMissionButton;
    
    public UnitMissionPanel(Unit u)
    {
    	log.debug("UnitMissionPanel constructor called");
        _Unit = u;
        
        initialisePanel();
        fillControls();
        setButtonState();
    }

    public void initialisePanel()
    {
    	setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    	
    	JPanel assignedMissionsPanel = new JPanel();
    	assignedMissionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,80));
    	assignedMissionsPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Assigned Mission", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	assignedMissionsPanel.setLayout(new BoxLayout(assignedMissionsPanel, BoxLayout.PAGE_AXIS));
    	add(assignedMissionsPanel);
    	
    	_AssignedMissionTextField = new JTextField();
    	_AssignedMissionTextField.setEditable(false);
    	assignedMissionsPanel.add(_AssignedMissionTextField);
    	
    	JPanel assignedMissionButtonPanel = new JPanel();
    	assignedMissionsPanel.add(assignedMissionButtonPanel);

    	assignedMissionButtonPanel.setLayout(new BoxLayout(assignedMissionButtonPanel, BoxLayout.LINE_AXIS));

    	_DeleteAssignedMissionButton = new JButton("Delete");
    	_DeleteAssignedMissionButton.addActionListener(this);
    	_DeleteAssignedMissionButton.setActionCommand("DeleteAssignedMission");
    	assignedMissionButtonPanel.add(_DeleteAssignedMissionButton);
    	
    	Component assignedMissionsHorizontalGlue = Box.createHorizontalGlue();
    	assignedMissionButtonPanel.add(assignedMissionsHorizontalGlue);
    	
    	_MarkMissionCompleteButton = new JButton("Mark Complete");
    	_MarkMissionCompleteButton.addActionListener(this);
    	_MarkMissionCompleteButton.setActionCommand("MarkMissionComplete");
    	assignedMissionButtonPanel.add(_MarkMissionCompleteButton);
    	
    	Component assignedMissionsHorizontalGlue2 = Box.createHorizontalGlue();
    	assignedMissionButtonPanel.add(assignedMissionsHorizontalGlue2);
    	
    	_GenerateNewMissionButton = new JButton("Generate");
    	_GenerateNewMissionButton.addActionListener(this);
    	_GenerateNewMissionButton.setActionCommand("GenerateNewMission");
    	assignedMissionButtonPanel.add(_GenerateNewMissionButton);
    	
    	Component assignedMissionsHorizontalGlue3 = Box.createHorizontalGlue();
    	assignedMissionButtonPanel.add(assignedMissionsHorizontalGlue3);
    	
    	_ExportAssignedMissionButton = new JButton("Export");
    	_ExportAssignedMissionButton.addActionListener(this);
    	_ExportAssignedMissionButton.setActionCommand("ExportAssignedMission");
    	assignedMissionButtonPanel.add(_ExportAssignedMissionButton);
    	
    	_ViewAssignedMissionButton = new JButton("View");
    	_ViewAssignedMissionButton.addActionListener(this);
    	_ViewAssignedMissionButton.setActionCommand("ViewAssignedMission");
    	assignedMissionButtonPanel.add(_ViewAssignedMissionButton);
    	
    	JPanel completedMissionsPanel = new JPanel();
    	completedMissionsPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Completed Missions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	completedMissionsPanel.setLayout(new BorderLayout(2, 2));
    	add(completedMissionsPanel);
    	
    	_CompletedMissionList = new JList<String>();
    	_CompletedMissionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	_CompletedMissionList.addListSelectionListener(this);
    	completedMissionsPanel.add(_CompletedMissionList, BorderLayout.CENTER);
    	
    	JPanel completedMissionButtonPanel = new JPanel();
    	completedMissionsPanel.add(completedMissionButtonPanel, BorderLayout.SOUTH);
    	completedMissionButtonPanel.setLayout(new BoxLayout(completedMissionButtonPanel, BoxLayout.X_AXIS));
    	
    	_DeleteCompletedMissionButton = new JButton("Delete");
    	_DeleteCompletedMissionButton.setActionCommand("DeleteCompleteMission");
    	_DeleteCompletedMissionButton.addActionListener(this);
    	completedMissionButtonPanel.add(_DeleteCompletedMissionButton);
    	
    	Component completedMissionsHorizontalGlue = Box.createHorizontalGlue();
    	completedMissionButtonPanel.add(completedMissionsHorizontalGlue);
    	
    	_ExportCompletedMissionButton = new JButton("Export");
    	_ExportCompletedMissionButton.addActionListener(this);
    	_ExportCompletedMissionButton.setActionCommand("ExportCompleteMission");
    	completedMissionButtonPanel.add(_ExportCompletedMissionButton);
    	
    	_ViewCompletedMissionButton = new JButton("View");
    	_ViewCompletedMissionButton.addActionListener(this);
    	_ViewCompletedMissionButton.setActionCommand("ViewCompletedMission");
    	completedMissionButtonPanel.add(_ViewCompletedMissionButton);
    	

    }

    private void fillControls()
    {
    	_CompletedMissionList.setListData(MissionManager.getInstance().getMissionList(_Unit.getCompletedMissions()));
    	_AssignedMissionTextField.setText(MissionManager.getInstance().getMissionName(_Unit.getAssignedMission()));
    }
    
    private void setButtonState()
    {
    	boolean itemSelected = _CompletedMissionList.getSelectedIndex() > -1;
    	_ViewCompletedMissionButton.setEnabled(itemSelected);
    	_ExportCompletedMissionButton.setEnabled(itemSelected);
    	_DeleteCompletedMissionButton.setEnabled(itemSelected);
    	
    	boolean unitHasAssignedMission = _Unit.getAssignedMission() != null;
    	_DeleteAssignedMissionButton.setEnabled(unitHasAssignedMission);
    	_MarkMissionCompleteButton.setEnabled(unitHasAssignedMission);
    	_GenerateNewMissionButton.setEnabled(!unitHasAssignedMission);
    	_ExportAssignedMissionButton.setEnabled(unitHasAssignedMission);
    	_ViewAssignedMissionButton.setEnabled(unitHasAssignedMission);
    }
    
    public boolean isClosable()
    {
        return true;
    }

    public void forceEditCompletion()
    {
    }

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String actionCommand = e.getActionCommand();
		if (actionCommand.equalsIgnoreCase("DeleteAssignedMission"))
		{
			if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this Mission?", "Delete Mission", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{
				try
				{
					_Unit.assignedMissionAbandoned();
					UnitManager.getInstance().saveUnit(_Unit);
					fillControls();
					setButtonState();
				} 
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}
			}
		}
		if (actionCommand.equalsIgnoreCase("MarkMissionComplete"))
		{
			CompletedMissionDialog dlg = new CompletedMissionDialog(Long.toString(_Unit.getAssignedMission()), _Unit.getAssignedMissionTitle());
			dlg.setLocationRelativeTo(this);
			dlg.setModal(true);
			dlg.setVisible(true);
			
			if (dlg.wasMissionCompleted())
			{
				try
				{
					_Unit.assignedMissionCompleted(dlg.getMissionResult(), dlg.getPrizeMoney());
					UnitManager.getInstance().saveUnit(_Unit);
				} 
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}
			}
		}
		if (actionCommand.equalsIgnoreCase("GenerateNewMission"))
		{
			GenerateNewMissionDialog dlg = new GenerateNewMissionDialog(_Unit);
			dlg.setLocationRelativeTo(this);
			dlg.setModalityType(ModalityType.APPLICATION_MODAL);
			dlg.setVisible(true);
			
			if (dlg.wasMissionGenerated())
			{
				fillControls();
				setButtonState();
			}
		}
		if (actionCommand.equalsIgnoreCase("ExportAssignedMission"))
		{
			
		}
		if (actionCommand.equalsIgnoreCase("ViewAssignedMission"))
		{
			try
			{
				MissionManager.getInstance().printMissionDirectly(_Unit, _Unit.getAssignedMission());
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}

		}
		if (actionCommand.equalsIgnoreCase("DeleteCompleteMission"))
		{
			
		}
		if (actionCommand.equalsIgnoreCase("ExportCompleteMission"))
		{
			
		}
		if (actionCommand.equalsIgnoreCase("ViewCompletedMission"))
		{
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) 
	{
		setButtonState();
	}

}
