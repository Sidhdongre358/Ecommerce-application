package com.ecommerce.project.controller;

import com.ecommerce.project.configurations.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.security.jwt.JwtUtils;
import com.ecommerce.project.security.request.LoginRequest;
import com.ecommerce.project.security.request.SignUpRequest;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.security.response.UserInforResponse;
import com.ecommerce.project.security.services.UserDetailImpl;
import com.ecommerce.project.security.services.UserDetailsServiceImpl;
import com.ecommerce.project.services.RoleServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private RoleServices roleServices;
    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword()));


        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);

        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetail);
        List<String> roles = userDetail.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();
        UserInforResponse userInforResponse = new UserInforResponse(userDetail.getId(), jwtToken, userDetail.getUsername(), roles);

        return new ResponseEntity<>(userInforResponse, HttpStatus.OK);
    }



    @PostMapping("/signup")
    public ResponseEntity<?> registerUser( @RequestBody SignUpRequest signUpRequest) {

        System.out.println("Register new User");
        if (userDetailsService.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken"));
        }
        if (userDetailsService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use"));
        }

        // Create  new User's account

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleServices.findByRoleName(AppRole.ROLE_USER);
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleServices.findByRoleName(AppRole.ROLE_ADMIN);
                        roles.add(adminRole);
                    }
                    case "seller" -> {
                        Role sellerRole = roleServices.findByRoleName(AppRole.ROLE_SELLER);
                        roles.add(sellerRole);
                    }
                    default -> {
                        Role userRole = roleServices.findByRoleName(AppRole.ROLE_USER);
                        roles.add(userRole);
                    }
                }
            });
        }
        user.setRoles(roles);
        User savedUser = userDetailsService.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut() {

        return null;
    }

    @GetMapping("/username")
    public ResponseEntity<?> getCurrentUsername() {

        return null;
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo() {

        return null;
    }

    @GetMapping("/sellers")
    public ResponseEntity<?> getAllSellers() {

        return null;
    }

}
