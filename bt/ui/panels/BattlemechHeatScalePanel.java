package bt.ui.panels;

import javax.swing.JPanel;
import javax.swing.JList;

import bt.elements.Battlemech;

import java.awt.BorderLayout;
import java.awt.Dimension;

public class BattlemechHeatScalePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JList _HeatScaleList = null;

	/**
	 * This method initializes 
	 * 
	 */
	public BattlemechHeatScalePanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
        this.setSize(new Dimension(256, 585));
        this.add(get_HeatScaleList(), BorderLayout.CENTER);
			
	}

	/**
	 * This method initializes _HeatScaleList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList get_HeatScaleList()
	{
		if (_HeatScaleList == null)
		{
			_HeatScaleList = new JList(Battlemech.HeatScale);
		}
		return _HeatScaleList;
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
