CREATE TABLE food_delivery.restaurant (
                                          id uuid NOT NULL DEFAULT food_delivery.uuid_generate_v4(),
                                          "name" varchar NULL,
                                          phone_number varchar NOT NULL,
                                          address varchar NULL,
                                          lat float8 NOT NULL,
                                          lng float8 NOT NULL,
                                          logo_bin bytea NULL,
                                          background_bin bytea NULL,
                                          status varchar NULL,
                                          created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                          CONSTRAINT restaurant_pkey PRIMARY KEY (id)
);
