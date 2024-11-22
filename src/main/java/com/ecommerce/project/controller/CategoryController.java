package com.ecommerce.project.controller;

import com.ecommerce.project.configurations.AppConstants;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.common.PaginatedResponse;
import com.ecommerce.project.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;


    @GetMapping("/public/categories")
    public ResponseEntity<PaginatedResponse<CategoryDTO>> getAllCategories(
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstants.Page_Number) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.Page_Size) Integer pageSize
    ,@RequestParam(name = "sortBy",required = false, defaultValue = AppConstants.Sort_Categories_BY) String sortBy
            , @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.Sort_DIR) String sortOrder
    ) {
        PaginatedResponse<CategoryDTO> paginatedResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(paginatedResponse, HttpStatus.OK);
    }


    @PostMapping("/admin/category")
    public ResponseEntity<CategoryDTO> createCategory( @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
        CategoryDTO deleteCategoryDTO = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deleteCategoryDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory( @Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId) {
        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);
    }
}
