package bt.managers;

import java.awt.image.BufferedImage;
import java.io.File;

import java.io.FileWriter;
import java.io.PrintWriter;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import bt.elements.Battlemech;
import bt.elements.Era;
import bt.elements.Faction;
import bt.elements.collection.ItemCollection;
import bt.elements.mapping.MapSet;
import bt.elements.mapping.MapSheet;
import bt.elements.missions.Mission;
import bt.elements.missions.MissionMapSet;
import bt.elements.missions.Objective;
import bt.elements.missions.OpponentBriefing;
import bt.elements.missions.PlayerBriefing;
import bt.elements.missions.PreviousSuccessfulPlayerMission;
import bt.elements.missions.SpecialRule;
import bt.elements.missions.Warchest;
import bt.elements.missions.WarchestOption;
import bt.elements.personnel.Mechwarrior;
import bt.elements.personnel.Rating;
import bt.elements.scenario.Scenario;
import bt.elements.scenario.Season;
import bt.elements.scenario.TimeOfDay;
import bt.elements.scenario.Wind;
import bt.elements.unit.MechUnitParameters;
import bt.elements.unit.Player;
import bt.elements.unit.QualityRating;
import bt.elements.unit.TechRating;
import bt.elements.unit.Unit;
import bt.html.Tag;
import bt.ui.renderers.BattlemechRenderer;
import bt.ui.renderers.MapLayoutRenderer;
import bt.util.Dice;
import bt.util.PropertyUtil;

public class MissionManager
{
	private static MissionManager theInstance = new MissionManager();
    
    private HashMap<Long,Mission> _Missions = new HashMap<Long,Mission>();

	private MissionManager()
	{
		try
		{
			loadMissions();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static MissionManager getInstance()
	{ return theInstance; }
	
	private void loadMissions() throws Exception
	{
		_Missions.clear();
		
        String Path = PropertyUtil.getStringProperty("DataPath", "data");
        
        try {            
            SAXBuilder b = new SAXBuilder();
            Document mapDoc = b.build(new File(Path, "/Missions.xml"));
            Element root = mapDoc.getRootElement();
            Iterator<?> iter = root.getChildren("Mission").iterator();
            while (iter.hasNext())
            {
            	org.jdom.Element missionElement = (org.jdom.Element)iter.next();
            	Mission ms = loadMission(missionElement);
            	_Missions.put(ms.getID(),ms);            	
            }
    	
        } catch(java.io.IOException ex) {
            System.out.println("Error Opening Missions File!");
            ex.printStackTrace();
        } catch (JDOMException jdex) {
        	System.out.println("Failure Parsing Missions File!");
            jdex.printStackTrace();
        }	
	}
	
	private Mission loadMission(org.jdom.Element element)
	{
		Mission m = new Mission();
		
		m.setID(Long.parseLong(element.getAttributeValue("ID")));
		m.setName(element.getAttributeValue("Name"));
		m.setDescription(element.getAttributeValue("Description"));
		
		if (element.getChild("Notes") != null)
			m.setNotes(element.getChildText("Notes"));
		
		if (element.getChild("GameSetup") != null)
		{
			org.jdom.Element gameSetupElement = element.getChild("GameSetup");
			m.setGameSetup(gameSetupElement.getText());
		}
		if (element.getChild("MapSet") != null)
		{
			org.jdom.Element mapSetElement = element.getChild("MapSet");
			m.setMapSetTerrain(mapSetElement.getAttributeValue("Terrain"));
		}
		
		if (element.getChild("PlayerBriefing") != null)
		{
			org.jdom.Element briefingElement = element.getChild("PlayerBriefing");
			PlayerBriefing pb = new PlayerBriefing();
			
			pb.setTeam(briefingElement.getAttributeValue("Team"));
			pb.setBriefing(briefingElement.getChildText("Briefing"));
			m.setPlayerBriefing(pb);
		}
		if (element.getChild("OpponentBriefing") != null)
		{
			org.jdom.Element briefingElement = element.getChild("OpponentBriefing");
			OpponentBriefing ob = new OpponentBriefing();
			
			ob.setTeam(briefingElement.getAttributeValue("Team"));
			ob.setBriefing(briefingElement.getChildText("Briefing"));
			
			org.jdom.Element previousMissionElement = element.getChild("PreviousSuccessfulPlayerMission");
			PreviousSuccessfulPlayerMission pspm = new PreviousSuccessfulPlayerMission();
			pspm.setModifier(Integer.parseInt(previousMissionElement.getAttributeValue("Modifier")));
			String missionList = "";
			if (previousMissionElement.getAttribute("Excludes") != null)
			{
				pspm.setExclusive();
				missionList = previousMissionElement.getAttributeValue("Excludes");
			}
			else
			{
				pspm.setInclusive();
				missionList = previousMissionElement.getAttributeValue("Includes");
			}
			for (String s: missionList.split(","))
			{
				if (!s.trim().isEmpty())
				{
					pspm.addMission(s.trim());
				}
			}
			ob.setPreviousSuccessfulPlayerMission(pspm);
			
			org.jdom.Element techRatingTableElement = element.getChild("TechRatingTable");
			{
				Iterator<?> iter = techRatingTableElement.getChildren("Option").iterator();
				while (iter.hasNext())
				{
					org.jdom.Element optionElement = (org.jdom.Element)iter.next();
					int roll = Integer.parseInt(optionElement.getAttributeValue("Roll"));
					TechRating tr = TechRating.fromString(optionElement.getAttributeValue("Tech"));
					ob.setTechRating(roll, tr);
				}
			}
			
			org.jdom.Element forceRatioTableElement = element.getChild("ForceRatioTable");
			{
				Iterator<?> iter = forceRatioTableElement.getChildren("Option").iterator();
				while (iter.hasNext())
				{
					org.jdom.Element optionElement = (org.jdom.Element)iter.next();
					int roll = Integer.parseInt(optionElement.getAttributeValue("Roll"));
					float ratio = Float.parseFloat(optionElement.getAttributeValue("Ratio"));
					ob.setForceRatio(roll, ratio);
				}
			}
			
			org.jdom.Element unitCompositionTableElement = element.getChild("UnitCompositionTable");
			{
				Iterator<?> iter = unitCompositionTableElement.getChildren("Option").iterator();
				while (iter.hasNext())
				{
					org.jdom.Element optionElement = (org.jdom.Element)iter.next();
					int roll = Integer.parseInt(optionElement.getAttributeValue("Roll"));
					String composition = optionElement.getAttributeValue("Composition");
					ob.setUnitComposition(roll, composition);
				}
			}

			org.jdom.Element randomAllocationTableElement = element.getChild("RandomAllocationTable");
			{
				Iterator<?> iter = randomAllocationTableElement.getChildren("Option").iterator();
				while (iter.hasNext())
				{
					org.jdom.Element optionElement = (org.jdom.Element)iter.next();
					int roll = Integer.parseInt(optionElement.getAttributeValue("Roll"));
					String rat = optionElement.getAttributeValue("RAT");
					ob.setUnitComposition(roll, rat);
				}
			}

			org.jdom.Element skillLevelTableElement = element.getChild("SkillLevelTable");
			{
				Iterator<?> iter = skillLevelTableElement.getChildren("Option").iterator();
				while (iter.hasNext())
				{
					org.jdom.Element optionElement = (org.jdom.Element)iter.next();
					int roll = Integer.parseInt(optionElement.getAttributeValue("Roll"));
					String level = optionElement.getAttributeValue("Level");
					ob.setUnitComposition(roll, level);
				}
			}

			m.setOpponentBriefing(ob);
		}
		
		if (element.getChild("Warchest") != null)
		{
			org.jdom.Element warchestElement = element.getChild("Warchest");
			
			Warchest w = new Warchest();
			w.setMissionCost(Integer.parseInt(warchestElement.getAttributeValue("MissionCost")));
			
			Iterator<?> iter = warchestElement.getChildren("Option").iterator();
			while (iter.hasNext())
			{
				org.jdom.Element optionElement = (org.jdom.Element)iter.next();
				WarchestOption wo = new WarchestOption();
				wo.setName(optionElement.getAttributeValue("Name"));
				wo.setBonus(Integer.parseInt(optionElement.getAttributeValue("Bonus")));
				wo.setNotes(optionElement.getAttributeValue("Notes"));
				w.addOption(wo);
			}
			
			m.setWarchest(w);
		}

		Iterator<?> iter = element.getChildren("Objective").iterator();
		while (iter.hasNext())
		{
			org.jdom.Element objectiveElement = (org.jdom.Element)iter.next();
			Objective o = new Objective();
			
			o.setName(objectiveElement.getAttributeValue("Name"));
			o.setDescription(objectiveElement.getAttributeValue("Description"));
			o.setPointsAwarded(Integer.parseInt(objectiveElement.getAttributeValue("PointsAwarded")));
			
			m.addObjective(o);
		}

		iter = element.getChildren("SpecialRule").iterator();
		while (iter.hasNext())
		{
			org.jdom.Element specialRuleElement = (org.jdom.Element)iter.next();
			SpecialRule sr = new SpecialRule();
			
			sr.setName(specialRuleElement.getAttributeValue("Name"));
			sr.setDescription(specialRuleElement.getAttributeValue("Description"));
			
			m.addSpecialRule(sr);
		}

		return m;
	}
	
	public org.jdom.Element saveMission(Mission mission)
	{
		org.jdom.Element missionElement = new org.jdom.Element("Mission");
		
		missionElement.setAttribute("ID", Long.toString(mission.getID()));
		missionElement.setAttribute("Name",mission.getName());
		missionElement.setAttribute("Description",mission.getDescription());
		
		missionElement.addContent(new org.jdom.Element("GameSetup").setText(mission.getDescription()));
		missionElement.addContent(new org.jdom.Element("Notes").setText(mission.getNotes()));

		org.jdom.Element mapSetElement = new org.jdom.Element("MapSet");
		mapSetElement.setAttribute("Terrain", mission.getMapSetTerrain());
		missionElement.addContent(mapSetElement);

		PlayerBriefing playerBriefing = mission.getPlayerBriefing();
		org.jdom.Element playerBriefingElement = new Element("PlayerBriefing");
		playerBriefingElement.setAttribute("Team", playerBriefing.getTeam());
		playerBriefingElement.addContent(new Element("Briefing").setText(playerBriefing.getBriefing()));
		playerBriefingElement.addContent(new Element("Notes").setText(playerBriefing.getNotes()));
		missionElement.addContent(playerBriefingElement);
		
		OpponentBriefing opponentBriefing = mission.getOpponentBriefing();
		org.jdom.Element opponentBriefingElement = new Element("OpponentBriefing");
		opponentBriefingElement.setAttribute("Team", opponentBriefing.getTeam());
		opponentBriefingElement.addContent(new Element("Briefing").setText(opponentBriefing.getBriefing()));
		opponentBriefingElement.addContent(new Element("Notes").setText(opponentBriefing.getNotes()));
		
		PreviousSuccessfulPlayerMission pspm = opponentBriefing.getPreviousSuccessfulPlayerMission();
		org.jdom.Element previousSuccessfulPlayerMissionElement = new Element("PreviousSuccessfulPlayerMission");
		previousSuccessfulPlayerMissionElement.setAttribute("Modifiers", Integer.toString(pspm.getModifier()));
		if (pspm.isInclusive())
			previousSuccessfulPlayerMissionElement.setAttribute("Includes",pspm.getList());
		else
			previousSuccessfulPlayerMissionElement.setAttribute("Excludes",pspm.getList());
		
		opponentBriefingElement.setContent(previousSuccessfulPlayerMissionElement);
		
		org.jdom.Element techRatingTableElement = new Element("TechRatingTable");
		for (int i = 1; i < 9; i++)
		{
			techRatingTableElement.setAttribute("Roll", Integer.toString(i));
			techRatingTableElement.setAttribute("Tech", opponentBriefing.getTechRating(i).toString());
		}
		opponentBriefingElement.setContent(techRatingTableElement);

		org.jdom.Element forceRatioTableElement = new Element("ForceRatioTable");
		for (int i = 1; i < 9; i++)
		{
			forceRatioTableElement.setAttribute("Roll", Integer.toString(i));
			forceRatioTableElement.setAttribute("Ratio", Float.toString(opponentBriefing.getForceRatio(i)));
		}
		opponentBriefingElement.setContent(forceRatioTableElement);

		org.jdom.Element unitCompositionTableElement = new Element("UnitCompositionTable");
		for (int i = 1; i < 9; i++)
		{
			unitCompositionTableElement.setAttribute("Roll", Integer.toString(i));
			unitCompositionTableElement.setAttribute("Composition", opponentBriefing.getUnitComposition(i));
		}
		opponentBriefingElement.setContent(unitCompositionTableElement);

		org.jdom.Element randomAllocationTableElement = new Element("RandomAllocationTable");
		for (int i = 1; i < 9; i++)
		{
			randomAllocationTableElement.setAttribute("Roll", Integer.toString(i));
			randomAllocationTableElement.setAttribute("RAT", opponentBriefing.getUnitComposition(i));
		}
		opponentBriefingElement.setContent(randomAllocationTableElement);

		org.jdom.Element skillLevelTableElement = new Element("SkillLevelTable");
		for (int i = 1; i < 9; i++)
		{
			skillLevelTableElement.setAttribute("Roll", Integer.toString(i));
			skillLevelTableElement.setAttribute("Level", opponentBriefing.getUnitComposition(i));
		}
		opponentBriefingElement.setContent(skillLevelTableElement);
		missionElement.addContent(opponentBriefingElement);
		
		Warchest warchest = mission.getWarchest();
		org.jdom.Element warchestElement = new Element("Warchest");
		warchestElement.setAttribute("MissionCost", Integer.toString(warchest.getMissionCost()));
		
		for (WarchestOption wo: warchest.getOptions())
		{
			org.jdom.Element optionElement = new Element("Option");
			optionElement.setAttribute("Name", wo.getName());
			optionElement.setAttribute("Bonus", Integer.toString(wo.getBonus()));
			optionElement.setAttribute("Notes", wo.getNotes());
			warchestElement.addContent(optionElement);
		}		
		missionElement.addContent(warchestElement);
		
		for (Objective o : mission.getObjectives())
		{
			org.jdom.Element objectiveElement = new Element("Objective");
			objectiveElement.setAttribute("Name", o.getName());
			objectiveElement.setAttribute("PointsAwarded", Integer.toString(o.getPointsAwarded()));
			objectiveElement.setAttribute("Description", o.getDescription());
			missionElement.addContent(objectiveElement);			
		}
		
		for (SpecialRule sr: mission.getSpecialRules())
		{
			org.jdom.Element specialRuleElement = new Element("SpecialRule");
			specialRuleElement.setAttribute("Name", sr.getName());
			specialRuleElement.setAttribute("Description", sr.getDescription());
			missionElement.addContent(specialRuleElement);			
		}
		
		return missionElement;
	}
	
	public Vector<String> getMissionList()
	{
		Vector<String> missionList = new Vector<String>();
		for (Long id : _Missions.keySet())
			missionList.add(_Missions.get(id).getName());
		
		return missionList; 
	}
	
	public Vector<String> getMissionList(Vector<Long> missionIDs)
	{
		Vector<String> missionList = new Vector<String>();
		for (Long id : missionIDs)
			missionList.add(_Missions.get(id).getName());
		
		return missionList; 
	}
	
	public String getMissionName(Long missionID)
	{
		Mission m = _Missions.get(missionID);
		if (m != null)
		{
			return m.getName();
		}
		
		return ""; 
	}
	
	public Mission getMission(String missionName)
	{
		for (Mission m : _Missions.values())
		{
			if (m.getName().equalsIgnoreCase(missionName))
				return m;
		}
		return null;
	}
	
	public Mission getRandomMission(ArrayList<Long> completedMissions)
	{
		ArrayList<Long> eligibleMissions = new ArrayList<Long>(_Missions.keySet());
		eligibleMissions.removeAll(completedMissions);
		if (eligibleMissions.size() > 0)
		{
			int randomMission = Dice.random(eligibleMissions.size()) - 1;
			return new Mission(_Missions.get(eligibleMissions.get(randomMission)));
		}
		return null;
	}
	
	public int getFinalBattleValue(int opposingForceSize, int opposingForceBV, int playerForceSize)
	{
		int finalBattleValue = opposingForceBV;

		if (opposingForceSize != playerForceSize)
		{
			int smallerForceSize = Math.min(opposingForceSize, playerForceSize);
			int largerForceSize = Math.max(opposingForceSize, playerForceSize);

			int rawForceDifference = largerForceSize - smallerForceSize;
			double BaseForcePercentageMultiplier = ((double) rawForceDifference / (double) smallerForceSize) * 100.0;
			int RawForcePercentageMultiplier = rawForceDifference * 10;
			double FinalPercentageModifier = (Math.min(RawForcePercentageMultiplier, BaseForcePercentageMultiplier) + 100) / 100;

			int modifiedStrength = (int) (opposingForceBV * FinalPercentageModifier);
			double playerForceMultiplier = 1;
			if (opposingForceSize > playerForceSize)
				playerForceMultiplier *= ((double) modifiedStrength / (double) opposingForceBV);
			else
				playerForceMultiplier *= ((double) opposingForceBV / (double) modifiedStrength);

			finalBattleValue = (int) (opposingForceBV * playerForceMultiplier);
		}
		return finalBattleValue;
	}
	
	private String substituteMapLocations(String setupText, MapSet mapSet)
	{
		CharSequence cs = "[MAPHEXES";
		if (setupText.contains(cs))
		{
			int startIndex = setupText.indexOf(cs.toString());
			int dashIndex = setupText.indexOf("-",startIndex);
			int endIndex = setupText.indexOf("]",startIndex);
			
			int mapNumber = Integer.parseInt(setupText.substring(dashIndex + 1, endIndex));
			String mapName = mapSet.getMap(mapNumber);
			if (mapName != null)
			{
				MapSheet sheet = MapManager.getInstance().getMapSheet(mapName);
				if (sheet != null)
				{
					String hexlist = "[" + sheet.getRandomHexList(6) + "]";
					String firstString = setupText.substring(0, startIndex);
					String lastString = setupText.substring(endIndex + 1);
					return firstString + hexlist + lastString;
				}
			}
		}
		return setupText;
	}
	
	private void completeSetup(Scenario s, Mission m)
	{
		/*
		PlayerBriefing briefing = m.getPlayerBriefing();
		briefing.setSetup(substituteMapLocations(briefing.getSetup(), mapSet));
		briefing.setObjective(substituteMapLocations(briefing.getObjective(), mapSet));
		briefing.setVictoryConditions(substituteMapLocations(briefing.getVictoryConditions(), mapSet));
		briefing.setSpecialConditions(substituteMapLocations(briefing.getSpecialConditions(), mapSet));
		*/
	}
	
	
	public Scenario generateScenario(Era era, Faction opposingFaction, Unit u, Rating opponentRating, QualityRating opponentQualityRating, TechRating opponentTechRating, String missionName, ItemCollection collection)
	{
		Scenario scenario = new Scenario();
		/*
		Mission m = missionName != null ? getMission(missionName) : getRandomMission(u.getCompletedMissionIDs());
		scenario.setMission(m);
		scenario.setTimeOfDay(TimeOfDay.random());
		scenario.setSeason(Season.getRandomSeason());
		scenario.setWind(Wind.getRandomWind());
		if (m.getMapSet() == null)
			throw new RuntimeException("Mission " + m.getName() + " does not have a MapSet set");
		scenario.setMapSet(MapManager.getInstance().getRandomMapSet(m.getMapSet().getMaps(),m.getMapSet().getRows(),m.getMapSet().getColumns()));
		completeSetup(scenario,m);
			
		int forceBV = 0;
		int forceSize = 0;
		String sideName = "Attacker";
		String oppositionName = "Defender";
		if (Dice.d6(1) > 3)
		{
			sideName = "Defender";
			oppositionName = "Attacker";
			forceBV = Math.round((float)u.getUnitBV() * m.getForceRatio());
			forceSize = Math.round((float)u.getUnitStrength() * m.getForceRatio());
		}
		else
		{
			forceBV = Math.round((float)u.getUnitBV() / m.getForceRatio());
			forceSize = Math.round((float)u.getUnitStrength() / m.getForceRatio());
		}
		
		Unit opposingUnit = generateOpposingUnit(era, opposingFaction, forceBV, forceSize, opponentRating, opponentQualityRating, opponentTechRating, collection);
		
		scenario.getSides().put(sideName, u);
		scenario.getSides().put(oppositionName, opposingUnit);
		*/
		return scenario;
	}
	
	private Unit generateOpposingUnit(Era era, Faction faction, int forceBV, int unitStrength, Rating rating, QualityRating qualityRating, TechRating techRating, ItemCollection collection)
	{
		
		Player p = new Player();
		String unitName = "Opposing Unit";
		
		Vector<ForceSize> forceSizes = new Vector<ForceSize>();
		for (int st = unitStrength - 2; st < unitStrength + 3; st++)
		{
			if (st > 2)
			{
				ForceSize fs = new ForceSize();
				fs.setNumUnits(st);
				fs.setBV(getFinalBattleValue(unitStrength,forceBV,st));
				forceSizes.add(fs);
				
				System.out.println("Force option ->  NumUnits:" + fs.getNumUnits() + " BV:" + fs.getBV());
			}
		}
		int unitBV = 0;
		Unit u = null;
		while (unitBV == 0 && forceSizes.size() > 0)
		{
			ForceSize chosenForceSize = forceSizes.elementAt(Dice.random(forceSizes.size())- 1);
			System.out.println("SELECTED Force option ->  NumUnits:" + chosenForceSize.getNumUnits() + " BV:" + chosenForceSize.getBV());
			
			forceSizes.remove(chosenForceSize);
			
			MechUnitParameters mup = new MechUnitParameters();
			mup.setName("Custom");
			mup.setIncludedMechWeights(MechUnitParameters.AllMechWeights);
			mup.setMinBV(chosenForceSize.getBV() - 20);
			mup.setMaxBV(chosenForceSize.getBV() + 20);
			mup.setMechCount(chosenForceSize.getNumUnits());
			
			try
			{
				u = UnitManager.getInstance().generateUnit(era, faction, p, unitName, mup, rating, qualityRating, techRating, 0, collection);
				unitBV = u.getUnitBV();
				if (unitBV == 0)
					System.out.println("Unit generation failed");
			} catch (Exception ex)
			{
				// Have another go...
			}
		}
		if (unitBV == 0)
			throw new RuntimeException("Unable to generate opposing force");
		return u;
	}
	
	public Scenario loadScenario(String filename) throws Exception
	{
        String Path = PropertyUtil.getStringProperty("ExternalDataPath", "data") + "/scenarios/";

        SAXBuilder b = new SAXBuilder();
        Document scenarioDoc = b.build(Path + filename);
        return loadScenario(scenarioDoc.getRootElement());
	}
	
	public Scenario loadScenario(org.jdom.Element element)
	{
		Scenario scenario = new Scenario();
		
		scenario.setMission(loadMission(element.getChild("Mission")));
		scenario.setSeason(Season.fromString(element.getChildText("Season")));
		
		Element windElement = element.getChild("Wind");
		if (windElement != null)
		{
			Wind wind = new Wind();
			wind.setStrength(Wind.Strength.fromString(windElement.getAttributeValue("Strength")));
			wind.setDirection(Integer.parseInt(windElement.getAttributeValue("Direction")));
			scenario.setWind(wind);
		}
		scenario.setTimeOfDay(TimeOfDay.fromString(element.getChildText("TimeOfDay")));
		Element mapSetElement = element.getChild("MapSet");
		if (mapSetElement != null)
			scenario.setMapSet(MapManager.getInstance().loadMapSet(mapSetElement));
		
		org.jdom.Element weatherElements = element.getChild("WeatherConditions");
		if (weatherElements != null)
		{
			Iterator<?> iter = weatherElements.getChildren("WeatherCondition").iterator();
			while (iter.hasNext())
			{
				Element weatherElement = (Element)iter.next();
				scenario.getWeatherConditions().add(weatherElement.getText());
			}
		}
		org.jdom.Element terrainElements = element.getChild("TerrainConditions");
		if (terrainElements != null)
		{
			Iterator<?> iter = terrainElements.getChildren("TerrainCondition").iterator();
			while (iter.hasNext())
			{
				Element terrainElement = (Element)iter.next();
				scenario.getTerrainConditions().add(terrainElement.getText());
			}
		}
		org.jdom.Element unitElements = element.getChild("Sides");
		if (unitElements != null)
		{
			Iterator<?> iter = unitElements.getChildren("Side").iterator();
			while (iter.hasNext())
			{
				org.jdom.Element unitElement = (Element)iter.next();
				String team = unitElement.getAttributeValue("Team");
				Unit u = UnitManager.getInstance().loadUnitFromElement(unitElement.getChild("Unit"));
				scenario.getSides().put(team, u);
			}
		}
		
		return scenario;
	}
	
	public Scenario loadScenarioForUnit(Unit u, long missionID) throws Exception
	{
		Mission m = _Missions.get(missionID);
		String filename = createScenarioFilename(m, u, ".xml");
		
		return loadScenario(filename);
	}
	
	public String createScenarioFilename(Mission m, Unit u, String extension)
	{
		return u.getName() + " - " + m.getName() + extension;
	}
	
	public void SaveScenarioForUnit(Unit u, Scenario s) throws Exception
	{
		String filename = createScenarioFilename(s.getMission(), u, ".xml");

		saveScenario(filename, s);
	}
	
	public void saveScenario(String filename, Scenario scenario) throws Exception
	{
        String Path = PropertyUtil.getStringProperty("ExternalDataPath", "data") + "/scenarios/";

		org.jdom.Document doc = new Document();
		doc.setRootElement(saveScenario(scenario));
		
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());            
        out.output(doc, new FileOutputStream(Path + filename));	
		
	}
	
	public org.jdom.Element saveScenario(Scenario scenario)
	{
		org.jdom.Element element = new Element("Scenario");
		
		element.addContent(saveMission(scenario.getMission()));
		element.addContent(new Element("Season").setText(scenario.getSeason().toString()));
		Wind wind = scenario.getWind();
		if (wind != null)
		{
			Element windElement = new Element("Wind");
			windElement.setAttribute("Strength", wind.getStrength().toString());
			windElement.setAttribute("Direction", Integer.toString(wind.getDirection()));
			element.addContent(windElement);
		}
		element.addContent(new Element("TimeOfDay").setText(scenario.getTimeOfDay().toString()));
		if (scenario.getMapSet() != null)
			element.addContent(MapManager.getInstance().saveMapSet(scenario.getMapSet()));
		
		org.jdom.Element weatherElements = new Element("WeatherConditions");
		for (String weatherCondition : scenario.getWeatherConditions())
			weatherElements.addContent(new Element("WeatherCondition").setText(weatherCondition));
		element.addContent(weatherElements);
		
		org.jdom.Element terrainElements = new Element("TerrainConditions");
		for (String terrainCondition : scenario.getTerrainConditions())
			terrainElements.addContent(new Element("TerrainCondition").setText(terrainCondition));
		element.addContent(terrainElements);
		
		org.jdom.Element unitElements = new Element("Sides");
		for (String team : scenario.getSides().keySet())
		{
			org.jdom.Element unitElement = new Element("Side");
			unitElement.setAttribute("Team",team);
			unitElement.addContent(UnitManager.getInstance().saveUnitToElement(scenario.getSides().get(team)));
			unitElements.addContent(unitElement);
		}
		element.addContent(unitElements);
		
		return element;
	}

	public String printScenarioToPDF(String folder, Unit scenarioUnit, int scenarioNumber, Scenario scenario) throws Exception
	{
		folder += "/" + scenarioUnit.getName() + "/Scenario " + Integer.toString(scenarioNumber) + "/";
		

		String filename = folder + "/Scenario.pdf";
		
		printScenarioToPDF(folder, scenarioUnit, scenario, filename);
		return filename;
	}
	
	public void printScenarioToPDF(String folder, Unit scenarioUnit, Scenario scenario, String filename) throws Exception
	{
		File f = new File(folder);
		if (!f.exists())
			f.mkdirs();

		f = new File(filename);
		while (f.exists())
			f.delete();
		
		com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4, 5, 5, 5, 5);
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		
		Paragraph title1 = new Paragraph(scenario.getMission().getName(), FontFactory.getFont(FontFactory.HELVETICA, 24, Font.BOLDITALIC));
		Chapter chapter1 = new Chapter(title1, 1);
		chapter1.setNumberDepth(0);

		chapter1.add(new Paragraph(scenario.getMission().getDescription(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));

		chapter1.add(new Paragraph("Season",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		chapter1.add(new Paragraph(scenario.getSeason().toString(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
		chapter1.add(new Paragraph("Time Of Day",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		chapter1.add(new Paragraph(scenario.getTimeOfDay().toString(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
		chapter1.add(new Paragraph("Wind",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		chapter1.add(new Paragraph(scenario.getWind().toString(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
		
		
		chapter1.add(new Paragraph("Map Set",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		chapter1.add(new Paragraph(scenario.getMapSet().getName(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));

		BufferedImage image = MapLayoutRenderer.getInstance().renderMapLayout(scenario.getMapSet());
		File mapFile = new File(folder + "MapSet.png");
        ImageIO.write(image, "PNG", mapFile);
        Image mapSetImage = Image.getInstance(mapFile.getAbsolutePath());
        mapSetImage.scalePercent(30);
		chapter1.add(mapSetImage);
		mapFile.delete();		
		
		document.add(chapter1);

		/*
		int ChapterNumber = 2;
		for (String teamName : scenario.getSides().keySet())
		{
			Unit u = scenario.getSides().get(teamName);
			PlayerBriefing briefing = scenario.getMission().getBriefing(teamName);

			Chapter chapter = new Chapter(new Paragraph(teamName + " - " + u.getName(),FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)), ChapterNumber++);
						
			chapter.add(new Paragraph("Briefing",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
			chapter.add(new Paragraph(briefing.getBriefing(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
			chapter.add(new Paragraph("Setup",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
			chapter.add(new Paragraph(briefing.getSetup(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
			chapter.add(new Paragraph("Objective",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
			chapter.add(new Paragraph(briefing.getObjective(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
			chapter.add(new Paragraph("Victory Conditions",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
			chapter.add(new Paragraph(briefing.getVictoryConditions(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));

			if (!briefing.getSpecialConditions().isEmpty())
			{
				chapter.add(new Paragraph("Special Conditions",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
				chapter.add(new Paragraph(briefing.getSpecialConditions(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
			}

			if (!briefing.getNotes().isEmpty())
			{
				chapter.add(new Paragraph("Notes",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
				chapter.add(new Paragraph(briefing.getNotes(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
			}

			int elementID = 0;
			for (Battlemech mech : u.getBattlemechs())				
			{
				Mechwarrior warrior = u.getMechwarriorAssignedToMech(mech.getIdentifier());
				image = BattlemechRenderer.getInstance().RenderBattlemech(mech, warrior);
				String mechFilename = folder + teamName + "Element " + Integer.toString(elementID) + ".png";
				File mechFile = new File(mechFilename);
		        ImageIO.write(image, "PNG", mechFile);
		        
		        Image mechImage = Image.getInstance(mechFile.getAbsolutePath());
		        mechImage.scalePercent(48);
				chapter.add(mechImage);
				mechFile.delete();
		        
				elementID++;
			}
			
			document.add(chapter);
		}
		*/

		document.close();
	}

	
	public void printScenarioToHTML(String folder, Unit scenarioUnit, int scenarioNumber, Scenario scenario) throws Exception
	{
		folder += "/" + scenarioUnit.getName() + "/Scenario " + Integer.toString(scenarioNumber) + "/";
		File f = new File(folder);
		if (!f.exists())
			f.mkdirs();		

		
		Tag html = new Tag("html");
		Tag body = new Tag("body");

		// a simple header
		Tag head = new Tag("head");
		Tag title = new Tag("title");
		title.add("CampaignManager Scenario : " + scenario.getMission().getName());
		head.add(title);
		body.add(head);

		// add h1 title
		Tag h1 = new Tag("h1");
		h1.add(scenario.getMission().getName());
		body.add(h1);
		
		Tag h2 = new Tag("h2");
		h2.add("Mission");
		body.add(h2);
		body.add(scenario.getMission().getDescription());

		h2 = new Tag("h3");
		h2.add("Season");
		body.add(h2);
		body.add(scenario.getSeason());
		h2 = new Tag("h3");
		h2.add("Time of Day");
		body.add(h2);
		body.add(scenario.getTimeOfDay());
		h2 = new Tag("h3");
		h2.add("Wind");
		body.add(h2);
		body.add(scenario.getWind());
		h2 = new Tag("h3");
		h2.add("Map Set");
		body.add(h2);
		body.add(scenario.getMapSet().getName());
		body.add(new Tag("br",false));
		
		BufferedImage image = MapLayoutRenderer.getInstance().renderMapLayout(scenario.getMapSet());
        ImageIO.write(image, "PNG", new File(folder + "MapSet.png"));
        
        Tag mapSetTag = new Tag("img","src=\"" + folder + "MapSet.png\" alt=\"" + scenario.getMapSet().getName() + "\"");
        body.add(mapSetTag);
		body.add(new Tag("br",false));
		
		h2 = new Tag("h3");
		h2.add("Notes");
		body.add(h2);
		body.add(scenario.getMission().getNotes());
		
		// add line break (no closing tag) after table
		body.add(new Tag("hr", false));

		/*
		for (String teamName : scenario.getSides().keySet())
		{
			Unit u = scenario.getSides().get(teamName);
			PlayerBriefing briefing = scenario.getMission().getBriefing(teamName);

			h2 = new Tag("h2");
			h2.add(teamName + " - " + u.getName());
			body.add(h2);
						
			h2 = new Tag("h3");
			h2.add("Briefing");
			body.add(h2);
			body.add(briefing.getBriefing());
			
			h2 = new Tag("h3");
			h2.add("Setup");
			body.add(h2);
			body.add(briefing.getSetup());

			h2 = new Tag("h3");
			h2.add("Objective");
			body.add(h2);
			body.add(briefing.getObjective());

			h2 = new Tag("h3");
			h2.add("Victory Conditions");
			body.add(h2);
			body.add(briefing.getVictoryConditions());

			if (!briefing.getSpecialConditions().isEmpty())
			{
				h2 = new Tag("h3");
				h2.add("Special Conditions");
				body.add(h2);
				body.add(briefing.getSpecialConditions());
			}

			if (!briefing.getNotes().isEmpty())
			{
				h2 = new Tag("h3");
				h2.add("Notes");
				body.add(h2);
				body.add(briefing.getNotes());
			}
			body.add(new Tag("br",false));

			int elementID = 0;
			for (Battlemech mech : u.getBattlemechs())				
			{
				Mechwarrior warrior = u.getMechwarriorAssignedToMech(mech.getIdentifier()); 
				image = BattlemechRenderer.getInstance().RenderBattlemech(mech, warrior);
				String mechFilename = folder + teamName + "Element " + Integer.toString(elementID) + ".png";
		        ImageIO.write(image, "PNG", new File(mechFilename));
		        
		        Tag mechTag = new Tag("img","src=\"" + mechFilename + "\" alt=\"" + mech.getDesignVariant() + " " + mech.getDesignName() + "\"");
		        body.add(mechTag);
				body.add(new Tag("br",false));
				elementID++;
			}
			
			body.add(new Tag("hr", false));
		}
		*/
		// simple string


		html.add(body); // no header here
		
		String filename = folder + "/Scenario.html";

		f = new File(filename);
		if (f.exists())
			f.delete();

		FileWriter outFile = new FileWriter(f);
		PrintWriter out = new PrintWriter(outFile);
        out.print(html.toString());	
		outFile.close();
		
	}
	
	public String printMissionDirectly(Unit u, Long missionID) throws Exception
	{
		Mission m = _Missions.get(missionID);
        String Path = PropertyUtil.getStringProperty("ExternalDataPath", "data") + "/scenarios/";
        String pdfFilename = Path + createScenarioFilename(m, u, ".pdf");
        
        Scenario scenario = loadScenarioForUnit(u, missionID);
        printScenarioToPDF(Path,u,scenario,pdfFilename);
        return pdfFilename;
	}
	
	public class ForceSize
	{
		private int _NumUnits;
		private int _BV;
		public int getNumUnits()
		{
			return _NumUnits;
		}
		public void setNumUnits(int numUnits)
		{
			_NumUnits = numUnits;
		}
		public int getBV()
		{
			return _BV;
		}
		public void setBV(int bV)
		{
			_BV = bV;
		}
		
	}
}
