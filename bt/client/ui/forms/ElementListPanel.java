package bt.client.ui.forms;

import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

import bt.client.ui.models.ElementTableModel;
import bt.client.ui.models.TableSorter;
import bt.common.elements.*;
import bt.common.elements.listeners.*;

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
public class ElementListPanel extends JPanel implements ListSelectionListener
{
	private static final long serialVersionUID = 1;

    protected ElementType m_ElementType;
    protected JTable m_ElementTable = new JTable();
    protected JScrollPane m_ScrollPane = new JScrollPane();
    protected ElementTableModel m_Model;
    protected TableSorter m_Sorter = new TableSorter();

    protected Vector<ElementListener> m_Listeners = new Vector<ElementListener>();

    public ElementListPanel(ElementType et)
    {
        m_ElementType = et;
        m_Model = new ElementTableModel(et);
        m_Sorter.setTableModel(m_Model);
        m_ElementTable.setModel(m_Sorter);
        m_Sorter.setTableHeader(m_ElementTable.getTableHeader());
        m_ElementTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        m_ElementTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_ElementTable.getSelectionModel().addListSelectionListener(this);

        m_ScrollPane = new JScrollPane(m_ElementTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        setLayout(new BorderLayout());
        add(m_ScrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

     public void AddListener(ElementListener el)
     {
         if (!m_Listeners.contains(el))
             m_Listeners.add(el);
     }

     public void RemoveListener(ElementListener el)
     {
         if (m_Listeners.contains(el))
             m_Listeners.remove(el);
     }

     public void NotifyElementSelected(Element e)
     {
         for (int i = 0; i < m_Listeners.size(); i++)
         {
             ElementListener el = (ElementListener)m_Listeners.elementAt(i);
             el.ElementSelected(e);
         }
     }

     public void valueChanged(ListSelectionEvent lse)
     {
        //int Index = m_Sorter.modelIndex(m_ElementTable.getSelectedRow());
        //TODO Fix this
        //Element e = ElementManager.GetElement(m_ElementTypeID,Index);
        //NotifyElementSelected(e);
    }

}
