package bt.managers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import bt.elements.Asset;
import bt.elements.BattleValue;
import bt.elements.Battlemech;
import bt.elements.BattlemechRepairReport;
import bt.elements.Era;
import bt.elements.Faction;
import bt.elements.ItemRepairDetail;
import bt.elements.collection.ItemCollection;
import bt.elements.design.BattlemechDesign;
import bt.elements.personnel.Administrator;
import bt.elements.personnel.Astech;
import bt.elements.personnel.JobType;
import bt.elements.personnel.Mechwarrior;
import bt.elements.personnel.Medic;
import bt.elements.personnel.Personnel;
import bt.elements.personnel.Rank;
import bt.elements.personnel.Rating;
import bt.elements.personnel.Technician;
import bt.elements.unit.CompletedMission;
import bt.elements.unit.Force;
import bt.elements.unit.MechAvailability;
import bt.elements.unit.MechUnitParameters;
import bt.elements.unit.PersonnelAssetAssignment;
import bt.elements.unit.Player;
import bt.elements.unit.QualityRating;
import bt.elements.unit.RandomName;
import bt.elements.unit.Role;
import bt.elements.unit.TechRating;
import bt.elements.unit.Unit;
import bt.ui.renderers.BattlemechRenderer;
import bt.util.Dice;
import bt.util.ExceptionUtil;
import bt.util.ExtensionFileFilter;
import bt.util.PropertyUtil;
import bt.util.SwingHelper;

/**
 * <p>
 * Title: Miradan Phedd
 * </p>
 * 
 * <p>
 * Description: Library for all Miradan related work
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * 
 * <p>
 * Company: Miradan Phedd
 * </p>
 * 
 * @author Daniel Cleyne
 * @version 0.1
 */
public class UnitManager
{
	private static final String MODEL = "Model";
	private static final String MANUFACTURER = "Manufacturer";
	private static final String PARTIAL_REPAIR_EFFECT = "Partial Repair Effect";
	private static final String PARTIAL_REPAIR_NUMBER = "Partial Repair#";
	private static final String TARGET_NUMBER = "Target#";
	private static final String COST = "Cost";
	private static final String TIME = "Time";
	private static final String SURNAME = "Surname";
	private static final String RANDOM_NAME = "RandomName";
	private static final String RANDOM_NAMES = "RandomNames";
	private static final String MISSIONS = "Missions";
	private static final String HITS = "Hits";
	private static final String GUNNERY_SKILL = "GunnerySkill";
	private static final String PILOTING_SKILL = "PilotingSkill";
	private static final String RATING = "Rating";
	private static final String RANK = "Rank";
	private static final String JOB_TYPE = "JobType";
	private static final String ROLE = "Role";
	private static final String ASSET_IDENTIFIER = "AssetIdentifier";
	private static final String ASSET_ASSIGNMENT = "AssetAssignment";
	private static final String ASSET_ASSIGNMENTS = "AssetAssignments";
	private static final String MISSION_DATE = "MissionDate";
	private static final String WARCHEST_POINTS = "WarchestPoints";
	private static final String RESULT = "Result";
	private static final String MISSION_TITLE = "MissionTitle";
	private static final String MISSION_ID = "MissionID";
	private static final String COMPLETED_MISSION = "CompletedMission";
	private static final String COMPLETED_MISSIONS = "CompletedMissions";
	private static final String TITLE = "Title";
	private static final String ID = "ID";
	private static final String ASSIGNED_MISSION = "AssignedMission";
	private static final String EXTERNAL_DATA_PATH = "ExternalDataPath";
	private static final String TOTAL_BV = "TotalBV";
	private static final String TOTAL_WEIGHT = "TotalWeight";
	private static final String BV = "BV";
	private static final String WEIGHT = "Weight";
	private static final String BATTLEMECH = "Battlemech";
	private static final String AVAILABILITY = "Availability";
	private static final String VARIANT = "Variant";
	private static final String FACTIONS = "Factions";
	private static final String ERA = "Era";
	private static final String MECH_AVAILABILITY = "MechAvailability";
	private static final String INCLUDED_MECH_WEIGHT = "IncludedMechWeight";
	private static final String MAX_WEIGHT = "MaxWeight";
	private static final String MIN_WEIGHT = "MinWeight";
	private static final String MAX_BV = "MaxBV";
	private static final String MIN_BV = "MinBV";
	private static final String MECH_COUNT = "MechCount";
	private static final String MECH_UNIT_PARAMETERS = "MechUnitParameters";
	private static final String DATA_PATH = "DataPath";
	private static final String UNITS = "Units";
	private static final String BATTLEMECHS = "Battlemechs";
	private static final String PLAYER = "Player";
	private static final String NOTES = "Notes";
	private static final String ESTABLISH_DATE = "EstablishDate";
	private static final String NAME = "Name";
	private static final String UNIT = "Unit";
	private static final String PERSONNEL = "Personnel";
	private static final String ASSETS = "Assets";
	private static final String MINIATURE_COLLECTION = "MiniatureCollection";
	private static final String TECH_RATING = "TechRating";
	private static final String QUALITY_RATING = "QualityRating";
	private static final String CURRENT_DATE = "CurrentDate";
	private static final String PARENT_UNIT = "ParentUnit";

	public interface PersonnelSaveHandler
	{
		public void savePersonnel(org.jdom.Element e, Personnel p);
	}

	public interface PersonnelLoadHandler
	{
		public Personnel loadPersonnel(org.jdom.Element e);
	}

	private static UnitManager theInstance = new UnitManager();
	
	BattlemechManager _BattlemechManager = new BattlemechManager();


	private HashMap<String, MechUnitParameters> _Parameters = new HashMap<String, MechUnitParameters>();
	private HashMap<Era, HashMap<Faction, ArrayList<MechAvailability>>> _MechAvailability = new HashMap<Era, HashMap<Faction, ArrayList<MechAvailability>>>();
	private ArrayList<RandomName> _RandomNames = new ArrayList<RandomName>();
	private int _LastServedName = -1;
	
	private HashMap<String, Unit> _Units = new HashMap<String, Unit>();

	private HashMap<JobType, PersonnelSaveHandler> _PersonnelSaveHandlers = new HashMap<JobType, PersonnelSaveHandler>();
	private HashMap<JobType, PersonnelLoadHandler> _PersonnelLoadHandlers = new HashMap<JobType, PersonnelLoadHandler>();

	private UnitManager()
	{
		try
		{
			loadMechUnitParameters();
			loadRandomNames();
			
			registerPersonnelSaveHandlers();
			registerPersonnelLoadHandlers();
			
			loadUnits();
		} catch (Exception ex)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}

	public static UnitManager getInstance()
	{
		return theInstance;
	}
	
	public ArrayList<Unit> getUnits()
	{
		return new ArrayList<Unit>(_Units.values()); 
	}
	
	public HashMap<String, MechUnitParameters> getMechUnitParameters()
	{
		return _Parameters;
	}
	
	public ArrayList<MechUnitParameters> getMechUnitParametersForBV(int bv)
	{
		ArrayList<MechUnitParameters> mups = new ArrayList<MechUnitParameters>();
		
		for (String key : _Parameters.keySet())
		{
			MechUnitParameters mup = _Parameters.get(key);
			if (bv >= mup.getMinBV() && bv <= mup.getMaxBV())
			{
				mups.add(mup);
			}
		}
		
		return mups;
	}

	private void loadMechUnitParameters() throws Exception
	{
		String fileName = PropertyUtil.getStringProperty(DATA_PATH, "data") + "/RandomMechUnitParameters.xml";

		SAXBuilder b = new SAXBuilder();
		Document doc = b.build(new File(fileName));

		org.jdom.Element rootElement = doc.getRootElement();

		Iterator<?> iter = rootElement.getChildren(MECH_UNIT_PARAMETERS).iterator();
		while (iter.hasNext())
		{
			org.jdom.Element paramElement = (org.jdom.Element) iter.next();

			MechUnitParameters mup = new MechUnitParameters();

			mup.setName(paramElement.getAttributeValue(NAME));
			mup.setMechCount(Integer.parseInt(paramElement.getAttributeValue(MECH_COUNT)));
			mup.setMinBV(Integer.parseInt(paramElement.getAttributeValue(MIN_BV)));
			mup.setMaxBV(Integer.parseInt(paramElement.getAttributeValue(MAX_BV)));
			mup.setMinWeight(Integer.parseInt(paramElement.getAttributeValue(MIN_WEIGHT)));
			mup.setMaxWeight(Integer.parseInt(paramElement.getAttributeValue(MAX_WEIGHT)));

			Iterator<?> weightIter = paramElement.getChildren(INCLUDED_MECH_WEIGHT).iterator();
			while (weightIter.hasNext())
			{
				org.jdom.Element incWeightElement = (org.jdom.Element) weightIter.next();
				mup.getIncludedMechWeights().add(Integer.parseInt(incWeightElement.getText()));
			}
			_Parameters.put(mup.getName(), mup);
		}
		iter = rootElement.getChildren(MECH_AVAILABILITY).iterator();
		while (iter.hasNext())
		{
			org.jdom.Element paramElement = (org.jdom.Element) iter.next();
			
			Era era = Era.fromString(paramElement.getAttributeValue(ERA));
			ArrayList<Faction> factions = getFactions(paramElement.getAttributeValue(FACTIONS));
			
			ArrayList<MechAvailability> mechAvailability = new ArrayList<MechAvailability>();
			Iterator<?> availIter = paramElement.getChildren().iterator();
			while (availIter.hasNext())
			{
				org.jdom.Element availElement = (org.jdom.Element) availIter.next();

				MechAvailability avail = new MechAvailability();
				avail.setName(availElement.getAttributeValue(NAME));
				avail.setVariant(availElement.getAttributeValue(VARIANT));
				avail.setAvailability(Integer.parseInt(availElement.getAttributeValue(AVAILABILITY)));

				mechAvailability.add(avail);
			}
			
			if (!_MechAvailability.containsKey(era))
			{
				_MechAvailability.put(era, new HashMap<Faction, ArrayList<MechAvailability>>());
			}
			HashMap<Faction, ArrayList<MechAvailability>> factionMechAvailability = _MechAvailability.get(era);
			for (Faction f : factions)
			{
				factionMechAvailability.put(f, new ArrayList<MechAvailability>(mechAvailability));
			}
		}
	}
	
	private ArrayList<Faction> getFactions(String factionList) throws Exception
	{
		ArrayList<Faction> factions = new ArrayList<Faction>();
		
		try
		{
			String[] fs = factionList.split(",");
			for (String f : fs)
			{
				if (!f.trim().isEmpty())
					factions.add(Faction.fromString(f.trim()));
			}
		}
		catch (Exception ex)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}
		return factions;
	}

	private ArrayList<Battlemech> getBestMechList(MechUnitParameters mup, ArrayList<ArrayList<Battlemech>> mechLists)
	{
		ArrayList<Battlemech> bestList = null;
		int bestBVDifference = Integer.MAX_VALUE;

		int goalBV = mup.getMinBV() + ((mup.getMaxBV() - mup.getMinBV()) / 2);
		
		for (ArrayList<Battlemech> list : mechLists)
		{
	        int totalBV = 0;
	        for (Battlemech mech : list)
	        {
	            totalBV += mech.getBV();
	        }
	        if (totalBV > 0)
	        {
	        	int diff = Math.abs(goalBV - totalBV);
	        	if (diff < bestBVDifference)
	        	{
	        		bestBVDifference = diff;
	        		bestList = list;
	        	}
	        }
		}
		
		return bestList;
	}
	
	public Unit generateUnit(Era era, Faction faction, Player p, String unitName, MechUnitParameters mup, Rating rating, QualityRating qualityRating, TechRating techRating, 
			ItemCollection collection) throws Exception
	{
		ArrayList<ArrayList<Battlemech>> mechLists = new ArrayList<ArrayList<Battlemech>>();
		for (int i = 0; i < 10; i++)
			mechLists.add(generateLance(era, faction, mup, collection));
		
		ArrayList<Battlemech> bestList = getBestMechList(mup, mechLists);
		if (bestList == null)
			throw new Exception("Unable to build list with Params(" + mup + ") from collection (" + collection + ")");
		
		Unit u = new Unit();
		u.setPlayer(p);
		u.setName(unitName);
		u.setQualityRating(qualityRating);
		u.setTechRating(techRating);
		u.setWarchestPoints(1000);

		u.addBattlemechs(bestList);
		int supportReq = 0;

		for (Battlemech mech : u.getBattlemechs())
		{
			Mechwarrior mw = new Mechwarrior();
			mw.setRating(rating);
			mw.setPilotingSkill(rating.getPilotingValue());
			mw.setGunnerySkill(rating.getGunneryValue());

			RandomName rn = RandomNameManager.getInstance().getRandomName();
			mw.setName(rn.toString());
			u.getPersonnel().add(mw);
			u.addPersonnelAssignment(mw.getName(), mech.getIdentifier(), Role.PILOT);

			supportReq += 40 + mech.getWeight() / 5;
		}
		int numTechs = (supportReq / 30) / 3;
		if (numTechs == 0)
			numTechs = 1;

		int numASTechs = numTechs / 2;
		if (numASTechs == 0)
			numASTechs = 1;

		int numMedics = ((4 + numASTechs + numTechs) / 20);
		if (numMedics == 0)
			numMedics = 1;

		int numAdmin = ((4 + numTechs + numASTechs + numMedics) / 10) + 1;

		for (int i = 0; i < numTechs; i++)
		{
			Technician tech = new Technician();
			tech.setRating(rating);
			RandomName rn = RandomNameManager.getInstance().getRandomName();
			tech.setName(rn.toString());
			u.getPersonnel().add(tech);
		}
		for (int i = 0; i < numASTechs; i++)
		{
			Astech tech = new Astech();
			tech.setRating(rating);
			RandomName rn = RandomNameManager.getInstance().getRandomName();
			tech.setName(rn.toString());
			u.getPersonnel().add(tech);
		}
		for (int i = 0; i < numMedics; i++)
		{
			Medic tech = new Medic();
			tech.setRating(rating);
			RandomName rn = RandomNameManager.getInstance().getRandomName();
			tech.setName(rn.toString());
			u.getPersonnel().add(tech);
		}
		for (int i = 0; i < numAdmin; i++)
		{
			Administrator admin = new Administrator();
			admin.setRating(rating);
			RandomName rn = RandomNameManager.getInstance().getRandomName();
			admin.setName(rn.toString());
			u.getPersonnel().add(admin);
		}

		return u;
	}

	public Unit generateUnit(Era era, Faction faction, Player p, String unitName, String lanceWeight, Rating rating, QualityRating qualityRating, TechRating techRating, ItemCollection collection) throws Exception
	{
		if (!_Parameters.containsKey(lanceWeight))
			throw new IllegalArgumentException("Unable to determine lance parameters from Lance Weight : " + lanceWeight);

		MechUnitParameters mup = _Parameters.get(lanceWeight);

		return generateUnit(era, faction, p, unitName, mup, rating, qualityRating, techRating, collection);

	}

	private ArrayList<Battlemech> generateLance(Era era, Faction faction, MechUnitParameters mup, ItemCollection collection) throws Exception
	{
		ArrayList<Battlemech> mechs = new ArrayList<Battlemech>();

		DesignManager dm = DesignManager.getInstance();

		int attempts = 0;
		while (mechs.size() == 0 && attempts < 10)
		{
			collection.resetPending();

			attempts++;
			ArrayList<MechAvailability> validMechs = new ArrayList<MechAvailability>(_MechAvailability.get(era).get(faction));
			for (int i = validMechs.size() - 1; i >= 0; i--)
			{
				MechAvailability ma = validMechs.get(i);
				BattlemechDesign design = dm.Design(ma.getVariantName());
				if (design != null)
				{
					if (!mup.getIncludedMechWeights().contains(design.getWeight()))
						validMechs.remove(ma);
				}
				else
				{
					System.out.println("Unable to located design:" + ma.getVariantName());
					validMechs.remove(ma);
				}
			}
	
			ArrayList<BattleValue> elements = new ArrayList<BattleValue>();
			int findMechAttempts = 0;
			while (elements.size() < mup.getMechCount() && findMechAttempts < 10)
			{
				findMechAttempts++;
				for (int mechDup = 0; mechDup < mup.getMechCount() - 1; mechDup++)
				{
					for (MechAvailability ma : validMechs)
					{
						if (Dice.d10(1) <= ma.getAvailability())
						{
							if (collection.isItemAvailable(ma.getName()))
							{
								BattlemechDesign design = dm.Design(ma.getVariantName());
								elements.add(design);
								collection.moveToPending(ma.getName());
							}
						}
					}
				}
			}
	
			ArrayList<ArrayList<Integer>> validSets = getValidSubsetSums(elements, mup.getMechCount(), mup.getMaxBV(), mup.getMinBV());
			if (validSets.size() > 0)
			{
				collection.consumePending();

				ArrayList<Integer> selectedSet = validSets.get(0);
				if (validSets.size() > 1)
					selectedSet = validSets.get(Dice.random(validSets.size()) - 1);
	
				for (int i = 0; i < selectedSet.size(); i++)
				{
					BattlemechDesign bd = (BattlemechDesign)elements.get(selectedSet.get(i));
					Battlemech b = _BattlemechManager.createBattlemechFromDesign(bd);
					mechs.add(b);
				}
			}
		}
		return mechs;
	}

	private void getValidSubsetSums(ArrayList<BattleValue> elements, int elementCount, int bvMax, int bvMin, int currentIndex, int bvTotal, int elementTotal, ArrayList<Integer> currentSet, ArrayList<ArrayList<Integer>> validSets)
	{
		if (currentIndex >= elements.size())
			return;

		elementTotal++;
		bvTotal += elements.get(currentIndex).getBV();
		currentSet.add(currentIndex);
		if (elementTotal == elementCount)
		{
			if (bvTotal >= bvMin && bvTotal <= bvMax)
				validSets.add(currentSet);

			return;
		} else if (elementTotal <= elementCount && bvTotal <= bvMax)
		{
			getValidSubsetSums(elements, elementCount, bvMax, bvMin, currentIndex + 1, bvTotal, elementTotal, currentSet, validSets);
		}


	}

	private ArrayList<ArrayList<Integer>> getValidSubsetSums(ArrayList<BattleValue> elements, int elementCount, int bvMax, int bvMin)
	{
		ArrayList<ArrayList<Integer>> validSets = new ArrayList<ArrayList<Integer>>();
		
		for (int skip = 0; skip < elements.size(); skip++)
		{
			for (int index = skip; index < elements.size(); index++)
			{
				getValidSubsetSums(elements, elementCount, bvMax, bvMin, index, 0, 0, new ArrayList<Integer>(), validSets);
			}
		}
		
		return validSets;
	}

	public void saveUnitSummaries(ArrayList<Unit> units) throws Exception
	{
		org.jdom.Document doc = new org.jdom.Document();

		org.jdom.Element unitsNode = new org.jdom.Element(UNITS);
		doc.addContent(unitsNode);

		for (Unit u : units)
		{
			org.jdom.Element unitElement = new org.jdom.Element(UNIT);
			int totalBV = 0;
			int totalWeight = 0;
			unitElement.setAttribute(NAME, u.getName());
			
			for (Battlemech b : u.getBattlemechs())
			{
				totalBV += b.getBV();
				totalWeight += b.getWeight();

				org.jdom.Element mechElement = new org.jdom.Element(BATTLEMECH);
				mechElement.setAttribute(NAME, b.getDesignName());
				mechElement.setAttribute(VARIANT, b.getDesignVariant());
				mechElement.setAttribute(WEIGHT, Integer.toString(b.getWeight()));
				mechElement.setAttribute(BV, Integer.toString(b.getBV()));

				unitElement.addContent(mechElement);
			}
			unitElement.setAttribute(TOTAL_WEIGHT, Integer.toString(totalWeight));
			unitElement.setAttribute(TOTAL_BV, Integer.toString(totalBV));

			unitsNode.addContent(unitElement);
		}
		String fileName = PropertyUtil.getStringProperty(EXTERNAL_DATA_PATH, "data") + "/units/UnitSummaries.xml";
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		out.output(doc, new FileOutputStream(fileName));
	}

	public void saveUnit(Unit u) throws Exception
	{
		String fileName = PropertyUtil.getStringProperty(EXTERNAL_DATA_PATH, "data") + "/units/" + u.getName() + ".xml";

		org.jdom.Document doc = new org.jdom.Document();
		doc.setRootElement(saveUnitToElement(u));

		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		out.output(doc, new FileOutputStream(fileName));
	}
	
	public org.jdom.Element saveUnitToElement(Unit u)
	{
		BattlemechManager bm = new BattlemechManager();

		org.jdom.Element unitNode = new org.jdom.Element(UNIT);

		unitNode.addContent(new org.jdom.Element(NAME).setText(u.getName()));
		unitNode.addContent(new org.jdom.Element(ESTABLISH_DATE).setText(SwingHelper.FormatDate(u.getEstablishDate())));
		unitNode.addContent(new org.jdom.Element(CURRENT_DATE).setText(SwingHelper.FormatDate(u.getCurrentDate())));
		unitNode.addContent(new org.jdom.Element(QUALITY_RATING).setText(u.getQualityRating().toString()));
		unitNode.addContent(new org.jdom.Element(TECH_RATING).setText(u.getTechRating().toString()));
		unitNode.addContent(new org.jdom.Element(WARCHEST_POINTS).setText(Integer.toString(u.getWarchestPoints())));
		unitNode.addContent(new org.jdom.Element(NOTES).setText(u.getNotes()));

		if (u.getPlayer() != null)
			unitNode.addContent(new org.jdom.Element(PLAYER).setText(u.getPlayer().getName()));

		org.jdom.Element mechsNode = new org.jdom.Element(BATTLEMECHS);
		for (Battlemech mech : u.getBattlemechs())
		{
			mechsNode.addContent(bm.saveBattlemech(mech));
		}
		unitNode.addContent(mechsNode);

		org.jdom.Element personnelNode = new org.jdom.Element(PERSONNEL);
		for (Personnel p : u.getPersonnel())
		{
			personnelNode.addContent(savePersonnel(p));
		}
		unitNode.addContent(personnelNode);

		if (u.getAssignedMission() != null)
		{
			org.jdom.Element assignedMissionNode = new org.jdom.Element(ASSIGNED_MISSION);
			assignedMissionNode.setAttribute(ID, Long.toString(u.getAssignedMission()));
			assignedMissionNode.setAttribute(TITLE, u.getAssignedMissionTitle());
			unitNode.addContent(assignedMissionNode);
		}
		
		org.jdom.Element completedMissionsNode = new org.jdom.Element(COMPLETED_MISSIONS);
		for (CompletedMission mission : u.getCompletedMissions())
		{
			org.jdom.Element completedMissionElement = new org.jdom.Element(COMPLETED_MISSION);
			completedMissionElement.setAttribute(MISSION_ID, Long.toString(mission.getMissionIdentifier()));
			completedMissionElement.setAttribute(MISSION_TITLE, mission.getMissionTitle());
			completedMissionElement.setAttribute(RESULT, mission.getResult().toString());
			completedMissionElement.setAttribute(WARCHEST_POINTS, Integer.toString(mission.getWarchestPoints()));
			completedMissionElement.setAttribute(MISSION_DATE, SwingHelper.FormatDate(mission.getMissionDate()));
			
			completedMissionsNode.addContent(completedMissionElement);
		}
		unitNode.addContent(completedMissionsNode);

		org.jdom.Element assetAssignmentsNode = new org.jdom.Element(ASSET_ASSIGNMENTS);
		for (PersonnelAssetAssignment ass : u.getPersonnelAssetAssignments())
		{
			org.jdom.Element assignedAssetNode = new org.jdom.Element(ASSET_ASSIGNMENT);
			assignedAssetNode.setAttribute(NAME, ass.getName());
			assignedAssetNode.setAttribute(ASSET_IDENTIFIER, ass.getAssetIdentifier());
			assignedAssetNode.setAttribute(ROLE, ass.getRole().toString());
			assetAssignmentsNode.addContent(assignedAssetNode);
		}
		unitNode.addContent(assetAssignmentsNode);
		
		return unitNode;

	}

	public org.jdom.Element saveForceToElement(Force f, String name)
	{
		org.jdom.Element unitNode = new org.jdom.Element(name);
		if (f != null)
		{
			BattlemechManager bm = new BattlemechManager();
	
	
			unitNode.addContent(new org.jdom.Element(PARENT_UNIT).setText(f.getParentUnit()));
			unitNode.addContent(new org.jdom.Element(CURRENT_DATE).setText(SwingHelper.FormatDate(f.getCurrentDate())));
			unitNode.addContent(new org.jdom.Element(QUALITY_RATING).setText(f.getQualityRating().toString()));
			unitNode.addContent(new org.jdom.Element(TECH_RATING).setText(f.getTechRating().toString()));
			unitNode.addContent(new org.jdom.Element(MINIATURE_COLLECTION).setText(f.getItemCollectionName()));
	
			org.jdom.Element mechsNode = new org.jdom.Element(ASSETS);
			for (Asset asset : f.getAssets())
			{
				if (asset instanceof Battlemech)
					mechsNode.addContent(bm.saveBattlemech((Battlemech)asset));
			}
			unitNode.addContent(mechsNode);
	
			org.jdom.Element personnelNode = new org.jdom.Element(PERSONNEL);
			for (Personnel p : f.getPersonnel())
			{
				personnelNode.addContent(savePersonnel(p));
			}
			unitNode.addContent(personnelNode);
	
			org.jdom.Element assetAssignmentsNode = new org.jdom.Element(ASSET_ASSIGNMENTS);
			for (PersonnelAssetAssignment ass : f.getPersonnelAssetAssignments())
			{
				org.jdom.Element assignedAssetNode = new org.jdom.Element(ASSET_ASSIGNMENT);
				assignedAssetNode.setAttribute(NAME, ass.getName());
				assignedAssetNode.setAttribute(ASSET_IDENTIFIER, ass.getAssetIdentifier());
				assignedAssetNode.setAttribute(ROLE, ass.getRole().toString());
				assetAssignmentsNode.addContent(assignedAssetNode);
			}
			unitNode.addContent(assetAssignmentsNode);
		}
		return unitNode;

	}

	private org.jdom.Element savePersonnel(Personnel p)
	{
		org.jdom.Element pElement = new org.jdom.Element(PERSONNEL);
		pElement.setAttribute(JOB_TYPE, p.getJobType().toString());
		pElement.setAttribute(NAME, p.getName());
		pElement.setAttribute(RANK, p.getRank().toString());
		pElement.setAttribute(RATING, p.getRating().toString());
		pElement.setAttribute(NOTES, p.getNotes());

		if (_PersonnelSaveHandlers.containsKey(p.getJobType()))
			_PersonnelSaveHandlers.get(p.getJobType()).savePersonnel(pElement, p);

		return pElement;
	}

	private Personnel loadPersonnel(org.jdom.Element e)
	{
		JobType jobType = JobType.fromString(e.getAttributeValue(JOB_TYPE));
		Personnel p = _PersonnelLoadHandlers.get(jobType).loadPersonnel(e);

		p.setName(e.getAttributeValue(NAME));
		p.setRank(Rank.fromString(e.getAttributeValue(RANK)));
		p.setRating(Rating.fromString(e.getAttributeValue(RATING)));
		p.setNotes(e.getAttributeValue(NOTES));
		return p;
	}

	private void registerPersonnelSaveHandlers()
	{
		_PersonnelSaveHandlers.put(JobType.MECHWARRIOR, new PersonnelSaveHandler() {
			public void savePersonnel(org.jdom.Element e, Personnel p)
			{
				saveMechwarrior(e, p);
			}
		});
	}

	private void registerPersonnelLoadHandlers()
	{
		_PersonnelLoadHandlers.put(JobType.MECHWARRIOR, new PersonnelLoadHandler() {
			public Personnel loadPersonnel(org.jdom.Element e)
			{
				return loadMechwarrior(e);
			}
		});
		_PersonnelLoadHandlers.put(JobType.TECHNICIAN, new PersonnelLoadHandler() {
			public Personnel loadPersonnel(org.jdom.Element e)
			{
				return loadTechnician(e);
			}
		});
		_PersonnelLoadHandlers.put(JobType.AEROSPACETECHNICIAN, new PersonnelLoadHandler() {
			public Personnel loadPersonnel(org.jdom.Element e)
			{
				return loadAstech(e);
			}
		});
		_PersonnelLoadHandlers.put(JobType.MEDTECH, new PersonnelLoadHandler() {
			public Personnel loadPersonnel(org.jdom.Element e)
			{
				return loadMedic(e);
			}
		});
		_PersonnelLoadHandlers.put(JobType.ADMINISTRATION, new PersonnelLoadHandler() {
			public Personnel loadPersonnel(org.jdom.Element e)
			{
				return loadAdministrator(e);
			}
		});
		_PersonnelLoadHandlers.put(JobType.ASSISTANTTECHNICIAN, new PersonnelLoadHandler() {
			public Personnel loadPersonnel(org.jdom.Element e)
			{
				return loadAstech(e);
			}
		});
	}

	private void saveMechwarrior(org.jdom.Element e, Personnel p)
	{
		Mechwarrior mw = (Mechwarrior) p;
		e.setAttribute(PILOTING_SKILL, Integer.toString(mw.getPilotingSkill()));
		e.setAttribute(GUNNERY_SKILL, Integer.toString(mw.getGunnerySkill()));
		e.setAttribute(HITS, Integer.toString(mw.getHits()));

		e.setAttribute(MISSIONS, Integer.toString(mw.getMissions()));
	}

	private Personnel loadMechwarrior(org.jdom.Element e)
	{
		Mechwarrior mw = new Mechwarrior();

		mw.setPilotingSkill(Integer.parseInt(e.getAttributeValue(PILOTING_SKILL)));
		mw.setGunnerySkill(Integer.parseInt(e.getAttributeValue(GUNNERY_SKILL)));
		mw.setHits(Integer.parseInt(e.getAttributeValue(HITS)));

		if (e.getAttribute(MISSIONS) != null)
			mw.setMissions(Integer.parseInt(e.getAttributeValue(MISSIONS)));

		return mw;
	}

	private Personnel loadTechnician(org.jdom.Element e)
	{
		return new Technician();
	}

	private Personnel loadAstech(org.jdom.Element e)
	{
		return new Astech();
	}

	private Personnel loadMedic(org.jdom.Element e)
	{
		return new Medic();
	}

	private Personnel loadAdministrator(org.jdom.Element e)
	{
		return new Administrator();
	}

	public Unit loadUnit(String filename) throws Exception
	{
		SAXBuilder b = new SAXBuilder();
		org.jdom.Document doc = b.build(new File(filename));

		org.jdom.Element unitNode = doc.getRootElement();
		return loadUnitFromElement(unitNode);
	}
	
	public Unit loadUnitFromElement(org.jdom.Element unitNode)
	{
		Unit u = new Unit();

		BattlemechManager bm = new BattlemechManager();

		u.setName(unitNode.getChildTextTrim(NAME));
		if (unitNode.getChild(ESTABLISH_DATE) != null)
		{
			try
			{
				u.setEstablishDate(SwingHelper.GetDateFromString(unitNode.getChildTextTrim(ESTABLISH_DATE)));
			}
			catch (Exception e) {}
		}
		if (unitNode.getChild(CURRENT_DATE) != null)
		{
			try
			{
				u.setCurrentDate(SwingHelper.GetDateFromString(unitNode.getChildTextTrim(CURRENT_DATE)));
			}
			catch (Exception e) {}
		}
		if (unitNode.getChild(QUALITY_RATING) != null)
		{
			u.setQualityRating(QualityRating.fromString(unitNode.getChildTextTrim(QUALITY_RATING)));
		}
		else
		{
			u.setQualityRating(QualityRating.D);
		}
		if (unitNode.getChild(TECH_RATING) != null)
		{
			u.setTechRating(TechRating.fromString(unitNode.getChildTextTrim(TECH_RATING)));
		}
		else
		{
			u.setTechRating(TechRating.D);
		}
		if (unitNode.getChild(NOTES) != null)
		{
			u.setNotes(unitNode.getChildTextTrim(NOTES));
		}
		
		if (unitNode.getChild(PLAYER) != null)
			u.setPlayer(PlayerManager.getInstance().getPlayer(unitNode.getChildTextTrim(PLAYER)));
		
		if (unitNode.getChild(WARCHEST_POINTS) != null)
			u.setWarchestPoints(Integer.parseInt(unitNode.getChildTextTrim(WARCHEST_POINTS)));

		org.jdom.Element mechsElement = unitNode.getChild(BATTLEMECHS);
		Iterator<?> iter = mechsElement.getChildren().iterator();
		while (iter.hasNext())
		{
			org.jdom.Element mechElement = (org.jdom.Element) iter.next();
			u.getAssets().add(bm.loadBattlemech(mechElement));
		}
		org.jdom.Element personnelElement = unitNode.getChild(PERSONNEL);
		iter = personnelElement.getChildren().iterator();
		while (iter.hasNext())
		{
			org.jdom.Element pe = (org.jdom.Element) iter.next();
			u.getPersonnel().add(loadPersonnel(pe));
		}
		org.jdom.Element assignedMissionsElement = unitNode.getChild(ASSIGNED_MISSION);
		if (assignedMissionsElement != null)
		{
			Long missionID = Long.parseLong(assignedMissionsElement.getAttributeValue(ID));
			String missionTitle = assignedMissionsElement.getAttributeValue(TITLE);
			u.setAssignedMission(missionID, missionTitle);
		}

		org.jdom.Element completedMissionsElement = unitNode.getChild(COMPLETED_MISSIONS);
		if (completedMissionsElement != null)
		{
			iter = completedMissionsElement.getChildren(COMPLETED_MISSION).iterator();
			while (iter.hasNext())
			{
				try
				{
					org.jdom.Element me = (org.jdom.Element)iter.next();
					long missionID = Long.parseLong(me.getAttributeValue(MISSION_ID));
					String missionTitle = me.getAttributeValue(MISSION_TITLE);
					CompletedMission.Result missionResult = CompletedMission.Result.fromString(me.getAttributeValue(RESULT));
					int warchestPoints = Integer.parseInt(me.getAttributeValue(WARCHEST_POINTS));
					Date missionDate = SwingHelper.GetDateFromString(me.getAttributeValue(MISSION_DATE));
					
					u.addCompletedMission(new CompletedMission(missionID,missionTitle,missionResult,warchestPoints,missionDate));
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}

		org.jdom.Element assetAssignmentsElement = unitNode.getChild(ASSET_ASSIGNMENTS);
		if (assetAssignmentsElement != null)
		{
			iter = assetAssignmentsElement.getChildren(ASSET_ASSIGNMENT).iterator();
			while (iter.hasNext())
			{
				org.jdom.Element me = (org.jdom.Element)iter.next();
				String name = me.getAttributeValue(NAME);
				String assetIdentifier = me.getAttributeValue(ASSET_IDENTIFIER);
				Role role = Role.fromString(me.getAttributeValue(ROLE));
				u.addPersonnelAssignment(name, assetIdentifier, role);
			}
		}
		return u;
	}

	public Force loadForceFromElement(org.jdom.Element unitNode)
	{
		Force f = new Force();

		BattlemechManager bm = new BattlemechManager();

		f.setParentUnit(unitNode.getChildTextTrim(PARENT_UNIT));
		if (unitNode.getChild(CURRENT_DATE) != null)
		{
			try
			{
				f.setCurrentDate(SwingHelper.GetDateFromString(unitNode.getChildTextTrim(CURRENT_DATE)));
			}
			catch (Exception e) {}
		}
		if (unitNode.getChild(QUALITY_RATING) != null)
		{
			f.setQualityRating(QualityRating.fromString(unitNode.getChildTextTrim(QUALITY_RATING)));
		}
		else
		{
			f.setQualityRating(QualityRating.D);
		}
		if (unitNode.getChild(TECH_RATING) != null)
		{
			f.setTechRating(TechRating.fromString(unitNode.getChildTextTrim(TECH_RATING)));
		}
		else
		{
			f.setTechRating(TechRating.D);
		}
		if (unitNode.getChild(MINIATURE_COLLECTION) != null)
		{
			f.setItemCollectionName(unitNode.getChildTextTrim(MINIATURE_COLLECTION));
		}
		else
		{
			f.setItemCollectionName("");
		}
		
		org.jdom.Element assetsElement = unitNode.getChild(ASSETS);
		if (assetsElement != null)
		{
			Iterator<?> iter = assetsElement.getChildren().iterator();
			while (iter.hasNext())
			{
				org.jdom.Element assetElement = (org.jdom.Element) iter.next();
				if (assetElement.getName().equalsIgnoreCase(BATTLEMECH))
					f.getAssets().add(bm.loadBattlemech(assetElement));
			}
		}
		org.jdom.Element personnelElement = unitNode.getChild(PERSONNEL);
		if (personnelElement != null)
		{
			Iterator<?> iter = personnelElement.getChildren().iterator();
			while (iter.hasNext())
			{
				org.jdom.Element pe = (org.jdom.Element) iter.next();
				f.getPersonnel().add(loadPersonnel(pe));
			}
		}
		org.jdom.Element assetAssignmentsElement = unitNode.getChild(ASSET_ASSIGNMENTS);
		if (assetAssignmentsElement != null)
		{
			Iterator<?> iter = assetAssignmentsElement.getChildren(ASSET_ASSIGNMENT).iterator();
			while (iter.hasNext())
			{
				org.jdom.Element me = (org.jdom.Element)iter.next();
				String name = me.getAttributeValue(NAME);
				String assetIdentifier = me.getAttributeValue(ASSET_IDENTIFIER);
				Role role = Role.fromString(me.getAttributeValue(ROLE));
				f.addPersonnelAssignment(name, assetIdentifier, role);
			}
		}
		return f;
	}

	private void loadUnits() throws Exception
	{
		String dataPath = PropertyUtil.getStringProperty(EXTERNAL_DATA_PATH, "data") + "/units/";
		String[] files = new File(dataPath).list(new ExtensionFileFilter("xml"));
		if (files != null)
		{
			for (String fileName : files)
			{
				Unit u = loadUnit(dataPath + fileName);
				_Units.put(u.getName(), u);
			}
		}
	}

	public ArrayList<String> getUnitNames()
	{
		return new ArrayList<String>(_Units.keySet());
	}

	public Unit getUnit(String unitName)
	{
		return _Units.get(unitName);
	}

	public void refreshUnitList() throws Exception
	{
		_Units.clear();
		loadUnits();
	}

	public void deleteUnit(String unitName)
	{
		String filename = PropertyUtil.getStringProperty(EXTERNAL_DATA_PATH, "data") + "/units/" + unitName + ".xml";
		try
		{
			new File(filename).delete();
			refreshUnitList();
		} catch (Exception ex)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}

	private void loadRandomNames() throws Exception
	{
		String fileName = PropertyUtil.getStringProperty(DATA_PATH, "data") + "/RandomNames.xml";

		SAXBuilder b = new SAXBuilder();
		File f = new File(fileName);
		if (f.exists())
		{
			Document doc = b.build(f);
	
			org.jdom.Element rootElement = doc.getRootElement();
	
			Iterator<?> iter = rootElement.getChildren().iterator();
			while (iter.hasNext())
			{
				org.jdom.Element randomNameElement = (org.jdom.Element) iter.next();
				String n = randomNameElement.getAttributeValue(NAME);
				String sn = randomNameElement.getAttributeValue(SURNAME);
				_RandomNames.add(new RandomName(n, sn));
			}
		}
	}

	private void saveRandomNames() throws Exception
	{
		org.jdom.Document doc = new org.jdom.Document();

		org.jdom.Element rootElement = new org.jdom.Element(RANDOM_NAMES);
		doc.addContent(rootElement);

		for (RandomName rn : _RandomNames)
		{
			org.jdom.Element nameElement = new org.jdom.Element(RANDOM_NAME);
			nameElement.setAttribute(NAME, rn.getName());
			nameElement.setAttribute(SURNAME, rn.getSurname());
			rootElement.addContent(nameElement);
		}

		String fileName = PropertyUtil.getStringProperty(DATA_PATH, "data") + "/RandomNames.xml";
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		out.output(doc, new FileOutputStream(fileName));
	}

	public void purgeRandomNames(ArrayList<RandomName> names) throws Exception
	{
		for (RandomName rn : names)
		{
			_RandomNames.remove(rn);
		}
		saveRandomNames();
	}

	public void ResetRandomNames()
	{
		_LastServedName = -1;
	}

	public RandomName GetRandomName()
	{
		return _RandomNames.get(++_LastServedName);
	}
	
	private com.itextpdf.text.pdf.PdfPCell createHeaderCell(String content, BaseColor foreColor, BaseColor background) throws Exception
	{
		Font f = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD);
		f.setColor(foreColor);
		com.itextpdf.text.Phrase phrase = new Phrase(content, f);
		com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(phrase);
		cell.setBackgroundColor(background);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		return cell;
	}
	
	private com.itextpdf.text.pdf.PdfPCell createDataCell(String content, BaseColor foreColor, BaseColor background) throws Exception
	{
		Font f = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD);
		f.setColor(foreColor);
		com.itextpdf.text.Phrase phrase = new Phrase(content, f);
		com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(phrase);
		cell.setBackgroundColor(background);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		return cell;
	}
	
	private com.itextpdf.text.pdf.PdfPTable getRepairDetailTable(String col1Header, ArrayList<ItemRepairDetail> details, int modifiedSkillTarget) throws Exception
	{
		com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(8);
		table.setWidthPercentage(100);
		
		table.addCell(createHeaderCell(col1Header, BaseColor.WHITE, BaseColor.GRAY));
		table.addCell(createHeaderCell(TIME, BaseColor.WHITE, BaseColor.GRAY));
		table.addCell(createHeaderCell(COST, BaseColor.WHITE, BaseColor.GRAY));
		table.addCell(createHeaderCell(TARGET_NUMBER, BaseColor.WHITE, BaseColor.GRAY));
		table.addCell(createHeaderCell(PARTIAL_REPAIR_NUMBER, BaseColor.WHITE, BaseColor.GRAY));
		table.addCell(createHeaderCell(PARTIAL_REPAIR_EFFECT, BaseColor.WHITE, BaseColor.GRAY));
		table.addCell(createHeaderCell(MANUFACTURER, BaseColor.WHITE, BaseColor.GRAY));
		table.addCell(createHeaderCell(MODEL, BaseColor.WHITE, BaseColor.GRAY));
		table.setHeaderRows(1);
		
		for (ItemRepairDetail ird : details)
		{
			table.addCell(ird.getItemType());
			table.addCell(Integer.toString(ird.getTime()));
			
			double cost = ird.getCost();
			if (cost >= 0)
				table.addCell(Double.toString(ird.getCost()));
			else
				table.addCell("**");
			
			table.addCell(Integer.toString(modifiedSkillTarget + ird.getSkillModifier()));
			
			if (ird.getPartialRepair() > Integer.MIN_VALUE)
				table.addCell(Integer.toString(modifiedSkillTarget + ird.getSkillModifier() - ird.getPartialRepair()));				
			else
				table.addCell(" ");
				
			table.addCell(ird.getPartialRepairEffect());
			table.addCell(ird.getItemManufacturer());
			table.addCell(ird.getItemModel());
		}
		
		return table;
	}
	
	private com.itextpdf.text.pdf.PdfPTable getBattlemechRepairReport(Battlemech mech, int modifiedSkillTarget) throws Exception
	{
		BattlemechManager bm = new BattlemechManager();
		BattlemechRepairReport report = bm.createRepairReport(mech, modifiedSkillTarget);
		com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(2);
		table.setWidths(new int[] {1,9});
		table.setWidthPercentage(98);
		
		com.itextpdf.text.pdf.PdfPCell header1Cell = createHeaderCell(mech.getDetails(), BaseColor.WHITE, BaseColor.BLACK);
		header1Cell.setColspan(2);
		table.addCell(header1Cell);
		//table.setHeaderRows(1);
		
		com.itextpdf.text.pdf.PdfPCell mechBVCell = createDataCell("Battle Value: " + Integer.toString(mech.getBV()), BaseColor.BLACK, BaseColor.WHITE);
		mechBVCell.setColspan(2);
		table.addCell(mechBVCell);
		
		com.itextpdf.text.pdf.PdfPCell mechAdjBVCell = createDataCell("Adjusted Battle Value: " + Integer.toString(mech.getAdjustedBV()), BaseColor.BLACK, BaseColor.WHITE);
		mechAdjBVCell.setColspan(2);
		table.addCell(mechAdjBVCell);

		if (report.hasReplacementDetails())
		{
			com.itextpdf.text.pdf.PdfPCell replacementHeaderCell = createHeaderCell("Replacements", BaseColor.WHITE, BaseColor.BLACK);
			replacementHeaderCell.setColspan(2);
			table.addCell(replacementHeaderCell);
			
			if (report.hasArmourReplacementDetails())
			{
				table.addCell("Armour");
				table.addCell(getRepairDetailTable("Location", report.getArmourReplacementDetails(), modifiedSkillTarget));
			}
			if (report.hasSectionReplacementDetails())
			{
				table.addCell("Section");
				table.addCell(getRepairDetailTable("Section", report.getSectionReplacementDetails(), modifiedSkillTarget));
			}
			if (report.hasItemReplacementDetails())
			{
				table.addCell("Item");
				table.addCell(getRepairDetailTable("Mounted Item", report.getItemReplacementDetails(), modifiedSkillTarget));
			}
		}
		
		if (report.hasRepairDetails())
		{
			com.itextpdf.text.pdf.PdfPCell repairsHeaderCell = createHeaderCell("Repairs", BaseColor.WHITE, BaseColor.BLACK);
			repairsHeaderCell.setColspan(2);
			table.addCell(repairsHeaderCell);
			
			if (report.hasInternalRepairDetails())
			{
				table.addCell("Armour");
				table.addCell(getRepairDetailTable("Location", report.getInternalRepairDetails(), modifiedSkillTarget));
			}
			if (report.hasSectionRepairDetails())
			{
				table.addCell("Section");
				table.addCell(getRepairDetailTable("Section", report.getSectionRepairDetails(), modifiedSkillTarget));
			}
			if (report.hasItemRepairDetails())
			{
				table.addCell("Item");
				table.addCell(getRepairDetailTable("Mounted Item", report.getItemRepairDetails(), modifiedSkillTarget));
			}
		}

		return table;
	}
	
	public void printUnitSummaryToPDF(Unit unit) throws Exception
	{
		String path = PropertyUtil.getStringProperty(EXTERNAL_DATA_PATH, "data");
		String filename = path + "/units/" + unit.getName() + " - Summary.pdf";

		File f = new File(filename);
		if (f.exists())
			f.delete();
		
		com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4, 5, 5, 5, 5);
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		
		Paragraph title1 = new Paragraph("Unit Summary - " + unit.getName(), FontFactory.getFont(FontFactory.HELVETICA, 24, Font.BOLDITALIC));
		Chapter chapter1 = new Chapter(title1, 1);
		chapter1.setNumberDepth(0);
		
		chapter1.add(new Paragraph("Current Date: " + SwingHelper.FormatDate(unit.getCurrentDate()),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
		chapter1.add(new Paragraph("Establish Date: " + SwingHelper.FormatDate(unit.getEstablishDate()),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
		chapter1.add(new Paragraph("Warchest Points: " + Double.toString(unit.getWarchestPoints()),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
		chapter1.add(new Paragraph("Base Monthly Salary: " + Double.toString(unit.getBaseMonthlySalary()),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));

		chapter1.add(new Paragraph(PERSONNEL,FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL)));
		chapter1.add(new Paragraph(" ",FontFactory.getFont(FontFactory.HELVETICA, 4, Font.NORMAL)));
		
		com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(7);
		table.setWidthPercentage(98);
		table.addCell(createHeaderCell(NAME, BaseColor.WHITE, BaseColor.BLACK));
		table.addCell(createHeaderCell("Callsign", BaseColor.WHITE, BaseColor.BLACK));
		table.addCell(createHeaderCell(RANK, BaseColor.WHITE, BaseColor.BLACK));
		table.addCell(createHeaderCell("Job Type", BaseColor.WHITE, BaseColor.BLACK));
		table.addCell(createHeaderCell(RATING, BaseColor.WHITE, BaseColor.BLACK));
		table.addCell(createHeaderCell("Base Monthly Salary", BaseColor.WHITE, BaseColor.BLACK));
		table.addCell(createHeaderCell(NOTES, BaseColor.WHITE, BaseColor.BLACK));
		table.setHeaderRows(1);
		
		for (Personnel p : unit.getPersonnel())
		{
			table.addCell(p.getName());
			table.addCell(p.getCallsign());
			table.addCell(p.getRank().toString());
			table.addCell(p.getJobType().toString());
			table.addCell(p.getRating().toString());
			table.addCell(Double.toString(p.getJobType().GetBaseMonthlySalary()));
			table.addCell(p.getNotes());
			
		}
		chapter1.add(table);

		chapter1.add(new Paragraph("Completed Missions",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL)));
		chapter1.add(new Paragraph(" ",FontFactory.getFont(FontFactory.HELVETICA, 4, Font.NORMAL)));
		com.itextpdf.text.pdf.PdfPTable missionTable = new com.itextpdf.text.pdf.PdfPTable(5);
		missionTable.setWidthPercentage(98);
		missionTable.addCell(createHeaderCell(ID, BaseColor.WHITE, BaseColor.BLACK));
		missionTable.addCell(createHeaderCell(TITLE, BaseColor.WHITE, BaseColor.BLACK));
		missionTable.addCell(createHeaderCell("Date", BaseColor.WHITE, BaseColor.BLACK));
		missionTable.addCell(createHeaderCell(RESULT, BaseColor.WHITE, BaseColor.BLACK));
		missionTable.addCell(createHeaderCell("Prize Money", BaseColor.WHITE, BaseColor.BLACK));
		missionTable.setHeaderRows(1);
		
		for (CompletedMission cm : unit.getCompletedMissions())
		{
			missionTable.addCell(Long.toString(cm.getMissionIdentifier()));
			missionTable.addCell(cm.getMissionTitle());
			missionTable.addCell(SwingHelper.FormatDate(cm.getMissionDate()));
			missionTable.addCell(cm.getResult().toString());
			missionTable.addCell(Integer.toString(cm.getWarchestPoints()));			
		}
		chapter1.add(missionTable);
		document.add(chapter1);
		
		document.setPageSize(PageSize.A4.rotate());
		Paragraph title2 = new Paragraph("Asset Condition Reports", FontFactory.getFont(FontFactory.HELVETICA, 24, Font.BOLDITALIC));
		Chapter chapter2 = new Chapter(title2, 2);
		chapter2.setNumberDepth(0);
		chapter2.add(new Paragraph(" ",FontFactory.getFont(FontFactory.HELVETICA, 4, Font.NORMAL)));

		for (Battlemech mech : unit.getBattlemechs())
		{
			// TODO: make sure we use the units real skill target
			chapter2.add(getBattlemechRepairReport(mech, 7));
			chapter2.add(new Paragraph(" ",FontFactory.getFont(FontFactory.HELVETICA, 4, Font.NORMAL)));
		}
		
		
		document.add(chapter2);
		
		document.setPageSize(PageSize.A4);
		Chapter chapter3 = new Chapter(3);
		chapter3.setNumberDepth(0);
		
		int elementID = 0;
		for (Battlemech mech : unit.getBattlemechs())				
		{
			Mechwarrior warrior = unit.getMechwarriorAssignedToMech(mech.getIdentifier());
			BufferedImage image = BattlemechRenderer.getInstance().RenderBattlemech(mech, warrior);
			String mechFilename = path + "/units/" + unit.getName() + "Element " + Integer.toString(elementID) + ".png";
			File mechFile = new File(mechFilename);
	        ImageIO.write(image, "PNG", mechFile);
	        
	        Image mechImage = Image.getInstance(mechFile.getAbsolutePath());
	        mechImage.scalePercent(48);
			chapter3.add(mechImage);
			mechFile.delete();
	        
			elementID++;
		}
		
		document.add(chapter3);
		
		document.close();
	}
	
}
