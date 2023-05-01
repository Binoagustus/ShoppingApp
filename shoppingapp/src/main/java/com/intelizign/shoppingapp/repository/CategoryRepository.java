package com.intelizign.shoppingapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.intelizign.shoppingapp.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String>{

	Optional<Category> findByCategoryName(String categoryName);
	
}
