package bt;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;

import javax.swing.DefaultDesktopManager;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import bt.ui.InternalFrameManager;
import bt.ui.dialogs.RandomGameDialog;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;

/**
 * <p>Title: Battletech Mercenary Management</p>
 * <p>Description: Utility to manage mercenary units and manage contracts</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Daniel Cleyne
 * @version 0.1
 */

public class CampaignManager extends JFrame implements ActionListener, WindowListener
{
	private static final String ABOUT = "About...";

	private static final String VIEW_UNITS = "ViewUnits";

	private static final String VIEW_MAP = "ViewMap";

	private static final String VIEW_PLAYERS = "ViewPlayers";

	private static final String CREATE_RANDOM_GAME = "CreateRandomGame";

	private static final long serialVersionUID = 1;
	
    private JDesktopPane _MainPane = new JDesktopPane();
    private InternalFrameManager _FrameManager = new InternalFrameManager(this, _MainPane);

    // Declarations
    //
    // Panels
    JScrollPane _Scrollpane;
    JPanel _Panel1;
    JPanel _Panel2;
    // Main menubar
    JMenuBar _MainMenu;

    // "File" menu
//    JMenuItem menuitemNew;
    // "Help" menu
    JMenuItem _AboutMenuItem;
    // Main toolbar
    JToolBar _MainToolbar;
    JButton _AboutButton;
    ImageIcon _AboutIcon;


    protected HashMap<String, JInternalFrame> _SolarSystemFrames = new HashMap<String,JInternalFrame>();


    public CampaignManager()
    {
        this("Battletech Campaign Manager");
        

    }

    public CampaignManager(String title)
    {
        // Initialize
    	try
    	{
	        PropertyUtil.loadSystemProperties("bt/system.properties");
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
    		System.exit(1);
    	}
        
        //
        // Common images for menubar and toolbar
        LoadCommonImages();
        // Frame
        setTitle(title);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0,0));

        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width-100,screenSize.height-200);
        setLocation((screenSize.width - getWidth() - 100) / 2, (screenSize.height - getHeight() - 200) / 2);

        setVisible(false);
        // Menu
        _MainMenu = new JMenuBar();
        setJMenuBar(_MainMenu);
        menuAddItems(_MainMenu);
        // Panels

        //m_MainPane.setOpaque(false);

        _MainPane.setDesktopManager(new DefaultDesktopManager());
        getContentPane().add(_MainPane,BorderLayout.CENTER);


        _Panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        getContentPane().add(BorderLayout.NORTH, _Panel2);
        _Panel2.setBounds(0,0,475,30);


        // Toolbar
        /*
        tbMain = new JToolBar();
        panel2.add(tbMain);
        tbMain.setBounds(0,0,375,30);
        tbAddButtons(tbMain);
*/

        // Register listeners
        //
        // Window listener
        addWindowListener(this);
    }

    protected void LoadCommonImages()
    {
        _AboutIcon = new ImageIcon("images/about.gif");
    }

    protected void tbAddButtons(JToolBar toolbar) {
        Rectangle bounds = new Rectangle();
        // Bounds for each button
        bounds.x=2;
        bounds.y=2;
        bounds.width=25;
        bounds.height=25;
        // Toolbar separator
        // Button size
        Dimension buttonsize = new Dimension(bounds.width,bounds.height);

        // About
        _AboutButton = new JButton(_AboutIcon);
        _AboutButton.setDefaultCapable(false);
        _AboutButton.setToolTipText("Display program information");
        _AboutButton.setMnemonic((int)'A');
        toolbar.add(_AboutButton);
        bounds.x += bounds.width;
        _AboutButton.setBounds(bounds);
        _AboutButton.setMinimumSize(buttonsize);
        _AboutButton.setMaximumSize(buttonsize);
        _AboutButton.setPreferredSize(buttonsize);
        _AboutButton.setActionCommand(ABOUT);
    }

    protected void menuAddItems(JMenuBar hmenu) {
        JMenu menu;
        JMenuItem item;
        //
        //"Campaign" menu
        menu = new JMenu("Campaign");
        menu.setActionCommand("Campaign");
        menu.setBorderPainted(false);
        menu.setMnemonic( (int)'C');
        hmenu.add(menu);

        // "View Players" item
        item = new JMenuItem("View Players");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand(VIEW_PLAYERS);
        item.setBorderPainted(false);
        item.setMnemonic( (int)'P');
        item.addActionListener(this);
        menu.add(item);

        // "View Units" item
        item = new JMenuItem("View Units");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand(VIEW_UNITS);
        item.setBorderPainted(false);
        item.setMnemonic( (int)'U');
        item.addActionListener(this);
        menu.add(item);

        //"Tools" menu
        menu = new JMenu("Tools");
        menu.setActionCommand("Tools");
        menu.setBorderPainted(false);
        menu.setMnemonic( (int)'T');
        hmenu.add(menu);
        // "Create Random Game" item
        item = new JMenuItem("Create Random Game");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand(CREATE_RANDOM_GAME);
        item.setBorderPainted(false);
        item.setMnemonic( (int)'C');
        item.addActionListener(this);
        menu.add(item);
        // "View Map" item
        item = new JMenuItem("View Map");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand(VIEW_MAP);
        item.setBorderPainted(false);
        item.setMnemonic( (int)'M');
        item.addActionListener(this);
        menu.add(item);

        item = new JMenuItem("Random Game");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand("RandomGame");
        item.setBorderPainted(false);
        item.setMnemonic( (int)'R');
        item.addActionListener(this);
        menu.add(item);
        
        
        //"Setup" menu
        menu = new JMenu("Setup");
        menu.setActionCommand("Setup");
        menu.setBorderPainted(false);
        menu.setMnemonic( (int)'S');
        hmenu.add(menu);
        // "Edit Unit Structure Setup item
        item = new JMenuItem("Edit Unit Structure Setup");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand("UnitStructure");
        item.setBorderPainted(false);
        item.setMnemonic( (int)'E');
        item.addActionListener(this);
        menu.add(item);
        
        //
        // "Help" menu
        //
        menu = new JMenu("Help");
        menu.setActionCommand("Help");
        menu.setBorderPainted(false);
        menu.setMnemonic((int)'H');
        hmenu.add(menu);
        // "About" item
        _AboutMenuItem = new JMenuItem(ABOUT);
        _AboutMenuItem.setHorizontalTextPosition(SwingConstants.RIGHT);
        _AboutMenuItem.setActionCommand(ABOUT);
        _AboutMenuItem.setBorderPainted(false);
        _AboutMenuItem.setMnemonic((int)'A');
        _AboutMenuItem.setIcon(_AboutIcon);
        _AboutMenuItem.addActionListener(this);
        menu.add(_AboutMenuItem);
    }

    static public void main(String args[])
    {
        /**
         * The entry point for this application.
         * @param args
         */
    	System.out.println("Starting CampaignManager");

        try {
            // Set the "look and feel" to the native system
            try {
                //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            } catch (Exception e) {
            }
            // Create a new instance of the application frame and make it visible
            (new CampaignManager()).setVisible(true);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
            // Exit with error condition
            System.exit(1);
        }
    }

    // Used by addNotify
    boolean addNotify_done=false;

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
    public void addNotify() {
        Dimension d=getSize();

        super.addNotify();

        if (addNotify_done) return;

        // Adjust size according to the insets so that entire component
        // areas are renderable.
        int menubarheight=0;
        JMenuBar menubar = getRootPane().getJMenuBar();
        if (menubar!=null) {
            menubarheight = menubar.getPreferredSize().height;
        }
        Insets insets=getInsets();
        setSize(insets.left + insets.right + d.width, insets.top + insets.bottom + d.height + menubarheight);
        addNotify_done=true;
    }

    protected void AboutApplication() {
        try {
            JOptionPane.showMessageDialog(this,
                                          "Battletech Mercenary Unit Manager",
                                          "About" ,
                                          JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
        }
    }
    
    private void CreateRandomGame()
    {
    	RandomGameDialog dlg = new RandomGameDialog(this);
    	dlg.setLocationRelativeTo(this);
    	dlg.setModal(true);
    	dlg.setVisible(true);
    }

    protected void ExitApplication()
    {
        try {
            Toolkit.getDefaultToolkit().beep();
            // Show an Exit confirmation dialog
            int reply = JOptionPane.showConfirmDialog(this,
                                                      "Do you really want to exit?",
                                                      "Exit" ,
                                                      JOptionPane.YES_NO_OPTION,
                                                      JOptionPane.QUESTION_MESSAGE);
            if (reply==JOptionPane.YES_OPTION) {
                // User answered "Yes", so cleanup and exit
                //
                //Close all open Internal Frames
                _FrameManager.CloseInternalFrames();

                // Hide the frame
                this.setVisible(false);
                // Free system resources
                this.dispose();
                // Exit the application
                System.exit(0);
            }
            System.exit(0); //Just exit for now. This should display a dialog if the document is dirty
        } catch (Exception e) {
        }
    }

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		// TODO Auto-generated method stub
            String command = event.getActionCommand();
            if (command==ABOUT) {
                AboutApplication();
            }
            if (command.equals(VIEW_UNITS))
            {
                _FrameManager.ShowUnitListFrame();
            }		
            if (command.equals(VIEW_MAP))
            {
                _FrameManager.ShowFlatStarMapFrame();
            }		
            if (command.equals(VIEW_PLAYERS))
            {
                _FrameManager.ShowPlayerListFrame();
            }		
            if (command.equals(CREATE_RANDOM_GAME))
            {
                _FrameManager.ShowRandomGameFrame();
            }		
            if (command.equals("RandomGame"))
            {
            	CreateRandomGame();
            }		
	}

	@Override
	public void windowActivated(WindowEvent event) 
	{
	}

	@Override
	public void windowClosed(WindowEvent event) 
	{
	}

	@Override
	public void windowClosing(WindowEvent event) 
	{
		this.ExitApplication();
	}

	@Override
	public void windowDeactivated(WindowEvent event) 
	{
	}

	@Override
	public void windowDeiconified(WindowEvent event) 
	{
	}

	@Override
	public void windowIconified(WindowEvent event) 
	{
	}

	@Override
	public void windowOpened(WindowEvent arg0) 
	{
	}

}
