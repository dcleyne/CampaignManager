package bt.ui.panels;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bt.elements.unit.Player;
import bt.util.SwingHelper;

public class PlayerDetailsPanel extends JPanel implements ClosableEditPanel, ActionListener
{
	private static final long serialVersionUID = 1; 
    protected Player m_Player = null;

    JPanel m_EditPanel = new JPanel();

    JTextField m_NameTextField = new JTextField();

    public PlayerDetailsPanel(Player a)
    {
        m_Player = a;

        m_EditPanel.setBorder(BorderFactory.createEtchedBorder());
        m_EditPanel.setLayout(new BoxLayout(m_EditPanel, BoxLayout.Y_AXIS));
        m_EditPanel.add(SwingHelper.GetTextField(m_NameTextField, "Name", "The Unit's Name",true));

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
        m_NameTextField.setText(m_Player.getName());

        m_NameTextField.requestFocus();
        m_NameTextField.selectAll();
    }

    protected void GetFields()
    {
        try
        {
            m_Player.setName(m_NameTextField.getText());
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }
}
