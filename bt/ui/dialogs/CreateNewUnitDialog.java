package bt.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import bt.elements.unit.Unit;
import bt.ui.panels.CreateNewUnitPanel;

public class CreateNewUnitDialog extends JDialog implements ActionListener
{
	private static final String CANCEL = "Cancel";
	private static final String OK = "Ok";
	private static final long serialVersionUID = 1L;

	private CreateNewUnitPanel _CreateNewUnitPanel = new CreateNewUnitPanel();
	private Unit _CreatedUnit;
	
	public CreateNewUnitDialog() 
	{
		JPanel contentPane = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		
		JButton okButton = new JButton(OK);
		okButton.addActionListener(this);
		buttonPanel.add(okButton);
		
		buttonPanel.add(Box.createHorizontalStrut(5));
		
		JButton cancelButton = new JButton(CANCEL);
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);
		
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		contentPane.add(_CreateNewUnitPanel, BorderLayout.CENTER);
		
		setContentPane(contentPane);
		
		initialise();
		pack();
	}

	private void initialise()
	{
		
	}
	
	public Unit getCreatedUnit()
	{
		return _CreatedUnit;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(OK))
		{
			_CreateNewUnitPanel.saveUnit();
			_CreatedUnit = _CreateNewUnitPanel.getCreatedUnit();
			setVisible(false);
		}
		if (e.getActionCommand().equals(CANCEL))
		{
			setVisible(false);
		}
	}
}
