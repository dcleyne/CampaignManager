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
package bt.client;

import java.util.Vector;

import bt.common.elements.unit.Unit;
import bt.common.net.ServerStatus;

public interface ClientListener
{
    public void error(String Error);
    public void connected();
    public void connectionFailed(String errorMessage);
    public void disconnected();
    public void serverShuttingDown();
    public void serverStatus(ServerStatus status);
    
    public void playerUnitList(Vector<String> unitList);
    public void playerUnitDetails(Unit u);
}
