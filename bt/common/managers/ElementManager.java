package bt.common.managers;

import java.util.Vector;
import java.util.HashMap;

import bt.common.elements.Element;
import bt.common.elements.ElementType;
import bt.common.util.Dice;

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
public class ElementManager
{
    protected static ElementManager theInstance;
    protected Vector<ElementType> m_ElementTypes;
    protected HashMap<ElementType,Vector<Element>> m_ElementMap = new HashMap<ElementType,Vector<Element>>();

    protected ElementManager()
    {
    }

    public static ElementManager GetInstance()
    {
        if (theInstance == null)
            theInstance = new ElementManager();

        return theInstance;
    }


    public static String GetElementName(ElementType et, long ElementID)
    {
        ElementManager em = GetInstance();
        if (!em.m_ElementMap.containsKey(et.toString()))
            return "";

        Vector<Element> v = em.m_ElementMap.get(et);
        for (int i = 0; i < v.size(); i++)
        {
            Element e = (Element)v.elementAt(i);
            if (e.getID() == ElementID)
                return e.GetName();
        }
        return "";
    }

    public static String GetRandomIdentifier()
    {
        String ID = "";
        for (int i = 0; i < 12; i++)
            ID += Integer.toString(Dice.d10(1) -1);

        ID += "-";
        for (int i = 0; i < 6; i++)
            ID += Integer.toString(Dice.d10(1) -1);

        ID += "-";
        for (int i = 0; i < 3; i++)
            ID += Integer.toString(Dice.d10(1) -1);

        return ID;
    }

    public static int GetElementCount(ElementType et)
    {
        ElementManager em = GetInstance();
        if (!em.m_ElementMap.containsKey(et.toString()))
            return 0;

        Vector<Element> v = em.m_ElementMap.get(et);
        return v.size();
    }

    public static Element GetElement(ElementType et, int Index)
    {
        ElementManager em = GetInstance();
        if (!em.m_ElementMap.containsKey(et.toString()))
            return null;

        Vector<Element> v = em.m_ElementMap.get(et);
        return (Element)v.elementAt(Index);
    }
}
