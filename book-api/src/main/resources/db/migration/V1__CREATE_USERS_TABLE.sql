CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL
);

INSERT INTO users (username, password, name, email, role) VALUES
    ('admin', 'admin', 'Admin', 'admin@mycompany.com', 'ROLE_ADMIN'),
    ('user', 'user', 'Users', 'user@mycompany.com', 'ROLE_USER');
