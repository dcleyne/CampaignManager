package bt.ui.models;

import javax.swing.table.AbstractTableModel;

import bt.elements.Asset;
import bt.elements.listeners.UnitListener;
import bt.elements.personnel.Personnel;
import bt.elements.unit.Formation;
import bt.elements.unit.Unit;


/**
 * <p>Title: Miradan Phedd</p>
 *
 * <p>Description: Library for all Miradan related work</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Miradan Phedd</p>
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
public class PersonnelTableModel extends AbstractTableModel implements UnitListener
{
	private static final long serialVersionUID = 1;
    private String ColumnNames[] =
        {"Name", "Callsign", "Rank", "HomePlanet","Rating","JobType","GroupAssignment","Notes"};
    private Class<?> ColumnTypes[] =
        {String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class};
    protected Unit m_Unit;

    public PersonnelTableModel(Unit u)
    {
        m_Unit = u;
    }

    public String getColumnName(int col)
    {
        return ColumnNames[col];
    }

    public int getRowCount()
    {return m_Unit.getPersonnel().size();}

    public int getColumnCount()
    {return ColumnNames.length;
    }

    public Object getValueAt(int row, int col)
    {
        Personnel p = m_Unit.getPersonnel().elementAt(row);
        switch (col)
        {
            case 0:
                return p.getName();
            case 1:
                return p.getCallsign();
            case 2:
                return p.getRank().toString();
            case 3:
                return "";
            case 4:
                return p.getRating().toString();
            case 5:
                return p.getJobType().toString();
            case 6:
                return "";
            case 7:
                return p.getNotes();

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

    public void UnitChanged(Unit u){}
    public void UnitGroupAdded(Formation ug){}
    public void UnitGroupChanged(Formation ug){}
    public void UnitGroupRemoved(Formation ug){}

    public void PersonnelAdded(Personnel p)
    {
        this.fireTableRowsInserted(m_Unit.getPersonnel().size(), m_Unit.getPersonnel().size());
        this.fireTableDataChanged();
    }
    public void PersonnelRemoved(Personnel p)
    {
        int Index = m_Unit.getPersonnel().indexOf(p);
        this.fireTableRowsDeleted(Index, Index);
        this.fireTableDataChanged();
    }

    public void PersonnelChanged(Personnel p)
    {
        int Index = m_Unit.getPersonnel().indexOf(p);
        this.fireTableRowsUpdated(Index, Index);
        this.fireTableDataChanged();
    }

    public void AssetAdded(Asset a){}
    public void AssetRemoved(Asset a){}
    public void AssetChanged(Asset a){}

}
