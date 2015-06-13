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
		GridBagConstraints gbclblScenario = new GridBagConstraints();
		gbclblScenario.anchor = GridBagConstraints.EAST;
		gbclblScenario.insets = new Insets(0, 5, 5, 5);
		gbclblScenario.gridx = 0;
		gbclblScenario.gridy = 3;
		add(lblScenario, gbclblScenario);
		
		JComboBox<String> _ScenarioSelectionComboBox = new JComboBox<String>();
		GridBagConstraints gbcScenarioSelectionComboBox = new GridBagConstraints();
		gbcScenarioSelectionComboBox.insets = new Insets(0, 0, 5, 5);
		gbcScenarioSelectionComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbcScenarioSelectionComboBox.gridx = 1;
		gbcScenarioSelectionComboBox.gridy = 3;
		add(_ScenarioSelectionComboBox, gbcScenarioSelectionComboBox);
		
		JButton _RandomScenarioButton = new JButton("...");
		GridBagConstraints gbcRandomScenarioButton = new GridBagConstraints();
		gbcRandomScenarioButton.insets = new Insets(0, 0, 5, 0);
		gbcRandomScenarioButton.gridx = 2;
		gbcRandomScenarioButton.gridy = 3;
		add(_RandomScenarioButton, gbcRandomScenarioButton);
		
		JButton btnGenerate = new JButton("Generate");
		GridBagConstraints gbc_btnGenerate = new GridBagConstraints();
		gbc_btnGenerate.insets = new Insets(0, 0, 5, 5);
		gbc_btnGenerate.gridx = 1;
		gbc_btnGenerate.gridy = 4;
		add(btnGenerate, gbc_btnGenerate);
		
		JPanel _Side1Panel = new JPanel();
		_Side1Panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Side 1", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbcSide1Panel = new GridBagConstraints();
		gbcSide1Panel.gridwidth = 3;
		gbcSide1Panel.insets = new Insets(0, 0, 5, 5);
		gbcSide1Panel.fill = GridBagConstraints.BOTH;
		gbcSide1Panel.gridx = 0;
		gbcSide1Panel.gridy = 5;
		add(_Side1Panel, gbcSide1Panel);
		_Side1Panel.setLayout(new BorderLayout(0, 0));
		
		JTextPane _Side1TextPane = new JTextPane();
		_Side1TextPane.setEditable(false);
		_Side1Panel.add(_Side1TextPane, BorderLayout.CENTER);
		
		JPanel _Side2Panel = new JPanel();
		_Side2Panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Side 2", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbcSide2Panel = new GridBagConstraints();
		gbcSide2Panel.gridwidth = 3;
		gbcSide2Panel.insets = new Insets(0, 0, 5, 5);
		gbcSide2Panel.fill = GridBagConstraints.BOTH;
		gbcSide2Panel.gridx = 0;
		gbcSide2Panel.gridy = 6;
		add(_Side2Panel, gbcSide2Panel);
		_Side2Panel.setLayout(new BorderLayout(0, 0));
		
		JTextPane _Side2TextPane = new JTextPane();
		_Side2TextPane.setEditable(false);
		_Side2Panel.add(_Side2TextPane, BorderLayout.CENTER);
		
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
		_ButtonPanel.add(_SaveGameButton);
		
		_ButtonPanel.add(Box.createHorizontalStrut(20));
		
		JButton _ExportGameButton = new JButton("Export Game");
		_ButtonPanel.add(_ExportGameButton);
	}


	
	
}
