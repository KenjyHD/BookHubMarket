CREATE TABLE IF NOT EXISTS book_contents (
    id UUID PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    mime_type VARCHAR(50) NOT NULL
);