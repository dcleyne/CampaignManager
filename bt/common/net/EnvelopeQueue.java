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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EnvelopeQueue
{
    private static Log log = LogFactory.getLog(EnvelopeQueue.class);

    private Vector<Envelope> m_Queue;
    
    private static EnvelopeQueue theInstance;
    
    private EnvelopeQueue()
    {
    	log.debug("new MessageQueue : " + this.toString());
        m_Queue = new Vector<Envelope>();
    }
    
    public static EnvelopeQueue getInstance()
    {
        if (theInstance == null)
            theInstance = new EnvelopeQueue();
        
        return theInstance;
    }
    
    public synchronized void pushEnvelope(Envelope e)
    { m_Queue.add(e); }
    
    public synchronized Envelope getEnvelope()
    {
        if (m_Queue.size() == 0) return null;
        
        Envelope e = m_Queue.elementAt(0);
        m_Queue.remove(0);
        
        return e;
    }
}
