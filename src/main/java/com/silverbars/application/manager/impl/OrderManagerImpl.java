package com.silverbars.application.manager.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.silverbars.application.beans.OrderBean;
import com.silverbars.application.beans.OrderSummaryBean;
import com.silverbars.application.beans.OrderType;
import com.silverbars.application.exception.ErrorBean;
import com.silverbars.application.exception.WebException;
import com.silverbars.application.manager.OrderManager;

/**
 * Provides services for managing orders.
 * 
 * @author Ankit
 *
 */
@Service
public class OrderManagerImpl implements OrderManager {

	// using synchronized version for thread safety
	private List<OrderBean> registeredOrders = Collections.synchronizedList(new ArrayList<>());
	private AtomicInteger lastOrderIndex = new AtomicInteger(0);

	@Override
	public void createOrder(OrderBean order) {
		// TODO: add validation steps
		order.setId(lastOrderIndex.addAndGet(1));
		registeredOrders.add(order);
	}

	@Override
	public List<OrderSummaryBean> getOrderSummary(OrderType orderType) {

		List<OrderSummaryBean> orderSummaryList = new ArrayList<>();
		Map<Double, Double> priceToQuantityMap = Collections.emptyMap();

		// filter orders of specified order type - then group orders with same
		// price while adding quantities of grouped orders
		synchronized (registeredOrders) {
			priceToQuantityMap = registeredOrders.stream().filter(order -> order.getOrderType().equals(orderType))
					.collect(Collectors.groupingBy(OrderBean::getPricePerKg,
							Collectors.summingDouble(OrderBean::getQuantityKg)));
		}

		// decide sorting order based on order type (ascending for SELL and
		// descending for BUY)
		Comparator<Map.Entry<Double, Double>> entryComparator = OrderType.SELL.equals(orderType)
				? Map.Entry.<Double, Double>comparingByKey() : Map.Entry.<Double, Double>comparingByKey().reversed();

		priceToQuantityMap.entrySet().stream().sorted(entryComparator)
				.forEachOrdered(e -> orderSummaryList.add(new OrderSummaryBean(e.getValue(), e.getKey(), orderType)));

		return orderSummaryList;
	}

	@Override
	public void deleteOrder(Integer orderId) throws WebException {
		Optional<OrderBean> orderToDelete = Optional.empty();
		synchronized (registeredOrders) {
			orderToDelete = registeredOrders.stream().filter(order -> order.getId().equals(orderId)).findFirst();
			if (orderToDelete.isPresent()) {
				registeredOrders.remove(orderToDelete.get());
			} else {
				// throw an exception with custom error code and message for
				// missing order
				throw new WebException(new ErrorBean("4040101", "error.order.not.found"), HttpStatus.NOT_FOUND);
			}
		}
	}

}
