package es.cipfpmislata.prueba2daw2627.domain.service.impl;

import es.cipfpmislata.prueba2daw2627.domain.dto.AuthorDTO;
import es.cipfpmislata.prueba2daw2627.domain.dto.BookDTO;
import es.cipfpmislata.prueba2daw2627.domain.exception.NotFoundException;
import es.cipfpmislata.prueba2daw2627.domain.model.Author;
import es.cipfpmislata.prueba2daw2627.domain.model.Book;
import es.cipfpmislata.prueba2daw2627.domain.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDTO bookDTO;
    private Author author;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        author = new Author(1L, "Gabriel García Márquez", "Colombiana");
        authorDTO = new AuthorDTO(1L, "Gabriel García Márquez", "Colombiana");
        book = new Book(1L, "Cien años de soledad", "Una saga familiar", "978-0060883287", author);
        bookDTO = new BookDTO(1L, "Cien años de soledad", "Una saga familiar", "978-0060883287", authorDTO);
    }

    @Test
    void findAll_shouldReturnAllBooks() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findAll()).thenReturn(books);

        List<BookDTO> result = bookService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Cien años de soledad");
    }

    @Test
    void findById_shouldReturnBook_whenExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDTO result = bookService.findById(1L);

        assertThat(result.getTitle()).isEqualTo("Cien años de soledad");
        assertThat(result.getAuthor().getName()).isEqualTo("Gabriel García Márquez");
    }

    @Test
    void findById_shouldThrowNotFoundException_whenNotExists() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void save_shouldReturnSavedBook() {
        Book newBook = new Book(null, "El amor en los tiempos del cólera", "Novela", "978-0060883288", author);
        Book savedBook = new Book(2L, "El amor en los tiempos del cólera", "Novela", "978-0060883288", author);
        BookDTO newBookDTO = new BookDTO(null, "El amor en los tiempos del cólera", "Novela", "978-0060883288", authorDTO);
        BookDTO savedBookDTO = new BookDTO(2L, "El amor en los tiempos del cólera", "Novela", "978-0060883288", authorDTO);

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookDTO result = bookService.save(newBookDTO);

        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getTitle()).isEqualTo("El amor en los tiempos del cólera");
    }

    @Test
    void update_shouldReturnUpdatedBook() {
        BookDTO updateDTO = new BookDTO(1L, "Cien años de soledad (Edición especial)", "Una saga familiar", "978-0060883287", authorDTO);
        Book updatedBook = new Book(1L, "Cien años de soledad (Edición especial)", "Una saga familiar", "978-0060883287", author);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        BookDTO result = bookService.update(1L, updateDTO);

        assertThat(result.getTitle()).isEqualTo("Cien años de soledad (Edición especial)");
    }

    @Test
    void update_shouldThrowNotFoundException_whenNotExists() {
        BookDTO updateDTO = new BookDTO(999L, "Título", "Descripción", "ISBN", authorDTO);

        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.update(999L, updateDTO))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void deleteById_shouldDeleteBook_whenExists() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteById(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_shouldThrowNotFoundException_whenNotExists() {
        when(bookRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> bookService.deleteById(999L))
                .isInstanceOf(NotFoundException.class);
    }
}
