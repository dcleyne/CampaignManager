/*******************************************************************************
 * Title: Legatus
 * 
 * Copyright Daniel Cleyne (c) 2004-2013
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at our option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * @author Daniel Cleyne
 ******************************************************************************/

package bt.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

public enum ImageUtil
{
	INSTANCE;
	
	private String _Path;

	private ImageUtil()
	{
		_Path = PropertyUtil.getStringProperty("DataPath", "data/") + "images/";
	}

	public static BufferedImage loadImage(String imageName)
	{
		try
		{
			return INSTANCE.loadImageFromPath(imageName);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedImage loadImage(String path, String imageName)
	{
		try
		{
			return INSTANCE.loadImageFromPath(path, imageName);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<BufferedImage> loadImages(String[] imageNames) throws Exception
	{
		return INSTANCE.loadImagesFromPath(new ArrayList<String>(Arrays.asList(imageNames)));
	}

	public static ArrayList<BufferedImage> loadImages(ArrayList<String> imageNames) throws Exception
	{
		return INSTANCE.loadImagesFromPath(imageNames);
	}

	public static ArrayList<BufferedImage> loadImages(String path, ArrayList<String> imageNames) throws Exception
	{
		return INSTANCE.loadImagesFromPath(imageNames);
	}

	private BufferedImage loadImageFromPath(String imageName) throws Exception
	{
		return loadImageFromPath(_Path, imageName);
	}

	private BufferedImage loadImageFromPath(String path, String imageName) throws Exception
	{
		try
		{
			return ImageIO.read(FileUtil.openDataResource(path, imageName));
		} catch (IOException ex)
		{
			throw new Exception(ex);
		}
	}

	private ArrayList<BufferedImage> loadImagesFromPath(ArrayList<String> imageNames) throws Exception
	{
		return loadImagesFromPath(_Path, imageNames);
	}

	private ArrayList<BufferedImage> loadImagesFromPath(String path, ArrayList<String> imageNames) throws Exception
	{
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		for (String imageName : imageNames)
		{
			images.add(loadImageFromPath(path, imageName));
		}

		return images;
	}
}
