package com.food_delivery.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food_delivery.dao.VoucherDao;
import com.food_delivery.model.Restaurant;
import com.food_delivery.model.VoucherInfo;

@Service
public class VoucherServiceImpl implements VoucherService{
    @Autowired
    VoucherDao voucherDao;

    @Override
    public List<VoucherInfo> getVouchers(int limit, int offset) {
        return voucherDao.getVouchers(limit, offset);
    }

    @Override
    public List<Restaurant> getRestaurantsByVoucher(String voucherId, int limit, int offset, double centerLat, double centerLng) {
        return voucherDao.getRestaurantsByVoucher(voucherId, limit, offset, centerLat, centerLng);
    }
}
