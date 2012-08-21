/**
 * Created on 24/11/2005
 * <p>Title: Miradan Phedd</p>
 *
 * <p>Description: Library for all Miradan related work</p>
 *
 * <p>Copyright: Copyright (c) 2004-2005</p>
 *
 * <p>Company: Miradan Phedd</p>
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
package bt.servermanager;

import java.util.Vector;

import bt.common.elements.galaxy.SolarSystemDetails;
import bt.common.elements.unit.Player;
import bt.common.elements.unit.PlayerSummary;
import bt.common.elements.unit.Unit;
import bt.common.elements.unit.UnitSummary;
import bt.common.net.ServerStatus;

public interface ServerManagerListener
{
    public void error(String Error);
    public void messageStart(String message);
    public void messageEnd();    
    public void connected();
    public void connectionFailed(String errorMessage);
    public void disconnected();
    public void serverShuttingDown();
    public void serverStatus(ServerStatus status);
    
    public void solarSystemDetails(Long id, SolarSystemDetails ssd);
    public void playerList(Vector<PlayerSummary> playerSummaries);
    public void playerDetails(String playerName, Player unit);
    public void unitList(Vector<UnitSummary> unitSummaries);
    public void unitDetails(String unitName, Unit unit);
}
