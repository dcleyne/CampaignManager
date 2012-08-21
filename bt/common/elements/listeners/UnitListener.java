package bt.common.elements.listeners;

import bt.common.elements.*;
import bt.common.elements.personnel.Personnel;
import bt.common.elements.unit.Formation;
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
public interface UnitListener
{
    public void UnitChanged(Unit u);

    public void UnitGroupAdded(Formation ug);
    public void UnitGroupChanged(Formation ug);
    public void UnitGroupRemoved(Formation ug);

    public void PersonnelAdded(Personnel p);
    public void PersonnelRemoved(Personnel p);
    public void PersonnelChanged(Personnel p);

    public void AssetAdded(Asset a);
    public void AssetRemoved(Asset a);
    public void AssetChanged(Asset a);
}
