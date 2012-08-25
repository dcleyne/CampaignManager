package bt.ui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import bt.elements.Battlemech;
import bt.elements.ItemStatus;
import bt.ui.renderers.BattlemechRenderer;
import bt.util.IndexedRectangle;

public class BattlemechStatusPanel extends JPanel implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 7369612760668862772L;

	private Battlemech _Mech;
	private BufferedImage _MechImage;
	private HashMap<String, HashMap<String, Vector<IndexedRectangle>>> _HotSpots;
	private HashMap<String, Area> _HotSpotAreas;
	private HashMap<String, HashMap<String, Area>> _HotSpotLocalAreas;
	private double _Scale;
	
    private List<Shape> shapes = new ArrayList<Shape>();
    private Shape currentShape = null;

    private ItemStatus _MarkingMode = ItemStatus.DESTROYED;
    
	public BattlemechStatusPanel(double scale)
	{
		_Scale = scale;
	}
	
	public BattlemechStatusPanel(Battlemech mech, double scale)
	{
		super(false);
		
		_Mech = mech;
		_Scale = scale;
		buildMechImage();
		
		setSize(getWidth(), getHeight());
		setPreferredSize(getSize());
		setOpaque(false);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void setMarkingMode(ItemStatus status)
	{
		_MarkingMode = status;
	}
	
	public ItemStatus getMarkingMode()
	{
		return _MarkingMode;
	}
	
	public Battlemech getBattlemech()
	{
		return _Mech;
	}
	
	public double getScale()
	{
		return _Scale;
	}
	
	public void setScale(double scale)
	{
		_Scale = scale;
		buildMechImage();
		repaint();
	}
	
	@Override
	public int getWidth()
	{
		if (_MechImage != null)
			return _MechImage.getWidth();
	
		return 1000;
	}
	
	@Override
	public int getHeight()
	{
		if (_MechImage != null)
			return _MechImage.getHeight();
	
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
		
		if (_MechImage != null)
		{
			Graphics2D g2d = (Graphics2D)g;
			g2d.drawImage(_MechImage, 0, 0, this);
			
	        g2d.setPaint ( Color.red );
			g2d.setStroke(new BasicStroke(5F));
	        for ( Shape shape : shapes )
	        {
	            g2d.draw ( shape );
	        }	        
		}
	}
	
	private void buildMechImage()
	{
		_MechImage = BattlemechRenderer.getInstance().RenderBattlemech(_Mech, _Scale);
		_HotSpots = BattlemechRenderer.getInstance().GenerateBattlemechDiagramHotspots((Graphics2D)_MechImage.getGraphics(), _Mech, _Scale);
		_HotSpotAreas = new HashMap<String, Area>();
		_HotSpotLocalAreas = new HashMap<String, HashMap<String,Area>>();
		for (String key: _HotSpots.keySet())
		{
			HashMap<String,Vector<IndexedRectangle>> rectVectorMap = _HotSpots.get(key);
			Area area = new Area();
			
			HashMap<String,Area> areaMap = new HashMap<String, Area>();
			
			for (String rectVectorKey : rectVectorMap.keySet())
			{
				Vector<IndexedRectangle> rects = rectVectorMap.get(rectVectorKey);
				Area localArea = new Area();
				for (IndexedRectangle rect : rects)
				{
					Area rectArea = new Area(rect.getRectangle());
					area.add(rectArea);
					localArea.add(rectArea);
				}
				areaMap.put(rectVectorKey, localArea);
			}
			_HotSpotAreas.put(key, area);
			_HotSpotLocalAreas.put(key, areaMap);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		Point p = e.getPoint();
		for (String key : _HotSpotAreas.keySet())
		{
			if (_HotSpotAreas.get(key).contains(p))
			{
				System.out.println("User clicked in Area : " + key);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{    
		currentShape = new Line2D.Double ( e.getPoint (), e.getPoint () );
	    shapes.add ( currentShape );
	    repaint ();
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		updateMechStatus();
		currentShape = null;
		shapes.clear();
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e){}
	@Override
	public void mouseExited(MouseEvent e){}
	
    public void mouseDragged ( MouseEvent e )
    {
	    Line2D shape = ( Line2D ) currentShape;
	    shape.setLine ( shape.getP1 (), e.getPoint () );
	    repaint ();
    }

	@Override
	public void mouseMoved(MouseEvent arg0){}
	
	private void updateMechStatus()
	{
	    Line2D shape = ( Line2D ) currentShape;
	    Rectangle shapeRect = new Rectangle(shape.getBounds());
	    if (shapeRect.isEmpty())
	    {
	    	shapeRect.width += 2;
	    	shapeRect.height += 2;
	    }
	    

		Vector<String> coveredLocations = new Vector<String>();
		for (String location : _HotSpotAreas.keySet())
		{
			Area currentShapeArea = new Area(shapeRect);
			currentShapeArea.intersect(_HotSpotAreas.get(location));
			if (!currentShapeArea.isEmpty())
			{
				coveredLocations.add(location);
			}
		}
		
		if (coveredLocations.size() == 1)
		{
			Vector<String> coveredLocalAreas = new Vector<String>();
			// Now we need to drill down to see what areas were covered by the drawing
			HashMap<String, Area> localAreas = _HotSpotLocalAreas.get(coveredLocations.elementAt(0));
			for (String key : localAreas.keySet())
			{
				Area currentShapeArea = new Area(shapeRect);
				currentShapeArea.intersect(localAreas.get(key));
				if (!currentShapeArea.isEmpty())
				{
					coveredLocalAreas.add(key);
				}
			}
			
			if (coveredLocalAreas.size() == 1)
			{
				// This is to ensure that the user did in fact mean to mark an area
				System.out.println("Marking :" + coveredLocations.elementAt(0) + " :" + coveredLocalAreas.elementAt(0));
				
			}
			else
				System.out.println("Marking :" + coveredLocalAreas.toString());
				
		}
	}
	
	
	
}
