package bt.managers;

import java.awt.image.BufferedImage;
import java.io.File;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

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

import bt.elements.BattleValue;
import bt.elements.Battlemech;
import bt.elements.BattlemechRepairReport;
import bt.elements.ItemRepairDetail;
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
	public interface PersonnelSaveHandler
	{
		public void savePersonnel(org.jdom.Element e, Personnel p);
	}

	public interface PersonnelLoadHandler
	{
		public Personnel loadPersonnel(org.jdom.Element e);
	}

	private static UnitManager theInstance = new UnitManager();

	private HashMap<String, MechUnitParameters> _Parameters = new HashMap<String, MechUnitParameters>();
	private Vector<MechAvailability> _MechAvailability = new Vector<MechAvailability>();
	private Vector<RandomName> _RandomNames = new Vector<RandomName>();
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
	
	public Vector<Unit> getUnits()
	{
		return new Vector<Unit>(_Units.values()); 
	}
	
	public HashMap<String, MechUnitParameters> getMechUnitParameters()
	{
		return _Parameters;
	}

	private void loadMechUnitParameters() throws Exception
	{
		String fileName = PropertyUtil.getStringProperty("DataPath", "data") + "/RandomMechUnitParameters.xml";

		SAXBuilder b = new SAXBuilder();
		Document doc = b.build(new File(fileName));

		org.jdom.Element rootElement = doc.getRootElement();

		Iterator<?> iter = rootElement.getChildren("MechUnitParameters").iterator();
		while (iter.hasNext())
		{
			org.jdom.Element paramElement = (org.jdom.Element) iter.next();

			MechUnitParameters mup = new MechUnitParameters();

			mup.setName(paramElement.getAttributeValue("Name"));
			mup.setMechCount(Integer.parseInt(paramElement.getAttributeValue("MechCount")));
			mup.setMinBV(Integer.parseInt(paramElement.getAttributeValue("MinBV")));
			mup.setMaxBV(Integer.parseInt(paramElement.getAttributeValue("MaxBV")));
			mup.setMinWeight(Integer.parseInt(paramElement.getAttributeValue("MinWeight")));
			mup.setMaxWeight(Integer.parseInt(paramElement.getAttributeValue("MaxWeight")));

			Iterator<?> weightIter = paramElement.getChildren("IncludedMechWeight").iterator();
			while (weightIter.hasNext())
			{
				org.jdom.Element incWeightElement = (org.jdom.Element) weightIter.next();
				mup.getIncludedMechWeights().add(Integer.parseInt(incWeightElement.getText()));
			}
			_Parameters.put(mup.getName(), mup);
		}
		iter = rootElement.getChildren("MechAvailability").iterator();
		while (iter.hasNext())
		{
			org.jdom.Element paramElement = (org.jdom.Element) iter.next();
			Iterator<?> availIter = paramElement.getChildren().iterator();
			while (availIter.hasNext())
			{
				org.jdom.Element availElement = (org.jdom.Element) availIter.next();

				MechAvailability avail = new MechAvailability();
				avail.setName(availElement.getAttributeValue("Name"));
				avail.setVariant(availElement.getAttributeValue("Variant"));
				avail.setWeight(Integer.parseInt(availElement.getAttributeValue("Weight")));
				avail.setBV(Integer.parseInt(availElement.getAttributeValue("BV")));
				avail.setAvailability(Integer.parseInt(availElement.getAttributeValue("Availability")));

				_MechAvailability.add(avail);
			}
		}
	}

	public Unit GenerateUnit(Player p, String unitName, MechUnitParameters mup, Rating rating, QualityRating qualityRating, TechRating techRating, double startingBankBalance) throws Exception
	{
		Unit u = new Unit();
		u.setPlayer(p);
		u.setName(unitName);
		u.setQualityRating(qualityRating);
		u.setTechRating(techRating);
		u.setCurrentBankBalance(startingBankBalance);

		u.addBattlemechs(generateLance(mup));
		int supportReq = 0;

		for (Battlemech mech : u.getBattlemechs())
		{
			Mechwarrior mw = new Mechwarrior();
			mw.setRating(rating);
			mw.setPilotingSkill(rating.getPilotingValue());
			mw.setGunnerySkill(rating.getGunneryValue());

			RandomName rn = RandomNameManager.getInstance().GetRandomName();
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
			RandomName rn = RandomNameManager.getInstance().GetRandomName();
			tech.setName(rn.toString());
			u.getPersonnel().add(tech);
		}
		for (int i = 0; i < numASTechs; i++)
		{
			Astech tech = new Astech();
			tech.setRating(rating);
			RandomName rn = RandomNameManager.getInstance().GetRandomName();
			tech.setName(rn.toString());
			u.getPersonnel().add(tech);
		}
		for (int i = 0; i < numMedics; i++)
		{
			Medic tech = new Medic();
			tech.setRating(rating);
			RandomName rn = RandomNameManager.getInstance().GetRandomName();
			tech.setName(rn.toString());
			u.getPersonnel().add(tech);
		}
		for (int i = 0; i < numAdmin; i++)
		{
			Administrator admin = new Administrator();
			admin.setRating(rating);
			RandomName rn = RandomNameManager.getInstance().GetRandomName();
			admin.setName(rn.toString());
			u.getPersonnel().add(admin);
		}

		return u;
	}

	public Unit GenerateUnit(Player p, String unitName, String lanceWeight, Rating rating, QualityRating qualityRating, TechRating techRating, double startingBankBalance) throws Exception
	{
		if (!_Parameters.containsKey(lanceWeight))
			throw new IllegalArgumentException("Unable to determine lance parameters from Lance Weight : " + lanceWeight);

		MechUnitParameters mup = _Parameters.get(lanceWeight);

		return GenerateUnit(p, unitName, mup, rating, qualityRating, techRating, startingBankBalance);

	}

	private Vector<Battlemech> generateLance(MechUnitParameters mup) throws Exception
	{
		Vector<Battlemech> mechs = new Vector<Battlemech>();

		DesignManager dm = DesignManager.getInstance();
		BattlemechManager bm = new BattlemechManager();

		int attempts = 0;
		while (mechs.size() == 0 && attempts < 10)
		{
			attempts++;
			Vector<MechAvailability> ValidMechs = new Vector<MechAvailability>(_MechAvailability);
			for (int i = ValidMechs.size() - 1; i >= 0; i--)
			{
				MechAvailability ma = ValidMechs.elementAt(i);
				if (!mup.getIncludedMechWeights().contains(new Integer(ma.getWeight())))
					ValidMechs.remove(ma);
			}
	
			Vector<BattleValue> elements = new Vector<BattleValue>();
			while (elements.size() < mup.getMechCount())
			{
				for (int mechDup = 0; mechDup < mup.getMechCount() - 1; mechDup++)
				{
					for (MechAvailability ma : ValidMechs)
					{
						if (Dice.d6(2) >= ma.getAvailability())
							elements.add(ma);
					}
				}
			}
	
			Vector<Vector<Integer>> validSets = getValidSubsetSums(elements, mup.getMechCount(), mup.getMaxBV(), mup.getMinBV());
			if (validSets.size() > 0)
			{
				Vector<Integer> selectedSet = validSets.elementAt(0);
				if (validSets.size() > 1)
					selectedSet = validSets.elementAt(Dice.random(validSets.size()) - 1);
	
				for (int i = 0; i < selectedSet.size(); i++)
				{
					MechAvailability ma = (MechAvailability)elements.elementAt(selectedSet.elementAt(i));
					BattlemechDesign bd = dm.Design(ma.getVariant() + " " + ma.getName());
					if (bd == null)
						throw new Exception("Unable to load Design " + ma.getVariant() + " " + ma.getName());
					
					Battlemech b = bm.createBattlemechFromDesign(bd);
					mechs.add(b);
				}
			}
		}
		return mechs;
	}

	private void getValidSubsetSums(Vector<BattleValue> elements, int elementCount, int bvMax, int bvMin, int currentIndex, int bvTotal, int elementTotal, Vector<Integer> currentSet, Vector<Vector<Integer>> validSets)
	{
		if (currentIndex >= elements.size())
			return;

		elementTotal++;
		bvTotal += elements.elementAt(currentIndex).getBV();
		currentSet.add(new Integer(currentIndex));
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

	private Vector<Vector<Integer>> getValidSubsetSums(Vector<BattleValue> elements, int elementCount, int bvMax, int bvMin)
	{
		Vector<Vector<Integer>> validSets = new Vector<Vector<Integer>>();
		
		for (int skip = 0; skip < elements.size(); skip++)
		{
			for (int index = skip; index < elements.size(); index++)
			{
				getValidSubsetSums(elements, elementCount, bvMax, bvMin, index, 0, 0, new Vector<Integer>(), validSets);
			}
		}
		
		return validSets;
	}

	public void saveUnitSummaries(Vector<Unit> units) throws Exception
	{
		org.jdom.Document doc = new org.jdom.Document();

		org.jdom.Element unitsNode = new org.jdom.Element("Units");
		doc.addContent(unitsNode);

		for (Unit u : units)
		{
			org.jdom.Element unitElement = new org.jdom.Element("Unit");
			int totalBV = 0;
			int totalWeight = 0;
			unitElement.setAttribute("Name", u.getName());
			
			for (Battlemech b : u.getBattlemechs())
			{
				totalBV += b.getBV();
				totalWeight += b.getWeight();

				org.jdom.Element mechElement = new org.jdom.Element("Battlemech");
				mechElement.setAttribute("Name", b.getDesignName());
				mechElement.setAttribute("Variant", b.getDesignVariant());
				mechElement.setAttribute("Weight", Integer.toString(b.getWeight()));
				mechElement.setAttribute("BV", Integer.toString(b.getBV()));

				unitElement.addContent(mechElement);
			}
			unitElement.setAttribute("TotalWeight", Integer.toString(totalWeight));
			unitElement.setAttribute("TotalBV", Integer.toString(totalBV));

			unitsNode.addContent(unitElement);
		}
		String fileName = PropertyUtil.getStringProperty("ExternalDataPath", "data") + "/units/UnitSummaries.xml";
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		out.output(doc, new FileOutputStream(fileName));
	}

	public void saveUnit(Unit u) throws Exception
	{
		String fileName = PropertyUtil.getStringProperty("ExternalDataPath", "data") + "/units/" + u.getName() + ".xml";

		org.jdom.Document doc = new org.jdom.Document();
		doc.setRootElement(saveUnitToElement(u));

		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		out.output(doc, new FileOutputStream(fileName));
	}
	
	public org.jdom.Element saveUnitToElement(Unit u)
	{
		BattlemechManager bm = new BattlemechManager();

		org.jdom.Element unitNode = new org.jdom.Element("Unit");

		unitNode.addContent(new org.jdom.Element("Name").setText(u.getName()));
		unitNode.addContent(new org.jdom.Element("EstablishDate").setText(SwingHelper.FormatDate(u.getEstablishDate())));
		unitNode.addContent(new org.jdom.Element("CurrentDate").setText(SwingHelper.FormatDate(u.getCurrentDate())));
		unitNode.addContent(new org.jdom.Element("QualityRating").setText(u.getQualityRating().toString()));
		unitNode.addContent(new org.jdom.Element("TechRating").setText(u.getTechRating().toString()));
		unitNode.addContent(new org.jdom.Element("CurrentBankBalance").setText(Double.toString(u.getCurrentBankBalance())));
		unitNode.addContent(new org.jdom.Element("Notes").setText(u.getNotes()));

		if (u.getPlayer() != null)
			unitNode.addContent(new org.jdom.Element("Player").setText(u.getPlayer().getName()));

		org.jdom.Element mechsNode = new org.jdom.Element("Battlemechs");
		for (Battlemech mech : u.getBattlemechs())
		{
			mechsNode.addContent(bm.saveBattlemech(mech));
		}
		unitNode.addContent(mechsNode);

		org.jdom.Element personnelNode = new org.jdom.Element("Personnel");
		for (Personnel p : u.getPersonnel())
		{
			personnelNode.addContent(savePersonnel(p));
		}
		unitNode.addContent(personnelNode);

		if (u.getAssignedMission() != null)
		{
			org.jdom.Element assignedMissionNode = new org.jdom.Element("AssignedMission");
			assignedMissionNode.setAttribute("ID", Long.toString(u.getAssignedMission()));
			assignedMissionNode.setAttribute("Title", u.getAssignedMissionTitle());
			unitNode.addContent(assignedMissionNode);
		}
		
		org.jdom.Element completedMissionsNode = new org.jdom.Element("CompletedMissions");
		for (CompletedMission mission : u.getCompletedMissions())
		{
			org.jdom.Element completedMissionElement = new org.jdom.Element("CompletedMission");
			completedMissionElement.setAttribute("MissionID", Long.toString(mission.getMissionIdentifier()));
			completedMissionElement.setAttribute("MissionTitle", mission.getMissionTitle());
			completedMissionElement.setAttribute("Result", mission.getResult().toString());
			completedMissionElement.setAttribute("PrizeMoney", Double.toString(mission.getPrizeMoney()));
			completedMissionElement.setAttribute("MissionDate", SwingHelper.FormatDate(mission.getMissionDate()));
			
			completedMissionsNode.addContent(completedMissionElement);
		}
		unitNode.addContent(completedMissionsNode);

		org.jdom.Element assetAssignmentsNode = new org.jdom.Element("AssetAssignments");
		for (PersonnelAssetAssignment ass : u.getPersonnelAssetAssignments())
		{
			org.jdom.Element assignedAssetNode = new org.jdom.Element("AssetAssignment");
			assignedAssetNode.setAttribute("Name", ass.getName());
			assignedAssetNode.setAttribute("AssetIdentifier", ass.getAssetIdentifier());
			assignedAssetNode.setAttribute("Role", ass.getRole().toString());
			assetAssignmentsNode.addContent(assignedAssetNode);
		}
		unitNode.addContent(assetAssignmentsNode);
		
		return unitNode;

	}

	private org.jdom.Element savePersonnel(Personnel p)
	{
		org.jdom.Element pElement = new org.jdom.Element("Personnel");
		pElement.setAttribute("JobType", p.getJobType().toString());
		pElement.setAttribute("Name", p.getName());
		pElement.setAttribute("Rank", p.getRank().toString());
		pElement.setAttribute("Rating", p.getRating().toString());
		pElement.setAttribute("Notes", p.getNotes());

		if (_PersonnelSaveHandlers.containsKey(p.getJobType()))
			_PersonnelSaveHandlers.get(p.getJobType()).savePersonnel(pElement, p);

		return pElement;
	}

	private Personnel loadPersonnel(org.jdom.Element e)
	{
		JobType jobType = JobType.fromString(e.getAttributeValue("JobType"));
		System.out.println("Loading JobType ->" + jobType.toString() + "<-");
		Personnel p = _PersonnelLoadHandlers.get(jobType).loadPersonnel(e);

		p.setName(e.getAttributeValue("Name"));
		p.setRank(Rank.fromString(e.getAttributeValue("Rank")));
		p.setRating(Rating.fromString(e.getAttributeValue("Rating")));
		p.setNotes(e.getAttributeValue("Notes"));
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
		e.setAttribute("PilotingSkill", Integer.toString(mw.getPilotingSkill()));
		e.setAttribute("GunnerySkill", Integer.toString(mw.getGunnerySkill()));
		e.setAttribute("Hits", Integer.toString(mw.getHits()));

		e.setAttribute("SuccessfulFiringAttempts", Integer.toString(mw.getSuccessfulFiringAttempts()));
		e.setAttribute("FailedFiringAttempts", Integer.toString(mw.getFailedFiringAttempts()));
		e.setAttribute("SuccessfulPilotingRolls", Integer.toString(mw.getSuccessfulPilotingRolls()));
		e.setAttribute("FailedPilotingRolls", Integer.toString(mw.getFailedPilotingRolls()));

		e.setAttribute("Missions", Integer.toString(mw.getMissions()));
	}

	private Personnel loadMechwarrior(org.jdom.Element e)
	{
		Mechwarrior mw = new Mechwarrior();

		mw.setPilotingSkill(Integer.parseInt(e.getAttributeValue("PilotingSkill")));
		mw.setGunnerySkill(Integer.parseInt(e.getAttributeValue("GunnerySkill")));
		mw.setHits(Integer.parseInt(e.getAttributeValue("Hits")));

		if (e.getAttribute("SuccessfulFiringAttempts") != null)
			mw.setSuccessfulFiringAttempts(Integer.parseInt(e.getAttributeValue("SuccessfulFiringAttempts")));
		if (e.getAttribute("FailedFiringAttempts") != null)
			mw.setFailedFiringAttempts(Integer.parseInt(e.getAttributeValue("FailedFiringAttempts")));
		if (e.getAttribute("SuccessfulPilotingRolls") != null)
			mw.setSuccessfulPilotingRolls(Integer.parseInt(e.getAttributeValue("SuccessfulPilotingRolls")));
		if (e.getAttribute("FailedPilotingRolls") != null)
			mw.setFailedPilotingRolls(Integer.parseInt(e.getAttributeValue("FailedPilotingRolls")));

		if (e.getAttribute("Missions") != null)
			mw.setMissions(Integer.parseInt(e.getAttributeValue("Missions")));

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

		u.setName(unitNode.getChildTextTrim("Name"));
		if (unitNode.getChild("EstablishDate") != null)
		{
			try
			{
				u.setEstablishDate(SwingHelper.GetDateFromString(unitNode.getChildTextTrim("EstablishDate")));
			}
			catch (Exception e) {}
		}
		if (unitNode.getChild("CurrentDate") != null)
		{
			try
			{
				u.setCurrentDate(SwingHelper.GetDateFromString(unitNode.getChildTextTrim("CurrentDate")));
			}
			catch (Exception e) {}
		}
		if (unitNode.getChild("QualityRating") != null)
		{
			u.setQualityRating(QualityRating.fromString(unitNode.getChildTextTrim("QualityRating")));
		}
		else
		{
			u.setQualityRating(QualityRating.D);
		}
		if (unitNode.getChild("TechRating") != null)
		{
			u.setTechRating(TechRating.fromString(unitNode.getChildTextTrim("TechRating")));
		}
		else
		{
			u.setTechRating(TechRating.D);
		}
		if (unitNode.getChild("Notes") != null)
		{
			u.setNotes(unitNode.getChildTextTrim("Notes"));
		}
		
		if (unitNode.getChild("Player") != null)
			u.setPlayer(PlayerManager.getInstance().getPlayer(unitNode.getChildTextTrim("Player")));
		
		if (unitNode.getChild("CurrentBankBalance") != null)
			u.setCurrentBankBalance(Double.parseDouble(unitNode.getChildTextTrim("CurrentBankBalance")));

		org.jdom.Element mechsElement = unitNode.getChild("Battlemechs");
		Iterator<?> iter = mechsElement.getChildren().iterator();
		while (iter.hasNext())
		{
			org.jdom.Element mechElement = (org.jdom.Element) iter.next();
			u.getAssets().add(bm.loadBattlemech(mechElement));
		}
		org.jdom.Element personnelElement = unitNode.getChild("Personnel");
		iter = personnelElement.getChildren().iterator();
		while (iter.hasNext())
		{
			org.jdom.Element pe = (org.jdom.Element) iter.next();
			u.getPersonnel().add(loadPersonnel(pe));
		}
		org.jdom.Element assignedMissionsElement = unitNode.getChild("AssignedMission");
		if (assignedMissionsElement != null)
		{
			Long missionID = Long.parseLong(assignedMissionsElement.getAttributeValue("ID"));
			String missionTitle = assignedMissionsElement.getAttributeValue("Title");
			u.setAssignedMission(missionID, missionTitle);
		}

		org.jdom.Element completedMissionsElement = unitNode.getChild("CompletedMissions");
		if (completedMissionsElement != null)
		{
			iter = completedMissionsElement.getChildren("CompletedMission").iterator();
			while (iter.hasNext())
			{
				try
				{
					org.jdom.Element me = (org.jdom.Element)iter.next();
					long missionID = Long.parseLong(me.getAttributeValue("MissionID"));
					String missionTitle = me.getAttributeValue("MissionTitle");
					CompletedMission.Result missionResult = CompletedMission.Result.fromString(me.getAttributeValue("Result"));
					double prizeMoney = Double.parseDouble(me.getAttributeValue("PrizeMoney"));
					Date missionDate = SwingHelper.GetDateFromString(me.getAttributeValue("MissionDate"));
					
					u.addCompletedMission(new CompletedMission(missionID,missionTitle,missionResult,prizeMoney,missionDate));
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}

		org.jdom.Element assetAssignmentsElement = unitNode.getChild("AssetAssignments");
		if (assetAssignmentsElement != null)
		{
			iter = assetAssignmentsElement.getChildren("AssetAssignment").iterator();
			while (iter.hasNext())
			{
				org.jdom.Element me = (org.jdom.Element)iter.next();
				String name = me.getAttributeValue("Name");
				String assetIdentifier = me.getAttributeValue("AssetIdentifier");
				Role role = Role.fromString(me.getAttributeValue("Role"));
				u.addPersonnelAssignment(name, assetIdentifier, role);
			}
		}
		return u;
	}

	private void loadUnits() throws Exception
	{
		String dataPath = PropertyUtil.getStringProperty("ExternalDataPath", "data") + "/units/";
		String[] files = new File(dataPath).list(new ExtensionFileFilter("xml"));
		for (String fileName : files)
		{
			Unit u = loadUnit(dataPath + fileName);
			_Units.put(u.getName(), u);
		}
	}

	public Vector<String> getUnitNames()
	{
		return new Vector<String>(_Units.keySet());
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
		String filename = PropertyUtil.getStringProperty("ExternalDataPath", "data") + "/units/" + unitName + ".xml";
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
		String fileName = PropertyUtil.getStringProperty("DataPath", "data") + "/RandomNames.xml";

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
				String n = randomNameElement.getAttributeValue("Name");
				String sn = randomNameElement.getAttributeValue("Surname");
				_RandomNames.add(new RandomName(n, sn));
			}
		}
	}

	private void saveRandomNames() throws Exception
	{
		org.jdom.Document doc = new org.jdom.Document();

		org.jdom.Element rootElement = new org.jdom.Element("RandomNames");
		doc.addContent(rootElement);

		for (RandomName rn : _RandomNames)
		{
			org.jdom.Element nameElement = new org.jdom.Element("RandomName");
			nameElement.setAttribute("Name", rn.getName());
			nameElement.setAttribute("Surname", rn.getSurname());
			rootElement.addContent(nameElement);
		}

		String fileName = PropertyUtil.getStringProperty("DataPath", "data") + "/RandomNames.xml";
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		out.output(doc, new FileOutputStream(fileName));
	}

	public void purgeRandomNames(Vector<RandomName> names) throws Exception
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
		return _RandomNames.elementAt(++_LastServedName);
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
	
	private com.itextpdf.text.pdf.PdfPTable getRepairDetailTable(String col1Header, Vector<ItemRepairDetail> details, int modifiedSkillTarget) throws Exception
	{
		com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(8);
		table.setWidthPercentage(100);
		
		table.addCell(createHeaderCell(col1Header, BaseColor.WHITE, BaseColor.GRAY));
		table.addCell(createHeaderCell("Time", BaseColor.WHITE, BaseColor.GRAY));
		table.addCell(createHeaderCell("Cost", BaseColor.WHITE, BaseColor.GRAY));
		table.addCell(createHeaderCell("Target#", BaseColor.WHITE, BaseColor.GRAY));
		table.addCell(createHeaderCell("Partial Repair#", BaseColor.WHITE, BaseColor.GRAY));
		table.addCell(createHeaderCell("Partial Repair Effect", BaseColor.WHITE, BaseColor.GRAY));
		table.addCell(createHeaderCell("Manufacturer", BaseColor.WHITE, BaseColor.GRAY));
		table.addCell(createHeaderCell("Model", BaseColor.WHITE, BaseColor.GRAY));
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
		String path = PropertyUtil.getStringProperty("ExternalDataPath", "data");
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
		chapter1.add(new Paragraph("Current Bank Balance: " + Double.toString(unit.getCurrentBankBalance()),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
		chapter1.add(new Paragraph("Base Monthly Salary: " + Double.toString(unit.getBaseMonthlySalary()),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));

		chapter1.add(new Paragraph("Personnel",FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL)));
		chapter1.add(new Paragraph(" ",FontFactory.getFont(FontFactory.HELVETICA, 4, Font.NORMAL)));
		
		com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(7);
		table.setWidthPercentage(98);
		table.addCell(createHeaderCell("Name", BaseColor.WHITE, BaseColor.BLACK));
		table.addCell(createHeaderCell("Callsign", BaseColor.WHITE, BaseColor.BLACK));
		table.addCell(createHeaderCell("Rank", BaseColor.WHITE, BaseColor.BLACK));
		table.addCell(createHeaderCell("Job Type", BaseColor.WHITE, BaseColor.BLACK));
		table.addCell(createHeaderCell("Rating", BaseColor.WHITE, BaseColor.BLACK));
		table.addCell(createHeaderCell("Base Monthly Salary", BaseColor.WHITE, BaseColor.BLACK));
		table.addCell(createHeaderCell("Notes", BaseColor.WHITE, BaseColor.BLACK));
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
		missionTable.addCell(createHeaderCell("ID", BaseColor.WHITE, BaseColor.BLACK));
		missionTable.addCell(createHeaderCell("Title", BaseColor.WHITE, BaseColor.BLACK));
		missionTable.addCell(createHeaderCell("Date", BaseColor.WHITE, BaseColor.BLACK));
		missionTable.addCell(createHeaderCell("Result", BaseColor.WHITE, BaseColor.BLACK));
		missionTable.addCell(createHeaderCell("Prize Money", BaseColor.WHITE, BaseColor.BLACK));
		missionTable.setHeaderRows(1);
		
		for (CompletedMission cm : unit.getCompletedMissions())
		{
			missionTable.addCell(Long.toString(cm.getMissionIdentifier()));
			missionTable.addCell(cm.getMissionTitle());
			missionTable.addCell(SwingHelper.FormatDate(cm.getMissionDate()));
			missionTable.addCell(cm.getResult().toString());
			missionTable.addCell(Double.toString(cm.getPrizeMoney()));			
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
