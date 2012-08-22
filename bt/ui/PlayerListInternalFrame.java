package bt.ui;


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

import bt.elements.unit.PlayerSummary;
import bt.ui.forms.PlayerListPanel;
import bt.ui.listeners.PlayerChangeListener;

public class PlayerListInternalFrame extends JInternalFrame implements ActionListener
{
	private static final long serialVersionUID = 1;
	
    protected PlayerListPanel m_PlayerListPanel = null;
    protected JPanel m_ButtonPanel = new JPanel();

    protected JButton m_NewButton = new JButton("New Player");
    protected JButton m_RemoveButton = new JButton("Remove Player");
    protected JButton m_EditButton = new JButton("Edit Player");

    public PlayerListInternalFrame(String Title, Vector<PlayerSummary> Players, PlayerChangeListener ucl)
    {
        super(Title, true, true, true, true);

        m_PlayerListPanel = new PlayerListPanel(Players);
        m_PlayerListPanel.addPlayerChangeListener(ucl);
        
        getContentPane().setLayout(new BorderLayout());

        //m_PlayerListPanel.setFocusable(false);
        getContentPane().add(m_PlayerListPanel, BorderLayout.CENTER);

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
//            Player a = PlayerManager.getInstance().GeneratePlayer((Player)null, "Fixme!!!", (MechPlayerParameters)null, Rating.GREEN);
//            PlayerManager.RequestEdit(a);
//            SwingUtilities.invokeLater(new factorymanager.database.data.managers.PlayerEditRequestor(a));
        }
        if (e.getActionCommand() == "Remove")
        {
            PlayerSummary a = m_PlayerListPanel.GetSelectedPlayer();
            if (a != null)
            {
                int Result = JOptionPane.showInternalConfirmDialog(this, "Are you sure you wish to delete this Player?", "Remove Player", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (Result == JOptionPane.YES_OPTION)
                {
                    //TODO 
                	// PlayerCache.getInstance().deletePlayer(a.getName());
                }
            }
        }
        if (e.getActionCommand() == "Edit")
        {
            PlayerSummary a = m_PlayerListPanel.GetSelectedPlayer();
            if (a != null)
            {
                m_PlayerListPanel.requestPlayerEdit(a.getName());
            }
        }
    }

}
