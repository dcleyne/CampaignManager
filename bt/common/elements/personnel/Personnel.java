package bt.common.elements.personnel;


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
    private String _Surname = "";
    private String _Callsign = "";
    private Rank _Rank = Rank.NONE;
    private Rating _Rating = Rating.GREEN;
    private String _Notes = "";
	public String getName()
	{
		return _Name;
	}
	public void setName(String name)
	{
		if (name == null)
			throw new IllegalArgumentException("Cannot set a Personnel name to null");
		
		_Name = name;
	}
	public String getSurname()
	{
		return _Surname;
	}
	public void setSurname(String surname)
	{
		if (surname == null)
			throw new IllegalArgumentException("Cannot set a Personnel surname to null");

		_Surname = surname;
	}
	public String getCallsign()
	{
		return _Callsign;
	}
	public void setCallsign(String callsign)
	{
		_Callsign = callsign;
	}
	public Rank getRank()
	{
		return _Rank;
	}
	public void setRank(Rank rank)
	{
		_Rank = rank;
	}
	public Rating getRating()
	{
		return _Rating;
	}
	public void setRating(Rating rating)
	{
		_Rating = rating;
	}
	public String getNotes()
	{
		return _Notes;
	}
	public void setNotes(String notes)
	{
		_Notes = notes;
	}

    public abstract JobType getJobType();

    public String getSummary()
    {
        return getJobType().toString() + "(" + getRating().toString() + ") " + _Name + " " + _Surname;
    }
}
