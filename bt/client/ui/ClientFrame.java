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
package bt.client.ui;

import java.util.Vector;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bt.client.*;
import bt.common.elements.unit.Player;
import bt.common.elements.unit.Unit;
import bt.common.managers.PasswordManager;
import bt.common.net.*;
import bt.common.ui.ConnectionDialog;
import bt.common.ui.GlassPane;
import bt.common.ui.mapping.flatstarmap.FlatStarMapPanel;
import bt.common.util.SwingWorker;

public class ClientFrame extends JFrame implements ActionListener, ClientListener
{
    private static final long serialVersionUID = 1;
    
    private final String m_Title = "Mercenary Unit Manager";
    private static Log log = LogFactory.getLog(ClientFrame.class);
    private Client m_Client;
    
    private JMenuItem m_ConnectMenuItem = null;
    private JMenuItem m_DisconnectMenuItem = null;
    
    
    // Declarations
    //
    // Main menubar
    private JMenuBar m_MainMenu;

    // "File" menu
//    JMenuItem menuitemNew;
    // "Help" menu
    private JMenuItem m_AboutMenuItem;
    private ImageIcon m_AboutIcon;
    
    private JMenu m_ServerMenu;
    private JMenu m_UnitsMenu;
    private JMenu m_SetupMenu;
    private JMenu m_HelpMenu;

    public ClientFrame(Client Client)
    {
        // Initialize
        //
        m_Client = Client;
        m_Client.addListener(this);
        
        
        // Common images for menubar and toolbar
        LoadCommonImages();
        // Frame
        setTitle(m_Title);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new FlatStarMapPanel());                
        setGlassPane(new GlassPane());
        
        
        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width - 100, screenSize.height - 200);

        setLocationRelativeTo(null);
        
        setVisible(false);
        // Menu
        m_MainMenu = new JMenuBar();
        setJMenuBar(m_MainMenu);
        createMenuItems();
        // Panels
        setMenus();

        // Register listeners
        //
        // Window listener
        EvtListener elWindow = new EvtListener();
        this.addWindowListener(elWindow);
    }
    
    protected void LoadCommonImages()
    {
        m_AboutIcon = new ImageIcon("images/about.gif");
    }


    protected void createMenuItems()
    {
        JMenuItem item;
        // Menu and toolbar listener
        //
        //"Tasks" menu
        m_ServerMenu = new JMenu("Server");
        m_ServerMenu.setActionCommand("Server");
        m_ServerMenu.setBorderPainted(false);

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
        // "Setup" menu
        //
        m_SetupMenu = new JMenu("Setup");
        m_SetupMenu.setActionCommand("Setup");
        m_SetupMenu.setBorderPainted(false);
        m_SetupMenu.setMnemonic( (int)'E');
        // "StaticData" item
        item = new JMenuItem("Preferences");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand("Preferences");
        item.setBorderPainted(false);
        item.setMnemonic( (int)'P');
        item.addActionListener(this);
        m_SetupMenu.add(item);

        //
        // "Help" menu
        //
        m_HelpMenu = new JMenu("Help");
        m_HelpMenu.setActionCommand("Help");
        m_HelpMenu.setBorderPainted(false);
        m_HelpMenu.setMnemonic( (int)'H');
        // "About" item
        m_AboutMenuItem = new JMenuItem("About...");
        m_AboutMenuItem.setHorizontalTextPosition(SwingConstants.RIGHT);
        m_AboutMenuItem.setActionCommand("About...");
        m_AboutMenuItem.setBorderPainted(false);
        m_AboutMenuItem.setMnemonic( (int)'A');
        m_AboutMenuItem.setIcon(m_AboutIcon);
        m_AboutMenuItem.addActionListener(this);
        m_HelpMenu.add(m_AboutMenuItem);

    }

    // Used by addNotify
    boolean addNotify_done = false;

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
            if (object == ClientFrame.this)
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
        } else if (command.equalsIgnoreCase("ServerConnect"))
            connectToServer();
        else if (command.equalsIgnoreCase("ServerDisconnect"))
        {
            int reply = JOptionPane.showConfirmDialog(this,
                    "Do you really want to disconnect?","Disconnect",
                    JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if (reply == JOptionPane.YES_OPTION)
            {
                m_Client.disconnect();
                disconnected();
            }
        }
        else if (command.length() > 5)
        {
        	if (command.substring(0, 5).equalsIgnoreCase("unit:"))
        	{
        		String unitName = command.substring(5);
        		m_Client.requestUnitDetails(unitName);
        	}
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
        
        m_Client.requestUnits();
        
    }
    
    public void disconnected()
    {
        JOptionPane.showMessageDialog(this,"Disconnected from Server!");
        m_ConnectMenuItem.setEnabled(true);
        m_DisconnectMenuItem.setEnabled(false);
        setTitle(m_Title);
        validate();
        repaint();
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
        log.debug(status.toString());
        String title = m_Title;
        title += " (" + status.getServerName() +")";
        if (m_Client != null)
            title += " " + m_Client.toString();
        setTitle(title);
    }
    
    public void playerUnitList(Vector<String> unitList)
    {
    	//Set up the Units Menu
    	if (m_UnitsMenu != null)
    	{
    		for (Component c : m_MainMenu.getComponents())
    		{
    			if (c instanceof JMenuItem)
    			{
    				JMenuItem jmi = (JMenuItem)c;
    				jmi.removeActionListener(this);
    			}
    		}
    	}
		JMenuItem requestNewUnit = new JMenuItem("Request New Item");
		requestNewUnit.setActionCommand("RequestNewUnit");
		requestNewUnit.addActionListener(this);
		
    	m_UnitsMenu = new JMenu("Units");
    	if (unitList.size() > 0)
    	{
    		for (String unitName: unitList)
    		{
    			JMenuItem jmi = new JMenuItem(unitName);
    			jmi.setActionCommand("unit:" + unitName);
    			jmi.addActionListener(this);
    			m_UnitsMenu.add(jmi);
    		}
    		m_UnitsMenu.add(new JSeparator());
    	}
    	m_UnitsMenu.add(requestNewUnit);
    	setMenus();
    }
    
    public void playerUnitDetails(Unit u)
    {
    	JOptionPane.showMessageDialog(this,"Unit Details for unit " + u.getName());    	
    }
    
    private void setMenus()
    {
    	m_MainMenu.removeAll();
    	
    	m_MainMenu.add(m_ServerMenu);
    	if (m_DisconnectMenuItem.isEnabled())
    	{
    		m_MainMenu.add(m_UnitsMenu);
    	}
    	m_MainMenu.add(m_SetupMenu);
    	m_MainMenu.add(m_HelpMenu);
    	m_MainMenu.revalidate();
    }
        
    private void connectToServer()
    {
        Player p = m_Client.getParticipant();
        ConnectionDialog dlg = new ConnectionDialog(this);
        dlg.setPlayerName(p.getName());
        dlg.setPassword("");
        dlg.setServerName("localhost");
        dlg.setServerPort(10000);
        dlg.setLocationRelativeTo(this);
        dlg.setModal(true);
        dlg.setVisible(true);
        if (dlg.doConnect())
        {
            p.setName(dlg.getPlayerName());
            p.setPassword(PasswordManager.encrypt(dlg.getPassword()));
            p.setNickname(dlg.getNickname());
            p.setEmailAddress(dlg.getEmailAddress());
            
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
    
    
}