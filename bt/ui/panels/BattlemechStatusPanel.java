package bt.ui.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import bt.elements.Battlemech;
import bt.ui.renderers.BattlemechRenderer;

public class BattlemechStatusPanel extends JPanel 
{
	private static final long serialVersionUID = 7369612760668862772L;

	private Battlemech _mech;
	private BufferedImage _mechImage;

	public BattlemechStatusPanel()
	{
	}
	
	public BattlemechStatusPanel(Battlemech mech)
	{
		super(false);
		
		_mech = mech;
		_mechImage = BattlemechRenderer.getInstance().RenderBattlemech(mech);
		
		setSize(getWidth(), getHeight());
		setPreferredSize(getSize());
		setOpaque(false);
	}
	
	public Battlemech getBattlemech()
	{
		return _mech;
	}
	
	@Override
	public int getWidth()
	{
		if (_mechImage != null)
			return _mechImage.getWidth();
	
		return 1000;
	}
	
	@Override
	public int getHeight()
	{
		if (_mechImage != null)
			return _mechImage.getHeight();
	
		return 1000;
	}
	
	@Override
	public Dimension getSize()
	{
		return new Dimension(getWidth(), getHeight());
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		return getSize();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if (_mechImage != null)
			((Graphics2D)g).drawImage(_mechImage, 0, 0, this);
	}
}
