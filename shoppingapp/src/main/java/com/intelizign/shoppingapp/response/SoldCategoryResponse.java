package com.intelizign.shoppingapp.response;

import com.intelizign.shoppingapp.model.Category;

public class SoldCategoryResponse {

	private Category category;
	private Long totalSold;

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Long getTotalSold() {
		return totalSold;
	}

	public void setTotalSold(Long totalSold) {
		this.totalSold = totalSold;
	}

	public SoldCategoryResponse(Category category, Long totalSold) {
	
		this.category = category;
		this.totalSold = totalSold;
	}
	
	public SoldCategoryResponse() {
		
	}
}
