CREATE TABLE IF NOT EXISTS purchase_requests (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    request_date TIMESTAMP NOT NULL,
    decision_date TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES books (id),
    CONSTRAINT unique_user_book UNIQUE (user_id, book_id)
);