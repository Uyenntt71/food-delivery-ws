package com.food_delivery.model;

import lombok.Data;

@Data
public class CartInfo {
    private String id;

    private String name;

    private Integer numItems;

    private Double distance;

    private Long totalValue;
}
