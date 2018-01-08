package bt.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import bt.elements.AlphaStrikeFaction;
import bt.elements.AlphaStrikeUnitSummary;
import bt.elements.Era;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;
import bt.util.WebFile;

public class AlphaStrikeUnitManager 
{
	private static String _BaseUrl = "http://www.masterunitlist.info";

	private ArrayList<Era> _Eras = new ArrayList<Era>();
	private ArrayList<AlphaStrikeFaction> _Factions = new ArrayList<AlphaStrikeFaction>();
	private HashMap<Integer,HashMap<Integer, ArrayList<Integer>>> _FactionEraUnitLinks = new HashMap<Integer,HashMap<Integer, ArrayList<Integer>>>();
	private HashMap<Integer, AlphaStrikeUnitSummary> _UnitSummaries = new HashMap<Integer, AlphaStrikeUnitSummary>();

	
	public AlphaStrikeUnitManager()
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
	
	public List<AlphaStrikeFaction> getFactions()
	{
		return _Factions;
	}
	
	public List<Integer> getFactionEraUnits(AlphaStrikeFaction faction, Era era)
	{
		return _FactionEraUnitLinks.get(faction.getID()).get(new Integer(era.getID()));
	}
	
	public AlphaStrikeUnitSummary getUnitSummary(int id)
	{
		if (!_UnitSummaries.containsKey(id))
		{
			AlphaStrikeUnitSummary us = loadUnitSummary(id);
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
            	AlphaStrikeFaction faction = new AlphaStrikeFaction();
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
	            	AlphaStrikeUnitSummary summary = new AlphaStrikeUnitSummary();
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
	            	_UnitSummaries.put(summary.getID(), summary);            	
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
	
			for (AlphaStrikeUnitSummary summary : _UnitSummaries.values())
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
                	
                    List<Element> unitElements = eraElement.getChildren("Unit");
                    for (Element unitElement: unitElements)
                    {
                    	Integer unitID = Integer.parseInt(unitElement.getAttributeValue("ID"));
                    	if (!units.contains(unitID))
                    		units.add(unitID);
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
		String content = WebFile.getWebPageContentAsString(url, "", 0);
		
		for (String tabDiv : tabDivNames)
		{
			int startIndex = content.indexOf(tabDiv);
			if (startIndex == -1)
				continue;
			int endIndex = content.indexOf(divTagString, startIndex);
			
			String chunk = content.substring(startIndex, endIndex);
			processUnitChunks(chunk, units);
				
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
	
	private static String cellStart = "<td";
	private void getSummaryPointsValues(AlphaStrikeUnitSummary summary) throws Exception
	{
		String url = _BaseUrl + "/Unit/Filter?Name=" + URLEncoder.encode(summary.getName(),"utf-8");
		String content = WebFile.getWebPageContentAsString(url, "", 0);

		String searchStart = "<td><a href=\"/Unit/Details/" + Integer.toString(summary.getID());
		
		int startIndex = content.indexOf(searchStart);
		for (int i = 0; i < 4; i++)
			startIndex = content.indexOf(cellStart, startIndex + 1);
		
		startIndex = content.indexOf(">", startIndex + 1);
		int endIndex = content.indexOf("<", startIndex);
		String subStr = content.substring(startIndex + 1, endIndex);
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
	private AlphaStrikeUnitSummary loadUnitSummary(int id)
	{
		AlphaStrikeUnitSummary summary = new AlphaStrikeUnitSummary();
		summary.setID(id);
		try
		{
		
			String url = _BaseUrl + "/Unit/Details/" + Integer.toString(id);
			String content = WebFile.getWebPageContentAsString(url, "", 0);
	
			int startIndex = content.indexOf(nameStart);
			int endIndex = content.indexOf(nameEnd, startIndex);
			String name = content.substring(startIndex + nameStart.length(), endIndex).trim();
			summary.setName(URLDecoder.decode(name, "utf-8"));
			
			startIndex = content.indexOf(tagStart, endIndex);
			endIndex = content.indexOf(tagEnd, startIndex);
			summary.setWeight(Integer.parseInt(content.substring(startIndex + tagStart.length(), endIndex).replace(",", "")));
			
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
	
			for (AlphaStrikeFaction faction : getFactions())
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
					for (Integer unit: units)
					{
						org.jdom.Element unitElement = new org.jdom.Element("Unit");
						unitElement.setAttribute("ID",unit.toString());
						eraElement.addContent(unitElement);					
					}
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
	
	public static void main(String[] args) 
	{
		try
		{
	        PropertyUtil.loadSystemProperties("bt/system.properties");

	        AlphaStrikeUnitManager asum = new AlphaStrikeUnitManager();

	        for (AlphaStrikeFaction faction : asum.getFactions())
	        {
	        	System.out.println(faction);
	        	
	        	for (Era era : asum.getEras())
	        	{
	        		if (!asum._FactionEraUnitLinks.get(faction.getID()).containsKey(era.getID()))
	        			continue;
	        			
		        	System.out.println(era);
	        		
		        	for (int unitId : asum._FactionEraUnitLinks.get(faction.getID()).get(era.getID()))
		        	{
		    			AlphaStrikeUnitSummary summary = asum.getUnitSummary(unitId);
		    			if (summary != null)
		    				System.out.println(summary);
		        	}
					asum.saveUnitSummaries();
	        	}
	        }
		}
		catch (Exception ex)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}
	
}
