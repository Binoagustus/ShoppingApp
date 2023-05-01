package com.intelizign.shoppingapp.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.intelizign.shoppingapp.exception.ShoppingException;
import com.intelizign.shoppingapp.model.Category;
import com.intelizign.shoppingapp.model.Product;
import com.intelizign.shoppingapp.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	CategoryRepository categoryRepo;

	public void addCategory(Category category) {
		if (categoryRepo.findByCategoryName(category.getCategoryName()).isPresent()) {
			throw new ShoppingException(category + "already present");
		} else
			categoryRepo.save(category);
	}

	public Page<Category> viewAllCategoriesWithProducts(int pagenumber, int pagesize) {
		Pageable pageable;
		if (categoryRepo.findAll() == null) {
			throw new ShoppingException("The list of categories is returning null");
		} else {
			pageable = PageRequest.of(pagenumber, pagesize, Sort.by("categoryName").ascending());
			return categoryRepo.findAll(pageable);
		}
	}

	public Page<Product> viewProductsByCategoryAndAvailability(String categoryName, int pagenumber, int pagesize) {
		Pageable pageable;
		if (categoryRepo.findByCategoryName(categoryName).isPresent()) {
			Optional<Category> category = categoryRepo.findByCategoryName(categoryName);
			List<Product> productList = category.get().getProducts().stream()
					.filter(product -> product.getAvailability().equals(true)).map(product -> product)
					.collect(Collectors.toList());
			pageable = PageRequest.of(pagenumber, pagesize);
			Page<Product> productpage = new PageImpl<>(productList, pageable, productList.size());
			return productpage;
		} else
			throw new ShoppingException("No such category is available");
	}

}
