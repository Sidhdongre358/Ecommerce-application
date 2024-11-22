package com.ecommerce.project.services;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.payload.common.PaginatedResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {


    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);


    PaginatedResponse<ProductDTO> searchProductsByCategory(Long categoryId,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    PaginatedResponse<ProductDTO>  searchProductsByCategoryName(String categoryName, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    PaginatedResponse<ProductDTO> searchProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);


    ProductDTO updateProduct(ProductDTO productDTO, Long productId);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

    PaginatedResponse<ProductDTO> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
