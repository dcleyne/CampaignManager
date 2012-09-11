package bt.ui.frames;

import javax.swing.*;
import bt.elements.galaxy.InnerSpherePlanet;
import bt.elements.galaxy.SolarSystemDetails;
import bt.ui.frames.ClosableEditFrame;
import bt.ui.panels.PlanetDetailsPanel;
import bt.ui.panels.PlanetPanel;

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
public class PlanetInternalFrame extends JInternalFrame implements ClosableEditFrame
{
	private static final long serialVersionUID = 1;
	
    private InnerSpherePlanet m_Planet;
    private PlanetDetailsPanel m_DetailsPanel;
    private PlanetPanel m_MapPanel;

    public PlanetInternalFrame(String Title, InnerSpherePlanet isp, SolarSystemDetails ssd)
    {
        super(Title,true,true,true,true);
        m_Planet = isp;

        m_DetailsPanel = new PlanetDetailsPanel(isp,ssd);
        m_MapPanel = new PlanetPanel(ssd,24);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        JScrollPane mapScrollPane = new JScrollPane(m_MapPanel);
        mapScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        mapScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        tabbedPane.addTab("Details", m_DetailsPanel);
        tabbedPane.addTab("Map", mapScrollPane);
        
        getContentPane().add(tabbedPane);

        setVisible(true);
    }

    public InnerSpherePlanet GetPlanet()
    { return m_Planet; }

    public boolean isFrameClosable()
    {
        boolean Closable = m_DetailsPanel.isClosable();
        return Closable;
    }

    public void forceFrameEditCompletion()
    {
        m_DetailsPanel.forceEditCompletion();
    }
}
