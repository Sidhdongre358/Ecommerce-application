package com.ecommerce.project.security.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank
    @Size(min = 4, message = "Username must contain at least 4 characters")
    private String username;
    @NotBlank
    @Size(min = 8, message = "password must contain at least 8 characters")
    private String password;
}
