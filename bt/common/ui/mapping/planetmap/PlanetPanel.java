package bt.common.ui.mapping.planetmap;

import java.awt.Cursor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;


import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bt.common.elements.galaxy.SolarSystemDetails;

public class PlanetPanel extends JPanel implements MouseListener
{
    private static Log log = LogFactory.getLog(PlanetPanel.class);
	
    private static final long serialVersionUID = 1;
    
    private SolarSystemDetails m_Details = null;
    private int m_HexSize = 24;
	private WorldMap m_WorldMap = null;
	private int m_CurrentSegment;
	private int m_CurrentSector;
	private int[] m_CurrentPath;
	
	private BufferedImage m_Image = null;
	
	
	public PlanetPanel(SolarSystemDetails details, int hexSize)
	{
		m_Details = details;
		m_HexSize = hexSize;
		
        addMouseListener(this);		
	}
	
    public void paintComponent(Graphics comp)
    {
    	if (m_Details.isMapGenerated())
    	{
	        if (m_Image == null)
	        {
	        	try
	        	{
	        		getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		        	PaintWorker pw = new PaintWorker();
		        	pw.execute();
	        	}
	        	catch (Exception ex)
	        	{
	        		log.error(ex);
	        	}
	        }
	        else
	        {        
		        Graphics2D g2D = (Graphics2D)comp;        
		        boolean ShowHexNeighbours = true;
		
		        g2D.drawImage(m_Image, null , 0, 0);
		
		        if (m_CurrentSegment != -1)
		            m_WorldMap.HighlightSegment(comp,m_CurrentSegment,true);
		        if (m_CurrentSector != -1)
		            m_WorldMap.HighlightSector(g2D,m_CurrentSector,true,ShowHexNeighbours);
		        if (m_CurrentPath != null)
		            m_WorldMap.HighlightPath(g2D,m_CurrentPath,true,ShowHexNeighbours);
	        }
    	}
    }
		
	public Dimension getPreferredSize()
	{
    	if (m_WorldMap != null)
    		return m_WorldMap.getMapSize();
    	
    	return super.getPreferredSize();
	}
	
	public Dimension getSize()
	{
    	if (m_WorldMap != null)
    		return m_WorldMap.getMapSize();
    	
    	return super.getSize();
	}

    public void mouseClicked(MouseEvent evt)
    {
    	if (m_WorldMap != null)
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
    }

    public void mouseEntered(MouseEvent evt) {}
    public void mouseExited(MouseEvent evt) {}
    public void mousePressed(MouseEvent evt) {}
    public void mouseReleased(MouseEvent evt)
    {
        mouseClicked(evt);
    }
    
    private class PaintWorker extends SwingWorker<BufferedImage, String>
    {
		@Override
		protected BufferedImage doInBackground() throws Exception 
		{
			m_WorldMap = new WorldMap(m_Details,m_HexSize);
			setSize(m_WorldMap.getMapSize());
			
	        Dimension size = m_WorldMap.getMapSize();
			BufferedImage image = new BufferedImage(size.width, size.height,BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D)image.getGraphics();
			g.setClip(0, 0, size.width, size.height);
			m_WorldMap.draw(g, 0, 0, WorldMap.DrawMode.MAP);
			return image;
		}
		
		@Override
		public void done()
		{
			try
			{
	        	m_Image = get();			
				repaint();
			}
			catch (Exception ex)
			{
				log.error(ex);
			}
			finally
			{
        		getRootPane().setCursor(Cursor.getDefaultCursor());    		
			}
		}
    	
    }
	
}
