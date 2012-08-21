package bt.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper 
{
	private static DateFormat m_LongDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	
	public static String longDateAsString(Date aDate)
	{
		return m_LongDateFormat.format(aDate);
	}
	
	public static Date longDateFromString(String dateString)
	{
		try
		{
			return m_LongDateFormat.parse(dateString);
		}
		catch (Exception ex)
		{
			
		}
		return null;
	}
}
