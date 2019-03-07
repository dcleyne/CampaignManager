package bt.mapping;

import java.util.Vector;

public class MapSet
{
	private String _Name;
	private int _MapCount;
	private int _ColumnCount;
	private int _RowCount;
	private Vector<MapCell> _Cells = new Vector<MapCell>();
	
	public String getName()
	{
		return _Name;
	}

	public void setName(String name)
	{
		_Name = name;
	}

	public int getMapCount()
	{
		return _MapCount;
	}

	public void setMapCount(int mapCount)
	{
		_MapCount = mapCount;
	}

	public int getColumnCount()
	{
		return _ColumnCount;
	}

	public void setColumnCount(int columnCount)
	{
		_ColumnCount = columnCount;
	}

	public int getRowCount()
	{
		return _RowCount;
	}

	public void setRowCount(int rowCount)
	{
		_RowCount = rowCount;
	}

	public Vector<MapCell> getCells()
	{
		return _Cells;
	}
	
	public MapCell getCell(int row, int col)
	{
		for (MapCell cell : _Cells)
		{
			if (cell.getRow() == row && cell.getColumn() == col)
				return cell;
		}
		return null;
	}
	
	public String getMap(int index)
	{
		int curMap = 1;
		
		for (int row = 1; row <= _RowCount; row++)
		{
			for (int col = 1; col <= _ColumnCount; col++)
			{
				MapCell cell = getCell(row,col);
				if (cell != null)
				{
					if (curMap == index)
						return cell.getMapName();
					
					curMap++;
				}
			}
		}
		return "";
	}

	public class MapCell
	{
		private String _MapName;
		private int _Row;
		private int _Column;
		private int _MapNumber;
		
		public String getMapName()
		{
			return _MapName;
		}
		public void setMapName(String mapName)
		{
			_MapName = mapName;
		}
		public int getRow()
		{
			return _Row;
		}
		public void setRow(int row)
		{
			_Row = row;
		}
		public int getColumn()
		{
			return _Column;
		}
		public void setColumn(int column)
		{
			_Column = column;
		}
		public int getMapNumber()
		{
			return _MapNumber;
		}
		public void setMapNumber(int mapNumber)
		{
			_MapNumber = mapNumber;
		}
		
		public MapCell()
		{
			
		}
		
		public MapCell(String name, int number, int row, int col)
		{
			_MapName = name;
			_Row = row;
			_Column = col;
			_MapNumber = number;
		}
	}
}
