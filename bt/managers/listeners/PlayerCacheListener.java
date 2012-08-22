package bt.managers.listeners;

import bt.elements.unit.Player;


public interface PlayerCacheListener 
{
    public void PlayerAdded(Player p);
    public void PlayerRemoved(Player p);
    public void PlayerChanged(Player p);
    public void PlayerEditRequested(Player p);
}
