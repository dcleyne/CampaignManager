package bt.common.elements.unit;


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
    private String _Name = "";
    private String _Surname = "";

    public String getName()
    { return _Name; }
    
    public String getSurname()
    { return _Surname; }
    
    public RandomName()
    {
    }
    
    public RandomName(String name, String surname)
    {
    	_Name = name;
    	_Surname = surname;
    }

    public void splitName(String Name)
    {
        String[] al = Name.split(" ");
        if (al.length > 1)
        {
            _Name = al[0];
            _Surname = al[1];
        }
        else
        {
            _Name = al[0];
            _Surname = "";
        }
    }

    public String toString()
    {
        if (!_Surname.equals(""))
            return _Name + " " + _Surname;
        else
            return _Name;
    }
    
    public void loadFromElement(org.jdom.Element e)
    {
    	_Name = e.getAttributeValue("Name");
    	_Surname = e.getAttributeValue("Surname");
    }

    public void saveToElement(org.jdom.Element e)
    {
    	e.setAttribute("Name", _Name);
    	e.setAttribute("Surname",_Surname);
    }

}
