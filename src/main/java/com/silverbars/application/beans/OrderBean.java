package com.silverbars.application.beans;

import java.math.BigDecimal;

/**
 * Model class for order
 * 
 * @author Ankit
 *
 */
public class OrderBean {

	private Integer id;
	private String userId;
	private Double quantityKg;
	private BigDecimal pricePerKg;
	private OrderType orderType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
		return "OrderBean [id=" + id + ", userId=" + userId + ", quantityKg=" + quantityKg + ", pricePerKg="
				+ pricePerKg + ", orderType=" + orderType + "]";
	}

}
