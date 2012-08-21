package bt.client.ui.forms;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import bt.common.elements.Element;
import bt.common.elements.ElementType;
import bt.common.elements.listeners.ElementListener;
import bt.common.elements.unit.Unit;
import bt.common.util.SwingHelper;

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
public class AddElementsDialog extends JDialog implements ChangeListener, ElementListener, ActionListener
{
	private static final long serialVersionUID = 1;

	protected Unit m_Unit;
    protected JTabbedPane m_Tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
    protected JPanel m_EditPanel = new JPanel();
    protected JTextField m_ElementTypeTextField = new JTextField();
    protected JTextField m_ElementNameTextField = new JTextField();
    protected JTextField m_QuantityTextField = new JTextField();

    protected JPanel m_ButtonPanel = new JPanel();
    protected JButton m_AddAssetsButton = new JButton("Add Assets To Unit");
    protected JButton m_CloseButton = new JButton("Close");

    protected Element m_SelectedElement = null;

    public AddElementsDialog(Component Owner, Unit u)
    {
        super(JOptionPane.getFrameForComponent(Owner), "Add Assets to Unit",false);
        this.setResizable(false);
        m_Unit = u;

        getContentPane().setLayout(new BorderLayout());

        for (ElementType et : ElementType.values())
        {
            ElementListPanel elp = new ElementListPanel(et);
            m_Tabs.add(et.toString(),elp);
        }

        m_Tabs.addChangeListener(this);

        m_EditPanel.setBorder(BorderFactory.createEtchedBorder());
        m_EditPanel.setLayout(new BoxLayout(m_EditPanel, BoxLayout.X_AXIS));
        m_EditPanel.add(SwingHelper.GetTextField(m_ElementTypeTextField, "Type", "The type of Element this asset is",true));
        m_EditPanel.add(SwingHelper.GetTextField(m_ElementNameTextField, "Name", "The name of the Element this asset is",true));
        m_EditPanel.add(SwingHelper.GetTextField(m_QuantityTextField, "Quantity", "Specify how many of these assets you want to add",true));
        m_ElementTypeTextField.setEnabled(false);
        m_ElementNameTextField.setEnabled(false);

        m_AddAssetsButton.setActionCommand("AddAssets");
        m_AddAssetsButton.addActionListener(this);
        m_CloseButton.setActionCommand("Close");
        m_CloseButton.addActionListener(this);

        m_ButtonPanel.setLayout(new BorderLayout());
        m_ButtonPanel.add(m_AddAssetsButton,BorderLayout.WEST);
        m_ButtonPanel.add(m_CloseButton,BorderLayout.EAST);

        getContentPane().add(m_Tabs, BorderLayout.NORTH);
        getContentPane().add(m_EditPanel, BorderLayout.CENTER);
        getContentPane().add(m_ButtonPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(Owner);
        m_Tabs.requestFocus();

        setSize(800,600);
    }

    public void stateChanged(ChangeEvent ce)
    {
    }


    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if (command.equals("Close"))
            this.setVisible(false);
        if (command.equals("AddAssets"))
            AddAssets();
    }

    public void ElementSelected(Element e)
    {
        m_SelectedElement = e;
        m_ElementNameTextField.setText(e.GetName());
        m_ElementTypeTextField.setText(e.GetElementType());
        m_QuantityTextField.setText("1");
    }

    protected void AddAssets()
    {
        int Quantity = 0;
        try
        {
            Quantity = Integer.parseInt(m_QuantityTextField.getText());
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Quantity must be a valid Number!");
            return;
        }
        if (Quantity < 0)
        {
            JOptionPane.showMessageDialog(this,"Quantity must be a valid Number greater than 0!");
            return;
        }
        if (m_SelectedElement == null)
        {
            JOptionPane.showMessageDialog(this,"Select an Element to add to the unit!");
            return;
        }
        for (int i = 0; i < Quantity; i++)
        {
        	//TODO Fix this!
            //m_Unit.AddNewAsset(ElementManager.GetElementTypeIDFromElement(m_SelectedElement),m_SelectedElement.GetID(),ElementManager.GetRandomIdentifier());
        }
    }

}
