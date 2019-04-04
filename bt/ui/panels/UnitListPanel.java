package bt.ui.panels;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import bt.elements.unit.Unit;
import bt.ui.listeners.UnitChangeListener;
import bt.ui.models.TableSorter;
import bt.ui.models.UnitTableModel;
import bt.util.SwingHelper;

public class UnitListPanel extends JPanel implements ListSelectionListener, MouseListener
{
	private static final long serialVersionUID = 1;

    protected JTable _UnitTable = new JTable();
    protected JScrollPane _ScrollPane = new JScrollPane();
    protected UnitTableModel _Model = null;
    protected TableSorter _Sorter = new TableSorter();
    private ArrayList<Unit> _Units;
    
    private ArrayList<UnitChangeListener> _EditRequestListeners = new ArrayList<UnitChangeListener>(); 

    public UnitListPanel(ArrayList<Unit> units)
    {
    	_Units = units;
    	_Model = new UnitTableModel(units);
        _Sorter.setTableModel(_Model);
        _UnitTable.setModel(_Sorter);
        _Sorter.setTableHeader(_UnitTable.getTableHeader());
        _UnitTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        _ScrollPane.setViewportView(_UnitTable);
        _UnitTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _UnitTable.getSelectionModel().addListSelectionListener(this);
        _UnitTable.addMouseListener(this);
        SwingHelper.resizeTableColumnWidth(_UnitTable);

        setLayout(new BorderLayout());
        add(_ScrollPane, BorderLayout.CENTER);

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
    
    public void requestUnitEdit(Unit u)
    {
    	for (UnitChangeListener l : _EditRequestListeners)
    		l.requestUnitEdit(u.getName());
    }

    public Unit GetSelectedUnit()
    {
        int Row = _UnitTable.getSelectedRow();
        if (Row < 0)
        {
            return null;
        }

        int Index = _Sorter.modelIndex(Row);
        return _Units.get(Index);
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
                _UnitTable.transferFocus();
                System.out.println("Requesting Edit for Unit : " + u.toString());
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
