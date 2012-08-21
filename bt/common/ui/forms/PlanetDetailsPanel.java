package bt.common.ui.forms;

import java.text.*;


import javax.swing.*;
import javax.swing.event.*;


import java.awt.BorderLayout;
import java.awt.event.*;

import bt.common.elements.galaxy.InnerSpherePlanet;
import bt.common.elements.galaxy.SolarSystemDetails;
import bt.common.elements.galaxy.StarType;
import bt.common.elements.unit.Faction;
import bt.common.ui.ClosableEditForm;
import bt.server.mapping.PlanetMapGenerator;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
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
public class PlanetDetailsPanel extends JPanel implements ClosableEditForm, ActionListener,ChangeListener
{
	private static final long serialVersionUID = 1;

    InnerSpherePlanet m_Planet;
    SolarSystemDetails m_Details;
    JPanel m_DetailsPanel = new JPanel();
    JPanel m_OwnerPanel = new JPanel();
    JPanel m_EditPanel = new JPanel();

    JTextField m_NameTextField = new JTextField();
    private JLabel m_MapNotGeneratedLabel;
    private JButton m_GeneratePlanetMapButton;
    private JTextField m_MeanSurfaceTemperatureAtEquatorTextField;
    private JLabel m_MeanSurfaceTempLabel;
    JTextField m_XCoordTextField = new JTextField();
    JTextField m_YCoordTextField = new JTextField();
    JTextField m_Owner_2750TextField = new JTextField();
    JTextField m_Owner_3025TextField = new JTextField();
    JTextField m_Owner_3030TextField = new JTextField();
    JTextField m_Owner_3040TextField = new JTextField();
    JTextField m_Owner_3052TextField = new JTextField();
    JTextField m_Owner_3057TextField = new JTextField();
    JTextField m_Owner_3062TextField = new JTextField();

    JComboBox m_StarTypeComboBox = new JComboBox();
    JComboBox m_StarMagnitudeComboBox = new JComboBox();
    JTextField m_PrimaryOrbitInAUTextField = new JTextField();
    JTextField m_JumpSailRechargeTimeTextField = new JTextField();
    JTextArea m_NotesTextArea = new JTextArea();
    JSlider m_OrbitSlider = new JSlider(JSlider.HORIZONTAL,1,100,50);
    JComboBox m_GRatingComboBox = new JComboBox();
    JTextField m_TransitTimeTextField = new JTextField();

    protected double m_BiozoneInner;
    protected double m_BiozoneOuter;
    protected double m_PlanetZoneRange;

	private JLabel m_Owner_3025Label = null;
	private JLabel m_Owner_3030Label = null;
	private JLabel m_Owner_2750_Label = null;
	private JLabel m_Owner_3040Label = null;
	private JLabel m_Owner_3052Label = null;
	private JLabel m_Owner_3057Label = null;
	private JLabel m_Owner_3062Label = null;
	private JLabel m_StarTypeLabel = null;
	private JLabel m_StarMagnitudeLabel = null;
	private JLabel m_JumpSailRechargeTimeLabel = null;
	private JLabel m_PrimaryOrbitInAULabel = null;
	private JLabel m_GRatingLabel = null;
	private JLabel m_TransitTimeLabel = null;
	private JLabel m_NotesLabel = null;
	private JLabel m_NameLabel = null;
	private JLabel m_X_CoordLabel = null;
	private JLabel m_YCoordLabel = null;

	private JLabel m_HydrographicsLabel = null;
	private JLabel m_MeanAltitudeLabel = null;
	private JLabel m_MeanSurfaceTemperatureLabel = null;

	private JTextField m_HydrographicsTextField = null;
	private JTextField m_MeanAltitudeTextField = null;
	private JTextField m_MeanSurfaceTemperatureTextField = null;

	public PlanetDetailsPanel(InnerSpherePlanet isp, SolarSystemDetails details)
    {
        m_Planet = isp;
        m_Details = details;

        initialize();

        LoadCombos();
        SetFields();

        m_StarTypeComboBox.addActionListener(this);
        m_StarTypeComboBox.setActionCommand("StarType");
        m_StarMagnitudeComboBox.addActionListener(this);
        m_StarMagnitudeComboBox.setActionCommand("StarMagnitude");
        m_GRatingComboBox.addActionListener(this);
        m_GRatingComboBox.setActionCommand("GRating");
        
        m_NameTextField.setEditable(false);
        m_XCoordTextField.setEditable(false);
        m_YCoordTextField.setEditable(false);
        m_Owner_2750TextField.setEditable(false);
        m_Owner_3025TextField.setEditable(false);
        m_Owner_3030TextField.setEditable(false);
        m_Owner_3040TextField.setEditable(false);
        m_Owner_3052TextField.setEditable(false);
        m_Owner_3057TextField.setEditable(false);
        m_Owner_3062TextField.setEditable(false);
        m_JumpSailRechargeTimeTextField.setEditable(false);
        m_PrimaryOrbitInAUTextField.setEditable(false);
        m_TransitTimeTextField.setEditable(false);

    }
        

    /**
	 * This method initializes this
	 * 
	 */
	private void initialize() 
	{
		GridBagConstraints gridBagConstraints62 = new GridBagConstraints();
		gridBagConstraints62.fill = GridBagConstraints.BOTH;
		gridBagConstraints62.gridy = 6;
		gridBagConstraints62.weightx = 1.0;
		gridBagConstraints62.insets = new Insets(2, 10, 0, 0);
		gridBagConstraints62.gridx = 3;
		GridBagConstraints gridBagConstraints52 = new GridBagConstraints();
		gridBagConstraints52.gridx = 2;
		gridBagConstraints52.anchor = GridBagConstraints.EAST;
		gridBagConstraints52.gridy = 6;
		m_MeanSurfaceTemperatureLabel = new JLabel();
		m_MeanSurfaceTemperatureLabel.setText("Average Land Surface Temperature");
		GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
		gridBagConstraints42.fill = GridBagConstraints.BOTH;
		gridBagConstraints42.gridy = 5;
		gridBagConstraints42.weightx = 1.0;
		gridBagConstraints42.insets = new Insets(2, 10, 0, 0);
		gridBagConstraints42.gridx = 3;
		GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
		gridBagConstraints32.gridx = 2;
		gridBagConstraints32.anchor = GridBagConstraints.EAST;
		gridBagConstraints32.gridy = 5;
		m_MeanAltitudeLabel = new JLabel();
		m_MeanAltitudeLabel.setText("Mean Altitude");
		GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
		gridBagConstraints22.fill = GridBagConstraints.BOTH;
		gridBagConstraints22.gridy = 5;
		gridBagConstraints22.weightx = 1.0;
		gridBagConstraints22.insets = new Insets(2, 10, 0, 0);
		gridBagConstraints22.gridx = 1;
		GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
		gridBagConstraints110.gridx = 0;
		gridBagConstraints110.anchor = GridBagConstraints.EAST;
		gridBagConstraints110.gridy = 5;
		m_HydrographicsLabel = new JLabel();
		m_HydrographicsLabel.setText("Surface Water Coverage");
		m_GRatingComboBox.setToolTipText("G Rating used to Transit between Jump Point and Planet");
		m_OrbitSlider.setToolTipText("Adjust the Primary Orbit");
		m_OrbitSlider.addChangeListener(this);
		m_StarMagnitudeComboBox.setToolTipText("The magnitude of the Primary Star");
		m_StarTypeComboBox.setToolTipText("The type of the Primary Star");
		m_YCoordTextField.setToolTipText("The Planet's Y Coordinate");
		m_XCoordTextField.setToolTipText("The Planet's X Coordinate");
		m_NameTextField.setToolTipText("The Planet's Name");
		GridBagConstraints gridBagConstraints311 = new GridBagConstraints();
		gridBagConstraints311.gridx = 4;
		gridBagConstraints311.insets = new Insets(0, 10, 0, 0);
		gridBagConstraints311.gridy = 0;
		m_YCoordLabel = new JLabel();
		m_YCoordLabel.setText("Y Coord");
		GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
		gridBagConstraints30.gridx = 2;
		gridBagConstraints30.insets = new Insets(0, 10, 0, 0);
		gridBagConstraints30.gridy = 0;
		m_X_CoordLabel = new JLabel();
		m_X_CoordLabel.setText("X Coord");
		GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
		gridBagConstraints29.gridx = 0;
		gridBagConstraints29.gridy = 0;
		m_NameLabel = new JLabel();
		m_NameLabel.setText("Name");
		GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
		gridBagConstraints28.fill = GridBagConstraints.BOTH;
		gridBagConstraints28.gridx = 5;
		gridBagConstraints28.gridy = 0;
		gridBagConstraints28.weightx = 0.25;
		gridBagConstraints28.insets = new Insets(2, 10, 0, 0);
		GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
		gridBagConstraints27.fill = GridBagConstraints.BOTH;
		gridBagConstraints27.gridx = 3;
		gridBagConstraints27.gridy = 0;
		gridBagConstraints27.weightx = 0.25;
		gridBagConstraints27.insets = new Insets(2, 10, 0, 0);
		GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
		gridBagConstraints26.fill = GridBagConstraints.BOTH;
		gridBagConstraints26.gridx = 1;
		gridBagConstraints26.gridy = 0;
		gridBagConstraints26.weightx = 1.0;
		gridBagConstraints26.insets = new Insets(2, 10, 0, 0);
		m_DetailsPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
		gridBagConstraints25.gridx = 0;
		gridBagConstraints25.anchor = GridBagConstraints.WEST;
		gridBagConstraints25.gridy = 7;
		m_NotesLabel = new JLabel();
		m_NotesLabel.setText("Notes");
		GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
		gridBagConstraints23.fill = GridBagConstraints.BOTH;
		gridBagConstraints23.gridx = 0;
		gridBagConstraints23.gridy = 9;
		gridBagConstraints23.weightx = 1.0;
		gridBagConstraints23.weighty = 1.0;
		gridBagConstraints23.gridwidth = 4;
		m_NotesTextArea.setWrapStyleWord(true);
		m_NotesTextArea.setLineWrap(true);
		m_PrimaryOrbitInAUTextField.setEditable(false);
		m_PrimaryOrbitInAUTextField.setToolTipText("The planet's orbital distance from the primary star");
		m_TransitTimeTextField.setEditable(false);
		m_TransitTimeTextField.setToolTipText("Transit time from planet to Jump Point");
		m_JumpSailRechargeTimeTextField.setEnabled(true);
		m_JumpSailRechargeTimeTextField.setToolTipText("Length of time it takes a Jumpship to recharge jump drive using the jump sail");
		m_JumpSailRechargeTimeTextField.setEditable(false);
		GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
		gridBagConstraints20.gridx = 2;
		gridBagConstraints20.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints20.anchor = GridBagConstraints.EAST;
		gridBagConstraints20.weightx = 1.0;
		gridBagConstraints20.gridy = 4;
		m_TransitTimeLabel = new JLabel();
		m_TransitTimeLabel.setText("Transit Time (hrs)");
		GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
		gridBagConstraints19.gridx = 0;
		gridBagConstraints19.anchor = GridBagConstraints.EAST;
		gridBagConstraints19.gridy = 4;
		m_GRatingLabel = new JLabel();
		m_GRatingLabel.setText("G Rating");
		GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
		gridBagConstraints18.gridx = 0;
		gridBagConstraints18.anchor = GridBagConstraints.EAST;
		gridBagConstraints18.gridy = 3;
		m_PrimaryOrbitInAULabel = new JLabel();
		m_PrimaryOrbitInAULabel.setText("Primary Orbit in AU");
		GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
		gridBagConstraints17.gridx = 2;
		gridBagConstraints17.anchor = GridBagConstraints.EAST;
		gridBagConstraints17.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints17.weightx = 1.0;
		gridBagConstraints17.gridy = 1;
		m_JumpSailRechargeTimeLabel = new JLabel();
		m_JumpSailRechargeTimeLabel.setText("Jump Sail Recharge Time (hrs)");
		GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
		gridBagConstraints16.gridx = 0;
		gridBagConstraints16.anchor = GridBagConstraints.EAST;
		gridBagConstraints16.gridy = 1;
		m_StarMagnitudeLabel = new JLabel();
		m_StarMagnitudeLabel.setText("Star Magnitude");
		GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
		gridBagConstraints15.gridx = 0;
		gridBagConstraints15.anchor = GridBagConstraints.EAST;
		gridBagConstraints15.gridy = 0;
		m_StarTypeLabel = new JLabel();
		m_StarTypeLabel.setText("Star Type");
		GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
		gridBagConstraints14.fill = GridBagConstraints.BOTH;
		gridBagConstraints14.gridx = 3;
		gridBagConstraints14.gridy = 4;
		gridBagConstraints14.weightx = 0.5;
		gridBagConstraints14.insets = new Insets(2, 10, 0, 0);
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.fill = GridBagConstraints.BOTH;
		gridBagConstraints13.gridx = 1;
		gridBagConstraints13.gridy = 4;
		gridBagConstraints13.weightx = 0.25;
		gridBagConstraints13.insets = new Insets(2, 10, 0, 0);
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.fill = GridBagConstraints.BOTH;
		gridBagConstraints12.gridx = 3;
		gridBagConstraints12.gridy = 1;
		gridBagConstraints12.weightx = 0.5;
		gridBagConstraints12.insets = new Insets(2, 10, 0, 0);
		GridBagConstraints gridBagConstraints111 = new GridBagConstraints();
		gridBagConstraints111.fill = GridBagConstraints.BOTH;
		gridBagConstraints111.gridx = 2;
		gridBagConstraints111.gridy = 3;
		gridBagConstraints111.weightx = 0.0;
		gridBagConstraints111.gridwidth = 2;
		gridBagConstraints111.anchor = GridBagConstraints.CENTER;
		gridBagConstraints111.insets = new Insets(2, 0, 0, 0);
		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.fill = GridBagConstraints.BOTH;
		gridBagConstraints10.gridx = 1;
		gridBagConstraints10.gridy = 3;
		gridBagConstraints10.weightx = 0.25;
		gridBagConstraints10.insets = new Insets(2, 10, 0, 0);
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.fill = GridBagConstraints.BOTH;
		gridBagConstraints9.gridx = 1;
		gridBagConstraints9.gridy = 1;
		gridBagConstraints9.weightx = 0.25;
		gridBagConstraints9.insets = new Insets(2, 10, 0, 0);
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.fill = GridBagConstraints.BOTH;
		gridBagConstraints8.gridx = 1;
		gridBagConstraints8.gridy = 0;
		gridBagConstraints8.weightx = 0.25;
		gridBagConstraints8.insets = new Insets(2, 10, 0, 0);
		GridBagLayout m_EditPanelLayout = new GridBagLayout();
		m_EditPanel.setLayout(m_EditPanelLayout);
		m_EditPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		m_EditPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
		m_EditPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.0, 0.1, 0.0, 0.1, 0.1, 0.1, 0.1};
		m_EditPanelLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 7, 7, 20, 7, 7, 7};
		m_Owner_3062TextField.setToolTipText("The planet's owner in 3062");
		m_Owner_3057TextField.setToolTipText("The planet's owner in 3057");
		m_Owner_3052TextField.setToolTipText("The planet's owner in 3052");
		m_Owner_3040TextField.setToolTipText("The planet's owner in 3040");
		m_Owner_3030TextField.setToolTipText("The planet's owner in 3030");
		m_Owner_3025TextField.setToolTipText("The planet's owner in 3025");
		m_Owner_2750TextField.setToolTipText("The planet's owner in 2750");
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.gridx = 12;
		gridBagConstraints7.insets = new Insets(0, 9, 0, 0);
		gridBagConstraints7.gridy = 1;
		m_Owner_3062Label = new JLabel();
		m_Owner_3062Label.setText("3062");
		GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
		gridBagConstraints61.gridx = 10;
		gridBagConstraints61.insets = new Insets(0, 9, 0, 0);
		gridBagConstraints61.gridy = 1;
		m_Owner_3057Label = new JLabel();
		m_Owner_3057Label.setText("3057");
		GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
		gridBagConstraints51.gridx = 8;
		gridBagConstraints51.insets = new Insets(0, 9, 0, 0);
		gridBagConstraints51.gridy = 1;
		m_Owner_3052Label = new JLabel();
		m_Owner_3052Label.setText("3052");
		GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
		gridBagConstraints41.gridx = 6;
		gridBagConstraints41.insets = new Insets(0, 9, 0, 0);
		gridBagConstraints41.gridy = 1;
		m_Owner_3040Label = new JLabel();
		m_Owner_3040Label.setText("3040");
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		gridBagConstraints31.gridx = 0;
		gridBagConstraints31.insets = new Insets(0, 9, 0, 0);
		gridBagConstraints31.gridy = 1;
		m_Owner_2750_Label = new JLabel();
		m_Owner_2750_Label.setText("2750");
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 4;
		gridBagConstraints21.insets = new Insets(0, 9, 0, 0);
		gridBagConstraints21.gridy = 1;
		m_Owner_3030Label = new JLabel();
		m_Owner_3030Label.setText("3030");
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 2;
		gridBagConstraints11.insets = new Insets(0, 9, 0, 0);
		gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints11.gridy = 1;
		m_Owner_3025Label = new JLabel();
		m_Owner_3025Label.setText("3025");
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.fill = GridBagConstraints.BOTH;
		gridBagConstraints6.gridx = 13;
		gridBagConstraints6.gridy = 1;
		gridBagConstraints6.weightx = 1.0;
		gridBagConstraints6.insets = new Insets(1, 1, 1, 1);
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.fill = GridBagConstraints.BOTH;
		gridBagConstraints5.gridx = 11;
		gridBagConstraints5.gridy = 1;
		gridBagConstraints5.weightx = 1.0;
		gridBagConstraints5.insets = new Insets(1, 1, 1, 1);
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.fill = GridBagConstraints.BOTH;
		gridBagConstraints4.gridx = 7;
		gridBagConstraints4.gridy = 1;
		gridBagConstraints4.weightx = 1.0;
		gridBagConstraints4.insets = new Insets(1, 1, 1, 1);
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.gridx = 5;
		gridBagConstraints3.gridy = 1;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.insets = new Insets(1, 1, 1, 1);
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.gridx = 9;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.insets = new Insets(1, 1, 1, 1);
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridx = 3;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.insets = new Insets(1, 1, 1, 1);
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 1, 1, 1);
		m_OwnerPanel.setLayout(new GridBagLayout());
		m_OwnerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Owner", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
        m_EditPanel.setBorder(BorderFactory.createEtchedBorder());
		m_EditPanel.add(m_StarTypeComboBox, gridBagConstraints8);
		m_EditPanel.add(m_StarMagnitudeComboBox, gridBagConstraints9);
		m_EditPanel.add(m_PrimaryOrbitInAUTextField, new GridBagConstraints(1, 2, 1, 1, 0.25, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 10, 0, 0), 0, 0));
		m_EditPanel.add(m_OrbitSlider, new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 0, 0, 0), 0, 0));
		m_EditPanel.add(m_JumpSailRechargeTimeTextField, new GridBagConstraints(3, 1, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 10, 0, 0), 0, 0));
		m_EditPanel.add(m_GRatingComboBox, new GridBagConstraints(1, 3, 1, 1, 0.25, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 10, 0, 0), 0, 0));
		m_EditPanel.add(m_TransitTimeTextField, new GridBagConstraints(3, 4, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 10, 0, 0), 0, 0));
		m_EditPanel.add(m_StarTypeLabel, gridBagConstraints15);
		m_EditPanel.add(m_StarMagnitudeLabel, gridBagConstraints16);
		m_EditPanel.add(m_JumpSailRechargeTimeLabel, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		m_EditPanel.add(m_PrimaryOrbitInAULabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		m_EditPanel.add(m_GRatingLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		m_EditPanel.add(m_TransitTimeLabel, new GridBagConstraints(2, 4, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		m_EditPanel.add(new JScrollPane(m_NotesTextArea), new GridBagConstraints(0, 11, 4, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		m_EditPanel.add(m_NotesLabel, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		m_EditPanel.add(m_HydrographicsLabel, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		m_EditPanel.add(getM_HydrographicsTextField(), new GridBagConstraints(1, 8, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 10, 0, 0), 0, 0));
		m_EditPanel.add(m_MeanAltitudeLabel, new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		m_EditPanel.add(getM_MeanAltitudeTextField(), new GridBagConstraints(3, 9, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 10, 0, 0), 0, 0));
		m_EditPanel.add(m_MeanSurfaceTemperatureLabel, new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		m_EditPanel.add(getM_MeanSurfaceTemperatureTextField(), new GridBagConstraints(3, 8, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 10, 0, 0), 0, 0));
		m_EditPanel.add(getM_MeanSurfaceTempLabel(), new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		m_EditPanel.add(getM_MeanSurfaceTemperatureAtEquatorTextField(), new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 10, 0, 0), 0, 0));
		m_EditPanel.add(getM_GeneratePlanetMapButton(), new GridBagConstraints(0, 6, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		m_EditPanel.add(getM_MapNotGeneratedLabel(), new GridBagConstraints(0, 6, 4, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		m_DetailsPanel.setBorder(BorderFactory.createEtchedBorder());
		m_DetailsPanel.add(m_NameTextField, gridBagConstraints26);
		m_DetailsPanel.add(m_XCoordTextField, gridBagConstraints27);
		m_DetailsPanel.add(m_YCoordTextField, gridBagConstraints28);
		m_DetailsPanel.add(m_NameLabel, gridBagConstraints29);
		m_DetailsPanel.add(m_X_CoordLabel, gridBagConstraints30);
		m_DetailsPanel.add(m_YCoordLabel, gridBagConstraints311);
		m_OwnerPanel.add(m_Owner_2750TextField, gridBagConstraints);
		m_OwnerPanel.add(m_Owner_3025TextField, gridBagConstraints1);
		m_OwnerPanel.add(m_Owner_3052TextField, gridBagConstraints2);
		m_OwnerPanel.add(m_Owner_3030TextField, gridBagConstraints3);
		m_OwnerPanel.add(m_Owner_3040TextField, gridBagConstraints4);
		m_OwnerPanel.add(m_Owner_3057TextField, gridBagConstraints5);
		m_OwnerPanel.add(m_Owner_3062TextField, gridBagConstraints6);
		m_OwnerPanel.add(m_Owner_3025Label, gridBagConstraints11);
		m_OwnerPanel.add(m_Owner_3030Label, gridBagConstraints21);
		m_OwnerPanel.add(m_Owner_2750_Label, gridBagConstraints31);
		m_OwnerPanel.add(m_Owner_3040Label, gridBagConstraints41);
		m_OwnerPanel.add(m_Owner_3052Label, gridBagConstraints51);
		m_OwnerPanel.add(m_Owner_3057Label, gridBagConstraints61);
		m_OwnerPanel.add(m_Owner_3062Label, gridBagConstraints7);
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(546, 412));
		this.add(m_OwnerPanel, BorderLayout.SOUTH);
		this.add(m_DetailsPanel, BorderLayout.NORTH);
		this.add(m_EditPanel, BorderLayout.CENTER);
	}

	protected void LoadCombos()
    {
        m_StarTypeComboBox.removeAllItems();
        for (StarType st: StarType.values())
        	m_StarTypeComboBox.addItem(st);

        m_StarMagnitudeComboBox.removeAllItems();
        m_StarMagnitudeComboBox.addItem(new Integer(0));
        m_StarMagnitudeComboBox.addItem(new Integer(1));
        m_StarMagnitudeComboBox.addItem(new Integer(2));
        m_StarMagnitudeComboBox.addItem(new Integer(3));
        m_StarMagnitudeComboBox.addItem(new Integer(4));
        m_StarMagnitudeComboBox.addItem(new Integer(5));
        m_StarMagnitudeComboBox.addItem(new Integer(6));
        m_StarMagnitudeComboBox.addItem(new Integer(7));
        m_StarMagnitudeComboBox.addItem(new Integer(8));
        m_StarMagnitudeComboBox.addItem(new Integer(9));

        m_GRatingComboBox.removeAllItems();
        m_GRatingComboBox.addItem(new Double(0.5));
        m_GRatingComboBox.addItem(new Double(0.6));
        m_GRatingComboBox.addItem(new Double(0.7));
        m_GRatingComboBox.addItem(new Double(0.8));
        m_GRatingComboBox.addItem(new Double(0.9));
        m_GRatingComboBox.addItem(new Double(1.0));
        m_GRatingComboBox.addItem(new Double(1.1));
        m_GRatingComboBox.addItem(new Double(1.2));
        m_GRatingComboBox.addItem(new Double(1.3));
        m_GRatingComboBox.addItem(new Double(1.4));
        m_GRatingComboBox.addItem(new Double(1.5));
        m_GRatingComboBox.setSelectedItem(new Double(1.0));
    }

    protected void SetFields()
    {
        m_NameTextField.setText(m_Planet.getSystem());
        m_XCoordTextField.setText(Double.toString(m_Planet.getXCoord()));
        m_YCoordTextField.setText(Double.toString(m_Planet.getYCoord()));
        m_Owner_2750TextField.setText(m_Planet.getOwner(2750));
        m_Owner_3025TextField.setText(m_Planet.getOwner(3025));
        m_Owner_3030TextField.setText(m_Planet.getOwner(3030));
        m_Owner_3040TextField.setText(m_Planet.getOwner(3040));
        m_Owner_3052TextField.setText(m_Planet.getOwner(3052));
        m_Owner_3057TextField.setText(m_Planet.getOwner(3057));
        m_Owner_3062TextField.setText(m_Planet.getOwner(3062));

        m_Owner_2750TextField.setToolTipText(Faction.getFromAbbreviation(m_Planet.getOwner(2750)).getName());
        m_Owner_3025TextField.setToolTipText(Faction.getFromAbbreviation(m_Planet.getOwner(3025)).getName());
        m_Owner_3030TextField.setToolTipText(Faction.getFromAbbreviation(m_Planet.getOwner(3030)).getName());
        m_Owner_3040TextField.setToolTipText(Faction.getFromAbbreviation(m_Planet.getOwner(3040)).getName());
        m_Owner_3052TextField.setToolTipText(Faction.getFromAbbreviation(m_Planet.getOwner(3052)).getName());
        m_Owner_3057TextField.setToolTipText(Faction.getFromAbbreviation(m_Planet.getOwner(3057)).getName());
        m_Owner_3062TextField.setToolTipText(Faction.getFromAbbreviation(m_Planet.getOwner(3062)).getName());

        m_StarTypeComboBox.setSelectedItem(m_Details.getStarType());
        m_StarMagnitudeComboBox.setSelectedItem(new Integer(m_Details.getStarMagnitude()));
        NumberFormat formatter = new DecimalFormat("0.00");
        m_PrimaryOrbitInAUTextField.setText(formatter.format(m_Details.getPrimaryPlanetOrbitInAU()));

        m_MeanSurfaceTemperatureAtEquatorTextField.setText(Integer.toString(m_Details.getMeanTemperatureAtSeaLevel()));
        if (m_Details.isMapGenerated())
        {
	    	m_HydrographicsTextField.setText(Integer.toString(m_Details.getMap().getHydrographics()) + "%");
	    	m_MeanAltitudeTextField.setText(Integer.toString(m_Details.getMap().getMeanAltitude()) + "'");
	    	m_MeanSurfaceTemperatureTextField.setText(Integer.toString(m_Details.getMap().getMeanSurfaceTemperature()) + "C");
        }
        
        m_NotesTextArea.setText(m_Details.getNotes());
        m_NotesTextArea.setCaretPosition(0);
        
        m_MapNotGeneratedLabel.setVisible(!m_Details.isMapGenerated());

        SetSliderPosition();

        RecalcFields();
    }

    protected void GetFields()
    {
    	m_Details.setStarType((StarType)m_StarTypeComboBox.getSelectedItem());
    	m_Details.setStarMagnitude(Integer.parseInt(m_StarMagnitudeComboBox.getSelectedItem().toString()));
    	m_Details.setPrimaryPlanetOrbitInAU(Double.parseDouble(m_PrimaryOrbitInAUTextField.getText()));
    	m_Details.setMeanTemperatureAtSeaLevel(Integer.parseInt(m_MeanSurfaceTemperatureAtEquatorTextField.getText()));
    	m_Details.setNotes(m_NotesTextArea.getText());
    }

    public boolean IsClosable()
    {
        return true;
    }

    public void ForceEditCompletion()
    {
        GetFields();
    }

    public void actionPerformed(ActionEvent ae)
    {
        String command = ae.getActionCommand();
        if (command.equals("StarType") || command.equals("StarMagnitude") || command.equals("GRating"))
        {
            GetFields();
            RecalcFields();
        }
        if (command.equals("GenerateMap"))
        {
    		getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    		MapGeneratorWorker mgw = new MapGeneratorWorker(m_Planet,m_Details);
    		mgw.execute();
        }
    }

    protected void SetSliderPosition()
    {
        m_BiozoneInner = SolarSystemDetails.GetBiozoneInnerLimit(m_Details.getStarType());
        m_BiozoneOuter = SolarSystemDetails.GetBiozoneOuterLimit(m_Details.getStarType());
        m_PlanetZoneRange = 100.0 / (m_BiozoneOuter - m_BiozoneInner);

        int TrackIndex = (int)((m_Details.getPrimaryPlanetOrbitInAU() - m_BiozoneInner) * m_PlanetZoneRange);
        m_OrbitSlider.setValue(TrackIndex);
    }

    protected void RecalcFields()
    {
        m_BiozoneInner = SolarSystemDetails.GetBiozoneInnerLimit(m_Details.getStarType());
        m_BiozoneOuter = SolarSystemDetails.GetBiozoneOuterLimit(m_Details.getStarType());
        m_PlanetZoneRange = 100.0 / (m_BiozoneOuter - m_BiozoneInner);

        m_JumpSailRechargeTimeTextField.setText(Integer.toString(SolarSystemDetails.getJumpSailRechargeTime(m_Details.getStarType(),m_Details.getStarMagnitude())));

        NumberFormat formatter = new DecimalFormat("0.00");
        Double GRating = (Double)m_GRatingComboBox.getSelectedItem();
        double transitTime = SolarSystemDetails.getTransitTime(m_Details.getStarType(),m_Details.getStarMagnitude(),m_Details.getPrimaryPlanetOrbitInAU(),GRating.doubleValue());
        m_TransitTimeTextField.setText(formatter.format(transitTime));

        int TrackIndex = m_OrbitSlider.getValue();

        double PlanetDistinAU = m_BiozoneInner + (TrackIndex / m_PlanetZoneRange);
        m_PrimaryOrbitInAUTextField.setText(formatter.format(PlanetDistinAU));

        int surfaceTemp = 28 + (TrackIndex / 4);
        m_MeanSurfaceTemperatureAtEquatorTextField.setText(Integer.toString(surfaceTemp));
    }

    public void stateChanged(ChangeEvent ce)
    {
        GetFields();
        RecalcFields();
    }


	/**
	 * This method initializes m_HydrographicsTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getM_HydrographicsTextField() {
		if (m_HydrographicsTextField == null) {
			m_HydrographicsTextField = new JTextField();
			m_HydrographicsTextField.setEditable(false);
		}
		return m_HydrographicsTextField;
	}


	/**
	 * This method initializes m_MeanAltitudeTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getM_MeanAltitudeTextField() {
		if (m_MeanAltitudeTextField == null) {
			m_MeanAltitudeTextField = new JTextField();
			m_MeanAltitudeTextField.setEditable(false);
		}
		return m_MeanAltitudeTextField;
	}


	/**
	 * This method initializes m_MeanSurfaceTemperatureTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getM_MeanSurfaceTemperatureTextField() {
		if (m_MeanSurfaceTemperatureTextField == null) {
			m_MeanSurfaceTemperatureTextField = new JTextField();
			m_MeanSurfaceTemperatureTextField.setEditable(false);
		}
		return m_MeanSurfaceTemperatureTextField;
	}
	
	private JLabel getM_MeanSurfaceTempLabel() {
		if(m_MeanSurfaceTempLabel == null) {
			m_MeanSurfaceTempLabel = new JLabel();
			m_MeanSurfaceTempLabel.setText("Mean Surface Temperature At Equator");
		}
		return m_MeanSurfaceTempLabel;
	}
	
	private JTextField getM_MeanSurfaceTemperatureAtEquatorTextField() {
		if(m_MeanSurfaceTemperatureAtEquatorTextField == null) {
			m_MeanSurfaceTemperatureAtEquatorTextField = new JTextField();
			m_MeanSurfaceTemperatureAtEquatorTextField.setEditable(false);
		}
		return m_MeanSurfaceTemperatureAtEquatorTextField;
	}
	
	private JButton getM_GeneratePlanetMapButton() {
		if(m_GeneratePlanetMapButton == null) {
			m_GeneratePlanetMapButton = new JButton();
			m_GeneratePlanetMapButton.setText("Generate Planet Map");
			m_GeneratePlanetMapButton.addActionListener(this);
			m_GeneratePlanetMapButton.setActionCommand("GenerateMap");
		}
		return m_GeneratePlanetMapButton;
	}
	
	private JLabel getM_MapNotGeneratedLabel() {
		if(m_MapNotGeneratedLabel == null) {
			m_MapNotGeneratedLabel = new JLabel();
			m_MapNotGeneratedLabel.setText("Map Not Yet Generated");
			m_MapNotGeneratedLabel.setFont(new java.awt.Font("Segoe UI",1,12));
			m_MapNotGeneratedLabel.setForeground(new java.awt.Color(255,0,0));
		}
		return m_MapNotGeneratedLabel;
	}

    private class MapGeneratorWorker extends SwingWorker<String, String>
    {
    	private InnerSpherePlanet _isp;
    	private SolarSystemDetails _ssd;
    	
    	public MapGeneratorWorker(InnerSpherePlanet isp, SolarSystemDetails ssd)
    	{
    		_isp = isp;
    		_ssd = ssd;
    	}

		@Override
		protected String doInBackground() throws Exception {
    		PlanetMapGenerator pmg = new PlanetMapGenerator();
    		pmg.generatePlanetMap(_isp, _ssd, true, true, false, false, true);
			return "Map Created";
		}
    	
		@Override
		protected void done()
		{
			try
			{
				SetFields();
		        RecalcFields();

			}
			finally
			{
        		getRootPane().setCursor(Cursor.getDefaultCursor());    		
			}
		
		}
    }


}  //  @jve:decl-index=0:visual-constraint="10,10"
