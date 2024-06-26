CREATE TABLE IF NOT EXISTS users_books (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    CONSTRAINT fk_users_books_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_users_books_books FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE
);