package com.danven.web_library.repository;

import com.danven.web_library.domain.book.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing Category entities from the database.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {


}
