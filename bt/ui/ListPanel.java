package bt.ui;


import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;

import bt.ui.models.TableSorter;


public class ListPanel extends JPanel implements ListSelectionListener, ListDataListener
{
	private static final long serialVersionUID = 1;
	
    protected JTable m_Table = new JTable();
    protected JScrollPane m_ScrollPane = new JScrollPane();
    protected AbstractTableModel m_Model;
    protected TableSorter m_Sorter = new TableSorter();

    protected Vector<ListPanelListener> m_Listeners = new Vector<ListPanelListener>();

    public ListPanel(AbstractTableModel Model)
    {
        m_Model = Model;
        m_Sorter.setTableModel(m_Model);
        m_Table.setModel(m_Sorter);
        m_Sorter.setTableHeader(m_Table.getTableHeader());
        m_Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        m_Table.setPreferredSize(new Dimension(1200, 800));
        m_ScrollPane.setViewportView(m_Table);
        m_Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_Table.getSelectionModel().addListSelectionListener(this);

        setLayout(new BorderLayout());
        add(m_ScrollPane, BorderLayout.CENTER);
        setPreferredSize(new Dimension(240,120));
        setMaximumSize(new Dimension(Short.MAX_VALUE,Short.MAX_VALUE));

        setVisible(true);
    }

    public int GetSelectedObject()
    {
        int Row = m_Table.getSelectedRow();
        if (Row < 0)
        {
            return -1;
        }

        return m_Sorter.modelIndex(Row);
    }

    public void AddListener(ListPanelListener lpl)
    {
        if (!m_Listeners.contains(lpl))
        {
            m_Listeners.add(lpl);
        }
    }

    public void RemoveListener(ListPanelListener lpl)
    {
        if (m_Listeners.contains(lpl))
        {
            m_Listeners.remove(lpl);
        }
    }

    public void ClearListeners()
    {
        m_Listeners.clear();
    }

    protected void NotifySelection(int Index)
    {
        for (int i = 0; i < m_Listeners.size(); i++)
        {
            ListPanelListener lpl = (ListPanelListener)m_Listeners.elementAt(i);
            lpl.RowSelected(Index);
        }
    }

    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
        {
            return;
        }

        int index = -1;

        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        int minIndex = lsm.getMinSelectionIndex();
        int maxIndex = lsm.getMaxSelectionIndex();

        for (int i = minIndex; i <= maxIndex; i++)
        {
            if (lsm.isSelectedIndex(i))
            {
                index = i;
                break;
            }
        }
        if (index >= 0)
        {
            NotifySelection(m_Sorter.modelIndex(index));
        }
    }

    public void contentsChanged(ListDataEvent e)
    {
        repaint();
    }

    public void intervalAdded(ListDataEvent e)
    {
        repaint();
    }

    public void intervalRemoved(ListDataEvent e)
    {
        repaint();
    }


}
