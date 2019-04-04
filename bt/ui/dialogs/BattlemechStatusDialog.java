package bt.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import bt.elements.Battlemech;
import bt.elements.personnel.Mechwarrior;
import bt.ui.panels.BattlemechStatusPanel;

public class BattlemechStatusDialog extends javax.swing.JDialog implements ActionListener 
{
	private static final String OK = "Ok";

	private static final String REPAIR_DAMAGE = "RepairDamage";

	private static final long serialVersionUID = 4477176044483816599L;

	private Battlemech _Mech;
	private JPanel _ButtonPanel;
	private JButton _RepairDamageButton;
	private JButton _OkButton;
	private JScrollPane _ScrollPane;
	private BattlemechStatusPanel _BattlemechStatusPanel;
	private boolean _DamageApplied = false;
	/**
	* Auto-generated main method to display this JDialog
	*/
		
	public BattlemechStatusDialog(Frame frame, Battlemech mech, Mechwarrior warrior) 
	{
		super(frame);
		_Mech = mech;
		initGUI(mech, warrior);
	}
	
	private void initGUI(Battlemech mech, Mechwarrior warrior) 
	{
		try 
		{
			getContentPane().setLayout(new BorderLayout());

			_ButtonPanel = new JPanel();
			_ButtonPanel.setLayout(new BoxLayout(_ButtonPanel, BoxLayout.X_AXIS));

			_ButtonPanel.add(Box.createHorizontalGlue());

			_RepairDamageButton = new JButton("Repair All Damage");
			_RepairDamageButton.setActionCommand(REPAIR_DAMAGE);
			_RepairDamageButton.addActionListener(this);
			_ButtonPanel.add(_RepairDamageButton);

			_ButtonPanel.add(Box.createHorizontalStrut(20));
			
			_OkButton = new JButton(OK);
			_OkButton.addActionListener(this);
			_OkButton.setActionCommand(OK);
			_ButtonPanel.add(_OkButton);

			getContentPane().add(_ButtonPanel, BorderLayout.SOUTH);

			_BattlemechStatusPanel = new BattlemechStatusPanel(mech, warrior, 0.8);
			_ScrollPane = new JScrollPane(_BattlemechStatusPanel);
			getContentPane().add(_ScrollPane, BorderLayout.CENTER);
			_ScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			_ScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			_ScrollPane.setAutoscrolls(true);
			setSize(1010, 600);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public boolean wasDamageApplied()
	{
		return _DamageApplied;
	}

    public void setBounds(int x, int y, int w, int h)
    {
    	if (_ScrollPane != null)
    	{
	        _ScrollPane.setPreferredSize(new Dimension(w,h-50));
	        _ScrollPane.setBounds(0,25,w,h-50);
    	}
        super.setBounds(x,y,w,h);
    }

    public void setSize(Dimension d)
    {
    	if (_ScrollPane != null)
    	{
	        _ScrollPane.setPreferredSize(new Dimension(d.width,d.height - 50));
	        _ScrollPane.setSize(new Dimension(d.width,d.height - 50));
    	}
        super.setSize(d);
    }


	@Override
	public void actionPerformed(ActionEvent ae) 
	{
		if (ae.getActionCommand().equals(OK))
		{
			_BattlemechStatusPanel.finaliseDamageNotation();
			_DamageApplied = true;
			this.setVisible(false);
		}
		if (ae.getActionCommand().equals(REPAIR_DAMAGE))
		{
			if (JOptionPane.showConfirmDialog(this, "Are you sure you want to repair all damage for this mech?","Repair Damage", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{
				_BattlemechStatusPanel.finaliseDamageNotation();
				_Mech.repairAllDamage();
				_BattlemechStatusPanel.refreshPanel();
			}
		}
	}

}
