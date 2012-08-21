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

import java.util.Vector;
import java.net.*;
import java.io.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bt.common.util.ExceptionUtil;

public class ConnectionFactory
{
    private static Log log = LogFactory.getLog(ConnectionFactory.class);

    private static ConnectionFactory theInstance;
    
    private Vector<Connection> m_Connections;
    private long m_NextConnectionID;
    
    private ConnectionFactory()
    {
        m_Connections = new Vector<Connection>();
        m_NextConnectionID = 1;
    }
    
    public static ConnectionFactory getInstance()
    {
        if (theInstance == null)
            theInstance = new ConnectionFactory();
        
        return theInstance;
    }
    
    public Connection getConnection(long ID)
    {
        try
        {
            for (int i = 0; i < m_Connections.size(); i++)
                if (m_Connections.elementAt(i).getID() == ID)
                    return m_Connections.elementAt(i);
        }
        catch (Exception ex)
        {
            log.fatal(ExceptionUtil.getExceptionStackTrace(ex));
        }
        
        return null;
    }
    
    public int getConnectionCount()
    {
        try
        {
        	//First, purge dead connections
            for (int i = m_Connections.size() - 1; i >=0 ; i--)
            	if (m_Connections.elementAt(i).getStatus() == ConnectionStatus.DISCONNECTED)
            		m_Connections.remove(i);
            	
            return m_Connections.size();
        }
        catch (Exception ex)
        {
            log.fatal(ExceptionUtil.getExceptionStackTrace(ex));
        }
        return 0;
    }
    
    public long getNextConnectionID()
    {
        m_NextConnectionID += 1;
        log.info("NextConnectionID (" + Long.toString(m_NextConnectionID) + ")");
        return m_NextConnectionID;
    }
    
    public Connection createServerConnection(Socket s)
    {
        try
        {
            Connection c = new Connection(getNextConnectionID(),s);
            m_Connections.add(c);
            return c;
        }
        catch (Exception ex)
        {
            log.fatal(ExceptionUtil.getExceptionStackTrace(ex));
        }
        return null;
    }

    public Connection createClientConnection(String Host, int Port) throws UnknownHostException, IOException, Exception
    {
        try
        {
            Socket s = new Socket(Host,Port);
            Connection c = new Connection(-1,s);
            m_Connections.add(c);
            return c;
        }
        catch (UnknownHostException ex)
        {
            log.fatal(ExceptionUtil.getExceptionStackTrace(ex));
            throw(ex);
        }
        catch (Exception ex)
        {
            log.fatal(ExceptionUtil.getExceptionStackTrace(ex));
            throw(ex);
        }
    }
    
    public void removeConnection(Connection c)
    {
        try
        {
            if (c.getStatus() != ConnectionStatus.DISCONNECTED)
                c.close();
            m_Connections.remove(c);
        }
        catch (Exception ex)
        {
            log.fatal(ExceptionUtil.getExceptionStackTrace(ex));
        }
    }
    
    public void broadcastMessage(Object o)
    {
        try
        {
            for (int i = 0; i < m_Connections.size(); i++)
                m_Connections.elementAt(i).postMessage(o);
        }
        catch (Exception e)
        {
            log.fatal(ExceptionUtil.getExceptionStackTrace(e));
        }
    }

    public void broadcastMessage(Message m)
    {
        try
        {
            for (int i = 0; i < m_Connections.size(); i++)
                m_Connections.elementAt(i).postMessage(m);
        }
        catch (Exception e)
        {
            log.fatal(ExceptionUtil.getExceptionStackTrace(e));
        }
    }
}
