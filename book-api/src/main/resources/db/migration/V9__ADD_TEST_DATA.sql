INSERT INTO users (username, password, name, email, role) VALUES
    ('admin', '$2a$10$FLy6uFUo4h/CgxD6sNL64.alknbAkSd.2W2TiVgUc5/tUa.G8SM02', 'Admin', 'admin@mycompany.com', 'ADMIN'),
    ('user', '$2a$10$B9E3MFXVR5zqb/i4UTrb7e7Fr2SqPLtDHhCvUgfACtoNJUwUGmcE2', 'Users', 'user@mycompany.com', 'USER');

-- Insert test data into book_content
INSERT INTO book_contents (id, file_name, mime_type) VALUES
    (gen_random_uuid(), 'content1.pdf', 'application/pdf'),
    (gen_random_uuid(), 'content2.pdf', 'application/pdf'),
    (gen_random_uuid(), 'content3.pdf', 'application/pdf'),
    (gen_random_uuid(), 'content4.pdf', 'application/pdf');

-- Insert test data into book_cover
INSERT INTO book_covers (id, file_name, mime_type) VALUES
    ('c96d2448-f18a-4397-ba71-44e3a9768aa4', 'Any Empire.png', 'image/png'),
    ('5a73a769-dac6-4c7b-a3ea-b4cd5fbf8e14', 'August Moon.jpg', 'image/jpeg'),
    ('76347db0-0555-495a-9644-70af57d07274', 'BB Wolf and the 3 LPs.jpg', 'image/jpeg'),
    ('ae1c3124-57a0-4b51-91f8-b1df2172499f', 'The Barefoot Serpent.jpg', 'image/jpeg');

-- Insert test data into books
INSERT INTO books (title, author, price, genre, description, book_content_id, book_cover_id) VALUES
    ('Any Empire', 'author1', 12.3, 'Genre1', 'Description1',
    (SELECT id FROM book_contents LIMIT 1 OFFSET 0),
    (SELECT id FROM book_covers LIMIT 1 OFFSET 0)),
    ('August Moon', 'author2', 12.3, 'Genre2', 'Description2',
    (SELECT id FROM book_contents LIMIT 1 OFFSET 1),
    (SELECT id FROM book_covers LIMIT 1 OFFSET 1)),
    ('The Barefoot Serpent', 'author3', 12.3, 'Genre3', 'Description3',
    (SELECT id FROM book_contents LIMIT 1 OFFSET 2),
    (SELECT id FROM book_covers LIMIT 1 OFFSET 2)),
    ('BB Wolf and the 3 LPs', 'author4', 12.3, 'Genre4', 'Description4',
    (SELECT id FROM book_contents LIMIT 1 OFFSET 3),
    (SELECT id FROM book_covers LIMIT 1 OFFSET 3));

INSERT INTO users_books (user_id, book_id) VALUES
    ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM books WHERE title = 'Any Empire')),
    ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM books WHERE title = 'August Moon')),
    ((SELECT id FROM users WHERE username = 'user'), (SELECT id FROM books WHERE title = 'The Barefoot Serpent')),
    ((SELECT id FROM users WHERE username = 'user'), (SELECT id FROM books WHERE title = 'BB Wolf and the 3 LPs'));

INSERT INTO purchase_requests (user_id, book_id, status) VALUES
    ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM books WHERE title = 'Any Empire'), 'APPROVED'),
    ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM books WHERE title = 'August Moon'), 'APPROVED'),
    ((SELECT id FROM users WHERE username = 'user'), (SELECT id FROM books WHERE title = 'The Barefoot Serpent'), 'APPROVED'),
    ((SELECT id FROM users WHERE username = 'user'), (SELECT id FROM books WHERE title = 'BB Wolf and the 3 LPs'), 'APPROVED');