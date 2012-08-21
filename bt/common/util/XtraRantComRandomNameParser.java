package bt.common.util;

import java.util.Vector;

import bt.common.elements.unit.RandomName;

public class XtraRantComRandomNameParser extends RandomNameParser
{

	@Override
	public Vector<RandomName> parseRandomNames(String nameStream)
	{
		Vector<RandomName> result = new Vector<RandomName>();
		
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
