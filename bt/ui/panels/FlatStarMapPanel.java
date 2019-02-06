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
	
    JToolBar _Toolbar;
    JButton[] _TBButtons;
    ButtonGroup _TBGroup;


    FlatStarMapRenderer _Map;
    FlatStarMapPane _MapPane;
    java.awt.Rectangle _MapBounds;
    JScrollPane _ScrollPane;
    JTextArea _StatusPane;
    JPanel _MapPanel;
    JTextField _SearchText;

    protected Vector<SolarSystemManagerListener> _SolarSystemListeners = new Vector<SolarSystemManagerListener>();

    public FlatStarMapPanel()
    {
        _MapPanel = new JPanel(new BorderLayout());

        _Map = new FlatStarMapRenderer();
        _MapPane = new FlatStarMapPane(_Map);
        _MapPane.addMouseListener(this);

        _ScrollPane = new JScrollPane(_MapPane,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        _ScrollPane.setAutoscrolls(true);
        _ScrollPane.setBackground(Color.lightGray);

        _StatusPane = new JTextArea();
        _StatusPane.setEditable(false);
        _StatusPane.setText("");
        _StatusPane.setBackground(Color.lightGray);
        _StatusPane.setColumns(80);
        _StatusPane.setRows(1);

        addToolbar();
        _MapPanel.add(_Toolbar,BorderLayout.NORTH);
        _MapPanel.add(_ScrollPane,BorderLayout.CENTER);
        _MapPanel.add(_StatusPane,BorderLayout.SOUTH);
        add(_MapPanel);


        setVisible(true);
    }
    protected void addToolbar()
    {
        _Toolbar = new JToolBar();
        //m_TBGroup = new ButtonGroup();
        _TBButtons = new JButton[6];

        _Toolbar.putClientProperty("JToolBar.isRollover",Boolean.TRUE);

        _TBButtons[0] = new JButton("Zoom In");
        _TBButtons[0].setToolTipText("Increase Zoom Level");
        _TBButtons[0].setActionCommand("zoom_in");

        _TBButtons[1] = new JButton("Zoom Out");
        _TBButtons[1].setToolTipText("Decrease Zoom Level");
        _TBButtons[1].setActionCommand("zoom_out");

        for (int i = 0; i < 2; i++)
        {
            _Toolbar.add(_TBButtons[i]);
            //m_TBGroup.add(m_TBButtons[i]);
            _TBButtons[i].addActionListener(this);
        }

        JPanel SearchPanel = new JPanel(new BorderLayout());
        _SearchText = new JTextField();
        SearchPanel.add(_SearchText,BorderLayout.CENTER);
        JButton FindButton = new JButton("Find");
        FindButton.setActionCommand("Find");
        FindButton.setToolTipText("Search for System(s)");
        FindButton.addActionListener(this);
        SearchPanel.add(FindButton,BorderLayout.EAST);
        _Toolbar.add(SearchPanel);

    }

    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();

        if (cmd.compareTo("zoom_in") == 0)
        {
            _Map.SetZoom(_Map.GetZoom() + 1.0);
//            m_MapPane.setSize(m_MapPane.getPreferredSize());
//            m_ScrollPane.scrollRectToVisible(m_MapPane.getBounds());
            _MapPane.revalidate();
            _MapPane.repaint();
        }
        if (cmd.compareTo("zoom_out") == 0)
        {
            _Map.SetZoom(_Map.GetZoom() - 1.0);
//            m_MapPane.setSize(m_MapPane.getPreferredSize());
//            m_ScrollPane.scrollRectToVisible(m_MapPane.getBounds());
            _MapPane.revalidate();
            _MapPane.repaint();
        }
        if (cmd.compareTo("Find") == 0)
        {
            _Map.FindSystems(_SearchText.getText());
            _MapPane.repaint();
        }
    }

    public void setCurrentSolarSystem(Point p)
    {
        _Map.selectSolarSystem(p);
        _MapPane.repaint();
    }

    public void toggleSystemSelection(Point p)
    {
        _Map.toggleSolarSystem(p);
        _MapPane.repaint();
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
                int index = _Map.GetSystemIndex(e.getPoint());
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
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            try
            {
                int index = _Map.GetSystemIndex(e.getPoint());
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
                    _StatusPane.setText(p1.toString());
                }
                else
                    _StatusPane.setText("");
            }
            catch (Exception ex)
            {
            	System.out.println("Exception occurred while loading SolarSystem : ");
            	ex.printStackTrace();
            }
        }

        if (e.getButton() == MouseEvent.BUTTON3)
        {
            int i1 = _Map.GetCurrentSystemIndex();
            int i2 = _Map.GetSystemIndex(e.getPoint());
            if (i1 != -1 && i2 != -1)
            {
                double dist = _Map.CalculateDistance(i1,i2);
                try
                {
                    InnerSpherePlanet p1 = SolarSystemManager.getPlanet(i1);
                    InnerSpherePlanet p2 = SolarSystemManager.getPlanet(i2);
                    _StatusPane.setText("Distance between " + p1.toString() + " and " + p2.toString() + " is " + String.valueOf(Math.round(dist)) + " lightyears");
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
        _ScrollPane.setPreferredSize(new Dimension(w,h-50));
        _ScrollPane.setBounds(0,25,w,h-50);
        super.setBounds(x,y,w,h);
    }

    public void setSize(Dimension d)
    {
        _ScrollPane.setPreferredSize(new Dimension(d.width,d.height - 50));
        _ScrollPane.setSize(new Dimension(d.width,d.height - 50));
        super.setSize(d);
    }

    public void paintComponent(Graphics comp)
    {
        _MapPane.repaint();
        super.paintComponent(comp);
    }

    public void addSolarSystemManagerListener(SolarSystemManagerListener sspml)
    {
        if (!_SolarSystemListeners.contains(sspml))
            _SolarSystemListeners.add(sspml);
    }

    public void removeSolarSystemManagerListener(SolarSystemManagerListener sspml)
    {
        if (_SolarSystemListeners.contains(sspml))
            _SolarSystemListeners.remove(sspml);
    }

    public void clearSolarSystemManagerListener()
    {
        _SolarSystemListeners.clear();
    }

    protected void NotifySolarSystemEditRequested(int Index)
    {
        Cursor OldCur = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        InnerSpherePlanet isp = SolarSystemManager.getPlanet(Index);
        SolarSystemDetails ssd = SolarSystemManager.getSystemDetails(isp);

        for (int i = 0; i < _SolarSystemListeners.size(); i++)
        {
            SolarSystemManagerListener sspml = (SolarSystemManagerListener)_SolarSystemListeners.elementAt(i);
            sspml.PlanetEditRequest(isp,ssd);
        }

        setCursor(OldCur);
    }


}
