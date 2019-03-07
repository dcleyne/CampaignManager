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
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import bt.mapping.Coordinate;
import bt.mapping.TerrainType;
import bt.mapping.campaign.CampaignBoard;
import bt.mapping.campaign.CampaignMap;
import bt.ui.controls.ZoomSlider;
import bt.ui.panels.BoardPanel;
import bt.ui.renderers.CampaignBoardRenderer;
import bt.ui.renderers.MapFactory;
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

			
			//_Board = (CampaignBoard) MapFactory.INSTANCE.getBoard("Campaign", "MapBoard2.board");
			_Board = constructBoard();
			
			
			_BoardRenderer = (CampaignBoardRenderer) MapFactory.INSTANCE.createBoardRenderer(_Board.getMapType());
			_BoardRenderer.setBoard(_BoardPanel, _Board);
			slider.setBoardRenderer(_BoardRenderer);

		} 
		catch (Exception e)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(e));
			System.exit(0);
		}
	}
	
	private CampaignBoard constructBoard() throws Exception
	{
		CampaignBoard board = new CampaignBoard();
		board.setName("Example");
		board.addMap(constructMap(), new Coordinate(1,1));
		board.completedAddingMaps();
		return board;
	}
	
	private CampaignMap constructMap()
	{
		CampaignMap map = new CampaignMap(7,11);
		map.createBlankMap(TerrainType.DESERT);
		
		return map;
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
