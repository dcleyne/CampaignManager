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

import bt.elements.unit.PlayerSummary;
import bt.ui.listeners.PlayerChangeListener;
import bt.ui.models.PlayerTableModel;
import bt.ui.models.TableSorter;

public class PlayerListPanel extends JPanel implements ListSelectionListener, MouseListener
{
	private static final long serialVersionUID = 1;
    private static Log log = LogFactory.getLog(PlayerListPanel.class);

    protected JTable m_PlayerTable = new JTable();
    protected JScrollPane m_ScrollPane = new JScrollPane();
    protected PlayerTableModel m_Model = null;
    protected TableSorter m_Sorter = new TableSorter();
    private Vector<PlayerSummary> m_Players;
    
    private Vector<PlayerChangeListener> _EditRequestListeners = new Vector<PlayerChangeListener>(); 

    public PlayerListPanel(Vector<PlayerSummary> Players)
    {
    	m_Players = Players;
    	m_Model = new PlayerTableModel(Players);
        m_Sorter.setTableModel(m_Model);
        m_PlayerTable.setModel(m_Sorter);
        m_Sorter.setTableHeader(m_PlayerTable.getTableHeader());
        m_PlayerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        m_PlayerTable.setPreferredSize(new Dimension(1200, 800));
        m_ScrollPane.setViewportView(m_PlayerTable);
        m_PlayerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_PlayerTable.getSelectionModel().addListSelectionListener(this);
        m_PlayerTable.addMouseListener(this);

        setLayout(new BorderLayout());
        add(m_ScrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
    
    public void addPlayerChangeListener(PlayerChangeListener l)
    {
    	if (!_EditRequestListeners.contains(l))
    		_EditRequestListeners.add(l);
    }
    
    public void removePlayerChangeListener(PlayerChangeListener l)
    {
    	if (_EditRequestListeners.contains(l))
    		_EditRequestListeners.remove(l);
    }
    
    public void requestPlayerEdit(String PlayerName)
    {
    	for (PlayerChangeListener l : _EditRequestListeners)
    		l.requestPlayerEdit(PlayerName);
    }

    public PlayerSummary GetSelectedPlayer()
    {
        int Row = m_PlayerTable.getSelectedRow();
        if (Row < 0)
        {
            return null;
        }

        int Index = m_Sorter.modelIndex(Row);
        return m_Players.elementAt(Index);
    }

    public void valueChanged(ListSelectionEvent lse)
    {
    }

    public void mouseClicked(MouseEvent me)
    {
        if (me.getClickCount() > 1)
        {
            PlayerSummary ps = GetSelectedPlayer();
            if (ps != null)
            {
                m_PlayerTable.transferFocus();
                log.debug("Requesting Edit for Player : " + ps.toString());
                requestPlayerEdit(ps.getName());                
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
