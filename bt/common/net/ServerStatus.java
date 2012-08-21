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
package bt.common.net;

import java.io.*;

public class ServerStatus extends SystemStatus implements Serializable
{
    static final long serialVersionUID = 1;
	
    public ServerStatus(String serverName, String serverAddress, long startTime, long connectionCount)
    {
    	super(serverName, serverAddress, startTime, connectionCount);
    }
    

    public String toString()
    {
        return super.toString();
    }
        
}
