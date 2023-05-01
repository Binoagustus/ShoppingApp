package com.intelizign.shoppingapp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intelizign.shoppingapp.model.Product;
import com.intelizign.shoppingapp.request.ProductRequest;
import com.intelizign.shoppingapp.request.ProductUpdateRequest;
import com.intelizign.shoppingapp.response.ResponseHandler;
import com.intelizign.shoppingapp.service.ProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	ProductService productService;
		
	@PostMapping("/addproduct")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> addProducts(@ModelAttribute ProductRequest product) {
		try {
			Product productResponse = productService.addProduct(product);
			return ResponseHandler.generateResponse("Product added succesfully", true, HttpStatus.OK, productResponse);
		} catch(Exception ex) {
			logger.error("Could not add the product " + ex.getMessage());
			ex.printStackTrace();
			return ResponseHandler.generateResponse("Product was not added", false, HttpStatus.OK, null);
		}
	}
	
	@GetMapping("/viewallproducts")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<Object> viewAllProducts() {
		try {
			List<Product> productList = productService.getAllProductList();
			return ResponseHandler.generateResponse("These are the list of products", true, HttpStatus.OK, productList);
		} catch(Exception ex) {
			logger.error("No product available" + ex.getMessage());
			return ResponseHandler.generateResponse("No products available to display", false, HttpStatus.OK, null);
		}
	}
	
	@GetMapping("/viewProduct")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<Object> viewProduct(@RequestParam Long productId) {
		try {	
			Product product = productService.getProduct(productId);
			return ResponseHandler.generateResponse("Product retrieved succesfully", true, HttpStatus.OK, product);
		} catch(Exception ex) {
			logger.error("No product available" + ex.getMessage());
			return ResponseHandler.generateResponse("No products available to display", false, HttpStatus.OK, null);
		}
	}
	
	@DeleteMapping("/delete_product_by_id")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> deleteProduct(@RequestParam Long productId) {
		try {
			productService.deleteProduct(productId);
			return ResponseHandler.generateResponse("Product deleted succesfully", true, HttpStatus.OK, "Product could not be deleted");
		} catch(Exception ex) {
			logger.error("Product could not be deleted" + ex.getMessage());
			return ResponseHandler.generateResponse("Product cannot be deleted", false, HttpStatus.OK, null);
		}
	}
	
	@PutMapping("/update_product")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> updateProductAvailabilityOrPrice(@RequestParam Long productId, @RequestBody ProductUpdateRequest updateRequest) {
		try {
			Product product = productService.updateProduct(productId, updateRequest);
			return ResponseHandler.generateResponse("Product updated succesfully", true, HttpStatus.OK, product);
		} catch(Exception ex) {
			logger.error("Product could not be deleted" + ex.getMessage());
			return ResponseHandler.generateResponse("Product could not be updated", false, HttpStatus.OK, null);
		}
	}
}
