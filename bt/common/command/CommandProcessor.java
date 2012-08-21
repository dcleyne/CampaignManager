/**
 * Created on Dec 4, 2007
 * <p>Title: Legatus</p>
 *
 * <p>Copyright: Copyright Daniel Cleyne (c) 2004-2007</p>
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
package bt.common.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bt.common.net.Connection;
import bt.common.net.ConnectionListener;
import bt.common.net.Message;
import bt.common.util.ExceptionUtil;

public class CommandProcessor extends Thread implements ConnectionListener
{
    private static Log log = LogFactory.getLog(CommandProcessor.class);

    private Vector<Message> m_Queue = new Vector<Message>();
    private boolean m_Running;
	
	private HashMap<Command, Vector<CommandHandler>> m_Handlers = new HashMap<Command, Vector<CommandHandler>>();
	
	private Vector<CommandProcessorListener> m_Listeners = new Vector<CommandProcessorListener>();
	
	
	public CommandProcessor(String name)
	{
		super("Command Processor : " + name);
		m_Running = true;
	}
	
	public void registerHandler(Command c, CommandHandler ch)
	{
		if (!m_Handlers.containsKey(c))
			m_Handlers.put(c, new Vector<CommandHandler>());
		
		m_Handlers.get(c).add(ch);
	}
	
    public void run()
    {
        log.debug("Running");
        notifyRunning();
        while (m_Running) {
            Message m = getNextMessage();
            if (m != null) {
                try 
                {
                    processMessage(m);
                }
                catch (Exception ex) {
                }
            }
        }
    }
    
    public synchronized void shutDown()
    {
        log.debug("Shut down");
        notifyShutdown();
        m_Queue.clear();
        m_Running = false;
        notifyAll();
    }
    
    private boolean hasPendingMessage()
    {
        return m_Queue.size() > 0;
    }
    
    private synchronized Message getNextMessage()
    {
        while (!hasPendingMessage() && m_Running)
        {
            try
            {
                wait();
            }
            catch (InterruptedException ex)
            {}
        }
        if (m_Running)
        {        
        	Message m = m_Queue.elementAt(0);
            m_Queue.remove(0);
            return m;
        }
        return null;
    }
    
    private synchronized void processMessage(Message m) throws IOException
    {
    	long connectionID = m.getConnectionID();
    	Object[] payloads = m.getData();
    	Object payload = payloads[0];
    	if (payload instanceof CommandPacket)
    	{
    		CommandPacket c = (CommandPacket)payload;
    		Vector<CommandHandler> handlers = m_Handlers.get(c.getType());
    		if (handlers != null)
    		{
		        log.debug("Processing Command : " + c.toString());            
    			for (CommandHandler ch: handlers)
    			{
    				try
    				{
    					ch.handleCommand(connectionID, c);
    				}
    				catch (Exception ex)
    				{
    					log.error(ExceptionUtil.getExceptionStackTrace(ex));
    				}
    			}
    		}
    	}    	
    }	
	
	public void addListener(CommandProcessorListener l)
	{
		if (!m_Listeners.contains(l))
			m_Listeners.add(l);
	}

	public void removeListener(CommandProcessorListener l)
	{
		if (m_Listeners.contains(l))
			m_Listeners.remove(l);
	}
	
    public synchronized void dataReceived(Message m)
    {
    	m_Queue.add(m);
    	notifyAll();
    }
    
    public void disconnected(Connection c)
    {
    	c.removeListener(this);
    	for (CommandProcessorListener cpl: m_Listeners)
    		cpl.disconnected(c);
    }
    
    public void notifyRunning()
    {
    	for (CommandProcessorListener cpl: m_Listeners)
    		cpl.running();
    }

    public void notifyShutdown()
    {
    	for (CommandProcessorListener cpl: m_Listeners)
    		cpl.shutdown();
    }
}
