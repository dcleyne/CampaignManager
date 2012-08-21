package bt.common.ui.models;

import java.util.Vector;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeModelListener;

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
public class UnitTreeModel implements TreeModel
{
    protected Unit m_Unit;

    protected Vector<TreeModelListener> m_listeners = new Vector<TreeModelListener>();

    public UnitTreeModel(Unit u)
    {
        m_Unit = u;
    }

    public void addTreeModelListener(TreeModelListener l)
    {
        m_listeners.add(l);
    }

    public Object getChild(Object parent, int index)
    {
    	//TODO Reimplement this
    	return null;
    }

    public int getChildCount(Object parent)
    {
    	//TODO Reimplement this
    	return 0;
    }

    public int getIndexOfChild(Object parent, Object child)
    {
    	//TODO Reimplement this
    	return 0;
    }

    public Object getRoot()
    {
    	//TODO Reimplement this
        return m_Unit;
    }

    public boolean isLeaf(Object node)
    {
    	//TODO Reimplement this
    	return true;
    }

    public void removeTreeModelListener(TreeModelListener l)
    {
        if (m_listeners.contains(l))
            m_listeners.remove(l);
    }

    public void valueForPathChanged(TreePath path, Object newValue)
    {
    }
}
