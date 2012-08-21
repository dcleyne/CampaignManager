package bt.common.elements.scenario;

public class SituationReport
{
	private String _Sender;
	private String _Recipient;
	private String _ReportDate;
	private String _Location;
	private String _Priority;
	private String _Message;
	public String getSender()
	{
		return _Sender;
	}
	public void setSender(String sender)
	{
		_Sender = sender;
	}
	public String getRecipient()
	{
		return _Recipient;
	}
	public void setRecipient(String recipient)
	{
		_Recipient = recipient;
	}
	public String getReportDate()
	{
		return _ReportDate;
	}
	public void setReportDate(String reportDate)
	{
		_ReportDate = reportDate;
	}
	public String getLocation()
	{
		return _Location;
	}
	public void setLocation(String location)
	{
		_Location = location;
	}
	public String getPriority()
	{
		return _Priority;
	}
	public void setPriority(String priority)
	{
		_Priority = priority;
	}
	public String getMessage()
	{
		return _Message;
	}
	public void setMessage(String message)
	{
		_Message = message;
	}
}
