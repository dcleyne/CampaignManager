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

import bt.common.util.ExceptionUtil;

public class EnvelopeReader extends Thread
{
    private static Log log = LogFactory.getLog(EnvelopeReader.class);

    private DataInputStream m_InputStream;
    private Connection m_Connection;
    private boolean m_Running;
    private boolean m_NameSet;
    
    public EnvelopeReader(Connection c) throws IOException
    {
        super("Envelope Reader (?)");
        m_Connection = c;
        m_InputStream = new DataInputStream(m_Connection.getSocket().getInputStream());
        m_Running = true;
    }
    
    public void run()
    {
        log.debug("EnvelopeReader.run (" + Long.toString(m_Connection.getID() ) + ")");
        while (m_Running) {
            try {            	
            	if (!m_NameSet && m_Connection.getID() != -1)
            	{
                    setName("Envelope Reader (" + Long.toString(m_Connection.getID()) + ")");
            		m_NameSet = true;
            	}                

            	Envelope e = readEnvelope();
                if (e != null) {
                    m_Connection.processEnvelope(e);
                }                
            } catch (Exception ex) {
            	log.fatal(ExceptionUtil.getExceptionStackTrace(ex));
                m_Connection.close();
                shutDown();
            }                   
        }
    }
    
    public synchronized void shutDown()
    {
        log.debug("EnvelopeReader.shutDown");
        m_Running = false;
        notifyAll();
    }
    
    private Envelope readEnvelope() throws IOException
    {
        Envelope e = null;
        try
        {
            long flags = m_InputStream.readLong();
            int len = m_InputStream.readInt();
            if (len > 0)
            {
                byte[] data = new byte[len];
                m_InputStream.readFully(data);
                e = new Envelope(flags, data);
            }
        }
        catch (OutOfMemoryError oome)
        {
        	log.fatal("Bad envelope length!");
            m_Running = false;
            if (m_Connection != null)
                m_Connection.close();
            shutDown();
        }
        catch (Exception ex)
        {
        	if (ex instanceof java.net.SocketException || ex instanceof java.io.EOFException)
        		log.info("Connection (" + Long.toString(m_Connection.getID()) + ") closed");
        	else
        		log.fatal(ExceptionUtil.getExceptionStackTrace(ex));
            m_Running = false;
            if (m_Connection != null)
                m_Connection.close();
            shutDown();
        }
        return e;
        
    }

}
