package bt.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import bt.elements.collection.MiniatureCollection;
import bt.elements.collection.MiniatureCollectionItem;
import bt.util.PropertyUtil;

public class MiniatureCollectionManager
{
	private ArrayList<MiniatureCollection> _Collections = new ArrayList<MiniatureCollection>();
	
    private static MiniatureCollectionManager theInstance = new MiniatureCollectionManager();

    private String _DataPath = PropertyUtil.getStringProperty("DataPath", "data");

    private MiniatureCollectionManager()
    {
    	loadCollections();
    }

    public static MiniatureCollectionManager getInstance()
    {
        return theInstance;
    }

	private void loadCollections()
	{
    	File dataPath = new File(_DataPath, "MiniatureCollections.xml");
    	try
    	{
            SAXBuilder b = new SAXBuilder();
            Document doc = b.build(dataPath);

            Element rootElement = doc.getRootElement();
            
        	Iterator<?> iter = rootElement.getChildren("Collection").iterator();
        	while (iter.hasNext())
            {
        		org.jdom.Element collectionElement = (org.jdom.Element)iter.next();
        		
        		MiniatureCollection mc = new MiniatureCollection();
        		mc.setName(collectionElement.getAttributeValue("Name"));
        		mc.setDescription(collectionElement.getAttributeValue("Description"));
        		
            	Iterator<?> iter2 = collectionElement.getChildren("Miniature").iterator();
            	while (iter2.hasNext())
                {
            		org.jdom.Element collectionItemElement = (org.jdom.Element)iter2.next();
            		
            		MiniatureCollectionItem mci = new MiniatureCollectionItem();
            		mci.setName(collectionItemElement.getAttributeValue("Name"));
            		mci.setNotes(collectionItemElement.getAttributeValue("Notes"));
            		mc.addItem(mci);
                }
            	_Collections.add(mc);
            }
            
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    	}
	}
	
	public ArrayList<String> getCollectionNames()
	{
		ArrayList<String> names = new ArrayList<String>();
		for (MiniatureCollection mc : _Collections)
		{
			names.add(mc.getName());
		}
		return names;
	}
}
