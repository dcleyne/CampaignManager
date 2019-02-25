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
public class AssetTableModel extends AbstractTableModel implements UnitListener
{
	private static final long serialVersionUID = 1;
	
    private String ColumnNames[] =
        {"ID", "Type", "Name", "Model","Status","Notes"};
    private Class<?> ColumnTypes[] =
        {Long.class,String.class,String.class,Long.class,String.class,String.class};
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
    	return m_Unit.getAssetCount();
    }

    public int getColumnCount()
    {return ColumnNames.length;
    }

    public Object getValueAt(int row, int col)
    {
        Asset a = m_Unit.getAsset(row);
        switch (col)
        {
            case 0:
                return a.getIdentifier();
            case 1:
                return a.getElementType().toString();
            case 2:
                return a.getName();
            case 3:
                return a.getModelInformation();
            case 4:
                return a.getStatus();
            case 5:
                return a.getNotes();

            default:
                return "Unknown Column";
        }
        
    }

    public boolean isCellEditable(int row, int col)
    {
    	return false;
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
        this.fireTableRowsInserted(m_Unit.getAssetCount(), m_Unit.getAssetCount());
        this.fireTableDataChanged();
    }

    public void AssetRemoved(Asset a)
    {
        int Index = m_Unit.getAssetIndex(a);
        this.fireTableRowsDeleted(Index, Index);
        this.fireTableDataChanged();
    }

    public void AssetChanged(Asset a)
    {
        int Index = m_Unit.getAssetIndex(a);
        this.fireTableRowsUpdated(Index, Index);
        this.fireTableDataChanged();
    }


}
