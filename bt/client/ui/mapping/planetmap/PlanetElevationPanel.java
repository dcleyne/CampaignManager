package bt.client.ui.mapping.planetmap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import bt.common.elements.galaxy.SolarSystemDetails;

public class PlanetElevationPanel extends JPanel 
{
    private static final long serialVersionUID = 1;
    
	private WorldMap m_WorldMap = null; 
	
	public PlanetElevationPanel(SolarSystemDetails details, int hexSize)
	{
		this.m_WorldMap = new WorldMap(details,hexSize);
		this.setSize(m_WorldMap.getMapSize());
		this.invalidate();
		this.repaint();
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		Graphics2D g2D = (Graphics2D)g;
		m_WorldMap.draw(g2D,0,0,WorldMap.DrawMode.ELEVATION);
		
		g.setColor(Color.GREEN);		
	}
	
	public Dimension getPreferredSize()
	{
		return m_WorldMap.getMapSize();
	}
	
	public Dimension getSize()
	{
		return m_WorldMap.getMapSize();
	}

}
