package com.food_delivery.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food_delivery.model.Category;
import com.food_delivery.model.Restaurant;
import com.food_delivery.model.VoucherInfo;
import com.food_delivery.model.form.RestaurantRequestForm;
import com.food_delivery.model.view.CategoryView;
import com.food_delivery.model.view.RestaurantView;
import com.food_delivery.model.view.VoucherInfoView;
import com.food_delivery.service.VoucherService;

import lombok.extern.log4j.Log4j2;

@Controller
@RestController
@RequestMapping(value = "/offer", produces = APPLICATION_JSON_UTF8_VALUE)
@CrossOrigin("*")
@Log4j2
@Validated
public class OfferController {
    @Autowired
    private VoucherService voucherService;

    @GetMapping("/vouchers")
    public ResponseEntity<Object> getVouchers( @RequestParam Optional<Integer> limit,
                                               @RequestParam Optional<Integer> offset){
        int os = offset.orElse(0);
        int lm = limit.orElse(30);
        List<VoucherInfo> vouchers = voucherService.getVouchers(lm, os);
        VoucherInfoView view = new VoucherInfoView(vouchers.size(), vouchers);
        return new ResponseEntity<>(view, HttpStatus.OK);
    }

    @GetMapping("/voucher/{id}/restaurants")
    public ResponseEntity<Object> getRestaurantByVoucherId(@PathVariable String id,
                                                           @RequestBody @Valid RestaurantRequestForm form) {
        int offset = form.getOffset().orElse(0);
        int limit = form.getLimit().orElse(30);
        double centerLat = form.getCenterLat();
        double centerLng = form.getCenterLng();
        List<Restaurant> restaurants = voucherService.getRestaurantsByVoucher(id, limit, offset, centerLat, centerLng);
        RestaurantView view = new RestaurantView(restaurants.size(), restaurants);
        return new ResponseEntity<>(view, HttpStatus.OK);
    }
}
