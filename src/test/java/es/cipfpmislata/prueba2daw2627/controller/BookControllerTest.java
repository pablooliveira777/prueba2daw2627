package es.cipfpmislata.prueba2daw2627.controller;

import es.cipfpmislata.prueba2daw2627.domain.dto.AuthorDTO;
import es.cipfpmislata.prueba2daw2627.domain.dto.BookDTO;
import es.cipfpmislata.prueba2daw2627.domain.exception.NotFoundException;
import es.cipfpmislata.prueba2daw2627.domain.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Test
    void findAll_shouldReturnListOfBooks() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO(1L, "Gabriel García Márquez", "Colombiana");
        BookDTO book1 = new BookDTO(1L, "Cien años de soledad", "Una saga familiar", "978-0060883287", authorDTO);
        BookDTO book2 = new BookDTO(2L, "El amor en los tiempos del cólera", "Novela", "978-0060883288", authorDTO);
        List<BookDTO> books = Arrays.asList(book1, book2);

        when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Cien años de soledad")))
                .andExpect(jsonPath("$[1].title", is("El amor en los tiempos del cólera")));
    }

    @Test
    void findById_shouldReturnBook_whenExists() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO(1L, "Gabriel García Márquez", "Colombiana");
        BookDTO book = new BookDTO(1L, "Cien años de soledad", "Una saga familiar", "978-0060883287", authorDTO);

        when(bookService.findById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Cien años de soledad")))
                .andExpect(jsonPath("$.author.name", is("Gabriel García Márquez")));
    }

    @Test
    void findById_shouldReturn404_whenNotExists() throws Exception {
        when(bookService.findById(999L)).thenThrow(new NotFoundException("Book", 999L));

        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturn201AndCreatedBook() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO(1L, "Gabriel García Márquez", "Colombiana");
        BookDTO input = new BookDTO(null, "Cien años de soledad", "Una saga familiar", "978-0060883287", authorDTO);
        BookDTO created = new BookDTO(1L, "Cien años de soledad", "Una saga familiar", "978-0060883287", authorDTO);

        when(bookService.save(any(BookDTO.class))).thenReturn(created);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Cien años de soledad")));
    }

    @Test
    void create_shouldReturn400_whenInvalidInput() throws Exception {
        BookDTO invalidBook = new BookDTO(null, "", "", "", null);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBook)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_shouldReturn200AndUpdatedBook() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO(1L, "Gabriel García Márquez", "Colombiana");
        BookDTO input = new BookDTO(1L, "Cien años de soledad (Edición especial)", "Una saga familiar", "978-0060883287", authorDTO);
        BookDTO updated = new BookDTO(1L, "Cien años de soledad (Edición especial)", "Una saga familiar", "978-0060883287", authorDTO);

        when(bookService.update(eq(1L), any(BookDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Cien años de soledad (Edición especial)")));
    }

    @Test
    void delete_shouldReturn204_whenBookDeleted() throws Exception {
        doNothing().when(bookService).deleteById(1L);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteById(1L);
    }

    @Test
    void delete_shouldReturn404_whenBookNotExists() throws Exception {
        doThrow(new NotFoundException("Book", 999L)).when(bookService).deleteById(999L);

        mockMvc.perform(delete("/api/books/999"))
                .andExpect(status().isNotFound());
    }
}
