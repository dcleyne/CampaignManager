package bt.ui.renderers;

import javax.swing.JPanel;

public abstract class BoardRendererView extends JPanel
{
	private static final long serialVersionUID = 1L;

	public abstract void setBoardRenderer(BoardRenderer renderer);
}
