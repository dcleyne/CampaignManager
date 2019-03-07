package bt.managers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
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
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import bt.elements.Asset;
import bt.elements.Battlemech;
import bt.elements.Era;
import bt.elements.Faction;
import bt.elements.collection.ItemCollection;
import bt.elements.missions.Mission;
import bt.elements.missions.Objective;
import bt.elements.missions.OpponentBriefing;
import bt.elements.missions.PlayerBriefing;
import bt.elements.missions.PreviousSuccessfulPlayerMission;
import bt.elements.missions.SpecialRule;
import bt.elements.missions.Warchest;
import bt.elements.missions.WarchestOption;
import bt.elements.personnel.Mechwarrior;
import bt.elements.personnel.Rating;
import bt.elements.scenario.Opponent;
import bt.elements.scenario.Scenario;
import bt.elements.unit.Force;
import bt.elements.unit.MechUnitParameters;
import bt.elements.unit.Player;
import bt.elements.unit.QualityRating;
import bt.elements.unit.TechRating;
import bt.elements.unit.Unit;
import bt.mapping.MapSet;
import bt.mapping.MapSheet;
import bt.ui.renderers.BattlemechRenderer;
import bt.ui.renderers.MapLayoutRenderer;
import bt.util.Dice;
import bt.util.PropertyUtil;

public class MissionManager
{
	private static MissionManager theInstance = new MissionManager();

	private HashMap<Long, Mission> _Missions = new HashMap<Long, Mission>();

	private MissionManager()
	{
		try
		{
			loadMissions();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static MissionManager getInstance()
	{
		return theInstance;
	}

	private void loadMissions() throws Exception
	{
		_Missions.clear();

		String Path = PropertyUtil.getStringProperty("DataPath", "data");

		try
		{
			SAXBuilder b = new SAXBuilder();
			Document mapDoc = b.build(new File(Path, "/Missions.xml"));
			Element root = mapDoc.getRootElement();
			Iterator<?> iter = root.getChildren("Mission").iterator();
			while (iter.hasNext())
			{
				org.jdom.Element missionElement = (org.jdom.Element) iter.next();
				Mission ms = loadMission(missionElement);
				_Missions.put(ms.getID(), ms);
			}

		} catch (java.io.IOException ex)
		{
			System.out.println("Error Opening Missions File!");
			ex.printStackTrace();
		} catch (JDOMException jdex)
		{
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

		if (element.getChild("Notes") != null) m.setNotes(element.getChildText("Notes"));

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
			pb.setForceRestriction(Float.parseFloat(briefingElement.getChildText("ForceRestriction")));
			m.setPlayerBriefing(pb);
		}
		if (element.getChild("OpponentBriefing") != null)
		{
			org.jdom.Element briefingElement = element.getChild("OpponentBriefing");
			OpponentBriefing ob = new OpponentBriefing();

			ob.setTeam(briefingElement.getAttributeValue("Team"));
			ob.setBriefing(briefingElement.getChildText("Briefing"));

			org.jdom.Element previousMissionElement = briefingElement.getChild("PreviousSuccessfulPlayerMission");
			PreviousSuccessfulPlayerMission pspm = new PreviousSuccessfulPlayerMission();
			pspm.setModifier(Integer.parseInt(previousMissionElement.getAttributeValue("Modifier")));
			String missionList = "";
			if (previousMissionElement.getAttribute("Excludes") != null)
			{
				pspm.setExclusive();
				missionList = previousMissionElement.getAttributeValue("Excludes");
			} else
			{
				pspm.setInclusive();
				missionList = previousMissionElement.getAttributeValue("Includes");
			}
			for (String s : missionList.split(","))
			{
				if (!s.trim().isEmpty())
				{
					pspm.addMission(s.trim());
				}
			}
			ob.setPreviousSuccessfulPlayerMission(pspm);

			org.jdom.Element techRatingTableElement = briefingElement.getChild("TechRatingTable");
			{
				Iterator<?> iter = techRatingTableElement.getChildren("Option").iterator();
				while (iter.hasNext())
				{
					org.jdom.Element optionElement = (org.jdom.Element) iter.next();
					int roll = Integer.parseInt(optionElement.getAttributeValue("Roll"));
					TechRating tr = TechRating.fromString(optionElement.getAttributeValue("Tech"));
					ob.setTechRating(roll, tr);
				}
			}

			org.jdom.Element forceRatioTableElement = briefingElement.getChild("ForceRatioTable");
			{
				Iterator<?> iter = forceRatioTableElement.getChildren("Option").iterator();
				while (iter.hasNext())
				{
					org.jdom.Element optionElement = (org.jdom.Element) iter.next();
					int roll = Integer.parseInt(optionElement.getAttributeValue("Roll"));
					float ratio = Float.parseFloat(optionElement.getAttributeValue("Ratio"));
					ob.setForceRatio(roll, ratio);
				}
			}

			org.jdom.Element unitCompositionTableElement = briefingElement.getChild("UnitCompositionTable");
			{
				Iterator<?> iter = unitCompositionTableElement.getChildren("Option").iterator();
				while (iter.hasNext())
				{
					org.jdom.Element optionElement = (org.jdom.Element) iter.next();
					int roll = Integer.parseInt(optionElement.getAttributeValue("Roll"));
					String composition = optionElement.getAttributeValue("Composition");
					ob.setUnitComposition(roll, composition);
				}
			}

			org.jdom.Element randomAllocationTableElement = briefingElement.getChild("RandomAllocationTable");
			{
				Iterator<?> iter = randomAllocationTableElement.getChildren("Option").iterator();
				while (iter.hasNext())
				{
					org.jdom.Element optionElement = (org.jdom.Element) iter.next();
					int roll = Integer.parseInt(optionElement.getAttributeValue("Roll"));
					String rat = optionElement.getAttributeValue("RAT");
					ob.setRandomAllocation(roll, rat);
				}
			}

			org.jdom.Element skillLevelTableElement = briefingElement.getChild("SkillLevelTable");
			{
				Iterator<?> iter = skillLevelTableElement.getChildren("Option").iterator();
				while (iter.hasNext())
				{
					org.jdom.Element optionElement = (org.jdom.Element) iter.next();
					int roll = Integer.parseInt(optionElement.getAttributeValue("Roll"));
					String level = optionElement.getAttributeValue("Level");
					ob.setSkillLevel(roll, level);
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
				org.jdom.Element optionElement = (org.jdom.Element) iter.next();
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
			org.jdom.Element objectiveElement = (org.jdom.Element) iter.next();
			Objective o = new Objective();

			o.setName(objectiveElement.getAttributeValue("Name"));
			o.setDescription(objectiveElement.getAttributeValue("Description"));
			o.setPointsAwarded(Integer.parseInt(objectiveElement.getAttributeValue("PointsAwarded")));

			m.addObjective(o);
		}

		iter = element.getChildren("SpecialRule").iterator();
		while (iter.hasNext())
		{
			org.jdom.Element specialRuleElement = (org.jdom.Element) iter.next();
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
		missionElement.setAttribute("Name", mission.getName());
		missionElement.setAttribute("Description", mission.getDescription());

		missionElement.addContent(new org.jdom.Element("GameSetup").setText(mission.getGameSetup()));
		missionElement.addContent(new org.jdom.Element("Notes").setText(mission.getNotes()));

		org.jdom.Element mapSetElement = new org.jdom.Element("MapSet");
		mapSetElement.setAttribute("Terrain", mission.getMapSetTerrain());
		missionElement.addContent(mapSetElement);

		PlayerBriefing playerBriefing = mission.getPlayerBriefing();
		org.jdom.Element playerBriefingElement = new Element("PlayerBriefing");
		playerBriefingElement.setAttribute("Team", playerBriefing.getTeam());
		playerBriefingElement.addContent(new Element("Briefing").setText(playerBriefing.getBriefing()));
		playerBriefingElement.addContent(
				new Element("ForceRestriction").setText(Float.toString(playerBriefing.getForceRestriction())));
		missionElement.addContent(playerBriefingElement);

		OpponentBriefing opponentBriefing = mission.getOpponentBriefing();
		org.jdom.Element opponentBriefingElement = new Element("OpponentBriefing");
		opponentBriefingElement.setAttribute("Team", opponentBriefing.getTeam());
		opponentBriefingElement.addContent(new Element("Briefing").setText(opponentBriefing.getBriefing()));
		opponentBriefingElement.addContent(new Element("Notes").setText(opponentBriefing.getNotes()));

		PreviousSuccessfulPlayerMission pspm = opponentBriefing.getPreviousSuccessfulPlayerMission();
		org.jdom.Element previousSuccessfulPlayerMissionElement = new Element("PreviousSuccessfulPlayerMission");
		previousSuccessfulPlayerMissionElement.setAttribute("Modifier", Integer.toString(pspm.getModifier()));
		if (pspm.isInclusive())
			previousSuccessfulPlayerMissionElement.setAttribute("Includes", pspm.getList());
		else
			previousSuccessfulPlayerMissionElement.setAttribute("Excludes", pspm.getList());

		opponentBriefingElement.addContent(previousSuccessfulPlayerMissionElement);

		org.jdom.Element techRatingTableElement = new Element("TechRatingTable");
		for (int i = 1; i < 9; i++)
		{
			org.jdom.Element optionElement = new org.jdom.Element("Option");
			optionElement.setAttribute("Roll", Integer.toString(i));
			optionElement.setAttribute("Tech", opponentBriefing.getTechRating(i).toString());
			techRatingTableElement.addContent(optionElement);
		}
		opponentBriefingElement.addContent(techRatingTableElement);

		org.jdom.Element forceRatioTableElement = new Element("ForceRatioTable");
		for (int i = 1; i < 9; i++)
		{
			org.jdom.Element optionElement = new org.jdom.Element("Option");
			optionElement.setAttribute("Roll", Integer.toString(i));
			optionElement.setAttribute("Ratio", Float.toString(opponentBriefing.getForceRatio(i)));
			forceRatioTableElement.addContent(optionElement);
		}
		opponentBriefingElement.addContent(forceRatioTableElement);

		org.jdom.Element unitCompositionTableElement = new Element("UnitCompositionTable");
		for (int i = 1; i < 9; i++)
		{
			org.jdom.Element optionElement = new org.jdom.Element("Option");
			optionElement.setAttribute("Roll", Integer.toString(i));
			optionElement.setAttribute("Composition", opponentBriefing.getUnitComposition(i));
			unitCompositionTableElement.addContent(optionElement);
		}
		opponentBriefingElement.addContent(unitCompositionTableElement);

		org.jdom.Element randomAllocationTableElement = new Element("RandomAllocationTable");
		for (int i = 1; i < 9; i++)
		{
			org.jdom.Element optionElement = new org.jdom.Element("Option");
			optionElement.setAttribute("Roll", Integer.toString(i));
			optionElement.setAttribute("RAT", opponentBriefing.getRandomAllocation(i));
			randomAllocationTableElement.addContent(optionElement);
		}
		opponentBriefingElement.addContent(randomAllocationTableElement);

		org.jdom.Element skillLevelTableElement = new Element("SkillLevelTable");
		for (int i = 1; i < 9; i++)
		{
			org.jdom.Element optionElement = new org.jdom.Element("Option");
			optionElement.setAttribute("Roll", Integer.toString(i));
			optionElement.setAttribute("Level", opponentBriefing.getSkillLevel(i));
			skillLevelTableElement.addContent(optionElement);
		}
		opponentBriefingElement.addContent(skillLevelTableElement);
		missionElement.addContent(opponentBriefingElement);

		Warchest warchest = mission.getWarchest();
		org.jdom.Element warchestElement = new Element("Warchest");
		warchestElement.setAttribute("MissionCost", Integer.toString(warchest.getMissionCost()));

		for (WarchestOption wo : warchest.getOptions())
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

		for (SpecialRule sr : mission.getSpecialRules())
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
			if (m.getName().equalsIgnoreCase(missionName)) return m;
		}
		return null;
	}

	public Mission getRandomMission()
	{
		ArrayList<Long> eligibleMissions = new ArrayList<Long>(_Missions.keySet());
		int randomMission = Dice.random(eligibleMissions.size()) - 1;
		return new Mission(_Missions.get(eligibleMissions.get(randomMission)));
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
			double FinalPercentageModifier = (Math.min(RawForcePercentageMultiplier, BaseForcePercentageMultiplier)
					+ 100) / 100;

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

	public String substituteMapLocations(String setupText, MapSet mapSet)
	{
		CharSequence cs = "[MAPHEXES";
		if (setupText.contains(cs))
		{
			int startIndex = setupText.indexOf(cs.toString());
			int dashIndex = setupText.indexOf("-", startIndex);
			int endIndex = setupText.indexOf("]", startIndex);

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

	public Scenario generateScenario(Era era, Mission mission, LocalDate missionDate, Force playerForce, Opponent opponent,
			ItemCollection collection) throws Exception
	{
		Scenario scenario = new Scenario();
		collection.resetCollection();

		scenario.setMission(mission);
		int numMaps = playerForce.getAssetCount() * 2 / 4;

		scenario.setMapSet(MapManager.getInstance().generateRandomMapSet(mission.getMapSetTerrain(), numMaps));
		scenario.generateOpponentValues();

		Force opponentForce = new Force();
		opponentForce.setCurrentDate(missionDate);
		opponentForce.setParentUnit(opponent.getFaction().toString());
		opponentForce.setTechRating(opponent.getTechRating());
		opponentForce.setQualityRating(opponent.getQualityRating());
		opponentForce.setItemCollectionName(collection.getName());

		int opponentBV = (int) (scenario.getOpponentForceRatio() * playerForce.getAssetBV());
		if (opponentBV == 0 || playerForce.getAssetCount() == 0)
		{
			throw new Exception("Player force is too small to generate Scenario");
		}
		int bvPerMech = opponentBV / playerForce.getAssetCount();
		int mechsRemaining = playerForce.getAssetCount();
		while (mechsRemaining > 0)
		{
			int mechsToAdd = Math.min(mechsRemaining, 4);
			int bvToAdd = mechsToAdd * bvPerMech;
			opponentForce.mergeUnit(generateOpposingUnit(era, opponent.getFaction(), bvToAdd, mechsToAdd,
					opponent.getRating(), opponent.getQualityRating(), opponent.getTechRating(), collection));

			mechsRemaining -= mechsToAdd;
		}

		if (mission.getPlayerBriefing().getTeam().equalsIgnoreCase("Attacker"))
		{
			scenario.setAttacker(playerForce);
			scenario.setDefender(opponentForce);
		} else
		{
			scenario.setAttacker(opponentForce);
			scenario.setDefender(playerForce);
		}
		return scenario;
	}

	private Unit generateOpposingUnit(Era era, Faction faction, int forceBV, int unitStrength, Rating rating,
			QualityRating qualityRating, TechRating techRating, ItemCollection collection)
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
				fs.setBV(getFinalBattleValue(unitStrength, forceBV, st));
				forceSizes.add(fs);

				System.out.println("Force option ->  NumUnits:" + fs.getNumUnits() + " BV:" + fs.getBV());
			}
		}
		int unitBV = 0;
		Unit u = null;
		while (unitBV == 0 && forceSizes.size() > 0)
		{
			ForceSize chosenForceSize = forceSizes.elementAt(Dice.random(forceSizes.size()) - 1);
			System.out.println("SELECTED Force option ->  NumUnits:" + chosenForceSize.getNumUnits() + " BV:"
					+ chosenForceSize.getBV());

			forceSizes.remove(chosenForceSize);

			MechUnitParameters mup = new MechUnitParameters();
			mup.setName("Custom");
			mup.setIncludedMechWeights(MechUnitParameters.AllMechWeights);
			mup.setMinBV(chosenForceSize.getBV() - 20);
			mup.setMaxBV(chosenForceSize.getBV() + 20);
			mup.setMechCount(chosenForceSize.getNumUnits());

			try
			{
				u = UnitManager.getInstance().generateUnit(era, faction, p, unitName, mup, rating, qualityRating,
						techRating, collection);
				unitBV = u.getUnitBV();
				if (unitBV == 0) System.out.println("Unit generation failed");
			} catch (Exception ex)
			{
				// Have another go...
			}
		}
		if (unitBV == 0) throw new RuntimeException("Unable to generate opposing force");

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

		Element mapSetElement = element.getChild("MapSet");
		if (mapSetElement != null) scenario.setMapSet(MapManager.getInstance().loadMapSet(mapSetElement));

		org.jdom.Element unitElement = element.getChild("Attacker");
		if (unitElement != null)
		{
			Force f = UnitManager.getInstance().loadForceFromElement(unitElement);
			scenario.setAttacker(f);
		}

		unitElement = element.getChild("Defender");
		if (unitElement != null)
		{
			Force f = UnitManager.getInstance().loadForceFromElement(unitElement);
			scenario.setDefender(f);
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
		if (scenario.getMapSet() != null) element.addContent(MapManager.getInstance().saveMapSet(scenario.getMapSet()));

		element.addContent(UnitManager.getInstance().saveForceToElement(scenario.getAttacker(), "Attacker"));
		element.addContent(UnitManager.getInstance().saveForceToElement(scenario.getDefender(), "Defender"));

		return element;
	}

	public String printScenarioToPDF(String unitName, int scenarioNumber, Scenario scenario) throws Exception
	{
		String folder = PropertyUtil.getStringProperty("ExternalDataPath", "data") + "/scenarios/";
		folder += "/" + unitName + "/Scenario " + Integer.toString(scenarioNumber) + "/";

		String filename = folder + "/Scenario.pdf";
		System.out.println(filename);
		printScenarioToPDF(folder, scenario, filename);
		return filename;
	}

	public void printScenarioToPDF(String folder, Scenario scenario, String filename) throws Exception
	{
		File f = new File(folder);
		if (!f.exists()) f.mkdirs();

		f = new File(filename);
		while (f.exists())
			f.delete();

		com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4, 5, 5, 5, 5);
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();

		Paragraph title1 = new Paragraph(scenario.getMission().getName(),
				FontFactory.getFont(FontFactory.HELVETICA, 24, Font.BOLDITALIC));
		Chapter chapter1 = new Chapter(title1, 1);
		chapter1.setNumberDepth(0);

		chapter1.add(new Paragraph(scenario.getMission().getDescription(),
				FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));

		chapter1.add(new Paragraph("Game Setup", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		chapter1.add(new Paragraph(scenario.getMission().getGameSetup(),
				FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));

		chapter1.add(new Paragraph("Map Set", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		chapter1.add(new Paragraph(scenario.getMapSet().getName(),
				FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));

		BufferedImage image = MapLayoutRenderer.getInstance().renderMapLayout(scenario.getMapSet());
		File mapFile = new File(folder + "MapSet.png");
		ImageIO.write(image, "PNG", mapFile);
		Image mapSetImage = Image.getInstance(mapFile.getAbsolutePath());
		mapSetImage.scalePercent(30);
		chapter1.add(mapSetImage);
		mapFile.delete();

		chapter1.add(new Paragraph("Warchest", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		Paragraph warchestPara = new Paragraph("Track Cost:",
				FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD));
		warchestPara.add(new Chunk(Integer.toString(scenario.getMission().getWarchest().getMissionCost()),
				FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
		chapter1.add(warchestPara);

		chapter1.add(new Paragraph("Options", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		for (WarchestOption option : scenario.getMission().getWarchest().getOptions())
		{
			Paragraph optionPara = new Paragraph("[" + Integer.toString(option.getBonus()) + "] ",
					FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD));
			optionPara.add(new Chunk(option.getName(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
			optionPara.add(new Chunk(":", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
			optionPara.add(new Chunk(option.getNotes(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
			chapter1.add(optionPara);
		}

		chapter1.add(new Paragraph("Objectives", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		for (Objective obj : scenario.getMission().getObjectives())
		{
			Paragraph objectivePara = new Paragraph(obj.getName(),
					FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD));
			objectivePara.add(new Chunk(" : ", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
			objectivePara.add(new Chunk(obj.getDescription(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
			objectivePara.add(new Chunk(" ", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
			objectivePara.add(new Chunk("[" + Integer.toString(obj.getPointsAwarded()) + "]",
					FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
			chapter1.add(objectivePara);
		}

		chapter1.add(new Paragraph("Special Rules", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		for (SpecialRule specialRule : scenario.getMission().getSpecialRules())
		{
			Paragraph objectivePara = new Paragraph(specialRule.getName(),
					FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD));
			objectivePara.add(new Chunk(" : ", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
			objectivePara.add(new Chunk(specialRule.getDescription(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
			chapter1.add(objectivePara);
		}

		document.add(chapter1);

		Mission mission = scenario.getMission();
		Force playerForce = mission.getPlayerBriefing().getTeam().equalsIgnoreCase("Attacker") ? scenario.getAttacker()
				: scenario.getDefender();
		Chapter chapter2 = new Chapter(
				new Paragraph(mission.getPlayerBriefing().getTeam() + " - " + playerForce.getParentUnit(),
						FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)), 2);

		chapter2.add(new Paragraph("Briefing", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		chapter2.add(new Paragraph(mission.getPlayerBriefing().getBriefing(),
				FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
		chapter2.add(new Paragraph("Miniature Collection", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		chapter2.add(new Paragraph(playerForce.getItemCollectionName(),
				FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));

		int elementID = 0;
		for (Asset asset : playerForce.getAssets())
		{
			if (asset instanceof Battlemech)
			{
				Battlemech mech = (Battlemech) asset;
				Mechwarrior warrior = (Mechwarrior) playerForce.getMechwarriorAssignedToMech(mech.getIdentifier());
				image = BattlemechRenderer.getInstance().RenderBattlemech(mech, warrior);
				String mechFilename = folder + mission.getPlayerBriefing().getTeam() + "-Element "
						+ Integer.toString(elementID) + ".png";
				File mechFile = new File(mechFilename);
				ImageIO.write(image, "PNG", mechFile);

				Image mechImage = Image.getInstance(mechFile.getAbsolutePath());
				mechImage.scalePercent(48);
				chapter2.add(mechImage);
				mechFile.delete();

				elementID++;
			}
		}
		document.add(chapter2);

		
		
		Force opponentForce = mission.getOpponentBriefing().getTeam().equalsIgnoreCase("Attacker") ? scenario.getAttacker()
				: scenario.getDefender();
		Chapter chapter3 = new Chapter(
				new Paragraph(mission.getOpponentBriefing().getTeam() + " - " + opponentForce.getParentUnit(),
						FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)), 3);

		chapter3.add(new Paragraph("Briefing", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		chapter3.add(new Paragraph(mission.getOpponentBriefing().getBriefing(),
				FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
		chapter3.add(new Paragraph("Miniature Collection", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		chapter3.add(new Paragraph(opponentForce.getItemCollectionName(),
				FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));

		elementID = 0;
		for (Asset asset : opponentForce.getAssets())
		{
			if (asset instanceof Battlemech)
			{
				Battlemech mech = (Battlemech) asset;
				Mechwarrior warrior = (Mechwarrior) opponentForce.getMechwarriorAssignedToMech(mech.getIdentifier());
				image = BattlemechRenderer.getInstance().RenderBattlemech(mech, warrior);
				String mechFilename = folder + mission.getOpponentBriefing().getTeam() + "-Element "
						+ Integer.toString(elementID) + ".png";
				File mechFile = new File(mechFilename);
				ImageIO.write(image, "PNG", mechFile);

				Image mechImage = Image.getInstance(mechFile.getAbsolutePath());
				mechImage.scalePercent(48);
				chapter3.add(mechImage);
				mechFile.delete();

				elementID++;
			}
		}

		document.add(chapter3);

		document.close();
	}

	public String printMissionDirectly(Unit u, Long missionID) throws Exception
	{
		Mission m = _Missions.get(missionID);
		String Path = PropertyUtil.getStringProperty("ExternalDataPath", "data") + "/scenarios/";
		String pdfFilename = Path + createScenarioFilename(m, u, ".pdf");

		Scenario scenario = loadScenarioForUnit(u, missionID);
		printScenarioToPDF(Path, scenario, pdfFilename);
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
