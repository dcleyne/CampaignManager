/**
 * Created on 26/05/2007
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
package bt.common.net;

public class Version
{
	private int m_Major = 0;
	private int m_Minor = 0;
	private int m_Sub = 0;
	private int m_Revision = 0;
	
	public Version(int major, int minor, int sub, int revision)
	{
		m_Major = major;
		m_Minor = minor;
		m_Sub = sub;
		m_Revision = revision;		
	}
	
	public Version(String version)
	{
		String[] arr = version.split("\\.");
		if (arr.length != 4)
			throw new RuntimeException("Invalid version string : " + version);
		
		m_Major = Integer.parseInt(arr[0]);
		m_Minor = Integer.parseInt(arr[1]);
		m_Sub = Integer.parseInt(arr[2]);
		m_Revision = Integer.parseInt(arr[3]);
	}
	
	public int getMajor()
	{ return m_Major; }
	
	public int getMinor()
	{ return m_Minor; }
	
	public int getSub()
	{ return m_Sub; }
	
	public int getRevision()
	{ return m_Revision; }
	
	public boolean isVersionSameOrLater(Version version)
	{
		if (version.m_Major < this.m_Major) return false;
		if (version.m_Minor < this.m_Minor) return false;
		if (version.m_Sub < this.m_Sub) return false;
		if (version.m_Revision < this.m_Revision) return false;
		return true;
	}
	
	public String toString()
	{
		String retVal = "";
		retVal += Integer.toString(m_Major) + ".";
		retVal += Integer.toString(m_Minor) + ".";
		retVal += Integer.toString(m_Sub) + ".";
		retVal += Integer.toString(m_Revision);		
		return retVal;
	}
}
