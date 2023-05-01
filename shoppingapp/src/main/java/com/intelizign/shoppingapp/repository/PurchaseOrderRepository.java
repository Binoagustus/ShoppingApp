package com.intelizign.shoppingapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.intelizign.shoppingapp.model.Product;
import com.intelizign.shoppingapp.model.PurchaseOrder;
import com.intelizign.shoppingapp.model.Customer;
import com.intelizign.shoppingapp.response.HigherSoldProductsResponse;
import com.intelizign.shoppingapp.response.SoldCategoryResponse;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

	@Query(value = "SELECT order.product from PurchaseOrder order WHERE order.user.id = ?1")
	List<Product> findProductsByUserId(Long userId);

	@Query(value = "SELECT DISTINCT po.user from PurchaseOrder po WHERE po.product.id = ?1")
	List<Customer> findUsersByProductId(Long productId);

	@Query(value = "SELECT new com.intelizign.shoppingapp.response.HigherSoldProductsResponse(o.product,SUM(o.quantity)) "
			+ "FROM PurchaseOrder o where DATE_PART('MONTH', datetime) = ?1 and DATE_PART('YEAR', datetime) = ?2 GROUP BY o.product ORDER BY SUM(o.quantity) DESC ")
	List<HigherSoldProductsResponse> findProductsByHighSalesByMonth(int month, int year);
	
	
//	List<SoldCategoryResponse> findProductsSoldBasedOnCategory();
}
