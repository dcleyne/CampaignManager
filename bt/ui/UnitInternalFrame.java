package bt.ui;


import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bt.elements.unit.Unit;
import bt.ui.ClosableEditForm;
import bt.ui.ClosableEditFrame;
import bt.ui.forms.UnitAssetPanel;
import bt.ui.forms.UnitDetailsPanel;
import bt.ui.forms.UnitFinancePanel;
import bt.ui.forms.UnitMissionPanel;
import bt.ui.forms.UnitPersonnelPanel;

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
            Closable &= ( (ClosableEditForm)m_Tabs.getComponentAt(count)).IsClosable();
        }
        return Closable;
    }

    public void ForceEditCompletion()
    {
        for (int count = 0; count < m_Tabs.getTabCount(); count++)
        {
        	if (m_Tabs.getComponentAt(count) instanceof ClosableEditForm)
        	{
        		( (ClosableEditForm)m_Tabs.getComponentAt(count)).ForceEditCompletion();
        	}
        }
    }

    public void stateChanged(ChangeEvent ce)
    {
        ForceEditCompletion();
    }

}
