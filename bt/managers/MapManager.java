package bt.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import bt.elements.mapping.MapSet;
import bt.elements.mapping.MapSheet;
import bt.util.Dice;
import bt.util.PropertyUtil;

public class MapManager
{
	private static MapManager theInstance;
    
	public enum Terrain
	{
		RANDOM,
		HILLS,
		BADLANDS,
		WETLANDS,
		LIGHT_URBAN,
		FLATLANDS,
		WOODED,
		HEAVY_URBAN,
		COASTAL,
		MOUNTAINS;
		
		private static String[] Names = {"Random","Hills","Badlands","Wetlands","Light Urban","Flatlands","Wooded","Heavy Urban","Coastal","Mountains"};
		
		private static String[][] Tables = {
				{"Desert Hills","Rolling Hills #1","Rolling Hills #2","Woodland","Box Canyon","Battleforce"},
				{"Desert Sinkhole #1","Desert Sinkhole #2","Moonscape #1","Moonscape #2","Desert Mountain #1","Desert Mountain #2"},
				{"Wide River","Lake Area","Large Lakes #1","Large Lakes #2","River Delta/Drainage Basin #1","River Delta/Drainage Basin #2"},
				{"City (Residential)","City (Suburbs)","City (Hills/Residential) #1","City (Hills/Residential) #2","City Street Grid/Park #1","City Street Grid/Park #2"},
				{"Open Terrain #1","Open Terrain #2","Desert Hills","City Ruins","CityTech Map","Scattered Woods"},
				{"Scattered Woods","Battletech","Woodland","Rolling Hills #1","Heavy Forest #1","Heavy Forest #2"},
				{"Military Base #1","Military Base #2","Drop Port #1","Drop Port #2","City (Skyscraper)","City (Downtown"},
				{"Archipelago #1","Archipelago #2","Coast #1","Coast #2","Seaport","River Delta/Drainage Basin #1"},
				{"Mountain Lake","River Valley","Desert Mountain #1","Desert Mountain #2","Large Mountain #1","Large Mountain #2"}
		};
		
		
		public static Terrain fromString(String string) throws Exception
		{
			for (Terrain terrain: values())
			{
				if (Names[terrain.ordinal()].equalsIgnoreCase(string))
					return terrain;
			}
			throw new Exception("Unable to get Terrain from String: " + string);
		}
		
		public String getRandomMapName()
		{
			return Tables[ordinal()-1][Dice.d6(1) - 1];
		}
		
		@Override
		public String toString()
		{
			return Names[ordinal()];
		}
	}
	
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
			ex.printStackTrace();
		}
	}
	
	public static MapManager getInstance()
	{
		if (theInstance == null)
			theInstance = new MapManager();
		
		return theInstance; 
	}
	
	@SuppressWarnings("unchecked")
	private void loadMaps()
	{
		_MapSheets.clear();
		_MapSets.clear();
		
        String Path = PropertyUtil.getStringProperty("DataPath","data");
        
        try {            
            SAXBuilder b = new SAXBuilder();
            Document mapDoc = b.build(new File(Path, "/Maps.xml"));
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
            System.out.println("Error Opening Player File!");
            ex.printStackTrace();
        } catch (JDOMException jdex) {
        	System.out.println("Failure Parsing Player File!");
        	jdex.printStackTrace();
        } catch (Exception exx) {
        	System.out.println("Failure Loading Player!");
        	exx.printStackTrace();
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
	
	public MapSet getRandomMapSet(int maps, int rows, int columns) throws Exception
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
			throw new Exception("Cannot find MapSet for Maps:" + maps + " Rows:" + rows + " Cols:" + columns);
	}
	
	public MapSet generateRandomMapSet(String terrainName, int maps) throws Exception
	{
		if (maps < 2)
			maps = 2;
		
		if (maps % 2 != 0)
			maps++;
		if (maps > 6)
			maps = 6;
		
		int rows = maps == 2 ? 1 : 2;
		int columns = maps / rows;
		
		Terrain terrain = Terrain.fromString(terrainName);
		if (terrain == Terrain.RANDOM)
			terrain = generateRandomTerrain();
		
		ArrayList<String> selectedMaps = new ArrayList<String>();
		for (int i = 0; i < maps; i++)
		{
			String selectedMap = terrain.getRandomMapName();
			while (selectedMaps.contains(selectedMap))
				selectedMap = terrain.getRandomMapName();
			selectedMaps.add(selectedMap);
		}
		MapSet mapSet = new MapSet();
		mapSet.setName(terrain.toString());
		mapSet.setRowCount(rows);
		mapSet.setColumnCount(columns);
		mapSet.setMapCount(maps);
		
		int mapNumber = 0;
		for (int col = 1; col <= columns; col++)
		{
			for (int row = 1; row <= rows; row++)
			{
				String mapName = selectedMaps.get(mapNumber);
				mapSet.getCells().addElement(mapSet.new MapCell(mapName, ++mapNumber, row, col));
			}
		}
		return mapSet;
	}
	
	private Terrain generateRandomTerrain()
	{
		switch (Dice.d6(2))
		{
			case 2:
				return Terrain.HILLS;
			case 3:
				return Terrain.BADLANDS;
			case 4:
				return Terrain.WETLANDS;
			case 5:
				return Terrain.LIGHT_URBAN;
			case 6:
				return Terrain.HILLS;
			case 7:
				return Terrain.FLATLANDS;
			case 8:
				return Terrain.WOODED;
			case 9:
				return Terrain.HEAVY_URBAN;
			case 10:
				return Terrain.COASTAL;
			case 11:
				return Terrain.WOODED;
			case 12:
				return Terrain.MOUNTAINS;
			default:
				return Terrain.FLATLANDS;
		}
	}
}
