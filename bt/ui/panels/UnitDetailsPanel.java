package bt.ui.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bt.elements.unit.Unit;
import bt.managers.UnitManager;
import bt.ui.dialogs.SelectDateDialog;
import bt.util.SwingHelper;

public class UnitDetailsPanel extends JPanel implements ClosableEditPanel, ActionListener
{
	private static final long serialVersionUID = 1; 
    protected Unit _Unit = null;

    JPanel _EditPanel = new JPanel();
    JPanel _ButtonPanel = new JPanel();

    JTextField _NameTextField = new JTextField();
    JTextField _CurrentDateTextField = new JTextField();
    JTextField _DateEstablishedTextField = new JTextField();
    JTextArea _NotesTextArea = new JTextArea();

    public UnitDetailsPanel(Unit a)
    {
        _Unit = a;

        _EditPanel.setBorder(BorderFactory.createEtchedBorder());
        _EditPanel.setLayout(new BoxLayout(_EditPanel, BoxLayout.Y_AXIS));
        _EditPanel.add(SwingHelper.GetTextField(_NameTextField, "Name", "The Unit's Name",true));
        _EditPanel.add(SwingHelper.GetTextFieldWithAction(_DateEstablishedTextField, "Date Established", "The Unit's Formation date",true,"SetEstablishDate","Set the date the unit was established",this));
        _EditPanel.add(SwingHelper.GetTextFieldWithAction(_CurrentDateTextField, "Current Date", "The current date for the unit",true,"SetCurrentDate","Set the current date",this));
        _EditPanel.add(SwingHelper.GetTextArea(_NotesTextArea, "Notes", "Notes for this Unit",true));

        setLayout(new BorderLayout());
        add(_EditPanel, BorderLayout.CENTER);
        
        
        JButton exportSummaryButton = new JButton("Export Summary");
        exportSummaryButton.setActionCommand("ExportSummary");
        exportSummaryButton.addActionListener(this);
        _ButtonPanel.setLayout(new BoxLayout(_ButtonPanel, BoxLayout.LINE_AXIS));
        _ButtonPanel.add(Box.createHorizontalGlue());
        _ButtonPanel.add(exportSummaryButton);
        add(_ButtonPanel, BorderLayout.SOUTH);

        setVisible(true);
        setFields();
    }

    public void actionPerformed(ActionEvent e)
    {
    	String actionCommand = e.getActionCommand();
    	if (actionCommand.equalsIgnoreCase("SetCurrentDate"))
    	{
    		SelectDateDialog dlg = new SelectDateDialog(_Unit.getCurrentDate());
    		dlg.setLocationRelativeTo(this);
    		dlg.setModal(true);
    		dlg.setVisible(true);
    		
    		if (dlg.wasValueSelected())
    		{
    			getFields();
    			_Unit.setCurrentDate(dlg.getValueSelected());
    			if (_Unit.getEstablishDate() == null)
    				_Unit.setEstablishDate(_Unit.getCurrentDate());
    			else
    			{
	    			if (_Unit.getCurrentDate().compareTo(_Unit.getEstablishDate()) < 0)
	    				_Unit.setCurrentDate(_Unit.getEstablishDate());
    			}	
    			setFields();
    		}
    	}
    	if (actionCommand.equalsIgnoreCase("SetEstablishDate"))
    	{
    		SelectDateDialog dlg = new SelectDateDialog(_Unit.getEstablishDate());
    		dlg.setLocationRelativeTo(this);
    		dlg.setModal(true);
    		dlg.setVisible(true);
    		
    		if (dlg.wasValueSelected())
    		{
    			getFields();
    			_Unit.setEstablishDate(dlg.getValueSelected());
    			if (_Unit.getCurrentDate() == null || _Unit.getCurrentDate().compareTo(_Unit.getEstablishDate()) < 0)
    				_Unit.setCurrentDate(_Unit.getEstablishDate());

    			setFields();
    		}
    	}
    	if (actionCommand.equalsIgnoreCase("ExportSummary"))
    	{
    		try
    		{
    			UnitManager.getInstance().printUnitSummaryToPDF(_Unit);
    		}
    		catch (Exception ex)
    		{
    			ex.printStackTrace();
    		}
    	}
    }

    public boolean isClosable()
    {
        return true;
    }

    public void forceEditCompletion()
    {
        getFields();
    }

    protected void setFields()
    {
        _NameTextField.setText(_Unit.getName());
        _CurrentDateTextField.setText(SwingHelper.FormatDate(_Unit.getCurrentDate()));
        _DateEstablishedTextField.setText(SwingHelper.FormatDate(_Unit.getEstablishDate()));
        _NotesTextArea.setText(_Unit.getNotes());

        _NameTextField.requestFocus();
        _NameTextField.selectAll();
    }

    protected void getFields()
    {
        try
        {
            _Unit.setName(_NameTextField.getText());
            _Unit.setEstablishDate(SwingHelper.GetDateFromString(_DateEstablishedTextField.getText()));
            _Unit.setCurrentDate(SwingHelper.GetDateFromString(_CurrentDateTextField.getText()));
            _Unit.setNotes(_NotesTextArea.getText());
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }
}
