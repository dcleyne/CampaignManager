/**
 * Created on 11/11/2005
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Message implements Serializable
{
    static final long serialVersionUID = 1;
    
    private static Log log = LogFactory.getLog(Message.class);

    private long m_ConnectionID;
    private Object[] m_Data;
    
    public Message(long ConnectionID, Object[] Data)
    {
    	log.debug("new Message : " + this.toString() + " for connection : " + Long.toString(ConnectionID));
        m_ConnectionID = ConnectionID;
        m_Data = Data;
    }

    public Message(long ConnectionID, Object Data)
    {
        m_ConnectionID = ConnectionID;
        m_Data = new Object[1];
        m_Data[0] = Data;
    }

    public final long getConnectionID()
    { return m_ConnectionID; }
    
    public final void setConnectionID(long ID)
    { m_ConnectionID = ID; }
    
    public final Object[] getData()
    { return m_Data; }
    
    public String toString()
    {
        String output = "Message :: ";
        for (int i = 0; i < m_Data.length; i++)
            output += m_Data[i].toString() + " ";
        return output;  
    }
}
