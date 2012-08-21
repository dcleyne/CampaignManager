package bt.client.ui.forms;

import javax.swing.*;

import bt.client.ui.ClosableEditForm;
import bt.common.elements.unit.Unit;

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
public class UnitMissionPanel extends JPanel implements ClosableEditForm
{
	private static final long serialVersionUID = 1;
	
    private static Log log = LogFactory.getLog(UnitMissionPanel.class);
    protected Unit m_Unit;

    public UnitMissionPanel(Unit u)
    {
    	log.debug("UnitFinancePanel constructor called");
        m_Unit = u;
    }

    public boolean IsClosable()
    {
        return true;
    }

    public void ForceEditCompletion()
    {
    }

}
