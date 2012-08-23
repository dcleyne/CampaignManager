package bt.ui.panels;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;


import javax.swing.JPanel;

import bt.elements.galaxy.SolarSystemDetails;
import bt.ui.renderers.WorldMapRenderer;

public class PlanetPanel extends JPanel implements MouseListener
{
    private static final long serialVersionUID = 1;
    
	private WorldMapRenderer m_WorldMap = null;
	private int m_CurrentSegment;
	private int m_CurrentSector;
	private int[] m_CurrentPath;
	
	private BufferedImage m_Image = null;
	
	
	public PlanetPanel(SolarSystemDetails details, int hexSize)
	{
		this.m_WorldMap = new WorldMapRenderer(details,hexSize);
		this.setSize(m_WorldMap.getMapSize());
		this.invalidate();
		this.repaint();
		
        addMouseListener(this);		
	}
	
    public void paintComponent(Graphics comp)
    {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        if (m_Image == null)
        {
	        Dimension size = m_WorldMap.getMapSize();
			m_Image = new BufferedImage(size.width, size.height,BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D)m_Image.getGraphics();
			g.setClip(0, 0, size.width, size.height);
			m_WorldMap.draw(g, 0, 0, WorldMapRenderer.DrawMode.MAP);
        }
        
        Graphics2D g2D = (Graphics2D)comp;        
        boolean ShowHexNeighbours = true;

        g2D.drawImage(m_Image, null , 0, 0);

        if (m_CurrentSegment != -1)
            m_WorldMap.HighlightSegment(comp,m_CurrentSegment,true);
        if (m_CurrentSector != -1)
            m_WorldMap.HighlightSector(g2D,m_CurrentSector,true,ShowHexNeighbours);
        if (m_CurrentPath != null)
            m_WorldMap.HighlightPath(g2D,m_CurrentPath,true,ShowHexNeighbours);

        this.setCursor(null);
    }
		
	public Dimension getPreferredSize()
	{
		return m_WorldMap.getMapSize();
	}
	
	public Dimension getSize()
	{
		return m_WorldMap.getMapSize();
	}

    public void mouseClicked(MouseEvent evt)
    {
        int x = evt.getX();
        int y = evt.getY();

        if (evt.getButton() == MouseEvent.BUTTON1)
        {
            m_CurrentPath = null;

            m_CurrentSegment = m_WorldMap.GetSelectedSegment(x,y);
            m_CurrentSector = m_WorldMap.GetSelectedSector(x,y);

            repaint();
        }
        else if (evt.getButton() == MouseEvent.BUTTON3)
        {
            int Sector = m_WorldMap.GetSelectedSector(x,y);
            m_CurrentPath = m_WorldMap.GetSectorPath(m_CurrentSector,Sector);
            repaint();
        }
    }

    public void mouseEntered(MouseEvent evt) {}
    public void mouseExited(MouseEvent evt) {}
    public void mousePressed(MouseEvent evt) {}
    public void mouseReleased(MouseEvent evt)
    {
        mouseClicked(evt);
    }
	
}
