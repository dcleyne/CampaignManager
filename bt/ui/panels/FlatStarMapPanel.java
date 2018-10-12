package bt.ui.panels;

import java.awt.*;

import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;

import bt.elements.galaxy.InnerSpherePlanet;
import bt.elements.galaxy.SolarSystemDetails;
import bt.managers.SolarSystemManager;
import bt.managers.listeners.SolarSystemManagerListener;
import bt.ui.panes.FlatStarMapPane;
import bt.ui.renderers.FlatStarMapRenderer;

/**
 * <p>Title: Battletech Mercenary Management</p>
 * <p>Description: Utility to manage mercenary units and manage contracts</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Daniel Cleyne
 * @version 0.1
 */

public class FlatStarMapPanel extends JPanel  implements ActionListener, MouseListener
{
	private static final long serialVersionUID = 1;
	
    JToolBar m_Toolbar;
    JButton[] m_TBButtons;
    ButtonGroup m_TBGroup;


    FlatStarMapRenderer m_Map;
    FlatStarMapPane m_MapPane;
    java.awt.Rectangle m_MapBounds;
    JScrollPane m_ScrollPane;
    JTextArea m_StatusPane;
    JPanel m_MapPanel;
    JTextField m_SearchText;

    protected Vector<SolarSystemManagerListener> m_SolarSystemListeners = new Vector<SolarSystemManagerListener>();

    public FlatStarMapPanel()
    {
        m_MapPanel = new JPanel(new BorderLayout());

        m_Map = new FlatStarMapRenderer();
        m_MapPane = new FlatStarMapPane(m_Map);
        m_MapPane.addMouseListener(this);

        m_ScrollPane = new JScrollPane(m_MapPane,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        m_ScrollPane.setAutoscrolls(true);
        m_ScrollPane.setBackground(Color.lightGray);

        m_StatusPane = new JTextArea();
        m_StatusPane.setEditable(false);
        m_StatusPane.setText("");
        m_StatusPane.setBackground(Color.lightGray);
        m_StatusPane.setPreferredSize(new Dimension(0,20));

        addToolbar();
        m_MapPanel.add(m_Toolbar,BorderLayout.NORTH);
        m_MapPanel.add(m_ScrollPane,BorderLayout.CENTER);
        m_MapPanel.add(m_StatusPane,BorderLayout.SOUTH);
        add(m_MapPanel);


        setVisible(true);
    }
    protected void addToolbar()
    {
        m_Toolbar = new JToolBar();
        //m_TBGroup = new ButtonGroup();
        m_TBButtons = new JButton[6];

        m_Toolbar.putClientProperty("JToolBar.isRollover",Boolean.TRUE);

        m_TBButtons[0] = new JButton("Zoom In");
        m_TBButtons[0].setToolTipText("Increase Zoom Level");
        m_TBButtons[0].setActionCommand("zoom_in");

        m_TBButtons[1] = new JButton("Zoom Out");
        m_TBButtons[1].setToolTipText("Decrease Zoom Level");
        m_TBButtons[1].setActionCommand("zoom_out");

        for (int i = 0; i < 2; i++)
        {
            m_Toolbar.add(m_TBButtons[i]);
            //m_TBGroup.add(m_TBButtons[i]);
            m_TBButtons[i].addActionListener(this);
        }

        JPanel SearchPanel = new JPanel(new BorderLayout());
        m_SearchText = new JTextField();
        m_SearchText.setPreferredSize(new Dimension(120,20));
        SearchPanel.add(m_SearchText,BorderLayout.CENTER);
        JButton FindButton = new JButton("Find");
        FindButton.setPreferredSize(new Dimension(70,20));
        FindButton.setActionCommand("Find");
        FindButton.setToolTipText("Search for System(s)");
        FindButton.addActionListener(this);
        SearchPanel.add(FindButton,BorderLayout.EAST);
        SearchPanel.setPreferredSize(new Dimension(200,24));
        m_Toolbar.add(SearchPanel);



    }

    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();

        if (cmd.compareTo("zoom_in") == 0)
        {
            m_Map.SetZoom(m_Map.GetZoom() + 1.0);
//            m_MapPane.setSize(m_MapPane.getPreferredSize());
//            m_ScrollPane.scrollRectToVisible(m_MapPane.getBounds());
            m_MapPane.revalidate();
            m_MapPane.repaint();
        }
        if (cmd.compareTo("zoom_out") == 0)
        {
            m_Map.SetZoom(m_Map.GetZoom() - 1.0);
//            m_MapPane.setSize(m_MapPane.getPreferredSize());
//            m_ScrollPane.scrollRectToVisible(m_MapPane.getBounds());
            m_MapPane.revalidate();
            m_MapPane.repaint();
        }
        if (cmd.compareTo("Find") == 0)
        {
            m_Map.FindSystems(m_SearchText.getText());
            m_MapPane.repaint();
        }
    }

    public void setCurrentSolarSystem(Point p)
    {
        m_Map.selectSolarSystem(p);
        m_MapPane.repaint();
    }

    public void toggleSystemSelection(Point p)
    {
        m_Map.toggleSolarSystem(p);
        m_MapPane.repaint();
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseClicked(MouseEvent e)
    {
        Cursor OldCur = getCursor();
        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) > 0)
        {
            try
            {
                int index = m_Map.GetSystemIndex(e.getPoint());
                if (index != -1)
                {
                    if (e.getClickCount() == 2)
                        NotifySolarSystemEditRequested(index);
                }
            }
            catch (Exception ex)
            {
            }
        }

        setCursor(OldCur);
    }

    public void mousePressed(MouseEvent e)
    {
    }
    public void mouseReleased(MouseEvent e)
    {
        if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) > 0)
        {
            try
            {
                int index = m_Map.GetSystemIndex(e.getPoint());
                if (index != -1)
                {
                    if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) > 0)
                        toggleSystemSelection(e.getPoint());
                    else
                    {
                        if (e.getClickCount() == 1)
                            setCurrentSolarSystem(e.getPoint());
                        else
                            NotifySolarSystemEditRequested(index);
                    }

                    InnerSpherePlanet p1 = SolarSystemManager.getPlanet(index);
                    m_StatusPane.setText(p1.toString());
                }
                else
                    m_StatusPane.setText("");
            }
            catch (Exception ex)
            {
            	System.out.println("Exception occurred while loading SolarSystem : ");
            	ex.printStackTrace();
            }
        }

        if ((e.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) > 0)
        {
            int i1 = m_Map.GetCurrentSystemIndex();
            int i2 = m_Map.GetSystemIndex(e.getPoint());
            if (i1 != -1 && i2 != -1)
            {
                double dist = m_Map.CalculateDistance(i1,i2);
                try
                {
                    InnerSpherePlanet p1 = SolarSystemManager.getPlanet(i1);
                    InnerSpherePlanet p2 = SolarSystemManager.getPlanet(i2);
                    m_StatusPane.setText("Distance between " + p1.toString() + " and " + p2.toString() + " is " + String.valueOf(Math.round(dist)) + " lightyears");
                }
                catch (Exception ex)
                {
                }
            }
        }
    }

    public void setBounds(int x, int y, int w, int h)
    {
//        m_MapPanel.setBounds(x,y,w,h);
        m_ScrollPane.setPreferredSize(new Dimension(w,h-50));
        m_ScrollPane.setBounds(0,25,w,h-50);
        super.setBounds(x,y,w,h);
    }

    public void setSize(Dimension d)
    {
        m_ScrollPane.setPreferredSize(new Dimension(d.width,d.height - 50));
        m_ScrollPane.setSize(new Dimension(d.width,d.height - 50));
        super.setSize(d);
    }

    public void paintComponent(Graphics comp)
    {
        m_MapPane.repaint();
        super.paintComponent(comp);
    }

    public void addSolarSystemManagerListener(SolarSystemManagerListener sspml)
    {
        if (!m_SolarSystemListeners.contains(sspml))
            m_SolarSystemListeners.add(sspml);
    }

    public void removeSolarSystemManagerListener(SolarSystemManagerListener sspml)
    {
        if (m_SolarSystemListeners.contains(sspml))
            m_SolarSystemListeners.remove(sspml);
    }

    public void clearSolarSystemManagerListener()
    {
        m_SolarSystemListeners.clear();
    }

    protected void NotifySolarSystemEditRequested(int Index)
    {
        Cursor OldCur = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        InnerSpherePlanet isp = SolarSystemManager.getPlanet(Index);
        SolarSystemDetails ssd = SolarSystemManager.getSystemDetails(isp);

        for (int i = 0; i < m_SolarSystemListeners.size(); i++)
        {
            SolarSystemManagerListener sspml = (SolarSystemManagerListener)m_SolarSystemListeners.elementAt(i);
            sspml.PlanetEditRequest(isp,ssd);
        }

        setCursor(OldCur);
    }


}
