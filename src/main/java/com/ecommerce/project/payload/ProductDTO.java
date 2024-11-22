package com.ecommerce.project.payload;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    @NotBlank
    @Size(min = 3, message = "Product name must contain at least 3 characters")
    private String productName;
    private String image;
    @NotBlank
    @Size(min = 12, message = "Product description must contain at least 12 characters")
    private String description;
    @Positive(message = "Quantity must be positive number")
    private Integer quantity;
    @DecimalMin(value = "0.1", message = "Price must be at least 0.1")
//  @DecimalMax(value = "10000", message = "Price must not be exceed 10000")
    private double price;
    @DecimalMin(value = "0.0", message = "Discount must be at least 0%")
    @DecimalMax(value = "100.0", message = "Discount must not exceed 100%")
    private double discount;
    private double specialPrice;
}
