package com.danven.web_library.service;


import com.danven.web_library.domain.book.Category;
import com.danven.web_library.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing categories.
 * Provides methods to retrieve all categories from the repository.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Constructs a new CategoryServiceImpl with the specified CategoryRepository.
     *
     * @param categoryRepository the repository to use for accessing category data.
     */
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retrieves all categories from the repository.
     *
     * @return a list of all categories.
     */
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
