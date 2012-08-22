package bt.elements.galaxy;

import java.io.Serializable;

public class Settlement implements Serializable
{

	private static final long serialVersionUID = 7610082268328758480L;
	private SettlementType m_Type;
	private String m_Name;
	private int m_Location;
	private int m_Population;
	
	public Settlement()
	{
	}

	public Settlement(int location)
	{
		m_Location = location;
	}
	
	public SettlementType getType()
	{ return m_Type; }
	
	public String getName()
	{ return m_Name; }
	
	public int getLocation()
	{ return m_Location; }
	
	public int getPopulation()
	{ return m_Population; }
	
	public void setType(SettlementType type)
	{ m_Type = type; }
	
	public void setName(String name)
	{ m_Name = name; }
	
	public void setPopulation(int population)
	{ m_Population = population; }

	public String toString()
	{
		return Integer.toString(m_Location) + " " + m_Name;
	}
	
	public void saveToElement(org.jdom.Element e)
	{
    	e.addContent(new org.jdom.Element("Type").setText(m_Type.toString()));
    	e.addContent(new org.jdom.Element("Name").setText(m_Name));
    	e.addContent(new org.jdom.Element("Location").setText(Integer.toString(m_Location)));    			
    	e.addContent(new org.jdom.Element("Population").setText(Integer.toString(m_Population)));    			
	}

	public void loadFromElement(org.jdom.Element e)
	{
    	m_Type = SettlementType.fromString(e.getChild("Type").getValue());
    	m_Name = e.getChild("Name").getValue();
    	m_Location = Integer.parseInt(e.getChild("Location").getValue());
    	m_Population = Integer.parseInt(e.getChild("Population").getValue());
	}
}
