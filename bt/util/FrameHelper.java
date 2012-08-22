/**
 * Created on Nov 27, 2007
 * <p>Title: Legatus</p>
 *
 * <p>Copyright: Copyright Daniel Cleyne (c) 2004-2007</p>
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
package bt.util;

import java.awt.Frame;

public class FrameHelper 
{
	public static Frame getActiveFrame()
	{
		Frame[] frames = Frame.getFrames();
		
		for (Frame f: frames)
			if (f.isActive())
				return f;
		
		if (frames.length > 0)
			return frames[0];
		
		return null;
	}
}
