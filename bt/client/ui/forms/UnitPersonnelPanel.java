package bt.client.ui.forms;

import java.awt.Dimension;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import bt.client.ui.ClosableEditForm;
import bt.client.ui.models.PersonnelTableModel;
import bt.client.ui.models.TableSorter;
import bt.common.elements.personnel.JobType;
import bt.common.elements.personnel.Personnel;
import bt.common.elements.personnel.Rank;
import bt.common.elements.personnel.Rating;
import bt.common.elements.unit.Unit;
import bt.common.managers.SolarSystemManager;
import bt.common.util.SwingHelper;
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
public class UnitPersonnelPanel extends JPanel implements ClosableEditForm, MouseListener, ActionListener
{
	private static final long serialVersionUID = 1;
	
    private static Log log = LogFactory.getLog(UnitPersonnelPanel.class);
    protected Unit m_Unit;
    protected JTable m_PersonnelTable = new JTable();
    protected JScrollPane m_ScrollPane;
    protected PersonnelTableModel m_Model;
    protected TableSorter m_Sorter = new TableSorter();
    protected JPanel m_EditPanel = new JPanel();
    protected JPanel m_ButtonPanel = new JPanel();

    protected JTextField m_NameTextField = new JTextField();
    protected JTextField m_CallsignTextField = new JTextField();
    protected JComboBox m_RankCombo = new JComboBox();
    protected JComboBox m_HomePlanetCombo = new JComboBox();
    protected JComboBox m_RatingCombo = new JComboBox();
    protected JComboBox m_JobTypeCombo = new JComboBox();
    protected JComboBox m_GroupAssignmentCombo = new JComboBox();
    protected JTextArea m_NotesTextArea = new JTextArea();

    protected JButton m_AddPersonnelButton = new JButton("Add");
    protected JButton m_RemovePersonnelButton = new JButton("Remove");

    protected Personnel m_CurrentPersonnel;
    protected Personnel m_PreviousPersonnel;

    public UnitPersonnelPanel(Unit u)
    {
    	log.debug("UnitPersonnelPanel constructor called");
        m_Unit = u;
        m_Model = new PersonnelTableModel(m_Unit);
        m_Sorter.setTableModel(m_Model);
        m_PersonnelTable.setModel(m_Sorter);
        m_Sorter.setTableHeader(m_PersonnelTable.getTableHeader());
        m_PersonnelTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        m_PersonnelTable.setPreferredSize(new Dimension(1000, 600));
        m_PersonnelTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_PersonnelTable.addMouseListener(this);
        m_ScrollPane = new JScrollPane(m_PersonnelTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        m_EditPanel.setBorder(BorderFactory.createEtchedBorder());
        m_EditPanel.setLayout(new BoxLayout(m_EditPanel, BoxLayout.Y_AXIS));
        m_EditPanel.add(SwingHelper.GetTextFieldWithAction(m_NameTextField, "Name", "Name of the Unit member",true,"RandomName","Generate a Random Name",this));
        m_EditPanel.add(SwingHelper.GetTextField(m_CallsignTextField, "Callsign", "The Person's callsign",true));
        m_EditPanel.add(SwingHelper.GetComboBox(m_RankCombo,"Rank","The persons Rank",true));
        m_EditPanel.add(SwingHelper.GetComboBox(m_HomePlanetCombo,"Home Planet","The persons Home Planet",true));
        m_EditPanel.add(SwingHelper.GetComboBox(m_RatingCombo,"Rating","The persons experience rating",true));
        m_EditPanel.add(SwingHelper.GetComboBox(m_JobTypeCombo,"Job Type","The job type this person is used for",true));
        m_EditPanel.add(SwingHelper.GetComboBox(m_GroupAssignmentCombo,"Group Assignment","The group this person is assigned to",true));
        m_EditPanel.add(SwingHelper.GetTextArea(m_NotesTextArea, "Notes", "Notes for this Personnel record",true));

        m_AddPersonnelButton.setActionCommand("AddPersonnel");
        m_AddPersonnelButton.addActionListener(this);
        m_RemovePersonnelButton.setActionCommand("RemovePersonnel");
        m_RemovePersonnelButton.addActionListener(this);

        m_ButtonPanel.setLayout(new BorderLayout());
        m_ButtonPanel.add(m_AddPersonnelButton,BorderLayout.WEST);
        m_ButtonPanel.add(m_RemovePersonnelButton,BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(m_EditPanel,BorderLayout.NORTH);
        add(m_ScrollPane, BorderLayout.CENTER);
        add(m_ButtonPanel,BorderLayout.SOUTH);

        FillCombos();

        m_CurrentPersonnel = null;
        m_PreviousPersonnel = null;

        setVisible(true);
    }

    public boolean IsClosable()
    {
        return true;
    }

    public void ForceEditCompletion()
    {
        GetFields();
    }

    public void mouseClicked(MouseEvent me)
    {
        int Index = m_PersonnelTable.getSelectedRow();
        m_CurrentPersonnel = m_Unit.getPersonnel().elementAt(Index);
        if (m_CurrentPersonnel != m_PreviousPersonnel)
        {
            GetFields();
            m_PreviousPersonnel = m_CurrentPersonnel;
            SetFields();
        }
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
        if (command.equals("AddPersonnel"))
        {
            AddNewPersonnel();
        }
        if (command.equals("RemovePersonnel"))
        {

        }
        if (command.equals("RandomName"))
        {
            SetRandomName();
        }
    }

    protected void AddNewPersonnel()
    {
        //m_Unit.addNewPersonnel("Bob Smith");
    }

    protected void FillCombos()
    {
        FillRankCombo();
        FillRatingCombo();
        FillJobTypeCombo();
        FillPlanetCombo();
    }

    protected void FillRankCombo()
    {
        m_RankCombo.removeAllItems();
        for (Rank r: Rank.values())
            m_RankCombo.addItem(r);
        m_RankCombo.setSelectedIndex( -1);
    }

    protected void FillRatingCombo()
    {
        m_RatingCombo.removeAllItems();
        for (Rating r: Rating.values())
            m_RatingCombo.addItem(r);
        m_RatingCombo.setSelectedIndex( -1);
    }

    protected void FillJobTypeCombo()
    {
        m_JobTypeCombo.removeAllItems();
        for (JobType jt: JobType.values())
            m_JobTypeCombo.addItem(jt);
        m_JobTypeCombo.setSelectedIndex( -1);
    }

    protected void FillPlanetCombo()
    {
        m_HomePlanetCombo.removeAllItems();
        int PlanetCount = SolarSystemManager.getPlanetCount();
        for (int i = 0; i < PlanetCount; i++)
            m_HomePlanetCombo.addItem(SolarSystemManager.getPlanet(i));
        m_HomePlanetCombo.setSelectedIndex( -1);
    }

    protected void SetFields()
    {
        if (m_CurrentPersonnel != null)
        {
            m_NameTextField.setText(m_CurrentPersonnel.getName());
            m_CallsignTextField.setText(m_CurrentPersonnel.getCallsign());
            m_RankCombo.setSelectedIndex(m_CurrentPersonnel.getRank().ordinal());
            m_RatingCombo.setSelectedIndex(m_CurrentPersonnel.getRating().ordinal());
            m_JobTypeCombo.setSelectedIndex(m_CurrentPersonnel.getJobType().ordinal());
            m_GroupAssignmentCombo.setSelectedIndex(-1);
            m_NotesTextArea.setText(m_CurrentPersonnel.getNotes());
        }
        else
        {
            m_NameTextField.setText("");
            m_CallsignTextField.setText("");
            m_RankCombo.setSelectedIndex(-1);
            m_HomePlanetCombo.setSelectedIndex(-1);
            m_RatingCombo.setSelectedIndex(-1);
            m_JobTypeCombo.setSelectedIndex(-1);
            m_GroupAssignmentCombo.setSelectedIndex(-1);
            m_NotesTextArea.setText("");
        }
    }

    protected void GetFields()
    {
        if (m_PreviousPersonnel == null) return;

        m_PreviousPersonnel.setName(m_NameTextField.getText());
        m_PreviousPersonnel.setCallsign(m_CallsignTextField.getText());
        m_PreviousPersonnel.setRank((Rank)m_RankCombo.getSelectedItem());
        m_PreviousPersonnel.setRating((Rating)m_RatingCombo.getSelectedItem());
        m_PreviousPersonnel.setNotes(m_NotesTextArea.getText());
    }

    protected void SetRandomName()
    {
        if (m_PreviousPersonnel == null)
        {
            JOptionPane.showMessageDialog(this,"Select a Personnel record or add a new Record!","Adding Random Name",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        m_NameTextField.setText("Fix this");
    }
}
