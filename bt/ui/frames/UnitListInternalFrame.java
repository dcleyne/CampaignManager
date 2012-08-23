package bt.ui.frames;


import java.awt.BorderLayout;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import bt.elements.unit.Unit;
import bt.managers.UnitManager;
import bt.managers.listeners.UnitManagerListener;
import bt.ui.listeners.UnitChangeListener;
import bt.ui.panels.UnitListPanel;

public class UnitListInternalFrame extends JInternalFrame implements ActionListener, UnitManagerListener
{
	private static final long serialVersionUID = 1;
	
    protected UnitListPanel m_UnitListPanel = null;
    protected JPanel m_ButtonPanel = new JPanel();

    protected JButton m_NewButton = new JButton("New Unit");
    protected JButton m_RemoveButton = new JButton("Remove Unit");
    protected JButton m_EditButton = new JButton("Edit Unit");

    public UnitListInternalFrame(String Title, Vector<Unit> units, UnitChangeListener ucl)
    {
        super(Title, true, true, true, true);

        m_UnitListPanel = new UnitListPanel(units);
        m_UnitListPanel.addUnitChangeListener(ucl);
        
        getContentPane().setLayout(new BorderLayout());

        //m_UnitListPanel.setFocusable(false);
        getContentPane().add(m_UnitListPanel, BorderLayout.CENTER);

        m_ButtonPanel.setBorder(BorderFactory.createEtchedBorder());
        m_ButtonPanel.setLayout(new BoxLayout(m_ButtonPanel, BoxLayout.X_AXIS));

        m_NewButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        m_NewButton.setActionCommand("New");
        m_NewButton.addActionListener(this);

        m_RemoveButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        m_RemoveButton.setActionCommand("Remove");
        m_RemoveButton.addActionListener(this);

        m_EditButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        m_EditButton.setActionCommand("Edit");
        m_EditButton.addActionListener(this);


        m_ButtonPanel.add(m_NewButton);
        m_ButtonPanel.add(m_EditButton);
        m_ButtonPanel.add(Box.createHorizontalBox());
        m_ButtonPanel.add(m_RemoveButton);
        m_ButtonPanel.add(Box.createHorizontalGlue());

        getContentPane().add(m_ButtonPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand() == "New")
        {
//            Unit a = UnitManager.getInstance().GenerateUnit((Player)null, "Fixme!!!", (MechUnitParameters)null, Rating.GREEN);
//            UnitManager.RequestEdit(a);
//            SwingUtilities.invokeLater(new factorymanager.database.data.managers.UnitEditRequestor(a));
        }
        if (e.getActionCommand() == "Remove")
        {
            Unit a = m_UnitListPanel.GetSelectedUnit();
            if (a != null)
            {
                int Result = JOptionPane.showInternalConfirmDialog(this, "Are you sure you wish to delete this Unit?", "Remove Unit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (Result == JOptionPane.YES_OPTION)
                {
                    UnitManager.getInstance().deleteUnit(a.getName());
                }
            }
        }
        if (e.getActionCommand() == "Edit")
        {
            Unit a = m_UnitListPanel.GetSelectedUnit();
            if (a != null)
            {
//                UnitManager.RequestEdit(a);
            }
        }
    }

    public void UnitAdded(Unit a)
    {
    }

    public void UnitRemoved(Unit a)
    {

    }

    public void UnitChanged(Unit a)
    {

    }

    public void UnitEditRequested(Unit a)
    {
    }

}
