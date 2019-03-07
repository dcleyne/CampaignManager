/*******************************************************************************
 * Title: Legatus
 * 
 * Copyright Daniel Cleyne (c) 2004-2013
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at our option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * @author Daniel Cleyne
 ******************************************************************************/

package bt.mapping;

import java.lang.reflect.Constructor;
import org.jdom.Element;
import bt.util.PersistToXML;

public abstract class TerrainFeature implements PersistToXML
{
	public static final String TERRAIN_FEATURE = "TerrainFeature";
	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = "ClassName";
	private static final String NAME = "Name";
	private String _Name;
	
	public TerrainFeature(String name)
	{
		_Name = name;
	}

	public String getName()
	{
		return _Name;
	}

	public void setName(String name)
	{
		_Name = name;
	}

	public Element saveToElement()
	{
		Element e = new Element(TERRAIN_FEATURE);
		e.setAttribute(NAME, _Name);
		e.setAttribute(CLASS_NAME, getClass().getName());
		
		return e;
	}

	public void loadFromElement(Element e) throws Exception
	{
		_Name = e.getAttributeValue(NAME);
	}

    public static TerrainFeature loadTerrainFeatureFromElement(org.jdom.Element e) throws Exception
    {
    	if (e.getAttribute(CLASS_NAME) != null && e.getAttribute(NAME) != null)
    	{
    		String className = e.getAttributeValue(CLASS_NAME);
        	Class<?> gameClass = Class.forName(className);
    		
        	Class<?> paramTypes[] = {String.class}; 
        	Constructor<?> terrainFeatureConstructor = gameClass.getDeclaredConstructor(paramTypes);
        	Object args[] = {e.getAttributeValue(NAME)};
        	TerrainFeature tf = (TerrainFeature)terrainFeatureConstructor.newInstance(args);
        	tf.loadFromElement(e);
        	return tf;
    	}
    	
    	throw new Exception("Unable to load TerrainFeature from Element of Type(" + e.getName() + ")");
    }
	
}
