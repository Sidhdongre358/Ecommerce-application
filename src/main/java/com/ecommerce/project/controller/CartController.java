package com.ecommerce.project.controller;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cart Management", description = "APIs for managing the shopping cart of users")
@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private CartService cartService;

    @Operation(
            summary = "Add product to cart",
            description = "Add a specific product to the cart with a specified quantity."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully added the product to the cart"),
            @ApiResponse(responseCode = "400", description = "Invalid product ID or quantity")
    })
    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(
            @Parameter(description = "ID of the product to be added to the cart")
            @PathVariable Long productId,

            @Parameter(description = "Quantity of the product to be added")
            @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all carts",
            description = "Fetch all carts available in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched all carts"),
            @ApiResponse(responseCode = "404", description = "No carts found")
    })
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts() {
        List<CartDTO> cartDTOs = cartService.getAllCarts();
        return new ResponseEntity<>(cartDTOs, HttpStatus.OK);
    }

    @Operation(
            summary = "Get user-specific cart",
            description = "Fetch the cart for the logged-in user based on their email."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched the user's cart"),
            @ApiResponse(responseCode = "404", description = "Cart not found for the user")
    })
    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById() {
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);
        if (cart == null) {
            throw new APIException("No cart exists for this : " +emailId );
        }
        Long cartId = cart.getCartId();
        CartDTO cartDTO = cartService.getCart(emailId, cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Update product quantity in cart",
            description = "Update the quantity of a product in the user's cart. Quantity can be increased or decreased."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated product quantity"),
            @ApiResponse(responseCode = "400", description = "Invalid operation or product ID")
    })
    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(
            @Parameter(description = "ID of the product to update in the cart")
            @PathVariable Long productId,

            @Parameter(description = "Operation to be performed on the quantity: 'delete' to remove, anything else to add")
            @PathVariable String operation) {
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId, operation.equalsIgnoreCase("delete") ? -1 : 1);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete product from cart",
            description = "Remove a specific product from the user's cart."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully removed the product from the cart"),
            @ApiResponse(responseCode = "404", description = "Product not found in the cart")
    })
    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(
            @Parameter(description = "ID of the cart")
            @PathVariable Long cartId,

            @Parameter(description = "ID of the product to be deleted from the cart")
            @PathVariable Long productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
