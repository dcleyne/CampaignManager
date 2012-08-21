/**
 * Created on Dec 31, 2006
 * <p>Title: Legatus</p>
 *
 * <p>Copyright: Copyright Daniel Cleyne (c) 2004-2006</p>
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
package bt.common.net;

import java.io.*;
import java.util.Date;

public class GameStatus implements Serializable
{
    static final long serialVersionUID = 1;
    
    private String m_Name;
    private String m_Type;
    private boolean m_CanConnect;
    private boolean m_GameStarted;
    private int m_CurrentTurn;
    private int m_PlayerCount;
    private int m_ObserverCount;
    private long m_LastActivity;

    public GameStatus()
    {
        
    }
    
    public void setName(String name)
    { m_Name = name; }
    
    public void setType(String type)
    { m_Type = type; }
    
    public void setCanConnect(boolean connect)
    { m_CanConnect = connect; }
    
    public void setGameStarted(boolean started)
    { m_GameStarted = started; }
    
    public void setCurrentTurn(int turn)
    { m_CurrentTurn = turn; }
    
    public void setPlayerCount(int count)
    { m_PlayerCount = count; }
    
    public void setObserverCount(int count)
    { m_ObserverCount = count; }
    
    public void setLastActivity(long eventTime)
    { m_LastActivity = eventTime; }
    
    public String getName()
    { return m_Name; }
    
    public String getType()
    { return m_Type; }
    
    public boolean getCanConnect()
    { return m_CanConnect; }
    
    public boolean getGameStarted()
    { return m_GameStarted; }
    
    public int getCurrentTurn()
    { return m_CurrentTurn; }
    
    public int getPlayerCount()
    { return m_PlayerCount; }
    
    public int getObserverCount()
    { return m_ObserverCount; }
    
    public long getLastActitivy()
    { return m_LastActivity; }
    
    public String getLastActitivyAsString()
    {
        Date d = new Date(m_LastActivity);
        return d.toString();
    }


    public String toString()
    {
        Date lastActive = new Date(m_LastActivity);
        
        String retVal = "";
        retVal += "Name(" + m_Name + ") ";
        retVal += "Type(" + m_Type + ") ";
        retVal += "CanConnect(" + (m_CanConnect ? "Yes" : "No") + ") ";
        retVal += "GameStarted(" + (m_GameStarted ? "Yes" : "No") + ") ";
        retVal += "Turn(" + Integer.toString(m_CurrentTurn) + ") ";
        retVal += "Players(" + Integer.toString(m_PlayerCount) + ") ";
        retVal += "Observers(" + Integer.toString(m_ObserverCount) + ")";
        retVal += "LastActivity(" + lastActive.toString() + ")";
        return retVal;
    }
    
}
