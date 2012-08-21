/**
 * Created on 11/11/2005
 * <p>Title: Miradan Phedd</p>
 *
 * <p>Description: Library for all Miradan related work</p>
 *
 * <p>Copyright: Copyright (c) 2004-2005</p>
 *
 * <p>Company: Miradan Phedd</p>
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
package bt.common.command;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import java.io.*;
import java.util.Vector;

public class CommandPacket implements Serializable
{
    static final long serialVersionUID = 1;
    
    
    private Command m_Type;
    private Object[] m_Data;
    
    public CommandPacket(Command type)
    {
        m_Type = type;
        m_Data = null;
    }

    public CommandPacket(Command type, Object[] data)
    {
        m_Type = type;
        m_Data = data;
    }

    public CommandPacket(Command type, Object data)
    {
        m_Type = type;
        m_Data = new Object[1];
        m_Data[0] = data;
    }
    
    public final boolean hasData()
    {
    	if (m_Data == null) return false;
    	if (m_Data.length == 0) return false;
    	return true; 
    }
    
    public final Command getType()
    { return m_Type; }
    
    public final Object[] getData()
    {
        return m_Data;
    }
    
    public final Object getData(int Index) throws ArrayIndexOutOfBoundsException
    {
        return m_Data[Index];
    }
    
    public void addObject(Object o)
    {
        Vector<Object> Data = new Vector<Object>();
        if (m_Data != null)
        {
            for (int i = 0; i < m_Data.length; i++)
                Data.add(m_Data[i]);
        }
        
        Data.add(o);
        m_Data = new Object[Data.size()];
        Data.copyInto(m_Data);
    }
    
    public String toString()
    {
        return m_Type.toString();
    }

}
