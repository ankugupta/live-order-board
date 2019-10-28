package com.silverbars.application.rest.api;

import static com.silverbars.application.constants.Constants.ERROR_CODE_ORDER_TYPE_INVALID;
import static com.silverbars.application.constants.Constants.ERROR_MSG_ORDER_TYPE_INVALID;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.silverbars.application.beans.OrderRequest;
import com.silverbars.application.beans.OrderSummaryBean;
import com.silverbars.application.beans.OrderType;
import com.silverbars.application.exception.ErrorBean;
import com.silverbars.application.exception.WebException;
import com.silverbars.application.manager.OrderManager;

/**
 * Rest API for order management.
 * All exceptions generated are handled by the WebExceptionHandler.
 * 
 * @see {@link com.silverbars.application.exception.WebExceptionHandler WebExceptionHandler}
 * 
 * @author Ankit
 *
 */
@RestController
@RequestMapping(value = "orders")
public class OrderController {

	@Resource
	private OrderManager orderManager;

	/**
	 * API to retrieve order summary for specified order type (BUY or SELL).
	 * This method could have been implemented in another way - returning
	 * summary of all orders at once - but instead of designing API as per a
	 * specific UI use case, this design suits more to REST style of API design.
	 * 
	 * @param orderType
	 *            type of order
	 * @return list of order summary records
	 * @throws WebException
	 *             if error processing request
	 */
	@GetMapping(value = "/{orderType}/summary")
	public ResponseEntity<List<OrderSummaryBean>> getOrderSummary(@PathVariable String orderType) throws WebException {
		OrderType type = null;
		try {
			type = OrderType.valueOf(orderType);
		} catch (IllegalArgumentException ex) {
			throw new WebException(new ErrorBean(ERROR_CODE_ORDER_TYPE_INVALID, ERROR_MSG_ORDER_TYPE_INVALID),
					HttpStatus.BAD_REQUEST);
		}

		List<OrderSummaryBean> orders = this.orderManager.getOrderSummary(type);
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	/**
	 * API to register order
	 * 
	 * @param orderBean
	 *            order request object
	 * @return response entity with no body
	 * @throws WebException if error in creating order
	 */
	@PostMapping
	public ResponseEntity<Void> createOrder(@RequestBody OrderRequest orderRequest) throws WebException {

		this.orderManager.createOrder(orderRequest);
		//can add location header to get created resource
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * API to delete
	 * 
	 * @param orderId
	 * @return
	 * @throws WebException
	 */
	@DeleteMapping("/{orderId}")
	public ResponseEntity<Void> deleteOrder(@PathVariable("orderId") Integer orderId) throws WebException {
		this.orderManager.deleteOrder(orderId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
