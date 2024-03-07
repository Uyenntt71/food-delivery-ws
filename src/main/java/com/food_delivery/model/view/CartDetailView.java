package com.food_delivery.model.view;

import java.util.List;

import com.food_delivery.model.CartItemInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartDetailView {
    private int totalItems;

    private List<CartItemInfo> itemInfoList;
}
