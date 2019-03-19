/*******************************************************************************
 * Title: Legatus
 * 
 * Copyright Daniel Cleyne (c) 2004-2013
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at our option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * @author Daniel Cleyne
 ******************************************************************************/

package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import bt.elements.campaign.Campaign;
import bt.elements.campaign.CampaignScenario;
import bt.elements.campaign.Force;
import bt.elements.campaign.Situation;
import bt.elements.campaign.SituationIntendedMovement;
import bt.elements.campaign.SituationUnitLocation;
import bt.managers.CampaignManager;
import bt.mapping.Coordinate;
import bt.mapping.Hexagon;
import bt.mapping.campaign.CampaignBoard;
import bt.mapping.campaign.CampaignMap;
import bt.ui.controls.ZoomSlider;
import bt.ui.panels.BoardPanel;
import bt.ui.renderers.CampaignBoardRenderer;
import bt.ui.renderers.MapFactory;
import bt.ui.sprites.CombatUnitCounter;
import bt.ui.sprites.HexStraightArrowSprite;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;


public class CampaignScenarioSituationRenderTest extends JFrame implements ItemListener, ActionListener
{
	private static final long serialVersionUID = 1;

	private BoardPanel _BoardPanel = null;
	private CampaignBoard _Board = null;
	private CampaignBoardRenderer _BoardRenderer = null;
	private JComboBox<String> _ScenariosComboBox;
	
	private Campaign _Campaign;
	
	public CampaignScenarioSituationRenderTest()
	{
		try
		{
			PropertyUtil.loadSystemProperties("bt/system.properties");

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setTitle("Campaign Map Rendering Test");
			setFont(new Font("SansSerif", Font.PLAIN, 9));

			_BoardPanel = new BoardPanel();
			JScrollPane scrollPane = new JScrollPane(_BoardPanel);
			
			JPanel contentPanel = new JPanel();
			contentPanel.setLayout(new BorderLayout());
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			
			ZoomSlider slider = new ZoomSlider(null);
			contentPanel.add(slider, BorderLayout.SOUTH);

			JPanel northPanel = new JPanel();
			northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.LINE_AXIS));
			northPanel.add(new JLabel("Scenario"));
			northPanel.add(Box.createHorizontalStrut(5));
			
			_ScenariosComboBox = new JComboBox<String>();
			_ScenariosComboBox.addItemListener(this);
			northPanel.add(_ScenariosComboBox);
			northPanel.add(Box.createHorizontalGlue());
			
			JButton saveButton = new JButton("Save Scenario Image");
			saveButton.setActionCommand("Save");
			saveButton.addActionListener(this);
			northPanel.add(saveButton);
			
			contentPanel.add(northPanel, BorderLayout.NORTH);
			
			
			setContentPane(contentPanel);

			setSize(570, 1060);
			// setSize(1280,1024);
			setVisible(true);

			ArrayList<String> availableCampaigns = CampaignManager.INSTANCE.getCampaignList();
			_Campaign = CampaignManager.INSTANCE.getCampaign(availableCampaigns.get(0));
			ArrayList<String> scenarioNames = new ArrayList<String>();
			for (CampaignScenario scenario: _Campaign.getScenarios())
				scenarioNames.add(scenario.getNumber());
			Collections.sort(scenarioNames);

			CampaignMap map = _Campaign.getMap();
			_Board = constructBoard(map);
			
			_BoardRenderer = (CampaignBoardRenderer) MapFactory.INSTANCE.createBoardRenderer(map.getMapType());
			_BoardRenderer.setBoard(_BoardPanel, _Board);
			slider.setBoardRenderer(_BoardRenderer);
			
			for (String scenarioName: scenarioNames)
				_ScenariosComboBox.addItem(scenarioName);
			
			_ScenariosComboBox.setSelectedIndex(0);
		} 
		catch (Exception e)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(e));
			System.exit(0);
		}
	}
	
	private CampaignBoard constructBoard(CampaignMap map) throws Exception
	{
		CampaignBoard board = new CampaignBoard(82);
		board.setName("Example");
		board.addMap(map, new Coordinate(1,1));
		board.completedAddingMaps();
		return board;
	}
	
	private void selectScenario(String scenarioName)
	{
		_BoardRenderer.getSpriteManager().clearAllSprites();
		
		CampaignScenario scenario = _Campaign.getScenario(scenarioName);
		if (scenario != null)
		{
			Situation situation = scenario.getSituation();
			for (SituationUnitLocation sul: situation.getUnitLocations())
			{
				String unitName = sul.getUnitName();
				System.out.println(unitName);
				Force force = _Campaign.getForceForUnit(unitName);
				Color unitColor = force.getColor();
				Coordinate coord = new Coordinate(sul.getCoordinate().x - 1, sul.getCoordinate().y - 1);
				CombatUnitCounter unitCounter = new CombatUnitCounter(_BoardRenderer, coord, unitColor, unitName, force.getAbbreviation());
				unitCounter.setVisible(true);
				_BoardRenderer.getSpriteManager().registerElement(unitCounter);				
			}
			
			for (SituationIntendedMovement sim: situation.getUnitMovements())
			{
				SituationUnitLocation sul = situation.getUnitLocation(sim.getUnitName());
				Coordinate startCoord = sul.getCoordinate();
				Coordinate endCoord = sim.getDestination();
				
				Hexagon startHex = _Board.getHexGrid().getHex(startCoord.x - 1, startCoord.y - 1);
				Hexagon endHex = _Board.getHexGrid().getHex(endCoord.x - 1, endCoord.y - 1);
				HexStraightArrowSprite hsas = new HexStraightArrowSprite(_BoardRenderer, startHex, endHex, false, Color.CYAN, Color.BLACK);
				hsas.setVisible(true);
				_BoardRenderer.getSpriteManager().registerWidget(hsas);				
			}
		}
	}
	
	public Color getColorForSide(String sideName)
	{
		if (sideName.equals("Kurita"))
			return Color.RED;
		if (sideName.equals("Davion"))
			return Color.BLUE;
		
		return Color.BLACK;
	}
	

	private void saveCurrentScenario()
	{
		try
		{
			Rectangle bounds = _BoardPanel.getBounds();
			BufferedImage image = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_3BYTE_BGR);
	
			Graphics g = image.getGraphics();
			g.setClip(bounds.x, bounds.y, bounds.width, bounds.height);
			_BoardPanel.paint(g);
			
			ImageIO.write(image, "PNG", new File("../CampaignManagerData/campaigns/Scenario_" + (String)_ScenariosComboBox.getSelectedItem() + ".png"));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			CampaignScenarioSituationRenderTest client = new CampaignScenarioSituationRenderTest();
			client.setVisible(true);
		} 
		catch (Exception e)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(e));
		}

	}

	@Override
	public void itemStateChanged(ItemEvent ie)
	{
		if (ie.getStateChange() == ItemEvent.SELECTED)
		{
			selectScenario((String)_ScenariosComboBox.getSelectedItem());			
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getActionCommand().equalsIgnoreCase("Save"))
		{
			saveCurrentScenario();
		}
	}
}
