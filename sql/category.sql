CREATE TABLE food_delivery.category (
                                        id uuid NOT NULL DEFAULT food_delivery.uuid_generate_v4(),
                                        category_name varchar NOT NULL,
                                        created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                        CONSTRAINT category_pkey PRIMARY KEY (id)
);

ALTER TABLE food_delivery.category ADD CONSTRAINT category_unq unique (category_name);

INSERT INTO food_delivery.category (id,category_name,created_at) VALUES
                                                                     ('dec6eb5e-4be6-4e63-9312-5faf82cccd37'::uuid,'Noodle','2022-12-23 14:42:21.623975'),
                                                                     ('0c4b9967-5cb4-4d07-822d-0e4a1a017843'::uuid,'Drinks','2022-12-23 14:42:21.623975'),
                                                                     ('3ab92b31-be68-4a70-a33f-bae294ba7afc'::uuid,'Cakes','2022-12-23 14:42:21.623975'),
                                                                     ('4ea4d994-1446-46f1-b335-559a000749c5'::uuid,'Special','2022-12-23 14:42:21.623975'),
                                                                     ('c8c89ad6-5083-4456-b4d6-44162f8cc716'::uuid,'Fastfood','2022-12-23 14:42:21.623975'),
                                                                     ('213b221d-ca53-464c-9696-ed9d12f0fddb'::uuid,'Snacks','2022-12-23 14:42:21.623975'),
                                                                     ('3849bd01-64a8-4f24-ab39-9d719758beb5'::uuid,'Healthy','2022-12-23 14:42:21.623975'),
                                                                     ('5af56034-ed8a-4398-a963-d2b6e99e98f6'::uuid,'Rice','2022-12-23 14:42:21.623975');