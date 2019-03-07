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

package bt.ui.renderers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import bt.mapping.Board;
import bt.mapping.MapHex;
import bt.mapping.campaign.CampaignBoard;
import bt.mapping.campaign.CampaignMapHex;


public class CampaignBoardRenderer extends HexBoardRenderer
{
	private CampaignBoard _Board = null;

	public CampaignBoardRenderer()
	{
		System.out.println("Constructing CenturionBoardRenderer");
	}
	
	public CampaignBoard getCampaignBoard()
	{
		return _Board;
	}

	@Override
	public HexBoardHex getBoardHex(MapHex theMapHex, int boardX, int boardY)
	{
		return new CampaignBoardHex(this, (CampaignMapHex) theMapHex, boardX, boardY);
	}
	
	@Override
	protected void setBoard(Board board)
	{
		_Board = (CampaignBoard) board;
		super.setBoard(board);
	}

	/*
	 * Register all the other elements loaded by the board
	 */
	public void registerCursors()
	{
	}

	@Override
	public BufferedImage getTerrainImage(MapHex mapHex)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<BufferedImage> getFeatureImages(MapHex mapHex)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getTextColour(MapHex mapHex)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getHexBorderColour()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
