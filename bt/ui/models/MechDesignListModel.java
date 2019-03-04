package bt.ui.models;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import bt.elements.unit.Unit;
import bt.managers.UnitManager;

public class MechDesignListModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
    private String ColumnNames[] =
        {"Name", "Availability", "Role"};
    private Class<?> ColumnTypes[] =
        {String.class, Integer.class, String.class};
    
    private ArrayList<Unit> _Units;

    public MechDesignListModel(ArrayList<Unit> units)
    {
    	_Units = units;
    }

    public String getColumnName(int col)
    {
        return ColumnNames[col];
    }

    public int getRowCount()
    {
    	return _Units.size();
    }

    public int getColumnCount()
    {return ColumnNames.length;
    }

    public Object getValueAt(int row, int col)
    {
        Unit u = _Units.get(row);
        switch (col)
        {
            case 0:
                return u.getName();
            case 1:
                return u.getEstablishDate();
            case 2:
                return u.getNotes();
            default:
                return "Unknown Column";
        }
    }

    public boolean isCellEditable(int row, int col)
    {return false;
    }

    public Class<?> getColumnClass(int c)
    {
        return ColumnTypes[c];
    }

    public void UnitAdded(Unit a)
    {
        this.fireTableRowsInserted(UnitManager.getInstance().getUnitNames().size(), UnitManager.getInstance().getUnitNames().size());
        this.fireTableDataChanged();
    }

    public void UnitRemoved(Unit u)
    {
        int Index = _Units.indexOf(u);
        this.fireTableRowsDeleted(Index, Index);
        this.fireTableDataChanged();
    }

    public void UnitChanged(Unit u)
    {
        int Index = _Units.indexOf(u);
        this.fireTableRowsUpdated(Index, Index);
        this.fireTableDataChanged();
    }

    public void UnitEditRequested(Unit a)
    {}
}
