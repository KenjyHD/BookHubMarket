INSERT INTO users (username, password, name, email, role) VALUES
    ('admin', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'Admin', 'admin@gmail.com', 'ADMIN'),
    ('author', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'Author', 'author@gmail.com', 'AUTHOR'),
    ('user', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'User', 'user@gmail.com', 'USER');

-- Insert test data into book_content
INSERT INTO book_contents (id, file_name, mime_type) VALUES
    ('00000000-0000-0000-0000-000000000001', 'content1.pdf', 'application/pdf'),
    ('00000000-0000-0000-0000-000000000002', 'content2.pdf', 'application/pdf'),
    ('00000000-0000-0000-0000-000000000003', 'content3.pdf', 'application/pdf'),
    ('00000000-0000-0000-0000-000000000004', 'content4.pdf', 'application/pdf');

-- Insert test data into book_cover
INSERT INTO book_covers (id, file_name, mime_type) VALUES
    ('00000000-0000-0000-0000-000000000001', 'Any Empire.png', 'image/png'),
    ('00000000-0000-0000-0000-000000000002', 'August Moon.jpg', 'image/jpeg'),
    ('00000000-0000-0000-0000-000000000003', 'BB Wolf and the 3 LPs.jpg', 'image/jpeg'),
    ('00000000-0000-0000-0000-000000000004', 'The Barefoot Serpent.jpg', 'image/jpeg');

-- Insert test data into books
INSERT INTO books (title, author, price, genre, description, author_id, book_content_id, book_cover_id) VALUES
    ('Any Empire', 'author1', 12.3, 'Genre1', 'Description1',
    (SELECT id FROM users WHERE username = 'admin'),
    (SELECT id FROM book_contents LIMIT 1 OFFSET 0),
    (SELECT id FROM book_covers LIMIT 1 OFFSET 0)),
    ('August Moon', 'author2', 12.3, 'Genre2', 'Description2',
    (SELECT id FROM users WHERE username = 'admin'),
    (SELECT id FROM book_contents LIMIT 1 OFFSET 1),
    (SELECT id FROM book_covers LIMIT 1 OFFSET 1)),
    ('The Barefoot Serpent', 'author3', 12.3, 'Genre3', 'Description3',
    (SELECT id FROM users WHERE username = 'author'),
    (SELECT id FROM book_contents LIMIT 1 OFFSET 2),
    (SELECT id FROM book_covers LIMIT 1 OFFSET 2)),
    ('BB Wolf and the 3 LPs', 'author4', 12.3, 'Genre4', 'Description4',
    (SELECT id FROM users WHERE username = 'author'),
    (SELECT id FROM book_contents LIMIT 1 OFFSET 3),
    (SELECT id FROM book_covers LIMIT 1 OFFSET 3));

INSERT INTO users_books (user_id, book_id) VALUES
    ((SELECT id FROM users WHERE username = 'author'), (SELECT id FROM books WHERE title = 'Any Empire')),
    ((SELECT id FROM users WHERE username = 'author'), (SELECT id FROM books WHERE title = 'August Moon')),
    ((SELECT id FROM users WHERE username = 'user'), (SELECT id FROM books WHERE title = 'August Moon')),
    ((SELECT id FROM users WHERE username = 'user'), (SELECT id FROM books WHERE title = 'The Barefoot Serpent'));

INSERT INTO purchase_requests (user_id, book_id, status, request_date) VALUES
    ((SELECT id FROM users WHERE username = 'author'), (SELECT id FROM books WHERE title = 'Any Empire'), 'APPROVED', CURRENT_TIMESTAMP),
    ((SELECT id FROM users WHERE username = 'author'), (SELECT id FROM books WHERE title = 'August Moon'), 'APPROVED', CURRENT_TIMESTAMP),
    ((SELECT id FROM users WHERE username = 'user'), (SELECT id FROM books WHERE title = 'August Moon'), 'APPROVED', CURRENT_TIMESTAMP),
    ((SELECT id FROM users WHERE username = 'user'), (SELECT id FROM books WHERE title = 'The Barefoot Serpent'), 'APPROVED', CURRENT_TIMESTAMP);