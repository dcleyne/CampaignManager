package bt.common.elements.listeners;

import bt.common.elements.unit.Formation;

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
public interface FormationListener
{
    public void xformationAdded(Formation ug);
    public void xformationChanged(Formation ug);
    public void xformationRemoved(Formation ug);
}
