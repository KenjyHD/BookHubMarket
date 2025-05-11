INSERT INTO users (username, password, name, email, role)
VALUES ('tolstoy', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'Leo Tolstoy', 'leo@example.com',
        'AUTHOR'),
       ('rowling', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'J.K. Rowling', 'jk@example.com',
        'AUTHOR'),
       ('orwell', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'George Orwell', 'george@example.com',
        'AUTHOR'),
       ('atwood', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'Margaret Atwood',
        'margaret@example.com', 'AUTHOR'),
       ('king', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'Stephen King', 'stephen@example.com',
        'AUTHOR');

INSERT INTO users (username, password, name, email, role)
VALUES ('alice123', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'Alice Johnson',
        'alice@example.com', 'USER'),
       ('bob_reader', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'Bob Smith',
        'bob@example.com', 'USER'),
       ('clara_books', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'Clara Davis',
        'clara@example.com', 'USER'),
       ('dave2023', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'Dave Wilson',
        'dave@example.com', 'USER'),
       ('emma_reads', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'Emma Brown',
        'emma@example.com', 'USER'),
       ('frank_k', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'Frank Klein',
        'frank@example.com', 'USER'),
       ('grace_m', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'Grace Miller',
        'grace@example.com', 'USER'),
       ('henry_t', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'Henry Taylor',
        'henry@example.com', 'USER'),
       ('ivy_booklover', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'Ivy Green',
        'ivy@example.com', 'USER'),
       ('jack_j', '$2a$10$BVMd1l4hIE3v35RzXLZOfOGrlUXcBvQK9kOIfxNjlxwfiYImxBR2G', 'Jack Jones', 'jack@example.com',
        'USER');

INSERT INTO book_contents (id, file_name, mime_type)
VALUES ('00000000-0000-0000-0000-000000000005', 'content1.pdf', 'application/pdf');

-- Insert test data into book_cover
INSERT INTO book_covers (id, file_name, mime_type)
VALUES ('00000000-0000-0000-0000-000000000005', 'Any Empire.png', 'image/png');


INSERT INTO books (title, author, price, genre, author_id, book_content_id, book_cover_id)
VALUES ('War and Peace', 'Leo Tolstoy', 24.99, 'Historical Fiction', 4, '00000000-0000-0000-0000-000000000005',
        '00000000-0000-0000-0000-000000000005'),
       ('Anna Karenina', 'Leo Tolstoy', 19.99, 'Literary Fiction', 4, '00000000-0000-0000-0000-000000000005',
        '00000000-0000-0000-0000-000000000005'),
       ('Resurrection', 'Leo Tolstoy', 17.99, 'Literary Fiction', 4, '00000000-0000-0000-0000-000000000005',
        '00000000-0000-0000-0000-000000000005'),
       ('The Death of Ivan Ilyich', 'Leo Tolstoy', 14.99, 'Philosophical Fiction', 4,
        '00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000005');

INSERT INTO books (title, author, price, genre, author_id, book_content_id, book_cover_id)
VALUES ('Harry Potter and the Philosopher''s Stone', 'J.K. Rowling', 29.99, 'Fantasy', 5,
        '00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000005'),
       ('Harry Potter and the Chamber of Secrets', 'J.K. Rowling', 29.99, 'Fantasy', 5,
        '00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000005'),
       ('Harry Potter and the Prisoner of Azkaban', 'J.K. Rowling', 29.99, 'Fantasy', 5,
        '00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000005'),
       ('Harry Potter and the Goblet of Fire', 'J.K. Rowling', 29.99, 'Fantasy', 5,
        '00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000005');

INSERT INTO books (title, author, price, genre, author_id, book_content_id, book_cover_id)
VALUES ('1984', 'George Orwell', 14.99, 'Dystopian', 6, '00000000-0000-0000-0000-000000000005',
        '00000000-0000-0000-0000-000000000005'),
       ('Animal Farm', 'George Orwell', 12.99, 'Political Satire', 6, '00000000-0000-0000-0000-000000000005',
        '00000000-0000-0000-0000-000000000005'),
       ('Homage to Catalonia', 'George Orwell', 16.99, 'Non-Fiction', 6, '00000000-0000-0000-0000-000000000005',
        '00000000-0000-0000-0000-000000000005'),
       ('Down and Out in Paris and London', 'George Orwell', 13.99, 'Memoir', 6, '00000000-0000-0000-0000-000000000005',
        '00000000-0000-0000-0000-000000000005');

INSERT INTO books (title, author, price, genre, author_id, book_content_id, book_cover_id)
VALUES ('The Handmaid''s Tale', 'Margaret Atwood', 17.99, 'Dystopian', 7, '00000000-0000-0000-0000-000000000005',
        '00000000-0000-0000-0000-000000000005'),
       ('Oryx and Crake', 'Margaret Atwood', 16.99, 'Science Fiction', 7, '00000000-0000-0000-0000-000000000005',
        '00000000-0000-0000-0000-000000000005'),
       ('The Blind Assassin', 'Margaret Atwood', 18.99, 'Literary Fiction', 7, '00000000-0000-0000-0000-000000000005',
        '00000000-0000-0000-0000-000000000005'),
       ('Alias Grace', 'Margaret Atwood', 15.99, 'Historical Fiction', 7, '00000000-0000-0000-0000-000000000005',
        '00000000-0000-0000-0000-000000000005');

INSERT INTO books (title, author, price, genre, author_id, book_content_id, book_cover_id)
VALUES ('The Shining', 'Stephen King', 15.99, 'Horror', 8, '00000000-0000-0000-0000-000000000005',
        '00000000-0000-0000-0000-000000000005'),
       ('IT', 'Stephen King', 18.99, 'Horror', 8, '00000000-0000-0000-0000-000000000005',
        '00000000-0000-0000-0000-000000000005'),
       ('Carrie', 'Stephen King', 12.99, 'Horror', 8, '00000000-0000-0000-0000-000000000005',
        '00000000-0000-0000-0000-000000000005'),
       ('Misery', 'Stephen King', 14.99, 'Thriller', 8, '00000000-0000-0000-0000-000000000005',
        '00000000-0000-0000-0000-000000000005');

INSERT INTO purchase_requests (user_id, book_id, status, request_date, decision_date)
VALUES
(6, 1, 'APPROVED', '2023-01-05 10:00:00', '2023-01-06 11:00:00'),
(7, 5, 'APPROVED', '2023-01-12 14:30:00', '2023-01-13 09:15:00'),
(8, 9, 'APPROVED', '2023-01-18 16:45:00', '2023-01-19 10:00:00'),

(9, 2, 'APPROVED', '2023-02-03 11:20:00', '2023-02-04 12:00:00'),
(10, 6, 'APPROVED', '2023-02-14 09:45:00', '2023-02-15 08:30:00'),
(11, 13, 'APPROVED', '2023-02-21 15:10:00', '2023-02-22 14:00:00'),

(12, 17, 'APPROVED', '2023-03-05 13:25:00', '2023-03-06 10:45:00'),
(6, 3, 'APPROVED', '2023-03-12 10:00:00', '2023-03-13 11:00:00'),
(7, 7, 'APPROVED', '2023-03-19 14:15:00', '2023-03-20 09:00:00'),

(13, 10, 'APPROVED', '2023-04-02 16:30:00', '2023-04-03 12:00:00'),
(8, 15, 'APPROVED', '2023-04-15 11:45:00', '2023-04-16 10:30:00'),
(9, 18, 'APPROVED', '2023-04-22 09:20:00', '2023-04-23 08:15:00'),

(14, 4, 'APPROVED', '2023-05-07 14:00:00', '2023-05-08 13:45:00'),
(10, 8, 'APPROVED', '2023-05-14 10:30:00', '2023-05-15 09:15:00'),
(11, 15, 'APPROVED', '2023-05-21 16:20:00', '2023-05-22 14:10:00'),

(12, 19, 'APPROVED', '2023-06-10 09:45:00', '2023-06-11 08:30:00'),
(6, 5, 'APPROVED', '2023-06-18 13:15:00', '2023-06-19 11:00:00'),
(7, 11, 'APPROVED', '2023-06-25 15:30:00', '2023-06-26 14:00:00'),

(13, 16, 'APPROVED', '2023-07-03 11:20:00', '2023-07-04 10:15:00'),
(8, 20, 'APPROVED', '2023-07-15 14:45:00', '2023-07-16 13:30:00'),
(9, 1, 'APPROVED', '2023-07-22 10:10:00', '2023-07-23 09:00:00'),

(14, 6, 'APPROVED', '2023-08-08 16:25:00', '2023-08-09 15:15:00'),
(10, 12, 'APPROVED', '2023-08-17 09:35:00', '2023-08-18 08:20:00'),
(11, 17, 'APPROVED', '2023-08-24 14:50:00', '2023-08-25 13:45:00'),

(12, 3, 'APPROVED', '2023-09-05 11:15:00', '2023-09-06 10:00:00'),
(6, 9, 'APPROVED', '2023-09-14 13:40:00', '2023-09-15 12:30:00'),
(7, 14, 'APPROVED', '2023-09-21 15:05:00', '2023-09-22 14:00:00'),

(13, 18, 'APPROVED', '2023-10-10 10:50:00', '2023-10-11 09:45:00'),
(8, 2, 'APPROVED', '2023-10-19 14:15:00', '2023-10-20 13:10:00'),
(9, 7, 'APPROVED', '2023-10-27 16:30:00', '2023-10-28 15:25:00'),

(14, 13, 'APPROVED', '2023-11-07 09:25:00', '2023-11-08 08:20:00'),
(10, 19, 'APPROVED', '2023-11-15 12:45:00', '2023-11-16 11:40:00');

INSERT INTO purchase_requests (user_id, book_id, status, request_date, decision_date)
VALUES (15, 5, 'REJECTED', '2023-02-28 14:00:00', '2023-03-01 12:00:00'), -- Expensive book
       (6, 20, 'REJECTED', '2023-04-10 11:30:00', '2023-04-11 10:00:00'), -- User payment failed
       (11, 8, 'REJECTED', '2023-05-19 16:45:00', '2023-05-20 15:30:00'), -- Out of stock
       (7, 15, 'REJECTED', '2023-06-22 10:15:00', '2023-06-23 09:00:00'), -- Age restriction
       (12, 4, 'REJECTED', '2023-07-11 13:20:00', '2023-07-12 12:15:00'),
       (9, 11, 'REJECTED', '2023-08-25 15:35:00', '2023-08-26 14:30:00'),
       (14, 17, 'REJECTED', '2023-09-18 09:50:00', '2023-09-19 08:45:00'),
       (8, 1, 'REJECTED', '2023-10-05 14:10:00', '2023-10-06 13:05:00'),
       (10, 7, 'REJECTED', '2023-11-12 16:25:00', '2023-11-13 15:20:00'),
       (13, 12, 'REJECTED', '2023-12-01 10:40:00', '2023-12-02 09:35:00');

INSERT INTO purchase_requests (user_id, book_id, status, request_date)
VALUES (15, 3, 'PENDING', '2023-12-05 09:15:00'),
       (6, 10, 'PENDING', '2023-12-06 11:30:00'),
       (7, 16, 'PENDING', '2023-12-07 14:45:00'),
       (12, 5, 'PENDING', '2023-12-08 10:00:00'),
       (9, 19, 'PENDING', '2023-12-09 13:20:00'),
       (11, 2, 'PENDING', '2023-12-10 15:35:00'),
       (14, 7, 'PENDING', '2023-12-11 16:50:00'),
       (8, 14, 'PENDING', '2023-12-12 09:05:00'),
       (10, 18, 'PENDING', '2023-12-13 12:15:00'),
       (13, 9, 'PENDING', '2023-12-14 14:30:00');