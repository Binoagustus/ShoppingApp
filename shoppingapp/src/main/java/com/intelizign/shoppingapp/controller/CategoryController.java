package com.intelizign.shoppingapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intelizign.shoppingapp.model.Category;
import com.intelizign.shoppingapp.model.Product;
import com.intelizign.shoppingapp.response.ResponseHandler;
import com.intelizign.shoppingapp.service.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

	Logger logger = LoggerFactory.getLogger(CategoryController.class);

	@Autowired
	CategoryService categoryService;

	@PostMapping("/addcategory")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> addCategory(@RequestBody Category category) {
		try {
			categoryService.addCategory(category);
			return ResponseHandler.generateResponse("category has been added", true, HttpStatus.OK, null);
		} catch (Exception ex) {
			logger.error("Category not added");
			return ResponseHandler.generateResponse("category has not been added", false, HttpStatus.OK, null);
		}
	}

	/**
	 * 
	 * View All Categories With Products
	 * 
	 * @param pagenumber
	 * @param pagesize
	 * @return Pages of category
	 */
	@GetMapping("/view_all_categories")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<Object> viewAllCatgeories(@RequestParam int pagenumber, @RequestParam int pagesize) {
		try {
			Page<Category> categories = categoryService.viewAllCategoriesWithProducts(pagenumber, pagesize);
			return ResponseHandler.generateResponse("List of categories", true, HttpStatus.OK, categories);

		} catch (Exception e) {
			logger.error("Category not added");
			return ResponseHandler.generateResponse("No categoreis present", false, HttpStatus.OK, null);

		}
	}

	/*
	 * View Product List By category
	 */
	@GetMapping("view_product_list_by_category/{categoryName}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<Object> viewAvailableProductsByCategory(@PathVariable String categoryName,
			@RequestParam int pagenumber, @RequestParam int pagesize) {
		try {
			Page<Product> products = categoryService.viewProductsByCategoryAndAvailability(categoryName, pagenumber,
					pagesize);
			return ResponseHandler.generateResponse("List of products retrieved succesfully based on availability",
					true, HttpStatus.OK, products);

		} catch (Exception e) {
			logger.error("List of products based on availability could not be fetched");
			return ResponseHandler.generateResponse("List of products could not be fetched", false, HttpStatus.OK,
					null);

		}
	}
}
