/*
 * MegaMek - Copyright (C) 2000-2002 Ben Mazur (bmazur@sev.org)
 * 
 *  This program is free software; you can redistribute it and/or modify it 
 *  under the terms of the GNU General Public License as published by the Free 
 *  Software Foundation; either version 2 of the License, or (at your option) 
 *  any later version.
 * 
 *  This program is distributed in the hope that it will be useful, but 
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 *  for more details.
 */

package bt.ui;

import java.awt.image.RGBImageFilter;

/**
 * Filters the pixels in the image by tinting a black and white image to a
 * certain color.
 */
public class TintFilter extends RGBImageFilter 
{
    private int colour;
    private int tintColour;

    public TintFilter(int colour, int tintColour) 
    {
        this.colour = colour;
        this.tintColour = tintColour;

        canFilterIndexColorModel = true;
    }

    public int filterRGB(int x, int y, int RGB) {
        final int alpha = RGB & 0xff000000;
        if (alpha != 0xff000000) {
            return RGB;
        }
        
        if (RGB == this.colour)
            return tintColour;
        else
            return RGB;
    }
}
