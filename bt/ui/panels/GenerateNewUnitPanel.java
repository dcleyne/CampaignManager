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
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JButton;

import bt.elements.Battlemech;
import bt.elements.Era;
import bt.elements.Faction;
import bt.elements.collection.UnlimitedCollection;
import bt.elements.personnel.Personnel;
import bt.elements.personnel.Rating;
import bt.elements.unit.Player;
import bt.elements.unit.QualityRating;
import bt.elements.unit.RandomName;
import bt.elements.unit.TechRating;
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
	private JComboBox<String> _UnitWeightComboBox = null;
	private JLabel _UnitRatingLabel = null;
	private JComboBox<Rating> _UnitRatingComboBox = null;
	private JLabel _UnitQualityRatingLabel = null;
	private JComboBox<QualityRating> _UnitQualityRatingComboBox = null;
	private JLabel _UnitTechRatingLabel = null;
	private JComboBox<TechRating> _UnitTechRatingComboBox = null;
	private JLabel _MechListLabel = null;
	private JScrollPane _MechList = null;
	private DefaultListModel<String> _MechListModel = null;
	private JLabel _PersonnelListLabel = null;
	private JScrollPane _PersonnelList = null;
	private DefaultListModel<String> _PersonnelListModel = null;
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
        _BiWeeklySalaryLabel.setBounds(new Rectangle(316, 544, 105, 16));
        _BiWeeklySalaryLabel.setText("BiWeekly Salary");
        _AnnualSalaryLabel = new JLabel();
        _AnnualSalaryLabel.setBounds(new Rectangle(210, 544, 91, 16));
        _AnnualSalaryLabel.setText("Annual Salary");
        _MechWeightLabel = new JLabel();
        _MechWeightLabel.setBounds(new Rectangle(106, 544, 90, 16));
        _MechWeightLabel.setText("Mech Weight");
        _MechBVLabel = new JLabel();
        _MechBVLabel.setBounds(new Rectangle(16, 544, 75, 16));
        _MechBVLabel.setText("Mech BV");
        _PersonnelListLabel = new JLabel();
        _PersonnelListLabel.setBounds(new Rectangle(15, 385, 77, 16));
        _PersonnelListLabel.setText("Personnel");
        _MechListLabel = new JLabel();
        _MechListLabel.setBounds(new Rectangle(15, 257, 61, 16));
        _MechListLabel.setText("Mechs");
        _UnitWeightLabel = new JLabel();
        _UnitWeightLabel.setBounds(new Rectangle(15, 196, 76, 16));
        _UnitWeightLabel.setText("Unit Weight");
        _UnitRatingLabel = new JLabel();
        _UnitRatingLabel.setBounds(new Rectangle(168, 196, 76, 16));
        _UnitRatingLabel.setText("Unit Rating");
        _UnitQualityRatingLabel = new JLabel();
        _UnitQualityRatingLabel.setBounds(new Rectangle(326, 196, 123, 16));
        _UnitQualityRatingLabel.setText("Unit Quality Rating");
        _UnitTechRatingLabel = new JLabel();
        _UnitTechRatingLabel.setBounds(new Rectangle(459, 196, 133, 16));
        _UnitTechRatingLabel.setText("Unit Tech Rating");
        _UnitNameLabel = new JLabel();
        _UnitNameLabel.setBounds(new Rectangle(15, 152, 76, 16));
        _UnitNameLabel.setText("Unit Name");
        this.setLayout(null);
        this.setSize(new Dimension(623, 644));
        this.add(get_PlayerPanel(), null);
        this.add(_UnitNameLabel, null);
        this.add(get_UnitNameTextField(), null);
        this.add(_UnitWeightLabel, null);
        this.add(get_UnitWeightComboBox(), null);
        this.add(_UnitRatingLabel, null);
        this.add(get_UnitRatingComboBox(), null);
        this.add(_UnitQualityRatingLabel, null);
        this.add(get_UnitQualityRatingComboBox(), null);
        this.add(_UnitTechRatingLabel, null);
        this.add(get_UnitTechRatingComboBox(), null);
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
        JList<String> list = new JList<String>(_PersonnelListModel);
        list.setBounds(15, 412, 584, 119);
        add(list);
        JList<String> list_1 = new JList<String>(_MechListModel);
        list_1.setBounds(15, 284, 583, 88);
        add(list_1);
			
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
			_UnitNameTextField.setBounds(new Rectangle(17, 167, 587, 20));
		}
		return _UnitNameTextField;
	}

	/**
	 * This method initializes _UnitWeightComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox<String> get_UnitWeightComboBox()
	{
		if (_UnitWeightComboBox == null)
		{
			_UnitWeightComboBox = new JComboBox<String>(_UnitWeights);
			_UnitWeightComboBox.setBounds(new Rectangle(15, 223, 143, 23));
		}
		return _UnitWeightComboBox;
	}

	/**
	 * This method initializes _UnitRatingComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox<Rating> get_UnitRatingComboBox()
	{
		if (_UnitRatingComboBox == null)
		{
			_UnitRatingComboBox = new JComboBox<Rating>(Rating.values());
			_UnitRatingComboBox.setBounds(new Rectangle(168, 223, 133, 23));
		}
		return _UnitRatingComboBox;
	}

	/**
	 * This method initializes _UnitQualityRatingComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox<QualityRating> get_UnitQualityRatingComboBox()
	{
		if (_UnitQualityRatingComboBox == null)
		{
			_UnitQualityRatingComboBox = new JComboBox<QualityRating>(QualityRating.values());
			_UnitQualityRatingComboBox.setBounds(new Rectangle(326, 223, 123, 23));
		}
		return _UnitQualityRatingComboBox;
	}
	
	/**
	 * This method initializes _UnitTechRatingComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox<TechRating> get_UnitTechRatingComboBox()
	{
		if (_UnitTechRatingComboBox == null)
		{
			_UnitTechRatingComboBox = new JComboBox<TechRating>(TechRating.values());
			_UnitTechRatingComboBox.setBounds(new Rectangle(459, 223, 141, 23));
		}
		return _UnitTechRatingComboBox;
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
			_MechListModel = new DefaultListModel<String>();
			_MechList = new JScrollPane();
			_MechList.setBounds(new Rectangle(15, 284, 585, 90));
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
			_PersonnelListModel = new DefaultListModel<String>();
			_PersonnelList = new JScrollPane();
			_PersonnelList.setBounds(new Rectangle(15, 412, 586, 121));
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
			_MechBVTextField.setBounds(new Rectangle(15, 571, 76, 20));
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
			_MechWeightTextField.setBounds(new Rectangle(106, 571, 90, 20));
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
			_AnnualSalaryTextField.setBounds(new Rectangle(210, 571, 91, 20));
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
			_BiWeeklySalaryTextField.setBounds(new Rectangle(316, 571, 105, 20));
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
			_GenerateButton.setBounds(new Rectangle(15, 602, 107, 31));
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
		            	
		            	// TODO fix the starting bank balance
						_GeneratedUnit = _UnitManager.generateUnit(Era.LATE_SUCCESSION_WAR_RENAISSANCE, Faction.MERCENARY, _Player, _UnitNameTextField.getText(), 
								_UnitWeightComboBox.getSelectedItem().toString(),(Rating)_UnitRatingComboBox.getSelectedItem(), 
								(QualityRating)_UnitQualityRatingComboBox.getSelectedItem(), (TechRating)_UnitTechRatingComboBox.getSelectedItem(),0,new UnlimitedCollection());
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
			_SaveButton.setBounds(new Rectangle(132, 602, 107, 31));
			_SaveButton.setText("Save");
			_SaveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
		            try
					{
						_UnitManager.saveUnit(_GeneratedUnit);

			            ArrayList<RandomName> names = new ArrayList<RandomName>();
			            for (Personnel p : _GeneratedUnit.getPersonnel())
			                names.add(RandomName.splitName(p.getName()));
	
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
