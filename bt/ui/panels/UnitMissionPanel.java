package bt.ui.panels;

import javax.swing.*;
import bt.elements.unit.Unit;
import bt.managers.MissionManager;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Component;
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
    protected Unit m_Unit;

    private JList<String> _CompletedMissionList;
    private JList<String> _AssignedMissionList;
    
    private JButton _DeleteCompletedMissionButton;
    private JButton _MarkCompletedMissionIncompleteButton;
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
        m_Unit = u;
        
        InitialisePanel();
        FillLists();
    }

    public void InitialisePanel()
    {
    	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    	
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
    	
    	_MarkCompletedMissionIncompleteButton = new JButton("Mark Incomplete");
    	_MarkCompletedMissionIncompleteButton.addActionListener(this);
    	_MarkCompletedMissionIncompleteButton.setActionCommand("MarkMissionIncomplete");
    	completedMissionButtonPanel.add(_MarkCompletedMissionIncompleteButton);
    	
    	Component completedMissionsHorizontalGlue2 = Box.createHorizontalGlue();
    	completedMissionButtonPanel.add(completedMissionsHorizontalGlue2);
    	
    	_ExportCompletedMissionButton = new JButton("Export");
    	_ExportCompletedMissionButton.addActionListener(this);
    	_ExportCompletedMissionButton.setActionCommand("ExportCompleteMission");
    	completedMissionButtonPanel.add(_ExportCompletedMissionButton);
    	
    	_ViewCompletedMissionButton = new JButton("View");
    	_ViewCompletedMissionButton.addActionListener(this);
    	_ViewCompletedMissionButton.setActionCommand("ViewCompletedMission");
    	completedMissionButtonPanel.add(_ViewCompletedMissionButton);
    	
    	JPanel assignedMissionsPanel = new JPanel();
    	assignedMissionsPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Assigned Missions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	assignedMissionsPanel.setLayout(new BorderLayout(2, 2));
    	add(assignedMissionsPanel);
    	
    	_AssignedMissionList = new JList<String>();
    	_AssignedMissionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	_AssignedMissionList.addListSelectionListener(this);
    	assignedMissionsPanel.add(_AssignedMissionList, BorderLayout.CENTER);
    	
    	JPanel assignedMissionButtonPanel = new JPanel();
    	assignedMissionsPanel.add(assignedMissionButtonPanel, BorderLayout.SOUTH);
    	assignedMissionButtonPanel.setLayout(new BoxLayout(assignedMissionButtonPanel, BoxLayout.X_AXIS));

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
    }

    private void FillLists()
    {
    	_CompletedMissionList.setListData(MissionManager.getInstance().getMissionList(m_Unit.getCompletedMissions()));
    	_AssignedMissionList.setListData(MissionManager.getInstance().getMissionList(m_Unit.getAssignedMissions()));
    }
    
    public boolean IsClosable()
    {
        return true;
    }

    public void ForceEditCompletion()
    {
    }

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

}
