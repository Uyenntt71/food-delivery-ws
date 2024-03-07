package com.food_delivery.model.form;

import java.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class RestaurantRequestForm {
    @NotNull
    private Optional<Integer> limit;

    @NotNull
    private Optional<Integer> offset;

    @NotNull
    @Min(value = -90)
    @Max(value = 90)
    private Double centerLat;

    @NotNull
    @Min(value = -180)
    @Max(value = 180)
    private Double centerLng;
}
