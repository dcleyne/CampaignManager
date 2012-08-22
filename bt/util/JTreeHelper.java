/**
 * Created on 28/02/2007
 * <p>Title: Legatus</p>
 *
 * <p>Copyright: Copyright Daniel Cleyne (c) 2004-2006</p>
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
package bt.util;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class JTreeHelper
{
	
	private JTreeHelper()
	{		
	}

    // Finds the path in tree as specified by the node array. The node array is a sequence
    // of nodes where nodes[0] is the root and nodes[i] is a child of nodes[i-1].
    // Comparison is done using Object.equals(). Returns null if not found.
    public static Vector<TreePath> find(JTree tree, Vector<Object> nodes) 
    {
    	Vector<TreePath> paths = new Vector<TreePath>();
    	
        TreeNode root = (TreeNode)tree.getModel().getRoot();
        for (int i = 0; i < nodes.size(); i++)
        {
        	TreePath path = find2(tree, new TreePath(root), nodes.elementAt(i));
        	if (path != null)
        		paths.add(path);
        }
        return paths; 
    }
    
    private static TreePath find2(JTree tree, TreePath parent, Object object) 
    {
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node instanceof DefaultMutableTreeNode)
        {
	        DefaultMutableTreeNode dmNode = (DefaultMutableTreeNode)node;
	        
	        // If equal, return.
	        if (dmNode.getUserObject().equals(object))
	            return parent;
        }

        // Traverse children
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                TreePath result = find2(tree, path, object);
                // Found a match
                if (result != null) {
                    return result;
                }
            }
        }
    
        // No match at this branch
        return null;
    }	
}
