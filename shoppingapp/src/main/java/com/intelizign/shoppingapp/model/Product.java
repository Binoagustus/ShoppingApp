package com.intelizign.shoppingapp.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Product {

	@Id
	private Long productId;
	private String productName;
	private int price;
	private String fileUri;
	private Boolean availability = true;

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL)
	private Category category;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getFileUri() {
		return fileUri;
	}

	public void setFileUri(String fileUri) {
		this.fileUri = fileUri;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Boolean getAvailability() {
		return availability;
	}

	public void setAvailability(Boolean availability) {
		this.availability = availability;
	}

	public Product(Long productId, String productName, int price, String fileUri, Category category) {

		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.fileUri = fileUri;
		this.category = category;
	}

	public Product() {

	}
}
