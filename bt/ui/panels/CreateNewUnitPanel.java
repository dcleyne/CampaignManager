package bt.ui.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import bt.elements.Era;
import bt.elements.Faction;
import bt.elements.collection.MiniatureCollection;
import bt.elements.design.BattlemechDesign;
import bt.elements.personnel.Rating;
import bt.elements.unit.Player;
import bt.elements.unit.QualityRating;
import bt.elements.unit.TechRating;
import bt.elements.unit.Unit;
import bt.managers.DesignManager;
import bt.managers.MiniatureCollectionManager;
import bt.managers.UnitManager;
import bt.ui.models.MechDesignTableModel;
import bt.ui.models.TableSorter;
import bt.util.ExceptionUtil;
import bt.util.SwingHelper;

public class CreateNewUnitPanel extends JPanel implements ItemListener, MouseListener, ActionListener
{
	private static final String SAVE_UNIT = "Save Unit";
	private static final String OPEN_IN_PDF = "Open in PDF";
	private static final String REMOVE = "Remove";
	private static final String ADD = "Add";
	private static final String GENERATE_UNIT = "Generate Unit";
	private static final long serialVersionUID = 1L;	
	
	private Unit _CreatedUnit = new Unit();
	private Player _Player = new Player();
	private UnitManager _UnitManager = UnitManager.getInstance();
	private DesignManager _DesignManager = DesignManager.getInstance();
	private MiniatureCollectionManager _MiniatureCollectionManager = MiniatureCollectionManager.getInstance();
    private JTextField _PlayerNameTextField;
    private JTextField _PlayerEmailAddressTextField;
    private JTextField _UnitNameTextField;
    
    private JComboBox<String> _EraComboBox;
    private JComboBox<String> _FactionComboBox;
    private JComboBox<String> _CollectionComboBox;
    
    private JTable _DesignTable = new JTable();
    private TableSorter _Sorter = new TableSorter();
    private MechDesignTableModel _DesignModel = new MechDesignTableModel();
    private DefaultListModel<String> _SelectedDesignListModel = new DefaultListModel<String>();
    private JList<String> _SelectedDesignList;
    private JTextField _UnitWeightTextField;
    private JTextField _UnitBVTextField;
    private JTextField _UnitCostTextField;
    private JTextField _UnitCountTextField;
    private JTextArea _UnitDetailsTextArea;
    
    private JComboBox<QualityRating> _UnitQualityComboBox;
    private JComboBox<Rating> _UnitRatingComboBox;
    private JComboBox<TechRating> _UnitTechRatingComboBox;

	/**
	 * This method initializes 
	 * 
	 */
	public CreateNewUnitPanel() {
		super();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel _PlayerPanel = new JPanel();
		_PlayerPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Player", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_PlayerPanel = new GridBagConstraints();
		gbc_PlayerPanel.insets = new Insets(0, 0, 5, 5);
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
		_EraComboBox.addItemListener(this);
		
		JLabel label = new JLabel("Faction");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 3;
		_PlayerPanel.add(label, gbc_label);
		
		_FactionComboBox = new JComboBox<String>();
		_FactionComboBox.setAlignmentX(1.0f);
		GridBagConstraints gbc_FactionComboBox = new GridBagConstraints();
		gbc_FactionComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_FactionComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_FactionComboBox.gridx = 1;
		gbc_FactionComboBox.gridy = 3;
		_PlayerPanel.add(_FactionComboBox, gbc_FactionComboBox);
		_FactionComboBox.addItemListener(this);

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
		_CollectionComboBox.addItemListener(this);
		
		JPanel _GenerateUnitPanel = new JPanel();
		GridBagConstraints gbc_GenerateUnitPanel = new GridBagConstraints();
		gbc_GenerateUnitPanel.gridwidth = 3;
		gbc_GenerateUnitPanel.insets = new Insets(0, 0, 5, 5);
		gbc_GenerateUnitPanel.fill = GridBagConstraints.BOTH;
		gbc_GenerateUnitPanel.gridx = 0;
		gbc_GenerateUnitPanel.gridy = 2;
		add(_GenerateUnitPanel, gbc_GenerateUnitPanel);
		_GenerateUnitPanel.setLayout(new BoxLayout(_GenerateUnitPanel, BoxLayout.X_AXIS));
		
		Component horizontalGlue = Box.createHorizontalGlue();
		_GenerateUnitPanel.add(horizontalGlue);
		
		JButton _GenerateUnitButton = new JButton(GENERATE_UNIT);
		_GenerateUnitPanel.add(_GenerateUnitButton);
		_GenerateUnitButton.addActionListener(this);

		JPanel _AvailableDesignsPanel = new JPanel();
		_AvailableDesignsPanel.setBorder(new TitledBorder(null, "Available Designs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_AvailableDesignsPanel = new GridBagConstraints();
		gbc_AvailableDesignsPanel.insets = new Insets(0, 0, 5, 5);
		gbc_AvailableDesignsPanel.weighty = 20.0;
		gbc_AvailableDesignsPanel.weightx = 30.0;
		gbc_AvailableDesignsPanel.fill = GridBagConstraints.BOTH;
		gbc_AvailableDesignsPanel.gridx = 0;
		gbc_AvailableDesignsPanel.gridy = 1;
		add(_AvailableDesignsPanel, gbc_AvailableDesignsPanel);
		GridBagLayout gbl_AvailableDesignsPanel = new GridBagLayout();
		gbl_AvailableDesignsPanel.columnWidths = new int[]{0, 0};
		gbl_AvailableDesignsPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_AvailableDesignsPanel.rowWeights = new double[]{1.0};
		_AvailableDesignsPanel.setLayout(gbl_AvailableDesignsPanel);
		
		_Sorter.setTableModel(_DesignModel);
		_DesignTable.setModel(_Sorter);
		_Sorter.setTableHeader(_DesignTable.getTableHeader());
		_DesignTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_DesignTable.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		_DesignTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		_DesignTable.addMouseListener(this);
        JScrollPane sp = new JScrollPane(_DesignTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		GridBagConstraints gbc_DesignList = new GridBagConstraints();
		gbc_DesignList.weighty = 100.0;
		gbc_DesignList.fill = GridBagConstraints.BOTH;
		gbc_DesignList.gridx = 0;
		gbc_DesignList.gridy = 0;
		_AvailableDesignsPanel.add(sp, gbc_DesignList);
		
		JPanel _DesignButtonPanel = new JPanel();
		GridBagConstraints gbc_DesignButtonPanel = new GridBagConstraints();
		gbc_DesignButtonPanel.insets = new Insets(0, 0, 5, 5);
		gbc_DesignButtonPanel.gridx = 1;
		gbc_DesignButtonPanel.gridy = 1;
		add(_DesignButtonPanel, gbc_DesignButtonPanel);
		_DesignButtonPanel.setLayout(new BoxLayout(_DesignButtonPanel, BoxLayout.PAGE_AXIS));
		
		Component verticalGlue = Box.createVerticalGlue();
		_DesignButtonPanel.add(verticalGlue);
		
		JButton btnAdd = new JButton(ADD);
		btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
		_DesignButtonPanel.add(btnAdd);
		btnAdd.addActionListener(this);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		_DesignButtonPanel.add(verticalStrut);
		
		JButton btnRemove = new JButton(REMOVE);
		btnRemove.setAlignmentX(Component.CENTER_ALIGNMENT);
		_DesignButtonPanel.add(btnRemove);
		btnRemove.addActionListener(this);
		
		Component verticalGlue_1 = Box.createVerticalGlue();
		_DesignButtonPanel.add(verticalGlue_1);
		
		JPanel _UnitDetails = new JPanel();
		_UnitDetails.setBorder(new TitledBorder(null, "Unit Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_UnitDetails = new GridBagConstraints();
		gbc_UnitDetails.insets = new Insets(0, 0, 5, 5);
		gbc_UnitDetails.weighty = 20.0;
		gbc_UnitDetails.weightx = 10.0;
		gbc_UnitDetails.fill = GridBagConstraints.BOTH;
		gbc_UnitDetails.gridx = 2;
		gbc_UnitDetails.gridy = 1;
		add(_UnitDetails, gbc_UnitDetails);
		GridBagLayout gbl_UnitDetails = new GridBagLayout();
		gbl_UnitDetails.columnWeights = new double[]{1.0};
		gbl_UnitDetails.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 1.0};
		_UnitDetails.setLayout(gbl_UnitDetails);
		
		JPanel _UnitNamePanel = new JPanel();
		GridBagConstraints gbc_UnitNamePanel = new GridBagConstraints();
		gbc_UnitNamePanel.insets = new Insets(0, 0, 5, 0);
		gbc_UnitNamePanel.weighty = 1.0;
		gbc_UnitNamePanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_UnitNamePanel.gridx = 0;
		gbc_UnitNamePanel.gridy = 0;
		_UnitDetails.add(_UnitNamePanel, gbc_UnitNamePanel);
		_UnitNamePanel.setLayout(new BoxLayout(_UnitNamePanel, BoxLayout.X_AXIS));
		
		JLabel lblName_1 = new JLabel("Name");
		_UnitNamePanel.add(lblName_1);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(5);
		_UnitNamePanel.add(horizontalStrut_1);
		
		_UnitNameTextField = new JTextField("New Unit");
		_UnitNamePanel.add(_UnitNameTextField);
		_UnitNameTextField.setColumns(10);
		
		JPanel _UnitQualityPanel = new JPanel();
		GridBagConstraints gbc_UnitQualityPanel = new GridBagConstraints();
		gbc_UnitQualityPanel.insets = new Insets(0, 0, 5, 0);
		gbc_UnitQualityPanel.fill = GridBagConstraints.BOTH;
		gbc_UnitQualityPanel.gridx = 0;
		gbc_UnitQualityPanel.gridy = 1;
		_UnitDetails.add(_UnitQualityPanel, gbc_UnitQualityPanel);
		GridBagLayout gbl_UnitQualityPanel = new GridBagLayout();
		gbl_UnitQualityPanel.columnWidths = new int[]{0, 0, 0};
		gbl_UnitQualityPanel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_UnitQualityPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_UnitQualityPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		_UnitQualityPanel.setLayout(gbl_UnitQualityPanel);
		
		JLabel lblNewLabel = new JLabel("Rating");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		_UnitQualityPanel.add(lblNewLabel, gbc_lblNewLabel);
		
		_UnitRatingComboBox = new JComboBox<Rating>(Rating.values());
		GridBagConstraints gbc_UnitRatingComboBox = new GridBagConstraints();
		gbc_UnitRatingComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_UnitRatingComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_UnitRatingComboBox.gridx = 1;
		gbc_UnitRatingComboBox.gridy = 0;
		_UnitQualityPanel.add(_UnitRatingComboBox, gbc_UnitRatingComboBox);
		
		JLabel lblQuality = new JLabel("Quality");
		GridBagConstraints gbc_lblQuality = new GridBagConstraints();
		gbc_lblQuality.insets = new Insets(0, 0, 5, 5);
		gbc_lblQuality.anchor = GridBagConstraints.EAST;
		gbc_lblQuality.gridx = 0;
		gbc_lblQuality.gridy = 1;
		_UnitQualityPanel.add(lblQuality, gbc_lblQuality);
		
		_UnitQualityComboBox = new JComboBox<QualityRating>(QualityRating.values());
		GridBagConstraints gbc_UnitQualityComboBox = new GridBagConstraints();
		gbc_UnitQualityComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_UnitQualityComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_UnitQualityComboBox.gridx = 1;
		gbc_UnitQualityComboBox.gridy = 1;
		_UnitQualityPanel.add(_UnitQualityComboBox, gbc_UnitQualityComboBox);
		
		JLabel lblTechRating = new JLabel("Tech Rating");
		GridBagConstraints gbc_lblTechRating = new GridBagConstraints();
		gbc_lblTechRating.anchor = GridBagConstraints.EAST;
		gbc_lblTechRating.insets = new Insets(0, 0, 0, 5);
		gbc_lblTechRating.gridx = 0;
		gbc_lblTechRating.gridy = 2;
		_UnitQualityPanel.add(lblTechRating, gbc_lblTechRating);
		
		_UnitTechRatingComboBox = new JComboBox<TechRating>(TechRating.values());
		GridBagConstraints gbc_TechRatingComboBox = new GridBagConstraints();
		gbc_TechRatingComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_TechRatingComboBox.gridx = 1;
		gbc_TechRatingComboBox.gridy = 2;
		_UnitQualityPanel.add(_UnitTechRatingComboBox, gbc_TechRatingComboBox);
		
		_SelectedDesignList = new JList<String>();
		_SelectedDesignList.setModel(_SelectedDesignListModel);
		_SelectedDesignList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagConstraints gbc_SelectedDesignList = new GridBagConstraints();
		gbc_SelectedDesignList.fill = GridBagConstraints.BOTH;
		gbc_SelectedDesignList.insets = new Insets(0, 0, 5, 0);
		gbc_SelectedDesignList.weighty = 100.0;
		gbc_SelectedDesignList.gridx = 0;
		gbc_SelectedDesignList.gridy = 3;
		JScrollPane listScrollPane = new JScrollPane(_SelectedDesignList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		_UnitDetails.add(listScrollPane, gbc_SelectedDesignList);
		_SelectedDesignList.addMouseListener(this);
		
		JPanel _UnitSummaryPanel = new JPanel();
		_UnitSummaryPanel.setBorder(new TitledBorder(null, "Summary", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_UnitSummaryPanel = new GridBagConstraints();
		gbc_UnitSummaryPanel.fill = GridBagConstraints.BOTH;
		gbc_UnitSummaryPanel.gridx = 0;
		gbc_UnitSummaryPanel.gridy = 4;
		_UnitDetails.add(_UnitSummaryPanel, gbc_UnitSummaryPanel);
		GridBagLayout gbl_UnitSummaryPanel = new GridBagLayout();
		gbl_UnitSummaryPanel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_UnitSummaryPanel.rowHeights = new int[]{0, 0, 0};
		gbl_UnitSummaryPanel.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_UnitSummaryPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		_UnitSummaryPanel.setLayout(gbl_UnitSummaryPanel);
		
		JLabel lblCount = new JLabel("Count");
		GridBagConstraints gbc_lblCount = new GridBagConstraints();
		gbc_lblCount.insets = new Insets(0, 0, 5, 5);
		gbc_lblCount.gridx = 0;
		gbc_lblCount.gridy = 0;
		_UnitSummaryPanel.add(lblCount, gbc_lblCount);
		
		JLabel lblWeight = new JLabel("Weight");
		GridBagConstraints gbc_lblWeight = new GridBagConstraints();
		gbc_lblWeight.insets = new Insets(0, 0, 5, 5);
		gbc_lblWeight.gridx = 1;
		gbc_lblWeight.gridy = 0;
		_UnitSummaryPanel.add(lblWeight, gbc_lblWeight);
		
		JLabel lblBattleValue = new JLabel("Battle Value");
		GridBagConstraints gbc_lblBattleValue = new GridBagConstraints();
		gbc_lblBattleValue.insets = new Insets(0, 0, 5, 5);
		gbc_lblBattleValue.gridx = 2;
		gbc_lblBattleValue.gridy = 0;
		_UnitSummaryPanel.add(lblBattleValue, gbc_lblBattleValue);
		
		JLabel lblCost = new JLabel("Cost");
		GridBagConstraints gbc_lblCost = new GridBagConstraints();
		gbc_lblCost.insets = new Insets(0, 0, 5, 0);
		gbc_lblCost.gridx = 3;
		gbc_lblCost.gridy = 0;
		_UnitSummaryPanel.add(lblCost, gbc_lblCost);
		
		_UnitCountTextField = new JTextField();
		GridBagConstraints gbc_UnitCountTextField = new GridBagConstraints();
		gbc_UnitCountTextField.anchor = GridBagConstraints.NORTH;
		gbc_UnitCountTextField.insets = new Insets(0, 0, 0, 5);
		gbc_UnitCountTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_UnitCountTextField.gridx = 0;
		gbc_UnitCountTextField.gridy = 1;
		_UnitSummaryPanel.add(_UnitCountTextField, gbc_UnitCountTextField);
		_UnitCountTextField.setColumns(10);
		
		_UnitWeightTextField = new JTextField();
		GridBagConstraints gbc_UnitWeightTextField = new GridBagConstraints();
		gbc_UnitWeightTextField.anchor = GridBagConstraints.NORTH;
		gbc_UnitWeightTextField.insets = new Insets(0, 0, 0, 5);
		gbc_UnitWeightTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_UnitWeightTextField.gridx = 1;
		gbc_UnitWeightTextField.gridy = 1;
		_UnitSummaryPanel.add(_UnitWeightTextField, gbc_UnitWeightTextField);
		_UnitWeightTextField.setColumns(10);
		
		_UnitBVTextField = new JTextField();
		GridBagConstraints gbc_UnitBVTextField = new GridBagConstraints();
		gbc_UnitBVTextField.insets = new Insets(0, 0, 0, 5);
		gbc_UnitBVTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_UnitBVTextField.gridx = 2;
		gbc_UnitBVTextField.gridy = 1;
		_UnitSummaryPanel.add(_UnitBVTextField, gbc_UnitBVTextField);
		_UnitBVTextField.setColumns(10);
		
		_UnitCostTextField = new JTextField();
		GridBagConstraints gbc_UnitCostTextField = new GridBagConstraints();
		gbc_UnitCostTextField.anchor = GridBagConstraints.NORTH;
		gbc_UnitCostTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_UnitCostTextField.gridx = 3;
		gbc_UnitCostTextField.gridy = 1;
		_UnitSummaryPanel.add(_UnitCostTextField, gbc_UnitCostTextField);
		_UnitCostTextField.setColumns(10);
		
		JPanel _UnitDetailsPanel = new JPanel();
		_UnitDetailsPanel.setLayout(new BorderLayout());
		GridBagConstraints gbc_UnitDetailsPanel = new GridBagConstraints();
		gbc_UnitDetailsPanel.gridwidth = 3;
		gbc_UnitDetailsPanel.insets = new Insets(5, 5, 5, 5);
		gbc_UnitDetailsPanel.fill = GridBagConstraints.BOTH;
		gbc_UnitDetailsPanel.gridx = 0;
		gbc_UnitDetailsPanel.gridy = 3;
		gbc_UnitDetailsPanel.weighty = 20;
		add(_UnitDetailsPanel, gbc_UnitDetailsPanel);
		
		_UnitDetailsTextArea = new JTextArea();
		_UnitDetailsTextArea.setRows(10);
		_UnitDetailsTextArea.setColumns(80);
		_UnitDetailsPanel.add(new JScrollPane(_UnitDetailsTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		
		JPanel _SaveUnitPanel = new JPanel();
		GridBagConstraints gbc_SaveUnitPanel = new GridBagConstraints();
		gbc_SaveUnitPanel.gridwidth = 3;
		gbc_SaveUnitPanel.insets = new Insets(0, 0, 0, 5);
		gbc_SaveUnitPanel.fill = GridBagConstraints.BOTH;
		gbc_SaveUnitPanel.gridx = 0;
		gbc_SaveUnitPanel.gridy = 4;
		add(_SaveUnitPanel, gbc_SaveUnitPanel);
		_SaveUnitPanel.setLayout(new BoxLayout(_SaveUnitPanel, BoxLayout.X_AXIS));
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		_SaveUnitPanel.add(horizontalGlue_1);
		
		JButton _OpenInPdfButton = new JButton(OPEN_IN_PDF);
		_SaveUnitPanel.add(_OpenInPdfButton);
		_OpenInPdfButton.addActionListener(this);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		_SaveUnitPanel.add(horizontalStrut);
		
		JButton _SaveUnitButton = new JButton(SAVE_UNIT);
		_SaveUnitPanel.add(_SaveUnitButton);
		_SaveUnitButton.addActionListener(this);
		
		initialize();
	}
	
	public Unit getCreatedUnit()
	{
		return _CreatedUnit;
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
				_UnitRatingComboBox.setSelectedItem(Rating.REGULAR);
				_UnitQualityComboBox.setSelectedItem(QualityRating.D);
				_UnitTechRatingComboBox.setSelectedItem(TechRating.D);
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
				SwingHelper.resizeTableColumnWidth(_DesignTable);
				clearSelectedDesigns();
			}
		}

		if (e.getSource() == _FactionComboBox)
		{
			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				SwingUtilities.invokeLater(new Runnable()
				{					
					@Override
					public void run()
					{
						selectFaction();
						SwingHelper.resizeTableColumnWidth(_DesignTable);
					}
				});
			}
		}
		if (e.getSource() == _CollectionComboBox)
		{
			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				SwingUtilities.invokeLater(new Runnable()
				{					
					@Override
					public void run()
					{
						_DesignModel.setCollection((String)_CollectionComboBox.getSelectedItem());
						SwingHelper.resizeTableColumnWidth(_DesignTable);
						clearSelectedDesigns();
					}
				});
			}			
		}
	}
	
	private void clearSelectedDesigns()
	{
		_SelectedDesignListModel.clear();
		recalculateUnitValues();
		_UnitDetailsTextArea.setText("");
	}

	private void recalculateUnitValues()
	{
		SwingUtilities.invokeLater(new Runnable()
		{			
			@Override
			public void run()
			{
				int unitWeight = 0;
				int unitBV = 0;
				int unitCost = 0;
		
				for (int i = 0; i < _SelectedDesignListModel.getSize(); i++)
				{
					String designName = _SelectedDesignListModel.getElementAt(i);
					BattlemechDesign bd = _DesignManager.Design(designName);
					unitWeight += bd.getWeight();
					unitBV += bd.getBV();
					unitCost += bd.getCost();
				}
				
				_UnitCountTextField.setText(Integer.toString(_SelectedDesignListModel.getSize()));
				_UnitWeightTextField.setText(Integer.toString(unitWeight));
				_UnitBVTextField.setText(Integer.toString(unitBV));
				_UnitCostTextField.setText(Long.toString(unitCost));
			}
		});
	}
	
	private void selectFactionsAvailableForEra(String era)
	{
		_FactionComboBox.removeAllItems();
		for (String factionName: UnitManager.getInstance().getAvailableFactionsInEra(era))
		{
			_FactionComboBox.addItem(factionName);
		}
	}
	
	private void selectFaction()
	{
		String selectedEra = (String)_EraComboBox.getSelectedItem();
		String selectedFaction = (String)_FactionComboBox.getSelectedItem();
		_DesignModel.setEraAndFaction(selectedEra, selectedFaction);
		clearSelectedDesigns();
	}

	private void consumeSelectedDesign()
	{
		if (_DesignTable.getSelectedRow() > -1)
		{
	        int index = _Sorter.modelIndex(_DesignTable.getSelectedRow());
	        String design = _DesignModel.consumeDesign(index);
	        if (design != null)
	        {
	        	_SelectedDesignListModel.addElement(design);
	        	recalculateUnitValues();
	        }
		}
	}
	
	private void releaseSelectedDesign()
	{
		if (_SelectedDesignList.getSelectedIndex() > -1)
		{
			String selectedDesign = _SelectedDesignListModel.getElementAt(_SelectedDesignList.getSelectedIndex());
			BattlemechDesign bd = _DesignManager.Design(selectedDesign);
			_DesignModel.releaseDesign(bd.getName());
			_SelectedDesignListModel.removeElementAt(_SelectedDesignList.getSelectedIndex());
			recalculateUnitValues();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getSource() == _DesignTable && e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
		{
			consumeSelectedDesign();
		}
		if (e.getSource() == _SelectedDesignList && e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
		{
			releaseSelectedDesign();
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(ADD))
		{
			consumeSelectedDesign();
		}
		if (e.getActionCommand().equals(REMOVE))
		{
			releaseSelectedDesign();
		}
		if (e.getActionCommand().equals(GENERATE_UNIT))
		{
			generateUnit();
		}
		if (e.getActionCommand().equals(OPEN_IN_PDF))
		{
			openUnitInPDF();
		}
		if (e.getActionCommand().equals(SAVE_UNIT))
		{
			saveUnit();
			JOptionPane.showMessageDialog(this, "Unit Saved!", "Unit Saved", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}

	private void generateUnit()
	{
		if (_PlayerNameTextField.getText().isEmpty())			
		{
			JOptionPane.showMessageDialog(this, "You must specify a player name", "Player Name empty", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (_PlayerEmailAddressTextField.getText().isEmpty())			
		{
			JOptionPane.showMessageDialog(this, "You must specify a player email addesss", "Player Email Address empty", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (_SelectedDesignListModel.getSize() == 0)
		{
			JOptionPane.showMessageDialog(this, "Select some mechs to add to the unit", "Unit empty", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (_UnitNameTextField.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(this, "The unit must have a valid name", "Unit name empty", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		ArrayList<String> designNames = new ArrayList<String>();
		for (int i = 0; i < _SelectedDesignListModel.getSize(); i++)
		{
			designNames.add(_SelectedDesignListModel.getElementAt(i));
		}
		
		try
		{
			Era era = Era.fromString((String)_EraComboBox.getSelectedItem());
			Faction faction = Faction.fromString((String)_FactionComboBox.getSelectedItem());
			MiniatureCollection ic = new MiniatureCollection(_MiniatureCollectionManager.getMiniatureCollection((String)_CollectionComboBox.getSelectedItem()));
			_Player = new Player();
			_Player.setName(_PlayerNameTextField.getText());
			_Player.setEmailAddress(_PlayerEmailAddressTextField.getText());
			
			Rating rating = (Rating)_UnitRatingComboBox.getSelectedItem();
			QualityRating qualityRating = (QualityRating)_UnitQualityComboBox.getSelectedItem();
			TechRating techRating = (TechRating)_UnitTechRatingComboBox.getSelectedItem();
			
			_CreatedUnit = null;
	        int tries = 0;
	        while (_CreatedUnit == null && tries < 10)
	        {
	        	_CreatedUnit = _UnitManager.generateUnitWithElements(era, faction, _Player, _UnitNameTextField.getText(), designNames, rating, qualityRating, techRating, ic);
	        	tries++;
	        }
        	if (_CreatedUnit == null)
        		throw new Exception("Failed :(");
        	
        	LocalDate date = LocalDate.of(3015, 1, 1);
        	_CreatedUnit.setEstablishDate(date);
        	_UnitDetailsTextArea.setText(_CreatedUnit.toString());
		}
		catch (Exception ex)
		{
			_UnitDetailsTextArea.setText("Failed to generate unit :(" + System.lineSeparator() + ExceptionUtil.getExceptionStackTrace(ex));
		}
	}
	
	private void openUnitInPDF()
	{
		if (_CreatedUnit != null)
		{
			try
			{
				String filename = _UnitManager.printUnitSummaryToPDF(_CreatedUnit);
				Desktop.getDesktop().open(new File(filename));
			}
			catch (Exception ex)
			{
				System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
			}
		}
	}
	
	public void saveUnit()
	{
		try
		{
			_UnitManager.saveUnit(_CreatedUnit);
			_UnitManager.refreshUnitList();
		}
		catch (Exception ex)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
