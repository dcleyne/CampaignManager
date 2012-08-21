package bt.client.ui;

import java.util.Vector;


import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bt.client.ui.listeners.UnitChangeListener;
import bt.common.elements.galaxy.InnerSpherePlanet;
import bt.common.elements.galaxy.SolarSystemDetails;
import bt.common.elements.unit.Unit;
import bt.common.managers.UnitManager;

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
public class InternalFrameManager implements InternalFrameListener, UnitChangeListener
{
    private static Log log = LogFactory.getLog(InternalFrameManager.class);

    protected JFrame m_ParentFrame;
    protected JDesktopPane m_DesktopPane;

    protected UnitListInternalFrame m_UnitListFrame;
    protected Vector<UnitInternalFrame> m_UnitFrames = new Vector<UnitInternalFrame>();
    protected Vector<PlanetInternalFrame> m_PlanetFrames = new Vector<PlanetInternalFrame>();

    public InternalFrameManager(JFrame frame, JDesktopPane DesktopPane)
    {
        m_ParentFrame = frame;
        m_DesktopPane = DesktopPane;
        m_UnitListFrame = null;
    }

    public void ShowUnitListFrame()
    {
        try
        {
            if (m_UnitListFrame == null)
            {
                m_UnitListFrame = new UnitListInternalFrame("Unit List", UnitManager.getInstance().getUnits(),this);
                m_DesktopPane.add(m_UnitListFrame);
                m_UnitListFrame.setBounds(10, 10, 640, 480);
                m_UnitListFrame.setVisible(true);
                m_UnitListFrame.addInternalFrameListener(this);
            }
            m_UnitListFrame.toFront();
        }
        catch (Exception e)
        {
            log.fatal("Failed to open Unit List Internal Frame", e);
        }
    }

    public void internalFrameActivated(InternalFrameEvent e)
    {
    }

    public void internalFrameDeactivated(InternalFrameEvent e)
    {
    }

    public void internalFrameIconified(InternalFrameEvent e)
    {
    }

    public void internalFrameDeiconified(InternalFrameEvent e)
    {
    }

    public void internalFrameClosing(InternalFrameEvent e)
    {
        JInternalFrame jif = e.getInternalFrame();
        if (jif.equals(m_UnitListFrame))
        {
            m_UnitListFrame = null;
        } else if (m_UnitFrames.contains(jif))
        {
            UnitInternalFrame aif = (UnitInternalFrame)jif;
            aif.ForceEditCompletion();
            m_UnitFrames.remove(jif);
        } else if (m_PlanetFrames.contains(jif))
        {
            PlanetInternalFrame pif = (PlanetInternalFrame)jif;
            pif.ForceEditCompletion();
            m_PlanetFrames.remove(jif);
        }
    }

    public void internalFrameClosed(InternalFrameEvent e)
    {
    }

    public void internalFrameOpened(InternalFrameEvent e)
    {
    }

    public void UnitAdded(Unit u)
    {
    }

    public void UnitRemoved(Unit u)
    {
        UnitInternalFrame aif = null;
        for (int i = 0; i < m_UnitFrames.size(); i++)
        {
            UnitInternalFrame uif = (UnitInternalFrame)m_UnitFrames.elementAt(i);
            Unit uif_a = uif.GetUnit();
            if (u.equals(uif_a))
            {
                uif.ForceEditCompletion();
                m_UnitFrames.remove(aif);
                m_DesktopPane.remove(aif);
                uif.dispose();
                m_ParentFrame.repaint();
            }
        }
    }

    public void UnitChanged(Unit u)
    {
        m_ParentFrame.repaint();
    }

	@Override
	public void requestEdit(Unit u) 
	{
        boolean found = false;
        UnitInternalFrame uif = null;
        for (int i = 0; i < m_UnitFrames.size(); i++)
        {
            uif = (UnitInternalFrame)m_UnitFrames.elementAt(i);
            Unit uif_a = uif.GetUnit();
            if (u.equals(uif_a))
            {
                found = true;
                break;
            }
        }
        if (!found)
        {
            uif = new UnitInternalFrame("Unit : " + u.getName(), u);
            m_DesktopPane.add(uif);
            uif.setBounds(10, 10, 640, 620);
            uif.addInternalFrameListener(this);
            m_UnitFrames.add(uif);
        }
        else
        {
            if (uif.isIcon())
            {
                try
                {
                    uif.setIcon(false);
                }
                catch (Exception e)
                {
                    log.warn("Exception changing internal frame out of Icon mode", e);
                }
            }

        }
        m_DesktopPane.getDesktopManager().activateFrame(uif);
	}
	
    public void PlanetChanged(InnerSpherePlanet isp) {}
    public void PlanetEditRequest(InnerSpherePlanet isp, SolarSystemDetails ssd)
    {
        boolean found = false;
        PlanetInternalFrame pif = null;
        for (int i = 0; i < m_PlanetFrames.size(); i++)
        {
            pif = (PlanetInternalFrame)m_PlanetFrames.elementAt(i);
            InnerSpherePlanet pif_a = pif.GetPlanet();
            if (isp.equals(pif_a))
            {
                found = true;
                break;
            }
        }
        if (!found)
        {
            pif = new PlanetInternalFrame("Planet : " + isp.getSystem(), isp, ssd);
            m_DesktopPane.add(pif);
            pif.setBounds(10, 10, 640, 620);
            pif.addInternalFrameListener(this);
            m_PlanetFrames.add(pif);
        }
        else
        {
            if (pif.isIcon())
            {
                try
                {
                    pif.setIcon(false);
                }
                catch (Exception e)
                {
                    log.warn("Exception changing internal frame out of Icon mode", e);
                }
            }

        }
        m_DesktopPane.getDesktopManager().activateFrame(pif);
//        SwingUtilities.invokeLater(new FrameFocusRequestor(aif));
    }

    public void CloseInternalFrames()
    {
        try
        {
            if (m_UnitListFrame != null)
                m_UnitListFrame.setClosed(true);

            for (int i = 0; i < m_UnitFrames.size(); i++)
            {
                UnitInternalFrame uif = (UnitInternalFrame) m_UnitFrames.elementAt(i);
                uif.setClosed(true);
            }
            for (int i = 0; i < m_PlanetFrames.size(); i++)
            {
                PlanetInternalFrame pif = (PlanetInternalFrame) m_PlanetFrames.elementAt(i);
                pif.setClosed(true);
            }
        }
        catch (Exception e)
        {
            log.debug("Failed to close an internal frame: " + e);
        }
    }


}
