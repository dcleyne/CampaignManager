package bt.ui.panels;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bt.elements.BattlemechSection;
import bt.elements.SectionStatus;

public class BattlemechSectionStatusPanel extends JPanel 
{
	private static final long serialVersionUID = -4648355923102197885L;

	private BattlemechSection _Section;
	
	private JComboBox<SectionStatus> _StatusComboBox;
	
	public BattlemechSectionStatusPanel(BattlemechSection section, SectionStatus status)
	{
		_Section = section;
		
		BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
		setLayout(layout);

		JTextField sectionNameTextField = new JTextField(section.toString());
		sectionNameTextField.setEditable(false);
		add(sectionNameTextField);
		
		_StatusComboBox = new JComboBox<SectionStatus>(section.validStatuses());
		_StatusComboBox.setSelectedItem(status);
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
	
	public SectionStatus getSectionStatus()
	{
		return (SectionStatus)_StatusComboBox.getSelectedItem();
	}
	
}
