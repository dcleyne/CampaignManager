package bt.ui.panels;

import java.util.Collections;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import bt.elements.Battlemech;
import bt.elements.BattlemechSection;
import bt.elements.SectionStatus;

public class BattlemechSectionStatusesPanel extends JPanel 
{
	private static final long serialVersionUID = 8170777919819400281L;
	
	private Battlemech _Mech;
	private Vector<BattlemechSectionStatusPanel> _StatusPanels = new Vector<BattlemechSectionStatusPanel>();

	public BattlemechSectionStatusesPanel(Battlemech mech)
	{
		_Mech = mech;
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);

		populatePanel();
	}
	
	public void refreshPanel()
	{
		removeAll();
		populatePanel();
	}
	
	private void populatePanel()
	{
		Vector<BattlemechSection> sections = new Vector<BattlemechSection>(_Mech.getSectionStatuses().keySet());
		Collections.sort(sections);
		
		for (BattlemechSection section : sections)
		{
			BattlemechSectionStatusPanel bssp = new BattlemechSectionStatusPanel(section, _Mech.getSectionStatuses().get(section));
			_StatusPanels.add(bssp);
			add(bssp);
		}
		
    	add(Box.createVerticalGlue());		
	}
	
	public void finaliseSectionDamage()
	{
		for (BattlemechSectionStatusPanel bssp: _StatusPanels)
		{
			BattlemechSection section = bssp.getSection();
			SectionStatus.Status status = bssp.getSectionStatus();
			boolean isBreached = bssp.isBreached();
			
			_Mech.getSectionStatuses().get(section).setStatus(status);
			_Mech.getSectionStatuses().get(section).setBreached(isBreached);
		}
	}
}
