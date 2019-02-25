package bt.ui.panels;

import javax.swing.*;
import bt.elements.Asset;
import bt.elements.Battlemech;
import bt.elements.ElementType;
import bt.elements.personnel.Mechwarrior;
import bt.elements.unit.Unit;
import bt.managers.RandomNameManager;
import bt.managers.UnitManager;
import bt.ui.dialogs.AddElementsDialog;
import bt.ui.dialogs.BattlemechStatusDialog;
import bt.ui.models.AssetTableModel;
import bt.ui.models.TableSorter;
import bt.util.SwingHelper;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

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
public class UnitAssetPanel extends JPanel implements ClosableEditPanel, MouseListener, ActionListener
{
	private static final long serialVersionUID = 1;
	
    protected Unit _Unit;
    protected JTable _AssetTable = new JTable();
    protected JScrollPane _ScrollPane;
    protected AssetTableModel _Model;
    protected TableSorter _Sorter = new TableSorter();
    protected JPanel _EditPanel = new JPanel();
    protected JPanel _ButtonPanel = new JPanel();

    protected JTextField _IdentifierTextField = new JTextField();
    protected JTextField _ElementTypeTextField = new JTextField();
    protected JTextField _ElementNameTextField = new JTextField();
    protected JTextField _ModelInformationTextField = new JTextField();
    protected JTextField _StatusTextField = new JTextField();
    protected JTextArea _NotesTextArea = new JTextArea();

    protected JButton _AddAssetButton = new JButton("Add");
    protected JButton _RemoveAssetButton = new JButton("Remove");

    protected Asset _CurrentAsset;
    protected Asset _PreviousAsset;

    protected AddElementsDialog _AddElementsDialog = null;

    public UnitAssetPanel(Unit u)
    {
    	System.out.println("Unit Asset Panel Constructor called");
    	
        _Unit = u;
        _Model = new AssetTableModel(_Unit);
        _Sorter.setTableModel(_Model);
        _AssetTable.setModel(_Sorter);
        _Sorter.setTableHeader(_AssetTable.getTableHeader());
        _AssetTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        _AssetTable.setPreferredSize(new Dimension(1000, 600));
        _AssetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _AssetTable.addMouseListener(this);
        _ScrollPane = new JScrollPane(_AssetTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        _EditPanel.setBorder(BorderFactory.createEtchedBorder());
        _EditPanel.setLayout(new BoxLayout(_EditPanel, BoxLayout.Y_AXIS));
        _EditPanel.add(SwingHelper.GetTextField(_IdentifierTextField, "Identifier", "Identification of the Asset",true));
        _EditPanel.add(SwingHelper.GetTextFieldWithAction(_ElementNameTextField, "Name", "The name of the Element this asset is",true,"RandomName","Generate a Random Name",this));
        _EditPanel.add(SwingHelper.GetTextField(_ElementTypeTextField, "Type", "The type of Element this asset is",true));
        _EditPanel.add(SwingHelper.GetTextField(_ModelInformationTextField, "Model", "The Model that this asset is",true));
        _EditPanel.add(SwingHelper.GetTextFieldWithAction(_StatusTextField, "Status", "The status of this Asset",true,"AssetCondition","Show the Condition of this Asset",this));
        _EditPanel.add(SwingHelper.GetTextArea(_NotesTextArea, "Notes", "Notes for this Asset",true));

        _ElementTypeTextField.setEnabled(false);
        _StatusTextField.setEnabled(false);
        _ModelInformationTextField.setEnabled(false);

        _AddAssetButton.setActionCommand("AddAsset");
        _AddAssetButton.addActionListener(this);
        _RemoveAssetButton.setActionCommand("RemoveAsset");
        _RemoveAssetButton.addActionListener(this);

        _ButtonPanel.setLayout(new BorderLayout());
        _ButtonPanel.add(_AddAssetButton,BorderLayout.WEST);
        _ButtonPanel.add(_RemoveAssetButton,BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(_EditPanel,BorderLayout.NORTH);
        add(_ScrollPane, BorderLayout.CENTER);
        add(_ButtonPanel,BorderLayout.SOUTH);

        FillCombos();

        _CurrentAsset = null;
        _PreviousAsset = null;

        setVisible(true);

    }


    public boolean isClosable()
    {
        return true;
    }

    public void forceEditCompletion()
    {
    }
    
    public void mouseClicked(MouseEvent me)
    {
        int Index = _AssetTable.getSelectedRow();
        _CurrentAsset = _Unit.getAsset(Index);
        if (_CurrentAsset != _PreviousAsset)
        {
            GetFields();
            _PreviousAsset = _CurrentAsset;
            SetFields();
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

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if (command.equals("AddAsset"))
        {
            AddNewAsset();
        }
        if (command.equals("RemoveAsset"))
        {

        }
        if (command.equals("RandomName"))
        {
            SetRandomName();
        }
        if (command.equals("AssetCondition"))
        {
        	ShowAssetCondition();
        }
    }

    protected void AddNewAsset()
    {
        if (_AddElementsDialog == null)
        {
            _AddElementsDialog = new AddElementsDialog(this,_Unit);
        }
        if (!_AddElementsDialog.isVisible())
            _AddElementsDialog.setVisible(true);

        _AddElementsDialog.requestFocus();
    }

    protected void SetFields()
    {
        if (_CurrentAsset != null)
        {
            _IdentifierTextField.setText(_CurrentAsset.getIdentifier());
            _ElementTypeTextField.setText(_CurrentAsset.getElementType().toString());
            _ElementNameTextField.setText(_CurrentAsset.getName());
            _ModelInformationTextField.setText(_CurrentAsset.getModelInformation());
            _StatusTextField.setText(_CurrentAsset.getStatus().toString());
            _NotesTextArea.setText(_CurrentAsset.getNotes());
        }
        else
        {
            _IdentifierTextField.setText("");
            _ElementTypeTextField.setText("");
            _ElementNameTextField.setText("");
            _ModelInformationTextField.setText("");
            _StatusTextField.setText("");
            _NotesTextArea.setText("");
        }
    }

    protected void GetFields()
    {
        if (_PreviousAsset == null) return;

        _PreviousAsset.setIdentifier(_IdentifierTextField.getText());
        _PreviousAsset.setName(_ElementNameTextField.getText());
        _PreviousAsset.setNotes(_NotesTextArea.getText());
    }


    protected void SetRandomName()
    {
        if (_CurrentAsset == null)
        {
            JOptionPane.showMessageDialog(this,"Select an Asset record or add a new Record!","Setting Random Name",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        _ElementNameTextField.setText(RandomNameManager.getInstance().getRandomName().toString());
    }

    protected void ShowAssetCondition()
    {
        if (_CurrentAsset == null)
        {
            JOptionPane.showMessageDialog(this,"Select an Asset record or add a new Record!","Show Asset Condition",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (_CurrentAsset.getElementType() == ElementType.BATTLEMECH)
        {
        	Battlemech mech = (Battlemech)_CurrentAsset;
        	Mechwarrior warrior = _Unit.getMechwarriorAssignedToMech(mech.getIdentifier());
        	BattlemechStatusDialog dialog = new BattlemechStatusDialog(JOptionPane.getFrameForComponent(this), mech, warrior);
        	dialog.setModal(true);
        	dialog.setVisible(true);
        	
        	if (dialog.wasDamageApplied())
        	{
        		try 
        		{
					UnitManager.getInstance().saveUnit(_Unit);
				} 
        		catch (Exception e) 
				{
					e.printStackTrace();
				}
        	}
        }
        else
        {
            JOptionPane.showMessageDialog(this,"Not Implemented Yet!","Show Asset Condition",JOptionPane.INFORMATION_MESSAGE);        	
        }
    }

    protected void FillCombos()
    {
    }

}
