package bt.ui.panels;

import javax.swing.JPanel;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTextField;

import bt.elements.InternalSlotStatus;
import bt.elements.ItemMount;
import bt.elements.ItemStatus;

import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class CriticalHitSectionPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JLabel _Row1Label = null;
	private JLabel _Row2Label = null;
	private JLabel _Row3Label = null;
	private JLabel _Row4Label = null;
	private JLabel _Row5Label = null;
	private JLabel _Row6Label = null;
	private JTextField _Row1TextField = null;
	private JTextField _Row2TextField = null;
	private JTextField _Row3TextField = null;
	private JTextField _Row4TextField = null;
	private JTextField _Row5TextField = null;
	private JTextField _Row6TextField = null;
	
	private HashMap<Integer,JTextField> _FieldIndex = new HashMap<Integer,JTextField>();
	/**
	 * This method initializes 
	 * 
	 */
	public CriticalHitSectionPanel() {
		super();
		initialize();
		
		_FieldIndex.put(1, _Row1TextField);
		_FieldIndex.put(2, _Row2TextField);
		_FieldIndex.put(3, _Row3TextField);
		_FieldIndex.put(4, _Row4TextField);
		_FieldIndex.put(5, _Row5TextField);
		_FieldIndex.put(6, _Row6TextField);
	}
	
	public void setInternalStatuses(String locationName, int table, Vector<ItemMount> mounts)
	{
		HashMap<Integer,String> equipment = new HashMap<Integer,String>();
		HashMap<Integer,ItemStatus> statuses = new HashMap<Integer,ItemStatus>();
		
		for (ItemMount mount : mounts)
		{
			for (InternalSlotStatus iss: mount.getSlotReferences())
			{
				if (iss.getInternalLocation().equalsIgnoreCase(locationName))
				{
					if (iss.getTable() == table)
					{
						String mountText = mount.getMountedItem().toString();
						if (iss.getRearFacing())
							mountText += " (R)";
						equipment.put(iss.getSlot(), mountText);
						statuses.put(iss.getSlot(), iss.getStatus());
					}
				}
			}
		}
		for (Integer i : equipment.keySet())
		{
			fillText(_FieldIndex.get(i),equipment.get(i), statuses.get(i));
		}
		
		
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private void fillText(JTextField textField, String equipment, ItemStatus status)
	{
		switch (status)
		{
		case DESTROYED:
			Font itemFont = textField.getFont();
			Map attributes = itemFont.getAttributes();
			attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
			Font newFont = new Font(attributes);
			textField.setFont(newFont);
			textField.setText(equipment);
			break;
		case DAMAGED:
			textField.setText("[D] " + equipment);
			break;
		case REPAIRED:
			textField.setText("[R] " + equipment);
			break;
		case JURYRIGGED:
			textField.setText("[J] " + equipment);
			break;
		default:
			textField.setText(equipment);
		}
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        _Row6Label = new JLabel();
        _Row6Label.setText("6.");
        _Row6Label.setBounds(new Rectangle(4, 106, 10, 16));
        _Row5Label = new JLabel();
        _Row5Label.setText("5.");
        _Row5Label.setBounds(new Rectangle(4, 86, 10, 16));
        _Row4Label = new JLabel();
        _Row4Label.setText("4.");
        _Row4Label.setBounds(new Rectangle(4, 66, 10, 16));
        _Row3Label = new JLabel();
        _Row3Label.setText("3.");
        _Row3Label.setBounds(new Rectangle(4, 46, 10, 16));
        _Row2Label = new JLabel();
        _Row2Label.setText("2.");
        _Row2Label.setBounds(new Rectangle(4, 26, 10, 16));
        _Row1Label = new JLabel();
        _Row1Label.setText("1.");
        _Row1Label.setBounds(new Rectangle(4, 6, 10, 16));
        
        this.setLayout(null);
        this.setSize(new Dimension(197, 126));
        
		this.setBackground(Color.white);

		this.add(_Row1Label, null);
		this.add(_Row2Label, null);
		this.add(_Row3Label, null);
		this.add(_Row4Label, null);
		this.add(_Row5Label, null);
		this.add(_Row6Label, null);
		this.add(get_Row1TextField(), null);
		this.add(get_Row2TextField(), null);
		this.add(get_Row3TextField(), null);
		this.add(get_Row4TextField(), null);
		this.add(get_Row5TextField(), null);
		this.add(get_Row6TextField(), null);
	}

	/**
	 * This method initializes _Row1TextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField get_Row1TextField()
	{
		if (_Row1TextField == null)
		{
			_Row1TextField = new JTextField();
			_Row1TextField.setText("");
			_Row1TextField.setBounds(new Rectangle(24, 4, 159, 20));
			_Row1TextField.setBorder(null);
		}
		return _Row1TextField;
	}

	/**
	 * This method initializes _Row2TextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField get_Row2TextField()
	{
		if (_Row2TextField == null)
		{
			_Row2TextField = new JTextField();
			_Row2TextField.setText("");
			_Row2TextField.setBounds(new Rectangle(24, 24, 159, 20));
			_Row2TextField.setBorder(null);
		}
		return _Row2TextField;
	}

	/**
	 * This method initializes _Row3TextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField get_Row3TextField()
	{
		if (_Row3TextField == null)
		{
			_Row3TextField = new JTextField();
			_Row3TextField.setText("");
			_Row3TextField.setBounds(new Rectangle(24, 44, 159, 20));
			_Row3TextField.setBorder(null);
		}
		return _Row3TextField;
	}

	/**
	 * This method initializes _Row4TextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField get_Row4TextField()
	{
		if (_Row4TextField == null)
		{
			_Row4TextField = new JTextField();
			_Row4TextField.setText("");
			_Row4TextField.setBounds(new Rectangle(24, 64, 159, 20));
			_Row4TextField.setBorder(null);
		}
		return _Row4TextField;
	}

	/**
	 * This method initializes _Row5TextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField get_Row5TextField()
	{
		if (_Row5TextField == null)
		{
			_Row5TextField = new JTextField();
			_Row5TextField.setText("");
			_Row5TextField.setBounds(new Rectangle(24, 84, 159, 20));
			_Row5TextField.setBorder(null);
	}
		return _Row5TextField;
	}

	/**
	 * This method initializes _Row6TextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField get_Row6TextField()
	{
		if (_Row6TextField == null)
		{
			_Row6TextField = new JTextField();
			_Row6TextField.setText("");
			_Row6TextField.setBounds(new Rectangle(24, 104, 159, 20));
			_Row6TextField.setBorder(null);
}
		return _Row6TextField;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
