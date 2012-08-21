/**
 * Created on 12/03/2006
 * <p>Title: Legatus</p>
 *
 * <p>Copyright: Copyright Daniel Cleyne (c) 2004-2006</p>
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
package bt.common.util;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertyUtil
{
	private static Log log = LogFactory.getLog(PropertyUtil.class);
	
    private PropertyUtil()
    {
        
    }
    
    public static void loadSystemProperties(String PropertyFileName) throws Exception
    {
        Properties SystemProperties = System.getProperties();

        Properties ServerProperties = new Properties();
        ServerProperties.load(new FileInputStream(PropertyFileName));
        
        SystemProperties.putAll(ServerProperties);
        System.setProperties(SystemProperties);
    }
    
    public static int getIntProperty(String propertyName, int defaultValue)
    {
    	try
    	{
	    	String property = System.getProperty(propertyName);
	    	if (property != null)
	    		return Integer.parseInt(property);
	    	else
	    		return defaultValue;
    	}
    	catch (Exception ex)
    	{
    		log.error("Property not set '" + propertyName + "' using default value of (" + Integer.toString(defaultValue) + ")");
    		return defaultValue;
    	}
    }

    public static boolean getBooleanProperty(String propertyName, boolean defaultValue)
    {
    	try
    	{
	    	String property = System.getProperty(propertyName);
	    	if (property != null)
	    		return Boolean.parseBoolean(property);
	    	else
	    		return defaultValue;
    	}
    	catch (Exception ex)
    	{
    		log.error("Property not set '" + propertyName + "' using default value of (" + Boolean.toString(defaultValue) + ")");
    		return defaultValue;
    	}
    }

    public static long getLongProperty(String propertyName, long defaultValue)
    {
    	try
    	{
	    	String property = System.getProperty(propertyName);
	    	if (property != null)
	    		return Long.parseLong(property);
	    	else
	    		return defaultValue;
    	}
    	catch (Exception ex)
    	{
    		log.error("Property not set '" + propertyName + "' using default value of (" + Long.toString(defaultValue) + ")");
    		return defaultValue;
    	}
    }

    public static String getStringProperty(String propertyName, String defaultValue)
    {
    	try
    	{
	    	String property = System.getProperty(propertyName);
	    	if (property != null)
	    		return property;
	    	else
	    		return defaultValue;
    	}
    	catch (Exception ex)
    	{
    		log.error("Property not set '" + propertyName + "' using default value of (" + defaultValue + ")");
    		return defaultValue;
    	}
    }
    
    public static void setIntProperty(String propertyName, int value)
    {
    	try
    	{
    		System.setProperty(propertyName, Integer.toString(value));
    	}
    	catch (Exception ex)
    	{
    		log.error("Property not set '" + propertyName + "' with value of (" + Integer.toString(value) + ")");
    	}
    }

    public void setBooleanProperty(String propertyName, boolean value)
    {
    	try
    	{
	    	System.setProperty(propertyName,Boolean.toString(value));
    	}
    	catch (Exception ex)
    	{
    		log.error("Property not set '" + propertyName + "' with value of (" + Boolean.toString(value) + ")");
    	}
    }

    public static void setLongProperty(String propertyName, long value)
    {
    	try
    	{
	    	System.setProperty(propertyName,Long.toString(value));
    	}
    	catch (Exception ex)
    	{
    		log.error("Property not set '" + propertyName + "' with value of (" + Long.toString(value) + ")");
    	}
    }

    public static void setStringProperty(String propertyName, String value)
    {
    	try
    	{
	    	System.setProperty(propertyName,value);
    	}
    	catch (Exception ex)
    	{
    		log.error("Property not set '" + propertyName + "' with value of (" + value + ")");
    	}
    }
    
}
