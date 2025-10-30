CREATE TABLE IF NOT EXISTS telegram_user (
id BIGSERIAL PRIMARY KEY,
telegram_id BIGINT NOT NULL UNIQUE,
user_name VARCHAR(255) NOT NULL,
first_name VARCHAR(255),
last_name VARCHAR(255),
language_code VARCHAR(255) NOT NULL,
email VARCHAR(255),
created_at TIMESTAMP DEFAULT now() NOT NULL,
updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categories (
id BIGSERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
description TEXT NOT NULL,
created_at TIMESTAMP DEFAULT now() NOT NULL,
updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS task (
id BIGSERIAL PRIMARY KEY,
title VARCHAR(255) NOT NULL,
description TEXT,
region VARCHAR(100),
price_expected NUMERIC(19,2),
reward_amount NUMERIC(19,2),
reward_percentage NUMERIC(5,2),
reward_type VARCHAR(50),
status VARCHAR(50),
escrow_status VARCHAR(50),
created_at TIMESTAMP DEFAULT now() NOT NULL,
updated_at TIMESTAMP,
category_id BIGINT NOT NULL,
telegram_user BIGINT NOT NULL,
CONSTRAINT FK_task_category FOREIGN KEY (category_id) REFERENCES categories(id),
CONSTRAINT FK_task_user FOREIGN KEY (telegram_user) REFERENCES telegram_user(id)
);

CREATE TABLE IF NOT EXISTS claim (
id BIGSERIAL PRIMARY KEY,
task_id BIGINT NOT NULL,
telegram_user_id BIGINT NOT NULL,
status VARCHAR(50) NOT NULL,
message TEXT,
created_at TIMESTAMP DEFAULT now() NOT NULL,
CONSTRAINT FK_claim_task FOREIGN KEY (task_id) REFERENCES task(id),
CONSTRAINT FK_claim_user FOREIGN KEY (telegram_user_id) REFERENCES telegram_user(id)
);
