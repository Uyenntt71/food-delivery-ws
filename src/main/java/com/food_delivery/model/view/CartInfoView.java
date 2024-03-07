package com.food_delivery.model.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.food_delivery.model.CartInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartInfoView {
    private int total;

    private List<CartInfo> cartInfoList;
}
