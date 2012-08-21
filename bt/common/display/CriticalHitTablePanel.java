package bt.common.display;



import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JLabel;

import bt.common.elements.Battlemech;

public class CriticalHitTablePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private CriticalHitSingleSectionPanel _HeadPanel = null;
	private CriticalHitDoubleSectionPanel _LeftArmPanel = null;
	private CriticalHitDoubleSectionPanel _RightArmPanel = null;
	private CriticalHitDoubleSectionPanel _CenterTorsoPanel = null;
	private CriticalHitDoubleSectionPanel _LeftTorsoPanel = null;
	private CriticalHitDoubleSectionPanel _RightTorsoPanel = null;
	private CriticalHitSingleSectionPanel _LeftLegPanel = null;
	private CriticalHitSingleSectionPanel _RightLegPanel = null;
	private JPanel _HitRegisterPanel = null;
	private JLabel _EngineHitsLabel = null;
	private JLabel _GyroHitsLabel = null;
	private JLabel _SensorHitsLabel = null;
	private JLabel _LifeSupportHitsLabel = null;
	
	private Dimension _Size;
	/**
	 * This method initializes 
	 * 
	 */
	public CriticalHitTablePanel() 
	{
		super();
		initialize();
	}
	
	public void setBattlemech(Battlemech mech)
	{
		_HeadPanel.setInternalStatuses(mech,"Head");
		_LeftArmPanel.setInternalStatuses(mech,"Left Arm");
		_RightArmPanel.setInternalStatuses(mech,"Right Arm");
		_CenterTorsoPanel.setInternalStatuses(mech,"Centre Torso");
		_LeftTorsoPanel.setInternalStatuses(mech,"Left Torso");
		_RightTorsoPanel.setInternalStatuses(mech,"Right Torso");
		_LeftLegPanel.setInternalStatuses(mech,"Left Leg");
		_RightLegPanel.setInternalStatuses(mech,"Right Leg");
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() 
	{
		setLayout(null);
		_Size = new Dimension(681, 872);
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), "Critical Hit Table", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
        this.setSize(new Dimension(681, 768));
		this.setBackground(Color.white);
		this.add(get_HeadPanel(), null);
		this.add(get_LeftArmPanel(), null);
		this.add(get_RightArmPanel(), null);
		this.add(get_CenterTorsoPanel(), null);
		this.add(get_LeftTorsoPanel(), null);
		this.add(get_RightTorsoPanel(), null);
		this.add(get_LeftLegPanel(), null);
		this.add(get_RightLegPanel(), null);
		this.add(get_HitRegisterPanel(), null);
	}

	/**
	 * This method initializes _HeadPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private CriticalHitSingleSectionPanel get_HeadPanel()
	{
		if (_HeadPanel == null)
		{
			_HeadPanel = new CriticalHitSingleSectionPanel();
			_HeadPanel.setTitle("Head");
			_HeadPanel.setBounds(new Rectangle(232, 16, 220, 156));
		}
		return _HeadPanel;
	}

	/**
	 * This method initializes _LeftArmPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private CriticalHitDoubleSectionPanel get_LeftArmPanel()
	{
		if (_LeftArmPanel == null)
		{
			_LeftArmPanel = new CriticalHitDoubleSectionPanel();
			_LeftArmPanel.setTitle("Left Arm");
			_LeftArmPanel.setBounds(new Rectangle(9, 16, 220, 287));
		}
		return _LeftArmPanel;
	}

	/**
	 * This method initializes _RightArmPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private CriticalHitDoubleSectionPanel get_RightArmPanel()
	{
		if (_RightArmPanel == null)
		{
			_RightArmPanel = new CriticalHitDoubleSectionPanel();
			_RightArmPanel.setTitle("Right Arm");
			_RightArmPanel.setBounds(new Rectangle(454, 16, 220, 290));
		}
		return _RightArmPanel;
	}

	/**
	 * This method initializes _CenterTorsoPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private CriticalHitDoubleSectionPanel get_CenterTorsoPanel()
	{
		if (_CenterTorsoPanel == null)
		{
			_CenterTorsoPanel = new CriticalHitDoubleSectionPanel();
			_CenterTorsoPanel.setTitle("Center Torso");
			_CenterTorsoPanel.setBounds(new Rectangle(232, 173, 220, 287));
		}
		return _CenterTorsoPanel;
	}

	/**
	 * This method initializes _LeftTorsoPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private CriticalHitDoubleSectionPanel get_LeftTorsoPanel()
	{
		if (_LeftTorsoPanel == null)
		{
			_LeftTorsoPanel = new CriticalHitDoubleSectionPanel();
			_LeftTorsoPanel.setTitle("Left Torso");
			_LeftTorsoPanel.setBounds(new Rectangle(7, 309, 220, 291));
		}
		return _LeftTorsoPanel;
	}

	/**
	 * This method initializes _RightTorsoPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private CriticalHitDoubleSectionPanel get_RightTorsoPanel()
	{
		if (_RightTorsoPanel == null)
		{
			_RightTorsoPanel = new CriticalHitDoubleSectionPanel();
			_RightTorsoPanel.setTitle("Right Torso");
			_RightTorsoPanel.setBounds(new Rectangle(454, 312, 220, 287));
		}
		return _RightTorsoPanel;
	}

	/**
	 * This method initializes _LeftLegPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private CriticalHitSingleSectionPanel get_LeftLegPanel()
	{
		if (_LeftLegPanel == null)
		{
			_LeftLegPanel = new CriticalHitSingleSectionPanel();
			_LeftLegPanel.setTitle("Left Leg");
			_LeftLegPanel.setBounds(new Rectangle(7, 606, 220, 157));
		}
		return _LeftLegPanel;
	}

	/**
	 * This method initializes _RightLegPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private CriticalHitSingleSectionPanel get_RightLegPanel()
	{
		if (_RightLegPanel == null)
		{
			_RightLegPanel = new CriticalHitSingleSectionPanel();
			_RightLegPanel.setTitle("Right Leg");
			_RightLegPanel.setBounds(new Rectangle(454, 605, 220, 158));
		}
		return _RightLegPanel;
	}

	/**
	 * This method initializes _HitRegisterPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel get_HitRegisterPanel()
	{
		if (_HitRegisterPanel == null)
		{
			_LifeSupportHitsLabel = new JLabel();
			_LifeSupportHitsLabel.setBounds(new Rectangle(15, 106, 196, 16));
			_LifeSupportHitsLabel.setText("Life Support Hits: O");
			_SensorHitsLabel = new JLabel();
			_SensorHitsLabel.setBounds(new Rectangle(15, 75, 196, 16));
			_SensorHitsLabel.setText("Sensor Hits: OO");
			_GyroHitsLabel = new JLabel();
			_GyroHitsLabel.setBounds(new Rectangle(15, 45, 195, 16));
			_GyroHitsLabel.setText("Gyro Hits: OO");
			_EngineHitsLabel = new JLabel();
			_EngineHitsLabel.setBounds(new Rectangle(15, 16, 196, 16));
			_EngineHitsLabel.setText("Engine Hits: OOO");
			_HitRegisterPanel = new JPanel();
			_HitRegisterPanel.setLayout(null);
			_HitRegisterPanel.setBounds(new Rectangle(232, 464, 220, 147));
			_HitRegisterPanel.add(_EngineHitsLabel, null);
			_HitRegisterPanel.add(_GyroHitsLabel, null);
			_HitRegisterPanel.add(_SensorHitsLabel, null);
			_HitRegisterPanel.add(_LifeSupportHitsLabel, null);
		}
		return _HitRegisterPanel;
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
}  //  @jve:decl-index=0:visual-constraint="10,10"
