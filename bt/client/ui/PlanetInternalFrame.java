package bt.client.ui;

import javax.swing.*;

import bt.client.ui.ClosableEditFrame;
import bt.client.ui.forms.PlanetDetailsPanel;
import bt.common.elements.galaxy.InnerSpherePlanet;
import bt.common.elements.galaxy.SolarSystemDetails;

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

    public PlanetInternalFrame(String Title, InnerSpherePlanet isp, SolarSystemDetails ssd)
    {
        super(Title,true,true,true,true);
        m_Planet = isp;

        m_DetailsPanel = new PlanetDetailsPanel(isp,ssd);
        getContentPane().add(m_DetailsPanel);

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
