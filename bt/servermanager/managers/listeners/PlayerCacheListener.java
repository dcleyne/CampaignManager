package bt.servermanager.managers.listeners;

import bt.common.elements.unit.Player;

public interface PlayerCacheListener 
{
    public void PlayerAdded(Player p);
    public void PlayerRemoved(Player p);
    public void PlayerChanged(Player p);
    public void PlayerEditRequested(Player p);
}
