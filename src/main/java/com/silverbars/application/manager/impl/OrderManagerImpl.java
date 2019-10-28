package com.silverbars.application.manager.impl;

import static com.silverbars.application.constants.Constants.ERROR_CODE_ORDER_NOT_FOUND;
import static com.silverbars.application.constants.Constants.ERROR_CODE_ORDER_PRICE_INVALID;
import static com.silverbars.application.constants.Constants.ERROR_CODE_ORDER_QUANTITY_INVALID;
import static com.silverbars.application.constants.Constants.ERROR_CODE_ORDER_TYPE_INVALID;
import static com.silverbars.application.constants.Constants.ERROR_MSG_ORDER_NOT_FOUND;
import static com.silverbars.application.constants.Constants.ERROR_MSG_ORDER_PRICE_INVALID;
import static com.silverbars.application.constants.Constants.ERROR_MSG_ORDER_QUANTITY_INVALID;
import static com.silverbars.application.constants.Constants.ERROR_MSG_ORDER_TYPE_INVALID;

import java.math.BigDecimal;
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
import com.silverbars.application.beans.OrderRequest;
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

	// NOTE: normally, this data handling would be implemented in a persistence
	// layer working over a DB

	// collection for order objects
	// using synchronized version for thread safety
	private List<OrderBean> registeredOrders = Collections.synchronizedList(new ArrayList<>());

	// track order id's
	private AtomicInteger lastOrderIndex = new AtomicInteger(0);

	@Override
	public OrderBean createOrder(OrderRequest orderRequest) throws WebException {
		OrderBean order = validateAndConvertRequest(orderRequest);
		order.setId(lastOrderIndex.addAndGet(1));
		registeredOrders.add(order);
		return order;
	}

	private static OrderBean validateAndConvertRequest(OrderRequest orderRequest) throws WebException {
		OrderBean orderBean = new OrderBean();
		List<ErrorBean> errors = new ArrayList<>();

		// validate and set order type
		OrderType type = null;
		try {
			type = OrderType.valueOf(orderRequest.getOrderType());
			orderBean.setOrderType(type);
		} catch (IllegalArgumentException ex) {
			errors.add(new ErrorBean(ERROR_CODE_ORDER_TYPE_INVALID, ERROR_MSG_ORDER_TYPE_INVALID));
		}

		// validate and set quantity
		double quantity = 0;
		try {
			quantity = Double.parseDouble(orderRequest.getQuantityKg());
			if (quantity <= 0) {
				errors.add(new ErrorBean(ERROR_CODE_ORDER_QUANTITY_INVALID, ERROR_MSG_ORDER_QUANTITY_INVALID));
			} else {
				orderBean.setQuantityKg(quantity);
			}
		} catch (NumberFormatException ex) {
			errors.add(new ErrorBean(ERROR_CODE_ORDER_QUANTITY_INVALID, ERROR_MSG_ORDER_QUANTITY_INVALID));
		}

		// validate and set price
		try {
			double priceVal = Double.parseDouble(orderRequest.getPricePerKg());
			if (priceVal <= 0) {
				errors.add(new ErrorBean(ERROR_CODE_ORDER_PRICE_INVALID, ERROR_MSG_ORDER_PRICE_INVALID));
			} else {
				orderBean.setPricePerKg(BigDecimal.valueOf(priceVal));
			}
		} catch (NumberFormatException ex) {
			errors.add(new ErrorBean(ERROR_CODE_ORDER_PRICE_INVALID, ERROR_MSG_ORDER_PRICE_INVALID));
		}

		if (!errors.isEmpty()) {
			throw new WebException(errors, HttpStatus.BAD_REQUEST);
		}

		return orderBean;
	}

	@Override
	public List<OrderSummaryBean> getOrderSummary(OrderType orderType) {

		List<OrderSummaryBean> orderSummaryList = new ArrayList<>();
		Map<BigDecimal, Double> priceToQuantityMap = Collections.emptyMap();

		// filter orders of specified order type - then group orders with same
		// price while adding quantities of grouped orders
		synchronized (registeredOrders) {
			priceToQuantityMap = registeredOrders.stream().filter(order -> order.getOrderType().equals(orderType))
					.collect(Collectors.groupingBy(OrderBean::getPricePerKg,
							Collectors.summingDouble(OrderBean::getQuantityKg)));
		}

		// decide sorting order based on order type (ascending for SELL and
		// descending for BUY)
		Comparator<Map.Entry<BigDecimal, Double>> entryComparator = OrderType.SELL.equals(orderType)
				? Map.Entry.<BigDecimal, Double>comparingByKey()
				: Map.Entry.<BigDecimal, Double>comparingByKey().reversed();

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
				throw new WebException(new ErrorBean(ERROR_CODE_ORDER_NOT_FOUND, ERROR_MSG_ORDER_NOT_FOUND),
						HttpStatus.NOT_FOUND);
			}
		}
	}

}
