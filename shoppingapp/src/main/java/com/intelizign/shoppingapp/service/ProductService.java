package com.intelizign.shoppingapp.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.intelizign.shoppingapp.exception.ShoppingException;
import com.intelizign.shoppingapp.model.Category;
import com.intelizign.shoppingapp.model.Product;
import com.intelizign.shoppingapp.repository.CategoryRepository;
import com.intelizign.shoppingapp.repository.ProductRepository;
import com.intelizign.shoppingapp.request.ProductRequest;
import com.intelizign.shoppingapp.request.ProductUpdateRequest;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepo;

	@Autowired
	CategoryRepository categoryRepo;

	@Autowired
	FileStorageService fileService;

	public Product addProduct(ProductRequest productRequest) throws IOException {

		Product product = null;

		if (categoryRepo.findByCategoryName(productRequest.getCategoryName()).isPresent()) {
			Optional<Category> category = categoryRepo.findByCategoryName(productRequest.getCategoryName());

			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
					.path(productRequest.getProductName()).toUriString();

			product = new Product(productRequest.getProductId(), productRequest.getProductName(),
					productRequest.getPrice(), fileDownloadUri,
					categoryRepo.findByCategoryName(productRequest.getCategoryName()).get());
			boolean isProductExist = category.get().getProducts().stream()
					.anyMatch(prod -> prod.getProductId() == (productRequest.getProductId()));

			if (isProductExist) {
				throw new ShoppingException("Product already present in the category");
			} else {
				List<Product> productList = category.get().getProducts();
				productList.add(product);
				category.get().setProducts(productList);
				fileService.save(productRequest.getFile());
				categoryRepo.save(category.get());
			}

		}
		return product;

	}

	public List<Product> getAllProductList() {
		if (productRepo.findAll() == null) {
			throw new ShoppingException("Product List is returning null");
		} else
			return productRepo.findAll();
	}

	public Product getProduct(Long productId) {

		if (productRepo.findById(productId).isPresent()) {
			return productRepo.findById(productId).get();
		} else
			throw new ShoppingException("Product Id is not present");
	}

	public void deleteProduct(Long productId) {
		if (productRepo.findById(productId).isPresent()) {
			productRepo.deleteById(productId);
		} else
			throw new ShoppingException("Product Id is not present. Could not be deleted");

	}

	public Product updateProduct(Long productId, ProductUpdateRequest updateRequest) {
		if (productRepo.findById(productId).isPresent()) {
			Optional<Product> product = productRepo.findById(productId);
			if (updateRequest.getProductName() != null) {
				product.get().setProductName(updateRequest.getProductName());
			}
			if (updateRequest.getPrice() != 0) {
				product.get().setPrice(updateRequest.getPrice());
			}
			if (updateRequest.getAvailability() != null) {
				product.get().setAvailability(updateRequest.getAvailability());
			}
			return productRepo.save(product.get());
		} else
			throw new ShoppingException("product Id not exists to update a product");
	}
}
