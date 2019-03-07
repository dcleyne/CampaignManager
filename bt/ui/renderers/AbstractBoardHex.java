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

package bt.ui.renderers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import bt.mapping.Decoration;
import bt.mapping.MapHex;
import bt.mapping.TerrainFeature;
import bt.mapping.TerrainType;
import bt.util.PersistToXML;

public abstract class AbstractBoardHex implements PersistToXML
{
	public static final String MAP_HEX = "MapHex";
	private static final String TERRAIN = "Terrain";
	private static final String CLASS_NAME = "ClassName";
	static final long serialVersionUID = 1;

	private TerrainType _TerrainType;
	private ArrayList<TerrainFeature> _TerrainFeatures= new ArrayList<TerrainFeature>();
	private ArrayList<Decoration> _Decorations = new ArrayList<Decoration>();

	public AbstractBoardHex()
	{
		_TerrainType = TerrainType.NONE;
	}

	public AbstractBoardHex(TerrainType terrainType)
	{
		setTerrainType(terrainType);
	}

	public TerrainType getTerrainType()
	{
		return _TerrainType;
	}

	public ArrayList<TerrainFeature> getTerrainFeatures()
	{
		return _TerrainFeatures;
	}

	public void setTerrainType(TerrainType terrainType)
	{
		if (terrainType != TerrainType.NONE)
		{
			_TerrainType = terrainType;
		}
	}
	
	public void clearDecorations()
	{
		_Decorations.clear();
	}
	
	public void addDecoration(Decoration d)
	{
		_Decorations.add(d);
	}
	
	public ArrayList<Decoration> getDecorations()
	{
		return _Decorations;
	}
	
	public Element saveToElement()
	{
    	org.jdom.Element e = new org.jdom.Element(MAP_HEX);
    	e.setAttribute(CLASS_NAME, getClass().getName());
    	
    	e.addContent(new Element(TERRAIN).setText(_TerrainType.toString()));
    	
		for (TerrainFeature feature : _TerrainFeatures)
		{
			e.addContent(feature.saveToElement());
		}

		for (Decoration d : _Decorations)
		{
			e.addContent(d.saveToElement());
		}		

		saveToXMLElement(e);
		
		return e;
	}
	
	@SuppressWarnings("unchecked")
	public void loadFromElement(Element e) throws Exception
	{
		Element terrainTypeElement = e.getChild(TERRAIN);
		if (terrainTypeElement != null)
			setTerrainType(TerrainType.fromString(terrainTypeElement.getText()));
		
		_TerrainFeatures.clear();
		for (Element terrainFeatureElement : (List<Element>)e.getChildren(TerrainFeature.TERRAIN_FEATURE))
		{
			_TerrainFeatures.add(TerrainFeature.loadTerrainFeatureFromElement(terrainFeatureElement));
		}
		
		_Decorations.clear();
		for (Element decorationElement : (List<Element>)e.getChildren(Decoration.DECORATION))
		{
			_Decorations.add(Decoration.loadDecorationFromElement(decorationElement));
		}
		
		loadFromXMLElement(e);
	}
	
	public static MapHex loadMapHexFromElement(Element e) throws Exception
	{
    	if (e.getAttribute(CLASS_NAME) != null)
    	{
    		String className = e.getAttributeValue(CLASS_NAME);
        	Class<?> MapHexClass = Class.forName(className);
    		
        	Class<?> paramTypes[] = {}; 
        	Constructor<?> MapHexConstructor = MapHexClass.getDeclaredConstructor(paramTypes);
        	Object args[] = {};
        	MapHex mh = (MapHex) MapHexConstructor.newInstance(args);
        	mh.loadFromElement(e);
        	return mh;
    	}
    	
    	throw new Exception("Unable to load MapHex from Element of Type(" + e.getName() + ")");
	}

	public abstract void saveToXMLElement(Element e);
	public abstract void loadFromXMLElement(Element e) throws Exception;
}
