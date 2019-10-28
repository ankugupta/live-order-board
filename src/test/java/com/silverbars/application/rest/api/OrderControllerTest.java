package com.silverbars.application.rest.api;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.silverbars.application.beans.OrderRequest;
import com.silverbars.application.beans.OrderSummaryBean;
import com.silverbars.application.beans.OrderType;
import com.silverbars.application.exception.WebException;
import com.silverbars.application.manager.OrderManager;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

	private OrderController orderController;

	@Mock
	private OrderManager orderManager;

	@Before
	public void init() throws NoSuchFieldException, SecurityException {
		orderController = new OrderController();
		FieldSetter.setField(orderController, orderController.getClass().getDeclaredField("orderManager"),
				orderManager);

	}

	@Test
	public void testGetOrderSummary() throws WebException {
		OrderSummaryBean orderSummaryBean = new OrderSummaryBean(2.0, new BigDecimal("2"), OrderType.SELL);
		Mockito.when(orderManager.getOrderSummary(OrderType.SELL)).thenReturn(Arrays.asList(orderSummaryBean));

		ResponseEntity<List<OrderSummaryBean>> response = orderController.getOrderSummary(OrderType.SELL.toString());

		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals(1, response.getBody().size());
	}

	@Test(expected = WebException.class)
	public void testGetOrderSummaryWhenRequestInvalid() throws WebException {

		orderController.getOrderSummary("invalid order type");

	}

	@Test
	public void testCreateOrder() throws WebException {
		OrderRequest request = new OrderRequest();

		request.setUserId("user001");
		request.setPricePerKg("2.50");
		request.setQuantityKg("5");
		request.setOrderType("SELL");

		ResponseEntity<Void> response = orderController.createOrder(request);

		Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void testDeleteOrder() throws WebException {

		ResponseEntity<Void> response = orderController.deleteOrder(1);

		Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
}
