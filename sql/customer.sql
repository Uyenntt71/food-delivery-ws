
CREATE TABLE food_delivery.customer (
                                        id uuid NOT NULL DEFAULT food_delivery.uuid_generate_v4(),
                                        email varchar NOT NULL,
                                        phone_number varchar NOT NULL,
                                        name varchar NULL,
                                        "password" varchar NOT NULL,
                                        address varchar NULL,
                                        photo_bin bytea NULL,
                                        created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                        CONSTRAINT customer_pkey PRIMARY KEY (id)
);
CREATE UNIQUE INDEX customer_email_uindex ON food_delivery.customer USING btree (email);
CREATE UNIQUE INDEX customer_phone_uindex ON food_delivery.customer USING btree (phone_number);

INSERT INTO food_delivery.customer (id,email,phone_number,"name","password",address,photo_bin,created_at) VALUES
('9c255e37-50c8-4f0a-95f0-1f67b751b20d'::uuid,'nguyenuyendhcn@gmail.comm','0335606978','uyen','$2a$10$xZf57xzqInBWxd.ITEbBruBQ4MFk5beizYYBFyQUp3YhWCjBspqai',NULL,NULL,'2022-12-20 15:41:16.244769'),
('1b2a96e8-f58a-4fb3-9e69-5d184f6c1548'::uuid,'nguyenuyendhcn@gmail.com','0335606977','Nguyen Uyen','$2a$10$.k.RKNRLERsjoT.kokc9cepkkHojKSYtPsc5abl1YgHnzmA/jCc1.','Co Nhue, Bac Tu Liem, Ha Noi',NULL,'2022-11-21 16:44:04.947976');