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
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import bt.elements.campaign.Campaign;
import bt.elements.campaign.Force;
import bt.elements.campaign.Situation;
import bt.elements.campaign.SituationIntendedMovement;
import bt.elements.campaign.SituationUnitLocation;
import bt.mapping.Board;
import bt.mapping.Coordinate;
import bt.mapping.HexGrid;
import bt.mapping.Hexagon;
import bt.mapping.MapHex;
import bt.mapping.TerrainFactory;
import bt.mapping.campaign.CampaignBoard;
import bt.mapping.campaign.CampaignMapHex;
import bt.ui.sprites.CombatUnitCounter;
import bt.ui.sprites.HexStraightArrowSprite;


public class CampaignBoardRenderer extends HexBoardRenderer
{
	private CampaignBoard _Board = null;

	public CampaignBoardRenderer()
	{
		System.out.println("Constructing CampaignBoardRenderer");
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
		return TerrainFactory.INSTANCE.getTerrainImage(mapHex.getTerrainType());
	}

	@Override
	public ArrayList<BufferedImage> getFeatureImages(MapHex mapHex)
	{
		return new ArrayList<BufferedImage>();
	}

	@Override
	public Color getTextColour(MapHex mapHex)
	{
		return TerrainFactory.INSTANCE.getTextColor(mapHex.getTerrainType());
	}

	@Override
	public Color getHexBorderColour()
	{
		return Color.BLACK;
	}

	@Override
	public void draw(Graphics2D g, ImageObserver obs)
	{
		super.draw(g, obs);

		Rectangle2D clipRect = g.getClipBounds();

		HexGrid grid = _Board.getHexGrid();
		for (int i = 0; i < grid.getSize(); i++)
		{
			CampaignBoardHex bh = (CampaignBoardHex)getBoardHex(i);
			if (bh.intersects(clipRect))
			{
				bh.drawRivers(g);
				bh.drawRoads(g);
				bh.drawSettlement(g);
				bh.drawLabel(g);
			}
		}

		getSpriteManager().drawSprites(g, obs, clipRect.getBounds());
	}
	
	public void setSituation(Campaign campaign, Situation situation)
	{
		getSpriteManager().clearAllSprites();
		
		for (SituationUnitLocation sul: situation.getUnitLocations())
		{
			String unitName = sul.getUnitName();
			Force force = campaign.getForceForUnit(unitName);
			Color unitColor = force.getColor();
			Coordinate coord = new Coordinate(sul.getCoordinate().x - 1, sul.getCoordinate().y - 1);
			CombatUnitCounter unitCounter = new CombatUnitCounter(this, coord, unitColor, unitName, force.getAbbreviation());
			unitCounter.setVisible(true);
			getSpriteManager().registerElement(unitCounter);				
		}
		
		for (SituationIntendedMovement sim: situation.getUnitMovements())
		{
			SituationUnitLocation sul = situation.getUnitLocation(sim.getUnitName());
			Coordinate startCoord = sul.getCoordinate();
			Coordinate endCoord = sim.getDestination();
			
			Hexagon startHex = _Board.getHexGrid().getHex(startCoord.x - 1, startCoord.y - 1);
			Hexagon endHex = _Board.getHexGrid().getHex(endCoord.x - 1, endCoord.y - 1);
			HexStraightArrowSprite hsas = new HexStraightArrowSprite(this, startHex, endHex, false, Color.CYAN, Color.BLACK);
			hsas.setVisible(true);
			getSpriteManager().registerWidget(hsas);				
		}

	}
	
	public BufferedImage renderImage()
	{
		Dimension size = getSize();
		BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2d = image.createGraphics();
		g2d.setClip(new Rectangle(0,0,image.getWidth(),image.getHeight()));
		draw(g2d, null);
		
		return image;
	}

}
