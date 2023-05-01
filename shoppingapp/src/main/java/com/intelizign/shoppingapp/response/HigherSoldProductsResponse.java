package com.intelizign.shoppingapp.response;

import com.intelizign.shoppingapp.model.Product;

public class HigherSoldProductsResponse {

	private Product product;
	private Long soldItems;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Long getSoldItems() {
		return soldItems;
	}

	public void setSoldItems(Long soldItems) {
		this.soldItems = soldItems;
	}

	public HigherSoldProductsResponse(Product product, Long soldItems) {
		this.product = product;
		this.soldItems = soldItems;
	}

	public HigherSoldProductsResponse() {

	}
}
