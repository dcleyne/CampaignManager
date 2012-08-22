package bt.elements.scenario;

public class ComstarMessage
{
	private String _MessageDate;
	private String _Sender;
	private String _Recipient;
	private String _ComstarOfficial;
	private String _ComstarFacility;
	private String _RecipientFacility;
	private String _Message;
	public String getMessageDate()
	{
		return _MessageDate;
	}
	public void setMessageDate(String messageDate)
	{
		_MessageDate = messageDate;
	}
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
	public String getComstarOfficial()
	{
		return _ComstarOfficial;
	}
	public void setComstarOfficial(String comstarOfficial)
	{
		_ComstarOfficial = comstarOfficial;
	}
	public String getComstarFacility()
	{
		return _ComstarFacility;
	}
	public void setComstarFacility(String comstarFacility)
	{
		_ComstarFacility = comstarFacility;
	}
	public String getRecipientFacility()
	{
		return _RecipientFacility;
	}
	public void setRecipientFacility(String recipientFacility)
	{
		_RecipientFacility = recipientFacility;
	}
	public String getMessage()
	{
		return _Message;
	}
	public void setMessage(String message)
	{
		_Message = message;
	}
	

	public String GetFormattedMessage()
	{
		String retVal = "--------------------\n";
		retVal += "Start of Transmission " + _MessageDate + "\n\n";
		retVal += "Message Authorised by : " + _ComstarOfficial + "\n";
		retVal += "Message Originated at : " + _ComstarFacility + "\n\n";
		retVal += "--------------------\n";
		retVal += "Message Received at : " + _RecipientFacility + "\n";
		retVal += "--------------------\n";
		retVal += "Message Addressed to : " + _Recipient + "\n\n";
		retVal += "Message Sent from : " + _Sender + "\n\n";
		retVal += "MessageFollows\n";
		retVal += "--------------------\n";
		retVal += _Message + "\n";
		retVal += "--------------------\n";
		retVal += "End of Message\n\n";
		retVal += "End of Transmission " + _MessageDate + "\n";
		return retVal;
	}

}
