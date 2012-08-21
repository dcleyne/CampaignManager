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
package bt.common.net;

public class Envelope
{
    public static final long PFCOMPRESSED = 1;
    
    
    private long m_Flags;
    private byte[] m_Data;
    
    public Envelope(long Flags, byte[] Data)
    {
        m_Flags = Flags;
        m_Data = Data;
    }
        
    public long getFlags()
    { return m_Flags; }
    
    public byte[] getData()
    { return m_Data; }
    
    public boolean flagSet(long Flag)
    {
        return (m_Flags & Flag) != 0;
    }
}
