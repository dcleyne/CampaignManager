package bt.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.toedter.calendar.JCalendar;

public class SelectDateDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = -4650013632156701447L;
	private JPanel _ButtonPanel;
	private JButton _CancelButton;
	private JButton _OkButton;
	private JCalendar _Calendar;
	
	private boolean _ValueSelected = false;
	
	public boolean wasValueSelected()
	{
		return _ValueSelected;
	}
	
	public SelectDateDialog(Date chosenDate)
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
		
		_Calendar = new JCalendar(chosenDate);
		getContentPane().add(_Calendar, BorderLayout.CENTER);
		
		setSize(250,250);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String actionCommand = e.getActionCommand();
		if (actionCommand.equalsIgnoreCase("Ok"))
		{
			_ValueSelected = true;
			setVisible(false);
		}
		if (actionCommand.equalsIgnoreCase("Cancel"))
		{
			setVisible(false);
		}		
	}
	
	public Date getValueSelected()
	{
		if (_ValueSelected)
		{
			return _Calendar.getDate();
		}
		return null;
	}
}
