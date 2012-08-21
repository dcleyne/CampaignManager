package bt.common.util;

import java.io.File;
import java.io.FilenameFilter;

public class ExtensionFileFilter implements FilenameFilter
{
	private String _Extension;
	
	public ExtensionFileFilter(String extension)
	{
		_Extension = extension;
	}

	@Override
	public boolean accept(File dir, String filename)
	{
	    boolean fileOK = true;

	    if (_Extension != null) {
	      fileOK &= filename.endsWith('.' + _Extension);
	    }
	    return fileOK;
	}

}
