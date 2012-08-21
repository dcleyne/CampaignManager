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
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EnvelopeWriter extends Thread
{
    private static Log log = LogFactory.getLog(EnvelopeWriter.class);

    private DataOutputStream m_OutputStream;
    private Connection m_Connection;
    private Vector<Envelope> m_Queue;
    private boolean m_Running;
    
    public EnvelopeWriter(Connection c) throws IOException
    {
        super("EnvelopeWriter (" + Long.toString(c.getID()) + ")");
        m_Connection = c;
        m_Queue = new Vector<Envelope>();
        m_OutputStream = new DataOutputStream(c.getSocket().getOutputStream());
        m_Running = true;
    }

    public void run()
    {
        log.debug("EnvelopeWriter.run (" + Long.toString(m_Connection.getID() ) + ")");
        while (m_Running) {
            //log.info("EnvelopeWriter.getNextEnvelope (" + Long.toString(m_Connection.getID() ) + ")");
            Envelope e = getNextEnvelope();
            if (e != null) {
                try 
                {
                    processEnvelope(e);
                    setName("EnvelopeWriter (" + Long.toString(m_Connection.getID()) + ")");
                }
                catch (Exception ex) {
                }
            }
        }
    }
    
    public synchronized void postEnvelope(Envelope e)
    {
        //log.debug("EnvelopeWriter.postEnvelope (" + Long.toString(m_Connection.getID() ) + ")");
        m_Queue.add(e);
        notifyAll();
    }
    
    public synchronized void shutDown()
    {
        log.debug("EnvelopeWriter.shutDown");
        m_Queue.clear();
        m_Running = false;
        notifyAll();        
    }
    
    private synchronized boolean hasPendingSend()
    {
        return m_Queue.size() > 0;
    }
    
    private synchronized Envelope getNextEnvelope()
    {
        Envelope e = null;
        while (!hasPendingSend() && m_Running)
        {
            //log.debug("EnvelopeWriter.getNextEnvelope (" + Long.toString(m_Connection.getID() ) + ")");
            try
            {
                wait();
            }
            catch (InterruptedException ex)
            {}
        }
        if (m_Running)
        {        
            e = m_Queue.elementAt(0);
            m_Queue.remove(0);
        }
        return e;
    }
    
    private void processEnvelope(Envelope e) throws IOException
    {
        //log.debug("EnvelopeWriter.processEnvelope (" + Long.toString(m_Connection.getID() ) + ")");
        m_OutputStream.writeLong(e.getFlags());
        byte[] data = e.getData();
        m_OutputStream.writeInt(data.length);
        m_OutputStream.write(data);
        m_OutputStream.flush();
    }
}
