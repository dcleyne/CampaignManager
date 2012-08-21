package bt.common.elements;

import java.util.Date;

import bt.common.util.DateHelper;

public abstract class Event implements Comparable<Event>
{
	private static final long serialVersionUID = 1;
	
	private Date m_EventTime;

		
	public Event(Date eventTime)
	{
		m_EventTime = eventTime;	
	}
	
	public Date getEventTime()
	{ return m_EventTime; }
	
	public int compareTo(Event e)
	{
		return this.m_EventTime.compareTo(e.m_EventTime);
	}
	
	public void saveToElement(org.jdom.Element e)
	{
		e.addContent(new org.jdom.Element("EventTime").setText(DateHelper.longDateAsString(m_EventTime)));
	}
	
	public void loadFromElement(org.jdom.Element e)
	{
		m_EventTime = DateHelper.longDateFromString(e.getChildText("EventTime"));
	}


}
