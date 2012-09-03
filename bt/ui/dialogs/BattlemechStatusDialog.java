package bt.ui.dialogs;

import bt.elements.Battlemech;
import bt.ui.panels.BattlemechStatusPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class BattlemechStatusDialog extends javax.swing.JDialog implements ActionListener 
{
	private static final long serialVersionUID = 4477176044483816599L;

	private JPanel _ButtonPanel;
	private JButton _CancelButton;
	private JButton _OkButton;
	private JScrollPane _ScrollPane;
	private BattlemechStatusPanel _BattlemechStatusPanel;
	private boolean _DamageApplied = false;
	/**
	* Auto-generated main method to display this JDialog
	*/
		
	public BattlemechStatusDialog(Frame frame, Battlemech mech) {
		super(frame);
		initGUI(mech);
	}
	
	private void initGUI(Battlemech mech) 
	{
		try 
		{
			BorderLayout thisLayout = new BorderLayout();
			getContentPane().setLayout(thisLayout);
			{
				_ButtonPanel = new JPanel();
				getContentPane().add(_ButtonPanel, BorderLayout.SOUTH);
				FlowLayout _ButtonPanelLayout = new FlowLayout();
				_ButtonPanelLayout.setAlignment(FlowLayout.RIGHT);
				_ButtonPanel.setLayout(_ButtonPanelLayout);
				{
					_OkButton = new JButton();
					_OkButton.addActionListener(this);
					_OkButton.setActionCommand("Ok");
					_ButtonPanel.add(_OkButton);
					_OkButton.setText("Ok");
				}
				{
					_CancelButton = new JButton();
					_CancelButton.setActionCommand("Cancel");
					_CancelButton.addActionListener(this);
					_ButtonPanel.add(_CancelButton);
					_CancelButton.setText("Cancel");
				}
			}
			{
				_BattlemechStatusPanel = new BattlemechStatusPanel(mech, 0.8);
				_ScrollPane = new JScrollPane(_BattlemechStatusPanel);
				getContentPane().add(_ScrollPane, BorderLayout.CENTER);
				_ScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				_ScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				_ScrollPane.setAutoscrolls(true);
				
			}
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
		if (ae.getActionCommand().equals("Ok"))
		{
			Battlemech mech = _BattlemechStatusPanel.getBattlemech();
			mech.applyDamage(_BattlemechStatusPanel.finaliseDamageNotation());
			_DamageApplied = true;
			this.setVisible(false);
		}
		if (ae.getActionCommand().equals("Cancel"))
		{
			this.setVisible(false);
		}
	}

}
