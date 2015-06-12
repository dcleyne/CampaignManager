package bt.ui.panels;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JTextPane;
import javax.swing.Box;

public class RandomGamePanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	public RandomGamePanel() 
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
		
		JComboBox<String> _GameSizeComboBox = new JComboBox<String>();
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
		
		JComboBox<String> _MiniatureCollection1ComboBox = new JComboBox<String>();
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
		
		JComboBox<String> _MiniatureCollection2ComboBox = new JComboBox<String>();
		GridBagConstraints gbcMiniatureCollection2ComboBox = new GridBagConstraints();
		gbcMiniatureCollection2ComboBox.insets = new Insets(0, 0, 5, 5);
		gbcMiniatureCollection2ComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbcMiniatureCollection2ComboBox.gridx = 1;
		gbcMiniatureCollection2ComboBox.gridy = 2;
		add(_MiniatureCollection2ComboBox, gbcMiniatureCollection2ComboBox);
		
		JLabel lblScenario = new JLabel("Scenario");
		GridBagConstraints gbc_lblScenario = new GridBagConstraints();
		gbc_lblScenario.anchor = GridBagConstraints.EAST;
		gbc_lblScenario.insets = new Insets(0, 5, 5, 5);
		gbc_lblScenario.gridx = 0;
		gbc_lblScenario.gridy = 3;
		add(lblScenario, gbc_lblScenario);
		
		JComboBox<String> _ScenarioSelectionComboBox = new JComboBox<String>();
		GridBagConstraints gbc__ScenarioSelectionComboBox = new GridBagConstraints();
		gbc__ScenarioSelectionComboBox.insets = new Insets(0, 0, 5, 5);
		gbc__ScenarioSelectionComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc__ScenarioSelectionComboBox.gridx = 1;
		gbc__ScenarioSelectionComboBox.gridy = 3;
		add(_ScenarioSelectionComboBox, gbc__ScenarioSelectionComboBox);
		
		JButton _RandomScenarioButton = new JButton("...");
		GridBagConstraints gbc__RandomScenarioButton = new GridBagConstraints();
		gbc__RandomScenarioButton.insets = new Insets(0, 0, 5, 0);
		gbc__RandomScenarioButton.gridx = 2;
		gbc__RandomScenarioButton.gridy = 3;
		add(_RandomScenarioButton, gbc__RandomScenarioButton);
		
		JButton btnGenerate = new JButton("Generate");
		GridBagConstraints gbc_btnGenerate = new GridBagConstraints();
		gbc_btnGenerate.insets = new Insets(0, 0, 5, 5);
		gbc_btnGenerate.gridx = 1;
		gbc_btnGenerate.gridy = 4;
		add(btnGenerate, gbc_btnGenerate);
		
		JPanel _Side1Panel = new JPanel();
		_Side1Panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Side 1", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc__Side1Panel = new GridBagConstraints();
		gbc__Side1Panel.gridwidth = 3;
		gbc__Side1Panel.insets = new Insets(0, 0, 5, 5);
		gbc__Side1Panel.fill = GridBagConstraints.BOTH;
		gbc__Side1Panel.gridx = 0;
		gbc__Side1Panel.gridy = 5;
		add(_Side1Panel, gbc__Side1Panel);
		_Side1Panel.setLayout(new BorderLayout(0, 0));
		
		JTextPane _Side1TextPane = new JTextPane();
		_Side1TextPane.setEditable(false);
		_Side1Panel.add(_Side1TextPane, BorderLayout.CENTER);
		
		JPanel _Side2Panel = new JPanel();
		_Side2Panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Side 2", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc__Side2Panel = new GridBagConstraints();
		gbc__Side2Panel.gridwidth = 3;
		gbc__Side2Panel.insets = new Insets(0, 0, 5, 5);
		gbc__Side2Panel.fill = GridBagConstraints.BOTH;
		gbc__Side2Panel.gridx = 0;
		gbc__Side2Panel.gridy = 6;
		add(_Side2Panel, gbc__Side2Panel);
		_Side2Panel.setLayout(new BorderLayout(0, 0));
		
		JTextPane _Side2TextPane = new JTextPane();
		_Side2TextPane.setEditable(false);
		_Side2Panel.add(_Side2TextPane, BorderLayout.CENTER);
		
		JPanel _ButtonPanel = new JPanel();
		GridBagConstraints gbc__ButtonPanel = new GridBagConstraints();
		gbc__ButtonPanel.gridwidth = 3;
		gbc__ButtonPanel.insets = new Insets(0, 0, 0, 5);
		gbc__ButtonPanel.fill = GridBagConstraints.BOTH;
		gbc__ButtonPanel.gridx = 0;
		gbc__ButtonPanel.gridy = 7;
		add(_ButtonPanel, gbc__ButtonPanel);
		_ButtonPanel.setLayout(new BoxLayout(_ButtonPanel, BoxLayout.X_AXIS));
		
		_ButtonPanel.add(Box.createHorizontalGlue());
		
		JButton _SaveGameButton = new JButton("Save Game");
		_ButtonPanel.add(_SaveGameButton);
		
		_ButtonPanel.add(Box.createHorizontalStrut(20));
		
		JButton _ExportGameButton = new JButton("Export Game");
		_ButtonPanel.add(_ExportGameButton);
	}


	
	
}
