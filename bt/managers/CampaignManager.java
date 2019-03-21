package bt.managers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import bt.elements.Era;
import bt.elements.Faction;
import bt.elements.campaign.Campaign;
import bt.elements.campaign.CampaignLocation;
import bt.elements.campaign.CampaignScenario;
import bt.elements.campaign.CampaignScenario.OutcomeType;
import bt.elements.campaign.CampaignScenarioOutcome;
import bt.elements.campaign.CampaignScenarioSide;
import bt.elements.campaign.CampaignScenarioSide.Participation;
import bt.elements.campaign.CampaignUnit;
import bt.elements.campaign.CampaignUnitElement;
import bt.elements.campaign.Force;
import bt.elements.campaign.Outcome;
import bt.elements.campaign.Side;
import bt.elements.campaign.Situation;
import bt.elements.campaign.SituationIntendedMovement;
import bt.elements.campaign.SituationUnitLocation;
import bt.elements.campaign.Track;
import bt.elements.unit.TechRating;
import bt.mapping.Coordinate;
import bt.mapping.campaign.CampaignBoard;
import bt.mapping.campaign.CampaignMap;
import bt.ui.renderers.CampaignBoardRenderer;
import bt.ui.renderers.MapFactory;
import bt.util.ExtensionFileFilter;
import bt.util.PropertyUtil;

public enum CampaignManager 
{
	INSTANCE;

	private static final String COLOUR = "Colour";
	private static final String ABBREVIATION = "Abbreviation";
	private static final String CAMPAIGN_RESULT = "CampaignResult";
	private static final String NEXT_SCENARIO = "NextScenario";
	private static final String WINNER = "Winner";
	private static final String PARTICIPATION = "Participation";
	private static final String DESTINATION = "Destination";
	private static final String INTENDED_MOVEMENT = "IntendedMovement";
	private static final String COORDINATE = "Coordinate";
	private static final String UNIT_NAME = "UnitName";
	private static final String UNIT_LOCATION = "UnitLocation";
	private static final String SITUATION = "Situation";
	private static final String DATE = "Date";
	private static final String OUTCOME_TYPE = "OutcomeType";
	private static final String SCENARIO = "Scenario";
	private static final String MAPSET = "Mapset";
	private static final String LOCATION = "Location";
	private static final String MAP = "Map";
	private static final String VARIANT = "Variant";
	private static final String DESIGN = "Design";
	private static final String ELEMENT = "Element";
	private static final String PARENT_UNIT_NAME = "ParentUnitName";
	private static final String SUB_UNIT_SIZE = "SubUnitSize";
	private static final String TYPE = "Type";
	private static final String SIZE = "Size";
	private static final String UNIT = "Unit";
	private static final String FORCE = "Force";
	private static final String TECH_RATING = "TechRating";
	private static final String ERA = "Era";
	private static final String FACTION = "Faction";
	private static final String REFERENCE = "Reference";
	private static final String TRACK = "Track";
	private static final String OUTCOME = "Outcome";
	private static final String SIDE = "Side";
	private static final String NUMBER = "Number";
	private static final String SYNOPSIS = "Synopsis";
	private static final String START_SCENARIO = "StartScenario";
	private static final String START_DATE = "StartDate";
	private static final String NAME = "Name";
	private static final String EXTERNAL_DATA_PATH = "ExternalDataPath";
	private HashMap<String,Campaign> _Campaigns = new HashMap<String,Campaign>();
	
	private CampaignManager()
	{
		try
		{
			loadCampaigns();
		}
		catch (Exception ex)
		{
			ex.printStackTrace(System.out);
		}
	}

	private void loadCampaigns() throws Exception
	{
		String dataPath = PropertyUtil.getStringProperty(EXTERNAL_DATA_PATH, "data") + "/campaigns/";
		String[] files = new File(dataPath).list(new ExtensionFileFilter("xml"));
		if (files != null)
		{
			for (String fileName : files)
			{
				loadCampaign(dataPath + fileName);
			}
		}
	}
 	
	
	private void loadCampaign(String filename)
	{
        File f = new File(filename);
        if (!f.exists())
        	return;
        
        try 
        {            
            SAXBuilder b = new SAXBuilder();
            Document playerDoc = b.build(f);
            Element root = playerDoc.getRootElement();
            
        	Campaign c = loadCampaign(root);
            _Campaigns.put(c.getName(),c);            	
        } 
        catch (Exception exx) 
        {
        	System.out.println("Failure Loading Campaign!");
            exx.printStackTrace();
        }
	}
	
	private Campaign loadCampaign(org.jdom.Element e) throws Exception
	{
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		Campaign c = new Campaign();
		c.setName(e.getAttributeValue(NAME));
		c.setStartDate(LocalDate.parse(e.getAttributeValue(START_DATE), dateFormatter));
		c.setStartScenario(e.getAttributeValue(START_SCENARIO));
		c.setSynopsis(e.getChildText(SYNOPSIS));

		Iterator<?> outcomeIterator = e.getChildren(OUTCOME).iterator();
		while (outcomeIterator.hasNext())
		{
			org.jdom.Element outcomeElement = (org.jdom.Element)outcomeIterator.next();
			Outcome outcome = new Outcome();
			outcome.setNumber(outcomeElement.getAttributeValue(NUMBER));
			outcome.setSide(outcomeElement.getAttributeValue(SIDE));
			outcome.setName(outcomeElement.getAttributeValue(NAME));			
			c.addOutcome(outcome);
		}

		Iterator<?> trackIterator = e.getChildren(TRACK).iterator();
		while (trackIterator.hasNext())
		{
			org.jdom.Element trackElement = (org.jdom.Element)trackIterator.next();
			Track track = new Track();
			track.setName(trackElement.getAttributeValue(NAME));			
			track.setReference(trackElement.getAttributeValue(REFERENCE));
			c.addTrack(track);
		}

		Iterator<?> sideIterator = e.getChildren(SIDE).iterator();
		while (sideIterator.hasNext())
		{
			org.jdom.Element sideElement = (org.jdom.Element)sideIterator.next();
			Side side = new Side();
			side.setName(sideElement.getAttributeValue(NAME));			
			side.setFaction(Faction.fromString(sideElement.getAttributeValue(FACTION)));
			side.setEra(Era.fromString(sideElement.getAttributeValue(ERA)));
			side.setTechRating(TechRating.fromString(sideElement.getAttributeValue(TECH_RATING)));

			Iterator<?> forceIterator = sideElement.getChildren(FORCE).iterator();
			while (forceIterator.hasNext())
			{
				org.jdom.Element forceElement = (org.jdom.Element)forceIterator.next();
				Force force = new Force();
				force.setName(forceElement.getAttributeValue(NAME));
				force.setAbbreviation(forceElement.getAttributeValue(ABBREVIATION));
				force.setColor(Color.decode(forceElement.getAttributeValue(COLOUR)));

				Iterator<?> unitIterator = forceElement.getChildren(UNIT).iterator();
				while (unitIterator.hasNext())
				{
					org.jdom.Element unitElement = (org.jdom.Element)unitIterator.next();
					CampaignUnit unit = new CampaignUnit();
					unit.setName(unitElement.getAttributeValue(NAME));
					unit.setSize(unitElement.getAttributeValue(SIZE));
					unit.setType(unitElement.getAttributeValue(TYPE));
					unit.setSubUnitSize(Integer.parseInt(unitElement.getAttributeValue(SUB_UNIT_SIZE)));
					unit.setParentUnitName(unitElement.getAttributeValue(PARENT_UNIT_NAME));
					
					Iterator<?> elementIterator = unitElement.getChildren(ELEMENT).iterator();
					while (elementIterator.hasNext())
					{
						org.jdom.Element elementElement = (org.jdom.Element)elementIterator.next();
						CampaignUnitElement element = new CampaignUnitElement();
						element.setType(elementElement.getAttributeValue(TYPE));
						element.setDesign(elementElement.getAttributeValue(DESIGN));
						element.setVariant(elementElement.getAttributeValue(VARIANT));
						
						unit.addElement(element);
					}
					
					force.addUnit(unit);
				}

				side.addForce(force);
			}
			
			c.addSide(side);
		}

		Iterator<?> locationIterator = e.getChildren(LOCATION).iterator();
		while (locationIterator.hasNext())
		{
			org.jdom.Element locationElement = (org.jdom.Element)locationIterator.next();
			CampaignLocation location = new CampaignLocation();
			location.setNumber(locationElement.getAttributeValue(NUMBER));
			location.setName(locationElement.getAttributeValue(NAME));
			location.setMapset(locationElement.getAttributeValue(MAPSET));

			c.addLocation(location);
		}
		
		c.setMap(new CampaignMap(e.getChild(MAP)));
		
		Iterator<?> scenarioIterator = e.getChildren(SCENARIO).iterator();
		while (scenarioIterator.hasNext())
		{
			org.jdom.Element scenarioElement = (org.jdom.Element)scenarioIterator.next();
			CampaignScenario scenario = new CampaignScenario();
			scenario.setNumber(scenarioElement.getAttributeValue(NUMBER));
			scenario.setName(scenarioElement.getAttributeValue(NAME));
			scenario.setMap(scenarioElement.getAttributeValue(MAP));
			scenario.setOutcomeType(OutcomeType.fromString(scenarioElement.getAttributeValue(OUTCOME_TYPE)));
			scenario.setTrack(scenarioElement.getAttributeValue(TRACK));
			scenario.setDateTime(LocalDateTime.parse(scenarioElement.getAttributeValue(DATE),dateTimeFormatter));
			scenario.setSynopsis(scenarioElement.getChildText(SYNOPSIS));

			org.jdom.Element situationElement = scenarioElement.getChild(SITUATION);
			Situation situation = new Situation();
			
			Iterator<?> unitLocationsIterator = situationElement.getChildren(UNIT_LOCATION).iterator();
			while (unitLocationsIterator.hasNext())
			{
				org.jdom.Element unitLocationElement = (org.jdom.Element)unitLocationsIterator.next();
				SituationUnitLocation unitLocation = new SituationUnitLocation();
				unitLocation.setUnitName(unitLocationElement.getAttributeValue(UNIT_NAME));
				unitLocation.setCoordinate(parseCoordinate(unitLocationElement.getAttributeValue(COORDINATE)));
				
				situation.addUnitLocation(unitLocation);
			}

			Iterator<?> intendedMovementsIterator = situationElement.getChildren(INTENDED_MOVEMENT).iterator();
			while (intendedMovementsIterator.hasNext())
			{
				org.jdom.Element intendedMovementElement = (org.jdom.Element)intendedMovementsIterator.next();
				SituationIntendedMovement unitIntendedMovement = new SituationIntendedMovement();
				unitIntendedMovement.setUnitName(intendedMovementElement.getAttributeValue(UNIT_NAME));
				unitIntendedMovement.setDestination(parseCoordinate(intendedMovementElement.getAttributeValue(DESTINATION)));
				
				situation.addUnitMovement(unitIntendedMovement);
			}
			scenario.setSituation(situation);

			Iterator<?> sidesIterator = scenarioElement.getChildren(SIDE).iterator();
			while (sidesIterator.hasNext())
			{
				org.jdom.Element scenarioSideElement = (org.jdom.Element)sidesIterator.next();
				CampaignScenarioSide side = new CampaignScenarioSide();
				side.setSideName(scenarioSideElement.getAttributeValue(NAME));
				side.setParticipation(Participation.fromString(scenarioSideElement.getAttributeValue(PARTICIPATION)));
				side.setForceName(scenarioSideElement.getAttributeValue(FORCE));
				side.setUnitName(scenarioSideElement.getAttributeValue(UNIT));
				
				scenario.addSide(side);
			}

			Iterator<?> outcomesIterator = scenarioElement.getChildren(OUTCOME).iterator();
			while (outcomesIterator.hasNext())
			{
				org.jdom.Element scenariooutcomeElement = (org.jdom.Element)outcomesIterator.next();
				CampaignScenarioOutcome outcome = new CampaignScenarioOutcome();
				outcome.setWinner(scenariooutcomeElement.getAttributeValue(WINNER));
				outcome.setNextScenario(scenariooutcomeElement.getAttributeValue(NEXT_SCENARIO));
				outcome.setCampaignResult(scenariooutcomeElement.getAttributeValue(CAMPAIGN_RESULT));
				
				scenario.addOutcome(outcome);
			}

			c.addScenario(scenario);
		}

		return c;
	}
		
	private Coordinate parseCoordinate(String string)
	{
		string = string.trim();
		String[] elements = string.split(",");
		int x = Integer.parseInt(elements[0].trim());
		int y = Integer.parseInt(elements[1].trim());
		return new Coordinate(x,y);
		
	}
	
	public ArrayList<String> getCampaignList()
	{
		return new ArrayList<String>(_Campaigns.keySet());
	}
	
	public Campaign getCampaign(String name)
	{
		return _Campaigns.get(name);
	}
	
	
	public String printCampaignToPDF(Campaign campaign) throws Exception
	{
		DateTimeFormatter fullDateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy - HH:mm");

		String path = PropertyUtil.getStringProperty(EXTERNAL_DATA_PATH, "data");
		String filename = path + "/campaigns/" + campaign.getName() + ".pdf";

		File f = new File(filename);
		if (f.exists())
			f.delete();
		
		CampaignMap map = campaign.getMap();
		CampaignBoardRenderer boardRenderer = (CampaignBoardRenderer) MapFactory.INSTANCE.createBoardRenderer(map.getMapType());
		CampaignBoard board = new CampaignBoard(82);
		board.addMap(map, new Coordinate(1,1));
		board.completedAddingMaps();
		boardRenderer.setBoard(null, board);
		
		com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4, 5, 5, 5, 5);
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		
		Paragraph title1 = new Paragraph(campaign.getName(), FontFactory.getFont(FontFactory.HELVETICA, 24, Font.BOLDITALIC));
		Chapter chapter1 = new Chapter(title1, 1);
		chapter1.setNumberDepth(0);
		
		chapter1.add(new Paragraph("Campaign Start Date: " + campaign.getStartDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.ITALIC)));
		chapter1.add(new Paragraph(campaign.getSynopsis(),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
		
		chapter1.add(new Paragraph("",FontFactory.getFont(FontFactory.HELVETICA, 10, Font.ITALIC)));
		chapter1.add(new Paragraph("Maps",FontFactory.getFont(FontFactory.HELVETICA, 10, Font.ITALIC)));
		chapter1.add(new Paragraph("",FontFactory.getFont(FontFactory.HELVETICA, 10, Font.ITALIC)));
		com.itextpdf.text.pdf.PdfPTable locationTable = new com.itextpdf.text.pdf.PdfPTable(4);
		locationTable.setWidthPercentage(98);
		locationTable.addCell(createHeaderCell("Number", BaseColor.WHITE, BaseColor.BLACK));
		locationTable.addCell(createHeaderCell("Name", BaseColor.WHITE, BaseColor.BLACK));
		locationTable.addCell(createHeaderCell("Map Type", BaseColor.WHITE, BaseColor.BLACK));
		locationTable.addCell(createHeaderCell("Reference", BaseColor.WHITE, BaseColor.BLACK));
		locationTable.setHeaderRows(1);

		ArrayList<CampaignLocation> campaignLocations = campaign.getLocations();
		campaignLocations.sort(new Comparator<CampaignLocation>()
		{
			@Override
			public int compare(CampaignLocation cl1, CampaignLocation cl2)
			{
				int num1 = Integer.parseInt(cl1.getNumber());
				int num2 = Integer.parseInt(cl2.getNumber());
				return Integer.compare(num1, num2);
			}
		});
		for (CampaignLocation location : campaignLocations)
		{
			locationTable.addCell(location.getNumber());
			locationTable.addCell(location.getName());
			locationTable.addCell(location.getMapset());
			Coordinate settlementCoord = campaign.getMap().getCoordinateForSettlement(location.getName());
			locationTable.addCell(settlementCoord != null ? settlementCoord.toShortString() : "");
		}
		chapter1.add(locationTable);
		chapter1.newPage();
		chapter1.add(new Paragraph("Campaign Map",FontFactory.getFont(FontFactory.HELVETICA, 18, Font.NORMAL)));
		
		Image image = createImage(boardRenderer.renderImage()); 
		image.scalePercent(80);
		chapter1.add(image);		
		document.add(chapter1);

		Chapter chapter2 = new Chapter("Sides", 2);

		ArrayList<String> sideNames = campaign.getSideNames();
		Collections.sort(sideNames);
		for (String sideName : sideNames)
		{
			Side side = campaign.getSide(sideName);

			chapter2.add(new Paragraph(side.getName() + " - " + side.getFaction(),FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL)));
			
			ArrayList<Force> forces = side.getForces();
			forces.sort(new Comparator<Force>()
			{
				@Override
				public int compare(Force f1, Force f2)
				{
					return f1.getName().compareTo(f2.getName());
				}
			});
			for (Force force: forces)
			{
				chapter2.add(new Paragraph(force.getName() + " (" + force.getAbbreviation() + ")",FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL)));
				
				ArrayList<CampaignUnit> units = force.getUnits();
				units.sort(new Comparator<CampaignUnit>()
				{
					@Override
					public int compare(CampaignUnit cu1, CampaignUnit cu2)
					{
						return cu1.getName().compareTo(cu2.getName());
					}
				});
				for (CampaignUnit unit : units)
				{
					chapter2.add(new Paragraph(unit.getName() + " (" + unit.getParentUnitName() + ")",FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
					
					com.itextpdf.text.pdf.PdfPTable elementTable = new com.itextpdf.text.pdf.PdfPTable(3);
					elementTable.setWidthPercentage(60);
					elementTable.addCell(createHeaderCell("Type", BaseColor.WHITE, BaseColor.BLACK));
					elementTable.addCell(createHeaderCell("Design", BaseColor.WHITE, BaseColor.BLACK));
					elementTable.addCell(createHeaderCell("Variant", BaseColor.WHITE, BaseColor.BLACK));
					elementTable.setHeaderRows(1);
					
					for (CampaignUnitElement element : unit.getElements())
					{
						elementTable.addCell(element.getType());
						elementTable.addCell(element.getDesign());
						elementTable.addCell(element.getVariant());
					}
					chapter2.add(elementTable);
				}
				chapter2.newPage();
			}
		}		
		document.add(chapter2);
		
		Chapter chapter3 = new Chapter("Scenarios", 3);

		ArrayList<CampaignScenario> scenarios = campaign.getScenarios();
		scenarios.sort(new Comparator<CampaignScenario>()
		{

			@Override
			public int compare(CampaignScenario cs1, CampaignScenario cs2)
			{
				return cs1.getNumber().compareTo(cs2.getNumber());
			}
		});
		for (CampaignScenario scenario: scenarios)
		{
			chapter3.add(new Paragraph("Scenario: " + scenario.getNumber(),FontFactory.getFont(FontFactory.HELVETICA, 18, Font.NORMAL)));
			chapter3.add(new Paragraph(scenario.getName(),FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL)));
			chapter3.add(new Paragraph(scenario.getTrack() + " - " + campaign.getLocation(scenario.getMap()).getName() + " : " + scenario.getDateTime().format(fullDateTimeFormatter),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
			chapter3.add(new Paragraph(scenario.getSynopsis(),FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
			
			chapter3.add(new Paragraph("Sides",FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
			for (String sideName: scenario.getSides())
			{
				CampaignScenarioSide side = scenario.getSide(sideName);
				chapter3.add(new Paragraph(side.getParticipation() + ": " + side.getSideName() + " - " + side.getUnitName() + " - " + side.getForceName() ,FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
			}

			chapter3.add(new Paragraph("Outcomes",FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
			for (String outcomeName: scenario.getOutcomes())
			{
				CampaignScenarioOutcome outcome = scenario.getOutcome(outcomeName);
				if (scenario.getOutcomeType() == CampaignScenario.OutcomeType.NEXT_SCENARIO)
					chapter3.add(new Paragraph("Winner: " + outcome.getWinner() + " goto Scenario: " + outcome.getNextScenario(),FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
				else
				{
					Outcome o = campaign.getOutcome(outcome.getCampaignResult());
					chapter3.add(new Paragraph("Winner: " + outcome.getWinner() + " - " + o.toString(),FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));					
				}
			}

			boardRenderer.setSituation(campaign, scenario.getSituation());
			Image scenarioImage = createImage(boardRenderer.renderImage()); 
			scenarioImage.scalePercent(50);
			chapter3.add(scenarioImage);		

			chapter3.newPage();
		}

		
		document.add(chapter3);
		
		document.close();
		System.out.println("Done!");
		
		return filename;
	}
	
	private com.itextpdf.text.Image createImage(BufferedImage image)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			return Image.getInstance(baos.toByteArray());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	
	private com.itextpdf.text.pdf.PdfPCell createHeaderCell(String content, BaseColor foreColor, BaseColor background) throws Exception
	{
		Font f = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD);
		f.setColor(foreColor);
		com.itextpdf.text.Phrase phrase = new Phrase(content, f);
		com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(phrase);
		cell.setBackgroundColor(background);
        cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
		return cell;
	}
	
	@SuppressWarnings("unused")
	private com.itextpdf.text.pdf.PdfPCell createDataCell(String content, BaseColor foreColor, BaseColor background) throws Exception
	{
		Font f = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD);
		f.setColor(foreColor);
		com.itextpdf.text.Phrase phrase = new Phrase(content, f);
		com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(phrase);
		cell.setBackgroundColor(background);
        cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
		return cell;
	}

}
