CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL
);

INSERT INTO users (username, password, name, email, role) VALUES
    ('admin', '$2a$10$FLy6uFUo4h/CgxD6sNL64.alknbAkSd.2W2TiVgUc5/tUa.G8SM02', 'Admin', 'admin@mycompany.com', 'ADMIN'),
    ('user', '$2a$10$B9E3MFXVR5zqb/i4UTrb7e7Fr2SqPLtDHhCvUgfACtoNJUwUGmcE2', 'Users', 'user@mycompany.com', 'USER');
