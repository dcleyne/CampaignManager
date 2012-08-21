/**
 * Created on 13/11/2005
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
package bt.common.elements;

import java.io.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import bt.common.util.DateHelper;


public class Player implements Serializable
{
    static final long serialVersionUID = 1;

    private long m_ID = -1;
    private String m_Name = "";
    private String m_Password = "";
    private String m_Nickname = "";
    private String m_EmailAddress = "";
    private Date m_LastLogin = new Date();
    private Vector<String> m_Units = new Vector<String>();
    
    private long m_ConnectionID;
    
    public Player(long id)
    {
    	m_ID = id;
    }
    
    public long getID()
    { return m_ID; }
    
    public void setName(String Name)
    { m_Name = Name; }
    
    public void setPassword(String Password)
    { m_Password = Password; }

    public void setNickName(String Name)
    { m_Nickname = Name; }
    
    public void setEmailAddress(String Address)
    { m_EmailAddress = Address; }
    
    public void setConnectionID(long ID)
    { m_ConnectionID = ID; }
    
    public void setLastLogin(Date lastLogin)
    { m_LastLogin = lastLogin; }
    
    public String getName()
    { return m_Name; }
    
    public String getPassword()
    { return m_Password; }
    
    public String getNickname()
    { return m_Nickname; }
    
    public String getEmailAddress()
    { return m_EmailAddress; }
    
    public long getConnectionID()
    { return m_ConnectionID; }
    
    public Date getLastLogin()
    { return m_LastLogin; }
    
    public void addUnit(String unitName)
    { m_Units.add(unitName); }
    
    public void removeUnit(String unitName)
    { m_Units.remove(unitName); }
    
    public Vector<String> getUnits()
    { return m_Units; }
    
    public boolean equals(Object o)
    {
    	if (this.getClass().equals(o.getClass()))
    	{
    		return ((Player)o).m_ConnectionID == this.m_ConnectionID;
    	} 
        else
        {
            if (o.getClass().getSuperclass().equals(this.getClass()))
                return ((Player)o).m_ConnectionID == this.m_ConnectionID;            
        }
    	return false;
    }
    
    public String toString()
    {
    	return "(" + Long.toString(m_ConnectionID) + ") " + m_Name;
    }
    
    @SuppressWarnings("unchecked")
	public void loadFromElement(org.jdom.Element e)
    {
    	try
    	{
    		m_ID = Long.parseLong(e.getChildText("ID"));
	        m_Name = e.getChildText("Name");
	        m_Password = e.getChildText("Password");
	        m_Nickname = e.getChildText("Nickname");
	        m_EmailAddress = e.getChildText("EmailAddress");
	        m_LastLogin = DateHelper.longDateFromString(e.getChildText("LastLogin"));
	        m_Units.clear();
	        
	        if (e.getChild("Units") != null)
	        {
	        	Iterator<Object> iter = e.getChild("Units").getChildren("Unit").iterator();
	        	while (iter.hasNext())
	        	{
	        		org.jdom.Element unitElement = (org.jdom.Element)iter.next();
	        		m_Units.add(unitElement.getText());
	        	}
	        }
        
    	}
    	catch (Exception ex)
    	{
    	}
    }
    
    public void saveToElement(org.jdom.Element e)
    {
    	e.addContent(new org.jdom.Element("ID").setText(Long.toString(m_ID)));
    	e.addContent(new org.jdom.Element("Name").setText(m_Name));
    	e.addContent(new org.jdom.Element("Password").setText(m_Password));
    	e.addContent(new org.jdom.Element("Nickname").setText(m_Nickname));
    	e.addContent(new org.jdom.Element("EmailAddress").setText(m_EmailAddress));
    	e.addContent(new org.jdom.Element("LastLogin").setText(DateHelper.longDateAsString(m_LastLogin)));
    	
    	org.jdom.Element unitsElement = new org.jdom.Element("Units");
    	for (String unitName: m_Units)
        	unitsElement.addContent(new org.jdom.Element("Unit").setText(unitName));    		

    	e.addContent(unitsElement);
    }
}
