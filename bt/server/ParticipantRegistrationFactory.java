/**
 * Created on 14/11/2005
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import bt.common.elements.unit.Player;

public class ParticipantRegistrationFactory
{
    private static ParticipantRegistrationFactory theInstance;
 
    private HashMap<Long,Player> m_Participants;
    private Vector<ParticipantRegistrationFactoryListener> m_Listeners;
    
    private ParticipantRegistrationFactory()
    {
        m_Participants = new HashMap<Long,Player>();
        m_Listeners = new Vector<ParticipantRegistrationFactoryListener>();
    }
    
    public static ParticipantRegistrationFactory getInstance()
    {
        if (theInstance == null)
            theInstance = new ParticipantRegistrationFactory();
        
        return theInstance;
    }
    
    public boolean isParticipantRegistered(long ConnectionID)
    {
        return m_Participants.containsKey(new Long(ConnectionID));
    }
    
    public Player getParticipant(long ConnectionID)
    {
        return m_Participants.get(new Long(ConnectionID));
    }
    
    public Vector<Player> getParticipants()
    {
        Vector<Player> Participants = new Vector<Player>();
        Set<Map.Entry<Long,Player>> RefSet = m_Participants.entrySet();
        Iterator<Map.Entry<Long,Player>> i = RefSet.iterator();
        while (i.hasNext())
        {
            Map.Entry<Long,Player> entry = i.next();
            Participants.add((Player)entry.getValue());
        }
        return Participants;
    }
    
    public void registerParticipant(Player p) throws Exception
    {
    	/*
        if (isParticipantRegistered(p.getConnectionID())) 
            throw new Exception("Player Already Registered");
        
        m_Participants.put(new Long(p.getConnectionID()),p);
        NotifyParticipantAdded(p);
        */
    }
    
    public void deRegisterParticipant(Player p) throws Exception
    {    	
    	//If there was no participant registered
    	/*
    	if (p == null) return;
    	if (p.getConnectionID() == -1) return;
    	
        if (!isParticipantRegistered(p.getConnectionID())) 
            throw new Exception("Player Not Registered");
        
        m_Participants.remove(new Long(p.getConnectionID()));
        NotifyParticipantRemoved(p);
        */
    }

    public void deRegisterParticipant(long ConnectionID) throws Exception
    {
        Player p = getParticipant(ConnectionID);
        deRegisterParticipant(p);
    }
    
    public void addListener(ParticipantRegistrationFactoryListener l)
    {
        if (!m_Listeners.contains(l))
            m_Listeners.add(l);
    }
    
    public void removeListener(ParticipantRegistrationFactoryListener l)
    {
        if (m_Listeners.contains(l))
            m_Listeners.remove(l);
    }
    
    /*
    private void NotifyParticipantAdded(Player p)
    {
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).ParticipantAdded(p);
    }

    private void NotifyParticipantRemoved(Player p)
    {
        for (int i = 0; i < m_Listeners.size(); i++)
            m_Listeners.elementAt(i).ParticipantRemoved(p);
    }
    */
}
