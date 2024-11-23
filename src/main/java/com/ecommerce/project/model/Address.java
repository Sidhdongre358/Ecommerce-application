package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id", nullable = false)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name size must be at least 5 characters")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name size must be at least 5 characters")
    private String buildingName;
    @NotBlank
    @Size(min = 4, message = "City name size must be at least 4 characters")
    private String city;

    @NotBlank
    @Size(min = 2, message = "Sate name size must be at least 2 characters")
    private String state;
    @NotBlank
    @Size(min = 2, message = "Country name size must be at least 2 characters")
    private String country;

    @NotBlank
    @Size(min = 6, message = "Pin code name size must be at least 6 characters")
    private String pincode;
    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address(String street, String buildingName, String city, String state, String country, String pincode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }
}
