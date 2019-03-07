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

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

import bt.mapping.campaign.CampaignMapHex;

public class CampaignBoardHex extends HexBoardHex
{

	public CampaignBoardHex(HexBoardRenderer renderer, CampaignMapHex theMapHex, int boardX, int boardY)
	{
		super(renderer, theMapHex, boardX, boardY);

	}

	protected CampaignMapHex getMapHex()
	{
		return (CampaignMapHex) _MapHex;
	}

	@Override
	public synchronized void draw(Graphics2D g, ImageObserver obs)
	{
		super.draw(g, obs);
	}

}
