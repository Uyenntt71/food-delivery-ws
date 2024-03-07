package com.food_delivery.model.view;

import java.util.List;

import com.food_delivery.model.VoucherInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoucherInfoView {
    private int total;

    private List<VoucherInfo> voucherInfoList;
}
