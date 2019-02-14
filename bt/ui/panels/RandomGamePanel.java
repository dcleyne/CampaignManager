package bt.ui.panels;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.border.TitledBorder;

import bt.elements.Era;
import bt.elements.Faction;
import bt.elements.collection.ItemCollection;
import bt.elements.collection.UnlimitedCollection;
import bt.elements.personnel.Rating;
import bt.elements.scenario.Scenario;
import bt.elements.unit.MechUnitParameters;
import bt.elements.unit.Player;
import bt.elements.unit.QualityRating;
import bt.elements.unit.TechRating;
import bt.elements.unit.Unit;
import bt.managers.MiniatureCollectionManager;
import bt.managers.MissionManager;
import bt.managers.UnitManager;
import bt.util.Dice;
import bt.util.ExceptionUtil;
import bt.util.FileUtil;

import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
import java.awt.Desktop;

import javax.swing.BoxLayout;
import javax.swing.JTextPane;
import javax.swing.Box;

public class RandomGamePanel extends JPanel implements ActionListener
{
	private static final String RANDOM_SCENARIO = "RandomScenario";
	private static final String GENERATE = "Generate";
	private static final String SAVE_GAME = "SaveGame";
	private static final String EXPORT_GAME = "ExportGame";
	private static final long serialVersionUID = 1L;
	
	private static int[] minBVs = {1500, 2000, 2500, 3000, 4000};
	
	private JComboBox<String> _GameSizeComboBox;
	private JComboBox<String> _MiniatureCollection1ComboBox;
	private JComboBox<String> _MiniatureCollection2ComboBox;
	private JTextPane _ScenarioTextPane;
	private JComboBox<String> _ScenarioSelectionComboBox;
	
	public RandomGamePanel() 
	{
		initialize();
	}

	private void initialize() 
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblGameSize = new JLabel("Game Size");
		GridBagConstraints gbc_lblGameSize = new GridBagConstraints();
		gbc_lblGameSize.insets = new Insets(0, 5, 5, 5);
		gbc_lblGameSize.anchor = GridBagConstraints.EAST;
		gbc_lblGameSize.gridx = 0;
		gbc_lblGameSize.gridy = 0;
		add(lblGameSize, gbc_lblGameSize);
		
		_GameSizeComboBox = new JComboBox<String>(new String[]{"Small", "Medium", "Large", "Very Large", "Huge"});
		GridBagConstraints gbcGameSizeComboBox = new GridBagConstraints();
		gbcGameSizeComboBox.insets = new Insets(0, 0, 5, 5);
		gbcGameSizeComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbcGameSizeComboBox.gridx = 1;
		gbcGameSizeComboBox.gridy = 0;
		add(_GameSizeComboBox, gbcGameSizeComboBox);
		
		JLabel lblSide1MiniatureCollection = new JLabel("Side 1 Miniature Collection");
		GridBagConstraints gbcSide1MiniatureCollection = new GridBagConstraints();
		gbcSide1MiniatureCollection.anchor = GridBagConstraints.EAST;
		gbcSide1MiniatureCollection.insets = new Insets(0, 5, 5, 5);
		gbcSide1MiniatureCollection.gridx = 0;
		gbcSide1MiniatureCollection.gridy = 1;
		add(lblSide1MiniatureCollection, gbcSide1MiniatureCollection);
		
		List<String> collections = MiniatureCollectionManager.getInstance().getCollectionNames();
		collections.add(0, "Unlimited");
		_MiniatureCollection1ComboBox = new JComboBox<String>(collections.toArray(new String[collections.size()]));
		
		GridBagConstraints gbcMiniatureCollection1ComboBox = new GridBagConstraints();
		gbcMiniatureCollection1ComboBox.insets = new Insets(0, 0, 5, 5);
		gbcMiniatureCollection1ComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbcMiniatureCollection1ComboBox.gridx = 1;
		gbcMiniatureCollection1ComboBox.gridy = 1;
		add(_MiniatureCollection1ComboBox, gbcMiniatureCollection1ComboBox);
		
		JLabel lblSide2MiniatureCollection = new JLabel("Side 2 Miniature Collection");
		GridBagConstraints gbclblSide2MiniatureCollection = new GridBagConstraints();
		gbclblSide2MiniatureCollection.anchor = GridBagConstraints.EAST;
		gbclblSide2MiniatureCollection.insets = new Insets(0, 5, 5, 5);
		gbclblSide2MiniatureCollection.gridx = 0;
		gbclblSide2MiniatureCollection.gridy = 2;
		add(lblSide2MiniatureCollection, gbclblSide2MiniatureCollection);
		
		_MiniatureCollection2ComboBox = new JComboBox<String>(collections.toArray(new String[collections.size()]));
		GridBagConstraints gbcMiniatureCollection2ComboBox = new GridBagConstraints();
		gbcMiniatureCollection2ComboBox.insets = new Insets(0, 0, 5, 5);
		gbcMiniatureCollection2ComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbcMiniatureCollection2ComboBox.gridx = 1;
		gbcMiniatureCollection2ComboBox.gridy = 2;
		add(_MiniatureCollection2ComboBox, gbcMiniatureCollection2ComboBox);
		
		JLabel lblScenario = new JLabel("Scenario");
		GridBagConstraints gbclblScenario = new GridBagConstraints();
		gbclblScenario.anchor = GridBagConstraints.EAST;
		gbclblScenario.insets = new Insets(0, 5, 5, 5);
		gbclblScenario.gridx = 0;
		gbclblScenario.gridy = 3;
		add(lblScenario, gbclblScenario);
		
		List<String> missions = MissionManager.getInstance().getMissionList();
		_ScenarioSelectionComboBox = new JComboBox<String>(missions.toArray(new String[missions.size()]));
		
		GridBagConstraints gbcScenarioSelectionComboBox = new GridBagConstraints();
		gbcScenarioSelectionComboBox.insets = new Insets(0, 0, 5, 5);
		gbcScenarioSelectionComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbcScenarioSelectionComboBox.gridx = 1;
		gbcScenarioSelectionComboBox.gridy = 3;
		add(_ScenarioSelectionComboBox, gbcScenarioSelectionComboBox);
		
		JButton _RandomScenarioButton = new JButton("...");
		_RandomScenarioButton.setActionCommand(RANDOM_SCENARIO);
		_RandomScenarioButton.addActionListener(this);
		GridBagConstraints gbcRandomScenarioButton = new GridBagConstraints();
		gbcRandomScenarioButton.insets = new Insets(0, 0, 5, 0);
		gbcRandomScenarioButton.gridx = 2;
		gbcRandomScenarioButton.gridy = 3;
		add(_RandomScenarioButton, gbcRandomScenarioButton);
		
		JButton btnGenerate = new JButton(GENERATE);
		btnGenerate.setActionCommand(GENERATE);
		btnGenerate.addActionListener(this);
		GridBagConstraints gbc_btnGenerate = new GridBagConstraints();
		gbc_btnGenerate.insets = new Insets(0, 0, 5, 5);
		gbc_btnGenerate.gridx = 1;
		gbc_btnGenerate.gridy = 4;
		add(btnGenerate, gbc_btnGenerate);
		
		JPanel _ScenarioPanel = new JPanel();
		_ScenarioPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Scenario", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbcScenarioPanel = new GridBagConstraints();
		gbcScenarioPanel.gridwidth = 3;
		gbcScenarioPanel.insets = new Insets(0, 0, 5, 5);
		gbcScenarioPanel.fill = GridBagConstraints.BOTH;
		gbcScenarioPanel.gridx = 0;
		gbcScenarioPanel.gridy = 5;
		add(_ScenarioPanel, gbcScenarioPanel);
		_ScenarioPanel.setLayout(new BorderLayout(0, 0));
		
		_ScenarioTextPane = new JTextPane();
		_ScenarioTextPane.setEditable(false);
		_ScenarioPanel.add(_ScenarioTextPane, BorderLayout.CENTER);
		
		JPanel _ButtonPanel = new JPanel();
		GridBagConstraints gbcButtonPanel = new GridBagConstraints();
		gbcButtonPanel.gridwidth = 3;
		gbcButtonPanel.insets = new Insets(0, 0, 0, 5);
		gbcButtonPanel.fill = GridBagConstraints.BOTH;
		gbcButtonPanel.gridx = 0;
		gbcButtonPanel.gridy = 7;
		add(_ButtonPanel, gbcButtonPanel);
		_ButtonPanel.setLayout(new BoxLayout(_ButtonPanel, BoxLayout.X_AXIS));
		
		_ButtonPanel.add(Box.createHorizontalGlue());
		
		JButton _SaveGameButton = new JButton("Save Game");
		_SaveGameButton.setActionCommand(SAVE_GAME);
		_SaveGameButton.addActionListener(this);
		_ButtonPanel.add(_SaveGameButton);
		
		_ButtonPanel.add(Box.createHorizontalStrut(20));
		
		JButton _ExportGameButton = new JButton("Export Game");
		_ExportGameButton.setActionCommand(EXPORT_GAME);
		_ExportGameButton.addActionListener(this);
		_ButtonPanel.add(_ExportGameButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getActionCommand().equalsIgnoreCase(RANDOM_SCENARIO))
		{
			_ScenarioSelectionComboBox.setSelectedIndex(Dice.random(_ScenarioSelectionComboBox.getItemCount()) - 1);
		}
		if (e.getActionCommand().equalsIgnoreCase(GENERATE))
		{
			generateGame();
		}
		if (e.getActionCommand().equalsIgnoreCase(SAVE_GAME))
		{
			
		}
		if (e.getActionCommand().equalsIgnoreCase(EXPORT_GAME))
		{
			
		}
	}

	private void generateGame()
	{
		int bv = minBVs[_GameSizeComboBox.getSelectedIndex()] + Dice.d100(10);
		ArrayList<MechUnitParameters> params = UnitManager.getInstance().getMechUnitParametersForBV(bv);
		MechUnitParameters mup = params.get(0);
		if (params.size() > 1)
			mup = params.get(Dice.random(params.size()));
		
		try
		{		
			Player p1 = new Player();
			p1.setName("Player 1");
			p1.setNickname("P1");

			ItemCollection collection1 = new UnlimitedCollection();
			if (_MiniatureCollection1ComboBox.getSelectedIndex() > 0)
			{
				String collectionName = _MiniatureCollection1ComboBox.getItemAt(_MiniatureCollection1ComboBox.getSelectedIndex());
				collection1 = MiniatureCollectionManager.getInstance().getMiniatureCollection(collectionName);
			}
			ItemCollection collection2 = new UnlimitedCollection();
			if (_MiniatureCollection2ComboBox.getSelectedIndex() > 0)
			{
				String collectionName = _MiniatureCollection2ComboBox.getItemAt(_MiniatureCollection2ComboBox.getSelectedIndex());
				collection2 = MiniatureCollectionManager.getInstance().getMiniatureCollection(collectionName);
			}
			
			Era era = Era.LATE_SUCCESSION_WAR_RENAISSANCE;
			Faction f = Faction.MERCENARY;
			Unit player1Unit = UnitManager.getInstance().generateUnit(era, f, p1, "Player 1 Unit", mup, Rating.REGULAR, QualityRating.D, TechRating.D, 0, collection1);
			if (player1Unit == null)
				throw new Exception("Unable to create unit for player 1");
			
			String missionName = _ScenarioSelectionComboBox.getSelectedItem().toString();
			Scenario s = MissionManager.getInstance().generateScenario(era, f, player1Unit, Rating.REGULAR, QualityRating.D, TechRating.D, missionName, collection2);
			if (s == null)
				throw new Exception("Unable to create scenario");
			
			String filename = MissionManager.getInstance().printScenarioToPDF(FileUtil.getTempFolder(), player1Unit, 1, s);
			Desktop.getDesktop().open(new File(filename));
			
			_ScenarioTextPane.setText("Scenario created Ok: " + filename);
		}
		catch (Exception ex)
		{
			_ScenarioTextPane.setText(ExceptionUtil.getExceptionStackTrace(ex));
		}

	}
	
	
}
