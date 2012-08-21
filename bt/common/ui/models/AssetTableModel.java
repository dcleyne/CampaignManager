package bt.common.ui.models;

import javax.swing.table.AbstractTableModel;

import bt.common.elements.Asset;
import bt.common.elements.listeners.UnitListener;
import bt.common.elements.personnel.Personnel;
import bt.common.elements.unit.Formation;
import bt.common.elements.unit.Unit;

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
public class AssetTableModel extends AbstractTableModel implements UnitListener
{
	private static final long serialVersionUID = 1;
	
    private String ColumnNames[] =
        {"Element Type", "Identifier", "Model Info", "Element Name", "Status","Condition","Notes"};
    private Class<?> ColumnTypes[] =
        {String.class,String.class,String.class,String.class,String.class,String.class,String.class};
    protected Unit m_Unit;

    public AssetTableModel(Unit u)
    {
        m_Unit = u;
    }

    public String getColumnName(int col)
    {
        return ColumnNames[col];
    }

    public int getRowCount()
    {
    	return m_Unit.getAssets().size();
    }

    public int getColumnCount()
    {return ColumnNames.length;
    }

    public Object getValueAt(int row, int col)
    {
        Asset a = m_Unit.getAssets().elementAt(row);
        switch (col)
        {
            case 0:
                return a.getElementType().toString();
            case 1:
                return a.getIdentifier();
            case 2:
            	return a.getModelInformation();
            case 3:
                return a.getName();
            case 4:
                return a.getStatus();
            case 5:
                return a.getCondition();
            case 6:
                return a.getNotes();

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
    public void PersonnelAdded(Personnel p){}
    public void PersonnelRemoved(Personnel p){}
    public void PersonnelChanged(Personnel p){}

    public void AssetAdded(Asset a)
    {
        this.fireTableRowsInserted(m_Unit.getAssets().size(), m_Unit.getAssets().size());
        this.fireTableDataChanged();
    }

    public void AssetRemoved(Asset a)
    {
        int Index = m_Unit.getAssets().indexOf(a);
        this.fireTableRowsDeleted(Index, Index);
        this.fireTableDataChanged();
    }

    public void AssetChanged(Asset a)
    {
        int Index = m_Unit.getAssets().indexOf(a);
        this.fireTableRowsUpdated(Index, Index);
        this.fireTableDataChanged();
    }


}
