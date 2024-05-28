CREATE TABLE IF NOT EXISTS users_books (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE
);

INSERT INTO users_books (user_id, book_id) VALUES
    ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM books WHERE title = 'Any Empire')),
    ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM books WHERE title = 'August Moon')),
    ((SELECT id FROM users WHERE username = 'user'), (SELECT id FROM books WHERE title = 'The Barefoot Serpent')),
    ((SELECT id FROM users WHERE username = 'user'), (SELECT id FROM books WHERE title = 'BB Wolf and the 3 LPs'));