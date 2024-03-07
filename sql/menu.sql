CREATE TABLE food_delivery.menu (
                                    id uuid NOT NULL DEFAULT food_delivery.uuid_generate_v4(),
                                    "name" varchar NULL,
                                    restaurant_id uuid NULL,
                                    created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                    CONSTRAINT menu_pkey PRIMARY KEY (id),
                                    CONSTRAINT fk_restaurant FOREIGN KEY (restaurant_id) REFERENCES food_delivery.restaurant(id)
);
