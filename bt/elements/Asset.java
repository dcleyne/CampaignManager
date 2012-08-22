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
public class Asset
{
//  private static Log log = LogFactory.getLog(Asset.class);
    
  protected ElementType m_ElementType;
  protected long m_ElementID;
  protected long m_GroupAssignment;
  protected String m_Identifier = "";
  protected String m_Status = "";
  protected String m_Condition = "";
  protected String m_Notes = "";

  public ElementType getElementType()
  { return m_ElementType; }

  public void setElementType(ElementType et)
  {
      m_ElementType = et;
  }

  public long getElementID()
  { return m_ElementID; }

  public void setElementID(long ElementID)
  {
      m_ElementID = ElementID;
  }

  public long getGroupAssignment()
  { return m_GroupAssignment; }

  public void setGroupAssignment(long GroupAssignment)
  {
      m_GroupAssignment = GroupAssignment;
  }

  public String getIdentifier()
  { return m_Identifier; }

  public void setIdentifier(String Identifier)
  {
      m_Identifier = Identifier;
  }

  public String getStatus()
  { return m_Status; }

  public void setStatus(String Status)
  {
      m_Status = Status;
  }

  public String getCondition()
  { return m_Condition; }

  public void setCondition(String Condition)
  {
      m_Condition = Condition;
  }

  public String getNotes()
  { return m_Notes; }

  public void setNotes(String Notes)
  {
      m_Notes = Notes;
  }
  
  public String getName()
  {
	  return "";
  }
  
  public String getModelInformation()
  {
	  return "";
  }

  public int getAdjustedBV()
  {
	  return 0;
  }
  
  public Asset()
  {
  }
  
  public void saveToElement(org.jdom.Element e)
  {
  }
  
  public void loadFromElement(org.jdom.Element e)
  {
  }

}
