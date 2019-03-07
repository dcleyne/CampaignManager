package bt.mapping.planet;

import java.util.Vector;

import bt.mapping.TerrainType;
import bt.util.Dice;

public class WorldTerrainGenerator
{
    private PlanetMap m_PlanetMap;
    private WorldMapNavigation m_WorldMapNavigation = new WorldMapNavigation();

    public WorldTerrainGenerator(int worldSize)
    {
        m_WorldMapNavigation.initialiseMap(worldSize);
        m_PlanetMap = new PlanetMap(m_WorldMapNavigation.getMapSectorCount());        
    }
    
    public WorldMapNavigation getWorldMapNavigation()
    { return m_WorldMapNavigation; }
    
    public PlanetMap getPlanetMap()
    {
    	return m_PlanetMap;
    }
    
    public void testRings(int sector, int distance)
    {
    	Vector<Integer> sectors = m_WorldMapNavigation.getSurroundingRing(sector,distance);
    	
    	m_PlanetMap.getPlanetSector(sector).setTerrainType(TerrainType.VOLCANO);
    	
    	for (Integer i: sectors) 
        	m_PlanetMap.getPlanetSector(i).setTerrainType(TerrainType.PLAINS);
    }

    public void testSurrounds(int sector, int distance)
    {
    	Vector<Integer> sectors = m_WorldMapNavigation.getSurroundingSectors(sector,distance);
    	
    	m_PlanetMap.getPlanetSector(sector).setTerrainType(TerrainType.VOLCANO);
    	
    	for (Integer i: sectors) 
        	m_PlanetMap.getPlanetSector(i).setTerrainType(TerrainType.PLAINS);
    }
    
    public void testRivers(int sector, int size)
    {
    	m_PlanetMap.getPlanetSector(sector).setTerrainType(TerrainType.PLAINS);
    	Vector<Integer> sectors = m_WorldMapNavigation.getSurroundingSectors(sector,1);
    	for (Integer i: sectors) 
        	m_PlanetMap.getPlanetSector(i).setTerrainType(TerrainType.PLAINS);
    	
    	PlanetMapSector pms = m_PlanetMap.getPlanetSector(sector);
    	for (int i = 0; i < 6; i++)
    		pms.setRiver(i, size);
    }
    
    public void testRiverGeneration()
    {
    	m_PlanetMap.getPlanetSector(499).setTerrainType(TerrainType.MOUNTAINS);
    	m_PlanetMap.getPlanetSector(500).setTerrainType(TerrainType.MOUNTAINS);    	
    	
    	m_PlanetMap.getPlanetSector(513).setTerrainType(TerrainType.MOUNTAINS);
    	m_PlanetMap.getPlanetSector(514).setTerrainType(TerrainType.MOUNTAINS);
    	m_PlanetMap.getPlanetSector(515).setTerrainType(TerrainType.MOUNTAINS);
    	m_PlanetMap.getPlanetSector(528).setTerrainType(TerrainType.MOUNTAINS);
    	m_PlanetMap.getPlanetSector(529).setTerrainType(TerrainType.HILLS);
    	m_PlanetMap.getPlanetSector(530).setTerrainType(TerrainType.MOUNTAINS);
    	m_PlanetMap.getPlanetSector(544).setTerrainType(TerrainType.MOUNTAINS);
    	m_PlanetMap.getPlanetSector(545).setTerrainType(TerrainType.PLAINS);
    	m_PlanetMap.getPlanetSector(546).setTerrainType(TerrainType.MOUNTAINS);
    	m_PlanetMap.getPlanetSector(561).setTerrainType(TerrainType.MOUNTAINS);
    	m_PlanetMap.getPlanetSector(562).setTerrainType(TerrainType.PLAINS);
    	m_PlanetMap.getPlanetSector(563).setTerrainType(TerrainType.MOUNTAINS);
    	
    	m_PlanetMap.getPlanetSector(580).setTerrainType(TerrainType.MOUNTAINS);
    	m_PlanetMap.getPlanetSector(581).setTerrainType(TerrainType.PLAINS);
    	m_PlanetMap.getPlanetSector(582).setTerrainType(TerrainType.MOUNTAINS);
    	
    	m_PlanetMap.getPlanetSector(600).setTerrainType(TerrainType.MOUNTAINS);
    	m_PlanetMap.getPlanetSector(601).setTerrainType(TerrainType.WATER);
    	m_PlanetMap.getPlanetSector(602).setTerrainType(TerrainType.MOUNTAINS);
    	
    	m_PlanetMap.getPlanetSector(499).setMeanAltitude(20000);
    	m_PlanetMap.getPlanetSector(500).setMeanAltitude(20000);

    	m_PlanetMap.getPlanetSector(513).setMeanAltitude(20000);
    	m_PlanetMap.getPlanetSector(514).setMeanAltitude(20000);
    	m_PlanetMap.getPlanetSector(515).setMeanAltitude(20000);
    	m_PlanetMap.getPlanetSector(528).setMeanAltitude(20000);
    	m_PlanetMap.getPlanetSector(530).setMeanAltitude(20000);
    	m_PlanetMap.getPlanetSector(544).setMeanAltitude(20000);
    	m_PlanetMap.getPlanetSector(546).setMeanAltitude(20000);
    	m_PlanetMap.getPlanetSector(561).setMeanAltitude(20000);
    	m_PlanetMap.getPlanetSector(563).setMeanAltitude(20000);
    	m_PlanetMap.getPlanetSector(580).setMeanAltitude(20000);
    	m_PlanetMap.getPlanetSector(582).setMeanAltitude(20000);
    	m_PlanetMap.getPlanetSector(600).setMeanAltitude(20000);
    	m_PlanetMap.getPlanetSector(602).setMeanAltitude(20000);

    	m_PlanetMap.getPlanetSector(529).setMeanAltitude(2000);
    	m_PlanetMap.getPlanetSector(545).setMeanAltitude(1500);
    	m_PlanetMap.getPlanetSector(562).setMeanAltitude(1000);
    	m_PlanetMap.getPlanetSector(581).setMeanAltitude(500);
    	m_PlanetMap.getPlanetSector(601).setMeanAltitude(-200);
    	
    	Vector<Integer> riverRegister = new Vector<Integer>();
    	generateRiver(529, 514, riverRegister);
    }
    
    public void generateRandomPlanet(int meanTemperatureAtSeaLevel)
    {
    	int continents = (Dice.d6(2) / 2) + 2;
    	int currentContinent = 0;
    	System.out.println("Generating Planet with " + Integer.toString(continents) + " continent" + (continents != 1 ? "s" : ""));
    	
    	Vector<Integer> usedSectors = new Vector<Integer>();

    	int count = 0;
    	while (currentContinent < continents)
    	{
    		count++;
    		System.out.println("Generating continent " + Integer.toString(count));

    		int segment = Dice.d10(1);
    		while (usedSectors.contains(segment))
    			segment = Dice.d10(1);
    		
    		usedSectors.add(segment);
    		
    		int location = m_WorldMapNavigation.getSegmentStart(segment) + Dice.random(m_WorldMapNavigation.getSegmentSize());
    		int width = m_WorldMapNavigation.getMapFaceLength();
    		int height = m_WorldMapNavigation.getMapFaceLength();
    		switch (Dice.d6(1))
    		{
	    		case 1: width = (int)(width * 0.5); break;
	    		case 2: width = (int)(width * 0.75); break;
	    		case 4: width = (int)(width * 1.25); break;
	    		case 5: width = (int)(width * 1.5); break;
	    		case 6: width = (int)(width * 2.0); break;
    		}
    		switch (Dice.d6(1))
    		{
	    		case 1: height = (int)(height * 0.5); break;
	    		case 2: height = (int)(height * 0.75); break;
	    		case 4: height = (int)(height * 1.25); break;
	    		case 5: height = (int)(height * 1.5); break;
	    		case 6: height = (int)(height * 2.0); break;
    		}
    		boolean ok = generateRandomContinent(location,width,height);
    		if (ok)
    			currentContinent++;
    		else
    		{
    			ClearTerrain();
    			currentContinent = 0;
    			usedSectors.clear();
            	System.out.println("Generating continent FAILED! Resetting map!");
            	count = 0;
    		}    		
    	}
    	finishGeneration(meanTemperatureAtSeaLevel);
    }

    public boolean generateRandomContinent(int location, int width, int height)
    {
    	System.out.println("Generating Continent Outline");
    	Vector<PlanetMapSector> sectorsInContinent = generateWater(location,width,height);

    	System.out.println("Solidifying Continent Outline");
    	if (!generateDeepSaltwater(location,sectorsInContinent))
    		return false;    	

    	System.out.println("Filling In Grassland");
    	fillContinentWithPlains(sectorsInContinent);
    	if (sectorsInContinent.size() > (width * height))
    		return false;

    	System.out.println("Adding Mountains");
    	generateMountainRanges(sectorsInContinent, width, height);

    	System.out.println("Adding Hills");
    	generateHills(sectorsInContinent, width, height);
    	
    	System.out.println("Generating Basic elevation");
    	generateElevation(sectorsInContinent, width, height);

    	System.out.println("Generating Lakes");
    	generateLakes(sectorsInContinent, width, height);

    	System.out.println("Generating Rivers");
    	generateRiversAndFreshWaterAndSwamp(sectorsInContinent, width, height);
    	
    	System.out.println("Generating Extra Terrain");
    	generateOtherTerrain(sectorsInContinent, width, height);

    	System.out.println("Generating Forests");
    	generateForests(sectorsInContinent, width, height);
    	
    	System.out.println("Completed Continent");
        return true;
    }
    
    public void finishGeneration(int meanTemperatureAtSeaLevel)
    {    	
    	System.out.println("Filling in blank sectors with ocean");
    	
        int Size = m_WorldMapNavigation.getMapSectorCount();
        //Fill in the blank spaces with deep water and set an initial elevation
        for (int i = 1; i <= Size; i++)
        {
            PlanetMapSector wsd = m_PlanetMap.getPlanetSector(i);
            if (wsd.getTerrainType() == TerrainType.NONE)
            {
            	wsd.setTerrainType(TerrainType.DEEPWATER);
            	wsd.setMeanAltitude(- ( Dice.random( 4 ) * 200 ) - 2000);
            }
        }
        System.out.println("Cleaning Up Stray Water");
        //Fill in the blank spaces with deep water and set an initial elevation
        for (int i = 1; i <= Size; i++)
        {
            PlanetMapSector wsd = m_PlanetMap.getPlanetSector(i);
            if (wsd.getTerrainType() == TerrainType.WATER)
            {
            	boolean remove = true;
				Vector<Integer> sectors = m_WorldMapNavigation.getSurroundingSectors(wsd.getSectorNumber(),1);
				for (Integer sec: sectors)
				{
					TerrainType tt = m_PlanetMap.getPlanetSector(sec).getTerrainType();
					if (tt != TerrainType.DEEPWATER && tt != TerrainType.WATER)
					{
						remove = false;
						break;
					}
				}
            	if (remove)
            	{
	            	wsd.setTerrainType(TerrainType.DEEPWATER);
	            	wsd.setMeanAltitude(- ( Dice.random( 4 ) * 200 ) - 2000);
            	}
            }
        }
        
        
    	System.out.println("Generating Temperatures");
        generateTemperature(meanTemperatureAtSeaLevel);
        
        m_PlanetMap.recalculateAverages();

    	System.out.println("Final Map Preparation Complete");
    }

    public void ClearTerrain()
    {

        int Size = m_WorldMapNavigation.getMapSectorCount();
        //Initial Random generate
        for (int i = 0; i < Size; i++)
        {
            PlanetMapSector wsd = m_PlanetMap.getPlanetSector(i);
            if (wsd != null)
            {
            	wsd.setTerrainType(TerrainType.NONE);
            	wsd.clearRivers();
            	wsd.clearRoads();
            	wsd.clearCrossings();
            }
        }
    }

    
    private Vector<PlanetMapSector> generateWater(int coord, int width, int height )
    {
    	Vector<PlanetMapSector> sectorsInContinent = new Vector<PlanetMapSector>();
    	
    	int centrePoint = coord;
    	int guidePoint = coord;
    	int startPoint = -1;
    	int guidePointX = 0;
    	int guidePointY = 0;
    	int currentPoint = coord;
    	int tempPoint;
    	int XDistance;
    	int YDistance;
    	double radians = 0.0;
    	//double iterations = Math.max(width, height) * 9/10 * Math.PI * 4;
    	double iterations = Math.max(width, height) * Math.PI * 3;
    	double thresholdFactor = 0.95;
    	double threshold = iterations * thresholdFactor; 

    	int[] randomVote = new int[6];
    	int[] closestVote = new int[6];
    	int randomDirection;
    	int closestDirection;

    	double skewRadians1 = Dice.d100( 10 ) / (double)Dice.d10( 10 );
    	double skewRadians2 = Dice.d100( 10 ) / (double)Dice.d10( 10 );
    	double skewRadians3 = Dice.d100( 10 ) / (double)Dice.d10( 10 );
    	double skewRadians4 = Dice.d100( 10 ) / (double)Dice.d10( 10 );
    	double skewOffset1 = Dice.d100( 10 ) / (double)Dice.d10( 10 );
    	double skewOffset2 = Dice.d100( 10 ) / (double)Dice.d10( 10 );
    	double skewOffset3 = Dice.d100( 10 ) / (double)Dice.d10( 10 );
    	double skewOffset4 = Dice.d100( 10 ) / (double)Dice.d10( 10 );
    	double skewScale1 = Dice.d10( 10 ) / 100.0;
    	double skewScale2 = Dice.d10( 10 ) / 100.0;
    	double skewScale3 = Dice.d10( 10 ) / 100.0;
    	double skewScale4 = Dice.d10( 10 ) / 100.0;
    	
    	m_PlanetMap.getPlanetSector(centrePoint).setTerrainType(TerrainType.VOLCANO);

    	for ( double i = 0; i < iterations; i++ )
    	{
    		radians += 2.0 * Math.PI / iterations * 1.05;
    		XDistance = (int)Math.round(Math.cos( radians ) * width  / 2 * 15/20
    			+ Math.sin( radians * skewRadians1 + skewOffset1 ) * width / 2 * 3/20 * skewScale1
    			+ Math.cos( radians * skewRadians2 + skewOffset2 ) * width / 2 * 2/20 * skewScale2);
    		YDistance = (int)Math.round(Math.sin( radians ) * height / 2 * 15/20
    			+ Math.cos( radians * skewRadians3 + skewOffset3 ) * width / 2 * 3/20 * skewScale3
    			+ Math.sin( radians * skewRadians4 + skewOffset4 ) * width / 2 * 2/20 * skewScale4);
    		
    		guidePoint = m_WorldMapNavigation.getHexOffsetByColsRows(centrePoint,XDistance,YDistance);
    		guidePointX = XDistance;
    		guidePointY = YDistance;
    		if (startPoint == -1)
    			startPoint = guidePoint;
    		
    		if ( i > ( threshold ) )
    		{
    			XDistance = (int)Math.round( guidePointX * ( 1 - ( i / iterations - thresholdFactor ) * 10 ));
    			YDistance = (int)Math.round( guidePointY * ( 1 - ( i / iterations - thresholdFactor ) * 10 ));
    			guidePoint = m_WorldMapNavigation.getHexOffsetByColsRows(startPoint,XDistance,YDistance);
    		}

    		if ( i == 0 )
    		{
    			PlanetMapSector pms = m_PlanetMap.getPlanetSector(guidePoint); 
    			pms.setTerrainType(TerrainType.WATER);
    			sectorsInContinent.add(pms);
    			currentPoint = guidePoint;
    		}

    		randomVote[ 0 ] = 0;
    		closestVote[ 0 ] = 0;
    		randomDirection = 0;
    		closestDirection = 0;
    		for ( int direction = 0; direction < 6; direction++ )
    		{
    			tempPoint = m_WorldMapNavigation.getHexNeighbour(currentPoint, direction + 1);
    			if ( m_PlanetMap.getPlanetSector(tempPoint).getTerrainType( ) != TerrainType.NONE )
    				randomVote[ direction ] = 0;
    			else
    			{
    				randomVote[ direction ] = Dice.d100( (int)(Math.sqrt( height * width ) / 50.0) ) + Dice.d10( 16 ) -
    					(int)Math.pow(m_WorldMapNavigation.getSectorPathLength(tempPoint,guidePoint),2);
    				if ( randomVote[ direction ] < 0 )
    					randomVote[ direction] = 0;
    			}

    			if ( randomVote[ direction ] > randomVote[ randomDirection ] )
    				randomDirection = direction;

    			closestVote[ direction ] = m_WorldMapNavigation.getSectorPathLength(tempPoint,guidePoint);
    			if ( closestVote[ direction ] < closestVote[ closestDirection ] )
    				closestDirection = direction;
    		}

    		if ( randomDirection == 0 && randomVote[ 0 ] < 1 )
    			randomDirection = closestDirection;

    		currentPoint = m_WorldMapNavigation.getHexNeighbour(currentPoint, randomDirection + 1);
			PlanetMapSector pms = m_PlanetMap.getPlanetSector(currentPoint); 
			if (!sectorsInContinent.contains(pms))
			{
				sectorsInContinent.add(pms);
				pms.setTerrainType(TerrainType.WATER);
			}
    	}
    	
    	return sectorsInContinent;
    	
    }
    
    private void fillContinentWithPlains(Vector<PlanetMapSector> sectorsInContinent)
    {
    	Vector<PlanetMapSector> plains = new Vector<PlanetMapSector>();
    	for (PlanetMapSector sector: sectorsInContinent)
    	{
    		int secNum = sector.getSectorNumber();
    		boolean coastal = false;
    		for (int i = 1; i < 7; i++)
    		{
        		int neighbour = m_WorldMapNavigation.getHexNeighbour(secNum,i);
        		PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(neighbour);
    			if (pms1.getTerrainType() == TerrainType.DEEPWATER)
    			{
    				coastal = true;
    				break;
    			}
    		}
			if (!coastal)
				plains.add(sector);
    	}    	
    	for (PlanetMapSector sector: plains)
    		sector.setTerrainType(TerrainType.PLAINS);
    }
    
    private void addLocationToContinent(int location, Vector<PlanetMapSector> sectorsInContinent) throws StackOverflowError 
    {
    	PlanetMapSector pms = m_PlanetMap.getPlanetSector(location);
    	if (pms.getTerrainType() == TerrainType.NONE || pms.getTerrainType() == TerrainType.VOLCANO)
    	{
	    	if (!sectorsInContinent.contains(pms))
	    	{
	    		pms.setTerrainType(TerrainType.WATER);
	    		sectorsInContinent.add(pms);
	    		for (int i = 1; i < 7; i++)
	    		{
	    			int neighbour = m_WorldMapNavigation.getHexNeighbour(location,i);
	    			addLocationToContinent(neighbour,sectorsInContinent);
	    		}
	    	}
    	}
    }

    private boolean generateDeepSaltwater(int centreLocation, Vector<PlanetMapSector> sectorsInContinent)
    {
    	try
    	{
    		addLocationToContinent(centreLocation,sectorsInContinent);
    	}
    	catch (StackOverflowError ex)
    	{
    		return false;
    	}
    	
    	Vector<Integer> locationsToChange = new Vector<Integer>();
    	for (PlanetMapSector sector: sectorsInContinent)
    	{
    		int secNum = sector.getSectorNumber();
    		for (int i = 1; i < 7; i++)
    		{
        		int neighbour = m_WorldMapNavigation.getHexNeighbour(secNum,i);
        		PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(neighbour);
    			if (pms1.getTerrainType() == TerrainType.NONE)
    			{
    				int[] path = m_WorldMapNavigation.getSectorPath(neighbour, centreLocation);
    				boolean crossedWater = false;
    				for (int j = 0; j < path.length; j++)
    				{
						PlanetMapSector pms2 = m_PlanetMap.getPlanetSector(path[j]); 
    					if (pms2.getTerrainType() == TerrainType.WATER)
    						crossedWater = true;

    				}
    				if (crossedWater)
    					locationsToChange.add(neighbour);    					
    			}
    				
    		}    		
    	}
    	for (Integer loc : locationsToChange)
    	{
    		PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(loc);
			pms1.setTerrainType(TerrainType.DEEPWATER);
    		if (!sectorsInContinent.contains(pms1))
    		{
    			sectorsInContinent.add(pms1);
    		}
    	}
    	return true;
    }
    
    private PlanetMapSector getRandomSectorInContinent(Vector<PlanetMapSector> sectorsInContinent)
    {
    	int random = Dice.random(sectorsInContinent.size()) - 1;
    	if (random < 0)
    		random = 0;
    	return sectorsInContinent.elementAt(random);
    }
    
    private boolean sectorInContinent(Vector<PlanetMapSector> sectorsInContinent, int sector)
    {
    	for (PlanetMapSector pms: sectorsInContinent)
    		if (pms.getSectorNumber() == sector)
    			return true;
    	return false;
    }

    void generateMountainRanges(Vector<PlanetMapSector> sectorsInContinent, int width, int height)
    {
    	int numOfRanges = Dice.d10 ( (int)( Math.sqrt( height * width ) / 30.0 ) );
    	int sizeOfRange;
    	boolean validStart;
    	int currentPoint = 0;
    	int guidePoint;
    	int tempPoint = 0;
    	int[] randomVote = new int[6];
    	int randomDirection;

    	if (numOfRanges < 1)
    		numOfRanges = 1;
    	for ( int range = 0; range < numOfRanges; range++ )
    	{
    		int count = 0;
    		validStart = false;
    		while ( !validStart && count < 1000)
    		{
    			PlanetMapSector pms = getRandomSectorInContinent(sectorsInContinent); 
    			currentPoint = pms.getSectorNumber();
    			if ( pms.getTerrainType( ) == TerrainType.PLAINS )
    			{
    				boolean validSurround = true;
    				Vector<Integer> surround = m_WorldMapNavigation.getSurroundingSectors(currentPoint, 2);
    				for ( Integer sur: surround )
    				{
    					PlanetMapSector temppms = m_PlanetMap.getPlanetSector(sur);
    					if (!sectorInContinent(sectorsInContinent,sur) ||
    						 !( temppms.getTerrainType( ) == TerrainType.PLAINS ||
    								 temppms.getTerrainType( ) == TerrainType.HILLS ||
    								 temppms.getTerrainType( ) == TerrainType.MOUNTAINS ) )
    					{
    						validSurround = false;
    						break;
    					}
    				}
    				validStart = validSurround;
    			}
    			count++;
    		}
    		if (count >= 1000)
    		{
    			System.out.println("Abandoning placing mountains!");
    			numOfRanges = 0;
    			continue;
    		}

    		PlanetMapSector pms = m_PlanetMap.getPlanetSector(currentPoint);
    		pms.setTerrainType(TerrainType.MOUNTAINS);
    		
    		sizeOfRange = ( Dice.random( height ) + Dice.random( width ) ) / 3;

    		int xOff = Dice.random( sizeOfRange ) - sizeOfRange / 2;
    		int yOff = Dice.random( sizeOfRange ) - sizeOfRange / 2;
    		guidePoint = m_WorldMapNavigation.getHexOffsetByColsRows(currentPoint, xOff, yOff);

    		tempPoint = currentPoint;
    		
    		while ( sizeOfRange > 0)
    		{
    			randomVote[ 0 ] = 0;
    			randomDirection = 0;
    			for ( int direction = 0; direction < 6; direction++ )
    			{
    				tempPoint = m_WorldMapNavigation.getHexNeighbour(currentPoint, direction + 1);
					PlanetMapSector temppms = m_PlanetMap.getPlanetSector(tempPoint);
					if (!sectorInContinent(sectorsInContinent,tempPoint) ||
    					!( temppms.getTerrainType( ) == TerrainType.PLAINS ||
    							temppms.getTerrainType( ) == TerrainType.HILLS ||
    							temppms.getTerrainType( ) == TerrainType.MOUNTAINS ) )
    					randomVote[ direction ] = 0;
    				else
    				{
    					boolean validSurround = true;
        				Vector<Integer> surround = m_WorldMapNavigation.getSurroundingSectors(tempPoint, 2);
        				for ( Integer sur: surround )
        				{
        					temppms = m_PlanetMap.getPlanetSector(sur);
        					if (!sectorInContinent(sectorsInContinent,sur) ||
        						 !( temppms.getTerrainType( ) == TerrainType.PLAINS ||
        								 temppms.getTerrainType( ) == TerrainType.HILLS ||
        								 temppms.getTerrainType( ) == TerrainType.MOUNTAINS ) )
        					{
        						validSurround = false;
        						break;
        					}
        				}
    					if ( validSurround )
    					{
    						randomVote[ direction ] = Dice.d10( 20 ) - m_WorldMapNavigation.getSectorPathLength(tempPoint,guidePoint);
    						if ( randomVote[ direction ] < 0 )
    							randomVote[ direction] = 0;
    					}
    					else
    						randomVote[ direction ] = 0;
    				}
    				
    				if ( randomVote[ direction ] > randomVote[ randomDirection ] )
    					randomDirection = direction;
    			}

    			if ( randomDirection == 0 && randomVote[ 0 ] == 0 )
    				sizeOfRange = 0;
    			else
    			{
    				currentPoint = m_WorldMapNavigation.getHexNeighbour(currentPoint, randomDirection + 1);
    				PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(currentPoint);
    				if ( Dice.d10(1) == 1 )
    				{
    					if ( Dice.d10(1) == 1 )
    						pms1.setTerrainType( TerrainType.VOLCANO);
    					else
    						pms1.setTerrainType( TerrainType.HILLS );
    				}
    				else
    					pms1.setTerrainType( TerrainType.MOUNTAINS );

    				sizeOfRange--;
    			}
    		}
    	}
    }

    void generateHills(Vector<PlanetMapSector> sectorsInContinent, int width, int height)
    {
    	
    	int numOfHills = Dice.d10( (int)( Math.sqrt( height * width ) / 2.0 ) );

    	Vector<PlanetMapSector> eligibleSectors = new Vector<PlanetMapSector>();
    	for (PlanetMapSector pms : sectorsInContinent)
    	{
    		if ( pms.getTerrainType() == TerrainType.PLAINS )
    		{
    			int currentPoint = pms.getSectorNumber();
				boolean validSurround = false;
				for ( int tmp = 1; tmp < 7; tmp++ )
				{
					int tempPoint = m_WorldMapNavigation.getHexNeighbour(currentPoint, tmp);
					PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(tempPoint);
					if (!sectorInContinent(sectorsInContinent,tempPoint) ||
						 ( pms1.getTerrainType( ) == TerrainType.MOUNTAINS ||
						   pms1.getTerrainType( ) == TerrainType.VOLCANO ||
						   pms1.getTerrainType( ) == TerrainType.HILLS ) )
						validSurround = true;
				}
				if (validSurround)
	    			eligibleSectors.add(pms);
    		}
    	}
    		
    	if (eligibleSectors.size() < numOfHills)
    		numOfHills = eligibleSectors.size();
    	
    	while ( numOfHills > 0)
    	{
    		PlanetMapSector pms = getRandomSectorInContinent(eligibleSectors);
    		pms.setTerrainType( TerrainType.HILLS );
    		eligibleSectors.remove(pms);
    		
    		numOfHills--;
    	}
    	
    }

    private void generateElevation(Vector<PlanetMapSector> sectorsInContinent, int width, int height)
    {
    	// Note elevation is in increments of 200' from -25600 to 25400
    	int currentPoint = 0;

    	int elevation = 0;
    	int minElevation = 0;
    	int maxElevation = 0;
    	int ring;

    	for (PlanetMapSector sector : sectorsInContinent)
    	{
			switch ( sector.getTerrainType() )
			{
				case WATER:
					elevation = - ( Dice.random( 2 ) * 200 );
					minElevation = -2000;
					maxElevation = -200;
					break;
				case DEEPWATER:
					elevation = - ( Dice.random( 4 ) * 200 ) - 2000;
					minElevation = -25600;
					maxElevation = -2200;
					break;
				case PLAINS:
					elevation = 200;
					minElevation = 200;
					maxElevation = 8000;
					break;
				case HILLS:
					elevation = ( Dice.random( 2 ) * 200 ) + 600;
					minElevation = 800;
					maxElevation = 12000;
					break;
				case MOUNTAINS:
				case VOLCANO:
					elevation = ( Dice.random( 16) * 200 ) + 2800;
					minElevation = 3000;
					maxElevation = 25400;
					break;
				default:
			}

			for ( ring = 1; ring < 9; ring++ )
			{
				Vector<Integer> sectors = m_WorldMapNavigation.getSurroundingSectors(currentPoint,ring);
				for (Integer sec: sectors)
				{
					PlanetMapSector pms = m_PlanetMap.getPlanetSector(sec);
					switch ( pms.getTerrainType() )
					{
						case WATER:
							elevation -= 50 / ( ring * ring );
							break;
						case DEEPWATER:
							elevation -= 200 / ( ring * ring );
							break;
						case PLAINS:
							elevation += 50 / ( ring * ring );
							break;
						case HILLS:
							elevation += 200 / ( ring * ring );
							break;
						case MOUNTAINS:
							elevation += 1000 / ( ring * ring );
							break;
						default:
					}
				}
			}

			if ( elevation < minElevation )
				elevation = minElevation;
			else if ( elevation > maxElevation )
				elevation = maxElevation;

//			System.out.println("Setting elevation of sector " + Integer.toString(sector.GetSectorNumber()) + " to " + Integer.toString(elevation));
			sector.setMeanAltitude( elevation );
		}
    }
    

    void generateLakes(Vector<PlanetMapSector> sectorsInContinent, int width, int height)
    {
    	
    	int numOfLakes = Dice.d10( (int)(Math.sqrt( height * width ) / 50.0) );
    	boolean validStart;
    	int currentPoint = 0;
    	int tempPoint;
    	int tempPoint2;

    	while ( numOfLakes  > 0)
    	{
    		int count = 0;
    		validStart = false;
    		while (!validStart && count < 1000)
    		{
    			PlanetMapSector pms = getRandomSectorInContinent(sectorsInContinent); 
    			currentPoint = pms.getSectorNumber();
    			if ( pms.getTerrainType( ) == TerrainType.PLAINS )
    			{
    				boolean validSurround = true;
    				for ( int tmp = 1; tmp < 7; tmp++ )
    				{
    					tempPoint = m_WorldMapNavigation.getHexNeighbour(currentPoint, tmp);
    					PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(tempPoint);
    					if (!sectorInContinent(sectorsInContinent,tempPoint) ||
    						 pms1.getTerrainType( ) == TerrainType.WATER ||
    						 pms1.getTerrainType( ) == TerrainType.DEEPWATER )
    					{
    						validSurround = false;
    					}
    				}
    				validStart = validSurround;
    			}		
    			count++;
    		}
    		if (count >= 1000)
    		{
    			numOfLakes = 0;
    			continue;
    		}

    		PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(currentPoint);
    		pms1.setTerrainType( TerrainType.LAKE );
    		
    		for ( int tmp = 1; tmp < 7; tmp++ )
    		{
				tempPoint = m_WorldMapNavigation.getHexNeighbour(currentPoint, tmp);
				pms1 = m_PlanetMap.getPlanetSector(tempPoint);
				if (sectorInContinent(sectorsInContinent,tempPoint) &&
						pms1.getTerrainType( ) == TerrainType.PLAINS  && Dice.d10(1) <= 2)				
    			{
    				pms1.setTerrainType( TerrainType.LAKE );
    				for ( int tmp2 = 1; tmp2 < 7; tmp2++ )
    				{
    					tempPoint2 = m_WorldMapNavigation.getHexNeighbour(tempPoint, tmp2);
    					pms1 = m_PlanetMap.getPlanetSector(tempPoint2);
    					if (sectorInContinent(sectorsInContinent,tempPoint2) &&
    							pms1.getTerrainType( ) == TerrainType.PLAINS  && Dice.d10(1) == 1)				
    					{
    						pms1.setTerrainType( TerrainType.LAKE );
    					}
    				}
    			}
    		}
    		numOfLakes--;
    	}
    	
    }


    void generateOtherTerrain(Vector<PlanetMapSector> sectorsInContinent, int width, int height)
    {
    	
    	// Generate remaining as Grassland, Hills, and Swamp

    	int tempPoint;
    	int voteGrassland;
    	int voteHillyGrassland;
    	int voteSwamp;
    	
    	for (PlanetMapSector sector : sectorsInContinent)
    	{
			if ( sector.getTerrainType() == TerrainType.PLAINS )
			{
				voteGrassland = 0;
				voteHillyGrassland = 0;
				voteSwamp = 0;
				for ( int tmp = 1; tmp < 7; tmp++ )
				{
					tempPoint = m_WorldMapNavigation.getHexNeighbour(sector.getSectorNumber(), tmp);
					if ( sectorInContinent(sectorsInContinent,tempPoint) )
					{
						PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(tempPoint);
						switch ( pms1.getTerrainType() ) 
						{
							case PLAINS:
								voteGrassland += 6;
								voteHillyGrassland += 4;
								break;
							case HILLS:
								voteHillyGrassland += 6;
								voteGrassland += 4;
								break;
							case MOUNTAINS:
							case VOLCANO:
								voteHillyGrassland += 8;
								voteGrassland += 2;
								break;
							case SWAMP:
								voteSwamp += 8;
								voteGrassland += 2;
								break;
							case DEEPWATER:
								voteSwamp += 5;
								voteGrassland += 5;
								break;
							case WATER:
							case LAKE:
								voteSwamp += 7;
								voteHillyGrassland += 3;
							default:
						}
					}
				}

				voteGrassland += Dice.d10( 8 );
				voteHillyGrassland += Dice.d10( 8 );
				voteSwamp += Dice.d10( 8 );

				if ( voteGrassland >= voteHillyGrassland && voteGrassland >= voteSwamp )
					sector.setTerrainType( TerrainType.PLAINS );
				else if ( voteHillyGrassland >= voteSwamp )
					sector.setTerrainType( TerrainType.HILLS );
				else
					sector.setTerrainType( TerrainType.SWAMP );
			}
		}
    }

    
    private void generateRiversAndFreshWaterAndSwamp(Vector<PlanetMapSector> sectorsInContinent, int width, int height)
    {
    	int totalRivers = Dice.d10( (int)(Math.sqrt( (double)height * width ) / 25.0) );
    	if (totalRivers == 0)
    		totalRivers = 1;
    	int numOfRivers = totalRivers;

    	Vector<PlanetMapSector> eligibleSectors = new Vector<PlanetMapSector>();
    	for (PlanetMapSector pms : sectorsInContinent)
    	{
    		if (pms.getMeanAltitude() > 3000 &&
   			     ( pms.getTerrainType( ) == TerrainType.HILLS ||
      				   pms.getTerrainType( ) == TerrainType.MOUNTAINS ))
    			eligibleSectors.add(pms);
    	}
    		
    	if (eligibleSectors.size() < numOfRivers)
    		numOfRivers = eligibleSectors.size();
    	
    	int upstreamRiverCoordinate = 0; 

    	int selected;

    	Vector<Integer> riverRegister = new Vector<Integer>();

    	System.out.println("Attempting to create " + Integer.toString(numOfRivers) + " rivers");
    	while ( numOfRivers > 0)
    	{
    		// determine upstreamRiverCoordinate
    		int count = 0;
    		boolean riverStart = false;
    		while ( !riverStart && numOfRivers > 0 && count < 1000)
    		{
    			PlanetMapSector pms = getRandomSectorInContinent(eligibleSectors);
    			upstreamRiverCoordinate = pms.getSectorNumber();

    			Vector<Integer> surround = m_WorldMapNavigation.getSurroundingSectors(upstreamRiverCoordinate, 1);
				boolean validSurround = true;
				for ( Integer sec: surround )
				{
					PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(sec);
					if ( riverRegister.contains(sec) || pms1.getTerrainType( ) == TerrainType.WATER )
					{
						validSurround = false;
						break;
					}
				}
				
				eligibleSectors.remove(pms);
				if (eligibleSectors.size() < numOfRivers)
					numOfRivers = eligibleSectors.size();
				
				riverStart = validSurround;
				count++;
    		}
    		if (count >= 1000)
    		{
    			System.out.println("Abandoning River Placement");
    			numOfRivers = 0;
    			continue;
    		}

    		// determine currentRiverCoordinate
    		int currentRiverCoordinate = 0;
    		int currentElevation = 25400;
    		Vector<Integer> surround = m_WorldMapNavigation.getSurroundingSectors(upstreamRiverCoordinate, 1);
    		Vector<Integer> validSurround = new Vector<Integer>();
    		for ( Integer sur: surround)
    		{
    			PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(sur);
    			if ( pms1.getMeanAltitude( ) < currentElevation )
    			{
    				currentElevation = pms1.getMeanAltitude( );
    				validSurround.clear();
    				validSurround.add( sur );
    			}
    			else if ( pms1.getMeanAltitude( ) == currentElevation )
    			{
    				validSurround.add( sur );
    			}
    		}
    		selected = Dice.random( validSurround.size() ) - 1;
    		currentRiverCoordinate = validSurround.elementAt(selected);
    		
    		generateRiver(currentRiverCoordinate, upstreamRiverCoordinate, riverRegister);

    		numOfRivers--;
    	}
    }
    
    private void generateRiver(int startLocation, int upstreamCoordinate, Vector<Integer> riverRegister)
    {
    	int upstreamRiverCoordinate = 0;
    	int currentRiverCoordinate = 0;
    	int downstreamRiverCoordinate = 0;
    	int currentElevation = 0;
    	int downstreamElevation = 0;
    	int upstreamRiverPoint = 0;
    	int downstreamRiverPoint = 0;
    	boolean clockwise = true;
		int riverLength = 0;
		int riverSize = 1;

		currentRiverCoordinate = startLocation;
		currentElevation = m_PlanetMap.getPlanetSector(startLocation).getMeanAltitude();
		upstreamRiverCoordinate = upstreamCoordinate;
		
    	// determine upstreamRiverPoint
		for ( int i = 0; i < 6; i ++ )
		{
			int neighbour = m_WorldMapNavigation.getHexNeighbour(currentRiverCoordinate, i + 1); 
			if ( neighbour  == upstreamRiverCoordinate )
				upstreamRiverPoint = ( i + ( Dice.random( 2 ) - 1 ) ) % 6;
		}
    	
		Vector<Integer> visited = new Vector<Integer>();
		boolean riverFlowing = true;
		
		while ( riverFlowing )
		{
			// check if can join ocean
			if ( downstreamRiverCoordinate == 0 )
			{
	    		Vector<Integer> surround = m_WorldMapNavigation.getSurroundingSectors(currentRiverCoordinate, 1);
				for ( Integer sur: surround )
				{
					PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(sur);
					if ( pms1.getTerrainType() == TerrainType.WATER )
					{
						downstreamRiverCoordinate = sur;
						downstreamElevation = pms1.getMeanAltitude( );
						riverFlowing = false;
						for (int j = 0; j < 6; j ++ )
						{
							if ( m_WorldMapNavigation.getHexNeighbour(currentRiverCoordinate,j + 1) == downstreamRiverCoordinate )
							{
								clockwise = ( Dice.d10(1) > 5 );
								if ( clockwise )
									downstreamRiverPoint = j;
								else
									downstreamRiverPoint = ( j + 1 ) % 6;
							}
						}
					}
				}
			}

			// check if can join lake
			if ( downstreamRiverCoordinate == 0 )
			{
				Vector<Integer> surround = m_WorldMapNavigation.getSurroundingSectors(currentRiverCoordinate, 1);
				for ( Integer sur: surround )
				{
					PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(sur);
					if ( pms1.getTerrainType( ) == TerrainType.LAKE )
					{
						downstreamRiverCoordinate = sur;
						downstreamElevation = pms1.getMeanAltitude( );
						riverFlowing = false;
						for (int j = 0; j < 6; j ++ )
						{
							if ( m_WorldMapNavigation.getHexNeighbour(currentRiverCoordinate,j + 1) == downstreamRiverCoordinate )
							{
								clockwise = ( Dice.d10(1) > 5 );
								if ( clockwise )
									downstreamRiverPoint = j;
								else
									downstreamRiverPoint = ( j + 1 ) % 6;
							}
						}
					}
				}
			}

			// check if can join an existing river
			if ( downstreamRiverCoordinate == 0 )
			{
				Vector<Integer> surround = m_WorldMapNavigation.getSurroundingSectors(currentRiverCoordinate, 1);
				for ( Integer sur: surround )
				{
					PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(sur);
					if ( riverRegister.contains( sur ) && !visited.contains( sur ) &&
						 pms1.getMeanAltitude() <= currentElevation &&
						 pms1.getMeanAltitude() < downstreamElevation )
					{
						downstreamRiverCoordinate = sur;
						downstreamElevation = pms1.getMeanAltitude( );
						riverFlowing = false;
					}
				}
				// can join river
				if ( ! ( downstreamRiverCoordinate == 0 ) )
				{
					PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(downstreamRiverCoordinate);
					for (int j = 0; j < 6; j ++ )
					{
						if ( m_WorldMapNavigation.getHexNeighbour(currentRiverCoordinate,j + 1) == downstreamRiverCoordinate )
						{
							clockwise = ( Dice.d10(1) > 5 );
							if ( clockwise )
							{
								downstreamRiverPoint = j;
								int k = ( j + 4 ) % 6;
								if ( !(pms1.getRiver( ( k + 5 ) % 6 ) > 0))
								{
									for ( ; !(pms1.getRiver( k ) > 0); k = ( k + 1 ) % 6 )
									{
										pms1.setRiver( k, 1 );
									}
								}
							}
							else
							{
								downstreamRiverPoint = ( j + 1 ) % 6;
								int k  = ( j + 2 ) % 6;
								if ( !(pms1.getRiver( ( k + 1 ) % 6 ) > 0) )
								{
									for ( ; !(pms1.getRiver( k ) > 0); k = ( k + 5 ) % 6 )
									{
										pms1.setRiver( k, 1 );
									}
								}
							}
							// Increase size of downstream river
							boolean startFound = false;
							boolean endFound = false;
							int startIndex = 0;
							int endIndex = 0;
							for (int l = 0; l < riverRegister.size(); l++)
							{
								if (riverRegister.elementAt(l) == downstreamRiverCoordinate)
								{
									startIndex = l;
									startFound = true;
									break;
								}
								if (startFound)
								{
									if (riverRegister.elementAt(l) == 0)
									{
										endIndex = l;
										endFound = true;
										break;
									}
								}
								
							}
							if (startFound && endFound)
							{	    							
    							for (int k = startIndex; k <= endIndex; k++ )
    							{
    								for ( int l = 0; l < 6; l++ )
    								{
    									PlanetMapSector pmsRiver = m_PlanetMap.getPlanetSector(riverRegister.elementAt(k)); 
    									int rSize = pmsRiver.getRiver( l );
    									if ( rSize > 0 && riverSize < 3 )
    										pmsRiver.setRiver( l, riverSize + 1 );
    								}
    							}
							}
						}
					}
				}
			}

			// otherwise
			if ( downstreamRiverCoordinate == 0 )
			{
				downstreamElevation = 25400;
				Vector<Integer> surround = m_WorldMapNavigation.getSurroundingSectors(currentRiverCoordinate, 1);
				Vector<Integer> validSurround = new Vector<Integer> ();
				for ( Integer sur: surround )
				{
					if ( !visited.contains( sur ) )
					{
						PlanetMapSector pms = m_PlanetMap.getPlanetSector(sur);
						if ( pms.getMeanAltitude( ) < downstreamElevation )
						{
							downstreamElevation = pms.getMeanAltitude( );
							validSurround.clear();
							validSurround.add( sur );
						}
						else if ( pms.getMeanAltitude() == downstreamElevation )
						{
							validSurround.add( sur );
						}
					}
				}
				if ( validSurround.size() > 0 )
				{
					int selected = Dice.random( validSurround.size() ) - 1;
					downstreamRiverCoordinate = validSurround.elementAt( selected );
				}
				for ( int i = 0; i < 6; i ++ )
				{
					if ( m_WorldMapNavigation.getHexNeighbour(currentRiverCoordinate,i + 1) == downstreamRiverCoordinate )
					{
						
						// can go clockwise?
						boolean canGoClockwise = true;
						int lowerPoint = i;
						int upperPoint = upstreamRiverPoint;
						if ( lowerPoint < upperPoint )
							lowerPoint += 6;
						for (int j = upperPoint; j < lowerPoint; j++ )
						{
							int coord = m_WorldMapNavigation.getHexNeighbour(currentRiverCoordinate,j + 1);
							PlanetMapSector pms = m_PlanetMap.getPlanetSector(coord);
							if ( pms.getRiver( ( j + 3 ) % 6 ) > 0)
								canGoClockwise = false;
						}

						boolean canGoAntiClockwise = true;
						lowerPoint = i + 1;
						upperPoint = upstreamRiverPoint;
						if ( lowerPoint > upperPoint )
							upperPoint += 6;
					
						for (int j = upperPoint - 1; j > lowerPoint - 1; j-- )
						{
							int coord = m_WorldMapNavigation.getHexNeighbour(currentRiverCoordinate,j + 1);
							PlanetMapSector pms = m_PlanetMap.getPlanetSector(coord);
							if ( pms.getRiver( ( j + 3 ) % 6 ) > 0)
								canGoAntiClockwise = false;
						}

						if ( canGoClockwise && canGoAntiClockwise )
							clockwise = ( Dice.d10(1) > 5 );
						else if ( canGoClockwise )
							clockwise = true;
						else
							clockwise = false;

						if ( clockwise )
							downstreamRiverPoint = i;
						else
							downstreamRiverPoint = ( i + 1 ) % 6;
					}
				}
			}		

			//draw it

			// lake
			if ( currentElevation < downstreamElevation )
			{
				boolean goBack = false;
				for (int i = 0; i < 6; i++ )
				{
					int coord = m_WorldMapNavigation.getHexNeighbour(currentRiverCoordinate,i + 1);
					PlanetMapSector pms = m_PlanetMap.getPlanetSector(coord);
					if ( pms.getRiver( ( i + 3 ) % 6 ) > 0)
						goBack = true;
				}

				if ( goBack )
				{
					PlanetMapSector goBackSector = m_PlanetMap.getPlanetSector(upstreamRiverCoordinate);
					for (int i = 0; i < 6; i++ )
					{    						
						goBackSector.setRiver( i, 0 );
					}
					goBackSector.setTerrainType( TerrainType.LAKE );
				}
				else
				{
					PlanetMapSector currentSector = m_PlanetMap.getPlanetSector(currentRiverCoordinate);
					currentSector.setTerrainType( TerrainType.LAKE );
					Vector<Integer> surround = m_WorldMapNavigation.getSurroundingSectors(currentRiverCoordinate, 1); 
					for ( Integer sur : surround )
					{
						PlanetMapSector pms = m_PlanetMap.getPlanetSector(sur);
						if ( ! pms.hasRiver( ) )
						{
							boolean nextToRiver = false;
							for (int j = 0; j < 6; j++ )
							{
								int coord = m_WorldMapNavigation.getHexNeighbour(sur,j + 1);
								if ( m_PlanetMap.getPlanetSector(coord).hasRiver( ) )
									nextToRiver = true;
							}
							if ( !nextToRiver && Dice.d10(1) > 3 )
							{
								pms.setTerrainType( TerrainType.LAKE );
								pms.setMeanAltitude( currentElevation );
							}
						}
					}
				}
				riverFlowing = false;
			}
			else // river
			{
				if ( currentElevation == downstreamElevation && Dice.d100(1) > 95 )
					m_PlanetMap.getPlanetSector(currentRiverCoordinate).setTerrainType( TerrainType.SWAMP );

				if ( clockwise )
				{
					if ( downstreamRiverPoint < upstreamRiverPoint )
						downstreamRiverPoint += 6;

					for ( int i = upstreamRiverPoint; i < downstreamRiverPoint; i++ )
					{
						m_PlanetMap.getPlanetSector(currentRiverCoordinate).setRiver( i % 6, riverSize );
					}
				}
				else
				{
					if ( downstreamRiverPoint > upstreamRiverPoint )
						upstreamRiverPoint += 6;
					
					for (int i = upstreamRiverPoint - 1; i > downstreamRiverPoint - 1; i-- )
					{
						m_PlanetMap.getPlanetSector(currentRiverCoordinate).setRiver( i % 6, riverSize );
					}
				}
			}

			if ( ( ( ++riverLength % 16 ) == 0 ) && riverSize < 3 )
				riverSize++;

			riverRegister.add(currentRiverCoordinate);
			// delimit end of river
			if ( !riverFlowing )
				riverRegister.add(0);
			
			visited.add(currentRiverCoordinate);
			for (int i = 0; i < 6; i ++ )
			{
				int coord = m_WorldMapNavigation.getHexNeighbour(currentRiverCoordinate,i + 1);
				if ( coord == downstreamRiverCoordinate )
				{
					//if ( clockwise )
						int coord1 = m_WorldMapNavigation.getHexNeighbour(currentRiverCoordinate,i + 6);
						visited.add( coord1 );
						coord1 = m_WorldMapNavigation.getHexNeighbour(coord1,i + 5);
						visited.add( coord1 );
						visited.add( m_WorldMapNavigation.getHexNeighbour(coord1,i + 5));
					//else
						coord1 = m_WorldMapNavigation.getHexNeighbour(currentRiverCoordinate,i + 2);
						visited.add( coord1 );
						coord1 = m_WorldMapNavigation.getHexNeighbour(currentRiverCoordinate,i + 3);
						visited.add( coord1 );
						visited.add( m_WorldMapNavigation.getHexNeighbour(currentRiverCoordinate,i + 3) );
				}
			}

			//move downstream up
			upstreamRiverCoordinate = currentRiverCoordinate;
		
			for (int i = 0; i < 6; i ++ )
			{
				if ( m_WorldMapNavigation.getHexNeighbour(currentRiverCoordinate,i + 1) == downstreamRiverCoordinate )
				{
					if ( i == ( downstreamRiverPoint % 6 ) )
						upstreamRiverPoint = ( downstreamRiverPoint + 4 ) % 6;
					else
						upstreamRiverPoint = ( downstreamRiverPoint + 2 ) % 6;
				}
			}

			currentRiverCoordinate = downstreamRiverCoordinate;
			currentElevation = downstreamElevation;
			downstreamRiverCoordinate = 0;
			downstreamElevation = 25400;
		}
    	
    }
    

    void generateForests(Vector<PlanetMapSector> sectorsInContinent, int width, int height)
    {
    	
    	int numOfForests = Dice.d10( (int)( Math.sqrt( height * width ) / 10.0 ) );
    	int sizeOfForest = 0;
    	int currentPoint = 0;
    	int tempPoint = 0;
    	boolean validStart = false;
    	int[] randomVote = new int[6];
    	int randomDirection = 0;

    	while ( numOfForests  > 0)
    	{
    		int count = 0;
			PlanetMapSector pms = getRandomSectorInContinent(sectorsInContinent);
    		validStart = false;
    		while ( !validStart  && count < 1000)
    		{
    			if ( pms.getTerrainType( ) == TerrainType.PLAINS || pms.getTerrainType( ) == TerrainType.HILLS )
    				validStart = true;
    			
    			if (!validStart)
    				pms = getRandomSectorInContinent(sectorsInContinent);
    			
    			count++;
    		}
    		
    		if (count >= 1000)
    		{
    			numOfForests = 0;
    			continue;
    		}
			currentPoint = pms.getSectorNumber();

    		if ( pms.getTerrainType( ) == TerrainType.PLAINS )
    			pms.setTerrainType( TerrainType.FOREST );
    		else
    			pms.setTerrainType( TerrainType.WOODEDHILLS );
    		
    		sizeOfForest = ( Dice.random( height ) + Dice.random( width ) ) / 3;

    		while ( sizeOfForest > 0 )
    		{
    			randomVote[ 0 ] = 0;
    			randomDirection = 0;
    			for ( int direction = 0; direction < 6; direction++ )
    			{
    				tempPoint = m_WorldMapNavigation.getHexNeighbour(currentPoint,direction + 1);
					PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(tempPoint);
    				
					if ( !sectorInContinent(sectorsInContinent,tempPoint) ||
	    					!( pms1.getTerrainType( ) == TerrainType.PLAINS ||
	    					   pms1.getTerrainType( ) == TerrainType.FOREST ||
	    					   pms1.getTerrainType( ) == TerrainType.WOODEDHILLS ||
	    					   pms1.getTerrainType( ) == TerrainType.HILLS ) )
    					randomVote[ direction ] = 0;
    				else
    				{
    					if ( pms1.getTerrainType( ) == TerrainType.FOREST ||
    						 pms1.getTerrainType( ) == TerrainType.WOODEDHILLS )
    						randomVote[ direction ] = Dice.d10(1);
    					else
    						randomVote[ direction ] = Dice.d10( 3 );
    				}
    				
    				if ( randomVote[ direction ] > randomVote[ randomDirection ] )
    					randomDirection = direction;
    			}

    			if ( randomDirection == 0 && randomVote[ 0 ] == 0 )
    				sizeOfForest = 0;
    			else
    			{
    				currentPoint = m_WorldMapNavigation.getHexNeighbour(currentPoint,randomDirection + 1);
    				PlanetMapSector pms1 = m_PlanetMap.getPlanetSector(currentPoint);
    				if ( pms1.getTerrainType( ) == TerrainType.PLAINS )
    					pms1.setTerrainType( TerrainType.FOREST );
    				else
    					pms1.setTerrainType( TerrainType.WOODEDHILLS );

    				sizeOfForest--;
    			}
    		}
    		numOfForests--;
    	}
    	
    }
    
    private void generateTemperature(int meanTemperatureAtSeaLevel)
    {
    	
    	// Assumptions
    	// Hex is 200 miles wide
    	
    	int northEquatorRow = m_WorldMapNavigation.getMapRowHeight() / 2;
    	int southEquatorRow = m_WorldMapNavigation.getMapRowHeight() - (m_WorldMapNavigation.getMapRowHeight() / 2);
    	
        int Size = m_WorldMapNavigation.getMapSectorCount();
        //Fill in the blank spaces with deep water and set an initial elevation
        for (int i = 1; i <= Size; i++)
        {
            PlanetMapSector wsd = m_PlanetMap.getPlanetSector(i);
            
            int row = m_WorldMapNavigation.getMapRow(wsd.getSectorNumber());
            int offset = 0;
            if (row <= northEquatorRow)
            	offset = northEquatorRow - row;
            else
            	offset = row - southEquatorRow;
            
            int tempDrop = (int)(offset * 1.25);
            int elevation = wsd.getMeanAltitude(); 
            if (elevation > 0)
            	tempDrop += elevation / 500;
            
            int currentTemp = meanTemperatureAtSeaLevel - tempDrop;
            switch (wsd.getTerrainType())
            {
				case DEEPWATER:
					currentTemp /= 1.2;
					break;
				case WATER:
					currentTemp /= 1.15;
					break;
				case LAKE:
					currentTemp /= 1.10;
					break;
				case FOREST:
					currentTemp /= 1.05;
				case PLAINS:
				case HILLS:
					break;
				case SWAMP:
					currentTemp *= 1.05;
					break;
				case MOUNTAINS:
				case VOLCANO:
					currentTemp *= 1.10;
					break;
				default:
            }
            
            wsd.setMeanSurfaceTemperature(currentTemp);
            if (currentTemp < 0)
            {
            	TerrainType tt = wsd.getTerrainType();
            	if (!(tt == TerrainType.WATER || tt == TerrainType.DEEPWATER || tt == TerrainType.LAKE))
            		wsd.setTerrainType(TerrainType.LANDICE);
            	else
            	{
            		if (currentTemp < -Dice.d10(1))
	            		wsd.setTerrainType(TerrainType.WATERICE);
            	}
            }
            else if (currentTemp > 30)
            {
            	switch (wsd.getTerrainType())
            	{
            		case PLAINS:
            			wsd.setTerrainType(TerrainType.DESERT);
            			break;
            		case FOREST:
            			wsd.setTerrainType(TerrainType.JUNGLE);
            			break;
            		case DEEPFOREST:
            			wsd.setTerrainType(TerrainType.DEEPJUNGLE);
            			break;
            		case SCRUB:
            			wsd.setTerrainType(TerrainType.WASTELAND);
            			break;
            		case WOODEDHILLS:
            			if (currentTemp > 35)
            				wsd.setTerrainType(TerrainType.HILLS);
            			break;
            		default:
            	}
            }
        }
    }
    
    
}
