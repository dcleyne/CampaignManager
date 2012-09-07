package bt.ui.panels;

import java.util.Collections;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import bt.elements.Battlemech;
import bt.elements.BattlemechSection;

public class BattlemechSectionStatusesPanel extends JPanel 
{
	private static final long serialVersionUID = 8170777919819400281L;

	public BattlemechSectionStatusesPanel(Battlemech mech)
	{
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);

		Vector<BattlemechSection> sections = new Vector<BattlemechSection>(mech.getSectionStatuses().keySet());
		Collections.sort(sections);
		
		for (BattlemechSection section : sections)
		{
			BattlemechSectionStatusPanel bssp = new BattlemechSectionStatusPanel(section, mech.getSectionStatuses().get(section));
			add(bssp);
		}
		
    	add(Box.createVerticalGlue());
	}
	
	public void finaliseSectionDamage()
	{
		
	}
}
