package bt.elements;

import java.io.Serializable;

public abstract class OrderResult implements Serializable 
{
	private static final long serialVersionUID = 1;

	
	public abstract void saveToElement(org.jdom.Element e);
	public abstract void loadFromElement(org.jdom.Element e);

}
