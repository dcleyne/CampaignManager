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
package bt.elements;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Vector;

import bt.util.DateHelper;


public class Player implements Serializable
{
    static final long serialVersionUID = 1;

    private long _ID = -1;
    private String _Name = "";
    private String _Password = "";
    private String _Nickname = "";
    private String _EmailAddress = "";
    private LocalDate _LastLogin = LocalDate.now();
    private Vector<String> _Units = new Vector<String>();
    
    private long _ConnectionID;
    
    public Player(long id)
    {
    	_ID = id;
    }
    
    public long getID()
    { return _ID; }
    
    public void setName(String Name)
    { _Name = Name; }
    
    public void setPassword(String Password)
    { _Password = Password; }

    public void setNickName(String Name)
    { _Nickname = Name; }
    
    public void setEmailAddress(String Address)
    { _EmailAddress = Address; }
    
    public void setConnectionID(long ID)
    { _ConnectionID = ID; }
    
    public void setLastLogin(LocalDate lastLogin)
    { _LastLogin = lastLogin; }
    
    public String getName()
    { return _Name; }
    
    public String getPassword()
    { return _Password; }
    
    public String getNickname()
    { return _Nickname; }
    
    public String getEmailAddress()
    { return _EmailAddress; }
    
    public long getConnectionID()
    { return _ConnectionID; }
    
    public LocalDate getLastLogin()
    { return _LastLogin; }
    
    public void addUnit(String unitName)
    { _Units.add(unitName); }
    
    public void removeUnit(String unitName)
    { _Units.remove(unitName); }
    
    public Vector<String> getUnits()
    { return _Units; }
    
    public boolean equals(Object o)
    {
    	if (this.getClass().equals(o.getClass()))
    	{
    		return ((Player)o)._ConnectionID == this._ConnectionID;
    	} 
        else
        {
            if (o.getClass().getSuperclass().equals(this.getClass()))
                return ((Player)o)._ConnectionID == this._ConnectionID;            
        }
    	return false;
    }
    
    public String toString()
    {
    	return "(" + Long.toString(_ConnectionID) + ") " + _Name;
    }
    
    @SuppressWarnings("unchecked")
	public void loadFromElement(org.jdom.Element e)
    {
    	try
    	{
    		_ID = Long.parseLong(e.getChildText("ID"));
	        _Name = e.getChildText("Name");
	        _Password = e.getChildText("Password");
	        _Nickname = e.getChildText("Nickname");
	        _EmailAddress = e.getChildText("EmailAddress");
	        _LastLogin = DateHelper.longDateFromString(e.getChildText("LastLogin"));
	        _Units.clear();
	        
	        if (e.getChild("Units") != null)
	        {
	        	Iterator<Object> iter = e.getChild("Units").getChildren("Unit").iterator();
	        	while (iter.hasNext())
	        	{
	        		org.jdom.Element unitElement = (org.jdom.Element)iter.next();
	        		_Units.add(unitElement.getText());
	        	}
	        }
        
    	}
    	catch (Exception ex)
    	{
    	}
    }
    
    public void saveToElement(org.jdom.Element e)
    {
    	e.addContent(new org.jdom.Element("ID").setText(Long.toString(_ID)));
    	e.addContent(new org.jdom.Element("Name").setText(_Name));
    	e.addContent(new org.jdom.Element("Password").setText(_Password));
    	e.addContent(new org.jdom.Element("Nickname").setText(_Nickname));
    	e.addContent(new org.jdom.Element("EmailAddress").setText(_EmailAddress));
    	e.addContent(new org.jdom.Element("LastLogin").setText(DateHelper.longDateAsString(_LastLogin)));
    	
    	org.jdom.Element unitsElement = new org.jdom.Element("Units");
    	for (String unitName: _Units)
        	unitsElement.addContent(new org.jdom.Element("Unit").setText(unitName));    		

    	e.addContent(unitsElement);
    }
}
