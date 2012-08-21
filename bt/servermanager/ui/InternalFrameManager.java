package bt.servermanager.ui;

import java.awt.Color;

import java.awt.Dimension;
import java.util.Vector;


import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bt.common.elements.galaxy.InnerSpherePlanet;
import bt.common.elements.galaxy.SolarSystemDetails;
import bt.common.elements.unit.Player;
import bt.common.elements.unit.PlayerSummary;
import bt.common.elements.unit.Unit;
import bt.common.elements.unit.UnitSummary;
import bt.common.managers.PlanetManager;
import bt.common.managers.SolarSystemDetailCache;
import bt.common.managers.UnitCache;
import bt.common.ui.FlatStarMapInternalFrame;
import bt.common.ui.PlanetInternalFrame;
import bt.common.ui.UnitInternalFrame;
import bt.common.ui.UnitListInternalFrame;
import bt.common.ui.listeners.UnitChangeListener;
import bt.server.managers.listeners.PlanetManagerListener;
import bt.servermanager.ServerManager;
import bt.servermanager.managers.PlayerCache;
import bt.servermanager.ui.listeners.PlayerChangeListener;

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
public class InternalFrameManager implements InternalFrameListener, PlanetManagerListener, UnitChangeListener, PlayerChangeListener
{
    private static Log log = LogFactory.getLog(InternalFrameManager.class);

    private ServerManager m_Client;
    
    private JFrame m_ParentFrame;
    private JDesktopPane m_DesktopPane;

    private UnitListInternalFrame m_UnitListFrame;
    private PlayerListInternalFrame m_PlayerListFrame;
    private FlatStarMapInternalFrame m_FlatStarMapFrame;
    private Vector<UnitInternalFrame> m_UnitFrames = new Vector<UnitInternalFrame>();
    private Vector<PlayerInternalFrame> m_PlayerFrames = new Vector<PlayerInternalFrame>();
    private Vector<PlanetInternalFrame> m_PlanetFrames = new Vector<PlanetInternalFrame>();
    
    private Vector<Long> m_PlanetRequests = new Vector<Long>();
    private Vector<String> m_UnitRequests = new Vector<String>();
    private Vector<String> m_PlayerRequests = new Vector<String>();

    public InternalFrameManager(JFrame frame, JDesktopPane DesktopPane, ServerManager client)
    {
        m_ParentFrame = frame;
        m_DesktopPane = DesktopPane;
        
        m_Client = client;
        
        m_UnitListFrame = null;
    }

    public void ShowUnitListFrame()
    {
        try
        {
            if (m_UnitListFrame == null)
            {
                m_UnitListFrame = new UnitListInternalFrame("Unit List", UnitCache.getInstance().getUnits(),this);
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
    
    public void ShowPlayerListFrame()
    {
        try
        {
            if (m_PlayerListFrame == null)
            {
                m_PlayerListFrame = new PlayerListInternalFrame("Player List", PlayerCache.getInstance().getPlayerSummaries(),this);
                m_DesktopPane.add(m_PlayerListFrame);
                m_PlayerListFrame.setBounds(10, 10, 640, 480);
                m_PlayerListFrame.setVisible(true);
                m_PlayerListFrame.addInternalFrameListener(this);
            }
            m_PlayerListFrame.toFront();
        }
        catch (Exception e)
        {
            log.fatal("Failed to open Player List Internal Frame", e);
        }
    }
    
    public void ShowFlatStarMapFrame()
    {
    	try
    	{
            if (m_FlatStarMapFrame == null)
            {
	    		m_FlatStarMapFrame = new FlatStarMapInternalFrame("Inner Sphere", this);
	    		m_DesktopPane.add(m_FlatStarMapFrame);
	    		m_FlatStarMapFrame.setBounds(10, 10, 640, 480);
	    		m_FlatStarMapFrame.setVisible(true);
	    		m_FlatStarMapFrame.addInternalFrameListener(this);
            }   
            m_FlatStarMapFrame.toFront();
    	}
    	catch (Exception e)
    	{
    		log.fatal("Failed to open Star Map Internal Frame", e);
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
        if (jif.equals(m_FlatStarMapFrame))
        {
        	m_FlatStarMapFrame = null;
        }
        
        if (jif.equals(m_UnitListFrame))
        {
            m_UnitListFrame = null;
        }
        
        if (jif.equals(m_PlayerListFrame))
        {
            m_PlayerListFrame = null;
        } 
        	
        if (m_UnitFrames.contains(jif))
        {
            UnitInternalFrame aif = (UnitInternalFrame)jif;
            aif.ForceEditCompletion();
            m_UnitFrames.remove(jif);
        }
        
        if (m_PlayerFrames.contains(jif))
        {
            PlayerInternalFrame aif = (PlayerInternalFrame)jif;
            aif.ForceEditCompletion();
            m_PlayerFrames.remove(jif);
        }
        
        if (m_PlanetFrames.contains(jif))
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
	public void requestUnitEdit(String unitName) 
	{
		Unit u = UnitCache.getInstance().getUnit(unitName);
		if (u != null)
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
		else
		{
    		m_UnitRequests.add(unitName);
    		m_Client.requestUnitDetails(unitName);
		}
	}
	
    public void PlanetChanged(InnerSpherePlanet isp) {}
    public void PlanetEditRequest(InnerSpherePlanet isp)
    {
    	SolarSystemDetails ssd = SolarSystemDetailCache.getInstance().getDetails(isp.getID());
    	if (ssd != null)
    	{
    		m_PlanetRequests.remove(isp.getID());
    		
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
	    		CreateNewPlanetFrame(isp);
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
	            
	            m_DesktopPane.getDesktopManager().activateFrame(pif);
	        }
    	}
    	else
    	{
    		Long planetID = new Long(isp.getID());
    		m_PlanetRequests.add(planetID);
    		m_Client.requestSolarSystemDetails(planetID);
    	}
    }
    
    private void CreateNewPlanetFrame(InnerSpherePlanet isp)
    {
    	
    	SolarSystemDetails ssd = SolarSystemDetailCache.getInstance().getDetails(isp.getID());
    	
        PlanetInternalFrame pif = null;
        pif = new PlanetInternalFrame("Planet : " + isp.getSystem(), isp, ssd);
        m_DesktopPane.add(pif);
        pif.setBounds(10, 10, 640, 620);
        pif.addInternalFrameListener(this);
        m_PlanetFrames.add(pif);
        m_DesktopPane.getDesktopManager().activateFrame(pif);
    }

    public void CloseInternalFrames()
    {
        try
        {
            if (m_UnitListFrame != null)
                m_UnitListFrame.setClosed(true);
            if (m_PlayerListFrame != null)
                m_PlayerListFrame.setClosed(true);
            if (m_FlatStarMapFrame != null)
            	m_FlatStarMapFrame.setClosed(true);

            for (int i = 0; i < m_UnitFrames.size(); i++)
            {
                UnitInternalFrame uif = (UnitInternalFrame) m_UnitFrames.elementAt(i);
                uif.setClosed(true);
            }
            for (int i = 0; i < m_PlayerFrames.size(); i++)
            {
                PlayerInternalFrame pif = (PlayerInternalFrame) m_PlayerFrames.elementAt(i);
                pif.setClosed(true);
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

    public void showGlassPane(String message)
    {
        JPanel glass = (JPanel)((JFrame)JOptionPane.getFrameForComponent(m_ParentFrame)).getGlassPane();
        glass.removeAll();

        glass.setVisible(false);
        glass.removeAll();
        glass.setLayout(null);
        
        if (!message.equalsIgnoreCase(""))
        {
            JPanel sheet = new JPanel();
            sheet.setBorder(new LineBorder(Color.BLACK,1));
            sheet.setBackground(Color.BLUE);
            
            JLabel label = new JLabel(message);
            label.setForeground(Color.WHITE);
            sheet.add(label);
            sheet.validate();
            Dimension d = sheet.getPreferredSize();
            sheet.setSize(d);
            
            int x = (glass.getWidth() - d.width) / 2;
            int y = (glass.getHeight() - d.height)/ 2;
            glass.add(sheet);        
            sheet.setLocation(x,y);    
            sheet.setVisible(true);

        }
        glass.setVisible(true);        
    }

    public void hideGlassPane() 
    {
        JPanel glass = (JPanel)m_ParentFrame.getRootPane().getGlassPane();
        glass.setVisible(false);
        glass.removeAll();
        glass.setLayout(null);
    }
    
    public void registerSolarSystemDetails(Long id, SolarSystemDetails ssd)
    {
    	SolarSystemDetailCache.getInstance().putDetails(id, ssd);
    	if (m_PlanetRequests.contains(id))
    	{
    		PlanetEditRequest(PlanetManager.getPlanetFromID(id));
    	}
    }
    
    public void registerUnitList(Vector<UnitSummary> summaries)
    {
    	UnitCache.getInstance().setUnits(summaries);
    }
    
    public void registerUnitDetails(String unitName, Unit unit)
    {
    	UnitCache.getInstance().putUnit(unitName, unit);
    	if (m_UnitRequests.contains(unitName))
    	{
    		requestUnitEdit(unitName);
    	}
    }

    public void registerPlayerList(Vector<PlayerSummary> summaries)
    {
    	PlayerCache.getInstance().setPlayerSummaries(summaries);
    }
    
    public void registerPlayerDetails(String playerName, Player player)
    {
    	PlayerCache.getInstance().putPlayer(playerName, player);
    	if (m_PlayerRequests.contains(playerName))
    	{
    		requestPlayerEdit(playerName);
    	}
    }

	@Override
	public void requestPlayerEdit(String playerName) 
	{
		Player p = PlayerCache.getInstance().getPlayer(playerName);
		if (p != null)
		{
	        boolean found = false;
	        PlayerInternalFrame uif = null;
	        for (int i = 0; i < m_PlayerFrames.size(); i++)
	        {
	            uif = (PlayerInternalFrame)m_PlayerFrames.elementAt(i);
	            Player uif_a = uif.GetPlayer();
	            if (p.equals(uif_a))
	            {
	                found = true;
	                break;
	            }
	        }
	        if (!found)
	        {
	            uif = new PlayerInternalFrame("Player : " + p.getName(), p);
	            m_DesktopPane.add(uif);
	            uif.setBounds(10, 10, 640, 620);
	            uif.addInternalFrameListener(this);
	            m_PlayerFrames.add(uif);
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
		else
		{
    		m_PlayerRequests.add(playerName);
    		m_Client.requestPlayerDetails(playerName);
		}
	}
    
}
