CREATE TABLE food_delivery.restaurant_category_mapping (
                                                           id uuid NOT NULL DEFAULT food_delivery.uuid_generate_v4(),
                                                           restaurant_id uuid NULL,
                                                           category_id uuid NULL,
                                                           created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                                           CONSTRAINT rest_cat_pkey PRIMARY KEY (id),
                                                           CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES food_delivery.category(id),
                                                           CONSTRAINT pk_restaurant FOREIGN KEY (restaurant_id) REFERENCES food_delivery.restaurant(id)
);