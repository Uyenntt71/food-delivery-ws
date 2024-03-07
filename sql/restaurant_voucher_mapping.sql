CREATE TABLE food_delivery.restaurant_voucher_mapping (
	id uuid NOT NULL DEFAULT food_delivery.uuid_generate_v4(),
	restaurant_id uuid NOT NULL,
	voucher_id uuid NOT NULL,
	created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT rest_vou_pkey PRIMARY KEY (id),
	CONSTRAINT fk_restaurant FOREIGN KEY (restaurant_id) REFERENCES food_delivery.restaurant(id),
	CONSTRAINT fk_voucher FOREIGN KEY (voucher_id) REFERENCES food_delivery.voucher(id)
);
