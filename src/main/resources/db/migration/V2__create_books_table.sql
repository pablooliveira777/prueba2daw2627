CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    isbn VARCHAR(20) NOT NULL,
    author_id BIGINT NOT NULL,
    CONSTRAINT fk_books_authors FOREIGN KEY (author_id) REFERENCES authors(id)
);
