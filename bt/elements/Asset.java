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
public abstract class Asset
{
	public enum Status
	{
		OK,
		DAMAGED,
		CRIPPLED,
		DESTROYED;
		
		private static String[] Names = {"Ok", "Damaged", "Crippled", "Destroyed"};
		
		public static Status fromString(String string) throws Exception
		{
			for (int i = 0; i < values().length; i++)
			{
				if (Names[i].equalsIgnoreCase(string))
					return values()[i];
			}
			throw new Exception("Unable to determine Status from String: " + string);
		}
		
		@Override
		public String toString()
		{
			return Names[ordinal()];
		}
	}
	
  protected String _Identifier = "";
  protected String _Notes = "";

  public abstract ElementType getElementType();

  public String getIdentifier()
  { return _Identifier; }

  public void setIdentifier(String Identifier)
  {
      _Identifier = Identifier;
  }

  public abstract Status getStatus();

  public String getNotes()
  { return _Notes; }

  public void setNotes(String Notes)
  {
      _Notes = Notes;
  }
  
  public void setName(String Name)
  {
  }
    
  public String getDetails()
  {
	  return "(" + getIdentifier() + ") " + getModelInformation() + " - '" + getName() + "'";
  }

  public abstract int getAdjustedBV();
  public abstract String getModelInformation();
  public abstract String getName();

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
