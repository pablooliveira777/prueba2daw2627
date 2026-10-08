package es.cipfpmislata.prueba2daw2627.persistence.dao.impl;

import es.cipfpmislata.prueba2daw2627.domain.model.Author;
import es.cipfpmislata.prueba2daw2627.domain.model.Book;
import es.cipfpmislata.prueba2daw2627.persistence.dao.BookDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class BookDaoJdbc implements BookDao {

    private final JdbcTemplate jdbcTemplate;

    public BookDaoJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Book> bookRowMapper = (rs, rowNum) -> {
        Author author = new Author(
                rs.getLong("author_id"),
                rs.getString("author_name"),
                rs.getString("author_nationality")
        );
        return new Book(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("isbn"),
                author
        );
    };

    @Override
    public List<Book> findAll() {
        String sql = """
                SELECT b.id, b.title, b.description, b.isbn,
                       a.id AS author_id, a.name AS author_name, a.nationality AS author_nationality
                FROM books b
                JOIN authors a ON b.author_id = a.id
                """;
        return jdbcTemplate.query(sql, bookRowMapper);
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = """
                SELECT b.id, b.title, b.description, b.isbn,
                       a.id AS author_id, a.name AS author_name, a.nationality AS author_nationality
                FROM books b
                JOIN authors a ON b.author_id = a.id
                WHERE b.id = ?
                """;
        List<Book> books = jdbcTemplate.query(sql, bookRowMapper, id);
        return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
    }

    @Override
    public Book save(Book book) {
        if (book.getAuthor() == null || book.getAuthor().getId() == null) {
            throw new IllegalArgumentException("El autor debe tener un ID válido");
        }

        String sql = "INSERT INTO books (title, description, isbn, author_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getDescription());
            ps.setString(3, book.getIsbn());
            ps.setLong(4, book.getAuthor().getId());
            return ps;
        }, keyHolder);

        book.setId(keyHolder.getKey().longValue());
        return book;
    }

    @Override
    public Book update(Book book) {
        if (book.getAuthor() == null || book.getAuthor().getId() == null) {
            throw new IllegalArgumentException("El autor debe tener un ID válido");
        }

        String sql = "UPDATE books SET title = ?, description = ?, isbn = ?, author_id = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                book.getTitle(),
                book.getDescription(),
                book.getIsbn(),
                book.getAuthor().getId(),
                book.getId()
        );
        return book;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM books WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM books";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }
}
