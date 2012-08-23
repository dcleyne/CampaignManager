package bt.ui.panes;

import javax.swing.*;

import bt.ui.renderers.FlatStarMapRenderer;

import java.awt.*;

/**
 * Title:        Space Conquest Game
 * Description:  The same old Game
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author Daniel Cleyne
 * @version 1.0
 */

public class FlatStarMapPane extends JPanel
{
	private static final long serialVersionUID = 1;
	
    FlatStarMapRenderer m_Map;

    public FlatStarMapPane(FlatStarMapRenderer Map)
    {
        m_Map = Map;
    }


    public Dimension getPreferredSize()
    {
        return m_Map.getMapSize();
//        return new Dimension(100,100);
    }

    public Dimension getMinimumSize()
    {
        return new Dimension(100,100);
    }

    public void paintComponent(Graphics comp)
    {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        m_Map.draw(comp,this);
        this.setCursor(null);
    }


}
