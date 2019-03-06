package bt.elements;

import java.time.LocalDate;

import bt.util.DateHelper;

public abstract class Event implements Comparable<Event>
{
	private LocalDate _EventTime;

		
	public Event(LocalDate eventTime)
	{
		_EventTime = eventTime;	
	}
	
	public LocalDate getEventTime()
	{ return _EventTime; }
	
	public int compareTo(Event e)
	{
		return this._EventTime.compareTo(e._EventTime);
	}
	
	public void saveToElement(org.jdom.Element e)
	{
		e.addContent(new org.jdom.Element("EventTime").setText(DateHelper.longDateAsString(_EventTime)));
	}
	
	public void loadFromElement(org.jdom.Element e)
	{
		_EventTime = DateHelper.longDateFromString(e.getChildText("EventTime"));
	}


}
