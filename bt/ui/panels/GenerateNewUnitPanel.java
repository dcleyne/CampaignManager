package bt.ui.panels;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JButton;

import bt.elements.Battlemech;
import bt.elements.personnel.Personnel;
import bt.elements.personnel.Rating;
import bt.elements.unit.Player;
import bt.elements.unit.RandomName;
import bt.elements.unit.Unit;
import bt.managers.UnitManager;

public class GenerateNewUnitPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private String[] _UnitWeights = {
			"Ultralight",
			"Light",
			"Light Medium",
			"Medium",
			"Medium Heavy",
			"Heavy",
			"Light Assault",
			"Assault",
			"Heavy Assault"}; 
	
    private Unit _GeneratedUnit = new Unit();
    private Player _Player = new Player();
    private UnitManager _UnitManager = UnitManager.getInstance();
	
	
	private JPanel _PlayerPanel = null;
	private JLabel _PlayerNameLabel = null;
	private JTextField _PlayerNameTextField = null;
	private JLabel _PlayerEmailAddressLabel = null;
	private JTextField _PlayerEmailAddressTextField = null;
	private JLabel _UnitNameLabel = null;
	private JTextField _UnitNameTextField = null;
	private JLabel _UnitWeightLabel = null;
	private JComboBox _UnitWeightComboBox = null;
	private JLabel _MechListLabel = null;
	private JScrollPane _MechList = null;
	private DefaultListModel _MechListModel = null;
	private JLabel _PersonnelListLabel = null;
	private JScrollPane _PersonnelList = null;
	private DefaultListModel _PersonnelListModel = null;
	private JLabel _MechBVLabel = null;
	private JLabel _MechWeightLabel = null;
	private JLabel _AnnualSalaryLabel = null;
	private JLabel _BiWeeklySalaryLabel = null;
	private JTextField _MechBVTextField = null;
	private JTextField _MechWeightTextField = null;
	private JTextField _AnnualSalaryTextField = null;
	private JTextField _BiWeeklySalaryTextField = null;
	private JButton _GenerateButton = null;
	private JButton _SaveButton = null;

	/**
	 * This method initializes 
	 * 
	 */
	public GenerateNewUnitPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        _BiWeeklySalaryLabel = new JLabel();
        _BiWeeklySalaryLabel.setBounds(new Rectangle(316, 466, 105, 16));
        _BiWeeklySalaryLabel.setText("BiWeekly Salary");
        _AnnualSalaryLabel = new JLabel();
        _AnnualSalaryLabel.setBounds(new Rectangle(210, 466, 91, 16));
        _AnnualSalaryLabel.setText("Annual Salary");
        _MechWeightLabel = new JLabel();
        _MechWeightLabel.setBounds(new Rectangle(106, 466, 90, 16));
        _MechWeightLabel.setText("Mech Weight");
        _MechBVLabel = new JLabel();
        _MechBVLabel.setBounds(new Rectangle(16, 466, 75, 16));
        _MechBVLabel.setText("Mech BV");
        _PersonnelListLabel = new JLabel();
        _PersonnelListLabel.setBounds(new Rectangle(14, 316, 77, 16));
        _PersonnelListLabel.setText("Personnel");
        _MechListLabel = new JLabel();
        _MechListLabel.setBounds(new Rectangle(15, 194, 61, 16));
        _MechListLabel.setText("Mechs");
        _UnitWeightLabel = new JLabel();
        _UnitWeightLabel.setBounds(new Rectangle(435, 151, 76, 16));
        _UnitWeightLabel.setText("Unit Weight");
        _UnitNameLabel = new JLabel();
        _UnitNameLabel.setBounds(new Rectangle(15, 152, 76, 16));
        _UnitNameLabel.setText("Unit Name");
        this.setLayout(null);
        this.setSize(new Dimension(623, 557));
        this.add(get_PlayerPanel(), null);
        this.add(_UnitNameLabel, null);
        this.add(get_UnitNameTextField(), null);
        this.add(_UnitWeightLabel, null);
        this.add(get_UnitWeightComboBox(), null);
        this.add(_MechListLabel, null);
        this.add(get_MechList(), null);
        this.add(_PersonnelListLabel, null);
        this.add(get_PersonnelList(), null);
        this.add(_MechBVLabel, null);
        this.add(_MechWeightLabel, null);
        this.add(_AnnualSalaryLabel, null);
        this.add(_BiWeeklySalaryLabel, null);
        this.add(get_MechBVTextField(), null);
        this.add(get_MechWeightTextField(), null);
        this.add(get_AnnualSalaryTextField(), null);
        this.add(get_BiWeeklySalaryTextField(), null);
        this.add(get_GenerateButton(), null);
        this.add(get_SaveButton(), null);
			
	}

	/**
	 * This method initializes _PlayerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel get_PlayerPanel()
	{
		if (_PlayerPanel == null)
		{
			_PlayerEmailAddressLabel = new JLabel();
			_PlayerEmailAddressLabel.setBounds(new Rectangle(14, 67, 93, 16));
			_PlayerEmailAddressLabel.setText("Email Address");
			_PlayerNameLabel = new JLabel();
			_PlayerNameLabel.setBounds(new Rectangle(15, 30, 38, 16));
			_PlayerNameLabel.setText("Name");
			_PlayerPanel = new JPanel();
			_PlayerPanel.setLayout(null);
			_PlayerPanel.setBounds(new Rectangle(15, 15, 589, 123));
			_PlayerPanel.setBorder(BorderFactory.createTitledBorder(null, "Player", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			_PlayerPanel.add(_PlayerNameLabel, null);
			_PlayerPanel.add(get_PlayerNameTextField(), null);
			_PlayerPanel.add(_PlayerEmailAddressLabel, null);
			_PlayerPanel.add(get_PlayerEmailAddressTextField(), null);
		}
		return _PlayerPanel;
	}

	/**
	 * This method initializes _PlayerNameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField get_PlayerNameTextField()
	{
		if (_PlayerNameTextField == null)
		{
			_PlayerNameTextField = new JTextField();
			_PlayerNameTextField.setBounds(new Rectangle(15, 45, 557, 20));
		}
		return _PlayerNameTextField;
	}

	/**
	 * This method initializes _PLayerEmailAddressTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField get_PlayerEmailAddressTextField()
	{
		if (_PlayerEmailAddressTextField == null)
		{
			_PlayerEmailAddressTextField = new JTextField();
			_PlayerEmailAddressTextField.setBounds(new Rectangle(14, 84, 558, 20));
		}
		return _PlayerEmailAddressTextField;
	}

	/**
	 * This method initializes _UnitNameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField get_UnitNameTextField()
	{
		if (_UnitNameTextField == null)
		{
			_UnitNameTextField = new JTextField();
			_UnitNameTextField.setBounds(new Rectangle(17, 167, 404, 20));
		}
		return _UnitNameTextField;
	}

	/**
	 * This method initializes _UnitWeightComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox get_UnitWeightComboBox()
	{
		if (_UnitWeightComboBox == null)
		{
			_UnitWeightComboBox = new JComboBox(_UnitWeights);
			_UnitWeightComboBox.setBounds(new Rectangle(436, 167, 165, 23));
		}
		return _UnitWeightComboBox;
	}

	/**
	 * This method initializes _MechList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JScrollPane get_MechList()
	{
		if (_MechList == null)
		{
			_MechListModel = new DefaultListModel();
			_MechList = new JScrollPane(new JList(_MechListModel));
			_MechList.setBounds(new Rectangle(16, 211, 585, 90));
		}
		return _MechList;
	}

	/**
	 * This method initializes _PersonnelList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JScrollPane get_PersonnelList()
	{
		if (_PersonnelList == null)
		{
			_PersonnelListModel = new DefaultListModel();
			_PersonnelList = new JScrollPane(new JList(_PersonnelListModel));
			_PersonnelList.setBounds(new Rectangle(15, 330, 586, 121));
		}
		return _PersonnelList;
	}

	/**
	 * This method initializes _MechBVTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField get_MechBVTextField()
	{
		if (_MechBVTextField == null)
		{
			_MechBVTextField = new JTextField();
			_MechBVTextField.setBounds(new Rectangle(15, 480, 76, 20));
			_MechBVTextField.setEditable(false);
		}
		return _MechBVTextField;
	}

	/**
	 * This method initializes _MechWeightTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField get_MechWeightTextField()
	{
		if (_MechWeightTextField == null)
		{
			_MechWeightTextField = new JTextField();
			_MechWeightTextField.setBounds(new Rectangle(106, 480, 90, 20));
			_MechWeightTextField.setEditable(false);
		}
		return _MechWeightTextField;
	}

	/**
	 * This method initializes _AnnualSalaryTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField get_AnnualSalaryTextField()
	{
		if (_AnnualSalaryTextField == null)
		{
			_AnnualSalaryTextField = new JTextField();
			_AnnualSalaryTextField.setBounds(new Rectangle(210, 480, 91, 20));
			_AnnualSalaryTextField.setEditable(false);
		}
		return _AnnualSalaryTextField;
	}

	/**
	 * This method initializes _BiWeeklySalaryTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField get_BiWeeklySalaryTextField()
	{
		if (_BiWeeklySalaryTextField == null)
		{
			_BiWeeklySalaryTextField = new JTextField();
			_BiWeeklySalaryTextField.setBounds(new Rectangle(316, 480, 105, 20));
			_BiWeeklySalaryTextField.setEditable(false);
		}
		return _BiWeeklySalaryTextField;
	}

	/**
	 * This method initializes _GenerateButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton get_GenerateButton()
	{
		if (_GenerateButton == null)
		{
			_GenerateButton = new JButton();
			_GenerateButton.setBounds(new Rectangle(14, 510, 107, 31));
			_GenerateButton.setText("Generate");
			_GenerateButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
		            _MechListModel.removeAllElements();
		            _PersonnelListModel.removeAllElements();

		            _Player.setName(_PlayerNameTextField.getText());
		            _Player.setEmailAddress(_PlayerEmailAddressTextField.getText());

		            try
					{
						_GeneratedUnit = _UnitManager.GenerateUnit(_Player, _UnitNameTextField.getText(), _UnitWeightComboBox.getSelectedItem().toString(),Rating.REGULAR);
					} catch (Exception e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if (_GeneratedUnit != null)
					{
			            int totalBV = 0;
			            int totalWeight = 0;
			            for (Battlemech mech : _GeneratedUnit.getBattlemechs())
			            {
			                totalBV += mech.getBV();
			                totalWeight += mech.getWeight();
	
			                _MechListModel.addElement(mech.getDesignVariant() + " " + mech.getDesignName());
			            }
			            int annualSalaries = 0;
			            for (Personnel p : _GeneratedUnit.getPersonnel())
			            {
			                annualSalaries += (int)((p.getJobType().GetBaseMonthlySalary() * 12) * p.getRating().getSalaryMultiplier());
			                _PersonnelListModel.addElement(p.getSummary());
			            }
			            _MechBVTextField.setText(Integer.toString(totalBV));
			            _MechWeightTextField.setText(Integer.toString(totalWeight));
	
			            _AnnualSalaryTextField.setText(Integer.toString(annualSalaries));
			            _BiWeeklySalaryTextField.setText(Integer.toString(annualSalaries / 26));
					}
				}
			});
		}
		return _GenerateButton;
	}

	/**
	 * This method initializes _SaveButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton get_SaveButton()
	{
		if (_SaveButton == null)
		{
			_SaveButton = new JButton();
			_SaveButton.setBounds(new Rectangle(134, 510, 107, 31));
			_SaveButton.setText("Save");
			_SaveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
		            try
					{
						_UnitManager.saveUnit(_GeneratedUnit);

			            Vector<RandomName> names = new Vector<RandomName>();
			            for (Personnel p : _GeneratedUnit.getPersonnel())
			                names.add(new RandomName(p.getName(), p.getSurname()));
	
			            _UnitManager.purgeRandomNames(names);
	
			            _MechListModel.removeAllElements();
			            _PersonnelListModel.removeAllElements();
	
			            _PlayerNameTextField.setText("");
			            _PlayerEmailAddressTextField.setText("");
	
			            _UnitNameTextField.setText("");
	
			            _MechBVTextField.setText("");
			            _MechWeightTextField.setText("");
	
			            _AnnualSalaryTextField.setText("");
			            _BiWeeklySalaryTextField.setText("");
	
			            _UnitWeightComboBox.setSelectedIndex(2);					
					} catch (Exception e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}
		return _SaveButton;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
