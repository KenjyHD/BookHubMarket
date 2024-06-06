CREATE TABLE IF NOT EXISTS books (
    id SERIAL PRIMARY KEY,
    author_id BIGINT,
    book_content_id UUID NOT NULL,
    book_cover_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    price FLOAT NOT NULL,
    genre VARCHAR(255),
    description TEXT,
    FOREIGN KEY (author_id) REFERENCES users(id),
    FOREIGN KEY (book_content_id) REFERENCES book_contents(id),
    FOREIGN KEY (book_cover_id) REFERENCES book_covers(id)
);
