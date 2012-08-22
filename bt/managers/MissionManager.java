package bt.managers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import java.io.FileWriter;
import java.io.PrintWriter;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.lowagie.text.Chapter;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import bt.elements.Battlemech;
import bt.elements.mapping.MapSet;
import bt.elements.mapping.MapSheet;
import bt.elements.missions.Mission;
import bt.elements.missions.MissionMapSet;
import bt.elements.missions.PlayerBriefing;
import bt.elements.personnel.Rating;
import bt.elements.scenario.Scenario;
import bt.elements.scenario.Season;
import bt.elements.scenario.TimeOfDay;
import bt.elements.scenario.Wind;
import bt.elements.unit.MechUnitParameters;
import bt.elements.unit.Player;
import bt.elements.unit.Unit;
import bt.html.Tag;
import bt.ui.BattlemechRenderer;
import bt.ui.MapLayoutRenderer;
import bt.util.Dice;
import bt.util.ExceptionUtil;

public class MissionManager
{
	private static MissionManager theInstance = new MissionManager();
    private Log log = LogFactory.getLog(MissionManager.class);
    
    private HashMap<String,Mission> _Missions = new HashMap<String,Mission>();

	private MissionManager()
	{
		try
		{
	   		log.info("Initialising MissionManager");
			loadMissions();
		}
		catch (Exception ex)
		{
			log.fatal(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}
	
	public static MissionManager getInstance()
	{ return theInstance; }
	
	private void loadMissions() throws Exception
	{
		_Missions.clear();
		
        String Path = System.getProperty("BaseDataPath");
        if (Path == null)
            Path = "data/";
        
        try {            
            SAXBuilder b = new SAXBuilder();
            Document mapDoc = b.build(new File(Path, "Missions.xml"));
            Element root = mapDoc.getRootElement();
            Iterator<?> iter = root.getChildren("Mission").iterator();
            while (iter.hasNext())
            {
            	org.jdom.Element missionElement = (org.jdom.Element)iter.next();
            	Mission ms = loadMission(missionElement);
            	_Missions.put(ms.getName(),ms);            	
            }
    	
        } catch(java.io.IOException ex) {
            log.info("Error Opening Player File!");
            log.error(ex);
        } catch (JDOMException jdex) {
            log.info("Failure Parsing Player File!");
            log.error(jdex);
        }	
	}
	
	private Mission loadMission(org.jdom.Element element)
	{
		Mission m = new Mission();
		
		m.setID(Long.parseLong(element.getAttributeValue("ID")));
		m.setName(element.getAttributeValue("Name"));
		
		if (element.getChild("Description") != null)
			m.setDescription(element.getChildText("Description"));
		if (element.getChild("ForceRatio") != null)
			if (!element.getChildText("ForceRatio").isEmpty())
				m.setForceRatio(Float.parseFloat(element.getChildText("ForceRatio")));
		if (element.getChild("Notes") != null)
			m.setNotes(element.getChildText("Notes"));
		
		if (element.getChild("MapSet") != null)
		{
			org.jdom.Element mapSetElement = element.getChild("MapSet");
			MissionMapSet mms = new MissionMapSet();
			mms.setMaps(Integer.parseInt(mapSetElement.getAttributeValue("Maps")));
			mms.setRows(Integer.parseInt(mapSetElement.getAttributeValue("Rows")));
			mms.setColumns(Integer.parseInt(mapSetElement.getAttributeValue("Columns")));
			
			m.setMapSet(mms);
		}
		
		Iterator<?> iter = element.getChildren("PlayerBriefing").iterator();
		while(iter.hasNext())
		{
			org.jdom.Element briefingElement = (org.jdom.Element)iter.next();
			PlayerBriefing pb = new PlayerBriefing();
			
			pb.setTeam(briefingElement.getAttributeValue("Team"));
			pb.setBriefing(briefingElement.getChildText("Briefing"));
			if (briefingElement.getChild("Setup") != null)
				pb.setSetup(briefingElement.getChildText("Setup"));
			if (briefingElement.getChild("Objective") != null)
				pb.setObjective(briefingElement.getChildText("Objective"));
			if (briefingElement.getChild("VictoryConditions") != null)
				pb.setVictoryConditions(briefingElement.getChildText("VictoryConditions"));
			if (briefingElement.getChild("SpecialConditions") != null)
				pb.setSpecialConditions(briefingElement.getChildText("SpecialConditions"));
			if (briefingElement.getChild("Notes") != null)
				pb.setNotes(briefingElement.getChildText("Notes"));
			
			m.getBriefings().add(pb);
		}
		
		return m;
	}
	
	public org.jdom.Element saveMission(Mission mission)
	{
		org.jdom.Element missionElement = new org.jdom.Element("Mission");
		
		missionElement.setAttribute("ID", Long.toString(mission.getID()));
		missionElement.setAttribute("Name",mission.getName());
		
		missionElement.addContent(new org.jdom.Element("Description").setText(mission.getDescription()));
		missionElement.addContent(new org.jdom.Element("ForceRatio").setText(Float.toString(mission.getForceRatio())));
		missionElement.addContent(new org.jdom.Element("Notes").setText(mission.getNotes()));
		
		MissionMapSet mms = mission.getMapSet(); 
		if (mms != null)
		{
			org.jdom.Element mapSetElement = new Element("MapSet");
			mapSetElement.setAttribute("Maps", Integer.toString(mms.getMaps()));
			mapSetElement.setAttribute("Rows", Integer.toString(mms.getRows()));
			mapSetElement.setAttribute("Columns", Integer.toString(mms.getColumns()));
			missionElement.addContent(mapSetElement);
		}
		
		for (PlayerBriefing briefing : mission.getBriefings())
		{
			org.jdom.Element briefingElement = new Element("PlayerBriefing");
			briefingElement.setAttribute("Team", briefing.getTeam());
			briefingElement.addContent(new Element("Briefing").setText(briefing.getBriefing()));
			briefingElement.addContent(new Element("Setup").setText(briefing.getSetup()));
			briefingElement.addContent(new Element("Objective").setText(briefing.getObjective()));
			briefingElement.addContent(new Element("VictoryConditions").setText(briefing.getVictoryConditions()));
			briefingElement.addContent(new Element("SpecialConditions").setText(briefing.getSpecialConditions()));
			briefingElement.addContent(new Element("Notes").setText(briefing.getNotes()));
			missionElement.addContent(briefingElement);
		}
		
		return missionElement;
	}
	
	public Vector<String> getMissionList()
	{
		return new Vector<String>(_Missions.keySet()); 
	}
	
	public Mission getRandomMission(Vector<String> excludeMissions)
	{
		Vector<String> eligibleMissions = new Vector<String>(_Missions.keySet());
		eligibleMissions.removeAll(excludeMissions);
		if (eligibleMissions.size() > 0)
		{
			int randomMission = Dice.random(eligibleMissions.size()) - 1;
			return new Mission(_Missions.get(eligibleMissions.elementAt(randomMission)));
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
		MapSet mapSet = s.getMapSet();
		for (PlayerBriefing briefing : m.getBriefings())
		{			
			briefing.setSetup(substituteMapLocations(briefing.getSetup(), mapSet));
			briefing.setObjective(substituteMapLocations(briefing.getObjective(), mapSet));
			briefing.setVictoryConditions(substituteMapLocations(briefing.getVictoryConditions(), mapSet));
			briefing.setSpecialConditions(substituteMapLocations(briefing.getSpecialConditions(), mapSet));
		}
	}
	
	
	public Scenario generateScenario(Unit u)
	{
		Scenario scenario = new Scenario();
		
		Mission m = getRandomMission(u.getAssignedMissions());
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
		
		Unit opposingUnit = generateOpposingUnit(forceBV, forceSize);
		
		scenario.getSides().put(sideName, u);
		scenario.getSides().put(oppositionName, opposingUnit);
		
		return scenario;
	}
	
	private Unit generateOpposingUnit(int forceBV, int unitStrength)
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
				
				log.debug("Force option ->  NumUnits:" + fs.getNumUnits() + " BV:" + fs.getBV());
			}
		}
		int unitBV = 0;
		Unit u = null;
		while (unitBV == 0 && forceSizes.size() > 0)
		{
			ForceSize chosenForceSize = forceSizes.elementAt(Dice.random(forceSizes.size())- 1);
			log.debug("SELECTED Force option ->  NumUnits:" + chosenForceSize.getNumUnits() + " BV:" + chosenForceSize.getBV());
			
			forceSizes.remove(chosenForceSize);
			
			MechUnitParameters mup = new MechUnitParameters();
			mup.setName("Custom");
			mup.setIncludedMechWeights(MechUnitParameters.AllMechWeights);
			mup.setMinBV(chosenForceSize.getBV() - 10000);
			mup.setMaxBV(chosenForceSize.getBV() + 10000);
			mup.setMechCount(chosenForceSize.getNumUnits());
			
			try
			{
				u = UnitManager.getInstance().GenerateUnit(p, unitName, mup, Rating.REGULAR);
				unitBV = u.getUnitBV();
				if (unitBV == 0)
					log.debug("Unit generation failed");
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		if (unitBV == 0)
			throw new RuntimeException("Unable to generate opposing force");
		return u;
	}
	
	public Scenario loadScenario(String filename) throws Exception
	{
        SAXBuilder b = new SAXBuilder();
        Document mapDoc = b.build(filename);
        return loadScenario(mapDoc.getRootElement());
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
	
	public void saveScenario(String filename, Scenario scenario) throws Exception
	{
		org.jdom.Document doc = new Document();
		doc.setRootElement(saveScenario(scenario));
		
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());            
        out.output(doc, new FileOutputStream(filename));	
		
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
	
	public void printScenarioToPDF(String folder, Unit scenarioUnit, int scenarioNumber, Scenario scenario) throws Exception
	{
		folder += "/" + scenarioUnit.getName() + "/Scenario " + Integer.toString(scenarioNumber) + "/";
		File f = new File(folder);
		if (!f.exists())
			f.mkdirs();		

		String filename = folder + "/Scenario.pdf";

		f = new File(filename);
		if (f.exists())
			f.delete();
		
		com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A4, 5, 5, 5, 5);
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		
		Paragraph title1 = new Paragraph(scenario.getMission().getName(), FontFactory.getFont(FontFactory.HELVETICA, 24, Font.BOLDITALIC, Color.black));
		Chapter chapter1 = new Chapter(title1, 1);
		chapter1.setNumberDepth(0);

		chapter1.add(new Paragraph(scenario.getMission().getDescription(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.black)));

		chapter1.add(new Paragraph("Season",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, Color.black)));
		chapter1.add(new Paragraph(scenario.getSeason().toString(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.black)));
		chapter1.add(new Paragraph("Time Of Day",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, Color.black)));
		chapter1.add(new Paragraph(scenario.getTimeOfDay().toString(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.black)));
		chapter1.add(new Paragraph("Wind",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, Color.black)));
		chapter1.add(new Paragraph(scenario.getWind().toString(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.black)));
		
		
		chapter1.add(new Paragraph("Map Set",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, Color.black)));
		chapter1.add(new Paragraph(scenario.getMapSet().getName(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.black)));

		BufferedImage image = MapLayoutRenderer.getInstance().renderMapLayout(scenario.getMapSet());
		File mapFile = new File(folder + "MapSet.png");
        ImageIO.write(image, "PNG", mapFile);
        Image mapSetImage = Image.getInstance(mapFile.getAbsolutePath());
        mapSetImage.scalePercent(30);
		chapter1.add(mapSetImage);
		mapFile.delete();		
		
		document.add(chapter1);

		int ChapterNumber = 2;
		for (String teamName : scenario.getSides().keySet())
		{
			Unit u = scenario.getSides().get(teamName);
			PlayerBriefing briefing = scenario.getMission().getBriefing(teamName);

			Chapter chapter = new Chapter(new Paragraph(teamName + " - " + u.getName(),FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, Color.black)), ChapterNumber++);
						
			chapter.add(new Paragraph("Briefing",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, Color.black)));
			chapter.add(new Paragraph(briefing.getBriefing(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.black)));
			chapter.add(new Paragraph("Setup",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, Color.black)));
			chapter.add(new Paragraph(briefing.getSetup(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.black)));
			chapter.add(new Paragraph("Objective",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, Color.black)));
			chapter.add(new Paragraph(briefing.getObjective(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.black)));
			chapter.add(new Paragraph("Victory Conditions",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, Color.black)));
			chapter.add(new Paragraph(briefing.getVictoryConditions(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.black)));

			if (!briefing.getSpecialConditions().isEmpty())
			{
				chapter.add(new Paragraph("Special Conditions",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, Color.black)));
				chapter.add(new Paragraph(briefing.getSpecialConditions(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.black)));
			}

			if (!briefing.getNotes().isEmpty())
			{
				chapter.add(new Paragraph("Notes",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, Color.black)));
				chapter.add(new Paragraph(briefing.getNotes(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.black)));
			}

			int elementID = 0;
			for (Battlemech mech : u.getBattlemechs())				
			{
				image = BattlemechRenderer.getInstance().RenderBattlemech(mech);
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
				image = BattlemechRenderer.getInstance().RenderBattlemech(mech);
				String mechFilename = folder + teamName + "Element " + Integer.toString(elementID) + ".png";
		        ImageIO.write(image, "PNG", new File(mechFilename));
		        
		        Tag mechTag = new Tag("img","src=\"" + mechFilename + "\" alt=\"" + mech.getDesignVariant() + " " + mech.getDesignName() + "\"");
		        body.add(mechTag);
				body.add(new Tag("br",false));
				elementID++;
			}
			
			body.add(new Tag("hr", false));
		}
		
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
