/**
 * Created on 4/01/2007
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
 * @author Daniel Cleyne
 * @version 0.1
 */
package bt.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

public class ClientDesktopPane extends JDesktopPane
{
    private static final long serialVersionUID = 1;
    private ImageIcon m_Icon;
	
	public ClientDesktopPane()
	{
		super();
		m_Icon = new ImageIcon("data/images/cruis.jpg");
	}
	
    protected void paintComponent(Graphics g) 
    {
    	//super.paintComponent(g);
        if (isOpaque()) 
        { //paint background
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        if (m_Icon != null) 
        {
            Insets insets = getInsets();
            int iconWidth = m_Icon.getIconWidth();
            int iconHeight = m_Icon.getIconHeight();
            
            Graphics2D g2d = (Graphics2D)g.create();

            Rectangle clipRect = new Rectangle();
            Rectangle iconRect = new Rectangle();
            int pad = 0;
            
            g.getClipBounds(clipRect);
            
            int iconX = insets.left;
            while (iconX <= insets.left + (this.getWidth() - insets.right)) 
            {
                int iconY = insets.top;
                while (iconY <= insets.top + (this.getHeight() - insets.bottom)) 
                {
	            	iconRect.setBounds(iconX, iconY, iconWidth, iconHeight);
		        	if (iconRect.intersects(clipRect)) 
		        	{
		        		m_Icon.paintIcon(this, g2d, iconX, iconY);
		        	}
	                iconY += (iconHeight + pad);
                }
                iconX += (iconWidth + pad);
            }
            g2d.dispose(); //clean up
        }
        else
        {
        	Color back = getBackground();
            g.setColor(Color.GREEN);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(back);
        }
        
    }    
	
}
