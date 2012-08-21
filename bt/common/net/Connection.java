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

import java.net.*;
import java.io.*;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Connection
{
    private static Log log = LogFactory.getLog(Connection.class);

    private long m_ID;
    private Socket m_Socket;
    private ConnectionStatus m_Status;
    private EnvelopeReader m_Reader;
    private EnvelopeWriter m_Writer;
    private int m_ConnectionType = 1; //1 is for client, 0 is for admin

    private Vector<ConnectionListener> m_Listeners;
    
    public Connection(long ID, Socket s)
    {
        m_ID = ID;
        m_Socket = s;
        m_Listeners = new Vector<ConnectionListener>();
        m_Status = ConnectionStatus.PENDING;
    }
    
    public void setConnectionType(int type)
    { m_ConnectionType = type; }
    
    public int getConnectionType()
    { return m_ConnectionType; }
    
    public void open()
    {
        try
        {
            log.debug("new EnvelopeReader");
            m_Reader = new EnvelopeReader(this);
            
            log.debug("new EnvelopeWriter");
            m_Writer = new EnvelopeWriter(this);
            
            m_Reader.start();
            m_Writer.start();
        }
        catch (IOException e)
        {
            log.info("IOException caught in open()");
            log.error(e);
        }
    }
    
    public final long getID()
    { return m_ID; }
    
    public final Socket getSocket()
    { return m_Socket; }
    
    public final ConnectionStatus getStatus()
    { return m_Status; }
    
    public final void setConnected()
    { m_Status = ConnectionStatus.CONNECTED; }
    
    public synchronized void close()
    {
        if (m_Status == ConnectionStatus.DISCONNECTED)
            return;
        
        log.debug(this.toString() + " : Connection.close()");
        m_Reader.shutDown();
        m_Writer.shutDown();
        m_Reader = null;
        m_Writer = null;
        if (m_Socket != null)
        {
            try
            {
                if (m_Socket.isConnected())
                    m_Socket.close();
            }
            catch (IOException e)
            {
                //TODO Not entirely sure if I care.
            }
        }
        m_Status = ConnectionStatus.DISCONNECTED;
        notifyDisconnected();
    }
    
    public void processEnvelope(Envelope e)
    {
        try
        {
            byte[] data = e.getData();
            
            ByteArrayInputStream BAStream = new ByteArrayInputStream(data);
            GZIPInputStream ZIPStream = new GZIPInputStream(BAStream);
            ObjectInputStream inStream = new ObjectInputStream(ZIPStream);
            //ObjectInputStream inStream = new ObjectInputStream(BAStream);
            Message m = (Message)inStream.readObject();
            
            // If we're a client and not fully connected
            if (getID() == -1)
            {
                m_ID = m.getConnectionID();
                m_Status = ConnectionStatus.CONNECTED;
            }
            if (m.getConnectionID() == -1)
            	m.setConnectionID(m_ID);
            
            notifyDataReceived(m);
        }
        catch (IOException ex)
        {
            log.info("IOException caught in processEnvelope");
            log.error(ex);
        }
        catch (ClassNotFoundException cnfex)
        {
            log.info("ClassNotFoundException caught in processEnvelope");
            log.error(cnfex);
        }
    }

    public void postMessage(Object o) throws IOException, Exception
    {
        Message m = new Message(getID(),o);
        postMessage(m);
    }

    public void postMessage(Object[] oArray) throws IOException, Exception
    {
        Message m = new Message(getID(),oArray);
        postMessage(m);
    }

    public void postMessage(Message m) throws IOException, Exception
    {
        postMessage(0,m);
    }
    
    public synchronized void postMessage(long Flags, Message m) throws IOException, Exception
    {
        //if (getID() == -1) throw new Exception("Connection not fully established");
    	if (getStatus() != ConnectionStatus.DISCONNECTED)
    	{        
	        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
	        GZIPOutputStream ZIPStream = new GZIPOutputStream(outByteStream);
	        ObjectOutputStream outStream = new ObjectOutputStream(ZIPStream);
	//        ObjectOutputStream outStream = new ObjectOutputStream(outByteStream);
	        outStream.writeObject(m);
	        outStream.flush();
	        outStream.close();
	        byte[] data = outByteStream.toByteArray();
	        Envelope e = new Envelope(Flags,data);
	        m_Writer.postEnvelope(e);
    	}
    }
    
    public void addListener(ConnectionListener l)
    {
        if (!m_Listeners.contains(l))
            m_Listeners.add(l);
    }
    
    public void removeListener(ConnectionListener l)
    {
        if (m_Listeners.contains(l))
            m_Listeners.remove(l);
    }
    
    private void notifyDataReceived(Message m)
    {
        //log.debug("notifyDataSentMessage (" + getID() + ")");
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).dataReceived(m);
    }

    private void notifyDisconnected()
    {
        log.debug(this.toString() + " Disconnected");
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).disconnected(this);
    }
    
    public String toString()
    {
        String retVal = "[";
        retVal += m_Socket.getInetAddress().getHostName() + "-";
        retVal += m_Socket.getInetAddress().getHostAddress() + "]";
        return retVal;
    }

}
