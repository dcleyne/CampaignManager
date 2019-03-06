package bt.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelper 
{
	private static DateTimeFormatter m_LongDateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	
	public static String longDateAsString(LocalDate aDate)
	{
		return m_LongDateFormat.format(aDate);
	}
	
	public static LocalDate longDateFromString(String dateString)
	{
		try
		{
			return LocalDate.parse(dateString, m_LongDateFormat);
		}
		catch (Exception ex)
		{
			
		}
		return null;
	}
}
