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
package bt.elements.unit;

import java.io.*;

import java.util.Vector;

public class Player implements Serializable
{
    static final long serialVersionUID = 1;

    private String m_Name = "";
    private String m_Password = "";
    private String m_Nickname = "";
    private String m_EmailAddress = "";
    private Vector<String> m_Units = new Vector<String>();

    public Player()
    {
    }
    
    public void setName(String Name)
    { m_Name = Name; }
    
    public void setPassword(String Password)
    { m_Password = Password; }

    public void setNickname(String Name)
    { m_Nickname = Name; }
    
    public void setEmailAddress(String Address)
    { m_EmailAddress = Address; }
    
    public String getName()
    { return m_Name; }
    
    public String getPassword()
    { return m_Password; }
    
    public String getNickname()
    { return m_Nickname; }
    
    public String getEmailAddress()
    { return m_EmailAddress; }
    
    public void addUnit(String unitName)
    { m_Units.add(unitName); }
    
    public void removeUnit(String unitName)
    { m_Units.remove(unitName); }
    
    public Vector<String> getUnits()
    { return m_Units; }
    
    public String toString()
    {
    	return m_Name;
    }
    

}
