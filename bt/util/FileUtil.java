package bt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
    private static String s_NumberPattern = " (%d)";
	private String _LockPath = "";
	
    public static void checkAndCreatePath(String path) throws Exception
    {
    	File file = new File(path);
    	if (!file.exists())
    	{
    		if (!file.mkdirs())
    		{
    			throw new Exception("Could not create path : " + path);
    		}
    	}
    }
    
	public boolean lock(String dataPath, String process) throws Exception
	{
		if (dataPath.isEmpty())
			throw new RuntimeException("Invalid Data path for Lock file");
		
		FileUtil.checkAndCreatePath(dataPath);
		
		File f = new File(dataPath + "/Lock");
		if (f.exists())
			return false;
		
		_LockPath = f.getAbsolutePath();
		createLockFile(process);
		
		return true;
	}

	public boolean unlock() throws Exception
	{
		if (_LockPath.isEmpty())
			throw new RuntimeException("No Lock to remove");

		File f = new File(_LockPath);
		f.delete();
		return true;
	}
	
	private void createLockFile(String processName) throws IOException
	{
		PrintWriter out = new PrintWriter(new FileWriter(_LockPath));
		out.println(processName);
		out.close();
	}

    private static final String getExtension(final String filename) 
    {
    	  if (filename == null) return null;
    	  final String afterLastSlash = filename.substring(filename.lastIndexOf('/') + 1);
    	  final int afterLastBackslash = afterLastSlash.lastIndexOf('\\') + 1;
    	  final int dotIndex = afterLastSlash.indexOf('.', afterLastBackslash);
    	  return (dotIndex == -1) ? "" : afterLastSlash.substring(dotIndex + 1);
    }
    
    public static String nextAvailableFilename(String path) throws Exception
    {
        // Short-cut if already available
        File f = new File(path);
        if (!f.exists())
            return path;

        // If path has extension then insert the number pattern just before the extension and return next filename
        String extension = getExtension(path);
        if (!extension.isEmpty())
            return getNextFilename(StringUtil.insertString(path, path.lastIndexOf(extension) - 1, s_NumberPattern));

        // Otherwise just append the pattern to the path and return next filename
        return getNextFilename(path + s_NumberPattern);
    }

    private static String getNextFilename(String pattern) throws Exception
    {
        String tmp = String.format(pattern, 1);
        if (tmp == pattern)
            throw new Exception("The pattern must include an index place-holder");
        
        File f = new File(tmp);
        if (!f.exists())
            return tmp; // short-circuit if no matches

        int min = 1, max = 2; // min is inclusive, max is exclusive/untested

        f = new File(String.format(pattern, max));
        while (f.exists())
        {
            min = max;
            max *= 2;
            f = new File(String.format(pattern, max));
        }

        while (max != min + 1)
        {
            int pivot = (max + min) / 2;
            f = new File(String.format(pattern, pivot));
            if (f.exists())
                min = pivot;
            else
                max = pivot;
        }

        return String.format(pattern, max);
    }
    
    public static void deleteFolderAndContents(String folderName)
    {
    	deleteFolderAndContents(new File(folderName));
    }
    
    public static void deleteFolderAndContents(File folder)
    {
    	final File[] files = folder.listFiles();
    	if (files != null)
    	{
	    	for (File f: files) 
	    		f.delete();
	    	folder.delete();
    	}
    }
    
    public static void deleteFile(String fileName)
    {
    	deleteFile(new File(fileName));
    }
    public static void deleteFile(File file)
    {
		file.delete();
    }
    
    public static String getParentPath(String filename)
    {
    	return new File(filename).getParent();
    }
    
    public static InputStream openDataResource(String path, String filename) throws Exception
    {
    	String fullpath = path.endsWith("/") ? path + filename : path + "/" + filename;
    	return openDataResource(fullpath);
    }
    
    public static InputStream openDataResource(String filename) throws Exception
    {
    	filename = filename.replace('\\', '/');
    	File f = new File(filename);
    	if (f.exists())
    		return new FileInputStream(f);
    	
    	return Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
    }
    
    public static List<String> listDataResources(String path, String filter) throws Exception
    {
    	ArrayList<String> files = new ArrayList<String>();
    	
		File dir = new File(path);
		if (dir.exists())
		{
			FilenameFilter Filter = new FilenameFilter() {
				public boolean accept(File dir, String Name)
				{
					return Name.endsWith(filter);
				}
			};
			for (File f : dir.listFiles(Filter))
				files.add(path + "/" + f.getName());
			
			return files;
		}

		URI uri = new URI("jar:file", FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath(),  null);
		if (uri != null)
		{
			Map<String, String> env = new HashMap<>();
			env.put("create", "true");
	
			FileSystem fileSystem = FileSystems.newFileSystem(uri, env);
	        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(fileSystem.getPath(path),"*" + filter)) 
	        {
	            for (Path p : directoryStream) 
	            {
	            	String pathName = p.toString();
	            	if (pathName.startsWith("/"))
	            		pathName = pathName.substring(1);
	                files.add(pathName);
	            }
	        	directoryStream.close();
	        } 
	        catch (IOException ex) 
	        {
		    	System.out.println("Failed loading directory stream");
		    	System.out.println(ExceptionUtil.getExceptionStackTrace(ex));
	        }
	        finally
	        {	
	        	fileSystem.close();
	        }
		}
		
    	return files;
    }
}
