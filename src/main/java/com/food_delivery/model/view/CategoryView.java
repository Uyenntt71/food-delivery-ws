package com.food_delivery.model.view;

import java.util.List;

import com.food_delivery.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryView {
    private int total;
    List<Category> categories;
}
