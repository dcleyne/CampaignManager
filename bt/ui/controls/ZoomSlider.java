package bt.ui.controls;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bt.ui.renderers.BoardRenderer;
import bt.ui.renderers.BoardRenderer.ZoomLevel;

public class ZoomSlider extends JSlider implements ChangeListener
{
   private static final long serialVersionUID = 1L;
   public static double SCALE = 1;
   private BoardRenderer _Renderer;

   public ZoomSlider(BoardRenderer renderer) 
   {
      super(JSlider.HORIZONTAL, 0, 8, (renderer != null ? renderer.getZoomLevel().ordinal() : ZoomLevel.NORMAL.ordinal()));
      _Renderer = renderer;
      setMajorTickSpacing(1);
      setMinorTickSpacing(1);
      setPaintTicks(true);
      addChangeListener(this);
      
      setState();
   }
   
   public void setBoardRenderer(BoardRenderer renderer)
   {
	   _Renderer = renderer;
	   setState();
	   setLevel(getValue());
   }

   public void stateChanged(ChangeEvent arg0) 
   {
      setLevel(((JSlider) arg0.getSource()).getValue());
   }
   
   private void setLevel(int value)
   {
	   if (_Renderer == null)
		   return;
	   
      ZoomLevel level = ZoomLevel.values()[value];
      _Renderer.setZoomLevel(level);	   
   }
   
   private void setState()
   {
	   setEnabled(_Renderer != null);
   }
}

