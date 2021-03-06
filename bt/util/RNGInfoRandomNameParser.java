package bt.util;

import java.util.ArrayList;

import bt.elements.unit.RandomName;

public class RNGInfoRandomNameParser extends RandomNameParser
{

	@Override
	public ArrayList<RandomName> parseRandomNames(String nameStream)
	{
		ArrayList<RandomName> result = new ArrayList<RandomName>();
		
		boolean finished = false;
		
		nameStream = nameStream.substring(nameStream.indexOf("<div class=\"results\">"));
		
		while (!finished)
		{
			if (nameStream.substring(0,4).equals("</ol>"))
			{
				finished = true;
				continue;
			}
			
			int LILeftPos = nameStream.indexOf("<li>");
			int LIRightPos = nameStream.indexOf("<li>", LILeftPos + 4);
			
			if (LIRightPos > LILeftPos && LILeftPos >= 0)
			{
				String name = nameStream.substring(LILeftPos + 4, LIRightPos).trim();
				String[] splits = name.replace("\t", "").split(" ");
				if (splits.length > 1)
				{
					RandomName rn = new RandomName(splits[0],splits[1]);
					result.add(rn);
				}
				
				nameStream = nameStream.substring(LIRightPos);
			}
			else
				finished = true;
		}
		
		return result;
	}

	
}
