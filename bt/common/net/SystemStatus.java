package bt.common.net;

import java.io.Serializable;
import java.util.Date;

public abstract class SystemStatus  implements Serializable
{
    static final long serialVersionUID = 1;

    private String m_ServerName;
    private String m_ServerAddress;
    private long m_StartTime;
    private long m_NumberOfConnections;

    public SystemStatus(String serverName, String serverAddress, long startTime, long connectionCount)
    {
	    m_ServerName = serverName;
	    m_ServerAddress = serverAddress;
	    m_StartTime = startTime;
	    m_NumberOfConnections = connectionCount;
    }
    
    public String getServerName()
    { return m_ServerName; }
    
    public String getServerAddress()
    { return m_ServerAddress; }
    
    public String getJavaVMVersion()
    { return System.getProperty("java.vm.version"); }

    public long getServerStartTime()
    { return m_StartTime; }
    
    public long getNumberOfConnections()
    { return m_NumberOfConnections; }

    public String toString()
    {
        Date startTime = new Date(m_StartTime);
        
        String retVal = "Server Status : ";
        retVal += "ServerName(" + m_ServerName + ") ";
        retVal += "ServerAddress(" + m_ServerAddress + ") ";
        retVal += "StartedOn(" + startTime.toString() + ") ";
        retVal += "Connections(" + Long.toString(m_NumberOfConnections) + ") ";
        return retVal;
    }
    
}
