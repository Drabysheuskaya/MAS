package com.danven.web_library.repository;

import com.danven.web_library.domain.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing Customer entities from the database.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
