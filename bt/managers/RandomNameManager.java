package bt.managers;

import java.io.File;


import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import bt.elements.unit.RandomName;
import bt.util.Dice;
import bt.util.PropertyUtil;
import bt.util.RandomNameParser;
import bt.util.WebFile;

public class RandomNameManager 
{
	private static final String RANDOM_NAME = "RandomName";

	private static final String NAME = "Name";

	private static final String SURNAME = "Surname";

	private static final String GIVEN_NAME = "GivenName";

	private static RandomNameManager theInstance;
	
	private final String _FileName = PropertyUtil.getStringProperty("DataPath", "data") + "/RandomNames.xml";

	private ArrayList<String> _Surnames = new ArrayList<String>();
	private ArrayList<String> _GivenNames = new ArrayList<String>();
	
	private ArrayList<RandomName> _ServedNames = new ArrayList<RandomName>();
	
	private String _RandomNameURL = PropertyUtil.getStringProperty("RandomNameSource","");
	private String _RandomNameParser = PropertyUtil.getStringProperty("RandomNameParser","");
	private int _RandomNameThreshold = PropertyUtil.getIntProperty("RandomNameThreshold",10000);	
	
	private RandomNameManager()
	{
		loadRandomNames();
		seedAvailableRandomNames();
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

	public void resetRandomNames()
	{
		_ServedNames.clear();
	}

	public RandomName getRandomName()
	{
		String givenName = _GivenNames.get(Dice.random(_GivenNames.size()) - 1);
		String surname = _Surnames.get(Dice.random(_Surnames.size()) - 1);
		
		RandomName name = new RandomName(givenName, surname);
		while (givenName.equalsIgnoreCase(surname) || _ServedNames.contains(name))
		{
			givenName = _GivenNames.get(Dice.random(_GivenNames.size()) - 1);
			surname = _Surnames.get(Dice.random(_Surnames.size()) - 1);
			
			name = new RandomName(givenName, surname);
		}
		_ServedNames.add(name);
		
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

				Iterator<?> iter = rootElement.getChildren(RANDOM_NAME).iterator();
				while (iter.hasNext())
				{
					org.jdom.Element nameElement = (org.jdom.Element) iter.next();
					String surname = nameElement.getAttributeValue(SURNAME);
					if (!_Surnames.contains(surname))
						_Surnames.add(surname);
					
					String givenName = nameElement.getAttributeValue(NAME);
					if (!_GivenNames.contains(givenName))
					_GivenNames.add(givenName);
				}

				iter = rootElement.getChildren(GIVEN_NAME).iterator();
				while (iter.hasNext())
				{
					org.jdom.Element givenNameElement = (org.jdom.Element) iter.next();
					String givenName = givenNameElement.getValue();
					if (!_GivenNames.contains(givenName))
						_GivenNames.add(givenName);
				}
				iter = rootElement.getChildren(SURNAME).iterator();
				while (iter.hasNext())
				{
					org.jdom.Element surameElement = (org.jdom.Element) iter.next();
					String surname = surameElement.getValue();
					if (!_Surnames.contains(surname))
						_Surnames.add(surname);
				}
			}
		}
		catch (Exception ex)
		{
			System.out.println("Failed to load Random Names");
			ex.printStackTrace();
			System.exit(1);
		}
	}

	private void saveRandomNames() throws Exception
	{
		org.jdom.Document doc = new org.jdom.Document();

		org.jdom.Element rootElement = new org.jdom.Element("RandomNames");
		doc.addContent(rootElement);

		for (String name : _GivenNames)
		{
			org.jdom.Element nameElement = new org.jdom.Element(GIVEN_NAME);
			nameElement.addContent(name);
			rootElement.addContent(nameElement);
		}
		for (String name : _Surnames)
		{
			org.jdom.Element nameElement = new org.jdom.Element(SURNAME);
			nameElement.addContent(name);
			rootElement.addContent(nameElement);
		}
		
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		out.output(doc, new FileOutputStream(_FileName));
	}

	public String dumpRandomNames()
	{
		StringBuilder result = new StringBuilder();
		
		for (int i = 0; i < _RandomNameThreshold; i++)
		{
			RandomName rn = getRandomName();
			result.append(rn.getName());
			result.append(" ");
			result.append(rn.getSurname());
			result.append("\n");
		}		
		
		return result.toString();
	}
	
	private void seedAvailableRandomNames()
	{
		try
		{
			while (_Surnames.size() < _RandomNameThreshold)
			{
				System.out.println("Random name pool below threshold. Collecting " + (_RandomNameThreshold - _Surnames.size()) + " more");
	            Class<?> imClass = Class.forName(_RandomNameParser, true, ClassLoader.getSystemClassLoader());
	            Class<?> superClass = imClass.getSuperclass();
	            if (!superClass.getName().equals("bt.util.RandomNameParser"))
	            {
	                throw new Exception("Class does not extend bt.common.util.RandomNameParser : " + _RandomNameParser);
	            }

	            Class<?>[] argDefs = {};
	            Object[] args = new Object[0];
	            java.lang.reflect.Constructor<?> parserConstructor = imClass.getConstructor(argDefs);
	            RandomNameParser parser =  (RandomNameParser)parserConstructor.newInstance(args);
	            
				System.out.println("Grabbing random names from : " + _RandomNameURL);
	        	String html = WebFile.getWebPageContentAsString(_RandomNameURL, "", 0);
	            ArrayList<RandomName> randomNames = parser.parseRandomNames(html);
	            for (RandomName rn : randomNames)
	            {
	            	if (!_Surnames.contains(rn.getSurname()))
	            		_Surnames.add(rn.getSurname());

	            	if (!_GivenNames.contains(rn.getName()))
	            		_GivenNames.add(rn.getName());
	            }
	            Thread.sleep(500 + Dice.random(500));
				
				saveRandomNames();
			}
			System.out.println("Random name pool seeded.");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
}
