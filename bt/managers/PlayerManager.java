package bt.managers;

import java.io.File;


import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import bt.elements.unit.Player;
import bt.elements.unit.PlayerSummary;
import bt.util.PropertyUtil;

public class PlayerManager 
{
	private static PlayerManager theInstance;

	private HashMap<String,Player> _Players = new HashMap<String,Player>();
	
	private PlayerManager()
	{
		loadPlayers();
	}

 	
	public static PlayerManager getInstance()
	{
		if (theInstance == null)
			theInstance = new PlayerManager();
		
		return theInstance;
	}
	
	public static void saveAllPlayers()
	{
		getInstance().savePlayers();
	}
	
	public Player addPlayer(String name, String password, String nickname, String emailAddress)
	{
		Player p = getPlayer(name);
		if (p == null)
		{
			p = new Player();
			p.setName(name);
			p.setPassword(password);
			p.setNickname(nickname);
			p.setEmailAddress(emailAddress);
			
			_Players.put(p.getName(), p);
		}
		return p;
	}
	
	public void removePlayer(Player p)
	{
		_Players.remove(p);
	}
	
	public Player getPlayer(long id)
	{
		return _Players.get(id);
	}
	
	public Player getPlayer(String name)
	{
		for (Player p : _Players.values())
		{
			if (p.getName().equalsIgnoreCase(name))
				return p;
		}
		return null;
	}
	
	public Vector<PlayerSummary> getPlayerSummaries()
	{
		Vector<PlayerSummary> summaries = new Vector<PlayerSummary>();
		
		for (Player p : _Players.values())
		{
			summaries.add(new PlayerSummary(p));
		}
		
		return summaries;
	}
	
	@SuppressWarnings("unchecked")
	private void loadPlayers()
	{
		_Players.clear();
		
        String Path = PropertyUtil.getStringProperty("ExternalDataPath", "data");
        File f = new File(Path + "/Players.xml");
        if (!f.exists())
        	return;
        
        try {            
            SAXBuilder b = new SAXBuilder();
            Document playerDoc = b.build(f);
            Element root = playerDoc.getRootElement();
            List<Element> playerElements = root.getChildren("Player");
            for (Element playerElement: playerElements)
            {
            	Player p = loadPlayer(playerElement);
            	_Players.put(p.getName(),p);            	
            }
    	
        } catch(java.io.IOException ex) {
        	System.out.println("Error Opening Player File!");
            ex.printStackTrace();
        } catch (JDOMException jdex) {
        	System.out.println("Failure Parsing Player File!");
            jdex.printStackTrace();
        } catch (Exception exx) {
        	System.out.println("Failure Loading Player!");
            exx.printStackTrace();
        }
	}
	
	private Player loadPlayer(org.jdom.Element e)
	{
		Player p = new Player();
		p.setName(e.getChildText("Name"));
		p.setPassword(e.getChildText("Password"));
		p.setNickname(e.getChildText("Nickname"));
		p.setEmailAddress(e.getChildText("EmailAddress"));
		p.getUnits().clear();

		if (e.getChild("Units") != null)
		{
			Iterator<?> iter = e.getChild("Units").getChildren("Unit")
					.iterator();
			while (iter.hasNext())
			{
				org.jdom.Element unitElement = (org.jdom.Element) iter.next();
				p.getUnits().add(unitElement.getText());
			}
		}

		return p;
	}

	
	public void savePlayers()
	{
        String Path = PropertyUtil.getStringProperty("ExternalDataPath", "data");        
        File f = new File(Path + "/Players.xml");
        if (f.exists())
        	f.delete();
        
        try {            
            Document planetDoc = new Document();
            
            Element root = new Element("Players"); 
            planetDoc.setRootElement(root);
            
            for (Player p : _Players.values())
            {
            	root.addContent(savePlayer(p));
            }

            XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());            
            out.output(planetDoc, new FileOutputStream(f));
                                      
        } catch (Exception exx) {
        	System.out.println("Failure Saving Players");
            exx.printStackTrace();
        }
	}
	
	private org.jdom.Element savePlayer(Player p)
	{
		org.jdom.Element e = new org.jdom.Element("Player");

		e.addContent(new org.jdom.Element("Name").setText(p.getName()));
		e.addContent(new org.jdom.Element("Password").setText(p.getPassword()));
		e.addContent(new org.jdom.Element("Nickname").setText(p.getNickname()));
		e.addContent(new org.jdom.Element("EmailAddress").setText(p
				.getEmailAddress()));

		org.jdom.Element unitsElement = new org.jdom.Element("Units");
		for (String unitName : p.getUnits())
			unitsElement.addContent(new org.jdom.Element("Unit")
					.setText(unitName));

		e.addContent(unitsElement);

		return e;
	}
}
