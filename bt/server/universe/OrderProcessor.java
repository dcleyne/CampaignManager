package bt.server.universe;

import java.util.Collections;
import java.util.Vector;

import bt.common.elements.Event;
import bt.common.elements.unit.Order;
import bt.common.elements.unit.OrderResult;

public class OrderProcessor extends Thread
{
	private Vector<Order> m_Orders;
	private Vector<Event> m_Events;
	private Vector<OrderResult> m_OrderResults = new Vector<OrderResult>();
	private Vector<OrderProcessorListener> m_Listeners = new Vector<OrderProcessorListener>(); 
	
	public OrderProcessor(Vector<Order> orders, Vector<Event> events)
	{
		m_Orders = orders;
		m_Events = events;
	}
	
	public void addListener(OrderProcessorListener opl)
	{
		if (!m_Listeners.contains(opl))
			m_Listeners.add(opl);
	}

	public void removeListener(OrderProcessorListener opl)
	{
		m_Listeners.remove(opl);
	}
	
	private void notifyOrderProcessingStarted()
	{
		for (OrderProcessorListener opl: m_Listeners)
			opl.orderProcessingStarted(this);
	}
	
	private void notifyOrderProcessed(Order o, OrderResult or)
	{
		for (OrderProcessorListener opl: m_Listeners)
			opl.orderProcessed(this, o, or);
	}
	
	private void notifyOrderProcessingCompleted()
	{
		for (OrderProcessorListener opl: m_Listeners)
			opl.orderProcessingCompleted(this, m_OrderResults);
		
		m_Listeners.clear();
	}
	
	public void run()
	{
		notifyOrderProcessingStarted();
		
		dumpOrdersToFile();
	
		for (Order o: m_Orders)
		{
			OrderResult or = processOrder(o);
			if (or != null)
			{
				m_OrderResults.add(or);
				notifyOrderProcessed(o, or);
			}
		}
		
		dumpOrderResultsToFile();
		
		Collections.sort(m_Events);
		
		notifyOrderProcessingCompleted();
	}
	
	private OrderResult processOrder(Order o)
	{
		//FIXME this is the critical routine
		return null;
	}
	
	private void dumpOrdersToFile()
	{
		
	}

	private void dumpOrderResultsToFile()
	{
		
	}
}
