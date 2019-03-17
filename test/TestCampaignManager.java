package test;

import java.util.ArrayList;

import bt.managers.CampaignManager;
import bt.util.ExceptionUtil;
import bt.util.PropertyUtil;

public class TestCampaignManager
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			PropertyUtil.loadSystemProperties("bt/system.properties");

			ArrayList<String> campaigns = CampaignManager.INSTANCE.getCampaignList();			
			for (String campaignName : campaigns)
				System.out.println(campaignName);

			System.out.println("\n");

		} catch (Exception ex)
		{
			System.out.print(ExceptionUtil.getExceptionStackTrace(ex));
		}
	}

}
