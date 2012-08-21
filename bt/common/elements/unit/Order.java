package bt.common.elements.unit;

import java.io.Serializable;

public abstract class Order implements Serializable 
{	
	private static final long serialVersionUID = 1;

	
	public abstract void saveToElement(org.jdom.Element e);
	public abstract void loadFromElement(org.jdom.Element e);
}
