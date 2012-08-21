package bt.common.ui;

import javax.swing.*;

import bt.common.elements.galaxy.InnerSpherePlanet;
import bt.common.elements.galaxy.SolarSystemDetails;
import bt.common.ui.ClosableEditFrame;
import bt.common.ui.forms.PlanetDetailsPanel;
import bt.common.ui.mapping.planetmap.PlanetPanel;

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
    private JTabbedPane m_TabPane;
    private PlanetPanel m_MapPanel;
    private PlanetDetailsPanel m_DetailsPanel;

    public PlanetInternalFrame(String Title, InnerSpherePlanet isp, SolarSystemDetails ssd)
    {
        super(Title,true,true,true,true);
        m_Planet = isp;

        m_DetailsPanel = new PlanetDetailsPanel(isp,ssd);
        m_MapPanel = new PlanetPanel(ssd,24);
        JScrollPane scrollPane = new JScrollPane(m_MapPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        m_TabPane = new JTabbedPane();
        m_TabPane.addTab("Details", m_DetailsPanel);
        m_TabPane.addTab("Map", scrollPane);
        
        getContentPane().add(m_TabPane);

        setVisible(true);
    }

    public InnerSpherePlanet GetPlanet()
    { return m_Planet; }

    public boolean IsClosable()
    {
        boolean Closable = m_DetailsPanel.IsClosable();
        return Closable;
    }

    public void ForceEditCompletion()
    {
        m_DetailsPanel.ForceEditCompletion();
    }
}
