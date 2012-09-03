package bt.ui.filters;

import java.awt.image.RGBImageFilter;

public class TransparentColorFilter extends RGBImageFilter 
{
    private int keyColor;

    public TransparentColorFilter(int keyColor) 
    {
        this.keyColor = keyColor | 0xFF000000;
    }

    public int filterRGB(int x, int y, int RGB) 
    {
        if ((RGB | 0xFF000000) == keyColor) 
        {
                // Mark the alpha bits as zero - transparent
                return 0xFFFFFF & RGB;
        } else {
                // nothing to do
                return RGB;
        }
    }
}
