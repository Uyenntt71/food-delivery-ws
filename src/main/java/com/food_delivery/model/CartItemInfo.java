package com.food_delivery.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CartItemInfo {
    private String id;

    private String productId;

    private String name;

    private long price;

    private int quantity;

}
