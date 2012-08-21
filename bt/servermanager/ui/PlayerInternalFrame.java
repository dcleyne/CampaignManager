package bt.servermanager.ui;


import java.awt.BorderLayout;
import javax.swing.JInternalFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bt.common.elements.unit.Player;
import bt.common.ui.ClosableEditFrame;
import bt.servermanager.ui.forms.PlayerDetailsPanel;

public class PlayerInternalFrame extends JInternalFrame implements ClosableEditFrame, ChangeListener
{
	private static final long serialVersionUID = 1;
	
    protected Player m_Player = null;
    protected PlayerDetailsPanel _PlayerDetailsPanel;

    public PlayerInternalFrame(String Title, Player p)
    {
        super(Title, true, true, true, true);
        m_Player = p;
        getContentPane().setLayout(new BorderLayout());

        _PlayerDetailsPanel = new PlayerDetailsPanel(p);
        getContentPane().add(_PlayerDetailsPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public Player GetPlayer()
    {return m_Player;
    }

    public boolean IsClosable()
    {
    	return _PlayerDetailsPanel.IsClosable();
    }

    public void ForceEditCompletion()
    {
		_PlayerDetailsPanel.ForceEditCompletion();
    }

    public void stateChanged(ChangeEvent ce)
    {
        ForceEditCompletion();
    }

}
