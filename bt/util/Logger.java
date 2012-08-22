package bt.util;

import java.io.*;
import java.util.*;

public class Logger
{
    protected static Logger m_Logger;
    protected static File m_LogFile;

    public static final int LEVEL_VERBOSE = 10;
    public static final int LEVEL_INFORMATION = 8;
    public static final int LEVEL_WARNING = 5;
    public static final int LEVEL_ERROR = 3;
    public static final int LEVEL_CRITICAL = 1;

    protected Logger()
    {
    }

    public static void SetLogFile(String LogFilePath) throws Exception
    {
        if (m_Logger == null)
        {
            m_Logger = new Logger();
        }

        m_LogFile = new File(LogFilePath);
        if (m_LogFile.exists() && !m_LogFile.canWrite())
        {
            m_LogFile = null;
            throw new Exception("Cannot Open Log file for Writing");
        }
        Log(LEVEL_INFORMATION, "-------------------------------------------------------------------------------------------");
    }

    public static void Log(int Level, String Message)
    {
        if (m_LogFile == null)
        {
            System.out.println("No Log File open for writing");
            return;
        }

        try
        {
            FileOutputStream fos = new FileOutputStream(m_LogFile, true);
            Date currentDate = new Date();
            String LogMessage = Integer.toString(Level);
            LogMessage += " : ";
            LogMessage += currentDate.toString();
            LogMessage += " : ";
            LogMessage += Message;
            LogMessage += "\n";

            fos.write(LogMessage.getBytes());
            fos.close();

        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
            System.exit(1);
        }
    }

    protected void finalize()
    {
        Log(LEVEL_INFORMATION, "-------------------------------------------------------------------------------------------");
    }
}
