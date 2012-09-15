/**
 * Created on 05/04/2007
 * <p>Title: Legatus</p>
 *
 * <p>Copyright: Copyright Daniel Cleyne (c) 2004-2007</p>
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
package bt.test;


import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import bt.elements.galaxy.InnerSpherePlanet;
import bt.elements.galaxy.SolarSystemDetails;
import bt.managers.PlanetManager;
import bt.managers.SolarSystemManager;
import bt.ui.panels.PlanetPanel;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;


public class WorldMapNavigationTest extends JFrame implements
ActionListener
{
    private static final long serialVersionUID = 1;
    
    private PlanetPanel m_PlanetPanel = null;
    private JScrollPane m_ScrollPane = null;
    private int m_PlanetSize = 30;
    
    private JMenuBar menuMain;
    
	public WorldMapNavigationTest()
	{
        try
        {
        	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            setTitle("Testing Planet Rendering");
                        
	        menuMain = new JMenuBar();
	        setJMenuBar(menuMain);
	        menuAddItems(menuMain);
	        
	        InnerSpherePlanet isp = PlanetManager.getPlanetFromID(5);
	        SolarSystemDetails ssd = SolarSystemManager.getSystemDetails(isp);
	        
	        m_PlanetPanel = new PlanetPanel(ssd,16);
	        //m_PlanetPanel = new PlanetPanel(m_PlanetSize,wtg.getPlanetMap(),32);
            
            m_ScrollPane = new JScrollPane(m_PlanetPanel);
            m_ScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            m_ScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            
            setContentPane(m_ScrollPane);
            
            setSize(1280,960);
            setVisible(true);

            
        }
        catch (Exception e)
        {
        	System.out.println(ExceptionUtil.getExceptionStackTrace(e));
        	System.exit(0);
        }
		
	}
	
	
    protected void menuAddItems(JMenuBar hmenu)
    {
        JMenu menu;
        JMenuItem item;
        // Menu and toolbar listener
        //
        //"Tasks" menu
        menu = new JMenu("File");
        menu.setActionCommand("File");
        hmenu.add(menu);
        // "StaticData" item

        item = new JMenuItem("Dump Planet To File");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand("DumpPlanet");
        item.setMnemonic( (int)'D');
        item.addActionListener(this);
        menu.add(item);
        
        menu.addSeparator();
        
        item = new JMenuItem("Exit");
        item.setHorizontalTextPosition(SwingConstants.RIGHT);
        item.setActionCommand("Exit");
        item.setMnemonic( (int)'X');
        item.addActionListener(this);
        menu.add(item);
    }	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
        try
        {
            PropertyUtil.loadSystemProperties("bt/system.properties");
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            WorldMapNavigationTest client = new WorldMapNavigationTest();
            client.setVisible(true);
        }
        catch (Exception e)
        {
        	System.out.println(ExceptionUtil.getExceptionStackTrace(e));
        }

	}

    
    public void actionPerformed(ActionEvent ae)
    {
    	if (ae.getActionCommand().equalsIgnoreCase("Exit"))
    	{
    		setVisible(false);
    	}
    	if (ae.getActionCommand().equalsIgnoreCase("DumpPlanet"))
    	{
    		dumpPlanetToFile();
    	}
    }
    
    private void dumpPlanetToFile()
    {
        try
        {
			Rectangle bounds = m_PlanetPanel.getBounds();
			BufferedImage image = new BufferedImage(bounds.width, bounds.height,BufferedImage.TYPE_3BYTE_BGR);
	
			Graphics g = image.getGraphics();
			g.setClip(bounds.x, bounds.y, bounds.width, bounds.height);
			m_PlanetPanel.paint(g);
			
			ImageIO.write(image, "PNG", new File("WorldMap_" + Integer.toString(m_PlanetSize) + ".png"));
        }
        catch (Exception e)
        {
        	System.out.println(ExceptionUtil.getExceptionStackTrace(e));
        }
		
    }
    
    
    public class SelectPlanetSizeDialog extends JDialog implements ActionListener
    {
        private static final long serialVersionUID = 1;
        
        private int m_PlanetSize = 0;
        
    	private JPanel m_ContentPane = null;
    	private JPanel m_ButtonPanel = null;
    	private JButton m_OkButton = null;
    	private JButton m_CancelButton = null;
    	private JTextArea m_InfoTextArea = null;
    	private JSpinner m_PlanetSizeSpinner = null;

    	/**
    	 * This method initializes 
    	 * 
    	 */
    	public SelectPlanetSizeDialog(Frame owner, int planetSize) 
    	{
    		super(owner);
    		m_PlanetSize = planetSize;
    		initialize(planetSize);
    	}
    	
    	public int getPlanetSize()
    	{
    		return m_PlanetSize;
    	}

    	/**
    	 * This method initializes this
    	 * 
    	 */
    	private void initialize(int planetSize) 
    	{
            this.setSize(new Dimension(307, 189));
            this.setContentPane(getM_ContentPane(planetSize));
            this.setTitle("Select Planet Size");
            this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    	}

    	/**
    	 * This method initializes m_ContentPane	
    	 * 	
    	 * @return javax.swing.JPanel	
    	 */
    	private JPanel getM_ContentPane(int planetSize)
    	{
    		if (m_ContentPane == null)
    		{
    			m_ContentPane = new JPanel();
    			m_ContentPane.setLayout(new BorderLayout());
    			m_ContentPane.add(getM_ButtonPanel(), BorderLayout.SOUTH);
    			m_ContentPane.add(getM_InfoTextArea(), BorderLayout.NORTH);
    			m_ContentPane.add(getM_PlanetSizeSpinner(planetSize), BorderLayout.CENTER);
    		}
    		return m_ContentPane;
    	}

    	/**
    	 * This method initializes m_ButtonPanel	
    	 * 	
    	 * @return javax.swing.JPanel	
    	 */
    	private JPanel getM_ButtonPanel()
    	{
    		if (m_ButtonPanel == null)
    		{
    			FlowLayout flowLayout = new FlowLayout();
    			flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
    			flowLayout.setVgap(2);
    			m_ButtonPanel = new JPanel();
    			m_ButtonPanel.setLayout(flowLayout);
    			m_ButtonPanel.add(getM_OkButton(), null);
    			m_ButtonPanel.add(getM_CancelButton(), null);
    		}
    		return m_ButtonPanel;
    	}

    	/**
    	 * This method initializes m_OkButton	
    	 * 	
    	 * @return javax.swing.JButton	
    	 */
    	private JButton getM_OkButton()
    	{
    		if (m_OkButton == null)
    		{
    			m_OkButton = new JButton();
    			m_OkButton.setText("Ok");
    			m_OkButton.addActionListener(this);
    		}
    		return m_OkButton;
    	}

    	/**
    	 * This method initializes m_CancelButton	
    	 * 	
    	 * @return javax.swing.JButton	
    	 */
    	private JButton getM_CancelButton()
    	{
    		if (m_CancelButton == null)
    		{
    			m_CancelButton = new JButton();
    			m_CancelButton.setText("Cancel");
    			m_CancelButton.addActionListener(this);
    		}
    		return m_CancelButton;
    	}

    	/**
    	 * This method initializes m_InfoTextArea	
    	 * 	
    	 * @return javax.swing.JTextArea	
    	 */
    	private JTextArea getM_InfoTextArea()
    	{
    		if (m_InfoTextArea == null)
    		{
    			m_InfoTextArea = new JTextArea();
    			m_InfoTextArea.setText("The planet size is determined by the circumference of the planet at the "
    					+ "equator. In Prefect each hex should be approximately 300 kilometres across. So the "
    					+ "number of hexes on the equator is determined from this. Due to the nature of the way "
    					+ "the map is displayed, the circumference must be a multiple of 1500 (5 x 300)");
    			m_InfoTextArea.setLineWrap(true);
    			m_InfoTextArea.setWrapStyleWord(true);
    		}
    		return m_InfoTextArea;
    	}

    	/**
    	 * This method initializes m_PlanetSizeSpinner	
    	 * 	
    	 * @return javax.swing.JSpinner	
    	 */
    	private JSpinner getM_PlanetSizeSpinner(int planetSize)
    	{
    		if (m_PlanetSizeSpinner == null)
    		{
    			SpinnerModel model =
    		        new SpinnerNumberModel(planetSize * 1500, //initial value
    		                               3000, //min
    		                               45000, //max
    		                               1500);                //step			
    			m_PlanetSizeSpinner = new JSpinner(model);
    		}
    		return m_PlanetSizeSpinner;
    	}
    	
    	public void actionPerformed(ActionEvent ae)
    	{
    		if (ae.getActionCommand().equalsIgnoreCase("Ok"))
    		{
    			int value = (Integer)m_PlanetSizeSpinner.getValue();
    			m_PlanetSize = value / 1500;
    			setVisible(false);
    		}
    		else if (ae.getActionCommand().equalsIgnoreCase("Cancel"))
    		{
    			setVisible(false);
    		}
    	}
    }    
}
