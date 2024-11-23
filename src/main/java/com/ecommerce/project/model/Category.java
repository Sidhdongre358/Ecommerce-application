package com.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String categoryName;

    @OneToMany(mappedBy = "category")
    private Set<Product> products;


    @Override
    public int hashCode() {
        return Objects.hash(categoryId); // Use only the ID for hashCode
    }
}
