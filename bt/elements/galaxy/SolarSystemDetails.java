package bt.elements.galaxy;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import bt.mapping.planet.PlanetMap;
import bt.mapping.planet.PlanetMapSector;
import bt.mapping.planet.WorldMapNavigation;
import bt.mapping.planet.WorldTerrainGenerator;
import bt.movement.MapSectorPathState;
import bt.util.Dice;
import bt.util.ai.aStar;
import bt.util.ai.aState;


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
public class SolarSystemDetails implements Serializable
{

	private static final long serialVersionUID = 7473729908210390580L;

    protected static int[][] m_JumpSailRecharge = {
        /*M*/{201,202,203,204,205,206,207,208,209,210},
        /*K*/{191,192,193,194,195,196,197,198,199,200},
        /*G*/{181,182,183,184,185,186,187,188,189,190},
        /*F*/{171,172,173,174,175,176,177,178,179,180},
        /*A*/{161,162,163,164,165,166,167,168,169,170},
        /*B*/{151,152,153,154,155,156,157,158,159,160}  };

        private static double[][] ProximityPoints = {
        /*M*/{  0.18,  0.16,  0.15,  0.13,  0.12,  0.11,  0.10, 0.09, 0.08, 0.07},
        /*K*/{  0.55,  0.49,  0.43,  0.39,  0.34,  0.31,  0.28, 0.25, 0.22, 0.20},
        /*G*/{  1.99,  1.74,  1.52,  1.33,  1.16,  1.02,  0.90, 0.79, 0.70, 0.62},
        /*F*/{  8.80,  7.51,  6.43,  5.51,  4.74,  4.08,  3.52, 3.04, 2.64, 2.29},
        /*A*/{ 48.59, 40.51, 33.85, 28.36, 23.82, 20.06, 19.63,14.32,12.15,10.32},
        /*B*/{347.84,282.07,229.40,187.12,153.06,125.56,103.29,85.20,70.47,58.44}};

        private static double[][] BiozoneLimitsAU = {
        /*M*/{0.1,0.2},
        /*K*/{0.5,0.6},
        /*G*/{0.8,1.2},
        /*F*/{1.6,2.4},
        /*A*/{3.1,4.7},
        /*B*/{30.0,45.0}};

        //private static double AUinBillionKm = 0.149598550;
        private static double AUinKm = 149598550.0;
        private static double GsInKmPHPH = 127008.0;

        public static int getJumpSailRechargeTime(StarType st, int starMagnitude)
        {
            int stIndex = GetStarTypeIndex(st);

            return m_JumpSailRecharge[stIndex][starMagnitude];
        }

        protected static double getJumpPointProximity(StarType st, int magnitude)
        {
                int StarTypeIndex = GetStarTypeIndex(st);
                int MagnitudeIndex = magnitude;
                double jpp = ProximityPoints[StarTypeIndex][MagnitudeIndex];
                return jpp;
        }

        public static double getJumpPointDistanceFromPlanet(StarType st, int starMagnitude, double planetOrbitInAU)
        {
                double JumpPointProximity = getJumpPointProximity(st,starMagnitude) * 1000000000.0;
                double PlanetDistance = planetOrbitInAU * AUinKm;
                double JPPSquare = JumpPointProximity * JumpPointProximity;
                double PDSquare = PlanetDistance * PlanetDistance;

                double Dist = Math.sqrt(JPPSquare + PDSquare);
                return Dist;
        }

        public static double getTransitTime(StarType st, int starMagnitude, double distance, double gRating)
        {
                if (distance == 0.0 || gRating == 0.0)
                        return 0.0;

                double Accell = GsInKmPHPH * gRating;
                double FullDist =  getJumpPointDistanceFromPlanet(st,starMagnitude,distance) / 2;
                double BurnUnits = FullDist / Accell * 2;
                double TransitTime = 2 * Math.sqrt(BurnUnits);
                return TransitTime;
        }

        public static double GetBiozoneInnerLimit(StarType st)
        {
                int Index = GetStarTypeIndex(st);
                return BiozoneLimitsAU[Index][0];
        }

        public static double GetBiozoneOuterLimit(StarType st)
        {
                int Index = GetStarTypeIndex(st);
                return BiozoneLimitsAU[Index][1];
        }

        protected static int GetStarTypeIndex(StarType st)
        {
        	switch (st)
        	{
        		case O: return 5;
        		case B: return 5;
    	    	case A: return 4;
    	    	case F: return 3;
    	    	case G: return 2;
    	    	case K: return 1;
    	    	case M: return 0;    		
        	}
            return 0;
        }
        
        public static StarType getRandomStarType()
        {
        	int d6 = Dice.d6(2);
        	switch (d6)
        	{
        	case 2:
        		return StarType.B;
        	case 3:
        		return StarType.A;
        	case 4:
        	case 5:
        		return StarType.F;
        	case 6:
        		return StarType.K;
        	case 7:
        	case 8:
        		return StarType.G;
        	default:
        		return StarType.M;
        	}
        }
        

    
    
    private long m_Planet;
    private StarType m_StarType = StarType.G;
    private int m_StarMagnitude = 3;
    private double m_PrimaryPlanetOrbitInAU = 1.0;
    private int m_Size = 30;
    private int m_MeanTemperatureAtSeaLevel = 40;    	// mean temperature at equator at sea level
    private String m_Notes = "";
    private boolean m_MapGenerated = false;
    private PlanetMap m_Map = null;
    private Vector<Settlement> m_Settlements = new Vector<Settlement>();

    public long getPlanet()
    { return m_Planet; }

    public void setPlanet(long ID)
    { m_Planet = ID; }

    public StarType getStarType()
    { return m_StarType; }

    public void setStarType(StarType st)
    { m_StarType = st; }

    public int getStarMagnitude()
    { return m_StarMagnitude; }

    public void setStarMagnitude(int StarMagnitude)
    { m_StarMagnitude = StarMagnitude; }

    public void setMeanTemperatureAtSeaLevel(int meanTemperatureAtSeaLevel) 
    { m_MeanTemperatureAtSeaLevel = meanTemperatureAtSeaLevel; }

	public int getMeanTemperatureAtSeaLevel() 
	{ return m_MeanTemperatureAtSeaLevel; }

	public double getPrimaryPlanetOrbitInAU()
    { return m_PrimaryPlanetOrbitInAU; }

	public int getPrimaryPlanetHydrographics()
	{
		if (m_Map != null)
		{
			return m_Map.getHydrographics();
		}	
		return 0;
	}

	public int getPrimaryPlanetMeanAltitude()
	{
		if (m_Map != null)
		{
			return m_Map.getMeanAltitude();
		}	
		return 0;
	}

	public int getPrimaryPlanetSurfaceTemperature()
	{
		if (m_Map != null)
		{
			return m_Map.getMeanSurfaceTemperature();
		}	
		return 0;
	}

	public void setPrimaryPlanetOrbitInAU(double OrbitInAU)
    { m_PrimaryPlanetOrbitInAU = OrbitInAU; }

    public String getNotes()
    { return m_Notes; }

    public void setNotes(String Notes)
    { m_Notes = Notes; }

    public int getSize()
    { return m_Size; }

    public void setSize(int size)
    { m_Planet = size; }

    public boolean isMapGenerated()
    { return m_MapGenerated; }
    
    public PlanetMap getMap()
    { return m_Map; }
    
    public void setMap(PlanetMap map)
    {
    	m_Map = map;
    	m_MapGenerated = (m_Map != null);
    }

    public SolarSystemDetails()
    {
    }
    
    public int getSettlementCount()
    { return m_Settlements.size(); }
    
    public Settlement getSettlement(int index)
    { return m_Settlements.elementAt(index); }
    
    public void generateRandomPlanet(boolean generateTerrain, boolean generateSettlements, boolean generateRoads)
    {
    	Vector<Integer> settlementLocations = new Vector<Integer>();    	
		WorldTerrainGenerator wtg = new WorldTerrainGenerator(m_Size);
    	WorldMapNavigation wmn = wtg.getWorldMapNavigation();
    	
    	if (generateTerrain)
    	{
	    	do
	    	{
	    		setSize(Dice.d6(2) + 23);
	    		wtg = new WorldTerrainGenerator(m_Size);
	        	wmn = wtg.getWorldMapNavigation();
	        	
				wtg.generateRandomPlanet(m_MeanTemperatureAtSeaLevel);
				setMap(wtg.getPlanetMap());
		
				//Place Settlements
				settlementLocations = m_Map.findSettlementSites(wtg.getWorldMapNavigation());
	    	} while(settlementLocations.size() == 0);    	
    	}
    	
    	if (generateSettlements)
    	{
    		m_Settlements.clear();
    		if (settlementLocations.size() == 0)
				settlementLocations = m_Map.findSettlementSites(wtg.getWorldMapNavigation());
    			
			HashMap<SettlementType, Integer> settlementTypes = new HashMap<SettlementType, Integer>();
			settlementTypes.put(SettlementType.CAPITOL, 1);
			int remainder = settlementLocations.size() - 1;
			settlementTypes.put(SettlementType.SETTLEMENT,remainder / 2);
			remainder -= settlementTypes.get(SettlementType.SETTLEMENT);
			settlementTypes.put(SettlementType.CITY, remainder / 3);
			remainder -= settlementTypes.get(SettlementType.CITY);
			settlementTypes.put(SettlementType.TOWN, remainder);
			
			SettlementType[] typeOrder = {SettlementType.CAPITOL,SettlementType.CITY,SettlementType.TOWN,SettlementType.SETTLEMENT};
			int currentType = 0;
			while (settlementLocations.size() > 0 && currentType < 4)
			{
				int typeCount = settlementTypes.get(typeOrder[currentType]);
				for (int i = 0; i < typeCount; i++)
				{
					int index = Dice.random(settlementLocations.size()) - 1;
					Integer location = settlementLocations.elementAt(index);
					Settlement s = new Settlement(location);
					s.setType(typeOrder[currentType]);
					s.setName(typeOrder[currentType].toString() + " " + Integer.toString(i + 1));
					m_Settlements.add(s);
					
					System.out.println("Adding Settlement of type " + typeOrder[currentType] + " at location " + Integer.toString(location));
					settlementLocations.removeElementAt(index);
					
					if (settlementLocations.size() == 0)
						break;
				}
				currentType++;
			}
			
			//Place the Starport
			System.out.println("Finding Location for Starport");
			int capitolLocation = m_Settlements.elementAt(0).getLocation();
			Vector<Integer> surround = wmn.getSurroundingSectors(capitolLocation, 1); 
			double distance = Integer.MAX_VALUE;
			int chosenLocation = 0;
			for (Integer dest: surround)
			{
	        	MapSectorPathState msps = new MapSectorPathState(capitolLocation,m_Map,wmn,0,dest);
	        	aStar engine = new aStar(msps);
	        	ArrayList<aState> result = engine.search();
	        	if (result != null)
	        	{
	        		MapSectorPathState msps1 = (MapSectorPathState)result.get(result.size() - 1);
	        		double dist = msps1.getDistFromStart();
	        		if (dist < distance)
	        		{
	        			distance = dist;
	        			chosenLocation = msps1.getSectorNumber();
	        		}
	        	}			
			}
			if (chosenLocation > 0)
			{
				System.out.println("Placing Spaceport at location " + Integer.toString(chosenLocation));
				Settlement starport = new Settlement(chosenLocation);
				starport.setName("Starport 1");
				starport.setType(SettlementType.SPACEPORT);
				m_Settlements.add(starport);
			}
    	}
		
    	if (generateRoads)
    	{
    		//Clear any existing roads
    		for (int i = 0; i < m_Map.getPlanetSectorCount(); i++)
    			m_Map.getPlanetSector(i + 1).clearRoads();
    		
			//Lay down roads
			HashMap<Integer,Vector<Integer>> builtRoads = new HashMap<Integer,Vector<Integer>>();
			for (Settlement place1: m_Settlements)
			{
				builtRoads.put(place1.getLocation(), new Vector<Integer>());
			}
			
			for (Settlement place1: m_Settlements)
			{
				System.out.println("Laying roads for settlement " + place1.toString());
				Integer key1 = place1.getLocation();
				builtRoads.put(key1,new Vector<Integer>());
				for (Settlement place2: m_Settlements)
				{
					if (place1.getLocation() == place2.getLocation())
						continue;
					
					Integer key2 = place2.getLocation();
					if (builtRoads.get(key2).contains(key1))
						continue;
					
					int roadWidth = Math.min(place1.getType().getRoadWidth(), place2.getType().getRoadWidth());
		        	
		        	MapSectorPathState msps = new MapSectorPathState(key1,m_Map,wmn,0,key2);
		        	aStar engine = new aStar(msps);
		        	ArrayList<aState> result = engine.search();
		        	if (result != null)
		        	{
		        		System.out.println("Adding road between " + key1.toString() + " and " + key2.toString());
		        		Iterator<aState> iter = result.iterator();
	        			MapSectorPathState sector1 = (MapSectorPathState)iter.next();
		        		while (iter.hasNext())
		        		{
		        			MapSectorPathState sector2 = (MapSectorPathState)iter.next();
		        			int sector1Num = sector1.getSectorNumber();
		        			int sector2Num = sector2.getSectorNumber();
		        			
		        			int dir = wmn.getDirectionOfNeighbour(sector1Num, sector2Num);
		        			if (dir >= 0)
		        			{	        			
			        			PlanetMapSector pms1 = m_Map.getPlanetSector(sector1Num);
			        			pms1.setRoad(dir - 1, roadWidth);
		        			}
		        			
		        			dir = wmn.getDirectionOfNeighbour(sector2Num, sector1Num);
		        			if (dir >= 0)
		        			{
			        			PlanetMapSector pms2 = m_Map.getPlanetSector(sector2Num);
			        			pms2.setRoad(dir - 1, roadWidth);
		        			}
		        				        			
		        			sector1 = sector2;
		        		}
		        	}
		        	builtRoads.get(key1).add(key2);
		        	builtRoads.get(key2).add(key1);
				}
			}
		}
    }
    
    @SuppressWarnings("unchecked")
    public void loadFromElement(org.jdom.Element e)
    {
    	m_Notes = e.getChildText("Notes");
    	m_StarType = StarType.fromString(e.getChildText("StarType"));
    	m_StarMagnitude = Integer.parseInt(e.getChildText("StarMagnitude"));
    	m_PrimaryPlanetOrbitInAU = Double.parseDouble(e.getChildText("PrimaryPlanetOrbitInAU"));
    	m_MeanTemperatureAtSeaLevel = Integer.parseInt(e.getChildText("MeanTempAtSeaLevel"));
    	org.jdom.Element mapElement = e.getChild("Map");
    	if (mapElement != null)
    		m_Map = new PlanetMap(mapElement);
    	
    	m_MapGenerated = (m_Map != null);
    	
    	org.jdom.Element settlements = e.getChild("Settlements");
    	if (settlements != null)
    	{
    		List<org.jdom.Element> settlementElements = settlements.getChildren("Settlement");
    		for (org.jdom.Element settlementElement : settlementElements)
    		{
    			Settlement s = new Settlement();
    			s.loadFromElement(settlementElement);
    			m_Settlements.add(s);
    		}    		
    	}    	
    }

    public void saveToElement(org.jdom.Element e)
    {
    	e.addContent(new org.jdom.Element("Notes").setText(m_Notes));
    	e.addContent(new org.jdom.Element("StarType").setText(m_StarType.toString()));
    	e.addContent(new org.jdom.Element("StarMagnitude").setText(Integer.toString(m_StarMagnitude)));
    	e.addContent(new org.jdom.Element("PrimaryPlanetOrbitInAU").setText(Double.toString(m_PrimaryPlanetOrbitInAU)));
    	e.addContent(new org.jdom.Element("MeanTempAtSeaLevel").setText(Integer.toString(m_MeanTemperatureAtSeaLevel)));
        	
    	if (m_Map != null)
    	{
    		org.jdom.Element map = new org.jdom.Element("Map"); 
    		m_Map.saveToElement(map);
    		e.addContent(map);
    	} 
    	org.jdom.Element settlements = new org.jdom.Element("Settlements");
    	for (Settlement s: m_Settlements)
    	{
    		org.jdom.Element settlement = new org.jdom.Element("Settlement");
    		s.saveToElement(settlement);
    		settlements.addContent(settlement);
    	}
    	e.addContent(settlements);
    }

}
