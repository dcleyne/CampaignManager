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

import java.net.UnknownHostException;

import java.util.Vector;

import javax.swing.UIManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

import bt.common.command.Command;
import bt.common.command.CommandPacket;
import bt.common.command.CommandHandler;
import bt.common.command.CommandProcessor;
import bt.common.command.CommandProcessorListener;
import bt.common.elements.unit.Player;
import bt.common.elements.unit.Unit;
import bt.common.net.Connection;
import bt.common.net.ConnectionFactory;
import bt.common.net.ConnectionListener;
import bt.common.net.ServerStatus;
import bt.common.util.ExceptionUtil;
import bt.common.util.PropertyUtil;
import bt.common.util.SwingWorker;
import bt.client.ui.ClientFrame;

public class Client implements CommandProcessorListener
{
    private static Log log = LogFactory.getLog(Client.class);
    private String m_ClientVersion = "0.0.020.001";
    private Connection m_Connection;
    private Player m_Participant;
    private CommandProcessor m_CommandProcessor;
    
    private Vector<ClientListener> m_Listeners;

    public Client()
    {
        m_Listeners = new Vector<ClientListener>();
        try
        {
            PropertyUtil.loadSystemProperties("bt/common/system.properties");
            PropertyUtil.loadSystemProperties("bt/client/client.properties");

            
            m_CommandProcessor = new CommandProcessor("Client");
            m_CommandProcessor.addListener(this);
            
            registerHandlers();
            
            m_CommandProcessor.start();
        }
        catch (Exception e)
        {
            log.error(ExceptionUtil.getExceptionStackTrace(e));
        }
    }    

    public void running()
    {
    }
    
    public void shutdown()
    {
    }
    
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        PropertyConfigurator.configure(Loader.getResource("bt/client/log4j.properties"));
        log.info("Starting Campaing Manager Client");

        try
        {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            final Client client = new Client();
            SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    try {(new ClientFrame(client)).setVisible(true);}
                    catch (Exception e)
                    { return "Failed";}
                    return "Done";
                }
             };
             worker.start();
            
        }
        catch (Exception e)
        {
            log.error(ExceptionUtil.getExceptionStackTrace(e));
        }

    }

    public void Connect(String ServerName, int ServerPort) throws Exception
    {
        try
        {
            m_Connection = ConnectionFactory.getInstance().createClientConnection(ServerName,ServerPort);
            m_Connection.addListener(m_CommandProcessor);
            m_Connection.open();
            
            m_Connection.postMessage(new CommandPacket(Command.Handshake,"Campaign Manager Client"));
        }
        catch (UnknownHostException ex)
        {
            String StackTrace = ExceptionUtil.getExceptionStackTrace(ex);
            log.error(StackTrace);
            notifyConnectionFailed("Unknown Host : " + ServerName + ":" + ServerPort);
        }
        catch (Exception e)
        {
            String StackTrace = ExceptionUtil.getExceptionStackTrace(e);
            log.debug("Exception thrown in Connect : " + StackTrace);
            log.error(StackTrace);
            notifyConnectionFailed(e.toString());
        }
    }
    
    public void addConnectionListener(ConnectionListener l) throws Exception
    {
        if (m_Connection == null)
            throw new Exception("Not Connected!");
        
        m_Connection.addListener(l);
    }
    
    public void removeConnectionListener(ConnectionListener l) throws Exception
    {
        if (m_Connection == null)
            throw new Exception("Not Connected!");
        
        m_Connection.removeListener(l);
    }
    
    public void disconnect()
    {
        try
        {
            m_Connection.removeListener(m_CommandProcessor);
            m_Connection.close();
            m_Connection = null;
        }
        catch (Exception ex)
        {
            log.fatal("Failed to disconnect!!! " + ExceptionUtil.getExceptionStackTrace(ex));
        }
    }

    public void setParticipant(Player p)
    {
        m_Participant = p;
    }
    
    public Player getParticipant()
    {
        if (m_Participant == null)
            m_Participant = new Player();
        
        return m_Participant; 
    }
    
    public void disconnected(final Connection c)
    {
        notifyDisconnected();
        ConnectionFactory.getInstance().removeConnection(c);
    }
    
    public void addListener(ClientListener l)
    {
        if (!m_Listeners.contains(l))
            m_Listeners.add(l);
    }
    
    public void removeListener(ClientListener l)
    {
        if (m_Listeners.contains(l))
            m_Listeners.remove(l);
    }
    
    private void notifyError(CommandPacket c)
    {
        String ErrorMessage = c.getData(0).toString();
        log.error(ErrorMessage);
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).error(ErrorMessage);
    }
    
    private void notifyDisconnected()
    {
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).disconnected();
    }

    private void notifyConnected()
    {
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).connected();
    }

    private void notifyConnectionFailed(String e)
    {
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).connectionFailed(e);
    }

    private void notifyServerShuttingDown()
    {
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).serverShuttingDown();
    }

    private void notifyServerStatus(ServerStatus status)
    {
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).serverStatus(status);
    }
    
    private void processPlayerInfoRequested()
    {
        log.info("Player Info Requested");
        try
        {
            sendPlayerInfo();
        }
        catch (Exception e)
        {
            log.info("Exception thrown in PlayerInfoRequested :" + e.toString());
            log.error(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void processPlayerUnitList(CommandPacket c)
    {
    	Vector<String> unitList = (Vector<String>)c.getData(0);
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).playerUnitList(unitList);
    }
    
    private void processPlayerUnitDetails(CommandPacket c)
    {
    	Unit u = (Unit)c.getData(0);
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).playerUnitDetails(u);
    }
    
    private void sendClientVersion()
    {
    	try
    	{
	        CommandPacket c = new CommandPacket(Command.Version);
	        c.addObject(m_ClientVersion);
	        m_Connection.postMessage(c);
    	}
        catch (Exception ex)
        {
        	throw new RuntimeException(ExceptionUtil.getExceptionStackTrace(ex));
        }
    }

    private void sendConnectionType()
    {
    	try
    	{
	        m_Connection.postMessage(new CommandPacket(Command.ConnectionType,new Integer(1)));
    	}
        catch (Exception ex)
        {
        	throw new RuntimeException(ExceptionUtil.getExceptionStackTrace(ex));
        }
    }

    private void sendPlayerInfo() throws Exception
    {
    	Object[] data = new Object[2];
    	data[0] = m_Participant.getName();
    	data[1] = m_Participant.getPassword();
        CommandPacket c = new CommandPacket(Command.PlayerInfo,data);
        m_Connection.postMessage(c);        
    }
    
    public void updateServerStatus(CommandPacket c)
    {
        ServerStatus status = (ServerStatus)c.getData(0);
        notifyServerStatus(status);
    }
    
    public void requestUnits()
    {
        try
        {
            m_Connection.postMessage(new CommandPacket(Command.PlayerUnitList));
        }
        catch (Exception e)
        {
            String StackTrace = ExceptionUtil.getExceptionStackTrace(e);
            log.debug("Exception thrown in requestUnits : " + StackTrace);
            log.error(StackTrace);
        }
    }
    
    public void requestUnitDetails(String name)
    {
        try
        {
            m_Connection.postMessage(new CommandPacket(Command.UnitDetails,name));
        }
        catch (Exception e)
        {
            String StackTrace = ExceptionUtil.getExceptionStackTrace(e);
            log.debug("Exception thrown in requestUnits : " + StackTrace);
            log.error(StackTrace);
        }
    }
    
    public void askServerStatus()
    {
        try
        {
            m_Connection.postMessage(new CommandPacket(Command.ServerStatus));
        }
        catch (Exception e)
        {
            String StackTrace = ExceptionUtil.getExceptionStackTrace(e);
            log.debug("Exception thrown in askServerStatus : " + StackTrace);
            log.error(StackTrace);
        }
    }

    @Override
    public String toString()
    {
        if (m_Connection != null)
            return m_Connection.toString();
        else
            return "Not Connected!";
    }
    
    
    public void registerHandler(Command command, CommandHandler ch)
    {
    	m_CommandProcessor.registerHandler(command,ch);
    }
    
    
    private void registerHandlers()
    {
    	registerHandler(Command.Disconnect,new CommandHandler(){public void handleCommand(long id, CommandPacket c){disconnect();}});
    	registerHandler(Command.Error,new CommandHandler(){public void handleCommand(long id, CommandPacket c){notifyError(c);}});
    	registerHandler(Command.ServerPassword,new CommandHandler(){public void handleCommand(long id, CommandPacket c){}});
    	registerHandler(Command.Version,new CommandHandler(){public void handleCommand(long id, CommandPacket c){sendClientVersion();}});
    	registerHandler(Command.ConnectionType,new CommandHandler(){public void handleCommand(long id, CommandPacket c){sendConnectionType();}});
    	registerHandler(Command.ServerStatus,new CommandHandler(){public void handleCommand(long id, CommandPacket c){updateServerStatus(c);}});
    	registerHandler(Command.ClientConnected,new CommandHandler(){public void handleCommand(long id, CommandPacket c){notifyConnected();}});
    	registerHandler(Command.ServerShutdown,new CommandHandler(){public void handleCommand(long id, CommandPacket c){notifyServerShuttingDown();}});
    	registerHandler(Command.PlayerInfo,new CommandHandler(){public void handleCommand(long id, CommandPacket c){processPlayerInfoRequested();}});
    	
    	registerHandler(Command.PlayerUnitList,new CommandHandler(){public void handleCommand(long id, CommandPacket c){processPlayerUnitList(c);}});
    	registerHandler(Command.UnitDetails,new CommandHandler(){public void handleCommand(long id, CommandPacket c){processPlayerUnitDetails(c);}});
    }
    
    
}
