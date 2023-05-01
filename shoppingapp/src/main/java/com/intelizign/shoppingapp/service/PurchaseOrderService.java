package com.intelizign.shoppingapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intelizign.shoppingapp.exception.ShoppingException;
import com.intelizign.shoppingapp.model.Product;
import com.intelizign.shoppingapp.model.PurchaseOrder;
import com.intelizign.shoppingapp.model.Customer;
import com.intelizign.shoppingapp.repository.ProductRepository;
import com.intelizign.shoppingapp.repository.PurchaseOrderRepository;
import com.intelizign.shoppingapp.repository.CustomerRepository;
import com.intelizign.shoppingapp.response.HigherSoldProductsResponse;

@Service
public class PurchaseOrderService {

	@Autowired
	PurchaseOrderRepository poRepo;

	@Autowired
	ProductRepository productRepo;

	@Autowired
	CustomerRepository userRepo;

	/**
	 * Returns Purchase Order with User Object and Product Object
	 * 
	 * @param productName - Name of the product
	 * @param name        - Name of the User
	 * @return - Purchase Order
	 */
	public PurchaseOrder purchase(Long productId, int quantity, String name) {
		Optional<Customer> user = userRepo.findByUsername(name);
		if (user.isPresent()) {

			Optional<Product> product = productRepo.findById(productId);
			if (product.isPresent()) {
				LocalDateTime dateTime = LocalDateTime.now();
				PurchaseOrder purchaseOrder = new PurchaseOrder(user.get(), product.get(), product.get().getPrice(),
						quantity, product.get().getPrice() * quantity, dateTime);
				return poRepo.save(purchaseOrder);

			} else
				throw new ShoppingException("product doesn't exists");

		} else
			throw new ShoppingException("user not exists");
	}

	/**
	 * Returns list of all Purchase Orders
	 */
	public List<PurchaseOrder> viewAllOrders() {
		if (poRepo.findAll() == null) {
			throw new ShoppingException("Orders is returning null");
		} else
			return poRepo.findAll();
	}

	/**
	 * Returns the list of products based on the User Id
	 * 
	 * @param request - HTTP Servlet Request
	 * @return - List of Products
	 */
	public List<Product> viewProducts(Long userId) {

		if (poRepo.findProductsByUserId(userId) == null) {
			throw new ShoppingException("List of Products by User Id is returning null");
		} else
			return poRepo.findProductsByUserId(userId);

	}

	public List<Customer> viewUserByProductId(Long productId) {

		if (poRepo.findUsersByProductId(productId) == null) {
			throw new ShoppingException("List of Users by Product Id is returning null");
		} else
			return poRepo.findUsersByProductId(productId);
	}

	public List<HigherSoldProductsResponse> viewListOfProductsWithHighSales(int month, int year) {

		if (poRepo.findProductsByHighSalesByMonth(month, year) == null) {
			throw new ShoppingException("List of Products With High Sales is returning null");
		} else
			return poRepo.findProductsByHighSalesByMonth(month, year);
	}
}
