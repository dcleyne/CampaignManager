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
public class Personnel
{
    private String m_Name = "";
    private String m_Callsign = "";
    private Rank m_Rank = Rank.NONE;
    private int m_Piloting = 9;
    private int m_Gunnery = 9;
    private int m_Age = 16;
    private long m_HomePlanet;
    private Rating m_Rating = Rating.GREEN;
    private JobType m_JobType = JobType.MECHWARRIOR;
    private String m_Notes = "";

    public String getName()
    { return m_Name; }

    public void setName(String Name)
    { m_Name = Name; }

    public String getCallsign()
    { return m_Callsign; }

    public void setCallsign(String Callsign)
    { m_Callsign = Callsign; }

    public Rank getRank()
    { return m_Rank; }

    public void setRank(Rank r)
    { m_Rank = r; }

    public long getHomePlanet()
    { return m_HomePlanet; }

    public void setHomePlanet(long HomePlanet)
    { m_HomePlanet = HomePlanet; }

    public Rating getRating()
    { return m_Rating; }

    public void setRating(Rating r)
    { m_Rating = r; }

    public JobType getJobType()
    { return m_JobType; }

    public void setJobType(JobType jt)
    { m_JobType = jt; }

    public String getNotes()
    { return m_Notes; }

    public void setNotes(String Notes)
    { m_Notes = Notes; }
    
    public void setPiloting(int piloting)
    { m_Piloting = piloting; }
    
    public void setGunnery(int gunnery)
    { m_Gunnery = gunnery; }
    
    public void setAge(int age)
    { m_Age = age; }
    

    public int getPiloting()
    { return m_Piloting; }
    
    public int getGunnery()
    { return m_Gunnery; }
    
    public int getAge()
    { return m_Age; }
    
    public Personnel()
    {
    }
    
     public String toString()
    { return m_Name + " [" + m_Rank + "]" ; }
     
     public void saveToElement(org.jdom.Element e)
     {
     	e.addContent(new org.jdom.Element("Name").setText(m_Name));
     	e.addContent(new org.jdom.Element("Callsign").setText(m_Callsign));
     	e.addContent(new org.jdom.Element("Rank").setText(m_Rank.toString()));
     	e.addContent(new org.jdom.Element("Piloting").setText(Integer.toString(m_Piloting)));
     	e.addContent(new org.jdom.Element("Gunnery").setText(Integer.toString(m_Gunnery)));
     	e.addContent(new org.jdom.Element("Age").setText(Integer.toString(m_Age)));
     	e.addContent(new org.jdom.Element("HomePlanet").setText(Long.toString(m_HomePlanet)));
     	e.addContent(new org.jdom.Element("Rating").setText(m_Rating.toString()));
     	e.addContent(new org.jdom.Element("JobType").setText(m_JobType.toString()));
     	e.addContent(new org.jdom.Element("Notes").setText(m_Notes));
     }
     
     public void loadFromElement(org.jdom.Element e)
     {
     	m_Name = e.getChildText("Name");
    	m_Callsign = e.getChildText("Callsign");
    	m_Rank = Rank.fromString(e.getChildText("Rank"));
    	m_Piloting = Integer.parseInt(e.getChildText("Piloting"));
    	m_Gunnery = Integer.parseInt(e.getChildText("Gunnery"));
    	m_Age = Integer.parseInt(e.getChildText("Age"));
    	m_HomePlanet = Long.parseLong(e.getChildText("HomePlanet"));
    	m_Rating = Rating.fromString(e.getChildText("Rating"));
    	m_JobType = JobType.fromString(e.getChildText("JobType"));
     	m_Notes = e.getChildText("Notes");
     }
}
