package com.silverbars.application.beans;

import java.math.BigDecimal;

/**
 * Model class for order summary
 * 
 * @author Ankit
 *
 */
public class OrderSummaryBean {

	private Double quantityKg;
	private BigDecimal pricePerKg;
	private OrderType orderType;

	public OrderSummaryBean(Double quantityKg, BigDecimal pricePerKg, OrderType orderType) {
		super();
		this.quantityKg = quantityKg;
		this.pricePerKg = pricePerKg;
		this.orderType = orderType;
	}

	public Double getQuantityKg() {
		return quantityKg;
	}

	public void setQuantityKg(Double quantityKg) {
		this.quantityKg = quantityKg;
	}

	public BigDecimal getPricePerKg() {
		return pricePerKg;
	}

	public void setPricePerKg(BigDecimal pricePerKg) {
		this.pricePerKg = pricePerKg;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	@Override
	public String toString() {
		return "OrderSummaryBean [quantityKg=" + quantityKg + ", pricePerKg=" + pricePerKg + ", orderType=" + orderType
				+ "]";
	}

}
