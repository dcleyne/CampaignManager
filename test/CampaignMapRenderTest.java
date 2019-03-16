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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import bt.elements.galaxy.SettlementType;
import bt.mapping.Coordinate;
import bt.mapping.Hexagon;
import bt.mapping.TerrainType;
import bt.mapping.campaign.CampaignBoard;
import bt.mapping.campaign.CampaignMap;
import bt.mapping.campaign.CampaignMapHex;
import bt.ui.controls.ZoomSlider;
import bt.ui.panels.BoardPanel;
import bt.ui.renderers.CampaignBoardRenderer;
import bt.ui.renderers.MapFactory;
import bt.ui.sprites.CombatUnitCounter;
import bt.ui.sprites.HexStraightArrowSprite;
import bt.util.ExceptionUtil;


public class CampaignMapRenderTest extends JFrame
{
	private static final long serialVersionUID = 1;

	private BoardPanel _BoardPanel = null;
	private CampaignBoard _Board = null;
	private CampaignBoardRenderer _BoardRenderer = null;
	
	public CampaignMapRenderTest()
	{
		try
		{
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
			
			setContentPane(contentPanel);

			setSize(1024, 768);
			// setSize(1280,1024);
			setVisible(true);

			CampaignMap map = constructMap();
			_Board = constructBoard(map);
			
			
			_BoardRenderer = (CampaignBoardRenderer) MapFactory.INSTANCE.createBoardRenderer(_Board.getMapType());
			_BoardRenderer.setBoard(_BoardPanel, _Board);
			slider.setBoardRenderer(_BoardRenderer);
			
			
			CombatUnitCounter unitCounter = new CombatUnitCounter(_BoardRenderer, new Coordinate(1,1), Color.RED, "Elias' Company");
			unitCounter.setVisible(true);
			_BoardRenderer.getSpriteManager().registerWidget(unitCounter);
			
			Hexagon startHex = _Board.getHexGrid().getHex(1, 1);
			Hexagon endHex = _Board.getHexGrid().getHex(5, 5);
			HexStraightArrowSprite hsas = new HexStraightArrowSprite(_BoardRenderer, startHex, endHex, false, Color.CYAN, Color.BLACK);
			hsas.setVisible(true);
			_BoardRenderer.getSpriteManager().registerElement(hsas);
			
			org.jdom.Document doc = new Document();
			doc.setRootElement(map.saveToElement());

			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			out.output(doc, System.out);
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
	
	private CampaignMap constructMap()
	{
		CampaignMap map = new CampaignMap(7,11);
		map.createBlankMap(TerrainType.PLAINS);
	
		setTerrain(map,3,0,TerrainType.MOUNTAINS, SettlementType.TOWN, "Wuhan Pass");
		setTerrain(map,5,1,TerrainType.MOUNTAINS, SettlementType.TOWN, "Harper's Canyon");
		setTerrain(map,3,2,TerrainType.PLAINS, SettlementType.TOWN, "Zhuru");
		setTerrain(map,4,3,TerrainType.SWAMP, SettlementType.TOWN, "Shidong");
		setTerrain(map,4,3,TerrainType.PLAINS, SettlementType.TOWN, "Janjigo");
		setTerrain(map,1,3,TerrainType.FOREST, SettlementType.TOWN, "Caidian Forest East");
		setTerrain(map,1,4,TerrainType.DEEPFOREST, SettlementType.TOWN, "Caidian Forest");
		setTerrain(map,0,4,TerrainType.FOREST, SettlementType.TOWN, "Caidian Forest North");
		setTerrain(map,3,5,TerrainType.SWAMP, SettlementType.CITY, "Daji Farm");
		setTerrain(map,5,5,TerrainType.FOREST, SettlementType.TOWN, "Wudong");
		setTerrain(map,1,6,TerrainType.PLAINS, SettlementType.TOWN, "Hongshan");
		setTerrain(map,4,7,TerrainType.PLAINS, SettlementType.TOWN, "Wuphushan");
		setTerrain(map,0,8,TerrainType.FOREST, SettlementType.TOWN, "Zhashan");
		setTerrain(map,3,9,TerrainType.PLAINS, SettlementType.CITY, "Tianhe");
		setTerrain(map,3,10,TerrainType.PLAINS, SettlementType.CAPITOL, "New Wuhan City");
		
		setTerrain(map,0,0,TerrainType.MOUNTAINS);
		setTerrain(map,1,0,TerrainType.MOUNTAINS);
		setTerrain(map,2,0,TerrainType.MOUNTAINS);
		setTerrain(map,2,1,TerrainType.MOUNTAINS);
		setTerrain(map,3,1,TerrainType.MOUNTAINS);
		setTerrain(map,4,0,TerrainType.MOUNTAINS);
		setTerrain(map,4,1,TerrainType.MOUNTAINS);
		setTerrain(map,4,2,TerrainType.MOUNTAINS);
		setTerrain(map,5,0,TerrainType.MOUNTAINS);
		setTerrain(map,6,0,TerrainType.MOUNTAINS);
		setTerrain(map,6,1,TerrainType.MOUNTAINS);
		setTerrain(map,6,2,TerrainType.MOUNTAINS);
		
		setTerrain(map,5,4,TerrainType.FOREST);
		setTerrain(map,6,4,TerrainType.FOREST);
		setTerrain(map,6,5,TerrainType.FOREST);
		setTerrain(map,0,5,TerrainType.FOREST);
		setTerrain(map,0,9,TerrainType.FOREST);
		setTerrain(map,0,10,TerrainType.FOREST);
		setTerrain(map,1,8,TerrainType.FOREST);
		setTerrain(map,1,9,TerrainType.FOREST);
		setTerrain(map,1,10,TerrainType.FOREST);

		setTerrain(map,2,2,TerrainType.DEEPFOREST);
		setTerrain(map,2,3,TerrainType.DEEPFOREST);
		setTerrain(map,2,4,TerrainType.DEEPFOREST);
		setTerrain(map,2,5,TerrainType.DEEPFOREST);
		setTerrain(map,3,3,TerrainType.DEEPFOREST);
		setTerrain(map,3,4,TerrainType.DEEPFOREST);

		setTerrain(map,4,8,TerrainType.DEEPFOREST);
		setTerrain(map,4,9,TerrainType.DEEPFOREST);
		setTerrain(map,4,10,TerrainType.DEEPFOREST);
		setTerrain(map,5,7,TerrainType.DEEPFOREST);
		setTerrain(map,5,8,TerrainType.DEEPFOREST);
		setTerrain(map,5,9,TerrainType.DEEPFOREST);
		setTerrain(map,5,10,TerrainType.DEEPFOREST);
		setTerrain(map,6,6,TerrainType.DEEPFOREST);
		setTerrain(map,6,7,TerrainType.DEEPFOREST);
		setTerrain(map,6,8,TerrainType.DEEPFOREST);
		setTerrain(map,6,9,TerrainType.DEEPFOREST);
		setTerrain(map,6,10,TerrainType.DEEPFOREST);

		setRoads(map,0,7,(new int[] {0,1,0,1,0,0}));
		setRoads(map,0,8,(new int[] {1,0,1,0,0,0}));
		setRoads(map,1,8,(new int[] {0,0,1,0,0,1}));
		setRoads(map,2,9,(new int[] {0,0,1,0,0,1}));
		setRoads(map,3,0,(new int[] {3,0,3,0,0,0}));
		setRoads(map,4,1,(new int[] {0,0,3,0,0,3}));
		setRoads(map,5,1,(new int[] {0,0,0,0,3,3}));
		setRoads(map,4,2,(new int[] {0,3,0,3,0,0}));
		setRoads(map,4,3,(new int[] {3,0,0,3,0,1}));
		setRoads(map,4,4,(new int[] {3,0,3,0,0,0}));
		setRoads(map,5,4,(new int[] {0,0,0,1,3,3}));
		setRoads(map,4,5,(new int[] {0,3,0,0,3,0}));
		setRoads(map,3,5,(new int[] {0,3,0,0,3,0}));
		setRoads(map,2,6,(new int[] {0,3,0,0,3,0}));
		setRoads(map,0,4,(new int[] {0,1,1,0,0,0}));
		setRoads(map,1,3,(new int[] {0,1,0,1,1,0}));
		setRoads(map,2,3,(new int[] {0,1,0,0,1,0}));
		setRoads(map,3,2,(new int[] {0,0,1,0,1,0}));
		setRoads(map,1,4,(new int[] {1,0,0,1,0,1}));
		setRoads(map,1,5,(new int[] {1,0,0,1,0,0}));
		setRoads(map,1,6,(new int[] {1,3,3,0,1,0}));
		setRoads(map,2,7,(new int[] {0,0,3,0,0,3}));
		setRoads(map,3,7,(new int[] {0,1,0,3,0,3}));
		setRoads(map,4,7,(new int[] {0,1,0,0,1,0}));
		setRoads(map,5,5,(new int[] {1,0,0,1,0,0}));
		setRoads(map,5,6,(new int[] {1,0,0,0,1,0}));
		setRoads(map,3,8,(new int[] {3,0,0,3,0,0}));
		setRoads(map,3,9,(new int[] {3,0,0,3,0,1}));
		setRoads(map,3,10,(new int[] {3,0,0,3,0,0}));
		
		
		setRivers(map,5,0, (new int[]{2,2,0,0,0,2}));
		setRivers(map,5,1, (new int[]{0,0,2,2,0,0}));
		setRivers(map,5,2, (new int[]{0,0,2,2,0,0}));
		setRivers(map,5,3, (new int[]{0,0,2,2,0,0}));
		setRivers(map,5,4, (new int[]{0,0,2,2,0,0}));
		setRivers(map,5,5, (new int[]{0,0,3,3,0,0}));
		setRivers(map,5,6, (new int[]{0,0,3,3,0,0}));
		setRivers(map,5,7, (new int[]{0,0,3,3,0,0}));
		setRivers(map,5,8, (new int[]{0,0,3,3,0,0}));
		setRivers(map,5,9, (new int[]{0,0,0,0,4,4}));
		setRivers(map,4,10, (new int[]{0,0,0,0,4,4}));
		setRivers(map,3,10, (new int[]{0,0,4,4,0,0}));
		
		return map;
	}
	
	private void setTerrain(CampaignMap map, int x, int y, TerrainType terrainType)
	{
		setTerrain(map,x,y,terrainType,SettlementType.NONE,"");
	}

	private void setTerrain(CampaignMap map, int x, int y, TerrainType terrainType, SettlementType settlementType, String settlementName)
	{
		CampaignMapHex mapHex = (CampaignMapHex)map.getHex(x,y);
		mapHex.setTerrainType(terrainType);
		mapHex.setSettlement(settlementType, settlementName);
	}
	
	private void setRoads(CampaignMap map, int x, int y, int[] roads)
	{
		CampaignMapHex mapHex = (CampaignMapHex)map.getHex(x,y);
		mapHex.setRoad(roads);
	}

	private void setRivers(CampaignMap map, int x, int y, int[] rivers)
	{
		CampaignMapHex mapHex = (CampaignMapHex)map.getHex(x,y);
		mapHex.setRiver(rivers);
	}

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			CampaignMapRenderTest client = new CampaignMapRenderTest();
			client.setVisible(true);
		} 
		catch (Exception e)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(e));
		}

	}
}
