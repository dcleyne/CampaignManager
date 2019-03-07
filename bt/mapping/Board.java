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

// Super class for all board types
public abstract class Board implements PersistToXML
{
	public static final String BOARD = "Board";
	private static final String CLASS_NAME = "ClassName";
	private static final String NAME = "Name";

	static final long serialVersionUID = 1;


	private String _Name = "";

	public void setName(String name)
	{
		_Name = name;
	}

	public String getName()
	{
		return _Name;
	}
	
	public void clearBoardElements()
	{
	}
	

	public abstract String getMapType();
	public abstract void clear();
	public abstract void addMap(Map map, Coordinate coord) throws Exception;
	public abstract void completedAddingMaps();
	public abstract void saveToXMLElement(org.jdom.Element e);
	public abstract void loadFromXMLElement(org.jdom.Element e) throws Exception;

	public abstract void loadFromDataElement(org.jdom.Element e) throws Exception;
	public abstract void saveToDataElement(org.jdom.Element e);

    public org.jdom.Element saveToElement()
    {
    	org.jdom.Element e = new org.jdom.Element(BOARD);
    	e.setAttribute(NAME, _Name);
    	e.setAttribute(CLASS_NAME, getClass().getName());
    	
    	saveToXMLElement(e);
    	return e;
    }
    
	public void loadFromElement(org.jdom.Element e) throws Exception
    {
    	_Name = e.getAttributeValue(NAME);
    	
    	loadFromXMLElement(e);    	
    }

    public static Board loadBoardFromElement(org.jdom.Element e) throws Exception
    {
    	if (e.getAttribute(CLASS_NAME) != null)
    	{
    		String className = e.getAttributeValue(CLASS_NAME);
        	Class<?> gameClass = Class.forName(className);
    		
        	Class<?> paramTypes[] = {}; 
        	Constructor<?> boardConstructor = gameClass.getDeclaredConstructor(paramTypes);
        	Object args[] = {};
        	Board b = (Board) boardConstructor.newInstance(args);
        	b.loadFromElement(e);
        	return b;
    	}
    	
    	throw new Exception("Unable to load Board from Element of Type(" + e.getName() + ")");
    }
    
	public final void saveToFile(File f) throws Exception
	{
		Document doc = new Document();
		Element root = new Element(getMapType() + "Board");
        doc.setRootElement(root);
        saveToDataElement(root);

        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat()); 
        FileOutputStream fos = new FileOutputStream(f);
        out.output(doc, fos);
        fos.close();
	}
	
	public final void load(String filename)
	{
		try
		{
			clearBoardElements();

			SAXBuilder b = new SAXBuilder();
			Document MapDoc = b.build(FileUtil.openDataResource(filename));
			Element root = MapDoc.getRootElement();
			
			loadFromDataElement(root);

			completedAddingMaps();
		} 
		catch (Exception exx)
		{
			System.out.print("Failure Loading Board File: " + filename);
			exx.printStackTrace();
		}

	}


}
