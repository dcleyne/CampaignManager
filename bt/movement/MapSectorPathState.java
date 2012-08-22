package bt.movement;

import java.util.Iterator;
import java.util.Vector;

import bt.elements.galaxy.PlanetMap;
import bt.elements.galaxy.PlanetMapSector;
import bt.elements.galaxy.WorldMapNavigation;
import bt.util.ai.aState;

public class MapSectorPathState extends aState
{
	private int m_SectorNumber;
	private PlanetMap m_PlanetMap;
	private WorldMapNavigation m_Navigation;
	private double m_DistanceFromStart;
	private int m_GoalSector;
	private double m_DistanceToGoal;

	public MapSectorPathState(int sectorNumber, PlanetMap m_Map, WorldMapNavigation wmn, double distanceFromStart, int goalSector)
	{
		m_SectorNumber = sectorNumber;
		m_PlanetMap = m_Map;
		m_Navigation = wmn;
		m_DistanceFromStart = distanceFromStart;
		m_GoalSector = goalSector;
		m_DistanceToGoal = getDistanceToGoal();
	}
	
	public int getSectorNumber()
	{ return m_SectorNumber; }
	
	@Override
	public boolean done()
	{
		return m_SectorNumber == m_GoalSector;
	}

	@Override
	public boolean equals(aState state)
	{
		if (state instanceof MapSectorPathState )
		{
			MapSectorPathState msps = (MapSectorPathState)state;
			return m_SectorNumber == msps.m_SectorNumber && m_DistanceFromStart == msps.m_DistanceFromStart;
		}
		return false;
	}

	@Override
	public double getDistFromStart()
	{
		return m_DistanceFromStart;
	}

	@Override
	public double getDistToGoal()
	{
		return m_DistanceToGoal;
	}

	@Override
	public Object getKey()
	{
		return new Long(m_SectorNumber);
	}

	@Override
	public Iterator<aState> neighbors()
	{
		Vector<aState> neighbours = new Vector<aState>();
		
		for (int i = 1; i < 7; i++)
		{
			int neighbour = m_Navigation.getHexNeighbour(m_SectorNumber,i);			
			double calcDist = calcDistance(m_SectorNumber,neighbour,true);
			if (calcDist < Integer.MAX_VALUE)
			{
				double distance = m_DistanceFromStart + calcDist;
				MapSectorPathState n = new MapSectorPathState(neighbour,m_PlanetMap,m_Navigation,distance,m_GoalSector);
				neighbours.add(n);
			}
		}
		
		return neighbours.iterator();
	}
	

	private double getDistanceToGoal()
	{
		double distance = 0.0;
		
		int[] path1 = m_Navigation.getShortestSectorPath((int)m_SectorNumber, (int)m_GoalSector, true);
		int[] path2 = m_Navigation.getShortestSectorPath((int)m_SectorNumber, (int)m_GoalSector, false);
		int[] path;
		if (path1.length < path2.length)
			path = path1;
		else
			path = path2;
		
		for (int i = 0; i < path.length - 1; i++)
		{
			distance += calcDistance(path[i],path[i+1],false);
		}		
		
		return distance * 0.5;
	}
	
	private double calcDistance(int sec1, int sec2, boolean includeRoads)
	{
		double distance = Integer.MAX_VALUE;
		
		PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(sec1);
		PlanetMapSector pms2 = m_PlanetMap.getPlanetSector(sec2);
		
		int elevationDiff = Math.abs(pms1.getMeanAltitude() - pms2.getMeanAltitude());
		int elevationCost = elevationDiff / 500; 

		if (includeRoads && pms2.hasRoad())
			distance = 1;
		else
		{
			int terrainCost = pms2.getTerrainType().movementCost();
			if (terrainCost < Integer.MAX_VALUE)
				distance = terrainCost + elevationCost;
			else
				distance = Integer.MAX_VALUE; 
		}
		return distance;
	}
	
}
