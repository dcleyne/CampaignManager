/**
 * Created on 25/11/2005
 * <p>Title: Miradan Phedd</p>
 *
 * <p>Description: Library for all Miradan related work</p>
 *
 * <p>Copyright: Copyright (c) 2004-2005</p>
 *
 * <p>Company: Miradan Phedd</p>
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
package bt.servermanager.ui;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.DefaultDesktopManager;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JPopupMenu.Separator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bt.common.elements.galaxy.SolarSystemDetails;
import bt.common.elements.unit.Player;
import bt.common.elements.unit.PlayerSummary;
import bt.common.elements.unit.Unit;
import bt.common.elements.unit.UnitSummary;
import bt.common.managers.PasswordManager;
import bt.common.net.*;
import bt.common.ui.GlassPane;
import bt.common.util.SwingWorker;
import bt.servermanager.ServerManager;
import bt.servermanager.ServerManagerListener;
import bt.servermanager.ui.ConnectionDialog;

public class ServerManagerFrame extends JFrame implements ActionListener, ServerManagerListener
{
    private static final long serialVersionUID = 1;
    
    private final String m_Title = "Campaign Manager Server Manager";
    private static Log log = LogFactory.getLog(ServerManagerFrame.class);
    
    private JDesktopPane m_MainPane = new JDesktopPane();
    private InternalFrameManager m_FrameManager = null;
    
    private ServerManager m_Client;
    private JMenuItem m_ConnectMenuItem = null;
    private JMenuItem m_DisconnectMenuItem = null;
    
    private Separator m_ServerSeperator = new Separator();
    private JMenu m_ServerMenu;
    private JMenu m_ServerOperations;
    
    
    // Declarations
    //
    // Main menubar
    private JMenuBar menuMain;

    // "File" menu
//    JMenuItem menuitemNew;
    // "Help" menu
    private JMenuItem menuitemAbout;
    private ImageIcon iconAbout;

    public ServerManagerFrame(ServerManager client)
    {
        // Initialize
        //
        m_Client = client;
        m_Client.addListener(this);
        
        m_FrameManager = new InternalFrameManager(this, m_MainPane, m_Client);
        
        // Common images for menubar and toolbar
        LoadCommonImages();
        // Frame
        setTitle(m_Title);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new ServerStatusPanel(m_Client),BorderLayout.NORTH);
        
        m_MainPane.setDesktopManager(new DefaultDesktopManager());
        getContentPane().add(m_MainPane,BorderLayout.CENTER);
        
        setGlassPane(new GlassPane());
        
        
        //Center the window
        setSize(1024, 768);

        setLocationRelativeTo(null);
        
        // Menu
        menuMain = new JMenuBar();
        setJMenuBar(menuMain);
        menuAddItems(menuMain);
        // Panels


        // Register listeners
        //
        // Window listener
        EvtListener elWindow = new EvtListener();
        this.addWindowListener(elWindow);        
    }
    
    protected void LoadCommonImages()
    {
        iconAbout = new ImageIcon("images/about.gif");
    }


    protected void menuAddItems(JMenuBar hmenu)
    {
        JMenu menu;
        JMenuItem item;
        // Menu and toolbar listener
        //
        //"Tasks" menu
        m_ServerMenu = new JMenu("Server");
        m_ServerMenu.setActionCommand("Server");
        m_ServerMenu.setBorderPainted(false);
        hmenu.add(m_ServerMenu);
        // "StaticData" item
        m_ConnectMenuItem = new JMenuItem("Connect to Server");
        m_ConnectMenuItem.setHorizontalTextPosition(SwingConstants.RIGHT);
        m_ConnectMenuItem.setActionCommand("ServerConnect");
        m_ConnectMenuItem.setBorderPainted(false);
        m_ConnectMenuItem.setMnemonic( (int)'S');
        m_ConnectMenuItem.addActionListener(this);
        m_ServerMenu.add(m_ConnectMenuItem);

        m_DisconnectMenuItem = new JMenuItem("Disconnect from Server");
        m_DisconnectMenuItem.setHorizontalTextPosition(SwingConstants.RIGHT);
        m_DisconnectMenuItem.setActionCommand("ServerDisconnect");
        m_DisconnectMenuItem.setBorderPainted(false);
        m_DisconnectMenuItem.setMnemonic( (int)'D');
        m_DisconnectMenuItem.addActionListener(this);
        m_DisconnectMenuItem.setEnabled(false);
        m_ServerMenu.add(m_DisconnectMenuItem);
        
        //
        //"Tasks" menu
        menu = new JMenu("Task");
        menu.setActionCommand("Task");
        menu.setBorderPainted(false);
        menu.setMnemonic( (int)'T');
        hmenu.add(menu);
        // "StaticData" item
        item = new JMenuItem("View Map");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand("ViewMap");
        item.setBorderPainted(false);
        item.setMnemonic( (int)'M');
        item.addActionListener(this);
        menu.add(item);
        // "StaticData" item
        item = new JMenuItem("View Players");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand("ViewPlayers");
        item.setBorderPainted(false);
        item.setMnemonic( (int)'P');
        item.addActionListener(this);
        menu.add(item);
        // "StaticData" item
        item = new JMenuItem("View Units");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand("ViewUnits");
        item.setBorderPainted(false);
        item.setMnemonic( (int)'V');
        item.addActionListener(this);
        menu.add(item);

        //
        // "Setup" menu
        //
        menu = new JMenu("Setup");
        menu.setActionCommand("Setup");
        menu.setBorderPainted(false);
        menu.setMnemonic( (int)'E');
        hmenu.add(menu);
        // "StaticData" item
        item = new JMenuItem("Preferences");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand("Preferences");
        item.setBorderPainted(false);
        item.setMnemonic( (int)'P');
        item.addActionListener(this);
        menu.add(item);

        //
        // "Help" menu
        //
        menu = new JMenu("Help");
        menu.setActionCommand("Help");
        menu.setBorderPainted(false);
        menu.setMnemonic( (int)'H');
        hmenu.add(menu);
        // "About" item
        menuitemAbout = new JMenuItem("About...");
        menuitemAbout.setHorizontalTextPosition(SwingConstants.RIGHT);
        menuitemAbout.setActionCommand("About...");
        menuitemAbout.setBorderPainted(false);
        menuitemAbout.setMnemonic( (int)'A');
        menuitemAbout.setIcon(iconAbout);
        menuitemAbout.addActionListener(this);
        menu.add(menuitemAbout);
        
        
        m_ServerOperations = new JMenu("Operations");
        m_ServerOperations.setActionCommand("Operations");
        m_ServerOperations.setBorderPainted(false);
        m_ServerOperations.setMnemonic( (int)'O');
        
        item = new JMenuItem("Shut Down");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand("ServerShutDown");
        item.setBorderPainted(false);
        item.setMnemonic( (int)'X');
        item.addActionListener(this);
        m_ServerOperations.add(item);

    }

    // Used by addNotify
    boolean addNotify_done = false;
    
    public InternalFrameManager getInternalFrameManager()
    {
    	return m_FrameManager;
    }

    /**
     * Makes this Container displayable by connecting it to
     * a native screen resource.  Making a container displayable will
     * cause any of its children to be made displayable.
     * This method is called internally by the toolkit and should
     * not be called directly by programs.
     * <p>
     * Overridden here to adjust the size of the frame if needed.
     * </p>
     * @see java.awt.Component#isDisplayable
     * @see java.awt.Container#removeNotify
     */
    public void addNotify()
    {
        Dimension d = getSize();

        super.addNotify();

        if (addNotify_done)
        {
            return;
        }

        // Adjust size according to the insets so that entire component
        // areas are renderable.
        int menubarheight = 0;
        JMenuBar menubar = getRootPane().getJMenuBar();
        if (menubar != null)
        {
            menubarheight = menubar.getPreferredSize().height;
        }
        Insets insets = getInsets();
        setSize(insets.left + insets.right + d.width, insets.top + insets.bottom + d.height + menubarheight);
        addNotify_done = true;
    }

    protected void AboutApplication()
    {
        try
        {
        	AboutDialog dlg = new AboutDialog(this);
        	dlg.setModal(true);
            dlg.setLocationRelativeTo(this);
        	dlg.setVisible(true);
        }
        catch (Exception e)
        {
            log.error(e);
        }
    }

    protected void ExitApplication()
    {
        try
        {
            Toolkit.getDefaultToolkit().beep();
            // Show an Exit confirmation dialog
            int reply = JOptionPane.showConfirmDialog(this,
                "Do you really want to exit?",
                "Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            if (reply == JOptionPane.YES_OPTION)
            {
                // User answered "Yes", so cleanup and exit
                //
                // Hide the frame
                this.setVisible(false);
                // Free system resources
                this.dispose();
                // Exit the application
                System.exit(0);
            }
        }
        catch (Exception e)
        {
            log.info("Exit Application");
            log.error(e);
        }
    }

    class EvtListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent event)
        {
            Object object = event.getSource();
            if (object == ServerManagerFrame.this)
            {
                Designer_windowClosing(event);
            }
        }
    }

    void Designer_windowClosing(WindowEvent event)
    {
        try
        {
            this.ExitApplication();
        }
        catch (Exception e)
        {
            log.info("Legatus_windowClosing");
            log.error(e);
        }
    }

    public void actionPerformed(ActionEvent event)
    {
        String command = event.getActionCommand();
        if (command == "About...")
        {
            menuitemAbout_Action(event);
        }
        if (command.equalsIgnoreCase("ServerConnect"))
            connectToServer();
        if (command.equalsIgnoreCase("ServerDisconnect"))
        {
            int reply = JOptionPane.showConfirmDialog(this,
                    "Do you really want to disconnect?","Disconnect",
                    JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if (reply == JOptionPane.YES_OPTION)
            {
                m_Client.disconnect();
            }
        }
        if (command.equalsIgnoreCase("ServerShutDown"))
        {
            int reply = JOptionPane.showConfirmDialog(this,
                    "Do you really want to shut this server down?","Shut Down",
                    JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if (reply == JOptionPane.YES_OPTION)
            {
                m_Client.shutdownServer();
            }
        }

        if (command.equals("ViewPlayers"))
        {
            m_FrameManager.ShowPlayerListFrame();
        }
        if (command.equals("ViewUnits"))
        {
            m_FrameManager.ShowUnitListFrame();
        }
        if (command.equals("ViewMap"))
        {
            m_FrameManager.ShowFlatStarMapFrame();
        }
    }

    void menuitemAbout_Action(ActionEvent event)
    {
        try
        {
            this.AboutApplication();
        }
        catch (Exception e)
        {
            log.info("menuitemAbout_Action");
            log.error(e);
        }
    }

    public void connected()
    {
        m_ConnectMenuItem.setEnabled(false);
        m_DisconnectMenuItem.setEnabled(true);
        m_ServerMenu.add(m_ServerSeperator);
        m_ServerMenu.add(m_ServerOperations);
        
        m_Client.requestUnitList();       
        m_Client.requestPlayerList();
    }
    
    public void disconnected()
    {
        JOptionPane.showMessageDialog(this,"Disconnected from Server!");
        m_ServerMenu.remove(m_ServerOperations);
        m_ServerMenu.remove(m_ServerSeperator);
        m_ConnectMenuItem.setEnabled(true);
        m_DisconnectMenuItem.setEnabled(false);
        setTitle(m_Title);
    }
    
    public void connectionFailed(String errorMessage)
    {
        JOptionPane.showMessageDialog(this,"Failed to connect to Server : " + errorMessage);
    }
    
    public void error(String Error)
    {
        JOptionPane.showMessageDialog(this,Error);
    }
    
    public void serverShuttingDown()
    {
    
    }
    
    public void serverStatus(ServerStatus status)
    {
        log.info(status.toString());
        String title = m_Title;
        title += " (" + status.getServerName() +")";
        if (m_Client != null)
            title += " " + m_Client.toString();
        setTitle(title);
    }
    
    public void gameJoined(String GameName, String GameType)
    {
        log.info("Woohoo! I have joined : " + GameName + " Type : " + GameType);
    }
    
    public void gameLeft(String GameName)
    {
        
    }
    
    public void gameClosed(String GameName)
    {
        
    }

    private void connectToServer()
    {
        Player p = m_Client.getParticipant();
        ConnectionDialog dlg = new ConnectionDialog(this);
        dlg.setPassword("");
        dlg.setServerName("localhost");
        dlg.setServerPort(10000);
        dlg.setLocationRelativeTo(this);
        dlg.setModal(true);
        dlg.setVisible(true);
        if (dlg.doConnect())
        {
            p.setName(dlg.getName());
            p.setPassword(PasswordManager.encrypt(dlg.getPassword()));
            p.setNickname("");
            p.setEmailAddress("");
            
            //Consider using SwingUtil to delay this
            try
            {
                final String server = dlg.getServerName();
                final int port = dlg.getServerPort();
                SwingWorker worker = new SwingWorker() {
                    public Object construct() {
                        try { m_Client.Connect(server,port);}
                        catch (Exception e)
                        { return "Failed";}
                        return "Done";
                    }
                 };
                 worker.start();
                
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this,"Error Connecting to Server : " + e.toString());
            }
        }        
    }
    
	@Override
    public void messageStart(String message)
    {
    	m_FrameManager.showGlassPane(message);
    }
    
	@Override
    public void messageEnd()
    {
    	m_FrameManager.hideGlassPane();
    }
    
	@Override
	public void solarSystemDetails(Long id, SolarSystemDetails ssd) 
	{
		m_FrameManager.registerSolarSystemDetails(id, ssd);
	}
	
	@Override
	public void playerList(Vector<PlayerSummary> playerSummaries)
	{
		m_FrameManager.registerPlayerList(playerSummaries);
	}

	@Override
	public void playerDetails(String playerName, Player player) 
	{
		m_FrameManager.registerPlayerDetails(playerName, player);
	}
	
	@Override
	public void unitList(Vector<UnitSummary> summaries) 
	{
		m_FrameManager.registerUnitList(summaries);
	}

	@Override
	public void unitDetails(String unitName, Unit unit) 
	{
		m_FrameManager.registerUnitDetails(unitName, unit);
	}
	
    
    
}