package bt.elements.personnel;


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
public abstract class Personnel
{
    private String _Name = "";
    private String _Callsign = "";
    private Rank _Rank = Rank.NONE;
    private int _Piloting = 9;
    private int _Gunnery = 9;
    private int _Age = 16;
    private long _HomePlanet;
    private Rating _Rating = Rating.GREEN;
    private JobType _JobType = JobType.MECHWARRIOR;
    private String _Notes = "";

    public String getName()
    { return _Name; }

    public void setName(String Name)
    { _Name = Name; }

    public String getCallsign()
    { return _Callsign; }

    public void setCallsign(String Callsign)
    { _Callsign = Callsign; }

    public Rank getRank()
    { return _Rank; }

    public void setRank(Rank r)
    { _Rank = r; }

    public long getHomePlanet()
    { return _HomePlanet; }

    public void setHomePlanet(long HomePlanet)
    { _HomePlanet = HomePlanet; }

    public Rating getRating()
    { return _Rating; }

    public void setRating(Rating r)
    { _Rating = r; }

    public JobType getJobType()
    { return _JobType; }

    public void setJobType(JobType jt)
    { _JobType = jt; }

    public String getNotes()
    { return _Notes; }

    public void setNotes(String Notes)
    { _Notes = Notes; }
    
    public void setPiloting(int piloting)
    { _Piloting = piloting; }
    
    public void setGunnery(int gunnery)
    { _Gunnery = gunnery; }
    
    public void setAge(int age)
    { _Age = age; }
    

    public int getPiloting()
    { return _Piloting; }
    
    public int getGunnery()
    { return _Gunnery; }
    
    public int getAge()
    { return _Age; }
    
    public Personnel()
    {
    }
    
    public String getSummary()
    {
        return getJobType().toString() + "(" + getRating().toString() + ") " + _Name;
    }
    
     public String toString()
    { return _Name + " [" + _Rank + "]" ; }
     
     public void saveToElement(org.jdom.Element e)
     {
     	e.addContent(new org.jdom.Element("Name").setText(_Name));
     	e.addContent(new org.jdom.Element("Callsign").setText(_Callsign));
     	e.addContent(new org.jdom.Element("Rank").setText(_Rank.toString()));
     	e.addContent(new org.jdom.Element("Piloting").setText(Integer.toString(_Piloting)));
     	e.addContent(new org.jdom.Element("Gunnery").setText(Integer.toString(_Gunnery)));
     	e.addContent(new org.jdom.Element("Age").setText(Integer.toString(_Age)));
     	e.addContent(new org.jdom.Element("HomePlanet").setText(Long.toString(_HomePlanet)));
     	e.addContent(new org.jdom.Element("Rating").setText(_Rating.toString()));
     	e.addContent(new org.jdom.Element("JobType").setText(_JobType.toString()));
     	e.addContent(new org.jdom.Element("Notes").setText(_Notes));
     }
     
     public void loadFromElement(org.jdom.Element e)
     {
     	_Name = e.getChildText("Name");
    	_Callsign = e.getChildText("Callsign");
    	_Rank = Rank.fromString(e.getChildText("Rank"));
    	_Piloting = Integer.parseInt(e.getChildText("Piloting"));
    	_Gunnery = Integer.parseInt(e.getChildText("Gunnery"));
    	_Age = Integer.parseInt(e.getChildText("Age"));
    	_HomePlanet = Long.parseLong(e.getChildText("HomePlanet"));
    	_Rating = Rating.fromString(e.getChildText("Rating"));
    	_JobType = JobType.fromString(e.getChildText("JobType"));
     	_Notes = e.getChildText("Notes");
     }
}
