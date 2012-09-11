package bt.ui.panels;

import javax.swing.*;

import bt.elements.unit.Unit;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
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
public class UnitFinancePanel extends JPanel implements ClosableEditPanel
{
	private static final long serialVersionUID = 1;
	
    private static Log log = LogFactory.getLog(UnitFinancePanel.class);
    protected Unit m_Unit;

    public UnitFinancePanel(Unit u)
    {
    	log.debug("UnitFinancePanel constructor called");
        m_Unit = u;
    }

    public boolean isClosable()
    {
        return true;
    }

    public void forceEditCompletion()
    {
    }

}
