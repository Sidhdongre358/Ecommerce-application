package com.ecommerce.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    private String productName;
    private String image;
    private String description;


    private Integer quantity;

    private double price;

    private double discount;

    private double specialPrice;


    @ManyToOne
    @JoinColumn(name = "category_id")

    private Category category;

    @Override
    public int hashCode() {
        return Objects.hash(productId); // Use only the ID for hashCode
    }
}
