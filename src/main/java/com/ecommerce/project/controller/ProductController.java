package com.ecommerce.project.controller;

import com.ecommerce.project.configurations.AppConstants;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.payload.common.PaginatedResponse;
import com.ecommerce.project.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(
            @Valid @RequestBody ProductDTO productDTO,
            @PathVariable Long categoryId) {
        ProductDTO savedProductDTO = productService.addProduct(categoryId, productDTO);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<PaginatedResponse<ProductDTO>> getAllProducts(
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstants.Page_Number) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.Page_Size) Integer pageSize
            , @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SORT_PRODUCTS_By) String sortBy
            , @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.Sort_DIR) String sortOrder

    ) {
        PaginatedResponse<ProductDTO> paginatedResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(paginatedResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<PaginatedResponse<ProductDTO>> searchByCategory(@PathVariable Long categoryId, @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstants.Page_Number) Integer pageNumber,
                                                                          @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.Page_Size) Integer pageSize
            , @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SORT_PRODUCTS_By) String sortBy
            , @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.Sort_DIR) String sortOrder
    ) {
        PaginatedResponse<ProductDTO> productResponse = productService.searchProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/name/{categoryName}/products")
    public ResponseEntity<PaginatedResponse<ProductDTO>> searchProductByCategoryName(
            @PathVariable String categoryName,
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstants.Page_Number) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.Page_Size) Integer pageSize
            , @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SORT_PRODUCTS_By) String sortBy
            , @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.Sort_DIR) String sortOrder
    ) {
        System.out.println(categoryName + " we got the categpry...");
        PaginatedResponse<ProductDTO> paginatedResponse = productService.searchProductsByCategoryName(categoryName, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(paginatedResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<PaginatedResponse<ProductDTO>> searchProductByKeyword(
            @PathVariable String keyword,
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstants.Page_Number) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.Page_Size) Integer pageSize
            , @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SORT_PRODUCTS_By) String sortBy
            , @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.Sort_DIR) String sortOrder
    ) {
        PaginatedResponse<ProductDTO> paginatedResponse = productService.searchProductsByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(paginatedResponse, HttpStatus.OK);
    }

    @PutMapping("/public/product/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @Valid @RequestBody ProductDTO productDTO,
            @PathVariable Long productId) {
        ProductDTO updatedProductDTO = productService.updateProduct(productDTO, productId);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        ProductDTO deleteProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deleteProduct, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updateProductDTO = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updateProductDTO, HttpStatus.OK);
    }
}
