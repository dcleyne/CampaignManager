package bt.ui.frames;

import javax.swing.*;

import bt.ui.frames.ClosableEditFrame;
import bt.ui.panels.RandomGamePanel;

/**
 * <p>Title: Miradan Phedd</p>
 *
 * <p>Description: Library for all Miradan related work</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Miradan Phedd</p>
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
public class RandomGameInternalFrame extends JInternalFrame implements ClosableEditFrame
{
	private static final long serialVersionUID = 1;
	
	private RandomGamePanel _DetailsPanel;
	
    public RandomGameInternalFrame()
    {
        super("Create Random Game",true,true,true,true);

        _DetailsPanel = new RandomGamePanel();
        
        JScrollPane detailsScrollPane = new JScrollPane(_DetailsPanel);
        detailsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        detailsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        getContentPane().add(detailsScrollPane);

        setVisible(true);
    }

    public boolean isFrameClosable()
    {
        boolean Closable = true; //m_DetailsPanel.isClosable();
        return Closable;
    }

    public void forceFrameEditCompletion()
    {
        //m_DetailsPanel.forceEditCompletion();
    }
}
