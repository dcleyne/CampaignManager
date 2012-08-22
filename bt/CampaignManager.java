package bt;

import java.util.*;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import bt.ui.InternalFrameManager;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

/**
 * <p>Title: Battletech Mercenary Management</p>
 * <p>Description: Utility to manage mercenary units and manage contracts</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Daniel Cleyne
 * @version 0.1
 */

public class CampaignManager extends JFrame
{
	private static final long serialVersionUID = 1;
	
    private static Log log = LogFactory.getLog(CampaignManager.class);
    private JDesktopPane m_MainPane = new JDesktopPane();
    private InternalFrameManager m_FrameManager = new InternalFrameManager(this, m_MainPane);

    // Declarations
    //
    // Panels
    JScrollPane scrollpane;
    JPanel panel1;
    JPanel panel2;
    // Main menubar
    JMenuBar menuMain;

    // "File" menu
//    JMenuItem menuitemNew;
    // "Help" menu
    JMenuItem menuitemAbout;
    // Main toolbar
    JToolBar tbMain;
    JButton buttonAbout;
    ImageIcon iconAbout;


    protected HashMap<String, JInternalFrame> m_SolarSystemFrames = new HashMap<String,JInternalFrame>();


    public CampaignManager()
    {
        this("Battletech Campaign Manager");
        

    }

    public CampaignManager(String title)
    {
        // Initialize
    	try
    	{
	        PropertyUtil.loadSystemProperties("bt/common/system.properties");
	        PropertyUtil.loadSystemProperties("bt/client/client.properties");
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
        menuMain = new JMenuBar();
        setJMenuBar(menuMain);
        menuAddItems(menuMain);
        // Panels

        //m_MainPane.setOpaque(false);

        m_MainPane.setDesktopManager(new DefaultDesktopManager());
        getContentPane().add(m_MainPane,BorderLayout.CENTER);


        panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        getContentPane().add(BorderLayout.NORTH, panel2);
        panel2.setBounds(0,0,475,30);


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
        EvtListener elWindow = new EvtListener();
        this.addWindowListener(elWindow);
        // Menu and toolbar listener
        ActListener alMenuToolbar = new ActListener();
//        buttonAbout.addActionListener(alMenuToolbar);
        menuitemAbout.addActionListener(alMenuToolbar);

    }

    protected void LoadCommonImages()
    {
        iconAbout = new ImageIcon("images/about.gif");
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
        buttonAbout = new JButton(iconAbout);
        buttonAbout.setDefaultCapable(false);
        buttonAbout.setToolTipText("Display program information");
        buttonAbout.setMnemonic((int)'A');
        toolbar.add(buttonAbout);
        bounds.x += bounds.width;
        buttonAbout.setBounds(bounds);
        buttonAbout.setMinimumSize(buttonsize);
        buttonAbout.setMaximumSize(buttonsize);
        buttonAbout.setPreferredSize(buttonsize);
        buttonAbout.setActionCommand("About...");
    }

    protected void menuAddItems(JMenuBar hmenu) {
        JMenu menu;
        JMenuItem item;
        // Menu and toolbar listener
        ActListener alMenuToolbar = new ActListener();

        //
        //"Tasks" menu
        menu = new JMenu("Task");
        menu.setActionCommand("Task");
        menu.setBorderPainted(false);
        menu.setMnemonic( (int)'T');
        hmenu.add(menu);
        // "StaticData" item
        item = new JMenuItem("Edit Unit Structure Setup");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand("UnitStructure");
        item.setBorderPainted(false);
        item.setMnemonic( (int)'U');
        item.addActionListener(alMenuToolbar);
        menu.add(item);
        // "StaticData" item
        item = new JMenuItem("View Map");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand("ViewMap");
        item.setBorderPainted(false);
        item.setMnemonic( (int)'M');
        item.addActionListener(alMenuToolbar);
        menu.add(item);
        // "StaticData" item
        item = new JMenuItem("View Units");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand("ViewUnits");
        item.setBorderPainted(false);
        item.setMnemonic( (int)'V');
        item.addActionListener(alMenuToolbar);
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
        menuitemAbout = new JMenuItem("About...");
        menuitemAbout.setHorizontalTextPosition(SwingConstants.RIGHT);
        menuitemAbout.setActionCommand("About...");
        menuitemAbout.setBorderPainted(false);
        menuitemAbout.setMnemonic((int)'A');
        menuitemAbout.setIcon(iconAbout);
        menu.add(menuitemAbout);
    }

    static public void main(String args[])
    {
        /**
         * The entry point for this application.
         * @param args
         */
        PropertyConfigurator.configure(Loader.getResource("bt/client/log4j.properties"));
        log.info("Starting CampaignManager");

        try {
            // Set the "look and feel" to the native system
            try {
                //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
                m_FrameManager.CloseInternalFrames();

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

    class EvtListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent event)
        {
            Object object = event.getSource();
            if (object==CampaignManager.this)
            {
                InnerSphereApp_windowClosing(event);
            }
        }
    }

    void InnerSphereApp_windowClosing(WindowEvent event)
    {
        try
        {
            this.ExitApplication();
        }
        catch (Exception e)
        {
            log.debug("Exception on Application Close : " + e);
        }
    }

    class ActListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String command = event.getActionCommand();
            if (command=="About...") {
                menuitemAbout_Action(event);
            }
            if (command.equals("ViewUnits"))
            {
                m_FrameManager.ShowUnitListFrame();
            }
        }
    }

    void menuitemAbout_Action(ActionEvent event) {

        // TODO:  code goes here

        try {
            this.AboutApplication();
        } catch (Exception e) {
        }
    }

}
