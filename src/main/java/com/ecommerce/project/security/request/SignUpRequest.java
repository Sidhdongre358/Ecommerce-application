package com.ecommerce.project.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank
    @Size(min = 6, message = "Username must contain at least 6 characters")
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, message = "password must contain at least 8 characters")
    private String password;
    @NotBlank
    private Set<String> role = new HashSet<>();


}
