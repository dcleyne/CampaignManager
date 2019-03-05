package test;

import java.awt.BorderLayout;


import javax.swing.JFrame;
import bt.ui.panels.GenerateNewUnitPanel;
import bt.util.PropertyUtil;

public class TestUnitGenerationPanel
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			PropertyUtil.loadSystemProperties("bt/system.properties");
	
	        //1. Create the frame.
			JFrame frame = new JFrame("Generate New Unit");
	
			//2. Optional: What happens when the frame closes?
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
			//3. Create components and put them in the frame.
			//...create emptyLabel...
			frame.getContentPane().add(new GenerateNewUnitPanel(), BorderLayout.CENTER);
	
			//4. Size the frame.
			frame.pack();
			frame.setSize(900, 720);
	
			//5. Show it.
			frame.setVisible(true);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
