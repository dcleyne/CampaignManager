package bt.ui.panels;

import javax.swing.JPanel;



import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import bt.elements.Battlemech;
import bt.elements.InternalSlotStatus;
import bt.elements.ItemMount;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Vector;

public class CriticalHitSingleSectionPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private Dimension _Size;
	private TitledBorder _Border;
	private CriticalHitSectionPanel _TopTablePanel = null;
	/**
	 * This method initializes 
	 * 
	 */
	public CriticalHitSingleSectionPanel() {
		super();
		initialize();
	}
	
	public void setInternalStatuses(Battlemech mech, String locationName)
	{
		Vector<ItemMount> mounts = mech.getAllMountsForInternalLocation(locationName);
		Vector<ItemMount> topTable = new Vector<ItemMount>();
		
		for (ItemMount mount : mounts)
		{
			for (InternalSlotStatus iss : mount.getSlotReferences())
			{
				if (iss.getInternalLocation().equalsIgnoreCase(locationName))
				{
					if (iss.getTable() == 1)
						if (!topTable.contains(mount))
							topTable.add(mount);
				}
			}
		}
		_TopTablePanel.setInternalStatuses(locationName,1,topTable);
	}
	
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() 
	{
		this._Size = new Dimension(220,180); 
        this.setLayout(null);
        _Border = BorderFactory.createTitledBorder(null, "Location Name", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION);
        this.setBorder(_Border);
		this.setBackground(Color.white);

		this.setSize(new Dimension(220, 161));
		this.add(get_TopTablePanel(), null);
	}

	/**
	 * This method initializes _TopTablePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private CriticalHitSectionPanel get_TopTablePanel()
	{
		if (_TopTablePanel == null)
		{
			_TopTablePanel = new CriticalHitSectionPanel();
			_TopTablePanel.setBounds(new Rectangle(6, 22, 208, 129));
		}
		return _TopTablePanel;
	}
	
	public Dimension getPreferredSize()
	{
		return _Size;
	}
	public Dimension getMinimumSize()
	{
		return _Size;
	}
	public Dimension getMaximumSize()
	{
		return _Size;
	}
	
	public void setTitle(String title)
	{
		_Border.setTitle(title);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
