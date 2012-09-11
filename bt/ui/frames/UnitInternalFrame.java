package bt.ui.frames;


import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bt.elements.unit.Unit;
import bt.managers.UnitManager;
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
	
    protected JTabbedPane _Tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
    protected Unit _Unit = null;

    public UnitInternalFrame(String Title, Unit u)
    {
        super(Title, true, true, true, true);
        _Unit = u;
        getContentPane().setLayout(new BorderLayout());

        _Tabs.add("Details", new UnitDetailsPanel(u));
        _Tabs.add("Structure", new JPanel());
        _Tabs.add("Personnel", new UnitPersonnelPanel(u));
        _Tabs.add("Assets", new UnitAssetPanel(u));
        _Tabs.add("Finances", new UnitFinancePanel(u));
        _Tabs.add("Missions", new UnitMissionPanel(u));

        _Tabs.addChangeListener(this);

        getContentPane().add(_Tabs, BorderLayout.CENTER);
        setVisible(true);

        _Tabs.requestFocus();
    }

    public Unit GetUnit()
    {return _Unit;
    }

    public boolean isFrameClosable()
    {
        boolean Closable = false;
        for (int count = 0; count < _Tabs.getTabCount(); count++)
        {
            Closable &= ( (ClosableEditPanel)_Tabs.getComponentAt(count)).isClosable();
        }
        return Closable;
    }

    public void forceFrameEditCompletion()
    {
        for (int count = 0; count < _Tabs.getTabCount(); count++)
        {
        	if (_Tabs.getComponentAt(count) instanceof ClosableEditPanel)
        	{
        		( (ClosableEditPanel)_Tabs.getComponentAt(count)).forceEditCompletion();
        	}
        }
        
        try
		{
			UnitManager.getInstance().saveUnit(_Unit);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
    }

    public void stateChanged(ChangeEvent ce)
    {
        forceFrameEditCompletion();
    }

}
