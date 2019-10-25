package com.silverbars.application.beans;

/**
 * Model class for order summary
 * 
 * @author Ankit
 *
 */
public class OrderSummaryBean {

	private Double quantityKg;
	private Double pricePerKg;
	private OrderType orderType;

	public OrderSummaryBean(Double quantityKg, Double pricePerKg, OrderType orderType) {
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

	public Double getPricePerKg() {
		return pricePerKg;
	}

	public void setPricePerKg(Double pricePerKg) {
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
