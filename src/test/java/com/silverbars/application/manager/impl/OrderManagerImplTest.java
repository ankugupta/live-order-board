package com.silverbars.application.manager.impl;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpStatus;

import com.silverbars.application.beans.OrderBean;
import com.silverbars.application.beans.OrderRequest;
import com.silverbars.application.beans.OrderSummaryBean;
import com.silverbars.application.beans.OrderType;
import com.silverbars.application.exception.WebException;
import com.silverbars.application.manager.OrderManager;

public class OrderManagerImplTest {

	private OrderManager orderManager;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void init() {
		orderManager = new OrderManagerImpl();
	}

	@Test
	public void testCreateOrderWhenRequestIsValid() throws WebException {
		OrderRequest request = new OrderRequest();

		request.setUserId("user001");
		request.setPricePerKg("2.50");
		request.setQuantityKg("5");
		request.setOrderType("SELL");

		OrderBean order = orderManager.createOrder(request);

		Assert.assertEquals(Double.valueOf(5.0), order.getQuantityKg());
		Assert.assertNotNull(order.getId());

	}

	@Test
	public void testCreateOrderWhenRequestHasInvalidData() throws WebException {
		OrderRequest request = new OrderRequest();

		request.setUserId("user001");
		// invalid - less than 0
		request.setPricePerKg("-2.50");
		// invalid - less than 0
		request.setQuantityKg("-5");
		// invalid
		request.setOrderType("BELL");

		thrown.expect(WebException.class);
		thrown.expect(Matchers.hasProperty("httpStatus", Matchers.is(HttpStatus.BAD_REQUEST)));

		orderManager.createOrder(request);

	}

	@Test
	public void testCreateOrderWhenRequestDataParseFailure() throws WebException {
		OrderRequest request = new OrderRequest();

		request.setUserId("user001");
		// invalid - not a number
		request.setPricePerKg("-2.50a");
		// invalid - not a number
		request.setQuantityKg("-5a");
		request.setOrderType("BELL");

		thrown.expect(WebException.class);
		thrown.expect(Matchers.hasProperty("httpStatus", Matchers.is(HttpStatus.BAD_REQUEST)));

		orderManager.createOrder(request);

	}

	@Test
	public void testDeleteOrderWhenOrderNotFound() throws WebException {
		thrown.expect(WebException.class);
		thrown.expect(Matchers.hasProperty("httpStatus", Matchers.is(HttpStatus.NOT_FOUND)));

		orderManager.deleteOrder(2);
	}

	@Test
	public void testDeleteOrder() throws WebException {

		// create an order
		OrderRequest request = new OrderRequest();

		request.setUserId("user001");
		request.setPricePerKg("2.50");
		request.setQuantityKg("5");
		request.setOrderType("SELL");

		OrderBean order = orderManager.createOrder(request);

		// then delete it
		orderManager.deleteOrder(order.getId());
	}

	@Test
	public void testGetOrderSummaryForSellOrders() throws WebException {

		// create few SELL orders
		OrderRequest request = new OrderRequest();

		request.setUserId("user001");
		request.setPricePerKg("2.50");
		request.setQuantityKg("5");
		request.setOrderType("SELL");

		orderManager.createOrder(request);

		request.setQuantityKg("50");
		orderManager.createOrder(request);

		request.setPricePerKg("3");
		request.setQuantityKg("2.50");
		orderManager.createOrder(request);

		// get order summary
		List<OrderSummaryBean> orders = orderManager.getOrderSummary(OrderType.SELL);

		Assert.assertEquals(2, orders.size());
		Assert.assertEquals(Double.valueOf(55), orders.get(0).getQuantityKg());

	}

	@Test
	public void testGetOrderSummaryForBuyOrders() throws WebException {

		// create few BUY orders
		OrderRequest request = new OrderRequest();

		request.setUserId("user001");
		request.setPricePerKg("2.50");
		request.setQuantityKg("5");
		request.setOrderType("BUY");

		orderManager.createOrder(request);

		request.setQuantityKg("50");
		orderManager.createOrder(request);

		request.setPricePerKg("3");
		request.setQuantityKg("2.50");
		orderManager.createOrder(request);

		// get order summary
		List<OrderSummaryBean> orders = orderManager.getOrderSummary(OrderType.BUY);

		Assert.assertEquals(2, orders.size());
		Assert.assertEquals(Double.valueOf(2.5), orders.get(0).getQuantityKg());

	}

}
