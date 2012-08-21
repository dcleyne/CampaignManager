package bt.servermanager.managers;

import java.util.HashMap;
import java.util.Vector;

import bt.common.elements.unit.Player;
import bt.common.elements.unit.PlayerSummary;

public class PlayerCache 
{
	private static PlayerCache theInstance;
	
	private Vector<PlayerSummary> _PlayerSummaries = new Vector<PlayerSummary>();
	private HashMap<String, Player> _Players = new HashMap<String, Player>();
	
	private PlayerCache()
	{
		
	}
	
	public static PlayerCache getInstance()
	{
		if (theInstance == null)
			theInstance = new PlayerCache();
		
		return theInstance;
	}
	
	public Vector<PlayerSummary> getPlayerSummaries()
	{
		return new Vector<PlayerSummary>(_PlayerSummaries);		
	}
	
	public void setPlayerSummaries(Vector<PlayerSummary> summaries)
	{
		_PlayerSummaries.clear();
		_PlayerSummaries.addAll(summaries);
	}
	
	public Vector<String> getPlayerNames()
	{
		return new Vector<String>(_Players.keySet());
	}
	
	public Player getPlayer(String name)
	{
		return _Players.get(name);
	}
	
	public void putPlayer(String name, Player p)
	{
		_Players.put(name, p);
	}
	
}
