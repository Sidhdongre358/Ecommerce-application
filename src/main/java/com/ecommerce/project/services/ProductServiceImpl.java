package com.ecommerce.project.services;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.payload.common.PaginatedResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.services.file.FileServices;
import com.ecommerce.project.utilities.PaginationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileServices fileServices;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Set<Product> products = category.getProducts();
        if (!products.isEmpty())
            products.forEach(product -> {
                if (product.getProductName().equalsIgnoreCase(productDTO.getProductName())) {
                    throw new APIException("Product already present with this product name " + productDTO.getProductName());
                }
            });


        Product product = modelMapper.map(productDTO, Product.class);


        product.setImage("dafault.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        category.getProducts().add(savedProduct);
        Category saved = categoryRepository.save(category);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public PaginatedResponse<ProductDTO> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = PaginationUtil.getSortByAndOrder(sortBy, sortOrder);
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);
        PaginatedResponse<ProductDTO> paginatedResponse = PaginationUtil.getPaginatedResponse(productPage, ProductDTO.class, modelMapper);
        return paginatedResponse;
    }

    @Override
    public PaginatedResponse<ProductDTO> searchProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "categoryId", categoryId)
        );
        Sort sortByAndOrder = PaginationUtil.getSortByAndOrder(sortBy, sortOrder);
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> page = productRepository.findByCategory(category, pageDetails);

        PaginatedResponse<ProductDTO> paginatedResponse = PaginationUtil.getPaginatedResponse(page, ProductDTO.class, modelMapper);
        return paginatedResponse;
    }

    @Override
    public PaginatedResponse<ProductDTO> searchProductsByCategoryName(String categoryName, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findByCategoryNameLikeIgnoreCase('%' + categoryName + '%');
        if (category == null) {
            throw new APIException("No category found with this name : " + categoryName);
        }

        Sort sortByAndOrder = PaginationUtil.getSortByAndOrder(sortBy, sortOrder);
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findByCategory(category, pageDetails);


        PaginatedResponse<ProductDTO> paginatedResponse = PaginationUtil.getPaginatedResponse(productPage, ProductDTO.class, modelMapper);
        return paginatedResponse;

    }

    @Override
    public PaginatedResponse<ProductDTO> searchProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = PaginationUtil.getSortByAndOrder(sortBy, sortOrder);
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageDetails);
        PaginatedResponse<ProductDTO> paginatedResponse = PaginationUtil.getPaginatedResponse(productPage, ProductDTO.class, modelMapper);
        return paginatedResponse;

    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        Product product = modelMapper.map(productDTO, Product.class);
        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setPrice(product.getPrice());
        double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();
        productFromDb.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(productFromDb);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.deleteById(productId);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        System.out.println("path " + path);
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        String fileName = fileServices.uploadImage(path, image);
        productFromDB.setImage(fileName);
        Product updatedProduct = productRepository.save(productFromDB);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }


}
