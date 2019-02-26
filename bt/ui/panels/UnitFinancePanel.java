package bt.ui.panels;

import java.text.NumberFormat;


import javax.swing.*;

import bt.elements.unit.Unit;
import bt.util.SwingHelper;
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
public class UnitFinancePanel extends JPanel implements ClosableEditPanel
{
	private static final long serialVersionUID = 1;
    protected Unit _Unit;
    
	private JFormattedTextField _WarchestPointBalanceTextField;


    public UnitFinancePanel(Unit u)
    {
    	System.out.println("UnitFinancePanel constructor called");
        _Unit = u;
        
		_WarchestPointBalanceTextField = new JFormattedTextField(NumberFormat.getCurrencyInstance());
		
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(SwingHelper.GetTextField(_WarchestPointBalanceTextField, "Warchest Point Balance", "The current warchest balance of the unit", true));

		
		_WarchestPointBalanceTextField.setValue(_Unit.getWarchestPoints());
    }

    public boolean isClosable()
    {
        return true;
    }

    public void forceEditCompletion()
    {
    }

}
