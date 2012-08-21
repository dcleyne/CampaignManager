package bt.test;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

import bt.common.util.PropertyUtil;
import bt.servermanager.ui.forms.GenerateNewUnitPanel;

public class TestUnitGenerationPanel
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
	        PropertyConfigurator.configure(Loader.getResource("bt/server/log4j.properties"));
			PropertyUtil.loadSystemProperties("bt/common/system.properties");
	        PropertyUtil.loadSystemProperties("bt/client/client.properties");
	
	        //1. Create the frame.
			JFrame frame = new JFrame("Generate New Unit");
	
			//2. Optional: What happens when the frame closes?
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
			//3. Create components and put them in the frame.
			//...create emptyLabel...
			frame.getContentPane().add(new GenerateNewUnitPanel(), BorderLayout.CENTER);
	
			//4. Size the frame.
			frame.pack();
			frame.setSize(650, 600);
	
			//5. Show it.
			frame.setVisible(true);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
