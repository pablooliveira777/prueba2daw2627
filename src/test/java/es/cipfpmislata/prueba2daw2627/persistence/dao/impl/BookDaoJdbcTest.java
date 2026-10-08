package es.cipfpmislata.prueba2daw2627.persistence.dao.impl;

import es.cipfpmislata.prueba2daw2627.domain.model.Author;
import es.cipfpmislata.prueba2daw2627.domain.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class BookDaoJdbcTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BookDaoJdbc bookDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM books");
        jdbcTemplate.execute("DELETE FROM authors");
        jdbcTemplate.execute("ALTER TABLE authors ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE books ALTER COLUMN id RESTART WITH 1");
    }

    private Long insertAuthor(String name, String nationality) {
        jdbcTemplate.update("INSERT INTO authors (name, nationality) VALUES (?, ?)", name, nationality);
        return jdbcTemplate.queryForObject("SELECT id FROM authors WHERE name = ?", Long.class, name);
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoBooks() {
        List<Book> books = bookDao.findAll();
        assertThat(books).isEmpty();
    }

    @Test
    void save_shouldPersistBookWithGeneratedId() {
        Long authorId = insertAuthor("Gabriel García Márquez", "Colombiana");

        Book book = new Book(null, "Cien años de soledad", "Una saga familiar", "978-0060883287", new Author(authorId, "Gabriel García Márquez", "Colombiana"));

        Book saved = bookDao.save(book);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Cien años de soledad");
    }

    @Test
    void findById_shouldReturnBook_whenExists() {
        Long authorId = insertAuthor("Isabel Allende", "Chilena");
        jdbcTemplate.update("INSERT INTO books (title, description, isbn, author_id) VALUES (?, ?, ?, ?)",
                "La casa de los espíritus", "Novela familiar", "978-0061120061", authorId);

        Long bookId = jdbcTemplate.queryForObject("SELECT id FROM books WHERE isbn = ?", Long.class, "978-0061120061");

        Optional<Book> result = bookDao.findById(bookId);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("La casa de los espíritus");
        assertThat(result.get().getAuthor().getName()).isEqualTo("Isabel Allende");
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        Optional<Book> result = bookDao.findById(999L);
        assertThat(result).isEmpty();
    }

    @Test
    void deleteById_shouldRemoveBook() {
        Long authorId = insertAuthor("Jorge Luis Borges", "Argentina");
        jdbcTemplate.update("INSERT INTO books (title, description, isbn, author_id) VALUES (?, ?, ?, ?)",
                "El Aleph", "Cuentos", "978-0060883287", authorId);
        Long bookId = jdbcTemplate.queryForObject("SELECT id FROM books WHERE isbn = ?", Long.class, "978-0060883287");

        bookDao.deleteById(bookId);

        Optional<Book> result = bookDao.findById(bookId);
        assertThat(result).isEmpty();
    }

    @Test
    void existsById_shouldReturnTrue_whenBookExists() {
        Long authorId = insertAuthor("Pablo Neruda", "Chileno");
        jdbcTemplate.update("INSERT INTO books (title, description, isbn, author_id) VALUES (?, ?, ?, ?)",
                "Veinte poemas de amor", "Poesía", "978-0061120061", authorId);
        Long bookId = jdbcTemplate.queryForObject("SELECT id FROM books WHERE isbn = ?", Long.class, "978-0061120061");

        assertThat(bookDao.existsById(bookId)).isTrue();
    }

    @Test
    void count_shouldReturnCorrectNumberOfBooks() {
        Long authorId = insertAuthor("Miguel de Cervantes", "Española");
        jdbcTemplate.update("INSERT INTO books (title, description, isbn, author_id) VALUES (?, ?, ?, ?)",
                "Don Quijote", "Novela caballeresca", "978-0060883287", authorId);
        jdbcTemplate.update("INSERT INTO books (title, description, isbn, author_id) VALUES (?, ?, ?, ?)",
                "Novelas ejemplares", "Cuentos", "978-0060883288", authorId);

        assertThat(bookDao.count()).isEqualTo(2);
    }
}
