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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import bt.mapping.Board;
import bt.mapping.Map;
import bt.util.ExceptionUtil;
import bt.util.FileUtil;
import bt.util.PropertyUtil;

public enum MapFactory
{
	INSTANCE;

	private HashMap<String, MapType> _MapTypes;

	private MapFactory()
	{
		refresh();
	}

	public void refresh()
	{
		_MapTypes = new HashMap<String, MapType>();
		loadMapTypes();		
	}
	
	public MapType getMapType(String gameType)
	{
		return _MapTypes.get(gameType);
	}

	private void loadMapTypes()
	{
		try
		{
			String Path = PropertyUtil.getStringProperty("DataPath", "data/");
			
			SAXBuilder b = new SAXBuilder();
			Document MapTypeDoc = b.build(FileUtil.openDataResource(Path, "MapTypes.xml"));
			Element root = MapTypeDoc.getRootElement();

			List<?> MapTypes = root.getChildren();
			for (int i = 0; i < MapTypes.size(); i++)
			{
				Element MapTypeElement = (Element) MapTypes.get(i);
				MapType mt = new MapType();

				List<?> Values = MapTypeElement.getChildren();
				for (int j = 0; j < Values.size(); j++)
				{
					Element val = (Element) Values.get(j);
					if (val.getName().equalsIgnoreCase("Name"))
					{
						mt.setName(val.getValue());
					}
					if (val.getName().equalsIgnoreCase("MapClass"))
					{
						mt.setMapClassName(val.getValue());
					}
					if (val.getName().equalsIgnoreCase("MapLocation"))
					{
						mt.setMapLocation(val.getValue());
					}
					if (val.getName().equalsIgnoreCase("BoardClass"))
					{
						mt.setBoardClassName(val.getValue());
					}
					if (val.getName().equalsIgnoreCase("BoardLocation"))
					{
						mt.setBoardLocation(val.getValue());
					}
					if (val.getName().equalsIgnoreCase("BoardRendererClass"))
					{
						mt.setBoardRendererClassName(val.getValue());
					}
				}
				_MapTypes.put(mt.getName(), mt);
			}

		} 
		catch (Exception exx)
		{
			System.out.print("Failure Loading MapTypes!");
			exx.printStackTrace();
		}
	}

	public ArrayList<String> getLocalMapTypes()
	{
		ArrayList<String> maps = new ArrayList<String>(_MapTypes.keySet());
		return maps;
	}

	public ArrayList<String> getAvailableMaps(String mapType) throws Exception
	{
		ArrayList<String> Maps = null;

		MapType mp = _MapTypes.get(mapType);
		if (mp != null)
		{
			Maps = mp.getAvailableMaps();
		} else
		{
			throw new Exception("Map Type Not Supported : " + mapType);
		}
		return Maps;
	}

	public Map getMap(String mapType, String mapName) throws Exception
	{
		Map m = null;
		MapType mt = _MapTypes.get(mapType);
		if (mt != null)
		{
			m = mt.getMap(mt.getMapLocation() + "/" + mapName);
		} else
		{
			throw new Exception("Map Type Not Supported : " + mapType);
		}

		return m;
	}

	public Map createMap(String mapType) throws Exception
	{
		MapType mp = _MapTypes.get(mapType);
		if (mp != null)
		{
			return mp.InstantiateMap();
		} else
		{
			throw new Exception("Map Type Not Supported : " + mapType);
		}
	}

	public ArrayList<String> getAvailableBoards(String mapType) throws Exception
	{
		ArrayList<String> Boards = null;

		MapType mp = _MapTypes.get(mapType);
		if (mp != null)
		{
			Boards = mp.getAvailableBoards();
		} else
		{
			throw new Exception("Map Type Not Supported : " + mapType);
		}
		return Boards;
	}

	public Board getBoard(String mapType, String boardName) throws Exception
	{
		Board b = null;
		MapType mt = _MapTypes.get(mapType);
		if (mt != null)
		{
			b = mt.getBoard(mt.getBoardLocation() + "/" + boardName);
		} else
		{
			throw new Exception("Map Type Not Supported : " + mapType);
		}

		return b;
	}

	public Board createBoard(String mapType) throws Exception
	{
		MapType mp = _MapTypes.get(mapType);
		if (mp != null)
		{
			return mp.instantiateBoard();
		} else
		{
			throw new Exception("Map Type Not Supported : " + mapType);
		}
	}

	public BoardRenderer createBoardRenderer(String mapType) throws Exception
	{
		MapType mp = _MapTypes.get(mapType);
		if (mp != null)
		{
			return mp.instantiateBoardRenderer();
		} else
		{
			throw new Exception("Map Type Not Supported : " + mapType);
		}
	}

	public class MapType
	{
		private String _Name;
		private String _MapClassName;
		private String _MapLocation;
		private String _BoardClassName;
		private String _BoardLocation;
		private String _BoardRendererClassName;

		private ArrayList<String> _MapFileList = new ArrayList<String>();
		private HashMap<String, Map> _MapCache = new HashMap<String, Map>();

		private ArrayList<String> _BoardFileList = new ArrayList<String>();
		private HashMap<String, Board> _BoardCache = new HashMap<String, Board>();

		public MapType()
		{
		}

		private boolean isSuperClass(String superName, Class<?> c)
		{
			Class<?> superClass = c.getSuperclass();
			if (superClass != null)
			{
				if (superClass.getName().equalsIgnoreCase(superName))
				{
					return true;
				} else
				{
					return isSuperClass(superName, superClass);
				}
			} else
			{
				return false;
			}
		}

		private Object instantiateClass(String className, String superName, Class<?>[] argTypes, Object[] args) throws Exception
		{
			try
			{
				Class<?> imClass = Class.forName(className, true, ClassLoader.getSystemClassLoader());
				if (!isSuperClass(superName, imClass))
				{
					String error = "Class does not extend " + superName + " : " + className;
					throw new Exception(error);
				}
				java.lang.reflect.Constructor<?> objectConstructor = imClass.getConstructor(argTypes);
				return objectConstructor.newInstance(args);
			} catch (Exception e)
			{
				throw new Exception(ExceptionUtil.getExceptionStackTrace(e));
			}
		}

		public void setName(String name)
		{
			_Name = name;
		}

		public String getName()
		{
			return _Name;
		}

		public void setMapClassName(String className)
		{
			_MapClassName = className;
		}

		public String getMapClassName()
		{
			return _MapClassName;
		}

		public void setMapLocation(String location)
		{
			_MapLocation = location;
			loadMapFileList();
		}

		public String getMapLocation()
		{
			return _MapLocation;
		}

		public Map getMap(String mapName)
		{
			Map m = _MapCache.get(mapName);
			if (m == null)
			{
				m = loadMap(mapName);
				if (m != null)
				{
					_MapCache.put(mapName, m);
				}
			}
			return m;
		}

		private void loadMapFileList()
		{
			try
			{
				_MapFileList.addAll(FileUtil.listDataResources(_MapLocation, ".map"));
			}
			catch (Exception ex)
			{
				System.out.println("Failed to load map file list");
				System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
			}
		}

		private Map loadMap(String mapName)
		{
			Map map = null;
			try
			{
				map = InstantiateMap();
				// tell the board to load!
				map.load(mapName);
			} 
			catch (Exception ex)
			{
				System.err.println("error opening file to load map!" + mapName);
				System.err.println(ex);
			}
			return map;
		}

		public ArrayList<String> getAvailableMaps()
		{
			return _MapFileList;
		}

		private Map InstantiateMap() throws Exception
		{
			return (Map) instantiateClass(_MapClassName, "bt.mapping.Map", null, null);
		}

		public void setBoardClassName(String className)
		{
			_BoardClassName = className;
		}

		public String getBoardClassName()
		{
			return _BoardClassName;
		}

		public void setBoardLocation(String location)
		{
			_BoardLocation = location;
			System.out.println("Loading Board files from : " + _BoardLocation);
			loadBoardFileList();
		}

		public void setBoardRendererClassName(String className)
		{
			_BoardRendererClassName = className;
		}

		public String getBoardRendererClassName()
		{
			return _BoardRendererClassName;
		}

		public String getBoardLocation()
		{
			return _BoardLocation;
		}

		public Board getBoard(String BoardName)
		{
			Board m = _BoardCache.get(BoardName);
			if (m == null)
			{
				m = loadBoard(BoardName);
				if (m != null)
				{
					_BoardCache.put(BoardName, m);
				}
			}
			return m;
		}

		private void loadBoardFileList()
		{
			try
			{
				_BoardFileList.addAll(FileUtil.listDataResources(_BoardLocation, ".board"));
			}
			catch (Exception ex)
			{
				System.out.println("Failed to load Board list");
				System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
			}
		}

		private Board loadBoard(String boardName)
		{
			Board board = null;
			try
			{
				// Construct the appropriate Board based class
				board = instantiateBoard();
				// tell the board to load!
				board.load(boardName);
			} catch (Exception ex)
			{
				System.err.println("error opening file to load Board!" + boardName);
				System.err.println(ex);
			}
			return board;
		}

		public ArrayList<String> getAvailableBoards()
		{
			return _BoardFileList;
		}

		private Board instantiateBoard() throws Exception
		{
			return (Board) instantiateClass(_BoardClassName, "bt.mapping.Board", null, null);
		}

		private BoardRenderer instantiateBoardRenderer() throws Exception
		{
			return (BoardRenderer) instantiateClass(_BoardRendererClassName, "bt.ui.renderers.BoardRenderer", null, null);
		}

	}

}
