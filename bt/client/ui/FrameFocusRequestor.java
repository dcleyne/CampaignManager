package bt.client.ui;
import javax.swing.JInternalFrame;

public class FrameFocusRequestor implements Runnable
{
    JInternalFrame m_InternalFrame;

    public FrameFocusRequestor(JInternalFrame jif)
    {
        m_InternalFrame = jif;
    }

    public void run()
    {
        m_InternalFrame.toFront();
        m_InternalFrame.getContentPane().requestFocus();
    }
}
