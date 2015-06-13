package bt.util;

import java.io.File;

public class FileUtil 
{

	public static String getTempFolder()
	{
		String folderName = "";
		try
		{
			File temp = File.createTempFile("temp-file-name", ".tmp"); 
			String absolutePath = temp.getAbsolutePath();
			folderName = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
			temp.delete();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return folderName;
	}
}
