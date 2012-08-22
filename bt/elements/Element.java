package bt.elements;

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
public abstract class Element
{
	private long m_ID; 
	
    public long getID()
    {
    	return m_ID;
    }
    
    public abstract String GetName();
    public abstract String GetElementType();
    public abstract int GetCost();
    public abstract double GetSupportRequirement();
    public abstract double GetCrewCompliment();
}
