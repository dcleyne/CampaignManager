package as.managers;

import java.io.File;

import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import as.elements.Faction;
import as.elements.UnitSummary;
import as.elements.Era;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;
import bt.util.WebFile;

public class UnitManager 
{
	private static String _BaseUrl = "http://www.masterunitlist.info";
	
	private static List<String> AerospaceTypes = Arrays.asList(
		"Advanced Aerospace - JumpShip",
		"Advanced Aerospace - Satellite",
		"Advanced Aerospace - Space Station",
		"Advanced Aerospace - Warship",
		"Aerospace - Aerospace Fighter",
		"Aerospace - Conventional Fighter",
		"Aerospace - DropShip",
		"Aerospace - Hybrid",
		"Aerospace - OmniFighter",
		"Aerospace - Small Craft"
	);
	private static List<String> VehicleTypes = Arrays.asList(
		"Combat Vehicle - Hover",
		"Combat Vehicle - Hybrid",
		"Combat Vehicle - Super Heavy Vehicle",
		"Combat Vehicle - Tracked",
		"Combat Vehicle - VTOL",
		"Combat Vehicle - Wheeled",
		"Combat Vehicle - WiGE"	
	);
	private static List<String> BattlemechTypes = Arrays.asList( 
		"BattleMech", 
		"BattleMech - LAM",
		"BattleMech - OmniMech",
		"BattleMech - QuadVee",			
		"IndustrialMech" 
	);
	private static List<String> BuildingTypes = Arrays.asList( 
		"Building - Fortress",
		"Building - Gun Emplacement",
		"Building - Standard Building"
	);
	private static List<String> NavalVesselTypes = Arrays.asList( 
		"Naval Vessel - Hybrid",
		"Naval Vessel - Submarine",
		"Naval Vessel - Surface"
	);
	private static List<String> SupportTypes = Arrays.asList( 
		"Advanced Support - Hybrid",
		"Advanced Support - Large Naval",
		"Advanced Support - Rail",
		"Advanced Support - Submarine",
		"Support Vehicle - Airship",
		"Support Vehicle - Conventional Fighter",
		"Support Vehicle - Fixed Wing",
		"Support Vehicle - Hover",
		"Support Vehicle - Hybrid",
		"Support Vehicle - Surface",
		"Support Vehicle - Tracked",
		"Support Vehicle - VTOL",
		"Support Vehicle - Wheeled",
		"Support Vehicle - WiGE"
	);

	private ArrayList<Era> _Eras = new ArrayList<Era>();
	private ArrayList<Faction> _Factions = new ArrayList<Faction>();
	private HashMap<Integer,HashMap<Integer, ArrayList<Integer>>> _FactionEraUnitLinks = new HashMap<Integer,HashMap<Integer, ArrayList<Integer>>>();
	private HashMap<Integer, UnitSummary> _UnitSummaries = new HashMap<Integer, UnitSummary>();
	
	private ArrayList<Integer> _AerospaceSummaries = new ArrayList<Integer>();
	private ArrayList<Integer> _VehicleSummaries = new ArrayList<Integer>();
	private ArrayList<Integer> _BattlemechSummaries = new ArrayList<Integer>();
	private ArrayList<Integer> _BuildingSummaries = new ArrayList<Integer>();
	private ArrayList<Integer> _NavalVesselSummaries = new ArrayList<Integer>();
	private ArrayList<Integer> _SupportSummaries = new ArrayList<Integer>();

	
	public UnitManager()
	{
		loadEras();
		loadFactions();
		loadFactionEraUnits();
		loadUnitSummaries();
	}
	
	public List<Era> getEras()
	{
		return _Eras;
	}
	
	public List<Faction> getFactions()
	{
		return _Factions;
	}
	
	public List<Integer> getFactionEraUnits(Faction faction, Era era)
	{
		return _FactionEraUnitLinks.get(faction.getID()).get(new Integer(era.getID()));
	}
	
	public List<UnitSummary> getAerospaceSummaries()
	{
		return getSummaries(_AerospaceSummaries);
	}
	
	public List<UnitSummary> getVehicleSummaries()
	{
		return getSummaries(_VehicleSummaries);
	}
	
	public List<UnitSummary> getBattlemechSummaries()
	{
		return getSummaries(_BattlemechSummaries);
	}
	
	public List<UnitSummary> getBuildingSummaries()
	{
		return getSummaries(_BuildingSummaries);
	}
	
	public List<UnitSummary> getSuppprtSummaries()
	{
		return getSummaries(_SupportSummaries);
	}
	
	public List<UnitSummary> getNavalVesselSummaries()
	{
		return getSummaries(_NavalVesselSummaries);
	}
	
	private List<UnitSummary> getSummaries(List<Integer> ids)
	{
		ArrayList<UnitSummary> summaries = new ArrayList<UnitSummary>();
		for (Integer id : ids)
			summaries.add(_UnitSummaries.get(id));
		
		return summaries;
	}
	
	public UnitSummary getUnitSummary(int id)
	{
		if (!_UnitSummaries.containsKey(id))
		{
			System.out.println("Loading unit (" + Integer.toString(id) + ")");
			UnitSummary us = loadUnitSummary(id);
			if (us != null)
				_UnitSummaries.put(id, us);
		}
		if (_UnitSummaries.containsKey(id))
			return _UnitSummaries.get(id);
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void loadEras()
	{		
		_Eras.clear();
		try
		{
			String Path = PropertyUtil.getStringProperty("DataPath", "data");
	        File f = new File(Path + "/Eras.xml");
	        
            SAXBuilder b = new SAXBuilder();
            Document playerDoc = b.build(f);
            Element root = playerDoc.getRootElement();
            List<Element> eraElements = root.getChildren("Era");
            for (Element eraElement: eraElements)
            {
            	Era e = new Era();
            	e.setID(Integer.parseInt(eraElement.getAttributeValue("ID")));
            	e.setName(eraElement.getAttributeValue("Name"));
            	e.setStartYear(Integer.parseInt(eraElement.getAttributeValue("StartYear")));
            	e.setEndYear(Integer.parseInt(eraElement.getAttributeValue("EndYear")));
            	_Eras.add(e);            	
            }
		}
		catch (Exception ex)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadFactions()
	{
		_Factions.clear();
		try
		{
			String Path = PropertyUtil.getStringProperty("DataPath", "data");
	        File f = new File(Path + "/Factions.xml");
	        
            SAXBuilder b = new SAXBuilder();
            Document playerDoc = b.build(f);
            Element root = playerDoc.getRootElement();
            List<Element> factionElements = root.getChildren("Faction");
            for (Element factionElement: factionElements)
            {
            	Faction faction = new Faction();
            	faction.setID(Integer.parseInt(factionElement.getAttributeValue("ID")));
            	faction.setName(factionElement.getAttributeValue("Name"));
            	faction.setGroup(factionElement.getAttributeValue("Group"));
            	String availableEras = factionElement.getAttributeValue("ErasAvailable");
            	String[] eras = availableEras.split(",");
            	if (eras != null)
            	{
	            	for (String era : eras)
	            	{
	            		if (!era.isEmpty())
	            			faction.addEra(Integer.parseInt(era));
	            	}
            	}
            	_Factions.add(faction);            	
            }
		}
		catch (Exception ex)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}		
	}
	
	private void addSummary(UnitSummary summary)
	{
		Integer id = summary.getID();
    	_UnitSummaries.put(id, summary);
    	
    	if (AerospaceTypes.contains(summary.getType()))
		{
    		_AerospaceSummaries.add(id);
		}

    	if (VehicleTypes.contains(summary.getType()))
		{
    		_VehicleSummaries.add(id);
		}

    	if (BattlemechTypes.contains(summary.getType()))
		{
    		_BattlemechSummaries.add(id);	
		}

    	if (BuildingTypes.contains(summary.getType()))
		{
    		_BuildingSummaries.add(id);
		}

    	if (NavalVesselTypes.contains(summary.getType()))
		{
    		_NavalVesselSummaries.add(id);
		}

    	if (SupportTypes.contains(summary.getType()))
		{
    		_SupportSummaries.add(id);
		} 
	}

	@SuppressWarnings("unchecked")
	private void loadUnitSummaries()
	{
		_UnitSummaries.clear();
		try
		{
			String Path = PropertyUtil.getStringProperty("ExternalDataPath", "data");
	        File f = new File(Path + "/UnitSummaries.xml");
	        if (f.exists())
	        {
	            SAXBuilder b = new SAXBuilder();
	            Document playerDoc = b.build(f);
	            Element root = playerDoc.getRootElement();
	            List<Element> factionElements = root.getChildren("Unit");
	            for (Element factionElement: factionElements)
	            {
	            	UnitSummary summary = new UnitSummary();
	            	summary.setID(Integer.parseInt(factionElement.getAttributeValue("ID")));
	            	summary.setName(factionElement.getAttributeValue("Name"));
	            	summary.setType(factionElement.getAttributeValue("Type"));
	            	summary.setBV(Integer.parseInt(factionElement.getAttributeValue("BV")));
	            	summary.setWeight(Integer.parseInt(factionElement.getAttributeValue("Weight")));
	            	summary.setCost(Long.parseLong(factionElement.getAttributeValue("Cost")));
	            	summary.setIntro(factionElement.getAttributeValue("Introduction"));
	            	summary.setRole(factionElement.getAttributeValue("Role"));
	            	summary.setRulesLevel(factionElement.getAttributeValue("RulesLevel"));
	            	summary.setTechnology(factionElement.getAttributeValue("Technology"));
	            	
	            	String pvString = factionElement.getAttributeValue("PV").replace("[", "").replace("]", "");
	            	String[] pvs = pvString.split(",");
	            	int index = 0;
	            	if (pvs != null)
	            	{
		            	for (String pv : pvs)
		            	{
		            		if (!pv.isEmpty())
		            		{
		            			summary.setPV(Integer.parseInt(pv.trim()), index++);
		            		}
		            	}
	            	}
	            	addSummary(summary);
	            }
	        }
		}
		catch (Exception ex)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}		
	}
	
	public void saveUnitSummaries()
	{
		try
		{
			org.jdom.Document doc = new org.jdom.Document();
	
			org.jdom.Element unitsNode = new org.jdom.Element("Units");
			doc.addContent(unitsNode);
	
			for (UnitSummary summary : _UnitSummaries.values())
			{
				org.jdom.Element unitNode = new org.jdom.Element("Unit");
				unitsNode.addContent(unitNode);
				
				unitNode.setAttribute("ID", Integer.toString(summary.getID()));
				unitNode.setAttribute("Name", summary.getName());
				unitNode.setAttribute("Type", summary.getType());				
				unitNode.setAttribute("BV", Integer.toString(summary.getBV()));
				unitNode.setAttribute("Weight", Integer.toString(summary.getWeight()));
				unitNode.setAttribute("Cost", Long.toString(summary.getWeight()));
				unitNode.setAttribute("Introduction", summary.getIntro());
				unitNode.setAttribute("Role", summary.getRole());				
				unitNode.setAttribute("RulesLevel", summary.getRulesLevel());				
				unitNode.setAttribute("Technology", summary.getTechnology());				
				unitNode.setAttribute("PV",Arrays.toString(summary.getPVs()));				
			}
	
			String fileName = PropertyUtil.getStringProperty("ExternalDataPath", "data") + "/UnitSummaries.xml";
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			out.output(doc, new FileOutputStream(fileName));
		}
		catch (Exception ex)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadFactionEraUnits()
	{
		_FactionEraUnitLinks.clear();
		try
		{
			String Path = PropertyUtil.getStringProperty("DataPath", "data");
	        File f = new File(Path + "/FactionEraUnitLinks.xml");
	        
            SAXBuilder b = new SAXBuilder();
            Document playerDoc = b.build(f);
            Element root = playerDoc.getRootElement();
            List<Element> factionElements = root.getChildren("Faction");
            for (Element factionElement: factionElements)
            {
            	Integer factionID = Integer.parseInt(factionElement.getAttributeValue("ID"));
            	if (!_FactionEraUnitLinks.containsKey(factionID))
            	{
            		_FactionEraUnitLinks.put(factionID, new HashMap<Integer, ArrayList<Integer>>());
            	}
            	
            	HashMap<Integer, ArrayList<Integer>> eraMap = _FactionEraUnitLinks.get(factionID);
            	
                List<Element> eraElements = factionElement.getChildren("Era");
                for (Element eraElement: eraElements)
                {
                	Integer eraID = Integer.parseInt(eraElement.getAttributeValue("ID"));
                	if (!eraMap.containsKey(eraID))
                	{
                		eraMap.put(eraID, new ArrayList<Integer>());
                	}
                	ArrayList<Integer> units = eraMap.get(eraID);
                	
                    Element unitElement = eraElement.getChild("Units");
                    String[] unitStrings = unitElement.getValue().split(",");
                    for (String unitString : unitStrings)
                    {
                    	unitString = unitString.trim();
                    	if (!unitString.isEmpty())
                    	{
	                    	Integer unitID = Integer.parseInt(unitString);
	                    	if (!units.contains(unitID))
	                    		units.add(unitID);
                    	}
                    }
                }
            }
		}
		catch (Exception ex)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}		
	}

	private static String[] tabDivNames = {"Tbattlemech", "Tcombat-vehicle", "Taerospace", "Tindustrialmech", "Tsupport-vehicle", "Tnaval-vessel", "Tadvanced-aerospace", "Tadvanced-support", "Tbuilding"};
	private static String divTagString = "</div>";
	
	private ArrayList<Integer> scrapeFactionEraUnitLinks(int faction, int era)
	{
		ArrayList<Integer> units = new ArrayList<Integer>();

		String url = _BaseUrl + "/Era/FactionEraDetails?FactionId=" + Integer.toString(faction) + "&EraId=" + Integer.toString(era);

		boolean successful = false;
		while (!successful)
		{
			successful = true;

			try
			{
				String content = WebFile.getWebPageContentAsString(url, "", 0);
				
				for (String tabDiv : tabDivNames)
				{
					//This is the tab declaration
					int startIndex = content.indexOf(tabDiv);
					if (startIndex == -1)
						continue;
		
					//This is the table
					startIndex = content.indexOf(tabDiv, startIndex + 1);
					if (startIndex == -1)
						continue;
		
					int endIndex = content.indexOf(divTagString, startIndex);
					
					String chunk = content.substring(startIndex, endIndex);
					processUnitChunks(chunk, units);
						
				}
			}
			catch (Exception ex)
			{
				System.out.println(url + " - Failed. Trying again");
				successful = false;
				units.clear();
			}
		}
		
		return units;
	}
	
	private static String unitChunkStart = "<tr class=\"filterable\" data-value=";
	private static String unitChunkEnd = "</tr>";
	private static String unitLineChunk = "<td><a href=\"/Unit/Details/";
	private static String unitLineChunkEnd = "/";
	private void processUnitChunks(String chunk, ArrayList<Integer> units)
	{
		int startIndex = chunk.indexOf(unitChunkStart);
		while (startIndex > -1)
		{
			int endIndex = chunk.indexOf(unitChunkEnd, startIndex);
			if (endIndex == -1)
				return; // Something has gone wrong...
			
			String tableRowChunk = chunk.substring(startIndex, endIndex);
			int idStart = tableRowChunk.indexOf(unitLineChunk) + unitLineChunk.length();
			int idEnd = tableRowChunk.indexOf(unitLineChunkEnd, idStart);
			String idValue = tableRowChunk.substring(idStart, idEnd);
			
			units.add(Integer.parseInt(idValue));
			
			startIndex = chunk.indexOf(unitChunkStart, endIndex);
		}
	}

	public void getUnitStatsImages(String outputPath, int unitId) throws Exception
	{
		getUnitStatsImage(outputPath, unitId, 4);
		getUnitStatsImage(outputPath, unitId, 3);
		getUnitStatsImage(outputPath, unitId, 2);
		getUnitStatsImage(outputPath, unitId, 5);
		getUnitStatsImage(outputPath, unitId, 1);
		getUnitStatsImage(outputPath, unitId, 7);
		getUnitStatsImage(outputPath, unitId, 6);
		getUnitStatsImage(outputPath, unitId, 0);
	}

	
	public static void getUnitStatsImage(String outputPath, int unitId, int skillLevel) throws Exception
	{
		try
		{
			String url = _BaseUrl + "/Unit/Card/" + Integer.toString(unitId) + "?skill=" + Integer.toString(skillLevel);

			byte[] bytes = WebFile.getWebPageContentAsByteArray(url, "", 0);
			if (bytes != null)
			{
				FileOutputStream fos = new FileOutputStream(outputPath + Integer.toString(unitId) + "-" + Integer.toString(skillLevel) + ".png");
				fos.write(bytes);
				fos.close();
			}
		}
		catch (Exception ex)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}
	

	private int getLowerSkillDecreaseValue(int pv)
	{
		if (pv < 15) return 1;
		if (pv < 25) return 2;
		if (pv < 35) return 3;
		if (pv < 45) return 4;
		if (pv < 55) return 5;
		if (pv < 65) return 6;
		if (pv < 75) return 7;
		if (pv < 85) return 8;
		if (pv < 95) return 9;
		if (pv < 105) return 10;
		
		return 10 + ((pv - 105) / 10);
	}

	private int getHigherSkillIncreaseValue(int pv)
	{
		if (pv < 8) return 1;
		if (pv < 13) return 2;
		if (pv < 18) return 3;
		if (pv < 23) return 4;
		if (pv < 28) return 5;
		if (pv < 33) return 6;
		if (pv < 38) return 7;
		if (pv < 43) return 8;
		if (pv < 48) return 9;
		if (pv < 53) return 10;
		
		return 10 + ((pv - 53) / 5);
	}

	private int getAlteredPV(int pv, int skill)
	{
		if (skill < 4)
		{
			int diff = 4 - skill;
			return pv + (diff * getHigherSkillIncreaseValue(pv));
		}
		else if (skill > 4)
		{		
			int diff = skill - 4;
			return pv - (diff * getLowerSkillDecreaseValue(pv));
		}
		return pv;
	}
	
	public String getPVList(int pv)
	{
		int[] pvs = new int[8];
		for (int i = 0; i < 8; i++)
			pvs[i] = getAlteredPV(pv, i);
		
		return Arrays.toString(pvs);
	}
	
	private static String cellStart = "<td";
	private void getSummaryPointsValues(UnitSummary summary) throws Exception
	{
		String name = summary.getName().replace(" ", "+");
		name = name.replace("<", "&lt;");
		name = name.replace(">", "&gt;");		
		if (name.indexOf("“") > -1)
			name = name.substring(0, name.indexOf("“"));

		String url = _BaseUrl + "/Unit/Filter?Name=" + name;
		String content = WebFile.getWebPageContentAsString(url, "", 0);

		String searchStart = "<td><a href=\"/Unit/Details/" + Integer.toString(summary.getID());
		int startIndex = content.indexOf(searchStart);
		for (int i = 0; i < 4; i++)
			startIndex = content.indexOf(cellStart, startIndex + 1);
		
		startIndex = content.indexOf(">", startIndex + 1);
		int endIndex = content.indexOf("<", startIndex);
		String subStr = content.substring(startIndex + 1, endIndex).replace(",", "").replace("\n", "");
		if (!subStr.isEmpty())
		{
			int pv = Integer.parseInt(subStr);
			
			for (int i = 0; i < 8; i++)
				summary.setPV(getAlteredPV(pv, i), i);
		}
	}
	
	
	private static String nameStart = "<h2>";
	private static String nameEnd = "</h2>";
	private static String tagStart = "<dd>";
	private static String tagEnd = "</dd>";
	private UnitSummary loadUnitSummary(int id)
	{
		UnitSummary summary = new UnitSummary();
		summary.setID(id);
		try
		{
		
			String url = _BaseUrl + "/Unit/Details/" + Integer.toString(id);
			String content = WebFile.getWebPageContentAsString(url, "", 0);
	
			int startIndex = content.indexOf(nameStart);
			int endIndex = content.indexOf(nameEnd, startIndex);
			String name = content.substring(startIndex + nameStart.length(), endIndex).trim();
			name = URLDecoder.decode(name, "utf-8");
			name = name.replace("&quot;", "\"");
			name = name.replace("&#39;", "'");
			name = name.replace("&#39;", "'");
			name = name.replace("&lt;", "<");
			name = name.replace("&gt;", ">");
			name = name.replace("G&#249;", "gù");
			summary.setName(name);
			
			startIndex = content.indexOf(tagStart, endIndex);
			endIndex = content.indexOf(tagEnd, startIndex);
			summary.setWeight(Integer.parseInt(content.substring(startIndex + tagStart.length(), endIndex).replace(",", "").replace("NA", "0")));
			
			startIndex = content.indexOf(tagStart, endIndex);
			endIndex = content.indexOf(tagEnd, startIndex);
			summary.setBV(Integer.parseInt(content.substring(startIndex + tagStart.length(), endIndex).replace(",", "").replace("NA", "0")));
			
			startIndex = content.indexOf(tagStart, endIndex);
			endIndex = content.indexOf(tagEnd, startIndex);
			summary.setCost(Long.parseLong(content.substring(startIndex + tagStart.length(), endIndex).replace(",", "").replace("NA", "0")));
	
			startIndex = content.indexOf(tagStart, endIndex);
			endIndex = content.indexOf(tagEnd, startIndex);
			summary.setRulesLevel(content.substring(startIndex + tagStart.length(), endIndex));
	
			startIndex = content.indexOf(tagStart, endIndex);
			endIndex = content.indexOf(tagEnd, startIndex);
			summary.setTechnology(content.substring(startIndex + tagStart.length(), endIndex));
	
			startIndex = content.indexOf(tagStart, endIndex);
			endIndex = content.indexOf(tagEnd, startIndex);
			summary.setType(content.substring(startIndex + tagStart.length(), endIndex));
	
			startIndex = content.indexOf(tagStart, endIndex);
			endIndex = content.indexOf(tagEnd, startIndex);
			summary.setRole(content.substring(startIndex + tagStart.length(), endIndex));
	
			startIndex = content.indexOf(tagStart, endIndex);
			endIndex = content.indexOf(tagEnd, startIndex);
			summary.setIntro(content.substring(startIndex + tagStart.length(), endIndex));
			
			getSummaryPointsValues(summary);

			return summary;
		}
		catch (Exception ex)
		{
			System.out.println("Failed to load: " + summary.toString());
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}
		return null;
	}
	
	public void buildFactionEraUnitLinks()
	{
		try
		{
			org.jdom.Document doc = new org.jdom.Document();
	
			org.jdom.Element factionsNode = new org.jdom.Element("Factions");
			doc.addContent(factionsNode);
	
			for (Faction faction : getFactions())
			{
				System.out.println(faction.getName());
	
				org.jdom.Element factionElement = new org.jdom.Element("Faction");
				factionElement.setAttribute("ID",Integer.toString(faction.getID()));
				factionsNode.addContent(factionElement);
	
				for (int era : faction.getEras())
				{
					System.out.print(era + ",");
					
					org.jdom.Element eraElement = new org.jdom.Element("Era");
					eraElement.setAttribute("ID",Integer.toString(era));
					factionElement.addContent(eraElement);
					
					ArrayList<Integer> units = scrapeFactionEraUnitLinks(faction.getID(), era);
					org.jdom.Element unitsElement = new org.jdom.Element("Units");
					unitsElement.setText(units.toString().replace("[", "").replace("]", ""));
					eraElement.addContent(unitsElement);
				}
				System.out.println();
			}
	
			String fileName = PropertyUtil.getStringProperty("DataPath", "data") + "/FactionEraUnitLinks.xml";
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			out.output(doc, new FileOutputStream(fileName));
		}
		catch (Exception ex)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}
	
	public void scrapeUnitSummaries()
	{
        for (Faction faction : getFactions())
        {
        	System.out.println(faction);
        	
        	for (Era era : getEras())
        	{
        		if (!_FactionEraUnitLinks.get(faction.getID()).containsKey(era.getID()))
        			continue;
        			
	        	System.out.println("    " + era);
        		
	        	for (int unitId : _FactionEraUnitLinks.get(faction.getID()).get(era.getID()))
	        	{
	    			getUnitSummary(unitId);
	        	}
        	}
        }
		saveUnitSummaries();
	}
	
	public List<String> getUnitTypes()
	{
		ArrayList<String> unitTypes = new ArrayList<String>();
		
		for (UnitSummary summary : _UnitSummaries.values())
		{
			if (!unitTypes.contains(summary.getType()))
				unitTypes.add(summary.getType());
		}
		
		return unitTypes;
	}
	
	public static void main(String[] args) 
	{
		try
		{
	        PropertyUtil.loadSystemProperties("bt/system.properties");

	        UnitManager asum = new UnitManager();
			
	        List<String> unitTypes = asum.getUnitTypes();
	        Collections.sort(unitTypes, Collator.getInstance());
	        for (String unitType: unitTypes)
	        	System.out.println(unitType);
			
//			AlphaStrikeUnitSummary summary = asum.getUnitSummary(5783);
//			System.out.println(summary);	 			
		}
		catch (Exception ex)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}
	
}
