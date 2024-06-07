CREATE TABLE IF NOT EXISTS author_requests (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT,
    status VARCHAR(50) NOT NULL,
    request_date TIMESTAMP NOT NULL,
    decision_date TIMESTAMP,
    CONSTRAINT fk_author_requests_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_author_requests_books FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE,
    CONSTRAINT unique_user_book_author_requests UNIQUE (user_id, book_id)
);