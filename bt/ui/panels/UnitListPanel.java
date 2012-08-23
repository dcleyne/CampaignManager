package bt.ui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bt.elements.unit.Unit;
import bt.ui.listeners.UnitChangeListener;
import bt.ui.models.TableSorter;
import bt.ui.models.UnitTableModel;

public class UnitListPanel extends JPanel implements ListSelectionListener, MouseListener
{
	private static final long serialVersionUID = 1;
    private static Log log = LogFactory.getLog(UnitListPanel.class);

    protected JTable m_UnitTable = new JTable();
    protected JScrollPane m_ScrollPane = new JScrollPane();
    protected UnitTableModel m_Model = null;
    protected TableSorter m_Sorter = new TableSorter();
    private Vector<Unit> m_Units;
    
    private Vector<UnitChangeListener> _EditRequestListeners = new Vector<UnitChangeListener>(); 

    public UnitListPanel(Vector<Unit> units)
    {
    	m_Units = units;
    	m_Model = new UnitTableModel(units);
        m_Sorter.setTableModel(m_Model);
        m_UnitTable.setModel(m_Sorter);
        m_Sorter.setTableHeader(m_UnitTable.getTableHeader());
        m_UnitTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        m_UnitTable.setPreferredSize(new Dimension(1200, 800));
        m_ScrollPane.setViewportView(m_UnitTable);
        m_UnitTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_UnitTable.getSelectionModel().addListSelectionListener(this);
        m_UnitTable.addMouseListener(this);

        setLayout(new BorderLayout());
        add(m_ScrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
    
    public void addUnitChangeListener(UnitChangeListener l)
    {
    	if (!_EditRequestListeners.contains(l))
    		_EditRequestListeners.add(l);
    }
    
    public void removeUnitChangeListener(UnitChangeListener l)
    {
    	if (_EditRequestListeners.contains(l))
    		_EditRequestListeners.remove(l);
    }
    
    private void requestUnitEdit(Unit u)
    {
    	for (UnitChangeListener l : _EditRequestListeners)
    		l.requestUnitEdit(u.getName());
    }

    public Unit GetSelectedUnit()
    {
        int Row = m_UnitTable.getSelectedRow();
        if (Row < 0)
        {
            return null;
        }

        int Index = m_Sorter.modelIndex(Row);
        return m_Units.elementAt(Index);
    }

    public void valueChanged(ListSelectionEvent lse)
    {
    }

    public void mouseClicked(MouseEvent me)
    {
        if (me.getClickCount() > 1)
        {
            Unit u = GetSelectedUnit();
            if (u != null)
            {
                m_UnitTable.transferFocus();
                log.debug("Requesting Edit for Unit : " + u.toString());
                requestUnitEdit(u);                
            }
        }
    }

    public void mouseEntered(MouseEvent me)
    {

    }

    public void mouseExited(MouseEvent me)
    {

    }

    public void mousePressed(MouseEvent me)
    {

    }

    public void mouseReleased(MouseEvent me)
    {
    }

}
