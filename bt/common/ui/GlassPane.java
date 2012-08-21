/**
 * Created on Feb 14, 2007
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
package bt.common.ui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GlassPane extends JPanel implements AWTEventListener
{
    static final long serialVersionUID = 1;
    
  // Events will be consumed for this window.
    private Window parentWindow;

    // Focus will be returned to this component.
    private Component lastFocusOwner = null;

    public GlassPane()
    {
        super();
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setOpaque(false);
        addMouseListener(new MouseAdapter()
        {
        });
        addKeyListener(new KeyAdapter()
        {
        });
        setInputVerifier(new InputVerifier()
        {
            public boolean verify(JComponent anInput)
            {
                return false;
            }
        });
    }

    public void setVisible(boolean aFlag)
    {
        if (aFlag)
        {
            if (parentWindow == null)
            {
                parentWindow = SwingUtilities.windowForComponent(this);
            }
            Component focusOwner = parentWindow.getFocusOwner();
            if (focusOwner != this)
            {
                lastFocusOwner = focusOwner;
            }
            Toolkit.getDefaultToolkit().addAWTEventListener(this,
                    AWTEvent.KEY_EVENT_MASK);
            requestFocus();
            super.setVisible(aFlag);
        }
        else
        {
            Toolkit.getDefaultToolkit().removeAWTEventListener(this);
            super.setVisible(aFlag);

            if (lastFocusOwner != null)
            {
                lastFocusOwner.requestFocus();
            }
        }
    }

    public void eventDispatched(AWTEvent anEvent)
    {
        if (anEvent instanceof KeyEvent
                && anEvent.getSource() instanceof Component)
        {
            if (SwingUtilities.windowForComponent((Component)anEvent
                    .getSource()) == parentWindow)
            {
                ((KeyEvent)anEvent).consume();
            }
        }
    }
}