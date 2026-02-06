-- Add columns to users
ALTER TABLE users
ADD COLUMN password VARCHAR(60),
ADD COLUMN date_created TIMESTAMP DEFAULT now();

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    date_created TIMESTAMP DEFAULT now()
);

CREATE TABLE user_roles (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY(role_id) REFERENCES roles(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT uq_user_role UNIQUE(user_id, role_id)
);
