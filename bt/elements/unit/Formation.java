package bt.elements.unit;

import java.io.Serializable;


/**
 * <p>Title: Miradan Phedd</p>
 *
 * <p>Description: Library for all Miradan related work</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Miradan Phedd</p>
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
public class Formation implements Serializable
{
	private static final long serialVersionUID = 1;
	
    protected String m_Name = "";
    protected UnitDesignation m_UnitDesignation;
    protected long m_Commander;
    protected String m_Notes = "";

    public String getName()
    { return m_Name; }

    public void setName(String name)
    { m_Name = name; }

    public UnitDesignation getUnitDesignation()
    { return m_UnitDesignation; }

    public void setUnitDesignation(UnitDesignation ud)
    { m_UnitDesignation = ud; }

    public long getCommander()
    { return m_Commander; }

    public void setCommander(long commander)
    {
        m_Commander = commander;
    }

    public String getNotes()
    { return m_Notes; }

    public void setNotes(String notes)
    { m_Notes = notes;}


    public Formation()
    {
    }

    public String toString()
    {
        return m_Name;
    }

    public void saveToElement(org.jdom.Element e)
    {
    	
    }
    
    public void loadFromElement(org.jdom.Element e)
    {
    	
    }

}
