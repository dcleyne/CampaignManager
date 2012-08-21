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
package bt.server;

import java.net.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

import bt.common.managers.PasswordManager;
import bt.common.managers.PlanetManager;
import bt.common.net.*;
import bt.common.command.Command;
import bt.common.command.CommandPacket;
import bt.common.command.CommandHandler;
import bt.common.command.CommandProcessor;
import bt.common.command.CommandProcessorListener;
import bt.common.elements.Event;
import bt.common.elements.galaxy.InnerSpherePlanet;
import bt.common.elements.galaxy.SolarSystemDetails;
import bt.common.elements.unit.Order;
import bt.common.elements.unit.OrderResult;
import bt.common.elements.unit.Player;
import bt.common.elements.unit.PlayerSummary;
import bt.common.elements.unit.Unit;
import bt.common.elements.unit.UnitSummary;
import bt.common.util.ExceptionUtil;
import bt.common.util.PropertyUtil;
import bt.server.managers.DesignManager;
import bt.server.managers.MapManager;
import bt.server.managers.MissionManager;
import bt.server.managers.PlayerManager;
import bt.server.managers.RandomNameManager;
import bt.server.managers.SolarSystemManager;
import bt.server.managers.UnitManager;
import bt.server.universe.OrderProcessTrigger;
import bt.server.universe.OrderProcessor;
import bt.server.universe.OrderProcessorListener;

/**
 * Main Server class.
 * When instantiated with main this fires up the main run loop.
 */
public class Server extends Thread 
implements CommandProcessorListener, 
	ParticipantRegistrationFactoryListener,
	OrderProcessorListener
{
    private static Log log = LogFactory.getLog(Server.class);
    
    private static String m_ServerVersion = "0.0.000.001";

    private ServerSocket m_Socket;
    private long m_StartTime;
    private String m_ServerName;
    private String m_ServerAddress;
    private boolean m_Running;
    
    private CommandProcessor m_CommandProcessor;
    
    private int[] m_OrderTimes;
    private int m_CurrentOrderTime;
    private OrderProcessTrigger m_OrderProcessTrigger;

    private Vector<Order> m_PendingOrders = new Vector<Order>();
    private Vector<Order> m_QueuedOrders = new Vector<Order>();

    private Vector<Event> m_PendingEvents = new Vector<Event>();
    
    public Server()
    {
        super("Campaign Manager Server");
        
        ParticipantRegistrationFactory.getInstance().addListener(this);        
        try
        {
            m_StartTime = System.currentTimeMillis();
            InetAddress localHost = InetAddress.getLocalHost(); 
            m_ServerName = localHost.getHostName();
            m_ServerAddress = InetAddress.getByName(m_ServerName).getHostAddress();
            
            m_Running = true;
            
            m_CommandProcessor = new CommandProcessor("Server");
            m_CommandProcessor.addListener(this);
            
            registerHandlers();
            
            m_CommandProcessor.start();
        
            startServer();
        }
        catch (Exception e)
        {
            log.fatal(ExceptionUtil.getExceptionStackTrace(e));
            System.exit(0);
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
        PropertyConfigurator.configure(Loader.getResource("bt/server/log4j.properties"));
        log.info("Starting CampaignManagerServer");
        
        try
        {
            PropertyUtil.loadSystemProperties("bt/common/system.properties");
            PropertyUtil.loadSystemProperties("bt/server/server.properties");
                        
            Server rl = new Server();
            rl.start();
        }
        catch (Exception ex)
        {
            log.fatal(ex);
        }
    }
    
    private void startServer()
    {
    	checkAndCreateFileStructure();
    	initialiseComponents();
    	
    	setupOrderProcessing();
    	
        openAdminPort();
    }
    
    private synchronized void stopServer(long connectionID)
    {
    	Connection conn = ConnectionFactory.getInstance().getConnection(connectionID);
    	if (conn.getStatus() == ConnectionStatus.CONNECTED)
    	{
	    	if (conn.getConnectionType() == 0)
	    	{
	    		
	    		stopOrderProcessing();
	    		
		    	m_CommandProcessor.shutDown();
		        m_Running = false;
		        
		        ConnectionFactory.getInstance().broadcastMessage(new CommandPacket(Command.Error,"Server Shutting Down"));
		
		    	m_CommandProcessor.shutDown();
		        notifyAll();
		        log.info("Stopping Campaign Manager Server");
		    	System.exit(0);
	    	}
	    	else
	    	{
	    		postCommand(connectionID, new CommandPacket(Command.Error,"You don't have permission to shut down server!"));
	    	}
    	}
    }
    
    private void openAdminPort()
    {
        String ServerPort = System.getProperty("DefaultServerPort");
        int PortNumber = Integer.parseInt(ServerPort);
        
        try
        {
            m_Socket = new ServerSocket(PortNumber);
        
        }
        catch (IOException e)
        {
            log.info("Failed to Open admin port!");
            log.error(ExceptionUtil.getExceptionStackTrace(e));
            System.exit(0);
        }
    }

    /**
     * Listen for incoming clients.
     */
    public void run() {
        log.debug("Main Connection Thread Listening");
        while (m_Running) 
        {
            try {
                log.debug("m_Socket.accept");
                Socket s = m_Socket.accept();

                log.debug("establish connection");
                Connection c =  ConnectionFactory.getInstance().createServerConnection(s);
                log.debug("add listener");
                c.addListener(m_CommandProcessor);
                log.debug("open");
                c.open();
                
            } catch(IOException ex) {
                log.info("IOEXception thrown in run()");
                log.error(ExceptionUtil.getExceptionStackTrace(ex));
            } catch (Exception e)
            {
                log.info("Exception thrown in run()");
                log.error(ExceptionUtil.getExceptionStackTrace(e));
                //System.exit(0);
            }
        }
    }
    
    private void sendVersionRequest(Connection c) throws Exception
    {
        log.debug("sendVersionRequest");
        c.postMessage(new CommandPacket(Command.Version));
    }

    private void sendConnectionTypeRequest(Connection c) throws Exception
    {
        log.debug("sendConnectionTypeRequest");
        c.postMessage(new CommandPacket(Command.ConnectionType));
    }

    private void sendPlayerInfoRequest(Connection c) throws Exception
    {
        log.debug("sendPlayerInfoRequest");
        c.postMessage(new CommandPacket(Command.PlayerInfo));
    }

    public void disconnected(Connection c)
    {
        try
        {
            log.info("Connection disconnected " + c.toString());
            ConnectionFactory.getInstance().removeConnection(c);
            ParticipantRegistrationFactory.getInstance().deRegisterParticipant(c.getID());
        }
        catch (Exception e)
        {
            log.debug("Exception thrown handling player disconnect : " + e.toString());
            log.error(ExceptionUtil.getExceptionStackTrace(e));
        }
    }
    
    private void postCommand(long ConnectionID, CommandPacket com)
    {
        Connection c = ConnectionFactory.getInstance().getConnection(ConnectionID);
        try
        {
            c.postMessage(com);
        }
        catch (IOException e)
        {
            log.error(ExceptionUtil.getExceptionStackTrace(e));
        }
        catch (Exception e)
        {
            log.error(ExceptionUtil.getExceptionStackTrace(e));
        }
    }

    //This function posts a command to all connections attached to this Server
    private void broadcastCommand(CommandPacket com)
    {
        try
        {
            ConnectionFactory.getInstance().broadcastMessage(com);
        }
        catch (Exception e)
        {
            log.error(ExceptionUtil.getExceptionStackTrace(e));
        }
    }
    
    private void setPlayerInfo(long ConnectionID, CommandPacket c)
    {
        log.info("setPlayerInfo : " + Long.toString(ConnectionID));
        try
        {
    		Connection conn = ConnectionFactory.getInstance().getConnection(ConnectionID);
            String playerName = (String)c.getData(0);
            String playerPassword = (String)c.getData(1);
            Player player = PlayerManager.getInstance().getPlayer(playerName);
            
            if (conn.getConnectionType() == 0)
            {
	            if (isAdminPasswordSet())
	            {
	                String adminPassword = PasswordManager.encrypt(PropertyUtil.getStringProperty("AdminPassword", ""));
	            	if (!playerPassword.equals(adminPassword))
	            	{
	            		conn.postMessage(new CommandPacket(Command.Error,"Incorrect Admin password"));
	            		log.error("Incorrect admin password. Terminating connection!!!");
	            		conn.close();
	            		ConnectionFactory.getInstance().removeConnection(conn);
	            		return;
	            	}            	
	            }
            }
            else
            {            
                if (player == null)
                {
            		conn.postMessage(new CommandPacket(Command.Error,"Unknown Username or Incorrect Password"));
            		log.error("Player not known. Terminating connection!!!");
            		conn.close();
            		ConnectionFactory.getInstance().removeConnection(conn);
            		return;
                }
            	if (!playerPassword.equals(player.getPassword()))
            	{
            		conn.postMessage(new CommandPacket(Command.Error,"Unknown Username or Incorrect Password"));
            		log.error("Player entered incorrect password. Terminating connection!!!");
            		conn.close();
            		ConnectionFactory.getInstance().removeConnection(conn);
            		return;
            	}            	
            }
            conn.setConnected();
        	/*
        	if (player != null)
        	{
	            ParticipantRegistrationFactory prf = ParticipantRegistrationFactory.getInstance(); 
	            if (prf.isParticipantRegistered(player.getConnectionID()))
	            {
	            	prf.deRegisterParticipant(player.getConnectionID());
	            	Connection oldConn = ConnectionFactory.getInstance().getConnection(player.getConnectionID());
	            	oldConn.postMessage(new CommandPacket(Command.Error,"Disconnecting: New connection from " + conn.toString()));
	            	oldConn.postMessage(new CommandPacket(Command.Disconnect));
	            }
	            player.setConnectionID(ConnectionID);
	            prf.registerParticipant(player);
            }
            */
            postCommand(ConnectionID, new CommandPacket(Command.ClientConnected));
            updateServerStatus(0);
        }
        catch (Exception e)
        {
            String StackTrace = ExceptionUtil.getExceptionStackTrace(e);
            log.error("Exception in setPlayerInfo : " + StackTrace);
            postCommand(ConnectionID,new CommandPacket(Command.Error,StackTrace));
        }
    }
    
    private void checkHandshake(long connectionID, CommandPacket c)
    {
        Connection conn = ConnectionFactory.getInstance().getConnection(connectionID);
    	
    	String handshakeString = (String)c.getData(0);
    	if (handshakeString.equals("Campaign Manager Client"))
    	{
    		try
	    	{
    			log.info("Handshake Ok. Sending version request");
				sendVersionRequest(conn);
	    	}
    		catch (Exception ex)
    		{
    			conn.close();
    		}
    	}
    	else
    		conn.close();    	
    }
    
    private boolean isAdminPasswordSet()
    {
    	String password = PropertyUtil.getStringProperty("AdminPassword", "");
    	return (password.length() > 0);
    }
    
    private void checkClientVersion(long connectionID, CommandPacket c)
    {
		log.info("Checking client version");
        Connection conn = ConnectionFactory.getInstance().getConnection(connectionID);
    	String clientVersionString = (String)c.getData(0);
    	
    	Version clientVersion = new Version(clientVersionString);
    	Version serverVersion = new Version(m_ServerVersion);
    	
    	try
    	{
	    	if (serverVersion.isVersionSameOrLater(clientVersion))
	    	{
	    		log.info("Client version : " + clientVersion.toString() + "Ok");
	    		sendConnectionTypeRequest(conn);
	            updateServerStatus(0);
	    	}
	    	else
	    	{
	    		String errorString = "Client version not compatible with server : Min spec version is : " + m_ServerVersion;
	    		log.fatal(errorString);
	    		postCommand(connectionID,new CommandPacket(Command.Error,errorString));
	    		conn.close();
	    	}
    	}
    	catch (Exception ex)
    	{
    		if (conn.getSocket().isConnected())
    			conn.close();
    		log.error(ExceptionUtil.getExceptionStackTrace(ex));
    	}
    }
    
    private void checkConnectionType(long connectionID, CommandPacket c)
    {
        Connection conn = ConnectionFactory.getInstance().getConnection(connectionID);
        conn.setConnectionType((Integer)c.getData(0));
    	try
    	{
    		sendPlayerInfoRequest(conn);
    	}
    	catch (Exception ex)
    	{
    		if (conn.getSocket().isConnected())
    			conn.close();
    		log.error(ExceptionUtil.getExceptionStackTrace(ex));
    	}

    }
    
    private void updateServerStatus(long ConnectionID)
    {
        int ConnectionCount = ConnectionFactory.getInstance().getConnectionCount();
        
        ServerStatus status = new ServerStatus(m_ServerName,m_ServerAddress,m_StartTime,ConnectionCount);
        log.info(status.toString());
        CommandPacket com = new CommandPacket(Command.ServerStatus,status);
        if (ConnectionID == 0)
            broadcastCommand(com);
        else
            postCommand(ConnectionID,com);

    }
    
    private void sendPlayerUnitList(long ConnectionID, CommandPacket c)
    {
    	Player p = ParticipantRegistrationFactory.getInstance().getParticipant(ConnectionID);
    	if (p != null)
    	{
    		Vector<UnitSummary> summaries = new Vector<UnitSummary>(); 
    		Vector<String> unitList = p.getUnits();
    		for (String unitName : unitList)
    		{
    			summaries.add(new UnitSummary(p.getName(), UnitManager.getInstance().getUnit(unitName)));
    		}
    		
    		CommandPacket com = new CommandPacket(Command.UnitList,summaries);
    		postCommand(ConnectionID,com);
    	}
    }

    private void sendUnitList(long ConnectionID, CommandPacket c)
    {
    	Vector<Player> players = ParticipantRegistrationFactory.getInstance().getParticipants();
		Vector<UnitSummary> summaries = new Vector<UnitSummary>(); 
    	for (Player p : players )
    	{
			Vector<String> unitList = p.getUnits();
			for (String unitName : unitList)
			{
				summaries.add(new UnitSummary(p.getName(), UnitManager.getInstance().getUnit(unitName)));
			}
    	}
    	
		CommandPacket com = new CommandPacket(Command.UnitList,summaries);
		postCommand(ConnectionID,com);
    }

    private void sendPlayerList(long ConnectionID, CommandPacket c)
    {
		Vector<PlayerSummary> summaries = PlayerManager.getInstance().getPlayerSummaries(); 
    	
		CommandPacket com = new CommandPacket(Command.PlayerList,summaries);
		postCommand(ConnectionID,com);
    }
  
    private void sendUnitDetails(long connectionID, CommandPacket c)
    {
    	String unitName =(String)c.getData(0); 
    	Unit u = UnitManager.getInstance().getUnit(unitName);
    	if (u != null)
    	{
    		postCommand(connectionID,new CommandPacket(Command.MessageStart,"Sending Unit Details"));
    		postCommand(connectionID,new CommandPacket(Command.UnitDetails,u));
    		postCommand(connectionID,new CommandPacket(Command.MessageEnd));
    	}
    	else
    		postCommand(connectionID,new CommandPacket(Command.Error,"Could not find details for " + unitName));
    }

    private void sendPlayerDetails(long connectionID, CommandPacket c)
    {
    	String playerName =(String)c.getData(0); 
    	Player p = PlayerManager.getInstance().getPlayer(playerName);
    	if (p != null)
    	{
    		postCommand(connectionID,new CommandPacket(Command.MessageStart,"Sending Player Details"));
    		Object[] args = new Object[2];
    		args[0] = playerName;
    		args[1] = p;
    		postCommand(connectionID,new CommandPacket(Command.PlayerDetails,args));
    		postCommand(connectionID,new CommandPacket(Command.MessageEnd));
    	}
    	else
    		postCommand(connectionID,new CommandPacket(Command.Error,"Could not find details for " + playerName));
    }

    private void sendSolarSystemDetails(long connectionID, CommandPacket c)
    {
    	Long planetID =(Long)c.getData(0);
    	InnerSpherePlanet isp = PlanetManager.getPlanetFromID(planetID);
    	SolarSystemDetails ssd = SolarSystemManager.getSystemDetails(isp);
    	
    	Object[] args = new Object[2];
    	args[0] = planetID;
    	args[1] = ssd;
		postCommand(connectionID,new CommandPacket(Command.MessageStart,"Sending Solar System Details for Planet : " + Long.toString(planetID)));
    	postCommand(connectionID,new CommandPacket(Command.SolarSystemDetails,args));
    	postCommand(connectionID,new CommandPacket(Command.MessageEnd));
    }

    public void ParticipantAdded(Player p)
    {
        updateServerStatus(0);
    }
    
    public void ParticipantRemoved(Player p)
    {
        updateServerStatus(0);
    }
 
    public void serverStatusChanged()
    {
        updateServerStatus(0);
    }
        
    
    public void registerHandler(Command command, CommandHandler ch)
    {
    	m_CommandProcessor.registerHandler(command,ch);
    }
    
    private void registerHandlers()
    {
    	registerHandler(Command.Handshake,new CommandHandler(){public void handleCommand(long id, CommandPacket c){checkHandshake(id,c);}});
    	registerHandler(Command.ServerPassword,new CommandHandler(){public void handleCommand(long id, CommandPacket c){}});
    	registerHandler(Command.Version,new CommandHandler(){public void handleCommand(long id, CommandPacket c){checkClientVersion(id,c);}});
    	registerHandler(Command.ConnectionType,new CommandHandler(){public void handleCommand(long id, CommandPacket c){checkConnectionType(id,c);}});
    	registerHandler(Command.ServerStatus,new CommandHandler(){public void handleCommand(long id, CommandPacket c){updateServerStatus(id);}});
    	registerHandler(Command.ServerShutdown,new CommandHandler(){public void handleCommand(long id, CommandPacket c){stopServer(id);}});
    	registerHandler(Command.PlayerInfo,new CommandHandler(){public void handleCommand(long id, CommandPacket c){setPlayerInfo(id,c);}});

    	registerHandler(Command.UnitList,new CommandHandler(){public void handleCommand(long id, CommandPacket c){sendUnitList(id,c);}});
    	registerHandler(Command.PlayerUnitList,new CommandHandler(){public void handleCommand(long id, CommandPacket c){sendPlayerUnitList(id,c);}});
    	registerHandler(Command.UnitDetails,new CommandHandler(){public void handleCommand(long id, CommandPacket c){sendUnitDetails(id,c);}});    	

    	registerHandler(Command.PlayerList,new CommandHandler(){public void handleCommand(long id, CommandPacket c){sendPlayerList(id,c);}});
    	registerHandler(Command.PlayerDetails,new CommandHandler(){public void handleCommand(long id, CommandPacket c){sendPlayerDetails(id,c);}});    	

    	registerHandler(Command.SolarSystemDetails, new CommandHandler(){public void handleCommand(long id, CommandPacket c){sendSolarSystemDetails(id,c);}});
    }
    
    private void checkAndCreateFileStructure()
    {
        String path = System.getProperty("UniverseDataPath");
        new File(path).mkdirs();
    }
    
    private void initialiseComponents()
    {
    	//Force the SolarSystemManager to load.
    	SolarSystemManager.getInstance();
    	RandomNameManager.getInstance();
    	MapManager.getInstance();
    	MissionManager.getInstance();
    	DesignManager.getInstance();
    	UnitManager.getInstance();
    	PlayerManager.getInstance();
    }
    
    private void setupOrderProcessing()
    {
    	String[] times = PropertyUtil.getStringProperty("OrderProcessingTimes", "4,10,16,22").split(",");
    	
    	if (times.length == 0)
    		throw new RuntimeException("Invalid Schedule for Order processing");
    	
    	m_OrderTimes = new int[times.length];
    	for (int i = 0; i < times.length; i++)
    	{
    		m_OrderTimes[i] = Integer.parseInt(times[i]);
    	}
    	m_CurrentOrderTime = 0;
    	
    	setOrderProcessTrigger(false);
    }
    
    private void setOrderProcessTrigger(boolean tomorrow)
    {
    	GregorianCalendar calendar = new GregorianCalendar();
    	calendar.setTime(new Date());
    	if (tomorrow)
    		calendar.add(Calendar.DAY_OF_YEAR,1);
    	calendar.set(Calendar.HOUR_OF_DAY,m_OrderTimes[m_CurrentOrderTime]);
    	calendar.set(Calendar.MINUTE,0);
    	calendar.set(Calendar.SECOND,0);
    	
    	m_OrderProcessTrigger = new OrderProcessTrigger(this,calendar.getTime());
    }

    private void stopOrderProcessing()
    {
    	if (m_OrderProcessTrigger != null)
    	{
    		m_OrderProcessTrigger.cancel();
    		m_OrderProcessTrigger = null;
    	}
    }
    
    public synchronized void addOrderToPendingQueue(Order o)
    {
		m_PendingOrders.add(o);
    }
    
    private synchronized void queuePendingOrders()
    {
    	m_QueuedOrders = m_PendingOrders;
    	m_PendingOrders = new Vector<Order>();    	
    }
    
    
    
    public void processOrders()
    {
    	queuePendingOrders();
    	
    	
    	//Instantiate a new Order Processing Thread
    	OrderProcessor op = new OrderProcessor(m_QueuedOrders,m_PendingEvents);
    	op.addListener(this);
    	op.start();

    }

	public void orderProcessingStarted(OrderProcessor op)
	{
		
	}
	
	public void orderProcessed(OrderProcessor op, Order o, OrderResult or)
	{
		
	}
	
	public void orderProcessingCompleted(OrderProcessor op, Vector<OrderResult> results)
	{
    	//Setup the next order trigger
    	m_CurrentOrderTime++;
    	boolean tomorrow = false;
    	if (m_CurrentOrderTime > m_OrderTimes.length - 1)
    	{
    		m_CurrentOrderTime = 0;
    		tomorrow = true;
    	}
    	setOrderProcessTrigger(tomorrow);
    	
    	//Open the order queue and notify it is open
    	
    	
		//publish the results to the players
	}


}

