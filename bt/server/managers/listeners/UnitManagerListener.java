package bt.server.managers.listeners;

import bt.common.elements.unit.Unit;

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
public interface UnitManagerListener
{
    public void UnitAdded(Unit u);

    public void UnitRemoved(Unit u);

    public void UnitChanged(Unit u);

    public void UnitEditRequested(Unit u);
}
