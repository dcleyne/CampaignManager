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
	
    protected String _Name = "";
    protected UnitDesignation _UnitDesignation;
    protected long _Commander;
    protected String _Notes = "";

    public String getName()
    { return _Name; }

    public void setName(String name)
    { _Name = name; }

    public UnitDesignation getUnitDesignation()
    { return _UnitDesignation; }

    public void setUnitDesignation(UnitDesignation ud)
    { _UnitDesignation = ud; }

    public long getCommander()
    { return _Commander; }

    public void setCommander(long commander)
    {
        _Commander = commander;
    }

    public String getNotes()
    { return _Notes; }

    public void setNotes(String notes)
    { _Notes = notes;}


    public Formation()
    {
    }

    public String toString()
    {
        return _Name;
    }

    public void saveToElement(org.jdom.Element e)
    {
    	
    }
    
    public void loadFromElement(org.jdom.Element e)
    {
    	
    }

}
