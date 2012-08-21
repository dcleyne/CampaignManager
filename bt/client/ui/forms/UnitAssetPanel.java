package bt.client.ui.forms;

import javax.swing.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import bt.client.ui.ClosableEditForm;
import bt.client.ui.models.AssetTableModel;
import bt.client.ui.models.TableSorter;
import bt.common.elements.Asset;
import bt.common.elements.unit.Unit;
import bt.common.util.SwingHelper;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

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
public class UnitAssetPanel extends JPanel implements ClosableEditForm, MouseListener, ActionListener
{
	private static final long serialVersionUID = 1;
	
    private static Log log = LogFactory.getLog(UnitAssetPanel.class);
    protected Unit m_Unit;
    protected JTable m_AssetTable = new JTable();
    protected JScrollPane m_ScrollPane;
    protected AssetTableModel m_Model;
    protected TableSorter m_Sorter = new TableSorter();
    protected JPanel m_EditPanel = new JPanel();
    protected JPanel m_ButtonPanel = new JPanel();

    protected JTextField m_IdentifierTextField = new JTextField();
    protected JTextField m_ElementTypeTextField = new JTextField();
    protected JTextField m_ElementNameTextField = new JTextField();
    protected JTextField m_StatusTextField = new JTextField();
    protected JTextField m_ConditionTextField = new JTextField();
    protected JComboBox m_GroupAssignmentCombo = new JComboBox();
    protected JTextArea m_NotesTextArea = new JTextArea();

    protected JButton m_AddAssetButton = new JButton("Add");
    protected JButton m_RemoveAssetButton = new JButton("Remove");

    protected Asset m_CurrentAsset;
    protected Asset m_PreviousAsset;

    protected AddElementsDialog m_AddElementsDialog = null;

    public UnitAssetPanel(Unit u)
    {
    	log.debug("Unit Asset Panel Constructor called");
    	
        m_Unit = u;
        m_Unit = u;
        m_Model = new AssetTableModel(m_Unit);
        m_Sorter.setTableModel(m_Model);
        m_AssetTable.setModel(m_Sorter);
        m_Sorter.setTableHeader(m_AssetTable.getTableHeader());
        m_AssetTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        m_AssetTable.setPreferredSize(new Dimension(1000, 600));
        m_AssetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_AssetTable.addMouseListener(this);
        m_ScrollPane = new JScrollPane(m_AssetTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        m_EditPanel.setBorder(BorderFactory.createEtchedBorder());
        m_EditPanel.setLayout(new BoxLayout(m_EditPanel, BoxLayout.Y_AXIS));
        m_EditPanel.add(SwingHelper.GetTextFieldWithAction(m_IdentifierTextField, "Identifier", "Identification of the Asset",true,"RandomName","Generate a Random Name",this));
        m_EditPanel.add(SwingHelper.GetTextField(m_ElementTypeTextField, "Element Type", "The type of Element this asset is",true));
        m_EditPanel.add(SwingHelper.GetTextField(m_ElementNameTextField, "Element Name", "The name of the Element this asset is",true));
        m_EditPanel.add(SwingHelper.GetTextField(m_StatusTextField, "Status", "The status of this Asset",true));
        m_EditPanel.add(SwingHelper.GetTextField(m_ConditionTextField, "Condition", "The condition of this Asset",true));
        m_EditPanel.add(SwingHelper.GetComboBox(m_GroupAssignmentCombo,"Group Assignment","The group this Asset is assigned to",true));
        m_EditPanel.add(SwingHelper.GetTextArea(m_NotesTextArea, "Notes", "Notes for this Asset",true));

        m_ElementTypeTextField.setEnabled(false);
        m_ElementNameTextField.setEnabled(false);

        m_AddAssetButton.setActionCommand("AddAsset");
        m_AddAssetButton.addActionListener(this);
        m_RemoveAssetButton.setActionCommand("RemoveAsset");
        m_RemoveAssetButton.addActionListener(this);

        m_ButtonPanel.setLayout(new BorderLayout());
        m_ButtonPanel.add(m_AddAssetButton,BorderLayout.WEST);
        m_ButtonPanel.add(m_RemoveAssetButton,BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(m_EditPanel,BorderLayout.NORTH);
        add(m_ScrollPane, BorderLayout.CENTER);
        add(m_ButtonPanel,BorderLayout.SOUTH);

        FillCombos();

        m_CurrentAsset = null;
        m_PreviousAsset = null;

        setVisible(true);

    }


    public boolean IsClosable()
    {
        return true;
    }

    public void ForceEditCompletion()
    {
    }
    
    public void mouseClicked(MouseEvent me)
    {
    	/*
        int Index = m_AssetTable.getSelectedRow();
        m_CurrentAsset = m_Unit.getAsset(Index);
        if (m_CurrentAsset != m_PreviousAsset)
        {
            GetFields();
            m_PreviousAsset = m_CurrentAsset;
            SetFields();
        }
        */
    }

    public void mouseEntered(MouseEvent me)
    {

    }

    public void mouseExited(MouseEvent me)
    {

    }

    public void mousePressed(MouseEvent me)
    {

    }

    public void mouseReleased(MouseEvent me)
    {
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if (command.equals("AddAsset"))
        {
            AddNewAsset();
        }
        if (command.equals("RemoveAsset"))
        {

        }
        if (command.equals("RandomIdentifier"))
        {
            SetRandomIdentifier();
        }
    }

    protected void AddNewAsset()
    {
        if (m_AddElementsDialog == null)
        {
            m_AddElementsDialog = new AddElementsDialog(this,m_Unit);
        }
        if (!m_AddElementsDialog.isVisible())
            m_AddElementsDialog.setVisible(true);

        m_AddElementsDialog.requestFocus();
    }

    protected void SetFields()
    {
        if (m_CurrentAsset != null)
        {
            m_IdentifierTextField.setText(m_CurrentAsset.getIdentifier());
            m_ElementTypeTextField.setText(m_CurrentAsset.getElementType().toString());
            m_ElementNameTextField.setText("Fix this");
            m_GroupAssignmentCombo.setSelectedIndex(-1);
            m_StatusTextField.setText(m_CurrentAsset.getStatus());
            m_ConditionTextField.setText(m_CurrentAsset.getCondition());
            m_NotesTextArea.setText(m_CurrentAsset.getNotes());
        }
        else
        {
            m_IdentifierTextField.setText("");
            m_ElementTypeTextField.setText("");
            m_ElementNameTextField.setText("");
            m_GroupAssignmentCombo.setSelectedIndex(-1);
            m_StatusTextField.setText("");
            m_ConditionTextField.setText("");
            m_NotesTextArea.setText("");
        }
    }

    protected void GetFields()
    {
        if (m_PreviousAsset == null) return;

        m_PreviousAsset.setIdentifier(m_IdentifierTextField.getText());
        m_PreviousAsset.setStatus(m_StatusTextField.getText());
        m_PreviousAsset.setCondition(m_ConditionTextField.getText());
        m_PreviousAsset.setGroupAssignment(-1);
        m_PreviousAsset.setNotes(m_NotesTextArea.getText());
    }


    protected void SetRandomIdentifier()
    {
        if (m_CurrentAsset == null)
        {
            JOptionPane.showMessageDialog(this,"Select an Asset record or add a new Record!","Setting Random Identifier",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        m_IdentifierTextField.setText("Fix this");
    }

    protected void FillCombos()
    {
    }

}
