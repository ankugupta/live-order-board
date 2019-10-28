package com.silverbars.application.beans;

/**
 * Model class for order request.
 * 
 * @author Ankit
 *
 */
public class OrderRequest {

	private String userId;
	private String quantityKg;
	private String pricePerKg;
	private String orderType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getQuantityKg() {
		return quantityKg;
	}

	public void setQuantityKg(String quantityKg) {
		this.quantityKg = quantityKg;
	}

	public String getPricePerKg() {
		return pricePerKg;
	}

	public void setPricePerKg(String pricePerKg) {
		this.pricePerKg = pricePerKg;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	@Override
	public String toString() {
		return "OrderRequest [userId=" + userId + ", quantityKg=" + quantityKg + ", pricePerKg=" + pricePerKg
				+ ", orderType=" + orderType + "]";
	}

}
