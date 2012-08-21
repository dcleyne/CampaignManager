package bt.common.ui.models;

import java.util.Date;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import bt.common.elements.unit.Unit;
import bt.common.elements.unit.UnitSummary;
import bt.server.managers.UnitManager;
import bt.server.managers.listeners.UnitManagerListener;

public class UnitTableModel extends AbstractTableModel implements UnitManagerListener
{
	private static final long serialVersionUID = 1;
    private String ColumnNames[] =
        {"Name", "Player", "Establish Date", "Notes"};
    private Class<?> ColumnTypes[] =
        {String.class, String.class, Date.class, String.class};
    
    private Vector<UnitSummary> m_Units;

    public UnitTableModel(Vector<UnitSummary> units)
    {
    	m_Units = units;
    }

    public String getColumnName(int col)
    {
        return ColumnNames[col];
    }

    public int getRowCount()
    {
    	return m_Units.size();
    }

    public int getColumnCount()
    {return ColumnNames.length;
    }

    public Object getValueAt(int row, int col)
    {
        UnitSummary u = m_Units.get(row);
        switch (col)
        {
            case 0:
                return u.getUnitName();
            case 1:
                return u.getUnitPlayer();
            case 2:
                return u.getEstablishDate();
            case 3:
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
        int Index = m_Units.indexOf(u);
        this.fireTableRowsDeleted(Index, Index);
        this.fireTableDataChanged();
    }

    public void UnitChanged(Unit u)
    {
        int Index = m_Units.indexOf(u);
        this.fireTableRowsUpdated(Index, Index);
        this.fireTableDataChanged();
    }

    public void UnitEditRequested(Unit a)
    {}
}
