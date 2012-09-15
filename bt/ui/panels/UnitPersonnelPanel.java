package bt.ui.panels;

import java.awt.Dimension;


import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import bt.elements.galaxy.InnerSpherePlanet;
import bt.elements.personnel.JobType;
import bt.elements.personnel.Personnel;
import bt.elements.personnel.Rank;
import bt.elements.personnel.Rating;
import bt.elements.unit.Unit;
import bt.managers.SolarSystemManager;
import bt.managers.UnitManager;
import bt.ui.dialogs.PersonnelAssetAssignmentDialog;
import bt.ui.models.PersonnelTableModel;
import bt.ui.models.TableSorter;
import bt.util.SwingHelper;
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
public class UnitPersonnelPanel extends JPanel implements ClosableEditPanel, MouseListener, ActionListener
{
	private static final long serialVersionUID = 1;
	
    protected Unit _Unit;
    protected JTable _PersonnelTable = new JTable();
    protected JScrollPane _ScrollPane;
    protected PersonnelTableModel _Model;
    protected TableSorter _Sorter = new TableSorter();
    protected JPanel _EditPanel = new JPanel();
    protected JPanel _ButtonPanel = new JPanel();

    protected JTextField _NameTextField = new JTextField();
    protected JTextField _CallsignTextField = new JTextField();
    protected JComboBox<Rank> _RankCombo = new JComboBox<Rank>();
    protected JComboBox<InnerSpherePlanet> _HomePlanetCombo = new JComboBox<InnerSpherePlanet>();
    protected JComboBox<Rating> _RatingCombo = new JComboBox<Rating>();
    protected JComboBox<JobType> _JobTypeCombo = new JComboBox<JobType>();
    protected JTextField _AssetAssignmentTextField = new JTextField();
    protected JTextArea _NotesTextArea = new JTextArea();

    protected JButton _AddPersonnelButton = new JButton("Add");
    protected JButton _RemovePersonnelButton = new JButton("Remove");

    protected Personnel _CurrentPersonnel;
    protected Personnel _PreviousPersonnel;

    public UnitPersonnelPanel(Unit u)
    {
        _Unit = u;
        _Model = new PersonnelTableModel(_Unit);
        _Sorter.setTableModel(_Model);
        _PersonnelTable.setModel(_Sorter);
        _Sorter.setTableHeader(_PersonnelTable.getTableHeader());
        _PersonnelTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        _PersonnelTable.setPreferredSize(new Dimension(1000, 600));
        _PersonnelTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _PersonnelTable.addMouseListener(this);
        _ScrollPane = new JScrollPane(_PersonnelTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        _EditPanel.setBorder(BorderFactory.createEtchedBorder());
        _EditPanel.setLayout(new BoxLayout(_EditPanel, BoxLayout.Y_AXIS));
        _EditPanel.add(SwingHelper.GetTextFieldWithAction(_NameTextField, "Name", "Name of the Unit member",true,"RandomName","Generate a Random Name",this));
        _EditPanel.add(SwingHelper.GetTextField(_CallsignTextField, "Callsign", "The Person's callsign",true));
        _EditPanel.add(SwingHelper.GetComboBox(_RankCombo,"Rank","The persons Rank",true));
        _EditPanel.add(SwingHelper.GetComboBox(_HomePlanetCombo,"Home Planet","The persons Home Planet",true));
        _EditPanel.add(SwingHelper.GetComboBox(_RatingCombo,"Rating","The persons experience rating",true));
        _EditPanel.add(SwingHelper.GetComboBox(_JobTypeCombo,"Job Type","The job type this person is used for",true));
        _EditPanel.add(SwingHelper.GetTextFieldWithAction(_AssetAssignmentTextField,"Asset Assignment","The Asset this person is assigned to",true, "AssetAssignment", "Set asset assignment for this person", this));
        _EditPanel.add(SwingHelper.GetTextArea(_NotesTextArea, "Notes", "Notes for this Personnel record",true));

        _AddPersonnelButton.setActionCommand("AddPersonnel");
        _AddPersonnelButton.addActionListener(this);
        _RemovePersonnelButton.setActionCommand("RemovePersonnel");
        _RemovePersonnelButton.addActionListener(this);

        _AssetAssignmentTextField.setEditable(false);
        
        _ButtonPanel.setLayout(new BorderLayout());
        _ButtonPanel.add(_AddPersonnelButton,BorderLayout.WEST);
        _ButtonPanel.add(_RemovePersonnelButton,BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(_EditPanel,BorderLayout.NORTH);
        add(_ScrollPane, BorderLayout.CENTER);
        add(_ButtonPanel,BorderLayout.SOUTH);

        FillCombos();

        _CurrentPersonnel = null;
        _PreviousPersonnel = null;

        setVisible(true);
    }

    public boolean isClosable()
    {
        return true;
    }

    public void forceEditCompletion()
    {
        GetFields();
    }

    public void mouseClicked(MouseEvent me)
    {
        int Index = _PersonnelTable.getSelectedRow();
        _CurrentPersonnel = _Unit.getPersonnel().elementAt(Index);
        if (_CurrentPersonnel != _PreviousPersonnel)
        {
            GetFields();
            _PreviousPersonnel = _CurrentPersonnel;
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
        if (command.equalsIgnoreCase("AssetAssignment"))
        {
        	PersonnelAssetAssignmentDialog dlg = new PersonnelAssetAssignmentDialog(_CurrentPersonnel.getName(), _Unit);
        	dlg.setLocationRelativeTo(this);
        	dlg.setModal(true);
        	dlg.setVisible(true);
        	
        	if (dlg.wasAssetAssigned())
        	{
        		try
        		{
        			UnitManager.getInstance().saveUnit(_Unit);
        			SetFields();
        		}
        		catch (Exception ex)
        		{
        			ex.printStackTrace();
        		}
        	}
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
        _RankCombo.removeAllItems();
        for (Rank r: Rank.values())
            _RankCombo.addItem(r);
        _RankCombo.setSelectedIndex( -1);
    }

    protected void FillRatingCombo()
    {
        _RatingCombo.removeAllItems();
        for (Rating r: Rating.values())
            _RatingCombo.addItem(r);
        _RatingCombo.setSelectedIndex( -1);
    }

    protected void FillJobTypeCombo()
    {
        _JobTypeCombo.removeAllItems();
        for (JobType jt: JobType.values())
            _JobTypeCombo.addItem(jt);
        _JobTypeCombo.setSelectedIndex( -1);
    }

    protected void FillPlanetCombo()
    {
        _HomePlanetCombo.removeAllItems();
        int PlanetCount = SolarSystemManager.getPlanetCount();
        for (int i = 0; i < PlanetCount; i++)
            _HomePlanetCombo.addItem(SolarSystemManager.getPlanet(i));
        _HomePlanetCombo.setSelectedIndex( -1);
    }

    protected void SetFields()
    {
        if (_CurrentPersonnel != null)
        {
            _NameTextField.setText(_CurrentPersonnel.getName());
            _CallsignTextField.setText(_CurrentPersonnel.getCallsign());
            _RankCombo.setSelectedIndex(_CurrentPersonnel.getRank().ordinal());
            _RatingCombo.setSelectedIndex(_CurrentPersonnel.getRating().ordinal());
            _JobTypeCombo.setSelectedIndex(_CurrentPersonnel.getJobType().ordinal());
            _AssetAssignmentTextField.setText(_Unit.getAssetDetailForAssetAssignedToPersonnel(_CurrentPersonnel.getName()));
            _NotesTextArea.setText(_CurrentPersonnel.getNotes());
        }
        else
        {
            _NameTextField.setText("");
            _CallsignTextField.setText("");
            _RankCombo.setSelectedIndex(-1);
            _HomePlanetCombo.setSelectedIndex(-1);
            _RatingCombo.setSelectedIndex(-1);
            _JobTypeCombo.setSelectedIndex(-1);
            _AssetAssignmentTextField.setText("");
            _NotesTextArea.setText("");
        }
    }

    protected void GetFields()
    {
        if (_PreviousPersonnel == null) return;

        _PreviousPersonnel.setName(_NameTextField.getText());
        _PreviousPersonnel.setCallsign(_CallsignTextField.getText());
        _PreviousPersonnel.setRank((Rank)_RankCombo.getSelectedItem());
        _PreviousPersonnel.setRating((Rating)_RatingCombo.getSelectedItem());
        _PreviousPersonnel.setNotes(_NotesTextArea.getText());
    }

    protected void SetRandomName()
    {
        if (_PreviousPersonnel == null)
        {
            JOptionPane.showMessageDialog(this,"Select a Personnel record or add a new Record!","Adding Random Name",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        _NameTextField.setText("Fix this");
    }
}
