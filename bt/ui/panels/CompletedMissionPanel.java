package bt.ui.panels;

import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bt.elements.unit.CompletedMission;
import bt.util.SwingHelper;

public class CompletedMissionPanel extends JPanel 
{
	private static final long serialVersionUID = 8619101813333802653L;

	JTextField _MissionIdTextField;
	JTextField _MissionTitleTextField;
	JFormattedTextField _PrizeMoneyTextField;
	JComboBox<CompletedMission.Result> _MissionResultComboBox;
	
	public CompletedMissionPanel(String missionID, String missionTitle)
	{
		_MissionIdTextField = new JTextField(missionID);
		_MissionTitleTextField = new JTextField(missionTitle);
		_MissionResultComboBox = new JComboBox<CompletedMission.Result>(CompletedMission.Result.values());
		_PrizeMoneyTextField = new JFormattedTextField(NumberFormat.getCurrencyInstance());
		
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(SwingHelper.GetTextField(_MissionIdTextField, "Mission ID", "The identifier of the mission", true));
		add(SwingHelper.GetTextField(_MissionTitleTextField, "Mission Title", "The title of the mission", true));
		add(SwingHelper.GetComboBox(_MissionResultComboBox, "Result", "The outcome of the Mission", true));
		add(SwingHelper.GetTextField(_PrizeMoneyTextField, "Prize Money", "Enter the prize money for the completion of this mission", true));
		
		_MissionIdTextField.setText(missionID);
		_MissionIdTextField.setEditable(false);
		_MissionTitleTextField.setText(missionTitle);
		_MissionTitleTextField.setEditable(false);
		_MissionResultComboBox.setSelectedItem(CompletedMission.Result.WIN);
		_PrizeMoneyTextField.setValue(new Double(0));
	}
	
	public double getPrizeMoney()
	{
		return new Double((Long)_PrizeMoneyTextField.getValue());
	}
	
	public CompletedMission.Result getResult()
	{
		return (CompletedMission.Result)_MissionResultComboBox.getSelectedItem();
	}
}
