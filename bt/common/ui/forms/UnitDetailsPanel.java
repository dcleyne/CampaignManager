package bt.common.ui.forms;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bt.common.elements.unit.Unit;
import bt.common.ui.ClosableEditForm;
import bt.common.util.SwingHelper;

public class UnitDetailsPanel extends JPanel implements ClosableEditForm, ActionListener
{
	private static final long serialVersionUID = 1; 
    protected Unit m_Unit = null;

    JPanel m_EditPanel = new JPanel();

    JTextField m_NameTextField = new JTextField();
    JTextField m_DateEstablishedTextField = new JTextField();
    JTextArea m_NotesTextArea = new JTextArea();

    public UnitDetailsPanel(Unit a)
    {
        m_Unit = a;

        m_EditPanel.setBorder(BorderFactory.createEtchedBorder());
        m_EditPanel.setLayout(new BoxLayout(m_EditPanel, BoxLayout.Y_AXIS));
        m_EditPanel.add(SwingHelper.GetTextField(m_NameTextField, "Name", "The Unit's Name",true));
        m_EditPanel.add(SwingHelper.GetTextField(m_DateEstablishedTextField, "Date Established", "The Unit's Formation date",true));
        m_EditPanel.add(SwingHelper.GetTextArea(m_NotesTextArea, "Notes", "Notes for this Unit",true));

        this.setLayout(new BorderLayout());
        this.add(m_EditPanel, BorderLayout.CENTER);

        setVisible(true);
        SetFields();
    }

    public void actionPerformed(ActionEvent e)
    {
    }

    public boolean IsClosable()
    {
        return true;
    }

    public void ForceEditCompletion()
    {
        GetFields();
    }

    protected void SetFields()
    {
        m_NameTextField.setText(m_Unit.getName());
        m_DateEstablishedTextField.setText(SwingHelper.FormatDate(m_Unit.getEstablishDate()));
        m_NotesTextArea.setText(m_Unit.getNotes());

        m_NameTextField.requestFocus();
        m_NameTextField.selectAll();
    }

    protected void GetFields()
    {
        try
        {
            m_Unit.setName(m_NameTextField.getText());
            m_Unit.setEstablishDate(SwingHelper.GetDateFromString(m_DateEstablishedTextField.getText()));
            m_Unit.setNotes(m_NotesTextArea.getText());
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }
}
