package bt.elements.unit;

import java.io.Serializable;

public class PlayerSummary implements Serializable 
{
	private static final long serialVersionUID = 7505929885793939647L;

    private String m_Name = "";
    private String m_EmailAddress = "";
	
    public PlayerSummary()
    {
    	
    }
    
    public PlayerSummary(Player p)
    {
    	m_Name = p.getName();
    	m_EmailAddress = p.getEmailAddress();
    }
    
    public void setName(String Name)
    { m_Name = Name; }
    
    public void setEmailAddress(String Address)
    { m_EmailAddress = Address; }
    
    public String getName()
    { return m_Name; }
    
    public String getEmailAddress()
    { return m_EmailAddress; }
    
}
