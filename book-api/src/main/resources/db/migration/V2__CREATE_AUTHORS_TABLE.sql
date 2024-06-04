CREATE TABLE IF NOT EXISTS authors (
    id SERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);