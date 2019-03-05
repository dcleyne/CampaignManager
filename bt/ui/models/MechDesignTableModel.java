package bt.ui.models;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import bt.elements.collection.MiniatureCollection;
import bt.elements.design.BattlemechDesign;
import bt.elements.unit.MechAvailability;
import bt.managers.DesignManager;
import bt.managers.MiniatureCollectionManager;
import bt.managers.UnitManager;

public class MechDesignTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
    private String ColumnNames[] =
        {"Name", "Availability", "Role", "Weight", "BV"};
    private Class<?> ColumnTypes[] =
        {String.class, Integer.class, String.class, Integer.class, Integer.class};
    
    
    private UnitManager _UnitManager = UnitManager.getInstance();
    private DesignManager _DesignManager = DesignManager.getInstance();
    private MiniatureCollectionManager _MiniatureCollectionManager = MiniatureCollectionManager.getInstance();
    private ArrayList<Design> _Designs = new ArrayList<Design>();
    private MiniatureCollection _Collection;
    private String _Era;
    private String _Faction;
    private ArrayList<MechAvailability> _MechAvailability;

    public MechDesignTableModel()
    {
    }
    
    public void setEraAndFaction(String eraName, String factionName)
    {
    	if (_Era == null || !_Era.equals(eraName) || _Faction == null || !_Faction.equals(factionName))
    	{
    		_Era = eraName;
    		_Faction = factionName;
    		buildDesignList();
    	}
    }
    
    public void setCollection(String collectionName)
    {
    	if (_Collection == null || !_Collection.getName().equals(collectionName))
    	{
        	_Collection = new MiniatureCollection(_MiniatureCollectionManager.getMiniatureCollection(collectionName));
        	_Collection.resetCollection();
    		
    		buildDesignList();
    	}
    }

    public String getColumnName(int col)
    {
        return ColumnNames[col];
    }

    public int getRowCount()
    {
    	return _Designs.size();
    }

    public int getColumnCount()
    {return ColumnNames.length;
    }

    public Object getValueAt(int row, int col)
    {
        Design d = _Designs.get(row);
        switch (col)
        {
            case 0:
                return d._VariantName;
            case 1:
                return d._Availability;
            case 2:
                return d._Role;
            case 3:
                return d._Weight;
            case 4:
                return d._BV;
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
    
    private void buildDesignList()
    {
    	int size = _Designs.size();
    	if (size > 0)
    	{
	    	_Designs.clear();
	    	fireTableRowsDeleted(0, size - 1);
	        fireTableDataChanged();
    	}
        
    	if (_Era == null || _Era.isEmpty())
    		return;
    	
    	if (_Faction == null || _Faction.isEmpty())
    		return;
    	
    	if (_Collection == null)
    		return;
        
        _MechAvailability = _UnitManager.getMechAvailability(_Era,_Faction);
        for (MechAvailability ma : _MechAvailability)
        {
        	if (_Collection.isItemAvailable(ma.getName()))
        	{
        		BattlemechDesign d = _DesignManager.Design(ma.getVariantName());
        		_Designs.add(new Design(ma.getName(), ma.getVariantName(), ma.getAvailability(), d.getRole(), d.getWeight(), d.getBV()));
        	}
        }

        fireTableRowsInserted(0, _Designs.size() - 1);
        fireTableDataChanged();
    }


    public String consumeDesign(int selectedIndex)
    {
    	Design d = _Designs.get(selectedIndex);
    	_Collection.moveToPending(d._Name);
    	if (!_Collection.isItemAvailable(d._Name))
    	{
    		// Last one consumed
    		removeAllItemsWithName(d._Name);
    	}
    	return d._VariantName;
    }
    
    public void releaseDesign(String designName)
    {
    	_Collection.removeFromPending(designName);
    	buildDesignList();
    }
    
    private void removeAllItemsWithName(String name)
    {
    	boolean tableChanged = false;
    	for (int i = _Designs.size() - 1; i>= 0; i--)
    	{
    		Design d = _Designs.get(i);
    		if (d._Name.equals(name))
    		{
    			_Designs.remove(i);
    	        fireTableRowsDeleted(i, i);
    	        tableChanged = true;
    		}
    	}
    	if (tableChanged)
	        this.fireTableDataChanged();
    }

    public void UnitAdded(Design a)
    {
        this.fireTableRowsInserted(UnitManager.getInstance().getUnitNames().size(), UnitManager.getInstance().getUnitNames().size());
        this.fireTableDataChanged();
    }

    public void UnitRemoved(Design u)
    {
        int Index = _Designs.indexOf(u);
        this.fireTableRowsDeleted(Index, Index);
        this.fireTableDataChanged();
    }

    public void UnitChanged(Design u)
    {
        int Index = _Designs.indexOf(u);
        this.fireTableRowsUpdated(Index, Index);
        this.fireTableDataChanged();
    }

    private class Design
    {
    	String _Name;
    	String _VariantName;
    	int _Availability;
    	String _Role;
    	int _Weight;
    	int _BV;
    	
    	public Design(String name, String variantName, int avail, String role, int weight, int bv)
    	{
    		_Name = name;
    		_VariantName = variantName;
    		_Availability = avail;
    		_Role = role;
    		_Weight = weight;
    		_BV = bv;
    	}
    }
}
