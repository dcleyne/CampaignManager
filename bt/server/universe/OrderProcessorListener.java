package bt.server.universe;

import java.util.Vector;

import bt.common.elements.unit.Order;
import bt.common.elements.unit.OrderResult;

public interface OrderProcessorListener 
{
	public void orderProcessingStarted(OrderProcessor op);
	public void orderProcessed(OrderProcessor op, Order o, OrderResult or);
	public void orderProcessingCompleted(OrderProcessor op, Vector<OrderResult> results);
}
