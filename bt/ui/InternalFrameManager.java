package bt.ui;

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

import bt.elements.galaxy.InnerSpherePlanet;
import bt.elements.galaxy.SolarSystemDetails;
import bt.elements.unit.Player;
import bt.elements.unit.Unit;
import bt.managers.PlanetManager;
import bt.managers.PlayerManager;
import bt.managers.SolarSystemManager;
import bt.managers.UnitManager;
import bt.managers.listeners.SolarSystemManagerListener;
import bt.ui.frames.FlatStarMapInternalFrame;
import bt.ui.frames.PlanetInternalFrame;
import bt.ui.frames.PlayerInternalFrame;
import bt.ui.frames.PlayerListInternalFrame;
import bt.ui.frames.UnitInternalFrame;
import bt.ui.frames.UnitListInternalFrame;
import bt.ui.listeners.PlayerChangeListener;
import bt.ui.listeners.UnitChangeListener;

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
public class InternalFrameManager implements InternalFrameListener, SolarSystemManagerListener, UnitChangeListener, PlayerChangeListener
{
    private static Log log = LogFactory.getLog(InternalFrameManager.class);

    protected JFrame _ParentFrame;
    protected JDesktopPane _DesktopPane;

    protected UnitListInternalFrame _UnitListFrame;
    private PlayerListInternalFrame _PlayerListFrame;
    private FlatStarMapInternalFrame _FlatStarMapFrame;
    protected Vector<UnitInternalFrame> _UnitFrames = new Vector<UnitInternalFrame>();
    protected Vector<PlanetInternalFrame> _PlanetFrames = new Vector<PlanetInternalFrame>();
    private Vector<PlayerInternalFrame> _PlayerFrames = new Vector<PlayerInternalFrame>();
    
    private Vector<Long> _PlanetRequests = new Vector<Long>();
    private Vector<String> _UnitRequests = new Vector<String>();
    private Vector<String> _PlayerRequests = new Vector<String>();

    
    public InternalFrameManager(JFrame frame, JDesktopPane DesktopPane)
    {
        _ParentFrame = frame;
        _DesktopPane = DesktopPane;
        _UnitListFrame = null;
    }

    public void ShowUnitListFrame()
    {
        try
        {
            if (_UnitListFrame == null)
            {
                _UnitListFrame = new UnitListInternalFrame("Unit List", UnitManager.getInstance().getUnits(),this);
                _DesktopPane.add(_UnitListFrame);
                _UnitListFrame.setBounds(10, 10, 640, 480);
                _UnitListFrame.setVisible(true);
                _UnitListFrame.addInternalFrameListener(this);
            }
            _UnitListFrame.toFront();
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
            if (_PlayerListFrame == null)
            {
                _PlayerListFrame = new PlayerListInternalFrame("Player List", PlayerManager.getInstance().getPlayerSummaries(),this);
                _DesktopPane.add(_PlayerListFrame);
                _PlayerListFrame.setBounds(10, 10, 640, 480);
                _PlayerListFrame.setVisible(true);
                _PlayerListFrame.addInternalFrameListener(this);
            }
            _PlayerListFrame.toFront();
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
            if (_FlatStarMapFrame == null)
            {
	    		_FlatStarMapFrame = new FlatStarMapInternalFrame("Inner Sphere", this);
	    		_DesktopPane.add(_FlatStarMapFrame);
	    		_FlatStarMapFrame.setBounds(10, 10, 640, 480);
	    		_FlatStarMapFrame.setVisible(true);
	    		_FlatStarMapFrame.addInternalFrameListener(this);
            }   
            _FlatStarMapFrame.toFront();
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
        if (jif.equals(_UnitListFrame))
        {
            _UnitListFrame = null;
        } 
        
        if (jif.equals(_FlatStarMapFrame))
        {
        	_FlatStarMapFrame = null;
        }
        
        if (jif.equals(_PlayerListFrame))
        {
            _PlayerListFrame = null;
        }
        
        if (_UnitFrames.contains(jif))
        {
            UnitInternalFrame aif = (UnitInternalFrame)jif;
            aif.forceFrameEditCompletion();
            _UnitFrames.remove(jif);
        } 
        
        if (_PlanetFrames.contains(jif))
        {
            PlanetInternalFrame pif = (PlanetInternalFrame)jif;
            pif.forceFrameEditCompletion();
            _PlanetFrames.remove(jif);
        }

        if (_PlayerFrames.contains(jif))
        {
            PlayerInternalFrame aif = (PlayerInternalFrame)jif;
            aif.forceFrameEditCompletion();
            _PlayerFrames.remove(jif);
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
        for (int i = 0; i < _UnitFrames.size(); i++)
        {
            UnitInternalFrame uif = (UnitInternalFrame)_UnitFrames.elementAt(i);
            Unit uif_a = uif.GetUnit();
            if (u.equals(uif_a))
            {
                uif.forceFrameEditCompletion();
                _UnitFrames.remove(aif);
                _DesktopPane.remove(aif);
                uif.dispose();
                _ParentFrame.repaint();
            }
        }
    }

    public void UnitChanged(Unit u)
    {
        _ParentFrame.repaint();
    }

	public void requestUnitEdit(String unitName) 
	{
		Unit u = UnitManager.getInstance().getUnit(unitName);
		if (u != null)
		{
	        boolean found = false;
	        UnitInternalFrame uif = null;
	        for (int i = 0; i < _UnitFrames.size(); i++)
	        {
	            uif = (UnitInternalFrame)_UnitFrames.elementAt(i);
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
	            _DesktopPane.add(uif);
	            uif.setBounds(10, 10, 640, 620);
	            uif.addInternalFrameListener(this);
	            _UnitFrames.add(uif);
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
	        _DesktopPane.getDesktopManager().activateFrame(uif);
		}
		else
		{
    		_UnitRequests.add(unitName);
		}
	}
	
	public void PlanetChanged(InnerSpherePlanet isp) {}
	
	@Override
	public void PlanetEditRequest(InnerSpherePlanet isp, SolarSystemDetails ssd) 
	{
		// TODO Auto-generated method stub
    	if (ssd != null)
    	{
    		_PlanetRequests.remove(isp.getID());
    		
	        boolean found = false;
	        PlanetInternalFrame pif = null;
	        for (int i = 0; i < _PlanetFrames.size(); i++)
	        {
	            pif = (PlanetInternalFrame)_PlanetFrames.elementAt(i);
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
	            
	            _DesktopPane.getDesktopManager().activateFrame(pif);
	        }
    	}
    	else
    	{
    		Long planetID = new Long(isp.getID());
    		_PlanetRequests.add(planetID);
    	}
		
	}
	
    private void CreateNewPlanetFrame(InnerSpherePlanet isp)
    {
    	
    	SolarSystemDetails ssd = SolarSystemManager.getSystemDetails(isp);
    	
        PlanetInternalFrame pif = null;
        pif = new PlanetInternalFrame("Planet : " + isp.getSystem(), isp, ssd);
        _DesktopPane.add(pif);
        pif.setBounds(10, 10, 640, 620);
        pif.addInternalFrameListener(this);
        _PlanetFrames.add(pif);
        _DesktopPane.getDesktopManager().activateFrame(pif);
    }

    public void CloseInternalFrames()
    {
        try
        {
            if (_UnitListFrame != null)
                _UnitListFrame.setClosed(true);
            if (_PlayerListFrame != null)
                _PlayerListFrame.setClosed(true);
            if (_FlatStarMapFrame != null)
            	_FlatStarMapFrame.setClosed(true);
            
            for (int i = 0; i < _UnitFrames.size(); i++)
            {
                UnitInternalFrame uif = (UnitInternalFrame) _UnitFrames.elementAt(i);
                uif.setClosed(true);
            }
            for (int i = 0; i < _PlanetFrames.size(); i++)
            {
                PlanetInternalFrame pif = (PlanetInternalFrame) _PlanetFrames.elementAt(i);
                pif.setClosed(true);
            }
            for (int i = 0; i < _PlayerFrames.size(); i++)
            {
                PlayerInternalFrame pif = (PlayerInternalFrame) _PlayerFrames.elementAt(i);
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
        JPanel glass = (JPanel)((JFrame)JOptionPane.getFrameForComponent(_ParentFrame)).getGlassPane();
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
        JPanel glass = (JPanel)_ParentFrame.getRootPane().getGlassPane();
        glass.setVisible(false);
        glass.removeAll();
        glass.setLayout(null);
    }
    
    public void registerSolarSystemDetails(Long id, SolarSystemDetails ssd)
    {
    	if (_PlanetRequests.contains(id))
    	{
    		PlanetEditRequest(PlanetManager.getPlanetFromID(id), ssd);
    	}
    }
    
    public void registerUnitDetails(String unitName, Unit unit)
    {
    	if (_UnitRequests.contains(unitName))
    	{
    		requestUnitEdit(unitName);
    	}
    }

    public void registerPlayerDetails(String playerName, Player player)
    {
    	if (_PlayerRequests.contains(playerName))
    	{
    		requestPlayerEdit(playerName);
    	}
    }

	@Override
	public void requestPlayerEdit(String playerName) 
	{
		Player p = PlayerManager.getInstance().getPlayer(playerName);
		if (p != null)
		{
	        boolean found = false;
	        PlayerInternalFrame uif = null;
	        for (int i = 0; i < _PlayerFrames.size(); i++)
	        {
	            uif = (PlayerInternalFrame)_PlayerFrames.elementAt(i);
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
	            _DesktopPane.add(uif);
	            uif.setBounds(10, 10, 640, 620);
	            uif.addInternalFrameListener(this);
	            _PlayerFrames.add(uif);
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
	        _DesktopPane.getDesktopManager().activateFrame(uif);
		}
		else
		{
    		_PlayerRequests.add(playerName);
		}
	}


}
