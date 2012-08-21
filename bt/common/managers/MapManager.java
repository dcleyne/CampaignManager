package bt.common.managers;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import bt.common.elements.mapping.MapSet;
import bt.common.elements.mapping.MapSheet;
import bt.common.util.Dice;
import bt.common.util.ExceptionUtil;

public class MapManager
{
	private static MapManager theInstance = new MapManager();
    private static Log log = LogFactory.getLog(MapManager.class);
    
    private HashMap<String, MapSheet> _MapSheets = new HashMap<String, MapSheet>();
    private Vector<MapSet> _MapSets = new Vector<MapSet>();

	private MapManager()
	{
		try
		{
			loadMaps();
		}
		catch (Exception ex)
		{
			log.fatal(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}
	
	public static MapManager getInstance()
	{ return theInstance; }
	
	@SuppressWarnings("unchecked")
	private void loadMaps()
	{
		_MapSheets.clear();
		_MapSets.clear();
		
        String Path = System.getProperty("DataPath");
        if (Path == null)
            Path = "data/";
        
        try {            
            SAXBuilder b = new SAXBuilder();
            Document mapDoc = b.build(new File(Path, "Maps.xml"));
            Element root = mapDoc.getRootElement();
            List<Element> mapElements = root.getChildren("Map");
            for (Element mapElement: mapElements)
            {
            	MapSheet ms = loadMapSheet(mapElement);
            	_MapSheets.put(ms.getName(),ms);            	
            }
            List<Element> mapSetElements = root.getChildren("MapSet");
            for (Element mapSetElement: mapSetElements)
            {
            	MapSet ms = loadMapSet(mapSetElement);
            	_MapSets.add(ms);            	
            }
    	
        } catch(java.io.IOException ex) {
            log.info("Error Opening Player File!");
            log.error(ex);
        } catch (JDOMException jdex) {
            log.info("Failure Parsing Player File!");
            log.error(jdex);
        } catch (Exception exx) {
            log.info("Failure Loading Player!");
            log.error(exx);
        }
	}
	
	private MapSheet loadMapSheet(org.jdom.Element element)
	{
		MapSheet sheet = new MapSheet();
		
		sheet.setName(element.getAttributeValue("Name"));
		sheet.setSet(element.getAttributeValue("Set"));
		sheet.setNotes(element.getAttributeValue("Notes"));
		
		if (element.getChild("Locations") != null)
		{
			String[] locations = element.getChildText("Locations").split(",");
			for (String location : locations)
				sheet.getLocations().add(location);
		}
		return sheet;
	}
	
	public MapSet loadMapSet(org.jdom.Element element)
	{
		MapSet set = new MapSet();
		
		set.setName(element.getAttributeValue("Name"));
		set.setMapCount(Integer.parseInt(element.getAttributeValue("Maps")));
		set.setRowCount(Integer.parseInt(element.getAttributeValue("Rows")));
		set.setColumnCount(Integer.parseInt(element.getAttributeValue("Columns")));
		
		Iterator<?> iter = element.getChildren("Map").iterator();
		while (iter.hasNext())
		{
			org.jdom.Element mapElement = (org.jdom.Element)iter.next();
			
			MapSet.MapCell cell = set.new MapCell();
			cell.setMapName(mapElement.getAttributeValue("Name"));
			cell.setRow(Integer.parseInt(mapElement.getAttributeValue("Row")));
			cell.setColumn(Integer.parseInt(mapElement.getAttributeValue("Column")));
			cell.setMapNumber(Integer.parseInt(mapElement.getAttributeValue("Number")));
			set.getCells().add(cell);
		}
		return set;
	}

	public org.jdom.Element saveMapSet(MapSet set)
	{
		org.jdom.Element element = new Element("MapSet");
		
		element.setAttribute("Name",set.getName());
		element.setAttribute("Maps",Integer.toString(set.getMapCount()));
		element.setAttribute("Rows",Integer.toString(set.getRowCount()));
		element.setAttribute("Columns",Integer.toString(set.getColumnCount()));

		for (MapSet.MapCell cell : set.getCells())
		{
			org.jdom.Element mapElement = new org.jdom.Element("Map");
			
			mapElement.setAttribute("Name",cell.getMapName());
			mapElement.setAttribute("Row",Integer.toString(cell.getRow()));
			mapElement.setAttribute("Column",Integer.toString(cell.getColumn()));
			mapElement.setAttribute("Number",Integer.toString(cell.getMapNumber()));
			element.addContent(mapElement);
		}
		return element;
	}

	public Vector<String> getMapSheetList()
	{
		Vector<String> sheetList = new Vector<String>(_MapSheets.keySet());
		return sheetList;
	}
	
	public MapSheet getMapSheet(String mapName)
	{
		return _MapSheets.get(mapName);
	}

	public Vector<String> getMapSetList()
	{
		Vector<String> setList = new Vector<String>();
		for (MapSet set : _MapSets)
			setList.add(set.getName());
		return setList;
	}
	
	public MapSet getMapSet(String name)
	{
		for (MapSet set : _MapSets)
			if (set.getName().equalsIgnoreCase(name))
				return set;
		
		return null;
	}
	
	public Vector<MapSet> getMapSets()
	{
		return _MapSets;
	}
	
	public MapSet getRandomMapSet(int maps, int rows, int columns)
	{
		Vector<MapSet> mapSets = new Vector<MapSet>(_MapSets);
		for (int i = mapSets.size() - 1; i >= 0; i--)
		{
			MapSet mapSet = mapSets.elementAt(i);
			if (mapSet.getMapCount() != maps || mapSet.getRowCount() != rows || mapSet.getColumnCount() != columns)
				mapSets.remove(i);
		}
		if (mapSets.size() > 0)
			return mapSets.elementAt(Dice.random(mapSets.size()) - 1);
		else
			throw new RuntimeException("Cannot find MapSet for Maps:" + maps + " Rows:" + rows + " Cols:" + columns);
	}
	
}
