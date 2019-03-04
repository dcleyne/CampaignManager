package bt.ui.panels;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import bt.elements.unit.Player;
import bt.elements.unit.Unit;
import bt.managers.MiniatureCollectionManager;
import bt.managers.UnitManager;
import bt.ui.models.MechDesignListModel;

public class GenerateNewUnitPanel extends JPanel implements ItemListener
{
	private static final long serialVersionUID = 1L;
	
	
    @SuppressWarnings("unused")
	private Unit _GeneratedUnit = new Unit();
    @SuppressWarnings("unused")
	private Player _Player = new Player();
	private UnitManager _UnitManager = UnitManager.getInstance();
	private MiniatureCollectionManager _MiniatureCollectionManager = MiniatureCollectionManager.getInstance();
    private JTextField _PlayerNameTextField;
    private JTextField _PlayerEmailAddressTextField;
    private JTextField _UnitNameTextField;
    
    private JComboBox<String> _EraComboBox;
    private JComboBox<String> _FactionComboBox;
    private JComboBox<String> _CollectionComboBox;
    
    private JList<String> _DesignList;
    private MechDesignListModel _DesignModel;
    private JList<String> _SelectedDesignList;

	/**
	 * This method initializes 
	 * 
	 */
	public GenerateNewUnitPanel() {
		super();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel _PlayerPanel = new JPanel();
		_PlayerPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Player", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_PlayerPanel = new GridBagConstraints();
		gbc_PlayerPanel.gridwidth = 3;
		gbc_PlayerPanel.fill = GridBagConstraints.BOTH;
		gbc_PlayerPanel.gridx = 0;
		gbc_PlayerPanel.gridy = 0;
		add(_PlayerPanel, gbc_PlayerPanel);
		GridBagLayout gbl_PlayerPanel = new GridBagLayout();
		gbl_PlayerPanel.columnWidths = new int[]{0, 0, 0};
		gbl_PlayerPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_PlayerPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		_PlayerPanel.setLayout(gbl_PlayerPanel);
		
		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		_PlayerPanel.add(lblName, gbc_lblName);
		
		_PlayerNameTextField = new JTextField();
		GridBagConstraints gbc_PlayerNameTextField = new GridBagConstraints();
		gbc_PlayerNameTextField.insets = new Insets(0, 0, 5, 0);
		gbc_PlayerNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_PlayerNameTextField.gridx = 1;
		gbc_PlayerNameTextField.gridy = 0;
		_PlayerPanel.add(_PlayerNameTextField, gbc_PlayerNameTextField);
		_PlayerNameTextField.setColumns(10);
		
		JLabel lblEmailAddress = new JLabel("EMail Address");
		GridBagConstraints gbc_lblEmailAddress = new GridBagConstraints();
		gbc_lblEmailAddress.anchor = GridBagConstraints.EAST;
		gbc_lblEmailAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmailAddress.gridx = 0;
		gbc_lblEmailAddress.gridy = 1;
		_PlayerPanel.add(lblEmailAddress, gbc_lblEmailAddress);
		
		_PlayerEmailAddressTextField = new JTextField();
		GridBagConstraints gbc_PlayerEmailAddressTextField = new GridBagConstraints();
		gbc_PlayerEmailAddressTextField.insets = new Insets(0, 0, 5, 0);
		gbc_PlayerEmailAddressTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_PlayerEmailAddressTextField.gridx = 1;
		gbc_PlayerEmailAddressTextField.gridy = 1;
		_PlayerPanel.add(_PlayerEmailAddressTextField, gbc_PlayerEmailAddressTextField);
		_PlayerEmailAddressTextField.setColumns(10);
		
		JLabel lblEra = new JLabel("Era");
		GridBagConstraints gbc_lblEra = new GridBagConstraints();
		gbc_lblEra.insets = new Insets(0, 0, 5, 5);
		gbc_lblEra.anchor = GridBagConstraints.EAST;
		gbc_lblEra.gridx = 0;
		gbc_lblEra.gridy = 2;
		_PlayerPanel.add(lblEra, gbc_lblEra);
		
		_EraComboBox = new JComboBox<String>();
		GridBagConstraints gbc_EraComboBox = new GridBagConstraints();
		gbc_EraComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_EraComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_EraComboBox.gridx = 1;
		gbc_EraComboBox.gridy = 2;
		_PlayerPanel.add(_EraComboBox, gbc_EraComboBox);
		
		JLabel label = new JLabel("Faction");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 3;
		_PlayerPanel.add(label, gbc_label);
		
		_FactionComboBox = new JComboBox<String>();
		_FactionComboBox.setAlignmentX(1.0f);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 3;
		_PlayerPanel.add(_FactionComboBox, gbc_comboBox);
		
		JLabel label_1 = new JLabel("Collection");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 4;
		_PlayerPanel.add(label_1, gbc_label_1);
		
		_CollectionComboBox = new JComboBox<String>();
		_CollectionComboBox.setAlignmentX(1.0f);
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.gridx = 1;
		gbc_comboBox_1.gridy = 4;
		_PlayerPanel.add(_CollectionComboBox, gbc_comboBox_1);
		_EraComboBox.addItemListener(this);
		
		JPanel _AvailableDesignsPanel = new JPanel();
		_AvailableDesignsPanel.setBorder(new TitledBorder(null, "Available Designs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_AvailableDesignsPanel = new GridBagConstraints();
		gbc_AvailableDesignsPanel.weighty = 20.0;
		gbc_AvailableDesignsPanel.weightx = 10.0;
		gbc_AvailableDesignsPanel.fill = GridBagConstraints.BOTH;
		gbc_AvailableDesignsPanel.gridx = 0;
		gbc_AvailableDesignsPanel.gridy = 1;
		add(_AvailableDesignsPanel, gbc_AvailableDesignsPanel);
		GridBagLayout gbl_AvailableDesignsPanel = new GridBagLayout();
		gbl_AvailableDesignsPanel.columnWidths = new int[]{0, 0};
		gbl_AvailableDesignsPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_AvailableDesignsPanel.rowWeights = new double[]{1.0};
		_AvailableDesignsPanel.setLayout(gbl_AvailableDesignsPanel);
		
		_DesignList = new JList<String>();
		_DesignList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagConstraints gbc_DesignList = new GridBagConstraints();
		gbc_DesignList.weighty = 100.0;
		gbc_DesignList.fill = GridBagConstraints.BOTH;
		gbc_DesignList.gridx = 0;
		gbc_DesignList.gridy = 0;
		_AvailableDesignsPanel.add(_DesignList, gbc_DesignList);
		
		JPanel _DesignButtonPanel = new JPanel();
		GridBagConstraints gbc_DesignButtonPanel = new GridBagConstraints();
		gbc_DesignButtonPanel.gridx = 1;
		gbc_DesignButtonPanel.gridy = 1;
		add(_DesignButtonPanel, gbc_DesignButtonPanel);
		_DesignButtonPanel.setLayout(new BoxLayout(_DesignButtonPanel, BoxLayout.PAGE_AXIS));
		
		Component verticalGlue = Box.createVerticalGlue();
		_DesignButtonPanel.add(verticalGlue);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
		_DesignButtonPanel.add(btnAdd);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		_DesignButtonPanel.add(verticalStrut);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.setAlignmentX(Component.CENTER_ALIGNMENT);
		_DesignButtonPanel.add(btnRemove);
		
		Component verticalGlue_1 = Box.createVerticalGlue();
		_DesignButtonPanel.add(verticalGlue_1);
		
		JPanel _UnitDetails = new JPanel();
		_UnitDetails.setBorder(new TitledBorder(null, "Unit Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_UnitDetails = new GridBagConstraints();
		gbc_UnitDetails.weighty = 20.0;
		gbc_UnitDetails.weightx = 10.0;
		gbc_UnitDetails.fill = GridBagConstraints.BOTH;
		gbc_UnitDetails.gridx = 2;
		gbc_UnitDetails.gridy = 1;
		add(_UnitDetails, gbc_UnitDetails);
		GridBagLayout gbl_UnitDetails = new GridBagLayout();
		gbl_UnitDetails.columnWeights = new double[]{1.0};
		gbl_UnitDetails.rowWeights = new double[]{0.0, 0.0, 1.0};
		_UnitDetails.setLayout(gbl_UnitDetails);
		
		JPanel _UnitNamePanel = new JPanel();
		GridBagConstraints gbc_UnitNamePanel = new GridBagConstraints();
		gbc_UnitNamePanel.weighty = 1.0;
		gbc_UnitNamePanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_UnitNamePanel.gridx = 0;
		gbc_UnitNamePanel.gridy = 0;
		_UnitDetails.add(_UnitNamePanel, gbc_UnitNamePanel);
		_UnitNamePanel.setLayout(new BoxLayout(_UnitNamePanel, BoxLayout.X_AXIS));
		
		JLabel lblName_1 = new JLabel("Name");
		_UnitNamePanel.add(lblName_1);
		
		_UnitNameTextField = new JTextField();
		_UnitNamePanel.add(_UnitNameTextField);
		_UnitNameTextField.setColumns(10);
		
		_SelectedDesignList = new JList<String>();
		_SelectedDesignList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagConstraints gbc_SelectedDesignList = new GridBagConstraints();
		gbc_SelectedDesignList.fill = GridBagConstraints.BOTH;
		gbc_SelectedDesignList.insets = new Insets(0, 0, 5, 0);
		gbc_SelectedDesignList.weighty = 100.0;
		gbc_SelectedDesignList.gridx = 0;
		gbc_SelectedDesignList.gridy = 1;
		_UnitDetails.add(_SelectedDesignList, gbc_SelectedDesignList);
		
		JPanel _UnitSummaryPanel = new JPanel();
		_UnitSummaryPanel.setBorder(new TitledBorder(null, "Summary", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_UnitSummaryPanel = new GridBagConstraints();
		gbc_UnitSummaryPanel.fill = GridBagConstraints.BOTH;
		gbc_UnitSummaryPanel.gridx = 0;
		gbc_UnitSummaryPanel.gridy = 2;
		_UnitDetails.add(_UnitSummaryPanel, gbc_UnitSummaryPanel);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() 
	{
		for (String era: _UnitManager.getAvailableEras())
			_EraComboBox.addItem(era);
		
		for (String collectionName: _MiniatureCollectionManager.getCollectionNames())
			_CollectionComboBox.addItem(collectionName);
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				_EraComboBox.setSelectedIndex(0);
			}
		}
		);
	}

	@Override
	public void itemStateChanged(ItemEvent e)
	{
		if (e.getSource() == _EraComboBox)
		{
			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				String selectedEra = (String)_EraComboBox.getSelectedItem();
				selectFactionsAvailableForEra(selectedEra);
			}
		}
	}
	
	private void selectFactionsAvailableForEra(String era)
	{
		_FactionComboBox.removeAllItems();
		for (String factionName: UnitManager.getInstance().getAvailableFactionsInEra(era))
		{
			_FactionComboBox.addItem(factionName);
		}
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
