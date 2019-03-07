package bt.util;

import java.io.Serializable;

public interface PersistToXML extends Serializable
{
    public org.jdom.Element saveToElement();
    public void loadFromElement(org.jdom.Element e) throws Exception;
}
