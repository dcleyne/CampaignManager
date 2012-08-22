package bt.ui;

import java.awt.BorderLayout;
import javax.swing.*;
import bt.managers.listeners.SolarSystemManagerListener;
import bt.ui.mapping.flatstarmap.FlatStarMapPanel;

/**
 * <p>Title: Miradan Phedd</p>
 *
 * <p>Description: Library for all Miradan related work</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Miradan Phedd</p>
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
public class FlatStarMapInternalFrame extends JInternalFrame implements ClosableEditFrame
{
	private static final long serialVersionUID = 1;
	
    private FlatStarMapPanel m_DetailsPanel;

    public FlatStarMapInternalFrame(String Title, SolarSystemManagerListener sspml)
    {
        super(Title,true,true,true,true);

        getContentPane().setLayout(new BorderLayout());

        m_DetailsPanel = new FlatStarMapPanel();
        getContentPane().add(m_DetailsPanel, BorderLayout.CENTER);

        m_DetailsPanel.addSolarSystemManagerListener(sspml);
    }

    public boolean IsClosable()
    {
        return true;
    }

    public void ForceEditCompletion()
    {
    }
}
