package bt.common.elements;


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
public class RandomName
{
    private String m_Name = "";
    private String m_Surname = "";

    public String getName()
    { return m_Name; }
    
    public String getSurname()
    { return m_Surname; }
    
    public RandomName()
    {
    }

    public void splitName(String Name)
    {
        String[] al = Name.split(" ");
        if (al.length > 1)
        {
            m_Name = al[0];
            m_Surname = al[1];
        }
        else
        {
            m_Name = al[0];
            m_Surname = "";
        }
    }

    public String toString()
    {
        if (!m_Surname.equals(""))
            return m_Name + " " + m_Surname;
        else
            return m_Name;
    }
    
    public void loadFromElement(org.jdom.Element e)
    {
    	m_Name = e.getAttributeValue("Name");
    	m_Surname = e.getAttributeValue("Surname");
    }

    public void saveToElement(org.jdom.Element e)
    {
    	e.setAttribute("Name", m_Name);
    	e.setAttribute("Surname",m_Surname);
    }

}
