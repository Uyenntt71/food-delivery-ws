package com.food_delivery.controller;

import static com.food_delivery.util.CommonUtils.validateLocation;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food_delivery.constant.ErrorDictionary;
import com.food_delivery.exception.CommonException;
import com.food_delivery.exception.ErrorCode;
import com.food_delivery.model.CartInfo;
import com.food_delivery.model.CartItemInfo;
import com.food_delivery.model.ProductInfo;
import com.food_delivery.model.form.AddressForm;
import com.food_delivery.model.view.CartDetailView;
import com.food_delivery.model.view.CartInfoView;
import com.food_delivery.service.CartService;
import com.food_delivery.service.ProductService;

import lombok.extern.log4j.Log4j2;

@Controller
@RestController
@RequestMapping(value = "/cart", produces = APPLICATION_JSON_UTF8_VALUE)
@CrossOrigin("*")
@Log4j2
public class CartController {
    private static final String INVALID_PRODUCT_ID = "Invalid productId";

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    @GetMapping()
    public ResponseEntity<Object> getCarts(@RequestParam double lat,
                                           @RequestParam double lng) throws CommonException {
        validateLocation(lat, lng);
        List<CartInfo> carts = cartService.getCartsByUser(lat, lng);
        CartInfoView view = new CartInfoView(carts.size(), carts);

        return new ResponseEntity<>(view, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCartItemsInfo(@PathVariable String id) throws CommonException {
        List<CartItemInfo> cartItemList = cartService.getCartItemsByCartId(id);
        CartDetailView view = new CartDetailView(cartItemList.size(), cartItemList);
        return new ResponseEntity<>(view, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<Object> getCartByRestaurant(@PathVariable String id) {
        Optional<CartInfo> cartInfo = cartService.getCartsByUserAndRest(id);
        return new ResponseEntity<>(cartInfo, HttpStatus.OK);
    }

    @PostMapping("/restaurant/{id}/add-item")
    public ResponseEntity<Object> addCartItem(@PathVariable String id,
                                              @RequestParam String productId,
                                              @RequestParam int quantity) throws CommonException {
        validateProductAndRestaurant(productId, id);
        cartService.addCartItem(id, productId, quantity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void validateProductAndRestaurant(String productId, String restaurantId) throws CommonException {
        Optional<ProductInfo> productInfo = productService.getProductByProductIdAndRestaurantId(productId, restaurantId);
        if (!productInfo.isPresent()) {
            throw new CommonException(INVALID_PRODUCT_ID, ErrorDictionary.INVALID_PRODUCT_ID, ErrorCode.INVALID_ARGUMENTS);
        }
    }

    @PostMapping("/restaurant/{id}/update-item")
    public ResponseEntity<Object> updateCartItem(@PathVariable String id,
                                              @RequestParam String productId,
                                              @RequestParam int quantity) throws CommonException {
        validateProductAndRestaurant(productId, id);
        cartService.addCartItem(id, productId, quantity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
