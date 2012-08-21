package bt.common.elements;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import bt.common.util.DateHelper;

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
public class Unit implements Serializable
{
	private static final long serialVersionUID = 1;
	
    private String m_Name = "";
    private Date m_EstablishDate = new Date();
    private String m_Notes = "";

    private Character m_Leader;
    private Vector<Personnel> m_Personnel = new Vector<Personnel>();
    private Vector<Asset> m_Assets = new Vector<Asset>();

    private Vector<Formation> m_Formations = new Vector<Formation>();

    public String getName()
    {
        return m_Name;
    }

    public void setName(String name)
    { m_Name = name; }
    
    public Character getLeader()
    { return m_Leader; }
    
    public void setLeader(Character leader)
    { m_Leader = leader; }

    public Date getEstablishDate()
    {
        return m_EstablishDate;
    }

    public void setEstablishDate(Date establishDate)
    { m_EstablishDate = establishDate; }

    public String getNotes()
    { return m_Notes; }

    public void setNotes(String Notes)
    {
        if (!m_Notes.equals(Notes))
        {
            m_Notes = Notes;

        }
    }

    public Unit()
    {
    }

    public int getPersonnelCount()
    { return m_Personnel.size(); }

    public Personnel getPersonnel(int Index)
    {
        if (Index < 0) return null;
        return (Personnel)m_Personnel.elementAt(Index);
    }

    public int getPersonnelIndex(Personnel p)
    {
        return m_Personnel.indexOf(p);
    }

    //Personnel
    public Personnel addNewPersonnel(String Name)
    {
        Personnel p = new Personnel();
        p.setName(Name);

        m_Personnel.add(p);
        return p;
    }

    public void removePersonnel(Personnel p)
    {
        if (m_Personnel.contains(p))
        {
            m_Personnel.remove(p);
        }
    }

    public int getGroupCount()
    {
        return m_Formations.size();
    }

    public Formation getGroup(int Index)
    {
        if (Index < 0) return null;
        return (Formation)m_Formations.elementAt(Index);
    }

    public int getGroupIndex(Formation ug)
    {
        return m_Formations.indexOf(ug);
    }

    public Formation addNewGroup(UnitDesignation ud)
    {
        Formation ug = new Formation();
        ug.setUnitDesignation(ud);
        ug.setCommander(-1);
        ug.setName("New Group");
        m_Formations.add(ug);
        return ug;
    }

    public void removeGroup(Formation f)
    { m_Formations.remove(f); }

    public int getAssetCount()
    { return m_Assets.size(); }

    public Asset getAsset(int Index)
    {
        if (Index < 0) return null;
        return (Asset)m_Assets.elementAt(Index);
    }

    public Asset addNewAsset(ElementType et, long ElementID, String Identifier)
    {
        Asset NewAsset = new Asset();
        NewAsset.setElementType(et);
        NewAsset.setElementID(ElementID);
        NewAsset.setIdentifier(Identifier);
        m_Assets.add(NewAsset);
        return NewAsset;
    }

    public int getAssetIndex(Asset a)
    {
        return m_Assets.indexOf(a);
    }

    public Vector<Asset> getGroupAssets(long GroupID)
    {
        Vector<Asset> Assets = new Vector<Asset>();
        for (int i = 0; i < m_Assets.size(); i++)
        {
            Asset a = (Asset)m_Assets.elementAt(i);
            if (a.getGroupAssignment() == GroupID)
                Assets.add(a);
        }
        return Assets;
    }

    public String toString()
    { return m_Name; }


    public void formationAdded(Formation ug)
    {
    }

    public void formationChanged(Formation ug)
    {
    }

    public void formationRemoved(Formation ug)
    {
    }

    public Formation[] getGroupHierachy(Formation f)
    {
        Vector<Formation> Heirachy = new Vector<Formation>();
        
        //FIXME This needs rework
        
        return (Formation[])Heirachy.toArray();
    }

    public Object[] getGroupHierachyParent(Formation f)
    {
        Vector<Formation> Heirachy = new Vector<Formation>();
        
        //FIXME This needs rework
        
        return (Formation[])Heirachy.toArray();
    }

    public Formation getGroupParent(Formation g)
    {
    	//TODO rewrite this!
        return null;
    }
    
    public void saveToElement(org.jdom.Element e)
    {
    	e.addContent(new org.jdom.Element("Name").setText(m_Name));
    	e.addContent(new org.jdom.Element("EstablishDate").setText(DateHelper.longDateAsString(m_EstablishDate) ));
    	e.addContent(new org.jdom.Element("Notes").setText(m_Notes));
    	
    	org.jdom.Element charElem = new org.jdom.Element("Character");
    	m_Leader.saveToElement(charElem);

    	org.jdom.Element personnelList = new org.jdom.Element("Personnel");
    	for (Personnel p: m_Personnel)
    	{
    		org.jdom.Element personnel = new org.jdom.Element("Personnel");
    		p.saveToElement(personnel);
    		personnelList.addContent(personnel);
    	}
    	e.addContent(personnelList);
    	
    	org.jdom.Element assetList = new org.jdom.Element("Assets");
    	for (Asset a: m_Assets)
    	{
    		org.jdom.Element asset = new org.jdom.Element("Asset");
    		a.saveToElement(asset);
    		assetList.addContent(asset);
    	}
    	e.addContent(assetList);

    	org.jdom.Element FormationList = new org.jdom.Element("Formations");
    	for (Formation f: m_Formations)
    	{
    		org.jdom.Element Formation = new org.jdom.Element("Formation");
    		f.saveToElement(Formation);
    		FormationList.addContent(Formation);
    	}
    	e.addContent(FormationList);
    }
    
    @SuppressWarnings("unchecked")
    public void loadFromElement(org.jdom.Element e)
    {
        m_Name = e.getChildText("Name");
        m_EstablishDate = DateHelper.longDateFromString(e.getChildText("EstablishDate"));
        m_Notes = e.getChildText("Notes");
        
        if (e.getChild("Character") != null)
        {
        	m_Leader = new Character();
        	m_Leader.loadFromElement((org.jdom.Element)e.getChild("Character"));
        }
        if (e.getChild("Personnel") != null)
        {
        	Iterator<Object> iter = e.getChild("Personnel").getChildren("Personnel").iterator();
        	while (iter.hasNext())
        	{
	    		org.jdom.Element personnelElement = (org.jdom.Element)iter.next();
	    		Personnel p = new Personnel();
	    		p.loadFromElement(personnelElement);
	    		m_Personnel.add(p);
        	}        	
        }    
        if (e.getChild("Asset") != null)
        {
        	Iterator<Object> iter = e.getChild("Assets").getChildren("Asset").iterator();
        	while (iter.hasNext())
        	{
	    		org.jdom.Element assetElement = (org.jdom.Element)iter.next();
	    		Asset a = new Asset();
	    		a.loadFromElement(assetElement);
	    		m_Assets.add(a);
        	}        	
        }        
        if (e.getChild("Formation") != null)
        {
        	Iterator<Object> iter = e.getChild("Formations").getChildren("Formation").iterator();
        	while (iter.hasNext())
        	{
	    		org.jdom.Element formationElement = (org.jdom.Element)iter.next();
	    		Formation f = new Formation();
	    		f.loadFromElement(formationElement);
	    		m_Formations.add(f);
        	}        	
        }        
    }
}
