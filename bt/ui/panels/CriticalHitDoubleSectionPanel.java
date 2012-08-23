package bt.ui.panels;

import javax.swing.JPanel;


import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import bt.elements.Battlemech;
import bt.elements.InternalSlotStatus;
import bt.elements.ItemMount;

import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Vector;

public class CriticalHitDoubleSectionPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private Dimension _Size;
	private CriticalHitSectionPanel _TopTablePanel = null;
	private CriticalHitSectionPanel _BottomTablePanel = null;
	private TitledBorder _Border;
	/**
	 * This method initializes 
	 * 
	 */
	public CriticalHitDoubleSectionPanel() {
		super();
		initialize();
	}

	public void setInternalStatuses(Battlemech mech, String locationName)
	{
		Vector<ItemMount> mounts = mech.getAllMountsForInternalLocation(locationName);
		Vector<ItemMount> topTable = new Vector<ItemMount>();
		Vector<ItemMount> bottomTable = new Vector<ItemMount>();
		
		for (ItemMount mount : mounts)
		{
			for (InternalSlotStatus iss : mount.getSlotReferences())
			{
				if (iss.getInternalLocation().equalsIgnoreCase(locationName))
				{
					if (iss.getTable() == 1)
						if (!topTable.contains(mount))
							topTable.add(mount);
					if (iss.getTable() == 2)
						if (!bottomTable.contains(mount))
							bottomTable.add(mount);
				}
			}
		}
		_TopTablePanel.setInternalStatuses(locationName,1,topTable);
		_BottomTablePanel.setInternalStatuses(locationName,2,bottomTable);
	}


	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() 
	{
		this._Size = new Dimension(220, 322);
        this.setLayout(null);
        _Border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Location Name", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51));
        this.setBorder(_Border);
		this.setBackground(Color.white);

		this.setSize(new Dimension(220, 291));
		this.add(get_TopTablePanel(), null);
		this.add(get_BottomTablePanel(), null);
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
			_TopTablePanel.setSize(208, 128);
			_TopTablePanel.setLocation(new Point(6, 22));
		}
		return _TopTablePanel;
	}

	/**
	 * This method initializes _BottomTablePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private CriticalHitSectionPanel get_BottomTablePanel()
	{
		if (_BottomTablePanel == null)
		{
			_BottomTablePanel = new CriticalHitSectionPanel();
			_BottomTablePanel.setSize(208, 129);
			_BottomTablePanel.setLocation(new Point(6, 155));
		}
		return _BottomTablePanel;
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
