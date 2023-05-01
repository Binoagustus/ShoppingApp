package com.intelizign.shoppingapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intelizign.shoppingapp.model.Product;
import com.intelizign.shoppingapp.model.PurchaseOrder;
import com.intelizign.shoppingapp.model.Customer;
import com.intelizign.shoppingapp.response.HigherSoldProductsResponse;
import com.intelizign.shoppingapp.response.ResponseHandler;
import com.intelizign.shoppingapp.service.PurchaseOrderService;

@RestController
@RequestMapping("/api/porder")
public class PurchaseOrderController {

	Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);

	@Autowired
	PurchaseOrderService poService;

	@PostMapping("/purchase")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<Object> addProductsToPurchaseOrder(@RequestParam Long productId, @RequestParam int quantity, HttpServletRequest request) {
		try {
			PurchaseOrder purchaseOrder = poService.purchase(productId, quantity, request.getUserPrincipal().getName());
			return ResponseHandler.generateResponse("Product purchased succesfully", true, HttpStatus.OK,
					purchaseOrder);
		} catch (Exception ex) {
			logger.error("Purchase Failed " + ex.getMessage());
			ex.printStackTrace();
			return ResponseHandler.generateResponse("Product was not added", false, HttpStatus.OK, null);
		}
	}

	@GetMapping("/viewallorders")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> viewAllOrders() {
		try {
			poService.viewAllOrders();
			return ResponseHandler.generateResponse("These are the list of orders placed", true, HttpStatus.OK, null);
		} catch (Exception ex) {
			logger.error("Could not retrieve the list of purchases" + ex.getMessage());
			return ResponseHandler.generateResponse("The orders could not be retrieved", false, HttpStatus.OK, null);
		}
	}

	@GetMapping("/view_products_by_userid")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<Object> viewUserOrderProducts(@RequestParam Long userId) {
		try {
			List<Product> products = poService.viewProducts(userId);
			return ResponseHandler.generateResponse("These are the list of products of user", true, HttpStatus.OK,
					products);
		} catch (Exception ex) {
			logger.error("Could not retrieve the list of purchases" + ex.getMessage());
			return ResponseHandler.generateResponse("The List of Products could not be ", false, HttpStatus.OK, null);
		}
	}

	@GetMapping("/view_users_by_productid")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> viewUsersByProductName(@RequestParam Long productId) {
		try {
			List<Customer> users = poService.viewUserByProductId(productId);
			return ResponseHandler.generateResponse("List of users retrieved succesfully", true, HttpStatus.OK, users);
		} catch (Exception ex) {
			logger.error("Could not retrieve the list of purchases" + ex.getMessage());
			return ResponseHandler.generateResponse("The List of Users could not be retrieved", false, HttpStatus.OK,
					null);
		}
	}

	@GetMapping("/view_products_with_sales")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> getListOfProductsWithHighSales(
			@RequestParam @Min(value = 1, message = "Value of Month should be greater than 1 and less than or equals 12") 
			@Max(value = 12, message = "Value of Month should be greater than 1 and less than or equals 12") int month,
			@RequestParam @Min(value = 2020, message = "Value of Year should be greater than 2020") 
			@Max(value = 2040, message = "Value of Year should less than or equals 2040") int year) {
		try {
			List<HigherSoldProductsResponse> highSales = poService.viewListOfProductsWithHighSales(month, year);
			return ResponseHandler.generateResponse("List of products with sales is", true, HttpStatus.OK, highSales);
		} catch (Exception ex) {
			logger.error("Could not retrieve the list of products with high sales" + ex.getMessage());
			return ResponseHandler.generateResponse("Could not retrieve the list of products with high sales", false,
					HttpStatus.OK, null);
		}
	}
}
