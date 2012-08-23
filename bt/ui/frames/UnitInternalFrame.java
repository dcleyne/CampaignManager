package bt.ui.frames;


import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bt.elements.unit.Unit;
import bt.ui.frames.ClosableEditFrame;
import bt.ui.panels.ClosableEditPanel;
import bt.ui.panels.UnitAssetPanel;
import bt.ui.panels.UnitDetailsPanel;
import bt.ui.panels.UnitFinancePanel;
import bt.ui.panels.UnitMissionPanel;
import bt.ui.panels.UnitPersonnelPanel;

public class UnitInternalFrame extends JInternalFrame implements ClosableEditFrame, ChangeListener
{
	private static final long serialVersionUID = 1;
	
    protected JTabbedPane m_Tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
    protected Unit m_Unit = null;

    public UnitInternalFrame(String Title, Unit u)
    {
        super(Title, true, true, true, true);
        m_Unit = u;
        getContentPane().setLayout(new BorderLayout());

        m_Tabs.add("Details", new UnitDetailsPanel(u));
        m_Tabs.add("Structure", new JPanel());
        m_Tabs.add("Personnel", new UnitPersonnelPanel(u));
        m_Tabs.add("Assets", new UnitAssetPanel(u));
        m_Tabs.add("Finances", new UnitFinancePanel(u));
        m_Tabs.add("Missions", new UnitMissionPanel(u));

        m_Tabs.addChangeListener(this);

        getContentPane().add(m_Tabs, BorderLayout.CENTER);
        setVisible(true);

        m_Tabs.requestFocus();
    }

    public Unit GetUnit()
    {return m_Unit;
    }

    public boolean IsClosable()
    {
        boolean Closable = false;
        for (int count = 0; count < m_Tabs.getTabCount(); count++)
        {
            Closable &= ( (ClosableEditPanel)m_Tabs.getComponentAt(count)).IsClosable();
        }
        return Closable;
    }

    public void ForceEditCompletion()
    {
        for (int count = 0; count < m_Tabs.getTabCount(); count++)
        {
        	if (m_Tabs.getComponentAt(count) instanceof ClosableEditPanel)
        	{
        		( (ClosableEditPanel)m_Tabs.getComponentAt(count)).ForceEditCompletion();
        	}
        }
    }

    public void stateChanged(ChangeEvent ce)
    {
        ForceEditCompletion();
    }

}
