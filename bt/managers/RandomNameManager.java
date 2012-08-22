package bt.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import bt.elements.unit.RandomName;
import bt.util.PropertyUtil;
import bt.util.RandomNameParser;
import bt.util.WebFile;

public class RandomNameManager 
{
	private static Log log = LogFactory.getLog(RandomNameManager.class);

	private static RandomNameManager theInstance;
	
	private final String _FileName = PropertyUtil.getStringProperty("UniverseDataPath", "bt/data/") + "/RandomNames.xml";
	private Vector<RandomName> _RandomNames = new Vector<RandomName>();
	private Vector<RandomName> _ServedNames = new Vector<RandomName>();
	
	private String _RandomNameURL = PropertyUtil.getStringProperty("RandomNameSource","");
	private String _RandomNameParser = PropertyUtil.getStringProperty("RandomNameParser","");
	private int _RandomNameThreshold = PropertyUtil.getIntProperty("RandomNameThreshold",100);	
	
	private RandomNameManager()
	{
   		log.info("Initialising RandomNameManager");
		
		loadRandomNames();
		checkAvailableRandomNames();
	}
	
	
	public static RandomNameManager getInstance()
	{
		if (theInstance == null)
			theInstance = new RandomNameManager();
		
		return theInstance;
	}
	
	
	public void purgeServedRandomNames() throws Exception
	{
		_ServedNames.clear();
		saveRandomNames();
	}

	public void ResetRandomNames()
	{
		_RandomNames.addAll(_ServedNames);
		_ServedNames.clear();
	}

	public RandomName GetRandomName()
	{
		checkAvailableRandomNames();
		RandomName name = _RandomNames.elementAt(0);
		_ServedNames.add(name);
		_RandomNames.remove(0);
		return name;
	}

	private void loadRandomNames()
	{
		try
		{
			File f = new File(_FileName);
			if (f.exists())
			{
				SAXBuilder b = new SAXBuilder();
				Document doc = b.build(f);
		
				org.jdom.Element rootElement = doc.getRootElement();
		
				Iterator<?> iter = rootElement.getChildren().iterator();
				while (iter.hasNext())
				{
					org.jdom.Element randomNameElement = (org.jdom.Element) iter.next();
					String n = "";
					String sn = "";
					if (randomNameElement.getAttribute("Name") != null)
						n = randomNameElement.getAttributeValue("Name");
					else if (randomNameElement.getChild("Name") != null)
						n = randomNameElement.getChild("Name").getValue();
						
					
					if (randomNameElement.getAttribute("Surname") != null)
						sn = randomNameElement.getAttributeValue("Surname");
					else if (randomNameElement.getChild("Surname") != null)
						sn = randomNameElement.getChild("Surname").getValue();				
					
					_RandomNames.add(new RandomName(n, sn));
				}
			}
		}
		catch (Exception ex)
		{
			log.fatal("Failed to load Random Names",ex);
			System.exit(1);
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

		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		out.output(doc, new FileOutputStream(_FileName));
	}

	public String dumpRandomNames()
	{
		StringBuilder result = new StringBuilder();
		
		for (RandomName rn : _RandomNames)
		{
			result.append(rn.getName());
			result.append(" ");
			result.append(rn.getSurname());
			result.append("\n");
		}		
		
		return result.toString();
	}
	
	private void checkAvailableRandomNames()
	{
		try
		{
			if (_RandomNames.size() < _RandomNameThreshold)
			{
				log.info("Random name pool has dropped below threshold. Collecting more");
	            Class<?> imClass = Class.forName(_RandomNameParser, true, ClassLoader.getSystemClassLoader());
	            Class<?> superClass = imClass.getSuperclass();
	            if (!superClass.getName().equals("bt.common.util.RandomNameParser"))
	            {
	                throw new Exception("Class does not extend bt.common.util.RandomNameParser : " + _RandomNameParser);
	            }

	            Class<?>[] argDefs = {};
	            Object[] args = new Object[0];
	            java.lang.reflect.Constructor<?> parserConstructor = imClass.getConstructor(argDefs);
	            RandomNameParser parser =  (RandomNameParser)parserConstructor.newInstance(args);
	            
				while (_RandomNames.size() < _RandomNameThreshold)
				{
					log.info("Grabbing random names from : " + _RandomNameURL);
		        	String html = WebFile.getWebPageContent(_RandomNameURL, "", 0);
		            _RandomNames.addAll(parser.parseRandomNames(html));			
		        }
				
				log.info("Random name pool topped up.");
				saveRandomNames();
			}
		}
		catch (Exception ex)
		{
			log.fatal("Unable to gather more random names", ex);
		}
		
	}
	
}
