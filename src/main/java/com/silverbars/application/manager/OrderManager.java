package com.silverbars.application.manager;

import java.util.List;

import com.silverbars.application.beans.OrderBean;
import com.silverbars.application.beans.OrderRequest;
import com.silverbars.application.beans.OrderSummaryBean;
import com.silverbars.application.beans.OrderType;
import com.silverbars.application.exception.WebException;

/**
 * This interface must be implemented by the service class for order management
 * 
 * @author Ankit
 *
 */
public interface OrderManager {

	/**
	 * method for registering a new order
	 * @param order object with order details
	 */
	OrderBean createOrder(OrderRequest order) throws WebException;
	
	/**
	 * method for retrieving summary of orders of the specified type
	 * 
	 * @param orderType type of order
	 * @return list of orders
	 */
	List<OrderSummaryBean> getOrderSummary(OrderType orderType);
	
	/**
	 * method for deleting a previously registered order
	 * @param orderId id of the order to be deleted
	 */
	void deleteOrder(Integer orderId) throws WebException;
}
