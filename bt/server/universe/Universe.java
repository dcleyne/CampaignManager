package bt.server.universe;

import java.util.Date;

public class Universe 
{
	private static Universe theInstance;
	
	private Date m_UniverseDate;
	
	private Universe()
	{
		loadUniverseSettings();
	}
	
	public static Universe getInstance()
	{
		if (theInstance == null)
		{
			theInstance = new Universe();
		}
		return theInstance;
	}
	
	public Date getUniverseDate()
	{ return m_UniverseDate; }
	
	public void advanceUniverseDate()
	{
		
	}
	
	private void loadUniverseSettings()
	{
		
	}
}
