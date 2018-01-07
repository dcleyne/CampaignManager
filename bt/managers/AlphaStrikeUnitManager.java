package bt.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import bt.elements.AlphaStrikeFaction;
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

	
	public AlphaStrikeUnitManager()
	{
		loadEras();
		loadFactions();
		loadFactionEraUnits();
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

	public static void getUnitStatsImages(String outputPath, int unitId) throws Exception
	{
		getUnitStatsImage(outputPath, unitId, 4);
		getUnitStatsImage(outputPath, unitId, 3);
		getUnitStatsImage(outputPath, unitId, 2);
		getUnitStatsImage(outputPath, unitId, 5);
		getUnitStatsImage(outputPath, unitId, 1);
		getUnitStatsImage(outputPath, unitId, 6);
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
			AlphaStrikeUnitManager asum = new AlphaStrikeUnitManager();
			
			List<Integer> units = asum.getFactionEraUnits(asum.getFactions().get(0), asum.getEras().get(1));
			for (Integer i : units)
			{
				System.out.println(i);
			}
		}
		catch (Exception ex)
		{
			System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}
	
}
