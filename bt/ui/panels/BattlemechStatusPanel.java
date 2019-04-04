package bt.ui.panels;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import bt.elements.Battlemech;
import bt.elements.personnel.Mechwarrior;

public class BattlemechStatusPanel extends JPanel
{
	private static final long serialVersionUID = -6415697568212173096L;

	private Battlemech _Mech;
	private BattlemechSectionStatusesPanel _SectionStatusesPanel;
	private BattlemechStatusDiagramPanel _DiagramPanel;
	
	public BattlemechStatusPanel(Battlemech mech, Mechwarrior warrior, double scale)
	{
		setLayout(new BorderLayout());
		
		_Mech = mech;
		
        JTabbedPane tabbedPane = new JTabbedPane();
        
        _SectionStatusesPanel = new BattlemechSectionStatusesPanel(mech);
        _DiagramPanel = new BattlemechStatusDiagramPanel(mech, warrior, scale);
        		
        tabbedPane.addTab("Sections", _SectionStatusesPanel);
        tabbedPane.addTab("Display", _DiagramPanel);
        
        add(tabbedPane, BorderLayout.CENTER);        
	}
	
	public Battlemech getBattlemech()
	{
		return _Mech;
	}
	
	public void finaliseDamageNotation()
	{
		_SectionStatusesPanel.finaliseSectionDamage();
		_Mech.applyDamage(_DiagramPanel.finaliseDamageNotation());
	}
	
	public void refreshPanel()
	{
		_SectionStatusesPanel.refreshPanel();
		_DiagramPanel.refreshPanel();
	}

}
