package com.food_delivery.service;

import java.util.List;

import com.food_delivery.model.Restaurant;
import com.food_delivery.model.VoucherInfo;

public interface VoucherService {
    List<VoucherInfo> getVouchers(int limit, int offset);

    List<Restaurant> getRestaurantsByVoucher(String voucherId, int limit, int offset, double centerLat, double centerLng);
}
