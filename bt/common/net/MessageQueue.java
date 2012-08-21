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

public class MessageQueue
{
    private static Log log = LogFactory.getLog(MessageQueue.class);

    private Vector<Message> m_Queue;
    
    private static MessageQueue theInstance;
    
    private MessageQueue()
    {
    	log.debug("new MessageQueue : " + this.toString());
        m_Queue = new Vector<Message>();
    }
    
    public static MessageQueue getInstance()
    {
        if (theInstance == null)
            theInstance = new MessageQueue();
        
        return theInstance;
    }
    
    public synchronized void pushMessage(Message m)
    { m_Queue.add(m); }
    
    public synchronized Message getMessage()
    {
        if (m_Queue.size() == 0) return null;
        
        Message m = m_Queue.elementAt(0);
        m_Queue.remove(0);
        
        return m;
    }
}
