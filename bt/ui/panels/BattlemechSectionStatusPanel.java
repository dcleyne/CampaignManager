package bt.ui.panels;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bt.elements.BattlemechSection;
import bt.elements.SectionStatus;

public class BattlemechSectionStatusPanel extends JPanel 
{
	private static final long serialVersionUID = -4648355923102197885L;

	private BattlemechSection _Section;
	
	private JCheckBox _BreachedCheckBox;
	private JComboBox<SectionStatus.Status> _StatusComboBox;
	
	public BattlemechSectionStatusPanel(BattlemechSection section, SectionStatus status)
	{
		_Section = section;
		
		BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
		setLayout(layout);

		JTextField sectionNameTextField = new JTextField(section.toString());
		sectionNameTextField.setEditable(false);
		add(sectionNameTextField);
		
		_BreachedCheckBox = new JCheckBox("Breached");
		_BreachedCheckBox.setSelected(status.isBreached());
		add(_BreachedCheckBox);
		
		_StatusComboBox = new JComboBox<SectionStatus.Status>(section.validStatuses());
		_StatusComboBox.setSelectedItem(status.getStatus());
		add(_StatusComboBox);
		
    	add(Box.createHorizontalGlue());
	}
	
	public Dimension getMaximumSize()
	{
		return new Dimension(Integer.MAX_VALUE, 30);
	}
	
	public BattlemechSection getSection()
	{
		return _Section;
	}
	
	public SectionStatus.Status getSectionStatus()
	{
		return (SectionStatus.Status)_StatusComboBox.getSelectedItem();
	}
	
	public boolean isBreached()
	{
		return _BreachedCheckBox.isSelected();
	}
	
}
