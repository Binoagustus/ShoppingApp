package com.intelizign.shoppingapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.intelizign.shoppingapp.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{

	Optional<Customer> findByUsername(String username);
	
	Optional<Customer> findByEmail(String email);

	Optional<Customer> findByPasswordResetToken(String token);
}
