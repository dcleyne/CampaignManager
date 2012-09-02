package bt.elements.unit;


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

    public static RandomName splitName(String Name)
    {
    	String name;
    	String surname;
    	
        String[] al = Name.split(" ");
        if (al.length > 1)
        {
            name = al[0];
            surname = al[1];
        }
        else
        {
            name = al[0];
            surname = "";
        }
        
        return new RandomName(name, surname);
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
