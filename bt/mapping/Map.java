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

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import bt.util.FileUtil;
import bt.util.PersistToXML;

public abstract class Map implements PersistToXML
{
	public static final String MAP = "Map";
	private static final String CLASS_NAME = "ClassName";
	private static final String NAME = "Name";
	static final long serialVersionUID = 1;

	public enum Edge
	{
		TOP, LEFT, BOTTOM, RIGHT;
	}

	private String _Name = "";

	public String getName()
	{
		return _Name;
	}

	public void setName(String Name)
	{
		_Name = Name;
	}

	public abstract String getMapType();
	
	public abstract void saveToXMLElement(Element e);
	public abstract void saveToDataElement(Element e);
	public abstract void loadFromXMLElement(Element e) throws Exception;

	public Element saveToElement()
	{
		Element e = new Element(MAP);
		e.setAttribute(CLASS_NAME, getClass().getName());
		e.setAttribute(NAME, _Name);
		
		saveToXMLElement(e);
		
		return e;
	}
	
	public void loadFromElement(Element e) throws Exception
	{
		Element nameElement = e.getChild(NAME);
		if (nameElement != null)
			_Name = nameElement.getText();
		
		loadFromXMLElement(e);
	}
	
	public static Map loadMapFromElement(Element e) throws Exception
	{
    	if (e.getAttribute(CLASS_NAME) != null)
    	{
    		String className = e.getAttributeValue(CLASS_NAME);
        	Class<?> MapClass = Class.forName(className);
    		
        	Class<?> paramTypes[] = {}; 
        	Constructor<?> MapConstructor = MapClass.getDeclaredConstructor(paramTypes);
        	Object args[] = {};
        	Map map = (Map) MapConstructor.newInstance(args);
        	map.loadFromElement(e);
        	return map;
    	}
    	
    	throw new Exception("Unable to load Map from Element of Type(" + e.getName() + ")");
	}
	
	public final void load(String mapName)
	{
		try
		{
			SAXBuilder b = new SAXBuilder();
			Document mapDoc = b.build(FileUtil.openDataResource(mapName));

			loadFromElement(mapDoc.getRootElement());

		} 
		catch (Exception exx)
		{
			exx.printStackTrace();
		}
	}

	public final void saveToFile(File f) throws Exception
	{
		Document doc = new Document();
		Element rootElement = new Element(getMapType() + "Map");
        doc.setRootElement(rootElement);
        saveToDataElement(rootElement);
        
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());  
        FileOutputStream fos = new FileOutputStream(f);
        out.output(doc, fos);
        fos.close();
	}

}
