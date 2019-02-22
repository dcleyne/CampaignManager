package bt.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import bt.elements.Era;
import bt.elements.Faction;
import bt.elements.unit.Unit;
import bt.ui.panels.NewOpponentPanel;

public class GenerateNewMissionDialog extends JDialog implements ActionListener
{
	private NewOpponentPanel _OpponentRatingsPanel;
	private JButton _OkButton;
	private JButton _CancelButton;
	
	@SuppressWarnings("unused")
	private Era _Era = Era.LATE_SUCCESSION_WAR_RENAISSANCE;
	@SuppressWarnings("unused")
	private Faction _Faction = Faction.MERCENARY;
	@SuppressWarnings("unused")
	private Unit _Unit;
	
	private boolean _MissionGenerated = false;
	
	public GenerateNewMissionDialog(Unit u) 
	{
		_Unit = u;
		setTitle("Generate New Mission");
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		_OpponentRatingsPanel = new NewOpponentPanel(u);
		_OpponentRatingsPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Opponent Ratings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(_OpponentRatingsPanel, BorderLayout.CENTER);
		
		JPanel _ButtonPanel = new JPanel();
		getContentPane().add(_ButtonPanel, BorderLayout.SOUTH);
		_ButtonPanel.setLayout(new BoxLayout(_ButtonPanel, BoxLayout.LINE_AXIS));
		
		Component horizontalGlue = Box.createHorizontalGlue();
		_ButtonPanel.add(horizontalGlue);
		
		_OkButton = new JButton("Ok");
		_OkButton.addActionListener(this);
		_OkButton.setActionCommand("Ok");
		_ButtonPanel.add(_OkButton);
		
		_CancelButton = new JButton("Cancel");
		_CancelButton.addActionListener(this);
		_CancelButton.setActionCommand("Cancel");
		_ButtonPanel.add(_CancelButton);
		
		pack();
	}
	private static final long serialVersionUID = 2108926199669612135L;

	public boolean wasMissionGenerated()
	{
		return _MissionGenerated;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String cmd = e.getActionCommand();
		
		if (cmd.equalsIgnoreCase("Ok"))
		{
			/*
			try 
			{
				Rating opponentRating = _OpponentRatingsPanel.getSelectedRating();
				QualityRating opponentQualityRating = _OpponentRatingsPanel.getSelectedQualityRating();
				TechRating opponentTechRating = _OpponentRatingsPanel.getSelectedTechRating();
				
				Scenario scenario = MissionManager.getInstance().generateScenario(_Era, _Faction, _Unit, opponentRating, opponentQualityRating, opponentTechRating, null, new UnlimitedCollection());
				MissionManager.getInstance().SaveScenarioForUnit(_Unit, scenario);
				_Unit.setAssignedMission(scenario.getMission().getID(), scenario.getMission().getName());
				UnitManager.getInstance().saveUnit(_Unit);
				
				_MissionGenerated = true;
				
				this.setVisible(false);			
			} 
			catch (Exception e1) 
			{
				e1.printStackTrace();
			}
			*/
		}
		if (cmd.equalsIgnoreCase("Cancel"))
		{
			this.setVisible(false);
		}
		
	}

	
}
