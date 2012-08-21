package bt.server.universe;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import bt.server.Server;

public class OrderProcessTrigger extends Timer 
{
	public OrderProcessTrigger(final Server server, Date time)
	{
		super("Campaign Manager Order Process Trigger", true);
		super.schedule(new TimerTask()
		{
			public void run()
			{
				server.processOrders();
			}
		}, time);
	}
}
