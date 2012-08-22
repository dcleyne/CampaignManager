package bt.ui.models;

import java.util.Vector;


import javax.swing.table.AbstractTableModel;

import bt.elements.unit.Player;
import bt.elements.unit.PlayerSummary;
import bt.managers.PlayerManager;
import bt.managers.listeners.PlayerCacheListener;

public class PlayerTableModel extends AbstractTableModel implements PlayerCacheListener
{
	private static final long serialVersionUID = 1;
    private String ColumnNames[] =
        {"Name", "Email Address"};
    private Class<?> ColumnTypes[] =
        {String.class, String.class};
    
    private Vector<PlayerSummary> m_Players;

    public PlayerTableModel(Vector<PlayerSummary> players)
    {
    	m_Players = players;
    }

    public String getColumnName(int col)
    {
        return ColumnNames[col];
    }

    public int getRowCount()
    {
    	return m_Players.size();
    }

    public int getColumnCount()
    {return ColumnNames.length;
    }

    public Object getValueAt(int row, int col)
    {
        PlayerSummary ps = m_Players.get(row);
        switch (col)
        {
            case 0:
                return ps.getName();
            case 1:
                return ps.getEmailAddress();
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

    public void PlayerAdded(Player p)
    {
        this.fireTableRowsInserted(PlayerManager.getInstance().getPlayerSummaries().size(), PlayerManager.getInstance().getPlayerSummaries().size());
        this.fireTableDataChanged();
    }

    public void PlayerRemoved(Player p)
    {
        int Index = m_Players.indexOf(new PlayerSummary(p));
        this.fireTableRowsDeleted(Index, Index);
        this.fireTableDataChanged();
    }

    public void PlayerChanged(Player p)
    {
        int Index = m_Players.indexOf(new PlayerSummary(p));
        this.fireTableRowsUpdated(Index, Index);
        this.fireTableDataChanged();
    }

    public void PlayerEditRequested(Player p)
    {}
}
