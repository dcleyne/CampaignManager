package bt.managers;

import java.util.HashMap;

import bt.elements.galaxy.SolarSystemDetails;

public class SolarSystemDetailCache 
{
	private static SolarSystemDetailCache theInstance;

	private HashMap<Long, SolarSystemDetails> _DetailCache = new HashMap<Long, SolarSystemDetails>();
	
	private SolarSystemDetailCache()
	{
		
	}

	public static SolarSystemDetailCache getInstance()
	{
		if (theInstance == null)
			theInstance = new SolarSystemDetailCache();
		
		return theInstance;
	}
	
	public SolarSystemDetails getDetails(Long id)
	{
		return _DetailCache.get(id);
	}
	
	public void putDetails(Long id, SolarSystemDetails ssd)
	{
		_DetailCache.put(id, ssd);
	}
	
	
}
