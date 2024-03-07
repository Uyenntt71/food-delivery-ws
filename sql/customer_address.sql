
CREATE TABLE food_delivery.customer_address (
                                                id uuid NOT NULL DEFAULT food_delivery.uuid_generate_v4(),
                                                address varchar NULL,
                                                lat float8 NOT NULL,
                                                lng float8 NOT NULL,
                                                created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                                customer_id uuid NOT NULL,
                                                name varchar NULL,
                                                CONSTRAINT cust_add_pkey PRIMARY KEY (id),
                                                CONSTRAINT fk_customer_id FOREIGN KEY (customer_id) REFERENCES food_delivery.customer(id)
);