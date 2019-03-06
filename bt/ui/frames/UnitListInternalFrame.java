package bt.ui.frames;


import java.awt.BorderLayout;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import bt.elements.unit.Unit;
import bt.managers.UnitManager;
import bt.managers.listeners.UnitManagerListener;
import bt.ui.dialogs.CreateNewUnitDialog;
import bt.ui.listeners.UnitChangeListener;
import bt.ui.panels.UnitListPanel;

public class UnitListInternalFrame extends JInternalFrame implements ActionListener, UnitManagerListener
{
	private static final long serialVersionUID = 1;
	
    protected UnitListPanel _UnitListPanel = null;
    protected JPanel _ButtonPanel = new JPanel();

    protected JButton _NewButton = new JButton("New Unit");
    protected JButton _RemoveButton = new JButton("Remove Unit");
    protected JButton _EditButton = new JButton("Edit Unit");

    public UnitListInternalFrame(String Title, ArrayList<Unit> units, UnitChangeListener ucl)
    {
        super(Title, true, true, true, true);

        _UnitListPanel = new UnitListPanel(units);
        _UnitListPanel.addUnitChangeListener(ucl);
        
        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(_UnitListPanel, BorderLayout.CENTER);

        _ButtonPanel.setBorder(BorderFactory.createEtchedBorder());
        _ButtonPanel.setLayout(new BoxLayout(_ButtonPanel, BoxLayout.X_AXIS));

        _NewButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        _NewButton.setActionCommand("New");
        _NewButton.addActionListener(this);

        _RemoveButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        _RemoveButton.setActionCommand("Remove");
        _RemoveButton.addActionListener(this);

        _EditButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        _EditButton.setActionCommand("Edit");
        _EditButton.addActionListener(this);

        _ButtonPanel.add(_NewButton);
        _ButtonPanel.add(_EditButton);
        _ButtonPanel.add(Box.createHorizontalBox());
        _ButtonPanel.add(_RemoveButton);
        _ButtonPanel.add(Box.createHorizontalGlue());

        getContentPane().add(_ButtonPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand() == "New")
        {
        	final CreateNewUnitDialog dlg = new CreateNewUnitDialog();
        	dlg.setModal(true);
        	dlg.setVisible(true);
        	if (dlg.getCreatedUnit() != null)
        	{
        		SwingUtilities.invokeLater(new Runnable()
				{					
					@Override
					public void run()
					{
		        		_UnitListPanel.requestUnitEdit(dlg.getCreatedUnit());
					}
				});
        	}
        }
        if (e.getActionCommand() == "Remove")
        {
            Unit a = _UnitListPanel.GetSelectedUnit();
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
            Unit a = _UnitListPanel.GetSelectedUnit();
            if (a != null)
            {
        		SwingUtilities.invokeLater(new Runnable()
				{					
					@Override
					public void run()
					{
		        		_UnitListPanel.requestUnitEdit(a);
					}
				});
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
