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
package bt.servermanager;

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
import bt.common.elements.galaxy.SolarSystemDetails;
import bt.common.elements.unit.Player;
import bt.common.elements.unit.PlayerSummary;
import bt.common.elements.unit.Unit;
import bt.common.elements.unit.UnitSummary;
import bt.common.net.Connection;
import bt.common.net.ConnectionFactory;
import bt.common.net.ConnectionListener;
import bt.common.net.ServerStatus;
import bt.common.util.ExceptionUtil;
import bt.common.util.PropertyUtil;
import bt.servermanager.ui.ServerManagerFrame;

public class ServerManager implements CommandProcessorListener
{
    private static Log log = LogFactory.getLog(ServerManager.class);
    private String m_ClientVersion = "0.0.000.001";
    private Connection m_Connection;
    private Player m_Participant;
    private CommandProcessor m_CommandProcessor;
    
    private Vector<ServerManagerListener> m_Listeners;

    public ServerManager()
    {
        m_Listeners = new Vector<ServerManagerListener>();
        try
        {
            PropertyUtil.loadSystemProperties("bt/common/system.properties");
            PropertyUtil.loadSystemProperties("bt/client/client.properties");

            m_CommandProcessor = new CommandProcessor("Server Manager");
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
        log.info("Starting Campaign Manager Server Manager");

        try
        {
        	
        	boolean validArgs = true;
        	boolean silentShutdown = false;
        	String serverName = "";
        	int serverPort = 0;
        	String serverPassword = "";
        	
        	for (String arg: args)
        	{
        		String subStr = arg.substring(0,2);
        		if (subStr.equalsIgnoreCase("-s"))
        			serverName = arg.substring(2);
        		else if (subStr.equalsIgnoreCase("-p"))
        			serverPort = Integer.parseInt(arg.substring(2));
        		else if (subStr.equalsIgnoreCase("-w"))
        			serverPassword = arg.substring(2);
        		else if (subStr.equalsIgnoreCase("-x"))
        			silentShutdown = true;
        		else
        			validArgs = false;
        	
        	}
        	if (!validArgs)
        	{
        		StringBuilder b = new StringBuilder();
        		b.append("USAGE: ServerManager -s[Server Address] -p[Server Port] -w[Server Password] <-x>\n");
        		b.append("    -s : DNS name or IP address of the server\n");
        		b.append("    -p : Port number that the server is listening on\n");
        		b.append("    -w : The Admin Password of the server\n");
        		b.append("    -x : Send server command to shut down\n");
        		System.out.println(b.toString());
        		System.exit(0);
        	}

            final ServerManager client = new ServerManager();
        	client.getParticipant().setPassword(serverPassword);
            if (silentShutdown && serverName.length() > 0 && serverPort > 0)
            {
            	try
            	{
            		client.addListener(new ServerManagerListener(){
            			public void error(String error){log.error(error);}
            		    public void messageStart(String message){};
            		    public void messageEnd(){};                			
            			public void serverStatus(ServerStatus status){}
            			public void serverShuttingDown(){
            				log.info("Server Shutting down");
            			}
            			public void connected(){client.shutdownServer();}
            			public void disconnected(){System.exit(0);}
            			public void connectionFailed(String reason){
            				log.error("Connection Failed: " + reason);
            			}
            			public void solarSystemDetails(Long id, SolarSystemDetails ssd){};
            			public void unitDetails(String unitName, Unit unit){}
						public void unitList(Vector<UnitSummary> unitNames) {}
						public void playerList(Vector<PlayerSummary> playerSummaries) {}
						public void playerDetails(String playerName, Player unit) {}
            		});
                	client.getParticipant().setName("Server Admin");
	            	client.Connect(serverName, serverPort);
            	}
            	catch (Exception ex)
            	{
	            	log.info("Failed to shut down Server " + serverName + "[" + Integer.toString(serverPort) + "]");
	            	log.info(ex.toString());            		
            	}
            }
            else
            {            
            	final ServerManagerFrame serverFrame = new ServerManagerFrame(client);
	            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            	serverFrame.setVisible(true);
            }
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
        ConnectionFactory.getInstance().removeConnection(c);
        notifyDisconnected();
    }
    
    public void addListener(ServerManagerListener l)
    {
        if (!m_Listeners.contains(l))
            m_Listeners.add(l);
    }
    
    public void removeListener(ServerManagerListener l)
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
    
    private void processSolarSystemDetails(CommandPacket c)
    {
    	Long id = (Long)c.getData(0);
    	SolarSystemDetails ssd = (SolarSystemDetails)c.getData(1);
    	
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).solarSystemDetails(id, ssd);
    }
    
    @SuppressWarnings("unchecked")
	private void processPlayerList(CommandPacket c)
    {
    	Vector<PlayerSummary> summaries = (Vector<PlayerSummary>)c.getData(0); 
    	
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).playerList(summaries);
    }
    
    private void processPlayerDetails(CommandPacket c)
    {
    	String playerName = (String)c.getData(0);
    	Player player = (Player)c.getData(1);
    	
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).playerDetails(playerName, player);
    }
    
    @SuppressWarnings("unchecked")
	private void processUnitList(CommandPacket c)
    {
    	Vector<UnitSummary> summaries = (Vector<UnitSummary>)c.getData(0); 
    	
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).unitList(summaries);
    }
    
    private void processUnitDetails(CommandPacket c)
    {
    	String unitName = (String)c.getData(0);
    	Unit unit = (Unit)c.getData(1);
    	
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).unitDetails(unitName, unit);
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
    
    private void sendServerPassword()
    {
    	try
    	{
	        m_Connection.postMessage(new CommandPacket(Command.ServerPassword,getParticipant().getPassword()));
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
	        m_Connection.postMessage(new CommandPacket(Command.ConnectionType,new Integer(0)));
    	}
        catch (Exception ex)
        {
        	throw new RuntimeException(ExceptionUtil.getExceptionStackTrace(ex));
        }
    }

    private void sendPlayerInfo() throws Exception
    {
        Object[] args = new Object[2];
        args[0] = "Admin";
        args[1] = m_Participant.getPassword();
        CommandPacket c = new CommandPacket(Command.PlayerInfo,args);
        m_Connection.postMessage(c);        
    }

    private void notifyMessageStart(CommandPacket c)    
    {
        String Message = (String)c.getData(0);
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).messageStart(Message);
    }
    
    private void notifyMessageEnd()
    {
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).messageEnd();
    }
    
    public void updateServerStatus(CommandPacket c)
    {
        ServerStatus status = (ServerStatus)c.getData(0);
        notifyServerStatus(status);
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
    
    public void shutdownServer()
    {
        try
        {
        	if (m_Connection != null)
        		m_Connection.postMessage(new CommandPacket(Command.ServerShutdown));
        	else
        		log.error("No connection to close!");
        }
        catch (Exception e)
        {
            String StackTrace = ExceptionUtil.getExceptionStackTrace(e);
            log.debug("Exception thrown in shutdownServer : " + StackTrace);
            log.error(StackTrace);
        }
    }
    
    public void requestSolarSystemDetails(Long planetID)
    {
    	try
    	{
    		m_Connection.postMessage(new CommandPacket(Command.SolarSystemDetails, planetID));
    	}
    	catch (Exception e)
    	{
            String StackTrace = ExceptionUtil.getExceptionStackTrace(e);
            log.debug("Exception thrown in requestSolarSystemDetails : " + StackTrace);
            log.error(StackTrace);
    	}
    }

    public void requestUnitList()
    {
    	try
    	{
    		m_Connection.postMessage(new CommandPacket(Command.UnitList));
    	}
    	catch (Exception e)
    	{
            String StackTrace = ExceptionUtil.getExceptionStackTrace(e);
            log.debug("Exception thrown in requestUnitList : " + StackTrace);
            log.error(StackTrace);
    	}
    }

    public void requestPlayerList()
    {
    	try
    	{
    		m_Connection.postMessage(new CommandPacket(Command.PlayerList));
    	}
    	catch (Exception e)
    	{
            String StackTrace = ExceptionUtil.getExceptionStackTrace(e);
            log.debug("Exception thrown in requestPlayerList : " + StackTrace);
            log.error(StackTrace);
    	}
    }

    public void requestUnitDetails(String unitName)
    {
    	try
    	{
    		m_Connection.postMessage(new CommandPacket(Command.UnitDetails, unitName));
    	}
    	catch (Exception e)
    	{
            String StackTrace = ExceptionUtil.getExceptionStackTrace(e);
            log.debug("Exception thrown in requestUnitDetails : " + StackTrace);
            log.error(StackTrace);
    	}
    }

    public void requestPlayerDetails(String playerName)
    {
    	try
    	{
    		m_Connection.postMessage(new CommandPacket(Command.PlayerDetails, playerName));
    	}
    	catch (Exception e)
    	{
            String StackTrace = ExceptionUtil.getExceptionStackTrace(e);
            log.debug("Exception thrown in requestPlayerDetails : " + StackTrace);
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
    	registerHandler(Command.MessageStart,new CommandHandler(){public void handleCommand(long id, CommandPacket c){notifyMessageStart(c);}});
    	registerHandler(Command.MessageEnd,new CommandHandler(){public void handleCommand(long id, CommandPacket c){notifyMessageEnd();}});    	
    	registerHandler(Command.ServerPassword,new CommandHandler(){public void handleCommand(long id, CommandPacket c){sendServerPassword();}});
    	registerHandler(Command.Version,new CommandHandler(){public void handleCommand(long id, CommandPacket c){sendClientVersion();}});
    	registerHandler(Command.ConnectionType,new CommandHandler(){public void handleCommand(long id, CommandPacket c){sendConnectionType();}});
    	registerHandler(Command.ServerStatus,new CommandHandler(){public void handleCommand(long id, CommandPacket c){updateServerStatus(c);}});
    	registerHandler(Command.ClientConnected,new CommandHandler(){public void handleCommand(long id, CommandPacket c){notifyConnected();}});
    	registerHandler(Command.ServerShutdown,new CommandHandler(){public void handleCommand(long id, CommandPacket c){notifyServerShuttingDown();}});
    	registerHandler(Command.PlayerInfo,new CommandHandler(){public void handleCommand(long id, CommandPacket c){processPlayerInfoRequested();}});
    	registerHandler(Command.SolarSystemDetails, new CommandHandler(){public void handleCommand(long id, CommandPacket c){processSolarSystemDetails(c);}});
    	registerHandler(Command.UnitList, new CommandHandler(){public void handleCommand(long id, CommandPacket c){processUnitList(c);}});
    	registerHandler(Command.UnitDetails, new CommandHandler(){public void handleCommand(long id, CommandPacket c){processUnitDetails(c);}});
    	registerHandler(Command.PlayerList, new CommandHandler(){public void handleCommand(long id, CommandPacket c){processPlayerList(c);}});
    	registerHandler(Command.PlayerDetails, new CommandHandler(){public void handleCommand(long id, CommandPacket c){processPlayerDetails(c);}});
    }
    
    
}
