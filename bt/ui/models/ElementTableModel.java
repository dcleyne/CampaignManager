package bt.ui.models;

import javax.swing.table.AbstractTableModel;

import bt.elements.Element;
import bt.elements.ElementType;
import bt.managers.ElementManager;

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
public class ElementTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	
    private String ColumnNames[] =
        {"Name", "Cost", "Support Requirement","Crew Compliment"};
    private Class<?> ColumnTypes[] =
        {String.class,Double.class,Double.class,Double.class};
    protected ElementType m_ElementType;

    public ElementTableModel(ElementType et)
    {
        m_ElementType = et;

    }
    public String getColumnName(int col)
    {
        return ColumnNames[col];
    }

    public int getRowCount()
    {
        return ElementManager.GetElementCount(m_ElementType);
    }

    public int getColumnCount()
    {return ColumnNames.length;
    }

    public Object getValueAt(int row, int col)
    {
        Element e = ElementManager.GetElement(m_ElementType,row);
        switch (col)
        {
            case 0:
                return e.GetName();
            case 1:
                return e.GetCost();
            case 2:
                return e.GetSupportRequirement();
            case 3:
                return e.GetCrewCompliment();
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
}
