package com.ecommerce.project.controller;

import com.ecommerce.project.configurations.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.security.jwt.JwtUtils;
import com.ecommerce.project.security.request.LoginRequest;
import com.ecommerce.project.security.request.SignUpRequest;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.security.response.UserInforResponse;
import com.ecommerce.project.security.services.UserDetailsImpl;
import com.ecommerce.project.security.services.UserDetailsServiceImpl;
import com.ecommerce.project.services.RoleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
        UserDetailsImpl userDetail = (UserDetailsImpl) authentication.getPrincipal();
        //  String jwtToken = jwtUtils.generateTokenFromUsername(userDetail.getUsername());

        // let's get the Cookie from user
        ResponseCookie cookie = jwtUtils.generateJwtCookie(userDetail);

        List<String> roles = userDetail.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();
        UserInforResponse userInforResponse =
                new UserInforResponse(
                        userDetail.getId(),
                        userDetail.getUsername(),
                        roles);

        // return new ResponseEntity<>(userInforResponse, HttpStatus.OK);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(userInforResponse);
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {

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
        ResponseCookie cleanJwtCookie = jwtUtils.generateCleanJwtCookie();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cleanJwtCookie.toString())
                .body(new MessageResponse("User logged out successfully"));
    }

    @GetMapping("/username")
    public String getCurrentUsername(Authentication authentication) {
        if (authentication != null) {
            return authentication.getName();
        } else {
            return "NULL";
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();
        UserInforResponse userInforResponse =
                new UserInforResponse(
                        userDetails.getId(),
                        userDetails.getUsername(),
                        roles);

        // return new ResponseEntity<>(userInforResponse, HttpStatus.OK);
        return ResponseEntity
                .ok()
                .body(userInforResponse);

    }

    @GetMapping("/sellers")
    public ResponseEntity<?> getAllSellers() {

        return null;
    }

}
