package com.food_delivery.model.view;

import java.util.List;

import com.food_delivery.model.AddressInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressInfoView {
    private int total;

    private List<AddressInfo> addressInfoList;
}
