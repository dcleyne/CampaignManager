package bt.util;

import java.util.ArrayList;

import bt.elements.unit.RandomName;

public class XtraRantComRandomNameParser extends RandomNameParser
{

	@Override
	public ArrayList<RandomName> parseRandomNames(String nameStream)
	{
		ArrayList<RandomName> result = new ArrayList<RandomName>();
		
		String firstName = "";
		String lastName = "";
		boolean finished = false;
		
		while (!finished)
		{
			int hrefPos = nameStream.indexOf("HREF=");
			int rightBracketPos = nameStream.indexOf(">", hrefPos);
			int leftBracketPos = nameStream.indexOf("<", rightBracketPos);
			
			firstName = nameStream.substring(rightBracketPos + 1, leftBracketPos).trim();
			if (firstName.equals("RNG Home"))
			{
				finished = true;
				continue;
			}
			
			hrefPos = nameStream.indexOf("HREF=",leftBracketPos);
			rightBracketPos = nameStream.indexOf(">", hrefPos);
			leftBracketPos = nameStream.indexOf("<", rightBracketPos);
			
			lastName = nameStream.substring(rightBracketPos + 1, leftBracketPos).trim();
			
			result.add(new RandomName(firstName,lastName));
			nameStream = nameStream.substring(leftBracketPos);
		}
		
		return result;
	}

	
}
