/**
 * Created on 23/01/2007
 * <p>Title: Legatus</p>
 *
 * <p>Copyright: Copyright Daniel Cleyne (c) 2004-2006</p>
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
 * @author Megamek project
 * @version 0.1
 * 
 * This code derived from internal class Sprite contained in class BoardView1 from the
 *  Megamek project
 *  http://megamek.sourceforge.net/idx.php?pg=main
 * 
 */
package bt.common.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

import javax.swing.JComponent;

/**
 * Everything in the main map view is either the board or it's a sprite
 * displayed on top of the board.  Most sprites store a transparent image
 * which they draw onto the screen when told to.  Sprites keep a bounds
 * rectangle, so it's easy to tell when they'return onscreen.
 */
public abstract class Sprite implements ImageObserver 
{
    protected static final int TRANSPARENT = 0xFFFF00FF;
	protected JComponent m_ParentComponent = null;
    protected Image m_Image = null;
    protected boolean m_Visible = false;

    
    public Sprite(JComponent parent)
    {
    	m_ParentComponent = parent;
    }
    
    
    /**
     * Do any necessary preparation.  This is called after creation,
     * but before drawing, when a device context is ready to draw with.
     * 
     * DC:- Any subclasses must cater for this call being made multiple times and handle that
     * gracefully. For classes creating dynamic images it should only get called once. For those 
     * using static images it will get called each time a draw operation fails because the outstanding
     * image(s) have not yet been loaded and then once for each image that completes loading.  
     */    
    public void prepare()
    {
        if (m_Image != null) return; //Don't prepare more than once.
    	
        Rectangle bounds = getBounds();
        
        // create image for buffer
        Image tempImage = createImage(bounds.width, bounds.height);
        Graphics2D graph = (Graphics2D)tempImage.getGraphics();

        // fill with key color
        graph.setColor(new Color(TRANSPARENT));
        graph.fillRect(0, 0, bounds.width, bounds.height);
        
        tempImage = drawContent(graph,tempImage,bounds);
    	
        m_Image = createImage(new FilteredImageSource(tempImage.getSource(), new KeyAlphaFilter(TRANSPARENT)));
        
        graph.dispose();
        tempImage.flush();
    }

    protected abstract Image drawContent(Graphics2D graph, Image tempImage, Rectangle bounds);
    
    
    public void repaint()
    {
    	m_ParentComponent.repaint(getBounds());
    }

    /**
     * When we draw our buffered images, it's necessary to implement
     * the ImageObserver interface.  This provides the neccesary
     * functionality.
     */
    public final boolean imageUpdate(Image image, int infoflags, int x, int y, int width, int height) 
    {
        if (infoflags == ImageObserver.ALLBITS) 
        {
            prepare();
            repaint();
            return false;
        }
		return true;
    }

    /**
     * Returns our bounding rectangle.  The coordinates here are stored
     * with the top left corner of the _board_ being 0, 0, so these do
     * not always correspond to screen coordinates.
     */
    public abstract Rectangle getBounds(); 

    /**
     * Are we ready to draw?  By default, checks to see that our buffered
     * image has been created.
     */
    public boolean isReady() {
        return m_Image != null;
    }
    
    public boolean isVisible()
    { return m_Visible; }

    /**
     * Draws this sprite onto the specified graphics context.
     */
    public void draw(Graphics2D g, ImageObserver observer) 
    {
        draw(g, observer, false);
    }

    public final void draw(Graphics2D g, ImageObserver observer, boolean makeTranslucent) {    	
        if (isReady()) 
        {
        	if (m_Visible)
        	{
        		Rectangle bounds = getBounds();
	            if (makeTranslucent) 
	            {
	                Graphics2D g2 = (Graphics2D) g;
	                
	                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
	                g2.drawImage(m_Image, bounds.x, bounds.y, observer);
	                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	            } else 
	            {
	                g.drawImage(m_Image, bounds.x, bounds.y, observer);
                    //g.setColor(Color.pink);
                    //g.drawRect(bounds.x,bounds.y,bounds.width,bounds.height);
	            }
        	}
        } else {
            // grrr... we'll be ready next time!
            prepare();
        }
    }
    
    protected Image createImage(int width, int height)
    {
    	return m_ParentComponent.createImage(width,height);
    }

    protected Image createImage(ImageProducer producer)
    {
    	return m_ParentComponent.createImage(producer);
    }
    
    public void setVisible(boolean visible)
    { 
        if (isVisible() != visible)
        {
        	prepare();
        	
            m_Visible = visible;
            if (m_ParentComponent != null)
                if (getBounds() != null)
                    m_ParentComponent.repaint(getBounds());
                else
                    m_ParentComponent.repaint();
        }
    }

}
