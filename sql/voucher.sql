CREATE TABLE food_delivery.voucher (
                                       id uuid NOT NULL DEFAULT food_delivery.uuid_generate_v4(),
                                       code varchar NOT NULL,
                                       "name" varchar NULL,
                                       description varchar NULL,
                                       discount numeric(12) NOT NULL,
                                       expired_time int8 NULL,
                                       min_spend numeric(12) NULL,
                                       created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                       CONSTRAINT voucher_pkey PRIMARY KEY (id)
);