package com.intelizign.shoppingapp.response;

public class ProductPurchaseResponse {

	private String productName;
	private String productCategory;
	private int purchaseQuantity;
	private String fileUri;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public int getPurchaseQuantity() {
		return purchaseQuantity;
	}

	public void setPurchaseQuantity(int purchaseQuantity) {
		this.purchaseQuantity = purchaseQuantity;
	}

	public String getFileUri() {
		return fileUri;
	}

	public void setFileUri(String fileUri) {
		this.fileUri = fileUri;
	}

	public ProductPurchaseResponse(String productName, String productCategory, int purchaseQuantity, String fileUri) {
	
		this.productName = productName;
		this.productCategory = productCategory;
		this.purchaseQuantity = purchaseQuantity;
		this.fileUri = fileUri;
	}
	
	public ProductPurchaseResponse() {
		
	}
}
